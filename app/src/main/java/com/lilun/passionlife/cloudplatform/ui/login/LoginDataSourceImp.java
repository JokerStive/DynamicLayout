package com.lilun.passionlife.cloudplatform.ui.login;

import android.support.annotation.NonNull;

import com.lilun.passionlife.cloudplatform.BaseDataSourceImp;
import com.lilun.passionlife.cloudplatform.bean.Account;
import com.lilun.passionlife.cloudplatform.bean.LoginRes;
import com.lilun.passionlife.cloudplatform.common.KnowPermission;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;

/**
 * Created by Administrator on 2016/8/29.
 */
public class LoginDataSourceImp extends BaseDataSourceImp implements LoginDataSource {


    @Override
    public void login(@NonNull Account account, @NonNull LoginCallback callback) {
        addSubscription(ApiFactory.login(account), new PgSubscriber<LoginRes>() {
            @Override
            public void on_Next(LoginRes loginRes) {
                callback.onLogin(loginRes);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                callback.onLoginError();
            }
        });
    }

    @Override
    public void checkIsAdmin(int userId, @NonNull checkIsAdminCallback callback) {
        addSubscription(ApiFactory.hasPermission(userId, KnowPermission.adminPermission), new PgSubscriber<Boolean>() {
            @Override
            public void on_Next(Boolean isAdmin) {
                callback.onCheckIsAdmin(isAdmin);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                callback.onIsAdminError();
            }
        });
    }
}
