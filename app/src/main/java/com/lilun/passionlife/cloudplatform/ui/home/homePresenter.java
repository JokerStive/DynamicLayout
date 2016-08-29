package com.lilun.passionlife.cloudplatform.ui.home;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.common.Admin;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.common.constans.BelongOass;
import com.lilun.passionlife.cloudplatform.common.constans.CurrentOas;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.CacheUtils;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by youke on 2016/8/26.
 */
public class HomePresenter implements HomeContract.Presenter,HomeDataSource.GetBelongOassCallback,HomeDataSource.GetModulesCallback,HomeDataSource.CheckModuleViewPermissionCallback {

    private HomeDataSource mHomeDataSource;
    private  HomeContract.View mHomeView;
    private  double mUserId;

    public HomePresenter(@NonNull double userId, @NonNull HomeDataSource homeDataSource, @NonNull HomeContract.View homeView) {
        this.mUserId = userId;
        this.mHomeDataSource = homeDataSource;
        this.mHomeView = homeView;
    }

    @Override
    public void start() {
        mHomeDataSource.getBelongOass(mUserId,this);
    }




    @Override
    public void loadModules(String url) {
        mHomeDataSource.getModules(url,this);
    }


    @Override
    public void OnPermission(Boolean hasPermission, String module) {
        mHomeView.hintLoading();
        if (hasPermission){
            mHomeView.openWhichActivity(module);
        }else{
            mHomeView.showErrorSnack(App.app.getString(R.string.no_permission));
        }
    }

    @Override
    public void OnCheckPermissionErroe() {
        mHomeView.hintLoading();
        mHomeView.showErrorSnack(App.app.getString(R.string.check_permission_false));
    }

    @Override
    public void OnGetBelongOass(List<OrganizationAccount> oass) {
        dealBelongOass(oass);
        String url = StringUtils.getCheckedOrgaId(SpUtils.getString(Constants.key_currentOrgaId)) + Constants.special_orgi_service;
        mHomeDataSource.getModules(url,this);
    }

    @Override
    public void OnGetBelongOassError() {
        mHomeView.showErrorSnack(App.app.getString(R.string.get_belong_orga_false));
        mHomeView.showErrorPage();
        mHomeView.stopReflashLoading();
    }

    @Override
    public void OnGetModules(List<OrganizationService> modules) {
        mHomeView.stopReflashLoading();
        dealModules(modules);
    }

    @Override
    public void OnGetModulesErroe() {
        mHomeView.showErrorSnack(App.app.getString(R.string.get_orga_service_false));
        mHomeView.showErrorPage();
        mHomeView.stopReflashLoading();
    }


    @Override
    public void checkPermission(String module) {
        mHomeView.showLoading();
        mHomeDataSource.checkModuleViewPermission(mUserId,module,this);
    }

    @Override
    public void dealBelongOass(List<OrganizationAccount> oass) {
        int belongOasIndex=0;
        for(int i=0;i<oass.size();i++){
            if (oass.get(i).isIsDefault()){
                belongOasIndex=i;
            }
        }
        setBelongOasToCache(oass);
        setDataToSp(oass.get(belongOasIndex));
    }



    @Override
    public void dealModules(List<OrganizationService> modules) {
        List<OrganizationService> configModules =  filteIsConfigModules(modules);
//        setModulesToCache(modules);
        mHomeView.showData(configModules);
    }

    /**
    *存cache--默认所属组织
    */
    public void setBelongOasToCache(List<OrganizationAccount> oass) {
        CacheUtils.putCache(BelongOass.oass, oass);
    }


    /**
    *存cache--当前所在组织的modules
    */
    public void setModulesToCache(List<OrganizationService> modules) {
        CacheUtils.putCache(BelongOass.modules, modules);
    }


    /**
     *把默认所在组织的id和name存进sp
     */
    private void setDataToSp(OrganizationAccount oa) {
        String organizationId = oa.getOrganizationId();
        String id = StringUtils.belongOgaId(organizationId);
        String name = StringUtils.belongOgaName(organizationId);
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name)) {
            id = Admin.id;
            name = Admin.name;
        }
        Logger.d("belong_oas_id = "+id+"-------"+"belong_oas_name = "+name);
        setBelongOasToSp(id, name);

        setCurrentOasToSp(id,name);
    }

    /**
    *存sp--默认所属组织
    */
    public void setBelongOasToSp(String id, String name) {
        SpUtils.setString(BelongOass.oas_id, id);
        SpUtils.setString(BelongOass.oas_name, name);
    }


    /**
    *存sp--当前操作的组织
    */
    public void setCurrentOasToSp(String id, String name) {
        SpUtils.setString(CurrentOas.id, id);
        SpUtils.setString(CurrentOas.name, name);
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

}
