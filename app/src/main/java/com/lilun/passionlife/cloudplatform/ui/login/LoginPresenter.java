package com.lilun.passionlife.cloudplatform.ui.login;

import android.support.annotation.NonNull;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.Account;
import com.lilun.passionlife.cloudplatform.bean.LoginRes;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.common.User;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by youke on 2016/8/29.
 */
public class LoginPresenter implements LoginConstract.Presenter,LoginDataSource.LoginCallback,LoginDataSource.checkIsAdminCallback{

    private  LoginDataSource mLoginDataSource;
    private  LoginView mLoginView;
    private String username;

    public LoginPresenter(@NonNull LoginView mLoginView, @NonNull LoginDataSource mLoginDataSource) {
        this.mLoginView = mLoginView;
        this.mLoginDataSource = mLoginDataSource;
    }

    @Override
    public void start() {

    }

    @Override
    public void login(Account account) {
        mLoginView.showLoading();
        this.username = account.getUsername();
        mLoginDataSource.login(account,this);
    }

    @Override
    public void checkIsAdmin(int userId) {
        mLoginDataSource.checkIsAdmin(userId,this);
    }

    @Override
    public void setUserDataToSp(LoginRes userData) {
        SpUtils.setString(User.username, username);
        SpUtils.setString(TokenManager.TOKEN, userData.getId());
        SpUtils.setString(TokenManager.CREATED, userData.getCreated());
        SpUtils.setInt(TokenManager.TTL, userData.getTtl());
        SpUtils.setInt(TokenManager.USERID, userData.getUserId());
    }


    @Override
    public void onLogin(LoginRes userData) {
        setUserDataToSp(userData);
        mLoginDataSource.checkIsAdmin(userData.getUserId(),this);
    }

    @Override
    public void onLoginError() {
        mLoginView.showErrorSnack(App.app.getString(R.string.login_false));
        mLoginView.hintLoading();
    }

    @Override
    public void onCheckIsAdmin(Boolean isAdmin) {
        setIsAdminToSp(isAdmin);
        mLoginView.hintLoading();
        Logger.d("loging presenter startHomeAct");
        mLoginView.startHomeActivity();
    }


    @Override
    public void onIsAdminError() {
        mLoginView.hintLoading();
        mLoginView.showErrorSnack("管理员权限检查失败");
    }



    private void setIsAdminToSp(Boolean isAdmin) {
        SpUtils.setBoolean(Constants.ADMIN, isAdmin);
    }



}
