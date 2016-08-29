package com.lilun.passionlife.cloudplatform.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.ListHomeModulesAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseNetAppcomActivity;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.common.Admin;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.KnowPermission;
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.common.User;
import com.lilun.passionlife.cloudplatform.common.constans.BelongOass;
import com.lilun.passionlife.cloudplatform.common.constans.CurrentOas;
import com.lilun.passionlife.cloudplatform.custom_view.LoadingPager;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.CacheUtils;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;
import com.lilun.passionlife.cloudplatform.utils.SnackbarHelper;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.Observable;

/**
 * Created by Administrator on 2016/8/25.
 */
public class MainActivity extends BaseNetAppcomActivity {


    ImageView homePersonal;

    TextView tvDeforgi;

    ImageView homeSetting;

    GridView gvModule;

    PtrFrameLayout homePtr;

    CoordinatorLayout coordinatorLayout;

    private String currentOasId;
    private String currentOasName;
    private List<OrganizationService> modules;
    private ListHomeModulesAdapter adapter;
    private LoadingPager loadingPager;
    private String orgaId;
    private double userId;
    private View successView;


    class HomeEvent{
        public String getOpenWhichActivity() {
            return openWhichActivity;
        }

        public HomeEvent(String openWhichActivity) {
            this.openWhichActivity = openWhichActivity;
        }
        private String openWhichActivity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_home);
        FrameLayout fl_container = (FrameLayout) findViewById(R.id.fl_container);

        homePersonal = (ImageView) findViewById(R.id.home_personal);
        tvDeforgi = (TextView) findViewById(R.id.tv_deforgi);
        homeSetting = (ImageView) findViewById(R.id.home_setting);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


        orgaId = SpUtils.getString(Constants.key_currentOrgaId);
        userId = (double) User.getId();


        assert fl_container != null;
        fl_container.addView(createContentView());

    }
    private View createContentView() {
        if (loadingPager==null){
            loadingPager = new LoadingPager(R.layout.page_empty,R.layout.page_error,R.layout.page_loading) {
                @Override
                protected View createSuccessView() {
                    return MainActivity.this.createSuccessView();
                }
            };
            loadingPager.setOnErrorPageClickListener(this::onErrorPageClick);
        }
        loadingPager.showPagerView();
        return loadingPager;
    }

    private View createSuccessView() {
        Logger.d("createSuccessView");
        if (successView==null){
            successView = LayoutInflater.from(App.app).inflate(R.layout.item_content_home, null);
            gvModule = (GridView) successView.findViewById(R.id.gv_module);
            homePtr = (PtrFrameLayout) successView.findViewById(R.id.home_ptr);
        }
        return successView;
    }


    private void onErrorPageClick() {
        getShowModuleData();
    }

//    逻辑代码=========================================

    @Override
    protected void onStart() {
        super.onStart();
        getShowModuleData();

    }

    private void getShowModuleData() {
        if (loadingPager!=null){
            loadingPager.showLoading();
        }
        Observable<List<OrganizationService>> observable  = getBelongOasObser().concatMap(this::getOasServiceObser);
        addSubscription(observable, new PgSubscriber<List<OrganizationService>>() {
            @Override
            public void on_Next(List<OrganizationService> modules) {
                if (modules.size()==0){
                    loadingPager.showEmpty();
                    return;
                }
                dealModuleData(modules);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                loadingPager.showError();
            }
        });
    }



    private Observable<List<OrganizationAccount>> getBelongOasObser() {
        return ApiFactory.getOrganizationList(userId,null);
    }


    private Observable<List<OrganizationService>>  getOasServiceObser(List<OrganizationAccount> oass){
        dealBelongOas(oass);
        String url = StringUtils.getCheckedOrgaId(orgaId) + Constants.special_orgi_service;
        return ApiFactory.getOrgiServices(url);
    }


    /**
    *处理modules数据
    */
    private void dealModuleData(List<OrganizationService> modules) {
        List<OrganizationService> configModules =  filteIsConfigModules(modules);
        this.modules = configModules;
        showModules(this.modules);
    }

    /**
    *展示modules
    */
    private void showModules(List<OrganizationService> modules) {
        loadingPager.showSuccess();
        showCurrentOas(currentOasName);
        if (adapter==null){
            adapter = new ListHomeModulesAdapter(modules,R.layout.item_gv_home);
//            gvModule.seto
        }

        setGvNoduleCoums(modules);
        gvModule.setAdapter(adapter);
    }


    private void setGvNoduleCoums(List<OrganizationService> modules) {
        gvModule.setNumColumns(modules.size()>1?2:1);
    }


    /**
    *过滤setting中isConfig为false的module
    */
    private List<OrganizationService> filteIsConfigModules(List<OrganizationService> modules) {
        for(int i=0;i<modules.size();i++){
            if (!isConfig(modules.get(i))) {
                modules.remove(i);
                i--;
            }
        }
        return modules;
    }


    /**
    *seting项的isconfig字段
    */
    public boolean isConfig(OrganizationService os){
        Map<String, String> settings = (Map<String, String>) os.getSettings();
        String isConfig = settings.get(KnownServices.ISCONFIG_Service);
        if ( !TextUtils.isEmpty(isConfig) && !Boolean.parseBoolean(isConfig)){
            return false;
        }
        return true;
    }

    /**
    *处理获取到的所属组织数据
    */
    private void dealBelongOas(List<OrganizationAccount> oass) {
        int belongOasIndex=0;
        for(int i=0;i<oass.size();i++){
            if (oass.get(i).isIsDefault()){
                belongOasIndex=i;
            }
        }
        CacheUtils.putCache(BelongOass.oass, oass);
        putBelongOasToSp(oass.get(belongOasIndex));
    }

    /**
    *把默认所在组织的id和name存进sp
    */
    private void putBelongOasToSp(OrganizationAccount oa) {
        String organizationId = oa.getOrganizationId();
        String id = StringUtils.belongOgaId(organizationId);
        String name = StringUtils.belongOgaName(organizationId);
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name)) {
            id = Admin.id;
            name = Admin.name;
        }
        Logger.d("belong_oas_id = "+id+"-------"+"belong_oas_name = "+name);
        SpUtils.setString(BelongOass.oas_id, id);
        SpUtils.setString(BelongOass.oas_name, name);

        //当前组织就是默认的所属组织
        SpUtils.setString(CurrentOas.id, id);
        SpUtils.setString(CurrentOas.name, name);


        setCurrentOasData(id,name);
    }

//    响应事件 =================================================

    @OnClick(R.id.home_setting)
    void openSystemConfig(){
        if (isLoadSuccess()){
            if (isAdmin()){
                IntentUtils.startAct(this,SystemConfigActivity.class);
            }else{
                this.checkPermission(KnownServices.SysConfig_Service+ KnowPermission.viewPermission,KnownServices.Module_Service);
            }
        }
    }

    @Subscribe
    public void openWhichActivity(HomeEvent event){
        String openWhichActivity = event.getOpenWhichActivity();
       IntentUtils.startModuleActivity(this,openWhichActivity);
    }


//    ==============================================================================


    /**
    *检查是否有权限
    */
    private void checkPermission(String permission,String serviceid) {
        addSubscription(ApiFactory.hasPermission(userId, permission), new PgSubscriber<Boolean>(this) {
            @Override
            public void on_Next(Boolean hasPermission) {
                if (hasPermission){
                    EventBus.getDefault().post(new HomeEvent(serviceid));
                }else{
                    SnackbarHelper.makeShort(coordinatorLayout,App.app.getString(R.string.no_permission), Color.RED);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                SnackbarHelper.makeShort(coordinatorLayout,App.app.getString(R.string.check_permission_false), Color.RED);
            }
        });
    }





    /**
    *设置当前组织id和name
    */
    private void setCurrentOasData(String id, String name) {
        this.currentOasId = id;
        this.currentOasName = name;
    }


    /**
    *界面显示当前组织
    */
    private void showCurrentOas(String name) {
        tvDeforgi.setText(name);
    }


    /**
    *停止刷新
    */
    private void stopReflash(){
        if (homePtr!=null && homePtr.isRefreshing()){
            homePtr.refreshComplete();
        }
    }

    /**
    *是否加载成功
    */
    private boolean isLoadSuccess(){
        return !TextUtils.isEmpty(tvDeforgi.getText());
    }


}
