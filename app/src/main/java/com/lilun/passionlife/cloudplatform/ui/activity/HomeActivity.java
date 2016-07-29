package com.lilun.passionlife.cloudplatform.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.GridView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.HomeGvAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.ACache;
import com.lilun.passionlife.cloudplatform.utils.CacheUtils;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;

public class HomeActivity extends BaseActivity {


    @Bind(R.id.gv_module)
    GridView gvModule;
    @Bind(R.id.tv_deforgi)
    TextView tvDeforgi;
    @Bind(R.id.home_ptr)
    PtrFrameLayout homePtr;

    private String defOrgaId = "";
    private String defOrgaName = "";
    private List<OrganizationService> visibleOrgiService = new ArrayList<>();
    private ACache aCache;
    private double userId1;
    private boolean isReflash = false;
    private Bitmap icon;


    @Override
    public int setContentView() {
        return R.layout.activity_main;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        userId1 = Double.valueOf(SpUtils.getInt(TokenManager.USERID));
        aCache = ACache.get(App.app);

        //TODO 展示进度条
        getorgiList();


        MaterialHeader header = new MaterialHeader(mCx);
        header.setPtrFrameLayout(homePtr);
        header.onUIRefreshBegin(homePtr);
        homePtr.setHeaderView(header);
        homePtr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isReflash = true;
                getBelongOrga(new callBack_getBelongOrga() {
                    @Override
                    public void onGetBelongOrga(List<OrganizationAccount> orgas) {
                        dealData(orgas);
                    }

                    @Override
                    public void onError() {
                        homePtr.refreshComplete();
                    }
                });
            }
        });
    }

    @OnClick(R.id.tv_deforgi)
    void changeChildOrga() {
        if (!TextUtils.isEmpty(defOrgaId) && !TextUtils.isEmpty(defOrgaName)) {
            Intent intent = new Intent(this, ChangeOrganizationActivity.class);
            this.startActivity(intent);
        }
    }

    /**
     * 获取到所属组织之后
     *
     * @param orgas
     */
    public void dealData(List<OrganizationAccount> orgas) {
        if (homePtr.isRefreshing()) {
            homePtr.refreshComplete();
        }
        if (orgas.size() == 0) {
            emptyBelOrga();
        } else {
            CacheUtils.putCache("belongOrgas",orgas);
            for (OrganizationAccount organization : orgas) {
                if (organization.isIsDefault()) {
                    //获取到组织后，把默认组织的id存进本地，post事件方便系统配置界面使用
                    String organizationId = organization.getOrganizationId();
                    String id = StringUtils.belongOgaId(organizationId);
                    String name = StringUtils.belongOgaName(organizationId);

                    defOrgaId = id;
                    defOrgaName = name;

                    //默认所属的组织
                    SpUtils.setString(Constants.key_currentBelongOrgaId, id);
                    SpUtils.setString(Constants.key_currentBelongOrgaName, name);

                    //当前组织就是默认的所属组织
                    SpUtils.setString(Constants.key_currentOrgaId, id);
                    SpUtils.setString(Constants.key_currentOrgaName, name);
                    getServiceList(id);
                    tvDeforgi.setText(name);
                    Logger.d("默认组织  = " + id + "-----" + name);
                }
            }
        }
    }

    /**
     * 个人中心
     */
    @OnClick(R.id.home_personal)
    void personalInfo() {
        IntentUtils.startAct(mAc, PersonalCenterActivity.class);
    }


    /**
     * 获取组织列表
     */
    private void getorgiList() {
        defOrgaName = SpUtils.getString(Constants.key_currentOrgaName);
        defOrgaId = SpUtils.getString(Constants.key_currentOrgaId);
        if (!TextUtils.isEmpty(defOrgaName) && !TextUtils.isEmpty(defOrgaId)) {
            getServiceList(defOrgaId);
            tvDeforgi.setText(defOrgaName);
        } else {
            getBelongOrga(new callBack_getBelongOrga() {
                @Override
                public void onGetBelongOrga(List<OrganizationAccount> orgas) {
                    dealData(orgas);
                }
            });
        }


    }


    /**
     * 组织为空的时候
     */
    private void emptyBelOrga() {
        tvDeforgi.setText("");
        visibleOrgiService = new ArrayList<>();
        showServices(visibleOrgiService);

    }


    /**
     * 异步获取服务列表
     */
    public void getServiceList(String orgId) {

        visibleOrgiService = (List<OrganizationService>) aCache.getAsObject(Constants.cacheKey_service);
        if (visibleOrgiService == null || isReflash) {
            Logger.d("get service  from net");
            getSerListFromNet(userId1, orgId, visibleOrgiService1 -> {
                visibleOrgiService = visibleOrgiService1;
                EventBus.getDefault().post(new Event.getOrgiServices_ok(visibleOrgiService));
            });
        } else {
            EventBus.getDefault().post(new Event.getOrgiServices_ok(visibleOrgiService));
        }
    }


    /**
     * 展示服务列表事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showServices(Event.getOrgiServices_ok event) {
        List<OrganizationService> data = event.getData();
        //把可以显示的服务存进本地
        showServices(data);
    }


    /**
     * 更新了服务，刷新缓存
     */
    @Subscribe
    public void addNewService(Event.postService event) {
        Logger.d("get add new service");
        OrganizationService service = event.getService();
        if (visibleOrgiService != null) {
            for (int i = 0; i < visibleOrgiService.size(); i++) {
                if (visibleOrgiService.get(i).getId().equals(service.getId())) {
                    visibleOrgiService.remove(i);
                }
            }
            visibleOrgiService.add(service);
            Logger.d("service size = " + visibleOrgiService.size());
            aCache.put(Constants.cacheKey_service, (Serializable) visibleOrgiService);
            showServices(visibleOrgiService);
        } else {
            Logger.d("visibleOrgiService is  null");
        }

    }

    //更新了一个服务
    @Subscribe
    public void putService(Event.putService event) {
        OrganizationService service = event.getService();
        String visible = service.getSettings().getVisible();
        if (visibleOrgiService != null) {
            for (int i = 0; i < visibleOrgiService.size(); i++) {
                if (visibleOrgiService.get(i).getId().equals(service.getId())) {
                    visibleOrgiService.remove(i);
                }
            }
        }
        if (Boolean.parseBoolean(visible)) {
            //如果是可见的在添加进来
            visibleOrgiService.add(service);
        }
        aCache.put(Constants.cacheKey_service, (Serializable) visibleOrgiService);
        showServices(visibleOrgiService);
    }

    /**
     * 删除了一个服务，从网络获取最新的服务，刷新缓存
     */
    @Subscribe
    public void deleteOrganiService(Event.deleteOrganiService event) {
        if (TextUtils.isEmpty(defOrgaId)) {
            return;
        }
        getSerListFromNet(userId1, defOrgaId, visibleOrgiService1 -> {
            visibleOrgiService = visibleOrgiService1;
            showServices(visibleOrgiService1);
        });
    }

    /**
     * 切换了旗下的组织
     */
    @Subscribe
    public void changeChildOrganization(Event.ChangeChildOrganization event) {
        //切换了当前的组织，刷新sp里面的orgaId和orgaName
        defOrgaId = event.getOrganizationId();
        defOrgaName = event.getOrganizationName();
        SpUtils.setString(Constants.key_currentOrgaId, defOrgaId);
        SpUtils.setString(Constants.key_currentOrgaName, defOrgaName);
        tvDeforgi.setText(defOrgaName);
    }


    private void showServices(List<OrganizationService> data) {
        gvModule.setNumColumns(data.size() == 1 ? 1 : 2);
        gvModule.setAdapter(new HomeGvAdapter(mCx, data));
        gvModule.setOnItemClickListener((parent, view, position, id) -> {
            if (data.get(position).getServiceId() != null) {
                String serviceId = data.get(position).getServiceId();


                //模块管理
                if (serviceId.equals(KnownServices.Module_Service)) {
                    IntentUtils.startAct(mAc, ModuleManagerActivity.class);
                }

                //员工管理
                else if (serviceId.equals(KnownServices.Account_Service)) {
                    IntentUtils.startAct(mAc, StaffManagerActivity.class);
                }

                //角色管理
                else if (serviceId.equals(KnownServices.Role_Service)) {
                    Logger.d("角色管理");
                    IntentUtils.startAct(mAc, RoleManagerActivity.class);
                }

                //组织机构管理
                else if (serviceId.equals(KnownServices.Organization_Service)) {
                    IntentUtils.startAct(mAc, OrganizationActivity.class);
                }

                //系统配置
                else if (serviceId.equals(KnownServices.SysConfig_Service)) {
                    IntentUtils.startAct(mAc, SystemConfigActivity.class);
                }


                //部门管理
                else if (serviceId.equals(KnownServices.Department_Service)) {
                    IntentUtils.startAct(mAc, DeptManagerActivity.class);
                }
            }
        });
    }


}
