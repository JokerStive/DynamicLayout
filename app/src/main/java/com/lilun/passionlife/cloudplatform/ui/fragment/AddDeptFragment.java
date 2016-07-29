package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.ExtRoleAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.IsInherited;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.bean.Principal;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.PicloadManager;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.ExtendItem;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.RequestBody;

/**
 * Created by youke on 2016/6/22.
 * 新增组织
 */
public class AddDeptFragment extends BaseFunctionFragment implements ExtendItem.onClickListener {


    @Bind(R.id.head)
    TextView head;

    @Bind(R.id.iv_head)
    CircleImageView ivHead;


    @Bind(R.id.input_orgi_name)
    RegItemView inputOrgiName;

    @Bind(R.id.exv_add_orgi)
    ExtendItem exvAddOrgi;

    @Bind(R.id.save)
    Button save;
    @Bind(R.id.input_orgi_desc)
    RegItemView inputOrgiDesc;

    private List<Role> roles;
    private List<Map<Role, List<Principal>>> rolePrinMapping = new ArrayList<>();
    private String orgaName;
    private Organization orgna;
    private String orgaDesc;
    int dex = 0;
    private int size;
    private Role newRole;
    private View view;
    private Bitmap icon;

    @Override
    public View setView() {

        view = inflater.inflate(R.layout.fragment_add_dept, null);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        roles = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        rootActivity.setTitle(App.app.getString(R.string.add_department));
        exvAddOrgi.setOnClickListen(this);
    }


    @OnClick(R.id.iv_head)
    void headPic() {
        Logger.d("选择头像");
        rootActivity.choiseHeadPic(view);
    }

    @Subscribe
    public void choisHead(Event.choiseHeadPic event) {
        Bitmap bitmap = event.getBitmap();
        if (bitmap != null) {
            icon = bitmap;
            ivHead.setImageBitmap(bitmap);

        }
    }


    /**
     * 保存
     */
    @OnClick(R.id.save)
    void save() {
        orgaName = inputOrgiName.getInput();
        orgaDesc = inputOrgiDesc.getInput();
        if (TextUtils.isEmpty(orgaName) || TextUtils.isEmpty(orgaDesc)) {
            ToastHelper.get(mCx).showShort(mCx.getString(R.string.not_orginame_empty));
            return;
        }
        saveOrgi();

    }


    /**
     * 如果新增了自己的部门，就轮询提交服务器
     *
     * @param id
     */
    private void saveRole(String id) {
        size = roles.size();
        List<Role> roleList = new ArrayList<>();
        for (int i = 0; i < roles.size(); i++) {
            Role role = roles.get(i);
            role.setOrganizationId(id + Constants.special_orgi_role);
            role.setName(id + ":" + role.getTitle());
            roleList.add(role);

        }

        if (roleList.size()==0){
//            rootActivity.backStack();
            return;
        }
        rootActivity.addSubscription(ApiFactory.postRoles(roleList), new PgSubscriber<List<Role>>(rootActivity) {
            @Override
            public void on_Next(List<Role> roles) {
                ToastHelper.get(mCx).showShort("新增职位成功");
                Logger.d("新增职位成功");
                for (int i = 0; i < roles.size(); i++) {
                    Role role = roles.get(i);
                    Map<Role, List<Principal>> roleListMap = rolePrinMapping.get(i);
                    List<Principal> principals = roleListMap.get(role);
                    postPrincipal((double) role.getId(), principals);
                }
            }
        });
    }

    private void postPrincipal(double roleId, List<Principal> p) {
        rootActivity.addSubscription(ApiFactory.postPrincipal(roleId, p), new PgSubscriber<Object>(rootActivity) {
            @Override
            public void on_Next(Object o) {
//                rootActivity.backStack();
                Logger.d("新增职位的权限成功");
            }
        });

    }


    /**
     * 存 新增的部门
     */
    private void saveOrgi() {
        orgna = new Organization();
        orgna.setId(orgiId + Constants.special_orgi_department + "/" + orgaName);
        orgna.setName(orgaName);
        orgna.setInherited(exvAddOrgi.isInherited());
        orgna.setParentId(orgiId + Constants.special_orgi_department);
        Logger.d(" dept parent id = " + orgna.getParentId());
        orgna.setDescription(orgaDesc);
        //TODO 设置icon
        rootActivity.addSubscription(ApiFactory.postOrganization(orgna), new PgSubscriber<Organization>(rootActivity) {
            @Override
            public void on_Next(Organization orga) {
                saveIcon(orga.getId());
                if (!orga.isInherited()) {
                    String id = orga.getId() + Constants.special_orgi_role;
                    rootActivity.setIsInherited(id, new IsInherited(orga.isInherited()));
                    saveRole(orga.getId());
                } rootActivity.backStack();


            }
        });
    }


    /**
     * 显示继承自父的东西
     */
    @Override
    public void onBtnChoise(boolean enabled) {
        if (enabled) {
            getExtendsRole(orgiId);
        } else if (roles != null) {
            //
            Logger.d("展示自己的部门");
            exvAddOrgi.setListviewData(new ExtRoleAdapter(roles, true, (parentOrgisAdapter, position) -> {
                deleteItem(parentOrgisAdapter, position);
            }));
        }

    }

    /**
     * 从内存中删除一个dep  并刷新视图
     */
    private void deleteItem(ExtRoleAdapter parentOrgisAdapter, int position) {
        roles.remove(position);
        rolePrinMapping.remove(position);
        parentOrgisAdapter.notifyDataSetChanged();
    }


    /**
     * 获取继承自父组织的ro
     */
    private void getExtendsRole(String organiId) {
        rootActivity.getOrgRoleList(organiId, roles -> {
            if (roles.size() == 0) {
                ToastHelper.get(mCx).showShort(mCx.getString(R.string.empty_role_list));
            }
            exvAddOrgi.setListviewData(new ExtRoleAdapter(roles, false, (parentOrgisAdapter, position) -> {

            }));

        });

    }

    @Override
    /**
     *新增what
     */
    public void onAddWhatClick() {
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new AddDeptRoleFragment(), mCx.getString(R.string.add_duty));
        EventBus.getDefault().post(event);

    }


    /**
     * 新增了一role
     */
    @Subscribe
    public void addDepartment(Event.addNewRole event) {
//        Logger.d("   get  a new depart");
        newRole = event.getRole();
        List<Principal> principals = event.getPrincipals();

        if (newRole != null && roles != null) {
            roles.add(newRole);
        }
        //role和prin的映射
        if (principals != null) {
            Map<Role, List<Principal>> mapping = new HashMap<>();
            mapping.put(newRole, principals);
            rolePrinMapping.add(mapping);
        }
    }


    /**
     * 新增组织Icon
     */
    private void saveIcon(String orgaId) {
        if (icon != null) {
            RequestBody requestBody = PicloadManager.getUploadIconRequestBody(icon);
            rootActivity.addSubscription(ApiFactory.postOrganizationIcon(orgaId, requestBody), new PgSubscriber() {
                @Override
                public void on_Next(Object o) {
                    ToastHelper.get().showShort(App.app.getString(R.string.dept_icon_upload_success));
                }
            });
        }

    }


//  保存fragmen状态  =====================================================================================================================

    @Override
    protected void onSaveState(Bundle outState) {
        super.onSaveState(outState);
        outState.putString("organiName", inputOrgiName.getInput());
        outState.putString("organiDesc", inputOrgiDesc.getInput());
    }

    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        if (savedInstanceState != null) {
            orgaName = savedInstanceState.getString("organiName");

            orgaDesc = savedInstanceState.getString("organiDesc");

        }

        Logger.d("organiName = " + orgaName + "organiDesc = " + orgaDesc + "  list size =  " + roles.size());

        if (roles != null) {
            exvAddOrgi.setListviewData(new ExtRoleAdapter(roles, true, (parentOrgisAdapter, position) -> {
                deleteItem(parentOrgisAdapter, position);
            }));
        }


//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        inputOrgiName.setInput(TextUtils.isEmpty(orgaName) ? "" : orgaName);
        inputOrgiDesc.setInput(TextUtils.isEmpty(orgaDesc) ? "" : orgaDesc);
    }


}
