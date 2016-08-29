package com.lilun.passionlife.cloudplatform.utils;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

/**
 * Created by Administrator on 2016/8/25.
 */
public class SnackbarHelper {
    public static void makeShort(CoordinatorLayout parentVew,String text){
        Snackbar.make(parentVew,text,Snackbar.LENGTH_SHORT).show();
    }


    public static void makeShort(CoordinatorLayout parentVew,String text,int color){
        Snackbar snackbar = Snackbar.make(parentVew, text, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(color);
        snackbar.show();
    }


    public  static void makeLong(CoordinatorLayout parentVew,String text){
        Snackbar.make(parentVew,text,Snackbar.LENGTH_LONG).show();
    }

    public  static void makeLong(CoordinatorLayout parentVew,String text,int color){
        Snackbar snackbar = Snackbar.make(parentVew, text, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(color);
        snackbar.show();
    }

}
