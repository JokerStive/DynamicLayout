package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.RoleListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.base.BaseModuleListAdapter;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.bean.Service;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.utils.CacheUtils;
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
public class RoleListFragmentCopy extends BaseFunctionFragment implements BaseModuleListAdapter.onDeleteClickListerer {

    @Bind(R.id.module_list)
    GridView gvModuleList;

    private List<Service> services;
    private String crumb_title;
    private RoleListAdapter adapter;
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
        getRoleList();
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
    *获取Role列表
    */
    private void getRoleList() {
        String filter = FilterUtils.roleFilter(orgiId);
        rootActivity.addSubscription(ApiFactory.getRoleListFilter(filter), new PgSubscriber<List<Role>>(rootActivity) {
            @Override
            public void on_Next(List<Role> roless) {
                roles = roless;
                CacheUtils.putCache(Constants.cacheKey_role,roles);
//                Logger.d(roless.size()+"");
                adapter = new RoleListAdapter(roles,RoleListFragmentCopy.this);
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

    /**
    *删除一个role
    */
    @Override
    public void onDeleteClick(int position) {
        if (roles!=null && roles.size()!=0){
            int roleId = roles.get(position).getId();
            Logger.d("roles = "+roleId);
            rootActivity.addSubscription(ApiFactory.deleteRole(roleId), new PgSubscriber<Object>(rootActivity) {
                @Override
                public void on_Next(Object integer) {
                    roles.remove(position);
                    CacheUtils.putCache(Constants.cacheKey_role,roles);
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
