package com.lilun.passionlife.cloudplatform.ui.activity;

import android.view.View;
import android.widget.Button;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionActivity;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;

import butterknife.Bind;

public class RegestActivity extends BaseFunctionActivity {


    @Bind(R.id.reg_nick_name)
    RegItemView regNickName;
    @Bind(R.id.reg_username)
    RegItemView regUsername;
    @Bind(R.id.btn_auth_code)
    Button btnAuthCode;
    @Bind(R.id.reg_auth_code)
    RegItemView regAuthCode;
    @Bind(R.id.reg_pwd)
    RegItemView regPwd;

    @Override
    public View setContent() {
        title.setText(getString(R.string.user_reg));
        return inflater.inflate(R.layout.activity_regest, null);
    }


}
