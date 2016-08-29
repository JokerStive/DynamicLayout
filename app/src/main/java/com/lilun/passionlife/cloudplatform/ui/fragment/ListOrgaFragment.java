package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.ListOrganizationAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseModuleLoadingFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.common.Admin;
import com.lilun.passionlife.cloudplatform.common.KnowPermission;
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.custom_view.AlertDiaog;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;

/**
 * Created by youke on 2016/6/22.
 */
public class ListOrgaFragment extends BaseModuleLoadingFragment{

    @Bind(R.id.module_list)
    GridView gvModuleList;

    private List<Organization> orgiChildren;
    private ListOrganizationAdapter adapter;
    private String orgaAddPermission = KnownServices.Organization_Service + KnowPermission.addPermission;
    private String orgaEditPermission = KnownServices.Organization_Service + KnowPermission.editPermission;
    private String orgaDeletePermission = KnownServices.Organization_Service + KnowPermission.deletePermission;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getOrgiChildren();

    }

    @Override
    protected View createSuccessView() {
        View rootView = inflater.inflate(R.layout.fragment_module_list, null);
        gvModuleList = (GridView) rootView.findViewById(R.id.module_list);
        return rootView;
    }

    /**
    *新增或者编辑了组织，需要刷新组织列表视图
    */
    @Subscribe
    public void reflashOrgaList(Event.reflashOrgaList event){
        getOrgiChildren();
    }

    @Subscribe
    public void addOrga(Event.AddX event) {
        openAddOrgaFragment();
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
        getOrgiChildren();
    }

    @Override
    public void onStart() {
        super.onStart();
        rootActivity.setTitle(App.app.getString(R.string.organization));
        rootActivity.setIsEditShow(true);
        rootActivity.setFbaShow(true);
        rootActivity.invalidateOptionsMenu();
    }




    /**
    *打开新增组织界面
    */
    private void openAddOrgaFragment() {
        if (isAdmin() || hasCheckAddPermission){
            EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddOrganizationFragment(), App.app.getString(R.string.add_orgi)));
        }else{
            rootActivity.checkHasPermission(userId, orgaAddPermission, hasPermission -> {
                setHasAddCheckPermission();
                if (hasPermission) {
                    EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddOrganizationFragment(), App.app.getString(R.string.add_orgi)));
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
        Bundle bundle = new Bundle();
        bundle.putSerializable("organiChildren",orgiChildren.get(position));
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new EditOrganizationFragment(), App.app.getString(R.string.edit_orgi));
        event.setBundle(bundle);
        EventBus.getDefault().post(event);
    }

    /**
    *获取组织列表
    */
    public void getOrgiChildren() {
        if (loadingPager!=null){
            showLoading();
        }
                rootActivity.addSubscription(ApiFactory.getOrgiChildren(orgiId), new PgSubscriber<List<Organization>>() {
                    @Override
                    public void on_Next(List<Organization> organizations) {

                        for(int i=0;i<organizations.size();i++){
                            String id = organizations.get(i).getId();
                            if (Admin.isRootOrganization(id)){
                                organizations.remove(organizations.get(i));
                                i--;
                            }
                        }
                        if (organizations.size()==0){
                            showEmpty();
                            return;
                        }
                        orgiChildren=organizations;
                        showOrganization();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        showError();
                    }
                });



    }

    private void showOrganization() {
        showSuccess();
        if (orgiChildren!=null){
            gvModuleList.setNumColumns(orgiChildren.size()==1?1:2);
            adapter = new ListOrganizationAdapter(orgiChildren,R.layout.item_module_list);
            adapter.setOnItemDeleteListener(this::confireDeleteItem);
            gvModuleList.setAdapter(adapter);
            gvModuleList.setOnItemClickListener((parent, view, position, id) -> openEditOrgaFragment(position));
        }
    }


    /**
     *删除其中的一个组织
     */

    public void confireDeleteItem(int position) {
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
                        setGvModuleColumns(gvModuleList,orgiChildren);
                        adapter.removeAt(position);
                        ToastHelper.get(App.app).showShort(App.app.getString(R.string.delete_orgi_success));
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
