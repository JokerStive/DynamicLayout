package com.lilang.passionlife.cloudplatform.net.User;

import android.content.Context;

import com.lilang.passionlife.cloudplatform.exception.ApiException;
import com.lilang.passionlife.cloudplatform.utils.ToastHelper;

/**
 * Created by youke on 2016/6/2.
 * 异常处理
 */
public class DealErrorUtils {
    public static void dealError(Throwable e, Context cx){
        if (e instanceof ApiException){
            ApiException e1 = (ApiException) e;
            ToastHelper.get(cx).showShort(e1.getMessage());
        }
    }
}
