package com.lilun.passionlife.cloudplatform.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/19.
 */
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mobile;
    private Object id;
    private boolean mobileVerified;
    private String username;
    private String email;
    private String realm;
    private boolean emailVerified;
    private String status;
    private String name;

    public String getName() {
        return name;
    }

    public Account setName(String name) {
        this.name = name;
        return this;
    }



    public String getPassword() {
        return password;
    }

    public Account setPassword(String password) {
        this.password = password;
        return this;
    }

    private String Picture;
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Object getId() {
        return id;
    }

    public Account setId(Object id) {
        this.id = id;
        return this;
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



    public String getPicture() {
        return Picture;
    }

    public void setPicture(String Picture) {
        this.Picture = Picture;
    }
}