package com.lilun.passionlife.cloudplatform.bean;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;

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
    public static class Event401 {
    }

    /**
     * 登录成功事件
     */
    public static class LoginSuccess {
    }

    ;


    /**
     * 开启一个新的fragment
     */
    public static class OpenNewFragmentEvent {
        public Fragment newFragment;
        public String cuumb_title;
        public Bundle bundle;

        public Bundle getBundle() {
            return bundle;
        }

        public OpenNewFragmentEvent setBundle(Bundle bundle) {
            this.bundle = bundle;
            return this;
        }

        public OpenNewFragmentEvent(Fragment newFragment, String cuumb_title) {
            this.cuumb_title = cuumb_title;
            this.newFragment = newFragment;
        }


    }


//    public static class OpenNewFragmentEventCopy {
//        public Fragment newFragment;
//        public String cuumb_title;
//        public Bundle bundle;
//
//        public Bundle getBundle() {
//            return bundle;
//        }
//
//        public OpenNewFragmentEventCopy setBundle(Bundle bundle) {
//            this.bundle = bundle;
//            return this;
//        }
//
//        public OpenNewFragmentEventCopy(Fragment newFragment, String cuumb_title) {
//            this.cuumb_title = cuumb_title;
//            this.newFragment = newFragment;
//        }
//
//
//    }

    /**
     * 编辑按钮被点击的事件
     */
    public static class EditClickEvent {

    }


    /**
     * 默认的组织机构有变化的事件
     */
    public static class DefOrgaHasChanges {
        public DefOrgaHasChanges(OrganizationAccount oa) {
            this.oa = oa;
        }

        public OrganizationAccount getOa() {
            return oa;
        }

        private OrganizationAccount oa;
    }


    /**
     * 当前组织下的模块已经缓存成功的事件
     */
    public static class CurrentOsHasCached {
    }

    /**
     * 新增了一个服务事件
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
     * de put服务事件
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
     * 新增了一个department事件
     */
    public static class addNewDepartment {
        public addNewDepartment(Organization department, Bitmap icon) {
            this.department = department;
            this.icon = icon;
        }

        public Organization getDepartment() {
            return department;
        }

        private Organization department;
        private Bitmap icon;

        public Bitmap getIcon() {
            return icon;
        }
    }


    /**
     * 新增了一个role
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


    /**
     * 复制职位到子组织成功的事件
     */
    public static class CopyRoleSuccess {

    }

    /**
     * 复制部门到子组织成功的事件
     */
    public static class CopyDeptSuccess {

    }


    /**
     * 刷新首页数据
     */
    public static class ReflashHomeData {

    }


    /**
     * save按钮被点击了
     */
    public static class OnSaveClick {

    }


    //   组织管理 ====================================================================================
    //需要刷新组织列表
    public static class reflashOrgaList {

    }

    //   部门管理管理 ====================================================================================
    //需要刷新组织列表
    public static class reflashDeptList {

    }


    //   角色管理管理 ====================================================================================
    //需要刷新组织列表
    public static class reflashRoleList {

    }


    //   员工管理管理 ====================================================================================
    //需要刷新组织列表
    public static class reflashStaffList {

    }


    //   功能模块管理 ====================================================================================
    //需要刷新组织列表
    public static class reflashServiceList {
        OrganizationService os;

        public reflashServiceList(OrganizationService os) {
            this.os = os;
        }

        public OrganizationService getOs() {
            return os;
        }
    }

    //   信息管理 ====================================================================================
    public static class reflashInfo {
        public reflashInfo(OrganizationInformation info) {
            this.info = info;
        }

        public OrganizationInformation getInfo() {
            return info;
        }

        private OrganizationInformation info;
    }

    /**
    *create by youke
    *desc : 显示删除信息的事件
    */
    public  static class setMessageDeleteShow{}


    /**
     *create by youke
     *desc : module_activity 新增事件
     */
    public  static class AddX{}

}
