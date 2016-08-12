package com.lilun.passionlife.cloudplatform.common;

/**
 * Created by Administrator on 2016/8/2.
 */
public class Admin {
    public static String id = "/";
    public static String name = "/";

    public static boolean isRootOrganization(String orgaId){
        if (orgaId.equals(id)){
            return true;
        }

        return false;
    }
}
