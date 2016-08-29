package com.lilun.passionlife.cloudplatform.bean;

/**
 * Created by Administrator on 2016/6/22.
 */
public class Service {

    /**
     * id : Account
     * title : 用户管理
     * settings : {"kind":{"id":"kind","title":"信息类型","type":"string"},"visibility":{"id":"visibility","title":"公开程度","type":"string"},"replyable":{"id":"replyable","title":"是否允许回复","type":"string"},"visible":{"id":"visible","title":" ","type":"bool","value":"false"}}
     * description : 对组织的用户进行管理
     * createdAt : 2016-07-04T08:35:29.823Z
     * updatedAt : 2016-07-04T08:35:29.823Z
     * creatorId : null
     * updatorId : null
     * enabled : true
     * privated : true
     * Icon : Service.Icon/account.png
     */

    private String id;
    private String title;
    /**
     * kind : {"id":"kind","title":"信息类型","type":"string"}
     * visibility : {"id":"visibility","title":"公开程度","type":"string"}
     * replyable : {"id":"replyable","title":"是否允许回复","type":"string"}
     * visible : {"id":"visible","title":" ","type":"bool","value":"false"}
     */

    private Object settings;
    private String description;
    private String createdAt;
    private String updatedAt;
    private Object creatorId;
    private Object updatorId;
    private boolean enabled;
    private boolean privated;
    private String Icon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getSettings() {
        return settings;
    }

    public void setSettings(Object settings) {
        this.settings = settings;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Object creatorId) {
        this.creatorId = creatorId;
    }

    public Object getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Object updatorId) {
        this.updatorId = updatorId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPrivated() {
        return privated;
    }

    public void setPrivated(boolean privated) {
        this.privated = privated;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

//    public static class SettingsBean {
//
//    }
}
