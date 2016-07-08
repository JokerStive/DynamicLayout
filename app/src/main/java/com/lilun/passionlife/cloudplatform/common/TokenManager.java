package com.lilun.passionlife.cloudplatform.common;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.lilun.passionlife.cloudplatform.ui.activity.LoginActivity;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;

/**
 * Created by Administrator on 2016/6/1.
 */
public  class TokenManager {
    public static String TOKEN="token";
    public static String TTL="ttl";
    public static String CREATED ="created";
    public static String AUTHROVITY ="authrovity";
    public static String USERID ="userid";




    /**
    *判断Token是否失效
    */
    public static boolean isTokenEnble(){
        int ttl = SpUtils.getInt(TTL);
        String created = SpUtils.getString(CREATED);
        if (ttl!=-1 && !TextUtils.isEmpty(created)){
            long time = System.currentTimeMillis() / 1000 - StringUtils.IOS2ToUTC(created);
//            Logger.d("时长 = "+time +"----"+"有效时长 = "+ttl);
            if (time<=ttl){
                return true;
            }
        }
        return false;
    }


    public static void translateLogin(Activity ac){
        Intent intent = new Intent(ac,LoginActivity.class);
        intent.putExtra(AUTHROVITY,AUTHROVITY);
        ac.startActivity(intent);
    }
}
