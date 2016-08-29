package com.lilun.passionlife.cloudplatform.net.retrofit;

import com.lilun.passionlife.cloudplatform.bean.Account;
import com.lilun.passionlife.cloudplatform.bean.InformationReview;
import com.lilun.passionlife.cloudplatform.bean.IsInherited;
import com.lilun.passionlife.cloudplatform.bean.LoginRes;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.OrganizationInformation;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.bean.Principal;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.bean.Service;
import com.lilun.passionlife.cloudplatform.common.Constants;

import java.util.List;

import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by youke on 2016/6/27.
 */
public class ApiFactory {


//    private static ApiService service = RetrofitManager.createService(Constants.BASE_URL,ApiService.class);


    public static Observable getRes(Observable observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static ApiService getService(){
        return  RetrofitManager.createService(Constants.BASE_URL,ApiService.class);
    }

    /**
     * 用户注册
     */
    public static Observable<Account> register(Account account) {
        return getRes(getService().register(account));
    }


    /**
     *新增组织机构图标
     */
    public static  Observable<Object> postAccountIcon(double userId, RequestBody file) {
        return getRes(getService().postAccountIcon((int) userId,file));
    }


    /**
     * 401检测
     */
    public static Observable<Object> check401() {
        return getRes(getService().check401());
    }


    /**
     * 用户登录
     */
    public static Observable<LoginRes> login(Account account) {
        return getRes(getService().login(account));
    }



    //  GET  ========================================================================================================================================================

/**
     * 获取所有的组织列表
     */
    public static Observable<List<OrganizationAccount>> getOrganizationList(double userid,String filter) {

        return getRes(getService().getOrganizationList((int) userid,filter));
    }



    /**
     * 获取父组织的直接儿子们
     */
    public static Observable<Organization> getOrganization(String organizatiId) {
        return getRes(getService().getOrganization(organizatiId));
    }



    /**
     * 获取父组织的直接儿子们
     */
    public static Observable<List<Organization>> getOrgiChildren(String parentOrgiId) {
        return getRes(getService().getOrgiChildren(parentOrgiId));
    }


    /**
     *查询是否有权限
     */
    public static   Observable<Boolean>  hasPermission(double userid,String permission){
        return getRes(getService().hasPermission((int)userid,permission));
    }

    /**
    *获取默认组织的功能服务列表
    */
    public static Observable<List<OrganizationService>> getOrgiServices(String OrgiId) {
        return getRes(getService().getOrgiServices(OrgiId));
    }



    /**
    * 获取所有的服务，广告栏什么的额
    */
    public static Observable<List<Service>> getServicess() {
        return getRes(getService().getServicess());
    }


    /**
     * 获取组织下面的部门
     */
    public static Observable<List<Organization>> getOrgiDepartment(String orgiId) {
        return getRes(getService().getOrgiDepartment(orgiId));
    }



    /**
     * 获取组织下面的OrganizationRole
     */
    public static Observable<List<Role>> getOrgiRole(String orgiId) {
        return getRes(getService().getOrgiRole(orgiId));
    }


    /**
     * 获取组织机构下面的role带filter
     */
    public static Observable<List<Role>> getOrgiRoleFilter(String orgiId,String filter) {
        return getRes(getService().getOrgiRoleFilter(orgiId,filter));
    }


    /**
     * 获取组织下面的Role
     */
    public static  Observable<List<Role>> getRoleListFilter(String filter) {
        return getRes(getService().getRoleListFilter(filter));
    }

    /**
     * 获取所有的权限
     */
    public static  Observable<List<Role>> getRoleList(){
        return getRes(getService().getRoleList());
    }


    /**
     * 获取所有的权限
     */
    public static  Observable<List<Principal>> getRolePrincials(double roleId){
        return getRes(getService().getRolePrincials((int)roleId));
    }


    /**
     * *获取account所属的role
     */
    public static    Observable<List<Role>> getAccountRole(double userId){
        return getRes(getService().getAccountRole((int) userId));
    }



    /**
     * *获取account所属的role
     */
    public static    Observable<List<OrganizationAccount>> getAccountDept(double userId){
        return getRes(getService().getAccountDept((int) userId));
    }




    /**
     * 获取OrganizationAccount列表
     */
    public static  Observable<List<OrganizationAccount>> getOrganizationAccountList(String filter) {
        return getRes(getService().getOrganizationAccountList(filter));
    }


    /**
     * 获取当前id是否isInherited
     */
    public static  Observable<Boolean> getIsInherited(String id) {
        return getRes(getService().getIsInherited(id));
    }


    /**
     * 获取OrganizationAccount列表
     */
    public static  Observable<List<OrganizationInformation>> getOrgaInfos(String filter) {
        return getRes(getService().getOrgaInfos(filter));
    }







    //  post  ========================================================================================================================================================


    /**
     *新增OrganizationService
     */
    public static Observable<OrganizationService> postOrgiServices(OrganizationService service1) {
        return getRes(getService().postOrgiServices(service1));
    }


    /**
     *新增新增OrganizationService图标
     */
    public static  Observable<Object> postOrgaServiceIcon(String orgaserviceId, RequestBody file) {
        return getRes(getService().postOrgaServiceIcon(orgaserviceId,file));
    }


    /**
     *新增Role
     */
    public static Observable<Role> postRole(Role role) {
        return getRes(getService().postRole(role));
    }


    /**
     *新增多个role
     */
    public static Observable<List<Role>> postRoles(List<Role> roles) {
        return getRes(getService().postRoles(roles));
    }


    /**
     *新增principal
     */
    public static Observable<Object> postPrincipal(String roleId,List<Principal> principals) {
        return getRes(getService().postPrincipal(roleId,principals));
    }






    /**
     * 新增Organization
     */
    public static Observable<Organization> postOrganization(Organization orga) {
        return getRes(getService().postOrganization(orga));
    }


    /**
     * 新增多个Organization
     */
    public static Observable<List<Organization>> postOrganizations(List<Organization> orgas) {
        return getRes(getService().postOrganizations(orgas));
    }


    /**
     *新增组织机构图标
     */
    public static  Observable<Object> postOrganizationIcon(String orgaId, RequestBody file) {
        return getRes(getService().postOrganizationIcon(orgaId,file));
    }


    /**
     *给account新增一个organization
     */
    public static Observable<List<OrganizationAccount>> postAccOrganization(double userId,List<OrganizationAccount> orgas) {
        return getRes(getService().postAccOrganization((int) userId,orgas));
    }

    /**
     *给account新增一个role
     */
    public static Observable<Object> postAccRole(double userId,String role) {
        return getRes(getService().postAccRole((int) userId,role));
    }


    /**
     *新增一个info
     */
    public static Observable<OrganizationInformation> postOassInfo( OrganizationInformation oi) {
        return getRes(getService().postOassInfo(oi));
    }



    /**
     *新增一个review
     */
    public static Observable<InformationReview> postReview(InformationReview review) {
        return getRes(getService().postReview(review));
    }





//  DELETE  ========================================================================================================================================================

    /**
     * 删除一个组织
     */
    public static Observable<Object> deleteOrganization(String orgiId) {
        return getRes(getService().deleteOrganization(orgiId));
    }



    /**
     * 删除一个组织服务
     */
    public static Observable<Object> deleteOrganiService(String orgServiceiId) {
        return getRes(getService().deleteOrganiService(orgServiceiId));
    }

    /**
     * 删除Role
     */
    public static Observable<Object> deleteRole(String  roleId) {
        return getRes(getService().deleteRole(roleId));
    }


    /**
     *删除Role下面的principal
     */
    public static Observable<Object> deletePrincipal(String   roleId,double ptId) {
        return getRes(getService().deletePrincipal(roleId,(int) ptId));
    }


    /**
     * 删除OrganizationRole
     */
    public static Observable<Object> deleteOrganiRole(String orgaRoleId) {
        return getRes(getService().deleteOrganiRole(orgaRoleId));
    }


    /**
     * 删除Account指定的dept
     */
    public static   Observable<Object> deleteAccOrga(double  ocId,double orgaAccId) {
        return getRes(getService().deleteAccOrga((int) ocId, (int) orgaAccId));
    }


    /**
     * 删除Account指定的role
     */
    public static   Observable<Object> deleteAccRole(double  ocId,String roleName) {
        return getRes(getService().deleteAccRole((int) ocId, roleName));
    }


    /**
     * 删除OrgaAccount
     */
    public static  Observable<Object> deleteOrgaAccount(double  ocId) {
        return getRes(getService().deleteOrgaAccount((int) ocId));
    }


    /**
     * 删除OrgaAccount
     */
    public static  Observable<Object> deleteInfo(double  ocId) {
        return getRes(getService().deleteInfo((int) ocId));
    }





//   PUT ==================================================================================================================================

    /**
     *更新一个account
     */
    public static  Observable<Account> putAccount(double accountId,Account account){
        return getRes(getService().putAccount((int) accountId,account));
    }



    /**
     *更新account所属组织
     */
    public static  Observable<OrganizationAccount> putDefBelongOrga(double accountId,String orgaId,OrganizationAccount oa){
        return getRes(getService().putDefBelongOrga((int) accountId,orgaId,oa));
    }


    /**
     *更新一个组织机构
     */
    public static Observable<Organization> putOrganization(String orgaId,Organization organizationBean){
        return getRes(getService().putOrganization(orgaId,organizationBean));
    }



    /**
     *更新OrgaService
     */
    public static Observable<OrganizationService> putOrgaService(String serviceId,OrganizationService orgaService){
        return getRes(getService().putOrgaService(serviceId,orgaService));
    }


    /**
     *更新role
     */
    public static Observable<Role> putRole(String roleId,Role role){
        return getRes(getService().putRole(roleId,role));
    }


    /**
     *设置isInherited
     */
    public static Observable<Object> putIsInheroted(String id,IsInherited isInherited){
        return getRes(getService().putIsInheroted(id,isInherited));
    }



}
