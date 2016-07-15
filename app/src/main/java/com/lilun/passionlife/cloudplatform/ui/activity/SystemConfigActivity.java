package com.lilun.passionlife.cloudplatform.ui.activity;

import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.SystemConfigGvAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionActivity;
import com.lilun.passionlife.cloudplatform.base.BaseNetActivity;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.custom_view.ShowOrgiPopupwindow;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.ACache;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrFrameLayout;


public class SystemConfigActivity extends BaseFunctionActivity implements ShowOrgiPopupwindow.onItemClick, BaseNetActivity.callBack_orgiChildren {


    @Bind(R.id.gv_service)
    GridView gvService;
    @Bind(R.id.home_ptr)
    PtrFrameLayout homePtr;
    private List<Organization> data = null;
    private ShowOrgiPopupwindow pop;
    private String childOrginaId;
    private String childOrginaName;
    private List<OrganizationService> visibleOrgiService;

//    @OnClick(R.id.orgi_manager)
//    /**
//     *组织机构管理
//     */
//    public void orgi_manager() {
//        IntentUtils.startAct(mAx, OrganizationActivity.class);
//    }
//
//    @OnClick(R.id.role_manager)
//    /**
//     *角色管理
//     */
//    void role_manager() {
//        IntentUtils.startAct(mAx, RoleManagerActivity.class);
//    }
//
//    @OnClick(R.id.staff_manager)
//    /**
//     *员工管理
//     */
//    void staff_manager() {
////        ToastHelper.get(App.app).showShort(App.app.getString(R.string.Write_Open));
//        IntentUtils.startAct(mAx, StaffManagerActivity.class);
//    }
//
//
//
//    @OnClick(R.id.dept_manager)
//    void dept_manager(){IntentUtils.startAct(mAx, DeptManagerActivity.class);}
//
//    @OnClick(R.id.module_manager)
//    /**
//     *模块管理
//     */
//    void module_manager() {
//        IntentUtils.startAct(mAx, ModuleManagerActivity.class);
//    }


    @Override
    public View setContent() {
        title.setText(getString(R.string.system_config));
        View view = inflater.inflate(R.layout.activity_system_config, null);

        return view;
    }

    @Override
    public void onCreate() {
        visibleOrgiService = (List<OrganizationService>) ACache.get(App.app).getAsObject(Constants.cacheKey_service);
        if (visibleOrgiService != null) {
            showServices(visibleOrgiService);
        } else {
            Logger.d("showServices  is null");
        }
//        setInitData();
//        childOrginaName =  SpUtils.getString(Constants.key_defOrgina);
//        def_org.setVisibility(View.VISIBLE);
//        def_org.setText(childOrginaName);

    }


    private void showServices(List<OrganizationService> data) {
        gvService.setNumColumns(data.size() == 1 ? 1 : 2);
        gvService.setAdapter(new SystemConfigGvAdapter(mCx, data));
        gvService.setOnItemClickListener((parent, view, position, id) -> {

        });
    }


    /**
//     * 设置初始化显示数据
//     */
//
//    private void setInitData() {
//        childOrginaId = SpUtils.getString(Constants.key_child_OrginaId);
//        childOrginaName = SpUtils.getString(Constants.key_child_OrginaName);
//        if (TextUtils.isEmpty(childOrginaId) || TextUtils.isEmpty(childOrginaName)) {
//            childOrginaId = SpUtils.getString(Constants.key_defOrginaId);
//            //
//            childOrginaName = SpUtils.getString(Constants.key_defOrgina);
//        }
//        //设置显示
//        def_org.setVisibility(View.VISIBLE);
//        def_org.setText(childOrginaName);
//
//        //获取默认组织直接儿子们
//        getOrgiChildren(childOrginaId, this);
//
//        //设置点击事件,show popupwindow
//        def_org.setOnClickListener(v -> {
//            pop = new ShowOrgiPopupwindow(App.app, first.getWidth(), first.getHeight(), this);
//            pop.setData(data);
//            bg.setVisibility(View.VISIBLE);
//            pop.getPopupWindow(def_org);
//        });
//
//
//    }
//
    @Override
    public void onItemClick(String orgId, String orgName) {
        //给默认的儿子赋值
        childOrginaId = orgId;
        childOrginaName = orgName;
        def_org.setText(orgName);
        getOrgiChildren(orgId, this);
    }

    @Override
    public void onDismiss() {
        //pop消失的时候把默认的儿子存进sp
        SpUtils.setString(Constants.key_child_OrginaId, childOrginaId);
        SpUtils.setString(Constants.key_child_OrginaName, childOrginaName);
//        bg.setVisibility(View.GONE);
    }

    @Override
    public void onGetOgriChildren(List<Organization> orgis) {
        data = orgis;
        if (pop != null) {
            pop.setData(data);
        }
    }
}
