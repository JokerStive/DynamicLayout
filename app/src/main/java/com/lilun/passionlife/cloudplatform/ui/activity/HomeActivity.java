package com.lilun.passionlife.cloudplatform.ui.activity;

import android.text.TextUtils;
import android.widget.GridView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.HomeGvAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.ACache;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
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

        MaterialHeader header = new MaterialHeader(mCx);
        header.setPtrFrameLayout(homePtr);
        header.onUIRefreshBegin(homePtr);
        homePtr.setHeaderView(header);
        homePtr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getBelongOrga((orgas, defOrgaId1, defOrgaName1) -> {
                    homePtr.refreshComplete();
                    tvDeforgi.setText(defOrgaName1);
                });
                getSerListFromNet(visibleOrgiService1 -> {
                    homePtr.refreshComplete();
                    showServices(visibleOrgiService1);
                });
            }
        });
    }

    /**
     * 个人中心
     */
    @OnClick(R.id.home_personal)
    void personalInfo() {

    }




    /**
     * 获取组织列表
     */
    private void getorgiList() {
        defOrgaName = SpUtils.getString(Constants.key_defOrgina);
        defOrgaId = SpUtils.getString(Constants.key_defOrginaId);
        if (!TextUtils.isEmpty(defOrgaName) && !TextUtils.isEmpty(defOrgaId)) {
            getServiceList();
            tvDeforgi.setText(defOrgaName);
        }else{
            getBelongOrga((orgas, defOrgaId1, defOrgaName1) -> {
                tvDeforgi.setText(defOrgaName);
                getServiceList();
            });
        }


    }



    /**
     * 异步获取服务列表
     */
    public void getServiceList() {

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
        getSerListFromNet(visibleOrgiService1 -> {
            visibleOrgiService = visibleOrgiService1;
            showServices(visibleOrgiService1);
        });
    }


    private void showServices(List<OrganizationService> data) {
        gvModule.setNumColumns(data.size() == 1 ? 1 : 2);
        gvModule.setAdapter(new HomeGvAdapter(mCx, data));
        gvModule.setOnItemClickListener((parent, view, position, id) -> {
            if (data.get(position).getServiceId() != null && data.get(position).getServiceId().equals("SysConfig")) {

//                Logger.d(StringUtils.randow()+"");
                IntentUtils.startAct(mAc, SystemConfigActivity.class);
            }
        });
    }


}
