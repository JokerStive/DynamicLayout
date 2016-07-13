package com.lilun.passionlife.cloudplatform.net.retrofit;

import com.lilun.passionlife.cloudplatform.bean.Account;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.Principal;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.bean.LoginRes;
import com.lilun.passionlife.cloudplatform.bean.OrganizationRole;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.bean.Service;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/6/27.
 */
public class ApiFactory {

    private static ApiService service = RetrofitManager.getRetrofit().create(ApiService.class);

    public static Observable getRes(Observable observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
//                .flatMap(RxJavaUtil::flatResult);
    }


    /**
     * 用户注册
     */
    public static Observable<Response<Account>> register(Account account) {
        return getRes(service.register(account));
    }


    /**
     * 用户登录
     */
    public static Observable<LoginRes> login(Account account) {
        return getRes(service.login(account));
    }
/*
    *//**
     * 获取所有的组织列表
     */
    public static Observable<List<OrganizationAccount>> getOrganizationList(int userid,String filter) {
        return getRes(service.getOrganizationList(userid,filter));
    }



    /**
     * 获取父组织的直接儿子们
     */
    public static Observable<Organization> getOrganization(String organizatiId) {
        return getRes(service.getOrganization(organizatiId));
    }



    /**
     * 获取父组织的直接儿子们
     */
    public static Observable<List<Organization>> getOrgiChildren(String parentOrgiId) {
        return getRes(service.getOrgiChildren(parentOrgiId));
    }


    /**
     *查询是否有权限
     */
    public static   Observable<Boolean>  hasPermission(int userid,String permission){;
        return getRes(service.hasPermission(userid,permission));
    }

    /**
    *获取默认组织的功能服务列表
    */
    public static Observable<List<OrganizationService>> getOrgiServices(String OrgiId) {
        return getRes(service.getOrgiServices(OrgiId));
    }



    /**
    * 获取所有的服务，广告栏什么的额
    */
    public static Observable<List<Service>> getServicess() {
        return getRes(service.getServicess());
    }


    /**
     * 获取组织下面的部门
     */
    public static Observable<List<Organization>> getOrgiDepartment(String orgiId) {
        return getRes(service.getOrgiDepartment(orgiId));
    }



    /**
     * 获取组织下面的OrganizationRole
     */
    public static Observable<List<OrganizationRole>> getOrgiRole(String orgiId) {
        return getRes(service.getOrgiRole(orgiId));
    }


    /**
     * 获取组织下面的Role
     */
    public static  Observable<List<Role>> getRoleListFilter(String filter) {
        return getRes(service.getRoleListFilter(filter));
    }

    /**
     * 获取所有的权限
     */
    public static  Observable<List<Role>> getRoleList(){
        return getRes(service.getRoleList());
    }





    /**
     * 获取OrganizationAccount列表
     */
    public static  Observable<List<OrganizationAccount>> getOrganizationAccountList(String filter) {
        return getRes(service.getOrganizationAccountList(filter));
    }




    /**
     *新增OrganizationService
     */
    public static Observable<OrganizationService> postOrgiServices(OrganizationService service1) {
        return getRes(service.postOrgiServices(service1));
    }


    /**
     *新增Role
     */
    public static Observable<Role> postRole(Role role) {
        return getRes(service.postRole(role));
    }


    /**
     *新增principal
     */
    public static Observable<Principal> postPrincipal(int roleId,Principal principal) {
        return getRes(service.postPrincipal(roleId,principal));
    }



    /**
     * 新增Organization
     */
    public static Observable<Organization> postOrganization(Organization orga) {
        return getRes(service.postOrganization(orga));
    }


    /**
     *给account新增一个organization
     */
    public static Observable<OrganizationAccount> postAccOrganization(int userId,OrganizationAccount orga) {
        return getRes(service.postAccOrganization(userId,orga));
    }

    /**
     *给account新增一个role
     */
    public static Observable<Object> postAccRole(int userId,String role) {
        return getRes(service.postAccRole(userId,role));
    }


    /**
     * 删除一个组织
     */
    public static Observable<Object> deleteOrganization(String orgiId) {
        return getRes(service.deleteOrganization(orgiId));
    }



    /**
     * 删除一个组织服务
     */
    public static Observable<Object> deleteOrganiService(String orgServiceiId) {
        return getRes(service.deleteOrganiService(orgServiceiId));
    }

    /**
     * 删除Role
     */
    public static Observable<Object> deleteRole(int  roleId) {
        return getRes(service.deleteRole(roleId));
    }


    /**
     *删除Role下面的principal
     */
    public static Observable<Object> deletePrincipal(int  roleId,int ptId) {
        return getRes(service.deletePrincipal(roleId,ptId));
    }


    /**
     * 删除OrganizationRole
     */
    public static Observable<Object> deleteOrganiRole(String orgaRoleId) {
        return getRes(service.deleteOrganiRole(orgaRoleId));
    }


    /**
     * 删除OrgaAccount
     */
    public static  Observable<Object> deleteOrgaAccount(int  ocId) {
        return getRes(service.deleteOrgaAccount(ocId));
    }


    /**
     *更新一个组织机构
     */
    public static Observable<Organization> putOrganization(String orgaId,Organization organizationBean){
        return getRes(service.putOrganization(orgaId,organizationBean));
    }



    /**
     *更新OrgaService
     */
    public static Observable<OrganizationService> putOrgaService(String serviceId,OrganizationService orgaService){
        return getRes(service.putOrgaService(serviceId,orgaService));
    }


    /**
     *更新OrgaService
     */
    public static Observable<OrganizationService> putRole(int roleId,Role role){
        return getRes(service.putRole(roleId,role));
    }



}
