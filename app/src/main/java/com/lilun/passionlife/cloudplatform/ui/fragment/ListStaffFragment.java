package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.OrgaAccountListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.base.BaseModuleListAdapter;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.utils.FilterUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;

/**
 * Created by youke on 2016/6/22.
 */
public class ListStaffFragment extends BaseFunctionFragment implements BaseModuleListAdapter.onDeleteClickListerer{

    @Bind(R.id.module_list)
    GridView gvModuleList;

    private List<OrganizationAccount> orgaAccs;
    private OrgaAccountListAdapter adapter;

    @Override
    public View setView() {
        return inflater.inflate(R.layout.fragment_module_list, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        rootActivity.setIsEditShow(true);
        getStaffList();
        gvModuleList.setOnItemClickListener((parent, view, position, id) -> {
            if (position==0){
                EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddStaffFragment(),mCx.getString(R.string.staff_add)));

            }
        });

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
