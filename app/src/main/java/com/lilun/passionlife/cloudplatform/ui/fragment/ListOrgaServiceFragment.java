package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.ListOrgServiceAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseModuleLoadingFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.bean.RxbusEvent;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.KnowPermission;
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.custom_view.AlertDiaog;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.RxBus;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;

/**
 * Created by you on 2016/6/22.
 */
public class ListOrgaServiceFragment extends BaseModuleLoadingFragment {

    @Bind(R.id.module_list)
    GridView gvModuleList;

    private List<OrganizationService> orgaServicess;
    private ListOrgServiceAdapter adapter;
    private String serviceAddPermission = KnownServices.Module_Service + KnowPermission.addPermission;
    private String serviceEditPermission = KnownServices.Module_Service + KnowPermission.editPermission;
    private String serviceDeletePermission = KnownServices.Module_Service + KnowPermission.deletePermission;
    private String url;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getOrgaService();
    }

    @Override
    protected View createSuccessView() {
        View rootView = inflater.inflate(R.layout.fragment_module_list, null);
        gvModuleList = (GridView) rootView.findViewById(R.id.module_list);
        return rootView;
    }

    /**
     *新增或者编辑了功能服务，需要刷新组织列表视图
     */
    @Subscribe
    public void reflashServiceList(Event.reflashServiceList event){
        for(int i = 0; i< orgaServicess.size(); i++){
            if (orgaServicess.get(i).getId().equals(event.getOs().getId())){
                orgaServicess.remove(i);
                i--;
            }
        }

        orgaServicess.add(event.getOs());
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void addOrgaService(Event.AddX event) {
        openAddServiceFeagment();
    }

    @Subscribe
    public void onEditClick(Event.EditClickEvent event) {
        if (adapter!=null){
            adapter.setIsDeleteShow();
        }
    }

    @Override
    protected void onErrorPageClick() {
        super.onErrorPageClick();
        getOrgaService();
    }


    @Override
    public void onStart() {
        super.onStart();
        rootActivity.setTitle(App.app.getString(R.string.module_manager));
        rootActivity.setIsEditShow(true);
        rootActivity.setFbaShow(true);
        rootActivity.invalidateOptionsMenu();
        if (orgaServicess != null) {
            showOrgaService();
        }
    }

    //获取功能服务列表
    private void getOrgaService() {
        url = StringUtils.getCheckedOrgaId(orgiId) + Constants.special_orgi_service;
        rootActivity.addSubscription(ApiFactory.getOrgiServices(url), new PgSubscriber<List<OrganizationService>>() {
            @Override
            public void on_Next(List<OrganizationService> services) {
                orgaServicess = services;
                showOrgaService();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showError();
            }
        });
    }

    public void showOrgaService() {
        showSuccess();
        if (orgaServicess.size()==0){
            showEmpty();
        }
        if (orgaServicess != null) {
            gvModuleList.setNumColumns(orgaServicess.size()==1?1:2);
            adapter = new ListOrgServiceAdapter(orgaServicess, R.layout.item_module_list);
            adapter.setOnItemDeleteListener(this::confireDeleteItem);
            gvModuleList.setAdapter(adapter);
            gvModuleList.setOnItemClickListener((parent, view, position, id) -> openEditServiceFragment(position));
        }
    }

    /**
    *打开编辑服务界面
    */
    private void openEditServiceFragment(int position) {
        if (isAdmin() || hasCheckAddPermission) {
            editOrgaService(position);
            return;
        }
        //检查是否有编辑权限
        rootActivity.checkHasPermission(userId, serviceEditPermission, hasPermission -> {
            setHasAddCheckPermission();
            if (hasPermission) {
                editOrgaService(position);
            } else {
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });
    }

    /**
    *打开新增服务界面
    */
    private void openAddServiceFeagment() {
        if (isAdmin() || hasCheckEditPermission) {
            EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddServiceFragment(), App.app.getString(R.string.service_add)));
            return;
        }
        //检查是否有Service.add权限
        rootActivity.checkHasPermission(userId, serviceAddPermission, hasPermission -> {
            setHasEditCheckPermission();
            if (hasPermission) {
                EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddServiceFragment(), App.app.getString(R.string.service_add)));
            } else {
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });
    }

    private void editOrgaService(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.orgaService, orgaServicess.get(position - 1));
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new EditServiceFragment(), App.app.getString(R.string.service_edit));
        event.setBundle(bundle);
        EventBus.getDefault().post(event);
    }


    


    /**
     * 删除一个服务
     */
    
    public void confireDeleteItem(int position) {
        if (isAdmin() || hasCheckDeletePermission) {
            deleteService(position);
            return;
        }
        //是否有删除服务的权限
        rootActivity.checkHasPermission(userId, serviceDeletePermission, hasPermission -> {
            setHasDeleteCheckPermission();
            if (hasPermission) {
                deleteService(position);
            } else {
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });

    }

    private void deleteService(int position) {
        new AlertDiaog(rootActivity, App.app.getString(R.string.confire_delete), () -> {
            if (orgaServicess != null && orgaServicess.size() != 0) {
                String deleOrgServiceId = orgaServicess.get(position).getId();
                Logger.d("deleOrgId = " + deleOrgServiceId);
                rootActivity.addSubscription(ApiFactory.deleteOrganiService(deleOrgServiceId), new PgSubscriber<Object>(rootActivity) {
                    @Override
                    public void on_Next(Object integer) {
                        orgaServicess.remove(position);
                        setGvModuleColumns(gvModuleList,orgaServicess);
                        adapter.removeAt(position);
                        ToastHelper.get(App.app).showShort(App.app.getString(R.string.delete_orgservice_success));
                        //post一个事件，告诉“首页”刷新数据
                        RxBus.getDefault().send(new RxbusEvent.deleteModule(deleOrgServiceId));
                    }
                });

            }

        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootActivity.setIsEditShow(false);
        rootActivity.setFbaShow(false);
        rootActivity.invalidateOptionsMenu();
    }

}
