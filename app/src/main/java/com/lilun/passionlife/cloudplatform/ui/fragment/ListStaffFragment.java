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
import com.lilun.passionlife.cloudplatform.custom_view.AlertDiaog;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.FilterUtils;
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


    @Override
    public View setView() {
        rootActivity.setTitle(mCx.getString(R.string.staff_manager));
        rootActivity.setIsEditShow(true);
        return inflater.inflate(R.layout.fragment_module_list, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getStaffList();

    }

    /**
     *新增或者编辑了员工，需要刷新组织列表视图
     */
    @Subscribe
    public void reflashStaffList(Event.reflashStaffList event){
        getStaffList();
    }

    @Override
    public void onStart() {
        super.onStart();
        bundle=new Bundle();
        if (adapter!=null){
            gvModuleList.setAdapter(adapter);
        }
        gvModuleList.setOnItemClickListener((parent, view, position, id) -> {
            if (position==0){
                openAddStafFragment();
            }else{
                openEditStaffFragment(position);

            }
        });

    }

    private void openEditStaffFragment(int position) {
        if (isAdmin() || hasCheckEditPermission){
            editeStaff(position);
            return;
        }
        rootActivity.checkHasPermission(userId,staffEditPermission,hasPermission -> {
            setHasEditCheckPermission();
            if (hasPermission){
                editeStaff(position);
            }else{
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });
    }

    private void openAddStafFragment() {
        if (isAdmin() || hasCheckAddPermission){
            EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddStaffFragment(),mCx.getString(R.string.staff_add)));
            return;
        }
        rootActivity.checkHasPermission(userId, staffAddPermission, hasPermission -> {
            setHasAddCheckPermission();
            if (hasPermission){
                EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddStaffFragment(),mCx.getString(R.string.staff_add)));
            }else{
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
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
        if (isAdmin() || hasCheckDeletePermission){
            deleteStaff(position);
            return;
        }
        //是否有删除员工的权限
        rootActivity.checkHasPermission(userId, staffDeletePermission, hasPermission -> {
            setHasDeleteCheckPermission();
            if (hasPermission){
                deleteStaff(position);
            }else{
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });
    }

    private void deleteStaff(int position) {
        new AlertDiaog(rootActivity, App.app.getString(R.string.confire_delete), () -> {
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

        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootActivity.setIsEditShow(false);
    }
}
