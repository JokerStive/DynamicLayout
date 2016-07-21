package com.lilun.passionlife.cloudplatform.common;

import com.lilun.passionlife.cloudplatform.utils.SpUtils;

/**
 * Created by Administrator on 2016/5/25.
 */
public class Constants {
    //默认超时时间
    public static final int DEFAULT_TIMEOUT = 5;

    //短缓存时间
    public static final int SHORT_CACHE_TIME = 60;

    //长缓存时间
    public static final int LONG_CACHE_TIME = 300;



    //所属组织
    public static final String BELONG_ORGA="belong_orga";

    //接口base_url
    public static final String BASE_URL = "http://222.182.202.94:3000/api/";

    //日志打印ltag
    public static final String LOGGER_TAG = "yk";


    public static final String SP_NAME = "skylife";

    //所属组织的cache_key
    public static final String CACHE_PARENT_ORGINS = "parentOrgins";


    //超级用户的sp_key
    public static final String ADMIN = "admin";

    //默认组织名的sp_key
    public static String key_defOrgina="defOrgina";

    //默认组织id的sp_key
    public static String key_defOrginaId= SpUtils.getInt(TokenManager.USERID)+"defOrginaId";

    //默认组织儿子的sp_key
    public static String key_child_OrginaId="childOrginaId";

    //默认组织名的sp_key
    public static String key_child_OrginaName="childOrginaName";

    //可见服务的key_cache
    public static String cacheKey_service="servicess";

    //部门key_cache
    public static String cacheKey_department="depts";

    //角色key_cache
    public static String cacheKey_role="roles";

    //特殊的组织——service
    public static String special_orgi_service="/#service";


    //特殊的组织——department
    public static String special_orgi_department="/#department";

    //特殊的组织——department
    public static String special_orgi_department1="/#department";



    //特殊的组织——Role
    public static String special_orgi_role="/#role";

    //特殊的组织——Rstaff
    public static String special_orgi_staff="/#staff";

    //标示--Role
    public static String role="role";

    //标示--dept
    public static String orgaDept ="orgaDept";

    //标示--orgaService
    public static String orgaService="orgaService";

    //标示--organization
    public static String organization="organization";

    //标示--orgaAccount
    public static String orgaAccount="orgaAccount";



    //标示--orgaService
//    public static String orgaService="orgaService";

}
