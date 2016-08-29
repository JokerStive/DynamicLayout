package com.lilun.passionlife.cloudplatform.ui.login;

import com.lilun.passionlife.cloudplatform.bean.Account;
import com.lilun.passionlife.cloudplatform.bean.LoginRes;
import com.lilun.passionlife.cloudplatform.ui.home.BasePresenter;
import com.lilun.passionlife.cloudplatform.ui.home.BaseView;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface LoginConstract {
    interface View extends BaseView<Presenter>{

    }


    interface Presenter extends BasePresenter{
        void login(Account account);
        void checkIsAdmin(int userId);
        void setUserDataToSp(LoginRes userData);
    }
}
