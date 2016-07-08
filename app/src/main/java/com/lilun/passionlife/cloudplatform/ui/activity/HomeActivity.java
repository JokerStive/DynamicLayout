package com.lilun.passionlife.cloudplatform.ui.activity;

import android.text.TextUtils;
import android.widget.GridView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.HomeGvAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.ACache;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {


    @Bind(R.id.gv_module)
    GridView gvModule;
    @Bind(R.id.tv_deforgi)
    TextView tvDeforgi;

    private int userId;
    private String defOrgaId = "";
    private String defOrgaName = "";
    private List<OrganizationService> visibleOrgiService;
    private ACache aCache;


    @Override
    public int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate() {
        aCache = ACache.get(App.app);
        userId = SpUtils.getInt(TokenManager.USERID);
        if (userId != -1) {
            //TODO 展示进度条
            getorgiList();
        }


    }

    /**
     * 个人中心
     */
    @OnClick(R.id.home_personal)
    void personalInfo() {

    }


    /**
     * 系统设置
     */
    @OnClick(R.id.home_setting)
    void setting() {
//        if (isAdmin()) {
//            next(true);
//            return;
//        }
//
//        if (visibleOrgiService.size() != 0) {
//            for (OrganizationService sercice : visibleOrgiService) {
//                if (sercice.getServiceId().equals(systemConfig)) {
//                    next(true);
//                }
//            }
//        }
        next(true);
    }


    /**
     * 根据权限执行操作
     */
    public void next(Boolean hasPermisson) {
        if (hasPermisson) {
            IntentUtils.startAct(mAc, SystemConfigActivity.class);
        } else {
            ToastHelper.get(mCx).showShort(mCx.getString(R.string.no_view_permissoion));
        }
    }


    /**
     * 获取组织列表
     */
    private void getorgiList() {
        defOrgaName= SpUtils.getString(Constants.key_defOrgina);
        if (!TextUtils.isEmpty(defOrgaName)){
            getServiceList();
            tvDeforgi.setText(defOrgaName);
            return;
        }
        addSubscription(ApiFactory.getOrganizationList(userId), new PgSubscriber<List<OrganizationAccount>>(mAc) {
            @Override
            public void on_Next(List<OrganizationAccount> organizations) {
                if (organizations.size() == 0) {
                    ToastHelper.get(mCx).showShort(getString(R.string.empty_orgi_list));
                } else {
                    //获取到组织后，把默认组织的id存进本地，post事件方便系统配置界面使用
                    for (OrganizationAccount organization : organizations) {
                        if (organization.isIsDefault()) {
                            defOrgaId = organization.getOrganizationId();

//                            defOrgaName = organization.getOrganization().getName();
//                            tvDeforgi.setText(defOrgaName);
                            SpUtils.setString(Constants.key_defOrginaId, defOrgaId);
//                            SpUtils.setString(Constants.key_defOrgina,defOrgaName);
                            getOrganization(defOrgaId);
                            getServiceList();
                            //根据id获取对应的组织

                        }
                    }
                }
            }
        });

    }

    /**
     *根据id获取对应的组织
     * @param defOrgaId
     */

    private void getOrganization(String defOrgaId) {
        addSubscription(ApiFactory.getOrganization(defOrgaId), new PgSubscriber<Organization>(mAc) {
            @Override
            public void on_Next(Organization organizationBean) {
                defOrgaName = organizationBean.getName();
                SpUtils.setString(Constants.key_defOrgina,defOrgaName);
                tvDeforgi.setText(defOrgaName);
            }

        });
    }

    /**
     * 异步获取服务列表
     */
    public void getServiceList() {

//        getSerListFromNet(visibleOrgiService1 -> {
//            visibleOrgiService = visibleOrgiService1;
//        });
//        从缓存获取
        visibleOrgiService = (List<OrganizationService>) aCache.getAsObject(Constants.cacheKey_service);
        if (visibleOrgiService == null) {
            Logger.d("get service  from net");
            getSerListFromNet(visibleOrgiService1 -> {
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
    public void addNewService(Event.addNewService event) {
        Logger.d("get add new service");
        OrganizationService service = event.getService();
        if (visibleOrgiService != null) {
            for (int i = 0; i < visibleOrgiService.size(); i++) {
                if (visibleOrgiService.get(i).getId().equals(service.getId())) {
                    visibleOrgiService.remove(i);
                }
            }
            visibleOrgiService.add(service);
            aCache.put(Constants.cacheKey_service, (Serializable) visibleOrgiService);
            showServices(visibleOrgiService);
        } else {
            Logger.d("visibleOrgiService is  null");
        }

    }

    /**
     * 删除了一个服务，从网络获取最新的服务，刷新缓存
     */
    @Subscribe
    public void deleteOrganiService(Event.deleteOrganiService event) {
        getSerListFromNet(visibleOrgiService1 -> {
            showServices(visibleOrgiService1);
        });
    }


    private void showServices(List<OrganizationService> data) {
        gvModule.setNumColumns(data.size() == 1 ? 1 : 2);
        gvModule.setAdapter(new HomeGvAdapter(mCx, data));
        gvModule.setOnItemClickListener((parent, view, position, id) -> {
            if (data.get(position).getServiceId()!=null && data.get(position).getServiceId().equals("SysConfig")) {

//                Logger.d(StringUtils.randow()+"");
                IntentUtils.startAct(mAc, SystemConfigActivity.class);
            }
        });
    }


}
