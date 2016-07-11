package com.lilun.passionlife.cloudplatform.utils;

/**
 * Created by Administrator on 2016/7/8.
 */
public class FilterUtils {
    /**
    *直接获取组织下的staff信息的filter
    */
    public static String orgaAccountFilter(String orgaId){
        return "{\"include\":\"account\", \"like\": {\"organizationId\": \""+orgaId+"/#staff\"}}";
    }


    /**
    *直接获取role关联的principal的filter
    */
    public static String roleFilter(String orgaId){
//        return "{\"where\": {\"name\":{\"like\": \""+orgaId+"%\"}}}";
        return "{\"where\":{\"name\":{\"like\":\"/物业:%\"}},\"include\":\"principals\"}";
    }




    /**
     *直接获取组织下role关联的filter
     */
    public static String role(String orgaId){
        return "{\"include\":\"principals\"}";
    }


}
