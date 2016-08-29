package com.lilun.passionlife.cloudplatform.common.constans;

import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;

/**
 * Created by Administrator on 2016/8/26.
 */
public class CurrentOas {
    public static String id= SpUtils.getInt(TokenManager.USERID)+"currentOrgaId";
    public static String name="currentOrgaName";
}
