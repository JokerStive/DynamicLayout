package com.lilun.passionlife.cloudplatform.ui.activity;

import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.SystemConfigGvAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionActivity;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.custom_view.ShowOrgiPopupwindow;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.ACache;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrFrameLayout;


public class SystemConfigActivity extends BaseFunctionActivity {


    @Bind(R.id.gv_service)
    GridView gvService;
    @Bind(R.id.home_ptr)
    PtrFrameLayout homePtr;
    private List<Organization> data = null;
    private ShowOrgiPopupwindow pop;
    private String childOrginaId;
    private String childOrginaName;
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
        OrganizationService os = new OrganizationService();
        os.setTitle(App.app.getString(R.string.module_manager));
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


            //模块管理
            if (serviceId.equals(KnownServices.Module_Service)){
                IntentUtils.startAct(mAc, ModuleManagerActivity.class);
                finishThis();
            }

            //员工管理
            else if(serviceId.equals(KnownServices.Account_Service)){
                IntentUtils.startAct(mAc, StaffManagerActivity.class);
                finishThis();
            }

            //角色管理
            else  if(serviceId.equals(KnownServices.Role_Service)){
                IntentUtils.startAct(mAc, RoleManagerActivity.class);
                finishThis();
            }

            //组织机构管理
            else if(serviceId.equals(KnownServices.Organization_Service)){
                IntentUtils.startAct(mAc, OrganizationActivity.class);
                finishThis();
            }

            //系统配置
            else if(serviceId.equals(KnownServices.SysConfig_Service)){
                IntentUtils.startAct(mAc, SystemConfigActivity.class);
                finishThis();
            }



            //部门管理
            else if(serviceId.equals(KnownServices.Department_Service)){
                IntentUtils.startAct(mAc, DeptManagerActivity.class);
                finishThis();
            }
        });
    }


    public void finishThis(){
        finish();
    }


}
