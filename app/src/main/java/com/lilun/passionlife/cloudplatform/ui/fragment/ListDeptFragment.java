package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.ListOrganizationAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseModuleLoadingFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.IsInherited;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.common.Admin;
import com.lilun.passionlife.cloudplatform.common.Constants;
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

import rx.Observable;

/**
 * Created by youke on 2016/6/22.
 */
public class ListDeptFragment extends BaseModuleLoadingFragment {

    
    GridView gvModuleList;

    private List<Organization> depts;
    private ListOrganizationAdapter adapter;
    private String deptAddPermission = KnownServices.Department_Service + KnowPermission.addPermission;
    private String deptEditPermission = KnownServices.Department_Service + KnowPermission.editPermission;
    private String deptDeletePermission = KnownServices.Department_Service + KnowPermission.deletePermission;
    private Boolean isInherited = false;
    private String isInheritedId;
    private int operationIndex;
    private int mPosition;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable<List<Organization>> observable;
        if (Admin.isRootOrganization(orgiId)) {
            observable = getDeptsObser(null);
        } else {
            observable = checkIsInHeritedObser().concatMap(this::getDeptsObser);
        }

        getDepts(observable);
    }



    @Override
    protected View createSuccessView() {
        View rootView = inflater.inflate(R.layout.fragment_module_list, null);
        gvModuleList = (GridView) rootView.findViewById(R.id.module_list);
        return rootView;
    }


    @Subscribe
    public void reflashRoleList(Event.reflashRoleList event) {
        getDepts(getDeptsObser(null));
    }


    @Subscribe
    public void addStaff(Event.AddX event) {
        operationIndex=0;
        openAddDeptFragment();
    }

    @Subscribe
    public void onEditClick(Event.EditClickEvent event) {
        if (adapter != null) {
            adapter.setIsDeleteShow();
        }
    }


    @Override
    protected void onErrorPageClick() {
        super.onErrorPageClick();
        getDepts(isInherited?getDeptsObser(null): checkIsInHeritedObser().concatMap(this::getDeptsObser));
    }



    @Override
    public void onStart() {
        super.onStart();
        rootActivity.setTitle(App.app.getString(R.string.dept_manager));
        rootActivity.setIsEditShow(true);
        rootActivity.setFbaShow(true);
        rootActivity.invalidateOptionsMenu();
        if (adapter!=null){
            showDepts();
        }
    }


    /**
     * 检查是否是继承
     */
    private Observable<Boolean> checkIsInHeritedObser() {
        isInheritedId = StringUtils.getCheckedOrgaId(orgiId) + Constants.special_orgi_department;
        return ApiFactory.getIsInherited(isInheritedId);
    }


    /**
    *获取部门列表的obser
    */
    private Observable<List<Organization>>  getDeptsObser(Boolean isInherited){
        if (isInherited!=null){
            this.isInherited = isInherited;
        }
        url = StringUtils.getCheckedOrgaId(orgiId) + Constants.special_orgi_department;
        return ApiFactory.getOrgiDepartment(url);
    }

    private void getDepts(Observable<List<Organization>> observable) {
        rootActivity.addSubscription(observable, new PgSubscriber<List<Organization>>() {
            @Override
            public void on_Next(List<Organization> organizations) {
                if (organizations.size()==0){
                    showEmpty();
                    return;
                }
                depts = organizations;
                showDepts();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

            }

        });
    }

    private void showDepts() {
        showSuccess();
        if (depts!=null){
            setGvModuleColumns(gvModuleList,depts);
            adapter = new ListOrganizationAdapter(depts,R.layout.item_module_list);
            adapter.setOnItemDeleteListener(this::confireDeleteItem);
            gvModuleList.setAdapter(adapter);
            gvModuleList.setOnItemClickListener((parent, view, position, id) -> {
                operationIndex=2;
                mPosition=position;
                openEditDeptFragment();
            });
        }
    }

    /**
    *打开编辑部门界面
    */
    private void openEditDeptFragment() {
        if (isAdmin() || hasCheckEditPermission) {
            copyDept();
            return;
        }
        rootActivity.checkHasPermission(userId, deptEditPermission, hasPermission -> {
            setHasEditCheckPermission();
            if (hasPermission) {
                copyDept();
            } else {
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });
    }

    /**
    *打开新增部门界面
    */
    private void openAddDeptFragment() {
        if (isAdmin() || hasCheckAddPermission) {
            copyDept();
            return;
        }
        rootActivity.checkHasPermission(userId, deptAddPermission, hasPermission -> {
            setHasAddCheckPermission();
            if (hasPermission) {
                copyDept();
            } else {
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });
    }

    /**
     * 复制一份roles到当前组织下面
     */
    private void copyDept() {
        if (isInherited && checkHasDept()) {
            new AlertDiaog(rootActivity, App.app.getString(R.string.isInhe_copy_dept_operate), () -> mergeObservable());
        } else {
            EventBus.getDefault().post(new Event.CopyDeptSuccess());
        }
    }

    private void mergeObservable() {
        Observable<List<Organization>> observable = ApiFactory.putIsInheroted(isInheritedId, new IsInherited(false))
                .concatMap(o -> ApiFactory.postOrganizations(depts));
        rootActivity.addSubscription(observable, new PgSubscriber(rootActivity) {
            @Override
            public void on_Next(Object o) {
                Logger.d("部门复制成功");
                EventBus.getDefault().post(new Event.CopyDeptSuccess());
            }
        });
    }

    /**
     * 检查是否有部门需要复制
     */
    private boolean checkHasDept() {
        if (depts != null && depts.size() != 0) {
            for (Organization dept : depts) {
                String deptName = dept.getName() == null ? "" : dept.getName();
                dept.setParentId(orgiId);
                dept.setId(orgiId + Constants.special_orgi_department + "/" + deptName);
            }

            return true;
        }

        return false;
    }


    /**
     * 复制成功后续操作
     */
    @Subscribe
    public void copySuccess(Event.CopyDeptSuccess event) {
        if (operationIndex == 0) {
            EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddDeptFragment(), App.app.getString(R.string.dept_add)));
        } else if (operationIndex == -1) {
            deleteDept(mPosition);
        } else {
            editDept(mPosition);
        }
    }


    /**
     * 部门编辑
     */
    private void editDept(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.orgaDept, depts.get(position - 1));
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new EditDeptFragment(), App.app.getString(R.string.edit_dept));
        event.setBundle(bundle);
        EventBus.getDefault().post(event);
    }





    /**
     * 删除其中的一个部门
     */

    public void confireDeleteItem(int position) {
        operationIndex = -1;
        mPosition = position;
        if (isAdmin() || hasCheckDeletePermission) {
            copyDept();
            return;
        }
        rootActivity.checkHasPermission(userId, deptDeletePermission, hasPermission -> {
            setHasDeleteCheckPermission();
            if (hasPermission) {
                copyDept();
            } else {
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });


    }

    /**
     * 删除部门
     */
    private void deleteDept(int position) {
        new AlertDiaog(rootActivity, App.app.getString(R.string.confire_delete), () -> {
            if (depts != null && depts.size() != 0) {
                String deleOrgId = depts.get(position).getId();
                Logger.d("deleOrgId = " + deleOrgId);
                rootActivity.addSubscription(ApiFactory.deleteOrganization(deleOrgId), new PgSubscriber<Object>(rootActivity) {
                    @Override
                    public void on_Next(Object integer) {
                        depts.remove(position);
                        setGvModuleColumns(gvModuleList,depts);
                        adapter.removeAt(position);
                        ToastHelper.get(App.app).showShort(App.app.getString(R.string.delete_dept_success));
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
