package com.lilun.passionlife.cloudplatform.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.ListHomeModulesAdapter;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.bean.RxbusEvent;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.common.constans.CurrentOas;
import com.lilun.passionlife.cloudplatform.custom_view.LoadingPager;
import com.lilun.passionlife.cloudplatform.custom_view.ProgressDialog;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.ui.activity.ChangeOrganizationActivity;
import com.lilun.passionlife.cloudplatform.ui.activity.PersonalCenterActivity;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;
import com.lilun.passionlife.cloudplatform.utils.RxBus;
import com.lilun.passionlife.cloudplatform.utils.SnackbarHelper;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;
import rx.Subscription;

/**
 * Created by youke on 2016/8/26.
 */
public class HomeView extends AppCompatActivity implements HomeContract.View {


    @Bind(R.id.tv_deforgi)
    TextView tvDeforgi;


    @Bind(R.id.fl_container)
    FrameLayout flContainer;

    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    PtrFrameLayout homePtr;

    private double userId;
    private HomePresenter homePresenter;
    private LoadingPager loadingPager;
    private View successView;
    private GridView gvModule;
    private ListHomeModulesAdapter adapter;
    private List<OrganizationService> modules;
    private Subscription subscription;
    private ProgressDialog pd;
    private boolean isAdmin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        assert flContainer != null;
        flContainer.addView(createContentView());

        responseRxbusEvent();

        isAdmin = SpUtils.getBoolean(Constants.ADMIN);
        userId = (double) SpUtils.getInt(TokenManager.USERID);
        homePresenter = new HomePresenter(userId, new RemoteDataSource(), this);


        loadData();
    }


    private View createContentView() {
        if (loadingPager == null) {
            loadingPager = new LoadingPager(R.layout.page_empty, R.layout.page_error, R.layout.page_loading) {
                @Override
                protected View createSuccessView() {
                    return HomeView.this.createSuccessView();
                }
            };
            loadingPager.setOnErrorPageClickListener(this::onErrorPageClick);
        }
        loadingPager.showPagerView();
        return loadingPager;
    }


    private View createSuccessView() {
        if (successView == null) {
            successView = LayoutInflater.from(App.app).inflate(R.layout.item_content_home, null);
            gvModule = (GridView) successView.findViewById(R.id.gv_module);
            homePtr = (PtrFrameLayout) successView.findViewById(R.id.home_ptr);
            initReflashView();
        }
        return successView;
    }

    private void initReflashView() {
        MaterialHeader header = new MaterialHeader(this);
        header.setPtrFrameLayout(homePtr);
        header.onUIRefreshBegin(homePtr);
        homePtr.setHeaderView(header);
        homePtr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                homePresenter.setBelongOasToCache(null);
                homePresenter.setModulesToCache(null);
                homePresenter.start();
            }
        });
    }


    @Override
    public void showLoading() {
        if (pd == null) {
            pd = new ProgressDialog(this);
            pd.setCancelable(false);
        }
        pd.showDialog();
    }

    @Override
    public void hintLoading() {
        if (pd != null) {
            pd.dissmissDialog();
            pd = null;
        }
    }


    @Override
    public void openWhichActivity(String moduleId) {
        IntentUtils.startModuleActivity(this, moduleId);
    }

    @Override
    public void showErrorSnack(String s) {
        SnackbarHelper.makeShort(coordinatorLayout, s, Color.RED);
    }

    @Override
    public void showNormalSnack(String s) {
        SnackbarHelper.makeShort(coordinatorLayout, s);
    }

    @Override
    public void showErrorPage() {
        assert loadingPager != null;
        loadingPager.showError();
    }


    @Override
    public void showData(List<OrganizationService> modules) {
        this.modules = modules;
        setBelongOasName(SpUtils.getString(CurrentOas.name));
        showModules(modules);
    }

    @Override
    public void stopReflashLoading() {
        if (homePtr != null && homePtr.isRefreshing()) {
            homePtr.refreshComplete();
        }
    }


    @Override
    public void setPresenter(HomeContract.Presenter presenter) {

    }

//    点击事件响应=========================================

    @OnClick(R.id.home_setting)
    void startSystemConfig() {
        if (!TextUtils.isEmpty(tvDeforgi.getText())) {
            if (isAdmin) {
                openWhichActivity(KnownServices.SysConfig_Service);
            } else {
                homePresenter.checkPermission(KnownServices.SysConfig_Service);
            }
        }
    }

    @OnClick(R.id.tv_deforgi)
    void changeOrganization() {
        IntentUtils.startAct(this, ChangeOrganizationActivity.class);
    }

    @OnClick(R.id.home_personal)
    void startPersonalCenter() {
        if (!TextUtils.isEmpty(tvDeforgi.getText())) {
            IntentUtils.startAct(this, PersonalCenterActivity.class);
        }
    }

//    ============================================================

    /**
     * 响应rxbus事件
     */
    private void responseRxbusEvent() {
        subscription = RxBus.getDefault().toObservable().subscribe(event -> {
            if (event instanceof RxbusEvent.reflashModules) {
                homePresenter.setModulesToCache(null);
                String url = StringUtils.getCheckedOrgaId(SpUtils.getString(Constants.key_currentOrgaId)) + Constants.special_orgi_service;
                homePresenter.loadModules(url);

            } else if (event instanceof RxbusEvent.reflashOasAndModules) {
                homePresenter.setBelongOasToCache(null);
                homePresenter.setModulesToCache(null);
                homePresenter.start();

            } else if(event instanceof RxbusEvent.addModule){
                RxbusEvent.addModule addModule = (RxbusEvent.addModule) event;
                assert  addModule.getModule()!=null && modules!=null && adapter!=null;
                modules.add(addModule.getModule());
                showModules(modules);
                Logger.d("add a module");

            } else if(event instanceof RxbusEvent.editdModule){
                RxbusEvent.editdModule editdModule = (RxbusEvent.editdModule) event;
                assert  editdModule.getModule()!=null && modules!=null && adapter!=null;
                filterSameModule(editdModule.getModule());
                showModules(modules);
                Logger.d("edit a module");

            }else if(event instanceof RxbusEvent.deleteModule){
                RxbusEvent.deleteModule deleteModule = (RxbusEvent.deleteModule) event;
                assert  deleteModule.serviceId!=null && modules!=null && adapter!=null;
                deleteModule(deleteModule.serviceId);
                showModules(modules);
                Logger.d("delete a module");
            }
        });
    }

    /**
    * delete a module
    */
    private void deleteModule(String moduleId) {
        for(int i=0;i<modules.size();i++){
            if (moduleId.equals(modules.get(i).getId())){
                modules.remove(i);
                i--;
            }
        }
    }

    /**
    *filter sanme module
    */
    private void filterSameModule(OrganizationService module) {
        for(int i=0;i<modules.size();i++){
            if (module.getId().equals(modules.get(i).getId())){
                modules.remove(i);
                i--;
            }
        }
        modules.add(module);
    }


    /**
     * 开始加载数据
     */
    private void loadData() {
        loadingPager.showLoading();
        homePresenter.start();
    }


    /**
     * 错误页面点击重新加载
     */
    private void onErrorPageClick() {
        loadData();
    }


    /**
     * 设置grigview的行数
     */
    private void setGvNoduleCoums(List<OrganizationService> modules) {
        gvModule.setNumColumns(modules.size() > 1 ? 2 : 1);
    }


    /**
     * 设置显示当前的组织
     */
    public void setBelongOasName(String belongOasName) {
        tvDeforgi.setText(belongOasName);
    }


    /**
     * 显示modules
     */
    private void showModules(List<OrganizationService> modules) {
        homePresenter.setModulesToCache(modules);
        if (modules.size()==0){
            loadingPager.showEmpty();
            return;
        }
        loadingPager.showSuccess();
        setGvNoduleCoums(modules);
        if (adapter==null){
            adapter = new ListHomeModulesAdapter(modules,R.layout.item_gv_home);
            gvModule.setOnItemClickListener((parent, view, position, id) -> {
                String serviceId = modules.get(position).getServiceId();
                if (isAdmin) {
                    openWhichActivity(serviceId);
                    return;
                }
                homePresenter.checkPermission(serviceId);
            });
            gvModule.setAdapter(adapter);
        }else{
            Logger.d("notify data ");
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
