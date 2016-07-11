package com.lilun.passionlife.cloudplatform.bean;

import java.io.Serializable;

/**
 * Created by 权限项 on 2016/7/8.
 */
public class Principal implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id : 0
     * principalType : string
     * principalId : string
     * roleId : 0
     */

    private String principalType;
    private String principalId;


    public String getPrincipalType() {
        return principalType;
    }

    public void setPrincipalType(String principalType) {
        this.principalType = principalType;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }


}
