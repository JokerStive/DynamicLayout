package com.lilang.passionlife.cloudplatform.bean;

/**
 * Created by Administrator on 2016/5/19.
 */
public class Account {

    /**
     * mobile : string
     * email : string
     * realm : string
     * username : string
     * credentials : {}
     * challenges : {}
     * emailVerified : true
     * status : string
     * created : 2016-05-19
     * lastUpdated : 2016-05-19
     * id : 0
     */

    private String mobile;
    private String email;
    private String realm;
    private String username;
    private boolean emailVerified;

    private String status;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String created;
    private String lastUpdated;
    private int id;






}
