package com.lilun.passionlife.cloudplatform.base;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.common.TokenManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * Created by youke on 2016/5/19.
 */
public abstract class BaseActivity extends BaseNetActivity {
    protected Application mCx;
    protected Activity mAc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentView());
        ButterKnife.bind(this);

        mCx = getApplication();
        mAc = this;
        EventBus.getDefault().register(this);

        onCreate();

    }

    public void onCreate(){};

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
    *接受token过期事件，跳转登录界面
    */
    public void login(Event.AuthoriseEvent event){
        TokenManager.translateLogin(mAc);
    }

    public abstract int  setContentView();

//    public void add

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onUnsubscribe();
        EventBus.getDefault().unregister(this);
    }
}
