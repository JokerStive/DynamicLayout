package com.lilun.passionlife.cloudplatform.utils;

import android.content.SharedPreferences;

import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.ui.App;

/**
 * Created by youke on 2016/6/27.
 */
public class SpUtils {
    public static String getString(String key){
        SharedPreferences sp = App.app.getSharedPreferences(Constants.SP_NAME, App.app.MODE_PRIVATE);
        return sp.getString(key,"");

    }

    public static boolean getBoolean( String key){
        SharedPreferences sp = App.app.getSharedPreferences(Constants.SP_NAME, App.app.MODE_PRIVATE);
        return sp.getBoolean(key,false);

    }

    public static int getInt(String key){
        SharedPreferences sp = App.app.getSharedPreferences(Constants.SP_NAME, App.app.MODE_PRIVATE);
        return sp.getInt(key,-1);

    }



    public static void setString( String key, String value) {
        SharedPreferences sp = App.app.getSharedPreferences(Constants.SP_NAME, App.app.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,value);
        edit.commit();
    }

    public static void setBoolean( String key, boolean value) {
        SharedPreferences sp = App.app.getSharedPreferences(Constants.SP_NAME, App.app.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key,value);
        edit.commit();
    }

    public static void setInt(String key, int value) {
        SharedPreferences sp = App.app.getSharedPreferences(Constants.SP_NAME, App.app.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key,value);
        edit.commit();
    }




}
