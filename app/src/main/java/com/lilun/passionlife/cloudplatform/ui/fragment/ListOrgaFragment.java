package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.OrgaListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.base.BaseModuleListAdapter;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;

/**
 * Created by youke on 2016/6/22.
 */
public class ListOrgaFragment extends BaseFunctionFragment implements BaseModuleListAdapter.onDeleteClickListerer {

    @Bind(R.id.module_list)
    GridView gvModuleList;

    private List<Organization> orgiChildren;
    private OrgaListAdapter adapter;

    @Override
    public View setView() {
        //设置编辑框显示
        rootActivity.setTitle(mCx.getString(R.string.organization));
        rootActivity.setIsEditShow(true);
        return inflater.inflate(R.layout.fragment_module_list, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        bundle = new Bundle();
        getOrgiChildren();

        gvModuleList.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddOrganizationFragment(), mCx.getString(R.string.add_orgi)));
            }else{
                bundle.putSerializable("organiChildren",orgiChildren.get(position-1));
                Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new EditOrganizationFragment(), mCx.getString(R.string.edit_orgi));
                event.setBundle(bundle);
                EventBus.getDefault().post(event);
            }
        });

    }

    public void getOrgiChildren() {
            String defOrgId = SpUtils.getString(Constants.key_currentOrgaId);
            if (!TextUtils.isEmpty(defOrgId)){
                rootActivity.addSubscription(ApiFactory.getOrgiChildren(defOrgId), new PgSubscriber<List<Organization>>(rootActivity) {
                    @Override
                    public void on_Next(List<Organization> organizations) {
                        orgiChildren=organizations;
//                        Logger.d("size  ="+orgiChildren.size());
                        adapter = new OrgaListAdapter(organizations,ListOrgaFragment.this);
                        gvModuleList.setAdapter(adapter);
                    }
                });
            }

        else{

        }

    }


    @Subscribe
    public void onEditClick(Event.EditClickEvent event){
        //TODO 检查当前用户有咩有删除组织机构的权限
        setEdText(adapter);
    }


    /**
     *删除其中的一个组织
     */
    @Override
    public void onDeleteClick(int position) {
        if (orgiChildren!=null && orgiChildren.size()!=0){
            String deleOrgId = orgiChildren.get(position).getId();
            Logger.d("deleOrgId = "+deleOrgId);
            rootActivity.addSubscription(ApiFactory.deleteOrganization(deleOrgId), new PgSubscriber<Object>(rootActivity) {
                @Override
                public void on_Next(Object integer) {
                    orgiChildren.remove(position);
                    adapter.notifyDataSetChanged();
                    ToastHelper.get(mCx).showShort(mCx.getString(R.string.delete_orgi_success));
                }
            });

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //设置编辑框框不显示
        rootActivity.setIsEditShow(false);
    }


}
