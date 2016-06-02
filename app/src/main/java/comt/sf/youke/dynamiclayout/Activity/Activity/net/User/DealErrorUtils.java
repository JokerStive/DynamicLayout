package comt.sf.youke.dynamiclayout.Activity.Activity.net.User;

import android.content.Context;

import comt.sf.youke.dynamiclayout.Activity.Activity.exception.ApiException;
import comt.sf.youke.dynamiclayout.Activity.Activity.utils.ToastHelper;

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
