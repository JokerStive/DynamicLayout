package com.lilun.passionlife.cloudplatform.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/20.
 */
public class OrganizationAccount implements Serializable {


    private boolean isDefault;
    private int id;
    private String organizationId;
    private int accountId;

    public Organization getOrganization() {
        return organization;
    }

    public OrganizationAccount setOrganization(Organization organization) {
        this.organization = organization;
        return this;
    }

    private Account account;
    private Organization organization;


    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


}
