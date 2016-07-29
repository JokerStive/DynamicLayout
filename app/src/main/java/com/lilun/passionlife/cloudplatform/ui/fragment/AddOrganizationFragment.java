package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.ExtDeptAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.IsInherited;
import com.lilun.passionlife.cloudplatform.bean.Organization;
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
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.RequestBody;

/**
 * Created by youke on 2016/6/22.
 * 新增组织
 */
public class AddOrganizationFragment extends BaseFunctionFragment implements ExtendItem.onClickListener {


    @Bind(R.id.head)
    TextView head;

    @Bind(R.id.iv_head)
    CircleImageView ivHead;


    @Bind(R.id.input_role_name)
    RegItemView inputOrgiName;

    @Bind(R.id.exv_add_orgi)
    ExtendItem exvAddOrgi;

    @Bind(R.id.save)
    Button save;
    @Bind(R.id.input_orgi_desc)
    RegItemView inputOrgiDesc;

    private List<Organization> departments = new ArrayList<>();
    private String orgaName;
    private Organization orgna;
    private String orgaDesc;
    int dex = 0;
    private int size;
    private Organization newDepartment;
    private View view;
    private Bitmap icon;

    @Override
    public View setView() {

        view = inflater.inflate(R.layout.fragment_add_organization, null);
        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        rootActivity.setTitle(App.app.getString(R.string.add_orgi));
        exvAddOrgi.setOnClickListen(this);
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


    @OnClick(R.id.iv_head)
    void choiseHead(){
        Logger.d("选择头像");
        rootActivity.choiseHeadPic(view);
    }

    @Subscribe
    public void choisHead(Event.choiseHeadPic event){
        Bitmap bitmap = event.getBitmap();
        if (bitmap!=null){
            StringUtils.getBytesFromBitmap(bitmap);
//            icon = bitmap;
//            ivHead.setImageBitmap(bitmap);

        }
    }


    /**
     * 如果新增了自己的部门，就轮询提交服务器
     *
     * @param id
     */
    private void saveDepartment(String id) {
        if (departments==null && departments.size()==0){return;}
        List<Organization>  depts = new ArrayList<>();
        for (int i = 0; i < departments.size(); i++) {
            Organization depart = departments.get(i);
            depart.setId(id + Constants.special_orgi_department + "/" + depart.getName());
            depart.setParentId(id + Constants.special_orgi_department);
            Logger.d("department id  = " + id + Constants.special_orgi_department + "/" + depart.getName());
            Logger.d("department parentid  = " + id + "/" + depart.getName() + Constants.special_orgi_department);
            depts.add(depart);
        }


        rootActivity.addSubscription(ApiFactory.postOrganizations(depts), new PgSubscriber<List<Organization>>(rootActivity) {
            @Override
            public void on_Next(List<Organization> organizationBean) {
                ToastHelper.get().showShort(App.app.getString(R.string.add_department_success));
                rootActivity.backStack();
                postDeptIcon();
            }
        });
    }

    /**
    *新增部门的图标
    */
    private void postDeptIcon() {
        //TODO 上传部门的图标

    }


    /**
     * 存新增的组织
     */
    private void saveOrgi() {
        orgna = new Organization();
        orgna.setInherited(exvAddOrgi.isInherited());
        orgna.setId(orgiId + "/" + orgaName);
        orgna.setName(orgaName);
        orgna.setParentId(orgiId);
        Logger.d("isInherited" + orgna.isInherited());
        orgna.setDescription(orgaDesc);
        //TODO 设置icon
        rootActivity.addSubscription(ApiFactory.postOrganization(orgna), new PgSubscriber<Organization>(rootActivity) {
            @Override
            public void on_Next(Organization orga) {
                ToastHelper.get().showShort(orgna.getName()+"组织新增成功");
                String id = orga.getId()+Constants.special_orgi_department;
                rootActivity.setIsInherited(id,new IsInherited(orga.isInherited()));
                if (icon!=null){
                    saveIcon(orga);
                }

                //不是集成就添加自己的部门
                if (!orga.isInherited()){
                    saveDepartment(orga.getId());
                }

            }

            @Override
            public void on_Error() {
                super.on_Error();
                ToastHelper.get().showShort(orgna.getName()+"组织新增失败");
            }
        });
    }

    /**
    *新增组织Icon
     */
    private void saveIcon(Organization orga) {
        if (icon!=null){
            String orgaId = orga.getId();
            RequestBody requestBody = PicloadManager.getUploadIconRequestBody(icon);
            rootActivity.addSubscription(ApiFactory.postOrganizationIcon(orgaId, requestBody), new PgSubscriber(rootActivity) {
                @Override
                public void on_Next(Object o) {
                    ToastHelper.get().showShort("组织图标上传成功");
                }
            });
        }

    }


    /**
     * 显示继承自父的东西
     */
    @Override
    public void onBtnChoise(boolean enabled) {
        if (enabled) {
            getExtendsDepartment(orgiId);
        } else if (departments != null) {
            //
            Logger.d("展示自己的部门");
            exvAddOrgi.setListviewData(new ExtDeptAdapter(departments, true, (parentOrgisAdapter, position) -> {
                deleteItem(parentOrgisAdapter, position);
            }));
        }

    }

    /**
     * 从内存中删除一个dep  并刷新视图
     */
    private void deleteItem(ExtDeptAdapter parentOrgisAdapter, int position) {
        departments.remove(position);
        parentOrgisAdapter.notifyDataSetChanged();
    }


    /**
     * 获取继承自父组织的部门
     */
    private void getExtendsDepartment(String organiId) {
        rootActivity.getOrgaDepartment(organiId, organizations -> {
            if (organizations.size() == 0) {
                ToastHelper.get(mCx).showShort(mCx.getString(R.string.empty_depart_list));
            }
            exvAddOrgi.setListviewData(new ExtDeptAdapter(organizations, false, (parentOrgisAdapter, position) -> {

            }));
        });
    }

    @Override
    /**
     *新增what
     */
    public void onAddWhatClick() {
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new AddDepartmentFragment(), mCx.getString(R.string.add_department));
        EventBus.getDefault().post(event);

    }


    /**
     * 新增了一个部门
     */
    @Subscribe
    public void addDepartment(Event.addNewDepartment event) {
        newDepartment = event.getDepartment();
        departments.add(newDepartment);
        Logger.d(" lis size = " +departments.size());
    }


//  保存fragmen状态  =====================================================================================================================

    @Override
    protected void onSaveState(Bundle outState) {
        super.onSaveState(outState);
        outState.putString("organiName", inputOrgiName.getInput());
        outState.putString("organiDesc", inputOrgiDesc.getInput());
//        outState.putSerializable("listData", (Serializable) departments);
    }

    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        if (savedInstanceState != null) {
            orgaName = savedInstanceState.getString("organiName");

            orgaDesc = savedInstanceState.getString("organiDesc");
//            departments = (List<Organization>) savedInstanceState.get("listData");
//            if (newDepartment != null) {
//                departments.add(newDepartment);
//                newDepartment = null;
//            }
        }

//        Logger.d("organiName = " + orgaName + "organiDesc = " + orgaDesc + "  list size =  " + departments.size());

        if (departments!=null){
            exvAddOrgi.setListviewData(new ExtDeptAdapter(departments, true, (parentOrgisAdapter, position) -> {
                deleteItem(parentOrgisAdapter, position);
            }));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        inputOrgiName.setInput(TextUtils.isEmpty(orgaName) ? "" : orgaName);
        inputOrgiDesc.setInput(TextUtils.isEmpty(orgaDesc) ? "" : orgaDesc);
    }


}
