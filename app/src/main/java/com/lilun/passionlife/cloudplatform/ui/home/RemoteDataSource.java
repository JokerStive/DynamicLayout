package com.lilun.passionlife.cloudplatform.ui.home;

import android.support.annotation.NonNull;

import com.lilun.passionlife.cloudplatform.BaseDataSourceImp;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.common.KnowPermission;
import com.lilun.passionlife.cloudplatform.common.constans.BelongOass;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.utils.CacheUtils;

import java.util.List;

/**
 * Created by youke on 2016/8/26.
 */
public class RemoteDataSource extends BaseDataSourceImp implements HomeDataSource {

    @Override
    public void getBelongOass(@NonNull double userId, @NonNull GetBelongOassCallback callback) {
        if (CacheUtils.getCache(BelongOass.oass)!=null){
            List<OrganizationAccount> oass = (List<OrganizationAccount>) CacheUtils.getCache(BelongOass.oass);
            callback.OnGetBelongOass(oass);
            return;
        }
        addSubscription(ApiFactory.getOrganizationList(userId, null), new PgSubscriber<List<OrganizationAccount>>() {
            @Override
            public void on_Next(List<OrganizationAccount> oass) {
                callback.OnGetBelongOass(oass);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                callback.OnGetBelongOassError();
            }
        });
    }

    @Override
    public void getModules(@NonNull String url, @NonNull GetModulesCallback callback) {
        if (CacheUtils.getCache(BelongOass.modules)!=null){
            List<OrganizationService> modules = (List<OrganizationService>) CacheUtils.getCache(BelongOass.modules);
            callback.OnGetModules(modules);
            return;
        }
        addSubscription(ApiFactory.getOrgiServices(url), new PgSubscriber<List<OrganizationService>>() {
            @Override
            public void on_Next(List<OrganizationService> modules) {
                callback.OnGetModules(modules);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                callback.OnGetModulesErroe();
            }
        });
    }

    @Override
    public void checkModuleViewPermission(@NonNull double userId, @NonNull String module,@NonNull CheckModuleViewPermissionCallback callback) {
        addSubscription(ApiFactory.hasPermission(userId, module + KnowPermission.viewPermission), new PgSubscriber<Boolean>() {
            @Override
            public void on_Next(Boolean hasPermission) {
                callback.OnPermission(hasPermission,module);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                callback.OnCheckPermissionErroe();
            }
        });
    }
}
