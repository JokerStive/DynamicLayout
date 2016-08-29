package com.lilun.passionlife.cloudplatform.ui.home;

import android.support.annotation.NonNull;

import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;

import java.util.List;

/**
 * Created by youke on 2016/8/26.
 *
 */
public interface HomeDataSource {

    interface GetBelongOassCallback {
        void OnGetBelongOass(List<OrganizationAccount> oass);
        void OnGetBelongOassError();
    }


    interface GetModulesCallback {
        void OnGetModules(List<OrganizationService> modules);
        void OnGetModulesErroe();
    }

    interface CheckModuleViewPermissionCallback {
        void OnPermission(Boolean hasPermission,String module);
        void OnCheckPermissionErroe();
    }


    void getBelongOass(@NonNull double userId,@NonNull GetBelongOassCallback callback);

    void getModules(@NonNull String url,@NonNull GetModulesCallback callback);


    void checkModuleViewPermission(@NonNull double userId,@NonNull String module,@NonNull CheckModuleViewPermissionCallback callback);
}
