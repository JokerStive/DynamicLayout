package com.lilun.passionlife.cloudplatform.ui.login;

import com.lilun.passionlife.cloudplatform.bean.Account;
import com.lilun.passionlife.cloudplatform.bean.LoginRes;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface LoginDataSource {
    void login(Account account,LoginCallback callback);
    void checkIsAdmin(int userId,checkIsAdminCallback callback);

    interface LoginCallback{
        void onLogin(LoginRes userData);
        void onLoginError();
    }

    interface checkIsAdminCallback{
        void onCheckIsAdmin(Boolean isAdmin);
        void onIsAdminError();
    }
}
