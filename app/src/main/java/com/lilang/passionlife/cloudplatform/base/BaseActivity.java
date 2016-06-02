package com.lilang.passionlife.cloudplatform.base;


import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/5/19.
 */
public class BaseActivity extends AppCompatActivity{
    protected CompositeSubscription subscription=new CompositeSubscription();
    protected  Application mCx=getApplication();




}
