package com.lilun.passionlife.cloudplatform.utils;

/**
 * Created by Administrator on 2016/7/8.
 */
public class FilterUtils {
    /**
    *直接获取组织下的staff信息的过滤器
    */
    public static String orgaAccountFilter(String orgaId){
        return "{\"include\":\"account\", \"like\": {\"organizationId\": \""+orgaId+"/#staff\"}}";
    }
}
