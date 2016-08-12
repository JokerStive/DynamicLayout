package com.lilun.passionlife.cloudplatform.ui.activity;

import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.SystemConfigGvAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.ACache;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class SystemConfigActivity extends BaseFunctionActivity {


    @Bind(R.id.gv_service)
    GridView gvService;
    private boolean hasModuleService;
    private List<OrganizationService> visibleOrgiService;



    @Override
    public View setContent() {
        title.setText(getString(R.string.system_config));
        View view = inflater.inflate(R.layout.activity_system_config, null);

        return view;
    }

    @Override
    public void onCreate() {
        def_org.setText(SpUtils.getString(Constants.key_currentOrgaName));
        def_org.setVisibility(View.VISIBLE);
        def_org.setOnClickListener(v -> {
            IntentUtils.startAct(mAc,ChangeOrganizationActivity.class);
        });
        setInitData();
    }

    /**
    *默认所属组织切换，首页的os数据缓存成功，可以刷新视图了
    */
    @Subscribe
    public void currentOsHasCached(Event.CurrentOsHasCached event){
        def_org.setText(SpUtils.getString(Constants.key_currentOrgaName));
        setInitData();
    }


    /**
     * 切换了旗下的组织
     */
    @Subscribe
    public void changeChildOrganization(Event.ChangeChildOrganization event) {
        //切换了当前的组织，刷新sp里面的orgaId和orgaName
        def_org.setText(event.getOrganizationName()==null?"":event.getOrganizationName());


    }


    /**
    *设置初始化数据，从缓存（首页保存的）数据中加载数据
    */
    private void setInitData() {
        OrganizationService os = new OrganizationService();
        os.setTitle(App.app.getString(R.string.module_manager));
        os.setDescription(App.app.getString(R.string.module_manager_desc));
        os.setServiceId(KnownServices.Module_Service);
        visibleOrgiService = (List<OrganizationService>) ACache.get(App.app).getAsObject(Constants.cacheKey_service);

        if (visibleOrgiService != null ) {
            for(int i=0;i<visibleOrgiService.size();i++){
                if (visibleOrgiService.get(i).getServiceId().equals(KnownServices.Module_Service)){
                    hasModuleService=true;
                }
            }
            if (!hasModuleService){
                visibleOrgiService.add(os);
            }
            showServices(visibleOrgiService);
        } else {
            visibleOrgiService = new ArrayList<>();
            visibleOrgiService.add(os);
            showServices(visibleOrgiService);

        }
    }


    private void showServices(List<OrganizationService> data) {
        gvService.setNumColumns(data.size() == 1 ? 1 : 2);
        gvService.setAdapter(new SystemConfigGvAdapter(mCx, data));
        gvService.setOnItemClickListener((parent, view, position, id) -> {
            String serviceId = data.get(position).getServiceId();
            IntentUtils.startModuleActivity(mAc,serviceId);
            finishThis();
        });
    }



    public void finishThis(){
        finish();
    }


}
