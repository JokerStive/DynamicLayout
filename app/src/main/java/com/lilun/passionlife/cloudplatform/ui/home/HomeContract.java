package com.lilun.passionlife.cloudplatform.ui.home;

import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;

import java.util.List;

/**
 * Created by Administrator on 2016/8/26.
 */
public interface HomeContract {
    interface View extends BaseView<Presenter> {


        void openWhichActivity(String moduleId);



        void showErrorPage();

        void showData(List<OrganizationService> noduls);

        void stopReflashLoading();


    }

    interface Presenter extends BasePresenter {

        void loadModules(String url);

        void checkPermission(String module);

        void dealBelongOass(List<OrganizationAccount> oass);

        void dealModules(List<OrganizationService> modules);
    }
}
