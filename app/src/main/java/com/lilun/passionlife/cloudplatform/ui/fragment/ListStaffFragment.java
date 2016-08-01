package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.OrgaAccountListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.base.BaseModuleListAdapter;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.KnowPermission;
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.FilterUtils;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.Bind;

/**
 * Created by youke on 2016/6/22.
 */
public class ListStaffFragment extends BaseFunctionFragment implements BaseModuleListAdapter.onDeleteClickListerer{

    @Bind(R.id.module_list)
    GridView gvModuleList;

    private List<OrganizationAccount> orgaAccs;
    private OrgaAccountListAdapter adapter;
    private String staffAddPermission = KnownServices.Account_Service+ KnowPermission.addPermission;
    private String staffEditPermission = KnownServices.Account_Service+KnowPermission.editPermission;
    private String staffDeletePermission = KnownServices.Account_Service+KnowPermission.deletePermission;
    private Double userId;

    @Override
    public View setView() {
        rootActivity.setTitle(mCx.getString(R.string.staff_manager));
        rootActivity.setIsEditShow(true);
        return inflater.inflate(R.layout.fragment_module_list, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        bundle=new Bundle();
        userId = Double.valueOf(SpUtils.getInt(TokenManager.USERID));
        getStaffList();
        gvModuleList.setOnItemClickListener((parent, view, position, id) -> {
            if (position==0){
                if (isAdmin()){
                    EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddStaffFragment(),mCx.getString(R.string.staff_add)));
                    return;
                }
                rootActivity.checkHasPermission(userId, staffAddPermission, hasPermission -> {
                    if (hasPermission){
                        EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddStaffFragment(),mCx.getString(R.string.staff_add)));
                    }else{
                        ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
                    }
                });
            }else{
                if (isAdmin()){
                    editeStaff(position);
                    return;
                }
                rootActivity.checkHasPermission(userId,staffEditPermission,hasPermission -> {
                    if (hasPermission){
                        editeStaff(position);
                    }else{
                        ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
                    }
                });

            }
        });

    }

    /**
    *进入员工编辑界面
    */
    private void editeStaff(int position) {
        bundle.putSerializable(Constants.orgaAccount,orgaAccs.get(position-1));
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new EditStaffFragment(), mCx.getString(R.string.staff_edit));
        event.setBundle(bundle);
        EventBus.getDefault().post(event);
    }

    /**
    *根据过滤器获取员工列表
    */
    private void getStaffList() {
        String filter = FilterUtils.orgaAccountFilter(orgiId);
        rootActivity.addSubscription(ApiFactory.getOrganizationAccountList(filter), new PgSubscriber<List<OrganizationAccount>>(rootActivity) {
            @Override
            public void on_Next(List<OrganizationAccount> organizationAccounts) {
                orgaAccs = organizationAccounts;
                //数据去重复
                Set<OrganizationAccount>  set = new TreeSet<>((lhs, rhs) -> {
                    int id1 = lhs.getAccountId();
                    int id2 = rhs.getAccountId();
                    if (id1 == id2) {
                        return 0;
                    }
                    return 1;
                });
                set.addAll(orgaAccs);
                orgaAccs = new ArrayList<>(set);
                adapter = new OrgaAccountListAdapter(orgaAccs,ListStaffFragment.this);
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
        if (isAdmin()){
            deleteStaff(position);
            return;
        }
        //是否有删除员工的权限
        rootActivity.checkHasPermission(userId, staffDeletePermission, hasPermission -> {
            if (hasPermission){
                deleteStaff(position);
            }else{
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });
    }

    private void deleteStaff(int position) {
        if (orgaAccs!=null && orgaAccs.size()!=0){
            double ocId = (double) orgaAccs.get(position).getId();
            rootActivity.addSubscription(ApiFactory.deleteOrgaAccount(ocId), new PgSubscriber<Object>(rootActivity) {
                @Override
                public void on_Next(Object object) {
                    orgaAccs.remove(position);
                    adapter.notifyDataSetChanged();
                    ToastHelper.get(mCx).showShort(mCx.getString(R.string.delete_staff_success));
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
