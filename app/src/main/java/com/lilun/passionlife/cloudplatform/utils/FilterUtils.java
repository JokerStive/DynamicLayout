package com.lilun.passionlife.cloudplatform.utils;

/**
 * Created by Administrator on 2016/7/8.
 */
public class FilterUtils {


    /**
    *根据organizationId获取组织下的information
    */
    public static String orgaInfoFilter(String orgaId,int parentId){
//        "\"include\":\"reviews\""
        return "{\"include\":\"reviews\",\"where\":{\"organizationId\":\""+StringUtils.getCheckedOrgaId(orgaId)+"/#information\",\"parentId\":\""+parentId+"\"}}";
    }


    /**
    *直接获取组织下的staff信息的filter
    */
    public static String orgaAccountFilter(String orgaId){
//        return "{\"include\":\"account\", \"like\":{\"organizationId\": \""+orgaId+"/#staff\"}}";
        return "{\"include\":\"account\",\"where\":{\"organizationId\":\""+StringUtils.getCheckedOrgaId(orgaId)+"/#staff\"}}";
    }


    /**
    *直接获取role关联的principal的filter
    */
    public static String roleFilter(String orgaId){
//        return "{\"where\": {\"name\":{\"like\": \""+orgaId+"%\"}}}";
        return "{\"where\":{\"name\":{\"like\":\""+orgaId+":%\"}},\"include\":\"principals\"}";
    }




    /**
     *直接获取组织下role关联principals
     */
    public static String role(){
        return "{\"include\":\"principals\"}";
    }



    /**
     *直接获取组织下staff关联的account
     */
    public static String staff(){
        return "{\"include\":\"account\"}";
    }




}
