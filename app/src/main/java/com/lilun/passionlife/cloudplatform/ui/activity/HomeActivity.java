package com.lilun.passionlife.cloudplatform.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.HomeGvAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.common.Admin;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.KnowPermission;
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.ACache;
import com.lilun.passionlife.cloudplatform.utils.CacheUtils;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    @Bind(R.id.home_setting)
    ImageView homeSetting;

    private String defOrgaId = "";
    private String defOrgaName = "";
    private List<OrganizationService> visibleOrgiService;
    private List<OrganizationAccount> belongOrgas;
    private ACache aCache;
    private double userId1;
    private boolean isReflash = false;
    private Bitmap icon;
    private int index = 0;
    private int serviceSize = 0;


    @OnClick(R.id.home_setting)
    void systemConfig() {
        IntentUtils.startAct(mAc, SystemConfigActivity.class);
    }


    @Override
    public int setContentView() {
        return R.layout.activity_main;
    }


    /**
     * 是否有systemConfig.view的权限
     */
    private void viewSystemConfig() {
        String permission = KnownServices.SysConfig_Service + KnowPermission.viewPermission;
        checkHasPermission(userId1, permission, hasPermission -> {
            if (hasPermission) {
                homeSetting.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInitData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(tvDeforgi.getText())) {
            tvDeforgi.setText(SpUtils.getString(Constants.key_currentBelongOrgaName));
        }
    }

    /**
     * 设置初始化显示数据
     */
    private void setInitData() {
        userId1 = Double.valueOf(SpUtils.getInt(TokenManager.USERID));
        if (isAdmin()) {
            homeSetting.setVisibility(View.VISIBLE);
        } else {
            viewSystemConfig();
        }
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
                EventBus.getDefault().post(new Event.ReflashHomeData());
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
        belongOrgas = (List<OrganizationAccount>) CacheUtils.getCache("belongOrgas");
        defOrgaId = SpUtils.getString(Constants.key_currentOrgaId);
        if (belongOrgas == null || isReflash) {
            getBelongOrga(new callBack_getBelongOrga() {
                @Override
                public void onGetBelongOrga(List<OrganizationAccount> orgas) {
                    dealBelongOrgaData(orgas);
                }

                @Override
                public void onError() {
                    super.onError();
                    CacheUtils.putCache("belongOrgas", null);
                    Logger.d("获取所属组织失败，请稍后再试");
                    belongOrgas = new ArrayList<>();
                    dealBelongOrgaData(belongOrgas);
                }
            });
        } else {
            getServiceList(defOrgaId);
        }


    }


    /**
     * 处理获取到的 "所属组织"
     *
     * @param orgas
     */
    public void dealBelongOrgaData(List<OrganizationAccount> orgas) {
//        isReflash=false;
        if (homePtr.isRefreshing()) {
            homePtr.refreshComplete();
        }
        if (orgas.size() == 0) {
            emptyBelOrga();
        } else {
            CacheUtils.putCache("belongOrgas", orgas);
            int defBelongOrgaIndex = 0;
            for (int i = 0; i < orgas.size(); i++) {
                if (orgas.get(i).isIsDefault()) {
                    defBelongOrgaIndex = i;
                }
            }
            //如果没有默认组织默认就是false
            setDefOrgaSp(orgas.get(defBelongOrgaIndex));

        }
    }

    /**
     * 设置 本地"所属组织"数据
     */
    private void setDefOrgaSp(OrganizationAccount oa) {
        //获取到组织后，把默认组织的id存进本地，post事件方便系统配置界面使用
        String organizationId = oa.getOrganizationId();
        String id = StringUtils.belongOgaId(organizationId);
        String name = StringUtils.belongOgaName(organizationId);


//
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name)) {
            id = Admin.id;
            name = Admin.name;
        }

        defOrgaId = id;
        defOrgaName = name;
        Logger.d(defOrgaName);
//        tvDeforgi.setText(defOrgaName);
        //默认所属的组织
        SpUtils.setString(Constants.key_currentBelongOrgaId, id);
        SpUtils.setString(Constants.key_currentBelongOrgaName, name);

        //当前组织就是默认的所属组织
        SpUtils.setString(Constants.key_currentOrgaId, id);
        SpUtils.setString(Constants.key_currentOrgaName, name);

        tvDeforgi.setText(defOrgaName);
        getServiceList(defOrgaId);
        Logger.d("默认组织  = " + id + "-----" + name);
    }


    /**
     * 组织为空的时候
     */
    private void emptyBelOrga() {
//        tvDeforgi.setText("");
        visibleOrgiService = new ArrayList<>();
        showServices(visibleOrgiService);

    }


    /**
     * 获取功能服务列表
     */
    public void getServiceList(String orgId) {
        visibleOrgiService = (List<OrganizationService>) aCache.getAsObject(Constants.cacheKey_service);
        if (visibleOrgiService == null || isReflash) {
            Logger.d("get service  from net");
            getSerListFromNet(orgId, new callBack_visible_service() {
                @Override
                public void onGetVisibleService(List<OrganizationService> oss) {
                    visibleOrgiService = oss;
                    dealServiceData(oss);
                }

                @Override
                public void onError() {
                    super.onError();
                    ToastHelper.get().showShort(App.app.getString(R.string.get_orga_service_false));
                    visibleOrgiService = new ArrayList<>();
                    showServices(visibleOrgiService);
                }
            });
        } else {
            showServices(visibleOrgiService);
        }
    }

    /**
     * 处理服务数据
     */
    private void dealServiceData(List<OrganizationService> vss) {
        if (isAdmin()) {
            //当前组织下的服务数据已经缓存成功，系统配置见面可以刷新视图
            ACache.get(App.app).put(Constants.cacheKey_service, (Serializable) vss);
            showServices(vss);
            EventBus.getDefault().post(new Event.CurrentOsHasCached());
        } else {
            visibleOrgiService.clear();
            String permission;
            serviceSize = vss.size();
            for (int i = 0; i < vss.size(); i++) {
                OrganizationService os = vss.get(i);
                permission = os.getServiceId() + KnowPermission.viewPermission;
                checkHasPermission(userId1, permission, hasPermission -> {
                    then();
                    if (hasPermission) {
                        visibleOrgiService.add(os);
                    }
                });
            }

        }


    }

    private void then() {
        Logger.d("index = " + index + "-----" + "size = " + serviceSize);
        if (index == serviceSize - 1) {
            ACache.get(App.app).put(Constants.cacheKey_service, (Serializable) visibleOrgiService);
            showServices(visibleOrgiService);
            //当前组织下的服务数据已经缓存成功，系统配置见面可以刷新视图
            EventBus.getDefault().post(new Event.CurrentOsHasCached());
        }
        index++;
    }


    @Subscribe
    public void loginSuccess(Event.LoginSuccess event) {
        Logger.d("登录成功设置首页初始换数据");
        isReflash = true;
        setInitData();
    }


    /**
     * post服务，刷新缓存
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
        String visible = "true";
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
        isReflash = true;
        getServiceList(defOrgaId);
    }

    /**
     * 切换了旗下的组织
     */
    @Subscribe
    public void changeChildOrganization(Event.ChangeChildOrganization event) {
        //切换了当前的组织，刷新sp里面的orgaId和orgaName
        defOrgaId = event.getOrganizationId();
        defOrgaName = event.getOrganizationName();
        changCurrentShowData(defOrgaId, defOrgaName, false);

    }


    /**
     * 切换了默认组织
     */
    @Subscribe
    public void changDefBelongOrga(Event.DefOrgaHasChanges event) {
        OrganizationAccount oa = event.getOa();
        String id = StringUtils.belongOgaId(oa.getOrganizationId());
        String name = StringUtils.belongOgaName(oa.getOrganizationId());
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name)) {
            id = Admin.id;
            name = Admin.id;
        }
        defOrgaId = id;
        defOrgaName = name;
        changCurrentShowData(id, name, true);
    }

    /**
     * 刷新首页数据
     */
    @Subscribe
    public void reflasHomeData(Event.ReflashHomeData event) {
        isReflash = true;
        getorgiList();
    }


    /**
     * 组织切换，修改当前显示和本地存储的数据
     */
    private void changCurrentShowData(String defOrgaId, String defOrgaName, boolean isReflash) {
        visibleOrgiService.clear();
        SpUtils.setString(Constants.key_currentOrgaId, defOrgaId);
        SpUtils.setString(Constants.key_currentOrgaName, defOrgaName);
        tvDeforgi.setText(defOrgaName);
        this.isReflash = isReflash;
        getServiceList(defOrgaId);
    }


    private void showServices(List<OrganizationService> data) {
        isReflash = false;
        gvModule.setNumColumns(data.size() == 1 ? 1 : 2);
        gvModule.setAdapter(new HomeGvAdapter(mCx, data));
        gvModule.setOnItemClickListener((parent, view, position, id) -> {
            if (data.get(position).getServiceId() != null) {
                String serviceId = data.get(position).getServiceId();
                IntentUtils.startModuleActivity(mAc,serviceId);

            }
        });
    }


}
