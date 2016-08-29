package com.lilun.passionlife.cloudplatform.ui.login;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.Account;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.common.User;
import com.lilun.passionlife.cloudplatform.custom_view.ProgressDialog;
import com.lilun.passionlife.cloudplatform.ui.home.HomeView;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;
import com.lilun.passionlife.cloudplatform.utils.SnackbarHelper;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/29.
 */
public class LoginView extends AppCompatActivity implements LoginConstract.View {
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_pwd)
    EditText etPwd;
    @Bind(R.id.regest)
    Button regest;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    private ProgressDialog pd;
    private LoginPresenter presenter;
    private String authrovity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        presenter = new LoginPresenter(this, new LoginDataSourceImp());

    }

    @Override
    protected void onStart() {
        super.onStart();
        authrovity = getIntent().getStringExtra(TokenManager.AUTHROVITY);
        String username = SpUtils.getString(User.username);
        if (!TextUtils.isEmpty(username)&& authrovity==null){
            startHomeActivity();
            finish();
        }else{
            etUsername.setText(StringUtils.filteEmpty(username));
        }
    }

    @Override
    public void setPresenter(LoginConstract.Presenter presenter) {

    }

    @Override
    public void showLoading() {
        initProgressDialog();
    }

    @Override
    public void hintLoading() {
        dismissProgressDialog();
    }

    @Override
    public void showErrorSnack(String s) {
        SnackbarHelper.makeShort(coordinatorLayout, s, Color.RED);
    }

    @Override
    public void showNormalSnack(String s) {
        SnackbarHelper.makeShort(coordinatorLayout, s);
    }

    @OnClick(R.id.login)
    void login() {
        String username = etUsername.getText().toString();
        String pwd = etPwd.getText().toString();
        if (checkUser(username) && checkPassword(pwd)) {
            Account account = new Account();
            account.setUsername(username);
            account.setPassword(pwd);
            presenter.login(account);
        }
    }

    public void startHomeActivity() {
//        LoginPresenter
        Logger.d("start homeactivity");
        IntentUtils.startAct(this, HomeView.class);
    }

    private boolean checkPassword(String pwd) {
        return true;
    }

    private boolean checkUser(String username) {
        return true;
    }


    private void initProgressDialog() {
        if (pd == null) {
            pd = new ProgressDialog(this, "加载中...");
            pd.setCancelable(false);
        }
        pd.showDialog();


    }

    private void dismissProgressDialog() {
        if (pd != null) {
            pd.dissmissDialog();
            pd = null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

    }
}
