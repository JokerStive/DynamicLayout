package com.lilun.passionlife.cloudplatform.utils;

import com.lilun.passionlife.cloudplatform.ui.App;
import com.orhanobut.logger.Logger;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by youke on 2016/6/2.
 * 异常处理
 */
public class DealErrorUtils {
    public static void dealError(Throwable e){
        if(!NetUtil.checkNet(App.app)){
            ToastHelper.get(App.app).showShort("网络异常");
            return;
        }
        if (e instanceof HttpException){
            HttpException e1 = (HttpException) e;
            Logger.d(e1.getMessage());
            ToastHelper.get(App.app).showShort(e1.getMessage());
        }else{
            Logger.d(e.getMessage());
            ToastHelper.get(App.app).showShort(e.getMessage());
        }
    }
}
