package com.lilun.passionlife.cloudplatform.common;

import com.lilun.passionlife.cloudplatform.utils.SpUtils;

/**
 * Created by Administrator on 2016/8/25.
 */
public class User {
    public static final String username="username";
    public static final String name="name";
    public static final String mobile="mobile";
    public static final String email="email";
    public static final String realm="realm";
    public static final String password="password";


    public static String getUsername(){
        return SpUtils.getString(username);
    }


    public static String getName(){
        return SpUtils.getString(name);
    }

    public static int getId(){
        return SpUtils.getInt(TokenManager.USERID);
    }

}
