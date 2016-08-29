package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.lilun.passionlife.cloudplatform.custom_view.InputView;
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
public class AddOrganizationFragment extends BaseFunctionFragment implements ExtendItem.onClickListener {


    @Bind(R.id.head)
    TextView head;

    @Bind(R.id.iv_head)
    CircleImageView ivHead;


    @Bind(R.id.input_role_name)
    InputView inputOrgiName;

    @Bind(R.id.exv_add_orgi)
    ExtendItem exvAddOrgi;


    @Bind(R.id.input_orgi_desc)
    InputView inputOrgiDesc;

    private List<Organization> departments = new ArrayList<>();
    private List<Map<String, Bitmap>> deptIconMappings;
    private String orgaName;
    private Organization orgna;
    private String orgaDesc;
    private Organization newDepartment;
    private View view;
    private Bitmap icon;
    private String organizationId;

    @Override
    public View setView() {

        view = inflater.inflate(R.layout.fragment_add_organization, null);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deptIconMappings = new ArrayList<>();
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
    @Override
    protected void save() {
        orgaName = inputOrgiName.getInput();
        orgaDesc = inputOrgiDesc.getInput();
        if (TextUtils.isEmpty(orgaName) || TextUtils.isEmpty(orgaDesc)) {
            ToastHelper.get(mCx).showShort(mCx.getString(R.string.not_orginame_empty));
            return;
        }
        postOrganization();

    }


    @OnClick(R.id.iv_head)
    void choiseHead() {
        Logger.d("选择头像");
        rootActivity.choiseHeadPic(view);
    }

    @Subscribe
    public void choisHead(Event.choiseHeadPic event) {
        Bitmap bitmap = event.getBitmap();
        if (bitmap != null) {
            StringUtils.getBytesFromBitmap(bitmap);
            icon = bitmap;
            ivHead.setImageBitmap(bitmap);

        }
    }


    /**
     * 如果新增了自己的部门，就轮询提交服务器
     *
     */
    private Observable<List<Organization>> postDept() {
        List<Organization> depts = new ArrayList<>();
        for (int i = 0; i < departments.size(); i++) {
            Organization depart = departments.get(i);
            depart.setId(organizationId + Constants.special_orgi_department + "/" + depart.getName());
            depart.setParentId(organizationId + Constants.special_orgi_department);
//            Logger.d("department id  = " + id + Constants.special_orgi_department + "/" + depart.getName());
//            Logger.d("department parentid  = " + id + "/" + depart.getName() + Constants.special_orgi_department);
            depts.add(depart);
        }


        return ApiFactory.postOrganizations(depts);

    }


    /**
     * 存新增的组织
     */
    private void postOrganization() {
        Observable observable ;
        if (!exvAddOrgi.isInherited()) {
            if (checkHasDept()){
                observable =  ApiFactory.postOrganization(newOrganization())
                        .concatMap(organization -> postIsInherited(organization.getId()))
                        .concatMap(o -> postDept());
            }else {
                observable =  ApiFactory.postOrganization(newOrganization()).concatMap(o -> postDept());
            }
        }else{
            observable = ApiFactory.postOrganization(newOrganization());
        }


        rootActivity.addSubscription(observable, new PgSubscriber(rootActivity) {
            @Override
            public void on_Next(Object o) {
                Logger.d("组织添加成功");
                ToastHelper.get().showShort(App.app.getString(R.string.add_orgi_success));
                saveOrgaIcon();
                saveDeptIcon();
                EventBus.getDefault().post(new Event.reflashOrgaList());
                rootActivity.backStack();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastHelper.get().showShort(App.app.getString(R.string.add_orgi_false));
            }
        });
    }

    private Observable<Object> postIsInherited(String orgaId) {
        String id = orgaId + Constants.special_orgi_department;
        return ApiFactory.putIsInheroted(id, new IsInherited(false));
    }

    /**
    *检查是部门是否为0
    */
    private boolean checkHasDept() {
        if (departments != null && departments.size() != 0) {
            return true;
        }
        return false;
    }



    private Organization newOrganization() {
        orgna = new Organization();
        orgna.setInherited(exvAddOrgi.isInherited());
        orgna.setId(StringUtils.getCheckedOrgaId(orgiId) + "/" + orgaName);
        orgna.setName(orgaName);
        orgna.setParentId(orgiId);
        orgna.setDescription(orgaDesc);
        organizationId =orgna.getId();
        return orgna;
    }

    /**
     * 新增组织Icon
     */
    private void saveOrgaIcon() {
        if (icon != null) {
            RequestBody requestBody = PicloadManager.getUploadIconRequestBody(icon);
            rootActivity.addSubscription(ApiFactory.postOrganizationIcon(organizationId, requestBody), new PgSubscriber() {
                @Override
                public void on_Next(Object o) {
                }
            });
        }

    }


    /**
     * 新增部门icon
     */
    private void saveDeptIcon() {
        if (deptIconMappings.size()!=0) {
            setMappingId(deptIconMappings);

            List<Observable<Object>>  observables = new ArrayList<>();
            for(Map<String,Bitmap> mapping:deptIconMappings){
                for(String name:mapping.keySet()){
                    RequestBody requestBody = PicloadManager.getUploadIconRequestBody(mapping.get(name));
                    observables.add(ApiFactory.postOrganizationIcon(organizationId+Constants.special_orgi_department+"/"+name, requestBody));
                }
            }
            rootActivity.addSubscription(Observable.merge(observables), new PgSubscriber() {
                @Override
                public void on_Next(Object o) {
                    Logger.d("部门图标上传成功");
                }
            });
        }

    }

    private void setMappingId(List<Map<String, Bitmap>> deptIconMappings) {
        for(Map<String,Bitmap> mapping:deptIconMappings){
            for(String name:mapping.keySet()){

            }
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
            setExData(false, departments);
            Logger.d("展示自己的部门");
        }

    }


    private void setExData(Boolean isInherited, List<Organization> depts) {
        if (isInherited) {
            exvAddOrgi.setListviewData(new ExtDeptAdapter(depts));
        } else {
            exvAddOrgi.setListviewData(new ExtDeptAdapter(depts, (parentOrgisAdapter, position) -> {
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

            setExData(true, organizations);
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

        if (event.getIcon() != null) {
            Map<String, Bitmap> mapping = new HashMap<>();
            mapping.put(newDepartment.getName(), event.getIcon());
            deptIconMappings.add(mapping);
        }
        Logger.d(" lis size = " + departments.size());
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


        ivHead.setImageBitmap(icon);
        if (departments != null) {
            setExData(false, departments);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        inputOrgiName.setInput(TextUtils.isEmpty(orgaName) ? "" : orgaName);
        inputOrgiDesc.setInput(TextUtils.isEmpty(orgaDesc) ? "" : orgaDesc);
    }


}
