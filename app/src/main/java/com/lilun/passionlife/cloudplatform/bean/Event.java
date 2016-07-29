package com.lilun.passionlife.cloudplatform.bean;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;

import java.util.List;
import java.util.Map;

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

    /**
     * 401event
     */
    public static class Event401{

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



    public static class OpenNewFragmentEventCopy {
        public Fragment newFragment;
        public String cuumb_title;
        public Bundle bundle;

        public Bundle getBundle() {
            return bundle;
        }

        public OpenNewFragmentEventCopy setBundle(Bundle bundle) {
            this.bundle = bundle;
            return this;
        }

        public OpenNewFragmentEventCopy(Fragment newFragment, String cuumb_title) {
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
    public static class postService {
        private OrganizationService service;

        public postService(OrganizationService service) {
            this.service = service;
        }

        public OrganizationService getService() {
            return service;
        }
    }


    /**
     * create by youke
     * desc : put服务事件
     */
    public static class putService {
        private OrganizationService service;

        public putService(OrganizationService service) {
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
     * create by youke
     * desc :新增了一个role
     */
    public static class addNewRole {

        public Role getRole() {
            return role;
        }

        private Role role;

        public addNewRole(List<Principal> principals, Role role) {
            this.principals = principals;
            this.role = role;
        }

        public List<Principal> getPrincipals() {
            return principals;
        }

        private List<Principal> principals;
    }


    /**
     * 删除了一个组织服务的事件
     */
    public static class deleteOrganiService {
    }


    /**
     * 选择了所属部门的事件
     */
    public static class choiseDepts {
        private List<Organization> depts;

        public List<Organization> getDepts() {
            return depts;
        }

        public choiseDepts(List<Organization> depts) {
            this.depts = depts;
        }
    }


    /**
     * 删除了一个所属部门的事件
     */
    public static class deleteBelongDept {
        private List<Map<OrganizationAccount, List<Role>>> deptAndRoleList;

        public deleteBelongDept(List<Map<OrganizationAccount, List<Role>>> deptAndRoleList) {
            this.deptAndRoleList = deptAndRoleList;
        }

        public List<Map<OrganizationAccount, List<Role>>> getDeptAndRoleList() {
            return deptAndRoleList;
        }
    }


    /**
     * 选择头像事件
     */
    public static class choiseHeadPic {
        private Bitmap bitmap;

        public Bitmap getBitmap() {
            return bitmap;
        }

        public choiseHeadPic(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
    }



    /**
     * 切换了旗下组织的事件
     */
    public static class ChangeChildOrganization {
        String organizationId;
        String organizationName;

        public String getOrganizationId() {
            return organizationId;
        }

        public String getOrganizationName() {
            return organizationName;
        }

        public ChangeChildOrganization(String organizationId, String organizationName) {
            this.organizationId = organizationId;
            this.organizationName = organizationName;
        }
    }

}
