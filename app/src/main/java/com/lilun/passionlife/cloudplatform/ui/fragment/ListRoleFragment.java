package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.OrgaRoleListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.base.BaseModuleListAdapter;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.IsInherited;
import com.lilun.passionlife.cloudplatform.bean.Principal;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.common.Admin;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.KnowPermission;
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.custom_view.AlertDiaog;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.FilterUtils;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import rx.Observable;

/**
 * Created by Administrator on 2016/6/22.
 */
public class ListRoleFragment extends BaseFunctionFragment implements BaseModuleListAdapter.onDeleteClickListerer {

    @Bind(R.id.module_list)
    GridView gvModuleList;



    private OrgaRoleListAdapter adapter;
    private List<Role> roles;
    private String roleAddPermission = KnownServices.Role_Service + KnowPermission.addPermission;
    private String roleEditPermission = KnownServices.Role_Service + KnowPermission.editPermission;
    private String roleDeletePermission = KnownServices.Role_Service + KnowPermission.deletePermission;
    private Boolean isInherited=false;

    private int operationIndex = -1;
    private String isInheritedId;
    private int mPosition;
    private List<Role> roleList;
    private List<Map<Role, List<Principal>>> rolePrincipalsMappingList;

    @Override
    public View setView() {
        rootActivity.setTitle(mCx.getString(R.string.role_manager));
        rootActivity.setIsEditShow(true);
        return inflater.inflate(R.layout.fragment_module_list, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getOrgaRoles();
        if (Admin.isRootOrganization(orgiId)){
            return;
        }
        isInheritedId = StringUtils.getCheckedOrgaId(orgiId) + Constants.special_orgi_role;
        rootActivity.getIsInherited(isInheritedId, isInherited -> {
            this.isInherited = isInherited;
        });

    }

    /**
     *新增或者编辑了角色，需要刷新组织列表视图
     */
    @Subscribe
    public void reflashRoleList(Event.reflashRoleList event){
        getOrgaRoles();
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
                openAddRoleFragment(position);
            } else {
                openEditRoleFragment();

            }
        });

    }

    /**
    *打开编辑角色页面
    */
    private void openEditRoleFragment() {
        if (isAdmin() || hasCheckEditPermission) {
            copyRole();
            return;
        }
        rootActivity.checkHasPermission(userId, roleEditPermission, hasPermission -> {
            setHasEditCheckPermission();
            if (hasPermission) {
                copyRole();
            } else {
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });
    }

    /**
    *打开新增角色页面
    */
    private void openAddRoleFragment(int position) {
        if (position == 0) {
            if (isAdmin() || hasCheckAddPermission) {
                copyRole();
                return;
            }
            rootActivity.checkHasPermission(userId, roleAddPermission, hasPermission -> {
                setHasAddCheckPermission();
                if (hasPermission) {
                    copyRole();
                } else {
                    ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
                }
            });
        }
    }

    /**
     * 复制一份roles到当前组织下面
     */
    private void copyRole() {
        if (isInherited  && checkHasRoles()) {
            new AlertDiaog(rootActivity, App.app.getString(R.string.isInhe_copy_role_operate), this::testFlatMap);
        } else {
            EventBus.getDefault().post(new Event.CopyRoleSuccess());
        }

    }

    /**
    *concatMap
    */
    private void testFlatMap() {
        Observable<Object> objectObservable = ApiFactory.putIsInheroted(isInheritedId, new IsInherited(false))
                .concatMap(o -> postRole())
                .concatMap(this::postPrincilap);
        rootActivity.addSubscription(objectObservable, new PgSubscriber(rootActivity) {
            @Override
            public void on_Next(Object o) {
                Logger.d("复制成功");
                EventBus.getDefault().post(new Event.CopyRoleSuccess());
            }
        });
    }

    private Observable<List<Role>> postRole() {
        isInherited=false;
        return ApiFactory.postRoles(roleList);
    }


    private Observable<Object> postPrincilap(List<Role> roless) {
        roles = roless;
        ArrayList<Observable<Object>> Observables = new ArrayList<>();
        for (int i = 0; i < roless.size(); i++) {
            Role role = roless.get(i);
            Map<Role, List<Principal>> roleListMap = rolePrincipalsMappingList.get(i);
            List<Principal> principals = roleListMap.get(roleList.get(i));
            Observables.add(ApiFactory.postPrincipal((Double) role.getId(), principals));
        }
        return Observable.merge(Observables);
    }

    /**
     * 检查是否有职位列表
     */
    private boolean checkHasRoles() {
        if (roles != null && roles.size()!=0) {
            roleList = new ArrayList<>();
            rolePrincipalsMappingList = new ArrayList<>();
            Logger.d("职位当前orgaId = " + orgiId + Constants.special_orgi_role);
            for (Role role : roles) {
                Map<Role, List<Principal>> mapping = new HashMap<>();
                role.setId(null);
                String name = role.getTitle() == null ? "" : role.getTitle();
                role.setName(orgiId + ":" + name);
                role.setOrganizationId(orgiId + Constants.special_orgi_role);
                List<Principal> principals = role.getPrincipals();
                for (Principal principal : principals) {
                    principal.setId(null);
                }

                roleList.add(role);
                mapping.put(role, principals);
                rolePrincipalsMappingList.add(mapping);
            }

            return true;
        }

        return false;
    }




    @Subscribe
    public void copySuccess(Event.CopyRoleSuccess event) {
        if (operationIndex == 0) {
            EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddRoleFragment(), mCx.getString(R.string.role_add)));
        } else if (operationIndex == -1) {
            deleteRole(mPosition);
        } else {
            editRole(mPosition);
        }
    }


    private void editRole(int position) {
        bundle.putSerializable(Constants.role, roles.get(position - 1));
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new EditRoleFragment(), mCx.getString(R.string.role_edit));
        event.setBundle(bundle);
        EventBus.getDefault().post(event);
    }

    /**
     * 获取角色列表数据
     */
    private void getOrgaRoles() {
        String url = StringUtils.getCheckedOrgaId(orgiId) + Constants.special_orgi_role;
        rootActivity.addSubscription(ApiFactory.getOrgiRoleFilter(url, FilterUtils.role()), new PgSubscriber<List<Role>>(rootActivity) {
            @Override
            public void on_Next(List<Role> roless) {
                roles = roless;
                Logger.d("roless size = " + roless.size());
                adapter = new OrgaRoleListAdapter(roless, ListRoleFragment.this);
                gvModuleList.setAdapter(adapter);
            }

        });
    }


    @Subscribe
    public void onEditClick(Event.EditClickEvent event) {
        if (adapter != null) {
            setEdText(adapter);
        }
    }

    @Override
    public void onDeleteClick(int position) {
        operationIndex = -1;
        mPosition = position;
        if (isAdmin() || hasCheckDeletePermission) {
            copyRole();
            return;
        }
        rootActivity.checkHasPermission(userId, roleDeletePermission, hasPermission -> {
            setHasDeleteCheckPermission();
            if (hasPermission) {
                copyRole();
            } else {
                ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
            }
        });

    }

    private void deleteRole(int position) {
        new AlertDiaog(rootActivity, App.app.getString(R.string.confire_delete), () -> {
            if (roles != null && roles.size() != 0) {
                double rolesId = (double) roles.get(position).getId();
                Logger.d("roles = " + rolesId);
                rootActivity.addSubscription(ApiFactory.deleteRole(rolesId), new PgSubscriber<Object>(rootActivity) {
                    @Override
                    public void on_Next(Object integer) {
                        roles.remove(position);
                        adapter.notifyDataSetChanged();
                        ToastHelper.get(mCx).showShort(mCx.getString(R.string.delete_orgRole_success));
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
