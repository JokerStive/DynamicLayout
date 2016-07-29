package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.OrgaRoleListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.base.BaseModuleListAdapter;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.bean.Service;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.utils.FilterUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/22.
 */
public class ListRoleFragment extends BaseFunctionFragment implements BaseModuleListAdapter.onDeleteClickListerer {

    @Bind(R.id.module_list)
    GridView gvModuleList;

    private List<Service> services;
    private String crumb_title;
    private OrgaRoleListAdapter adapter;
    private List<Role> roles;

    @Override
    public View setView() {
        rootActivity.setIsEditShow(true);
        return inflater.inflate(R.layout.fragment_module_list, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        bundle = new Bundle();
        getOrgaRoles();
        gvModuleList.setOnItemClickListener((parent, view, position, id) -> {
            if (position==0){
                //新增角色
                EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddRoleFragment(),mCx.getString(R.string.role_add)));
            }else{
                bundle.putSerializable(Constants.role,roles.get(position-1));
                Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new EditRoleFragment(), mCx.getString(R.string.role_edit));
                event.setBundle(bundle);
                EventBus.getDefault().post(event);
            }
        });

    }

    /**
    *获取角色列表数据
    */
    private void getOrgaRoles() {
        String url  = orgiId+ Constants.special_orgi_role;
        Logger.d(orgiId);
        String filter = "{\"include\":\"principals\"}";
        rootActivity.addSubscription(ApiFactory.getOrgiRoleFilter(url, FilterUtils.role()), new PgSubscriber<List<Role>>(rootActivity) {
            @Override
            public void on_Next(List<Role> roless) {
                roles = roless;
                Logger.d("roless size = " +roless.size());
                adapter = new OrgaRoleListAdapter(roless, ListRoleFragment.this);
                gvModuleList.setAdapter(adapter);
            }

        });
    }


    @Subscribe
    public void onEditClick(Event.EditClickEvent event){
        if (adapter!=null){
            setEdText(adapter);
        }
    }

    @Override
    public void onDeleteClick(int position) {
        if (roles!=null && roles.size()!=0){
            double rolesId = (double) roles.get(position).getId();
            Logger.d("roles = "+rolesId);
            rootActivity.addSubscription(ApiFactory.deleteRole(rolesId), new PgSubscriber<Object>(rootActivity) {
                @Override
                public void on_Next(Object integer) {
                    roles.remove(position);
                    adapter.notifyDataSetChanged();
                    ToastHelper.get(mCx).showShort(mCx.getString(R.string.delete_orgRole_success));
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
