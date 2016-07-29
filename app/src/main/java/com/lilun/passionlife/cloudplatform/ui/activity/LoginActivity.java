package com.lilun.passionlife.cloudplatform.ui.activity;

import android.text.TextUtils;
import android.widget.EditText;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseActivity;
import com.lilun.passionlife.cloudplatform.bean.Account;
import com.lilun.passionlife.cloudplatform.bean.LoginRes;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_pwd)
    EditText etPwd;


    private String spKey = "logined";
    private String authrovity = "";
    private String username;
    private String username_next;


    @Override
    public int setContentView() {

        return R.layout.activity_login;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //authrovity 是否因为token失效跳转到login
        authrovity = getIntent().getStringExtra(TokenManager.AUTHROVITY);

        boolean isLogined = SpUtils.getBoolean(spKey);
        username_next = SpUtils.getString("username");

        //不是首次登陆，跳转首页
        if (isLogined  && TextUtils.isEmpty(authrovity)) {
            IntentUtils.startAct(mAc, HomeActivity.class);
            finish();
        }

        if (username_next != null) {
            etUsername.setText(username_next);
        }

    }

    /**
     * 登录
     */
    @OnClick(R.id.login)
    public void login() {
        username = etUsername.getText().toString();
        String pwd = etPwd.getText().toString();
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(pwd);
        if (checkUser(username) && checkPassword(pwd)) {
            addSubscription(ApiFactory.login(account), new PgSubscriber<LoginRes>(mAc) {
                @Override
                public void on_Next(LoginRes loginRes) {
                    then(loginRes);
                }
            });

        }


    }

    /**
     * 登录成功后续操作
     */
    private void then(LoginRes loginRes) {
        ToastHelper.get(mAc).showShort(R.string.login_success);
        setAccountInf(loginRes);
        if (TextUtils.isEmpty(authrovity) || !username.equals(username_next)) {
            //是否超级用户
            IntentUtils.startAct(mAc, HomeActivity.class);
            isAdmin(loginRes.getUserId());
        } else {
            finish();
        }


    }


    /**
     * 查看是否超级用户，存进本地
     *
     * @param userId
     */
    public void isAdmin(int userId) {
//        addSubscription(ApiFactory.hasPermission(userId, Constants.ADMIN), new PgSubscriber<Boolean>(mAc) {
//            @Override
//            public void on_Next(Boolean hasPermisson) {
//                SpUtils.setBoolean(Constants.ADMIN, hasPermisson);
//            }
//        });
        SpUtils.setBoolean(Constants.ADMIN, username.equals("admin"));
    }


    /**
     * 存储登录信息
     */
    private void setAccountInf(LoginRes loginRes) {
        Logger.d("存储登录信息");
        SpUtils.setBoolean(spKey, true);
        SpUtils.setString("username", username);
        SpUtils.setString(TokenManager.TOKEN, loginRes.getId());
        SpUtils.setString(TokenManager.CREATED, loginRes.getCreated());
        SpUtils.setInt(TokenManager.TTL, loginRes.getTtl());
        SpUtils.setInt(TokenManager.USERID, loginRes.getUserId());
    }


    private boolean checkPassword(String pwd) {
        return true;
    }

    private boolean checkUser(String username) {
        return true;
    }

    /**
     * 注册
     */
    @OnClick(R.id.regest)
    public void regest() {
        IntentUtils.startAct(mAc, RegestActivity.class);
    }


}
