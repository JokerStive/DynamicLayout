package com.lilun.passionlife.cloudplatform.ui.activity;

import android.text.TextUtils;
import android.widget.EditText;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseActivity;
import com.lilun.passionlife.cloudplatform.bean.Account;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.LoginRes;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.KnowPermission;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.common.User;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.home.HomeView;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

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
    private boolean isLogined;


    @Override
    public int setContentView() {

        return R.layout.activity_login;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //authrovity 是否因为token失效跳转到login
        authrovity = getIntent().getStringExtra(TokenManager.AUTHROVITY);

        isLogined = SpUtils.getBoolean(spKey);
        username_next = SpUtils.getString("username");

        //不是首次登陆，跳转首页
        if (isLogined && authrovity==null) {
//            IntentUtils.startAct(mAc, HomeActivity.class);
            IntentUtils.startAct(mAc, HomeView.class);
            EventBus.getDefault().post(new Event.LoginSuccess());
            finish();
        }

        if (username_next != null ) {
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
        if (!TextUtils.isEmpty(authrovity) && isLogined){
            finish();
        }else{
            SpUtils.setBoolean(spKey, true);
//            IntentUtils.startAct(mAc, HomeActivity.class);
            IntentUtils.startAct(mAc, HomeView.class);
            isAdmin(loginRes.getUserId());
            EventBus.getDefault().post(new Event.LoginSuccess());
            finish();
        }

    }


    /**
     * 查看是否超级用户，存进本地
     *
     * @param userId
     */
    public void isAdmin(int userId) {
        checkHasPermission(userId, KnowPermission.adminPermission, hasPermission -> {
                SpUtils.setBoolean(Constants.ADMIN, hasPermission);
        });
    }


    /**
     * 存储登录信息
     */
    private void setAccountInf(LoginRes loginRes) {
        Logger.d("存储登录信息");


//        SpUtils.setString(User.username, StringUtils.filteEmpty(loginRes.get));
        SpUtils.setString(User.name, username);
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
        IntentUtils.startAct(mAc, ChangeIpActivity.class);
    }


}
