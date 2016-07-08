package com.lilun.passionlife.cloudplatform.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/8.
 */
public class Organization implements Serializable {


    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String parentId;
    private String description;
    private String icon;
    private boolean isNew;
    private String idService;

    public String getIdService() {
        return idService;
    }

    public Organization setIdService(String idService) {
        this.idService = idService;
        return this;
    }

    public boolean isNew() {
        return isNew;
    }

    public Organization setNew(boolean aNew) {
        isNew = aNew;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
