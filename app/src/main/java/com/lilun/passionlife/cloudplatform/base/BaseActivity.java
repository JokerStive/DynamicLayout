package com.lilun.passionlife.cloudplatform.base;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

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


    public abstract int  setContentView();

//    public void add

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }
}
