package com.lilun.passionlife.cloudplatform.base;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.IsInherited;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.ACache;
import com.lilun.passionlife.cloudplatform.utils.CacheUtils;
import com.lilun.passionlife.cloudplatform.utils.FilterUtils;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by youke on 2016/6/28.
 */
public class BaseNetActivity extends FragmentActivity {

    private CompositeSubscription mCompositeSubscription;
    private PgSubscriber pgSubscriber;
    private List<OrganizationService> visibleOrgiService;
    private List<OrganizationService> settingVisibleService;
    private int serviceCount;
    private int index=0;

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        if (subscriber instanceof PgSubscriber) {
            pgSubscriber = (PgSubscriber) subscriber;
        }
        mCompositeSubscription.add(observable
                .doOnSubscribe(() -> {
                    String token = SpUtils.getString(TokenManager.TOKEN);
                    if (!TextUtils.isEmpty(token)) {
                        if (!TokenManager.isTokenEnble()) {
                            mCompositeSubscription.unsubscribe();
                            Logger.d("登录失效");
                            EventBus.getDefault().post(new Event.AuthoriseEvent());
                            if (pgSubscriber != null) {
                                pgSubscriber.dismissProgressDialog();
                            }
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }


    public boolean isAdmin() {
        return SpUtils.getBoolean(Constants.ADMIN);
    }


    /**
    *获取用户所属组织列表
    */
    protected void getBelongOrga(callBack_getBelongOrga listen) {
        Double userId = Double.valueOf(SpUtils.getInt(TokenManager.USERID));
        String filter = FilterUtils.belongOgaFilter();
        addSubscription(ApiFactory.getOrganizationList(userId,filter), new PgSubscriber<List<OrganizationAccount>>(this) {
            @Override
            public void on_Next(List<OrganizationAccount> organizations) {
                String organizationId="";
                String name="";
                if (organizations.size() == 0) {
                    ToastHelper.get(App.app).showShort(getString(R.string.empty_orgi_list));
                } else {
                    //获取到组织后，把默认组织的id存进本地，post事件方便系统配置界面使用
                    for (OrganizationAccount organization : organizations) {
                        if (organization.isIsDefault()) {
                            organizationId = organization.getOrganizationId();
                             name = organization.getOrganization().getName();
                            SpUtils.setString(Constants.key_defOrginaId, organizationId);
                            SpUtils.setString(Constants.key_defOrgina, name);
                        }
                    }

                    listen.onGetBelongOrga(organizations,organizationId,name);
                }
            }
        });

    }







    /**
     * 从网络获取功能服务列表
     */
    public void getSerListFromNet(callBack_visible_service callBack) {
        String defOrgaId = SpUtils.getString(Constants.key_defOrginaId);
        if (!TextUtils.isEmpty(defOrgaId)) {
            String url = defOrgaId + "/#service";
            addSubscription(ApiFactory.getOrgiServices(url), new PgSubscriber<List<OrganizationService>>(this) {
                @Override
                public void on_Next(List<OrganizationService> OrganizationServices) {
                    visibleOrgiService=new ArrayList<>();
                    if (OrganizationServices.size() == 0) {
                        ToastHelper.get(App.app).showShort(App.app.getString(R.string.no_services_list));
                        return;
                    }

                    //首先检查setting选项的visible值
                    List<OrganizationService> settingVisible = checkSettingVisible(OrganizationServices);

                    //如果是超级管理员，不需要检查权限
                    if (isAdmin()) {
                        //把可见的service存起来
                        ACache.get(App.app).put(Constants.cacheKey_service,(Serializable)settingVisible);
                        callBack.onGetVisibleService(settingVisible);
                        return;

                    }

                    serviceCount = settingVisible.size();
                    //普通用户，需要逐个检车服务的visible权限和seting项的visiblse属性
                    for (int i = 0; i < serviceCount; i++) {
                        checkViewPermission(settingVisible.get(i),callBack);
                    }

                }
            });
        }
    }


    public List<OrganizationService> checkSettingVisible(List<OrganizationService> servicess){
        OrganizationService.SettingsBean settings;
        for (int i=0;i<servicess.size();i++){
            settings = servicess.get(i).getSettings();
            if (settings!=null){
                if (settings.getVisible()!=null && settings.getVisible().equals("false")){
                    servicess.remove(i);
                }
            }


        }
        return servicess;
    }


    /**
     * 检查权限
     */
    public  void checkViewPermission(OrganizationService services,callBack_visible_service callBack) {
        if (index==serviceCount-1){
            callBack.onGetVisibleService(visibleOrgiService);
            ACache.get(App.app).put(Constants.cacheKey_service,(Serializable)visibleOrgiService);
        }
        addSubscription(ApiFactory.hasPermission(Double.valueOf(SpUtils.getInt(TokenManager.USERID)), services.getServiceId() + ".view"), new PgSubscriber<Boolean>(this) {
            @Override
            public void on_Next(Boolean hasPermisson) {
                if (hasPermisson){
                    visibleOrgiService.add(services);
                    index++;
                }
            }
        });


    }

    /**
    *获取组织机构的直接儿子
    */

    public void getOrgiChildren(String parentId,callBack_orgiChildren listen) {
        addSubscription(ApiFactory.getOrgiChildren(parentId), new PgSubscriber<List<Organization>>(this) {
            @Override
            public void on_Next(List<Organization> orgss) {
                listen.onGetOgriChildren(orgss);
            }

        });
    }


    /**
    *获取部门列表
    */
    public void getOrgaDepartment(String organiId,callBack_getOrgaDepartment listen) {
        String url = organiId+ Constants.special_orgi_department;
        addSubscription(ApiFactory.getOrgiDepartment(url), new PgSubscriber<List<Organization>>(this) {
            @Override
            public void on_Next(List<Organization> depts) {
                CacheUtils.putCacheExpri(Constants.cacheKey_department,depts,Constants.LONG_CACHE_TIME);
                listen.onGetOrgaDepartment(depts);
            }


        });
    }



    /**
     * 获取权限列表
     */
    public void getAuthrovityList(String orgiId,callBack_getAuthrovity callBack) {
        //TODO 可能有什么限制
        addSubscription(ApiFactory.getRoleList(), new PgSubscriber<List<Role>>(this) {

            @Override
            public void on_Next(List<Role> role) {
                if (role.size() == 0) {
                    ToastHelper.get(App.app).showShort(App.app.getString(R.string.empty_authrovity_list));
                    return;
                }
                //去除admin和自定义的role  /物业：设计师
                for (int i = 0; i < role.size(); i++) {
                    if (role.get(i).getOrganizationId()!=null || role.get(i).getName().equals("admin")) {
                        role.remove(i);
                    }
                }
                callBack.onGetAuthrovity(role);
            }

        });
    }


    /**
    *获取组织下的role
    */
    public void getOrgRoleList(String orgId, callBack_getRole listen){
        String s = orgId + Constants.special_orgi_role;
        addSubscription(ApiFactory.getOrgiRole(s), new PgSubscriber<List<Role>>(this) {
            @Override
            public void on_Next(List<Role> roles) {
                listen.onGetRoleList(roles);
            }


        });
    }


    /**
    *设置 id 是否isInherited
    */
    public void setIsInherited(String id ,IsInherited isInherited){
        addSubscription(ApiFactory.putIsInheroted(id, isInherited), new PgSubscriber(this) {
            @Override
            public void on_Next(Object o) {

            }
        });
    }



    /**
     *设置 id 是否isInherited
     */
    public void getIsInherited(String id,callBack_getIsInherited listen){
        addSubscription(ApiFactory.getIsInherited(id), new PgSubscriber<Boolean>(this) {
            @Override
            public void on_Next(Boolean isInherited) {
                listen.onGetIsInherited(isInherited);
            }
        });
    }







    public interface callBack_visible_service{
        void onGetVisibleService(List<OrganizationService> visibleOrgiService);
    }

    public interface callBack_orgiChildren{
        void onGetOgriChildren(List<Organization> orgis);
    }

    public interface callBack_getOrgaDepartment{
        void onGetOrgaDepartment(List<Organization> depts);
    }

    public interface callBack_getAuthrovity{
        void onGetAuthrovity(List<Role> authrovites);
    }


    public interface callBack_getBelongOrga{
        void onGetBelongOrga(List<OrganizationAccount> orgas,String defOrgaId,String defOrgaName);
    }

    public interface callBack_getRole{
        void onGetRoleList(List<Role> roles);
    }


    public interface callBack_getIsInherited{
        void onGetIsInherited(Boolean isInherited);
    }
}
