package com.lilun.passionlife.cloudplatform.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by youke on 2016/7/7.
 * 权限类
 */
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    private Object id;
    private String name;
    private String description;
    private String created;
    private String modified;
    private String title;
    private boolean isNew;

    public boolean isNew() {
        return isNew;
    }

    public Role setNew(boolean aNew) {
        isNew = aNew;
        return this;
    }

    public boolean isHave() {
        return isHave;
    }

    public Role setHave(boolean have) {
        isHave = have;
        return this;
    }

    private boolean isHave;

    public String getOrganizationId() {
        return organizationId;
    }

    public Object getId() {
        return id;
    }

    public Role setId(Object id) {
        this.id = id;
        return this;
    }

    public Role setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
        return this;

    }

    private String organizationId;

    public List<Principal> getPrincipals() {
        return principals;
    }

    public Role setPrincipals(List<Principal> principals) {
        this.principals = principals;
        return this;
    }

    private List<Principal> principals;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
