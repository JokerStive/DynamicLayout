package com.lilun.passionlife.cloudplatform.bean;


import android.os.Bundle;

import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;

import java.util.List;

/**
 * Created by youke on 2016/6/3.
 * desc:事件类
 */
public class Event {

    /**
     * 验证失效
     */
    public static class AuthoriseEvent {
    }

    ;


    public static class OpenNewFragmentEvent {
        public BaseFunctionFragment newFragment;
        public String cuumb_title;
        public Bundle bundle;

        public Bundle getBundle() {
            return bundle;
        }

        public OpenNewFragmentEvent setBundle(Bundle bundle) {
            this.bundle = bundle;
            return this;
        }

        public OpenNewFragmentEvent(BaseFunctionFragment newFragment, String cuumb_title) {
            this.cuumb_title = cuumb_title;
            this.newFragment = newFragment;
        }


    }

    public static class EditClickEvent {

    }


    public static class AllOrganEvent {
        private List<OrganizationAccount> allOrganization;
        private String defOrgaId;

        public AllOrganEvent(List<OrganizationAccount> allOrganization, String defOrgaId, String defOrgaName) {
            this.allOrganization = allOrganization;
            this.defOrgaId = defOrgaId;
            this.defOrgaName = defOrgaName;
        }

        private String defOrgaName;
    }

    /**
     * create by youke
     * desc : 查询服务列表成功
     */
    public static class getOrgiServices_ok {
        private List<OrganizationService> data;

        public getOrgiServices_ok(List<OrganizationService> data) {
            this.data = data;
        }

        public List<OrganizationService> getData() {
            return data;
        }
    }


    /**
     * create by youke
     * desc : 查询组织结构列表成功
     */
    public static class getOrgiList_ok {
        private List<OrganizationAccount> data;
        private String defOrgiName;

        public getOrgiList_ok(List<OrganizationAccount> data, String defOrgiName) {
            this.data = data;
            this.defOrgiName = defOrgiName;
        }

        public String getDefOrgiName() {
            return defOrgiName;
        }

        public List<OrganizationAccount> getData() {
            return data;

        }
    }

    /**
     * create by youke
     * desc : 查看view权限事件
     */
    public static class checkViewPermission {
        private OrganizationService service;
        private boolean viewPermission;
        private int serviceCount;

        public checkViewPermission(OrganizationService service, boolean viewPermission, int serviceCount) {
            this.service = service;
            this.viewPermission = viewPermission;
            this.serviceCount = serviceCount;
        }

        public int getServiceCount() {
            return serviceCount;
        }

        public OrganizationService getService() {

            return service;
        }

        public boolean isViewPermission() {
            return viewPermission;
        }
    }


    /**
     * create by youke
     * desc : 默认的组织机构有变化的事件
     */
    public static class SystemConfigOrgId {
        public SystemConfigOrgId(String orgId) {
            this.orgId = orgId;
        }

        public String getOrgId() {
            return orgId;
        }

        private String orgId;
    }

    /**
     * create by youke
     * desc : 新增了一个服务事件
     */
    public static class addNewService {
        private OrganizationService service;

        public addNewService(OrganizationService service) {
            this.service = service;
        }

        public OrganizationService getService() {
            return service;
        }
    }


    /**
     * create by youke
     * desc :新增了一个department事件
     */
    public static class addNewDepartment {
        public addNewDepartment(Organization department) {
            this.department = department;
        }

        public Organization getDepartment() {
            return department;
        }

        private Organization department;
    }


    /**
    *删除了一个组织服务的事件
    */
    public static class deleteOrganiService{}

}
