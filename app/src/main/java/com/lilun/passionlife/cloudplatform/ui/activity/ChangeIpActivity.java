package com.lilun.passionlife.cloudplatform.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

public class ChangeIpActivity extends BaseFunctionActivity {


    @Bind(R.id.et_ip)
    EditText etIp;



    @Override
    public View setContent() {
        return inflater.inflate(R.layout.activity_change_ip, null);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        title.setText("切换地址");
        etIp.setHint(Constants.BASE_URL);
    }

    @OnClick(R.id.saveIp)
    void save(){
        String ip = etIp.getText().toString();
        if (!TextUtils.isEmpty(ip)){
            Constants.BASE_URL = "http://"+ip+"/api/";
            SpUtils.clearSp();
            EventBus.getDefault().post(new Event.AuthoriseEvent());
//            ApiFactory.setService();
            finish();
        }
    }
}
