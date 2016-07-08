package com.lilun.passionlife.cloudplatform.bean;

/**
 * Created by Administrator on 2016/6/16.
 */
public class LoginRes {




    private String id;
    private int ttl;
    private String created;
    private int userId;

    /**
    *获取token
    */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
