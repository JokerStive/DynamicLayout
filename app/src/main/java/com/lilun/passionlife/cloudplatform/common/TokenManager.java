package com.lilun.passionlife.cloudplatform.common;

import android.app.Activity;
import android.content.Intent;

import com.lilun.passionlife.cloudplatform.ui.activity.LoginActivity;

/**
 * Created by Administrator on 2016/6/1.
 */
public  class TokenManager {
    public static String TOKEN="token";
    public static String TTL="ttl";
    public static String CREATED ="created";
    public static String AUTHROVITY ="authrovity";
    public static String USERID ="userid";





    public static void translateLogin(Activity ac){
        Intent intent = new Intent(ac,LoginActivity.class);
        intent.putExtra(AUTHROVITY,AUTHROVITY);
        ac.startActivity(intent);
    }
}
