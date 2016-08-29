package com.lilun.passionlife.cloudplatform.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/30.
 */
public class OrganizationService implements Serializable{
    private static final long serialVersionUID = 1L;

    public String getServiceName() {
        return serviceName;
    }

    public OrganizationService setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    /**
     * id : string
     * title : string
     * description : string
     * settings : {}
     * organizationId : string
     * serviceId : string
     * Icon : .
     */


    private String id;
    private String title;
    private String description;
    private Object settings;
    private String organizationId;
    private String serviceId;
    private String serviceName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getSettings() {
        return settings;
    }

    public void setSettings(Object settings) {
        this.settings = settings;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

//    public static class SettingsBean implements Serializable{
//
//        private static final long serialVersionUID = 1L;
//        private String visible;
//        private String wigth;
//
//        public String getWigth() {
//            return wigth;
//        }
//
//        public SettingsBean setWigth(String wigth) {
//            this.wigth = wigth;
//            return this;
//        }
//
//        public String getHeight() {
//            return height;
//        }
//
//        public SettingsBean setHeight(String height) {
//            this.height = height;
//            return this;
//        }
//
//        private String height;
//
//        public String getVisible() {
//            return visible;
//        }
//
//        public void setVisible(String visible) {
//            this.visible = visible;
//        }
//
//
//    }
}
