package com.lilun.passionlife.cloudplatform.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;
import com.lilun.passionlife.cloudplatform.utils.LogUtils;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ${hyz} on 2016/7/21.
 */
public class AccountSecurityActivity extends BaseFunctionActivity {


    @Bind(R.id.accounts_nub)
    TextView accountsNum;

    @Override
    public View setContent() {
        title.setText(getString(R.string.account_safe));
        return inflater.inflate(R.layout.activity_account_security,null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        accountsNum.setText(SpUtils.getString("username"));
        LogUtils.D("TAG",SpUtils.getString("username"));
    }

    @OnClick(R.id.change_password)
    public void changePassword(){
       IntentUtils.startAct(mAc,ModifyPasswordActivity.class);
   }

    /**
    *退出登录
    */
    @OnClick(R.id.logout)
    void logout(){
        //退出登录，信息重置
        SpUtils.setString(Constants.key_currentOrgaName,"");
        SpUtils.setString(Constants.key_currentOrgaId,"");
        SpUtils.setBoolean("logined",false);
        EventBus.getDefault().post(new Event.AuthoriseEvent());
    }
}
