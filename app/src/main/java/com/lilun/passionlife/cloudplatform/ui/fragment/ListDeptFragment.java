package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.OrgaListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.base.BaseModuleListAdapter;
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

import butterknife.Bind;
import rx.Observable;

/**
 * Created by youke on 2016/6/22.
 */
public class ListDeptFragment extends BaseFunctionFragment implements BaseModuleListAdapter.onDeleteClickListerer {

    @Bind(R.id.module_list)
    GridView gvModuleList;

    private List<Organization> depts;
    private OrgaListAdapter adapter;
    private String deptAddPermission = KnownServices.Department_Service + KnowPermission.addPermission;
    private String deptEditPermission = KnownServices.Department_Service + KnowPermission.editPermission;
    private String deptDeletePermission = KnownServices.Department_Service + KnowPermission.deletePermission;
    private Double userId;
    private Boolean isInherited = false;
    private String isInheritedId;
    private int operationIndex;
    private int mPosition;
    private String url;


    @Override
    public View setView() {
        //设置编辑框显示
        rootActivity.setTitle(mCx.getString(R.string.dept_manager));
        rootActivity.setIsEditShow(true);
        return inflater.inflate(R.layout.fragment_module_list, null);
    }


    /**
     *新增或者编辑了角色，需要刷新组织列表视图
     */
    @Subscribe
    public void reflashDeptList(Event.reflashDeptList event){
        getOrgiChildren();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getOrgiChildren();
        if (Admin.isRootOrganization(orgiId)) {
            return;
        }
        isInheritedId = StringUtils.getCheckedOrgaId(orgiId) + Constants.special_orgi_department;
        rootActivity.getIsInherited(isInheritedId, isInherited -> {
            this.isInherited = isInherited;
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        bundle = new Bundle();
        if (adapter!=null){
            gvModuleList.setAdapter(adapter);
        }
        gvModuleList.setOnItemClickListener((parent, view, position, id) -> {
            operationIndex = position;
            mPosition = position;
            if (position == 0) {
                openAddDeptFragment();
            } else {
                openEditDeptFragment();
            }
        });

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
            EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddDeptFragment(), mCx.getString(R.string.dept_add)));
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
        bundle.putSerializable(Constants.orgaDept, depts.get(position - 1));
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new EditDeptFragment(), mCx.getString(R.string.edit_dept));
        event.setBundle(bundle);
        EventBus.getDefault().post(event);
    }

    /**
     * 获取部门列表
     */
    public void getOrgiChildren() {
        url = StringUtils.getCheckedOrgaId(orgiId) + Constants.special_orgi_department;
        rootActivity.addSubscription(ApiFactory.getOrgiDepartment(url), new PgSubscriber<List<Organization>>(rootActivity) {
            @Override
            public void on_Next(List<Organization> organizations) {
                depts = organizations;
                adapter = new OrgaListAdapter(organizations, ListDeptFragment.this);
                gvModuleList.setAdapter(adapter);
            }
        });

    }


    @Subscribe
    public void onEditClick(Event.EditClickEvent event) {
        setEdText(adapter);
    }


    /**
     * 删除其中的一个部门
     */
    @Override
    public void onDeleteClick(int position) {
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
                        adapter.notifyDataSetChanged();
                        ToastHelper.get(mCx).showShort(mCx.getString(R.string.delete_dept_success));
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
