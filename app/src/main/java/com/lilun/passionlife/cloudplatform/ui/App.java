package com.lilun.passionlife.cloudplatform.ui;

import android.app.Application;
import android.content.Context;

import com.lilun.passionlife.cloudplatform.common.Constants;
import com.orhanobut.logger.Logger;

import st.lowlevel.storo.StoroBuilder;

/**
 * Created by Administrator on 2016/6/14.
 */
public class App extends Application {
    public static  Context app;

    @Override
    public void onCreate() {
        super.onCreate();
        app=getApplicationContext();

        initLogger();

        StoroBuilder.configure(10240)  // maximum size to allocate in bytes
                .setDefaultCacheDirectory(this)
                .initialize();
    }

    private void initLogger() {
        Logger.init(Constants.LOGGER_TAG).methodCount(1).hideThreadInfo();
    }
}
