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

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/6/27.
 */
public interface ApiService {
    /**
     * 用户注册
     */

    @POST("Accounts")
    Observable<Account> register(@Body Account user);

    /**
     * 用户登录
     */

    @POST("Accounts/login")
    Observable<LoginRes> login(@Body Account account);

    /**
     * 获取用户所属组织列表
     */
//    @Headers("Cache-Control: public, max-age=360000,  max-stale=480000")
    @GET("Accounts/{id}/organizations")
    Observable<List<OrganizationAccount>> getOrganizationList(@Path("id") int userid,@Query("filter") String filter);



    /**
     * 根据id获取一个组织
     */
    @GET("Organizations/{id}")
    Observable<Organization> getOrganization(@Path("id") String OrgiId);


    /**
     * 获取组织的直接儿子
     */
    @GET("Organizations/{id}/children")
    Observable<List<Organization>> getOrgiChildren(@Path("id") String OrgiId);


    /**
     * *查询是否具有权限
     */
//    @Headers("Cache-Control: public, max-age=300,  max-stale=500")
    @GET("Accounts/{id}/hasPermission")
    Observable<Boolean> hasPermission(@Path("id") int userid, @Query("role") String permission);



    /**
     *获取组织机构服务列表
     */
//    @Headers("Cache-Control: public, max-age=300,  max-stale=500")
    @GET("Organizations/{id}/children")
    Observable<List<OrganizationService>> getOrgiServices(@Path("id") String OrgiId);





    /**
     *获取所有的服务
     */
    @Headers("Cache-Control: public, max-age=300,  max-stale=500")
    @GET("Services")
    Observable<List<Service>> getServicess();



    /**
     *获取组织机构下面的部门
     */
    @GET("Organizations/{id}/children")
    Observable<List<Organization>> getOrgiDepartment(@Path("id") String OrgiId);


    /**
     *获取组织机构下面的OrganizationRole
     */
    @GET("Organizations/{id}/children")
    Observable<List<Role>> getOrgiRole(@Path("id") String OrgiId);


    /**
     *获取role列表
     */
    @GET("Roles")
    Observable<List<Role>> getRoleListFilter(@Query("filter") String filter);

    /**
     *获取所有的权限
     */
    @GET("Roles")
    Observable<List<Role>> getRoleList();


    /**
     *获取account所属的role
     */
    @GET("Accounts/{id}/roles")
    Observable<List<Role>> getAccountRole(@Path("id") int userId);



    /**
     *获取当前id是否isInherited
     */
    @GET("Organizations/{id}/isInherited")
    Observable<Boolean> getIsInherited(@Path("id") String id);




    /**
     *获取OrganizationAccount列表
     */
    @GET("OrganizationAccounts")
    Observable<List<OrganizationAccount>> getOrganizationAccountList(@Query("filter") String filter);


    /**
     *新增OrganizationService
     */
    @POST("OrganizationServices")
    Observable<OrganizationService> postOrgiServices(@Body OrganizationService service);


    /**
     *新增Role
     */
    @POST("Roles")
    Observable<Role> postRole(@Body Role role);

    /**
     *新增principal
     */
    @POST("Roles/{id}/principals")
    Observable<Principal> postPrincipal(@Path("id")  int roleId,@Body Principal principal);


    /**
     *新增组织机构
     */
    @POST("Organizations")
    Observable<Organization> postOrganization(@Body Organization organizationBean);

    /**
     *给account新增一个organization
     */
    @POST("Accounts/{id}/organizations")
    Observable<OrganizationAccount> postAccOrganization(@Path("id") int userId ,@Body OrganizationAccount orga);

    /**
     *给account新增一个role
     */
    @PUT("Accounts/{id}/roles/{role}")
    Observable<Object> postAccRole(@Path("id") int userId ,@Path("role") String role);

    /**
    *删除组织
    */
    @DELETE("Organizations/{id}")
    Observable<Object> deleteOrganization(@Path("id") String OrgiId);

    /**
     *删除组织机构--服务
     */
    @DELETE("OrganizationServices/{id}")
    Observable<Object> deleteOrganiService(@Path("id") String OrgiServiceId);

    /**
     *删除组OrganizationRole
     */
    @DELETE("OrganizationRoles/{id}")
    Observable<Object> deleteOrganiRole(@Path("id") String orgaRoleId);


    /**
     *删除Role
     */
    @DELETE("Roles/{id}")
    Observable<Object> deleteRole(@Path("id") int roleId);

    /**
     *删除Role下面的principal
     */
    @DELETE("Roles/{id}/principals/{fk}")
    Observable<Object> deletePrincipal(@Path("id") int roleId,@Path("fk") int ptId);



    /**
     *删除OrgaAccount
     */
    @DELETE("OrganizationAccounts/{id}")
    Observable<Object> deleteOrgaAccount(@Path("id") int ocId);


    /**
     *删除Account指定的dept
     */
    @DELETE("Accounts/{id}/organizations/{orgaId}")
    Observable<Object> deleteAccOrga(@Path("id") int ocId,@Path(("orgaId")) int orgaId);


    /**
     *删除Account指定的role
     */
    @DELETE("Accounts/{id}/roles/{id}")
    Observable<Object> deleteAccRole(@Path("id") int ocId,@Path(("id")) int roleId);





    /**
     *更新一个组织机构
     */
    @PUT("Organizations/{id}")
    Observable<Organization> putOrganization(@Path("id") String orgaId,@Body Organization organizationBean);





    /**
     *更新OrgaService
     */
    @PUT("OrganizationServices/{id}")
    Observable<OrganizationService> putOrgaService(@Path("id") String serviceId,@Body OrganizationService orgaService);


    /**
     *更新Role
     */
    @PUT("Role/{id}")
    Observable<OrganizationService> putRole(@Path("id") int roleId,@Body Role role);


    /**
     *设置isInherited
     */
    @PUT("Organizations/{id}/isInherited")
    Observable<Object> putIsInheroted(@Path("id") String id,@Body IsInherited isInherited);




}
