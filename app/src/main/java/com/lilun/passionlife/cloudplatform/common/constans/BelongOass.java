package com.lilun.passionlife.cloudplatform.common.constans;

import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;

/**
 * Created by Administrator on 2016/8/26.
 */
public class BelongOass {

    public static String oass="belong_oass";

    public static String modules="modules";

    public static String oas_id= SpUtils.getInt(TokenManager.USERID)+"currentBelongOrgaId";

    public static String oas_name="currentBelongOrgaName";
}
