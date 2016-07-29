package com.lilun.passionlife.cloudplatform.utils;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.lilun.passionlife.cloudplatform.ui.App;

/**
 * Created by youke on 2016/7/11.
 */
public class UIUtils {



    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }


    public static int dip2px(Context context,float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }


    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static float getScreenHeight(){
        WindowManager wm = (WindowManager) App.app.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getHeight();
    }



}
