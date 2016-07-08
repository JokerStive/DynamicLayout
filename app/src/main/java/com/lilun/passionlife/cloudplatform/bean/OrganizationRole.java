package com.lilun.passionlife.cloudplatform.bean;

/**
 * Created by Administrator on 2016/7/5.
 */
public class OrganizationRole {

    /**
     * id : string
     * role : string
     * organizationId : string
     */

    private String id;

    public String getTitle() {
        return title;
    }

    public OrganizationRole setTitle(String title) {
        this.title = title;
        return this;
    }

    private String title;
    private String role;
    private String organizationId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
