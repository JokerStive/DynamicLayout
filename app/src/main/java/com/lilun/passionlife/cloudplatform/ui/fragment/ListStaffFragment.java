package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.ListStaffAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseModuleLoadingFragment;
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

/**
 * Created by youke on 2016/6/22.
 */
public class ListStaffFragment extends BaseModuleLoadingFragment  {


    GridView gvModuleList;

    private List<OrganizationAccount> orgaAccs;
    private ListStaffAdapter adapter;
    private String staffAddPermission = KnownServices.Account_Service + KnowPermission.addPermission;
    private String staffEditPermission = KnownServices.Account_Service + KnowPermission.editPermission;
    private String staffDeletePermission = KnownServices.Account_Service + KnowPermission.deletePermission;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getStaffList();

    }

    @Override
    protected View createSuccessView() {
        View rootView = inflater.inflate(R.layout.fragment_module_list, null);
        gvModuleList = (GridView) rootView.findViewById(R.id.module_list);
        return rootView;
    }

    /**
     * 新增或者编辑了员工，需要刷新组织列表视图
     */
    @Subscribe
    public void reflashStaffList(Event.reflashStaffList event) {
        getStaffList();
    }

    @Subscribe
    public void addStaff(Event.AddX event) {
        openAddStafFragment();
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
        getStaffList();
    }

    @Override
    public void onStart() {
        super.onStart();
        rootActivity.setTitle(App.app.getString(R.string.staff_manager));
        rootActivity.setIsEditShow(true);
        rootActivity.setFbaShow(true);
        rootActivity.invalidateOptionsMenu();
        if (orgaAccs != null) {
            showStaff();
        }

    }



    /**
     * 根据过滤器获取员工列表
     */
    private void getStaffList() {
        if (loadingPager!=null){
            showLoading();
        }
        String filter = FilterUtils.orgaAccountFilter(orgiId);
        rootActivity.addSubscription(ApiFactory.getOrganizationAccountList(filter), new PgSubscriber<List<OrganizationAccount>>() {
            @Override
            public void on_Next(List<OrganizationAccount> organizationAccounts) {
                if (organizationAccounts.size() == 0) {
                    showEmpty();
                    return;
                }

                removeRepead(organizationAccounts);
                showStaff();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showError();
            }
        });
    }

    /**
     * 去除重复数据
     */
    private void removeRepead(List<OrganizationAccount> organizationAccounts) {
        orgaAccs = organizationAccounts;
        //数据去重复
        Set<OrganizationAccount> set = new TreeSet<>((lhs, rhs) -> {
            int id1 = lhs.getAccountId();
            int id2 = rhs.getAccountId();
            if (id1 == id2) {
                return 0;
            }
            return 1;
        });
        set.addAll(orgaAccs);
        orgaAccs = new ArrayList<>(set);
    }

    public void showStaff() {
        showSuccess();
        if (orgaAccs != null) {
            gvModuleList.setNumColumns(orgaAccs.size()==1?1:2);
            adapter = new ListStaffAdapter(orgaAccs, R.layout.item_module_list);
            adapter.setOnItemDeleteListener(this::confireDeleteItem);
            gvModuleList.setAdapter(adapter);
            gvModuleList.setOnItemClickListener((parent, view, position, id) -> openEditStaffFragment(position));
        }
    }


    private void openEditStaffFragment(int position) {
        if (isAdmin() || hasCheckEditPermission) {
            editeStaff(position);
            return;
        }
        rootActivity.checkHasPermission(userId, staffEditPermission, hasPermission -> {
            setHasEditCheckPermission();
            if (hasPermission) {
                editeStaff(position);
            } else {
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });
    }

    private void openAddStafFragment() {
        if (isAdmin() || hasCheckAddPermission) {
            EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddStaffFragment(), App.app.getString(R.string.staff_add)));
            return;
        }
        rootActivity.checkHasPermission(userId, staffAddPermission, hasPermission -> {
            setHasAddCheckPermission();
            if (hasPermission) {
                EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddStaffFragment(), App.app.getString(R.string.staff_add)));
            } else {
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });
    }

    /**
     * 进入员工编辑界面
     */
    private void editeStaff(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.orgaAccount, orgaAccs.get(position));
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new EditStaffFragment(), App.app.getString(R.string.staff_edit));
        event.setBundle(bundle);
        EventBus.getDefault().post(event);
    }



    public void confireDeleteItem(int position) {
        if (isAdmin() || hasCheckDeletePermission) {
            deleteStaff(position);
            return;
        }
        //是否有删除员工的权限
        rootActivity.checkHasPermission(userId, staffDeletePermission, hasPermission -> {
            setHasDeleteCheckPermission();
            if (hasPermission) {
                deleteStaff(position);
            } else {
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });
    }

    private void deleteStaff(int position) {
        new AlertDiaog(rootActivity, App.app.getString(R.string.confire_delete), () -> {
            if (orgaAccs != null && orgaAccs.size() != 0) {
                double ocId = (double) orgaAccs.get(position).getId();
                rootActivity.addSubscription(ApiFactory.deleteOrgaAccount(ocId), new PgSubscriber<Object>(rootActivity) {
                    @Override
                    public void on_Next(Object object) {
                        orgaAccs.remove(position);
                        setGvModuleColumns(gvModuleList,orgaAccs);
                        adapter.removeAt(position);
                        ToastHelper.get(App.app).showShort(App.app.getString(R.string.delete_staff_success));
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
