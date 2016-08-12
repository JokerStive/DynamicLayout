package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.OrgaListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.base.BaseModuleListAdapter;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.common.KnowPermission;
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.custom_view.AlertDiaog;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
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
    private String orgaAddPermission = KnownServices.Organization_Service + KnowPermission.addPermission;
    private String orgaEditPermission = KnownServices.Organization_Service + KnowPermission.editPermission;
    private String orgaDeletePermission = KnownServices.Organization_Service + KnowPermission.deletePermission;


    @Override
    public View setView() {
        //设置编辑框显示
        rootActivity.setTitle(mCx.getString(R.string.organization));
        rootActivity.setIsEditShow(true);
        return inflater.inflate(R.layout.fragment_module_list, null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getOrgiChildren();

    }

    /**
    *新增或者编辑了组织，需要刷新组织列表视图
    */
    @Subscribe
    public void reflashOrgaList(Event.reflashOrgaList event){
        getOrgiChildren();
    }

    @Override
    public void onStart() {
        super.onStart();
        bundle = new Bundle();
        if (adapter!=null){
            gvModuleList.setAdapter(adapter);
        }
        gvModuleList.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                openAddOrgaFragment();

            }else{
                openEditOrgaFragment(position);
            }
        });
    }




    /**
    *打开新增组织界面
    */
    private void openAddOrgaFragment() {
        if (isAdmin() || hasCheckAddPermission){
            EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddOrganizationFragment(), mCx.getString(R.string.add_orgi)));
        }else{
            rootActivity.checkHasPermission(userId, orgaAddPermission, hasPermission -> {
                setHasAddCheckPermission();
                if (hasPermission) {
                    EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddOrganizationFragment(), mCx.getString(R.string.add_orgi)));
                }else{
                    ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
                }
            });
        }
    }



    /**
     *打开编辑组织界面
     */
    private void openEditOrgaFragment(int position) {
        if (isAdmin() || hasCheckEditPermission){
            editOrga(position);
            return;
        }
        rootActivity.checkHasPermission(userId,orgaEditPermission,hasPermission -> {
            setHasEditCheckPermission();
            if (hasPermission){
                editOrga(position);
            }else{
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });
    }



    private void editOrga(int position) {
        bundle.putSerializable("organiChildren",orgiChildren.get(position-1));
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new EditOrganizationFragment(), mCx.getString(R.string.edit_orgi));
        event.setBundle(bundle);
        EventBus.getDefault().post(event);
    }

    /**
    *获取组织列表
    */
    public void getOrgiChildren() {
                rootActivity.addSubscription(ApiFactory.getOrgiChildren(orgiId), new PgSubscriber<List<Organization>>(rootActivity) {
                    @Override
                    public void on_Next(List<Organization> organizations) {
                        for(int i=0;i<organizations.size();i++){
                            String id = organizations.get(i).getId();
                            if (StringUtils.isSpecialOrga(id)){
                                organizations.remove(organizations.get(i));
                                i--;
                            }
                        }
                        orgiChildren=organizations;
                        adapter = new OrgaListAdapter(organizations,ListOrgaFragment.this);
                        gvModuleList.setAdapter(adapter);
                    }
                });

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
        if (isAdmin() || hasCheckDeletePermission){
            deleteOrga(position);
            return;
        }
        rootActivity.checkHasPermission(userId, orgaDeletePermission, hasPermission -> {
            setHasDeleteCheckPermission();
            if (hasPermission){
                deleteOrga(position);
            }else{
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });


    }

    private void deleteOrga(int position) {
        new AlertDiaog(rootActivity, App.app.getString(R.string.confire_delete), () -> {

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
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //设置编辑框框不显示
        rootActivity.setIsEditShow(false);
    }


}
