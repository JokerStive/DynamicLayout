package com.lilun.passionlife.cloudplatform.net.retrofit;

import com.lilun.passionlife.cloudplatform.bean.Account;
import com.lilun.passionlife.cloudplatform.bean.IsInherited;
import com.lilun.passionlife.cloudplatform.bean.LoginRes;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.bean.Principal;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.bean.Service;

import java.util.List;

import okhttp3.RequestBody;
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
     * 401检测
     */
    public static Observable<Object> check401() {
        return getRes(service.check401());
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
    public static Observable<List<OrganizationAccount>> getOrganizationList(double userid,String filter) {

        return getRes(service.getOrganizationList((int) userid,filter));
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
    public static   Observable<Boolean>  hasPermission(double userid,String permission){;
        return getRes(service.hasPermission((int)userid,permission));
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
    public static Observable<List<Role>> getOrgiRole(String orgiId) {
        return getRes(service.getOrgiRole(orgiId));
    }


    /**
     * 获取组织机构下面的role带filter
     */
    public static Observable<List<Role>> getOrgiRoleFilter(String orgiId,String filter) {
        return getRes(service.getOrgiRoleFilter(orgiId,filter));
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
     * *获取account所属的role
     */
    public static    Observable<List<Role>> getAccountRole(double userId){
        return getRes(service.getAccountRole((int) userId));
    }




    /**
     * 获取OrganizationAccount列表
     */
    public static  Observable<List<OrganizationAccount>> getOrganizationAccountList(String filter) {
        return getRes(service.getOrganizationAccountList(filter));
    }


    /**
     * 获取当前id是否isInherited
     */
    public static  Observable<Boolean> getIsInherited(String id) {
        return getRes(service.getIsInherited(id));
    }




    /**
     *新增OrganizationService
     */
    public static Observable<OrganizationService> postOrgiServices(OrganizationService service1) {
        return getRes(service.postOrgiServices(service1));
    }


    /**
     *新增新增OrganizationService图标
     */
    public static  Observable<Object> postOrgaServiceIcon(String orgaserviceId, RequestBody file) {
        return getRes(service.postOrgaServiceIcon(orgaserviceId,file));
    }


    /**
     *新增Role
     */
    public static Observable<Role> postRole(Role role) {
        return getRes(service.postRole(role));
    }


    /**
     *新增多个role
     */
    public static Observable<List<Role>> postRoles(List<Role> roles) {
        return getRes(service.postRoles(roles));
    }


    /**
     *新增principal
     */
    public static Observable<Object> postPrincipal(double roleId,List<Principal> principals) {
        return getRes(service.postPrincipal((int) roleId,principals));
    }






    /**
     * 新增Organization
     */
    public static Observable<Organization> postOrganization(Organization orga) {
        return getRes(service.postOrganization(orga));
    }


    /**
     * 新增多个Organization
     */
    public static Observable<List<Organization>> postOrganizations(List<Organization> orgas) {
        return getRes(service.postOrganizations(orgas));
    }


    /**
     *新增组织机构图标
     */
    public static  Observable<Object> postOrganizationIcon(String orgaId, RequestBody file) {
        return getRes(service.postOrganizationIcon(orgaId,file));
    }


    /**
     *给account新增一个organization
     */
    public static Observable<List<OrganizationAccount>> postAccOrganization(double userId,List<OrganizationAccount> orgas) {
        return getRes(service.postAccOrganization((int) userId,orgas));
    }

    /**
     *给account新增一个role
     */
    public static Observable<Object> postAccRole(double userId,String role) {
        return getRes(service.postAccRole((int) userId,role));
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
    public static Observable<Object> deleteRole(double  roleId) {
        return getRes(service.deleteRole((int) roleId));
    }


    /**
     *删除Role下面的principal
     */
    public static Observable<Object> deletePrincipal(double  roleId,double ptId) {
        return getRes(service.deletePrincipal((int) roleId,(int) ptId));
    }


    /**
     * 删除OrganizationRole
     */
    public static Observable<Object> deleteOrganiRole(String orgaRoleId) {
        return getRes(service.deleteOrganiRole(orgaRoleId));
    }


    /**
     * 删除Account指定的dept
     */
    public static   Observable<Object> deleteAccOrga(double  ocId,double orgaAccId) {
        return getRes(service.deleteAccOrga((int) ocId, (int) orgaAccId));
    }


    /**
     * 删除Account指定的role
     */
    public static   Observable<Object> deleteAccRole(double  ocId,String roleName) {
        return getRes(service.deleteAccRole((int) ocId, roleName));
    }


    /**
     * 删除OrgaAccount
     */
    public static  Observable<Object> deleteOrgaAccount(double  ocId) {
        return getRes(service.deleteOrgaAccount((int) ocId));
    }




    /**
     *更新一个account
     */
    public static  Observable<Account> putAccount(double accountId,Account account){
        return getRes(service.putAccount((int) accountId,account));
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
    public static Observable<OrganizationService> putRole(double roleId,Role role){
        return getRes(service.putRole((int)roleId,role));
    }


    /**
     *设置isInherited
     */
    public static Observable<Object> putIsInheroted(String id,IsInherited isInherited){
        return getRes(service.putIsInheroted(id,isInherited));
    }



}
