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
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
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
import rx.Observable;

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
    private String deptId;

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
        saveDept();

    }


    /**
     * 如果新增了自己的部门，就轮询提交服务器
     *
     * @param id
     */
    private Observable<List<Role>> postRole(String id) {

        List<Role> roleList = new ArrayList<>();
        for (int i = 0; i < roles.size(); i++) {
            Role role = roles.get(i);
            role.setOrganizationId(id + Constants.special_orgi_role);
            role.setName(id + ":" + role.getTitle());
            roleList.add(role);
        }

        return ApiFactory.postRoles(roleList);
    }

    private Observable<Object> postPrincipal(List<Role> roless) {
        ArrayList<Observable<Object>> observables = new ArrayList<>();
        for (int i = 0; i < roless.size(); i++) {
            Role role = roless.get(i);
            Map<Role, List<Principal>> roleListMap = rolePrinMapping.get(i);
            List<Principal> principals = roleListMap.get(roles.get(i));
            observables.add(ApiFactory.postPrincipal((double) role.getId(), principals));
        }

        return Observable.merge(observables);

    }


    /**
     * 存新增的部门
     */
    private void saveDept() {
        Observable observable;
        if (!exvAddOrgi.isInherited() && checkHasRole()) {
            observable =  ApiFactory.postOrganization(newOrganization())
                    .concatMap(organization -> postRole(organization.getId()))
                    .concatMap(this::postPrincipal)
                    .concatMap(o -> postIsInherited());
        }else{
            observable = ApiFactory.postOrganization(newOrganization());
        }

        rootActivity.addSubscription(observable, new PgSubscriber(rootActivity) {
            @Override
            public void on_Next(Object o) {
                Logger.d("部门添加成功");
                ToastHelper.get().showShort(App.app.getString(R.string.add_department_success));
                saveIcon();
                rootActivity.backStack();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastHelper.get().showShort(App.app.getString(R.string.add_department_false));
            }
        });

    }

    /**
     * 设置是否继承
     */
    private Observable postIsInherited() {
        String id = deptId + Constants.special_orgi_role;
        return ApiFactory.putIsInheroted(id, new IsInherited(false));
    }

    /**
    *检查是否有roles
    */
    private boolean checkHasRole() {
        if (roles != null && roles.size() != 0) {
            return true;
        }
        return false;
    }

    /**
    *new 一个dept
    */
    private Organization newOrganization() {
        orgna = new Organization();
        orgna.setId(StringUtils.getCheckedOrgaId(orgiId) + Constants.special_orgi_department + "/" + orgaName);
        orgna.setParentId(StringUtils.getCheckedOrgaId(orgiId) + Constants.special_orgi_department);

        orgna.setName(orgaName);
        orgna.setInherited(exvAddOrgi.isInherited());
        Logger.d(" dept parent id = " + orgna.getParentId());
        orgna.setDescription(orgaDesc);

        deptId = orgna.getId();
        return orgna;
    }


    /**
     * 显示继承自父的东西
     */
    @Override
    public void onBtnChoise(boolean enabled) {
        if (enabled) {
            getExtendsRole(StringUtils.getCheckedOrgaId(orgiId));
        } else if (roles != null) {
            //
            Logger.d("展示自己的部门");
            setExData(false, roles);
        }

    }


    /**
     * 设置继承框显示数据
     */
    private void setExData(Boolean isInherited, List<Role> roles) {
        if (isInherited) {
            exvAddOrgi.setListviewData(new ExtRoleAdapter(roles));
        } else {
            exvAddOrgi.setListviewData(new ExtRoleAdapter(roles, new ExtRoleAdapter.OnItemDeleteListen() {
                @Override
                public void onItemDelete(ExtRoleAdapter parentOrgisAdapter, int position) {
                    deleteItem(parentOrgisAdapter, position);
                }

                @Override
                public void onItemEdit(ExtRoleAdapter parentOrgisAdapter, int position) {

                }
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
            setExData(true, roles);

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
     * 部门icon
     */
    private void saveIcon() {
        if (icon!=null){
            RequestBody requestBody = PicloadManager.getUploadIconRequestBody(icon);
            rootActivity.addSubscription(ApiFactory.postOrganizationIcon(deptId, requestBody), new PgSubscriber() {
                @Override
                public void on_Next(Object o) {
                    Logger.d("部门图标上传成功");
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
        ivHead.setImageBitmap(icon);
        if (roles != null) {
            setExData(false, roles);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        inputOrgiName.setInput(TextUtils.isEmpty(orgaName) ? "" : orgaName);
        inputOrgiDesc.setInput(TextUtils.isEmpty(orgaDesc) ? "" : orgaDesc);

    }


}
