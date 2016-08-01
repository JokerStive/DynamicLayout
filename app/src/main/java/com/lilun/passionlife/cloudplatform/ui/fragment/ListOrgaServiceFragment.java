package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.OrgaServiceListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.base.BaseModuleListAdapter;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.bean.Service;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.KnowPermission;
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.ACache;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by you on 2016/6/22.
 */
public class ListOrgaServiceFragment extends BaseFunctionFragment implements BaseModuleListAdapter.onDeleteClickListerer {

    @Bind(R.id.module_list)
    GridView gvModuleList;

    private List<Service> services;
    private List<OrganizationService> visibleOrgiService;
    private int serviceCount;
    private double userId;
    private OrgaServiceListAdapter adapter;
    private Bundle bundle;
    private String serviceAddPermission = KnownServices.Module_Service+ KnowPermission.addPermission;
    private String serviceEditPermission = KnownServices.Module_Service+KnowPermission.editPermission;
    private String serviceDeletePermission = KnownServices.Module_Service+KnowPermission.deletePermission;

    @Override
    public View setView() {
        //设置编辑框显示
        rootActivity.setIsEditShow(true);
        return inflater.inflate(R.layout.fragment_module_list, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Logger.d("onCreate");
        userId = Double.valueOf(SpUtils.getInt(TokenManager.USERID));

    }


    @Override
    public void onStart() {
        super.onStart();
        rootActivity.setTitle(App.app.getString(R.string.module_manager));
        bundle = new Bundle();
        visibleOrgiService = (List<OrganizationService>) ACache.get(App.app).getAsObject(Constants.cacheKey_service);
        if (visibleOrgiService!=null){
            showServices(visibleOrgiService);
        }else{
            visibleOrgiService = new ArrayList<>();
            showServices(visibleOrgiService);
        }


    }

    public void showServices(List<OrganizationService> data) {
        adapter = new OrgaServiceListAdapter(data,this);
        gvModuleList.setAdapter(adapter);
        gvModuleList.setOnItemClickListener((parent, view, position, id) -> {
            if (position==0){
                if (isAdmin()){
                    EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddServiceFragment(),mCx.getString(R.string.service_add)));
                    return;
                }
                //检查是否有Service.add权限
                rootActivity.checkHasPermission(userId, serviceAddPermission, hasPermission -> {
                    if (hasPermission){
                        EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddServiceFragment(),mCx.getString(R.string.service_add)));
                    }else{
                        ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
                    }
                });
            }else{
                if (isAdmin()){
                    editOrgaService(position);
                    return;
                }
                //检查是否有编辑权限
                rootActivity.checkHasPermission(userId, serviceEditPermission, hasPermission -> {
                    if (hasPermission){
                        editOrgaService(position);
                    }else{
                        ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
                    }
                });


            }
        });

    }

    private void editOrgaService(int position) {
        bundle.putSerializable(Constants.orgaService,visibleOrgiService.get(position-1));
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new EditServiceFragment(), mCx.getString(R.string.service_edit));
        event.setBundle(bundle);
        EventBus.getDefault().post(event);
    }


    /**
    *刷新 “编辑” 显示
    */
    @Subscribe
    public void onEditClick(Event.EditClickEvent event){
        if (adapter!=null){
            setEdText(adapter);
        }
    }


    /**
    *删除一个服务
    */
    @Override
    public void onDeleteClick(int position) {
        if (isAdmin()){
            deleteService(position);
            return;
        }
        //是否有删除服务的权限
        rootActivity.checkHasPermission(userId, serviceDeletePermission, hasPermission -> {
            if (hasPermission){
                deleteService(position);
            }else{
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });

    }

    private void deleteService(int position) {
        if (visibleOrgiService!=null && visibleOrgiService.size()!=0){
            String deleOrgServiceId = visibleOrgiService.get(position).getId();
            Logger.d("deleOrgId = "+deleOrgServiceId);
            rootActivity.addSubscription(ApiFactory.deleteOrganiService(deleOrgServiceId), new PgSubscriber<Object>(rootActivity) {
                @Override
                public void on_Next(Object integer) {
                    visibleOrgiService.remove(position);
                    adapter.notifyDataSetChanged();
                    ToastHelper.get(mCx).showShort(mCx.getString(R.string.delete_orgservice_success));
                    //post一个事件，告诉“首页”刷新数据
                    EventBus.getDefault().post(new Event.deleteOrganiService());
                }
            });

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootActivity.setIsEditShow(false);
    }

}
