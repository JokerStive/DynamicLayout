package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.AuthrovityListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Principal;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.custom_view.AlertDiaog;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by Administrator on 2016/6/22.
 */
public class EditRoleFragment extends BaseFunctionFragment {


    @Bind(R.id.head)
    TextView head;

    @Bind(R.id.iv_head)
    CircleImageView ivHead;

    @Bind(R.id.input_role_name)
    RegItemView inputRoleName;

    @Bind(R.id.input_role_desc)
    RegItemView inputRoleDesc;

    @Bind(R.id.lv_auth_list)
    ListView lvAuthList;

/*
* 1.post一个Role
* 2.轮询post选中的权限
*
* */

    private List<Integer> choiseAuthrisIndex = new ArrayList<>();
    private AuthrovityListAdapter adapter;
    private Role role;
    private String roleName;
    private List<Principal> principals;
    private List<Role> allAuthrovity;
    private List<Integer> isHaveIndex = new ArrayList<>();
    private Double roleId;
    private String roleDesc;
    private boolean needGetPrinsFromNet;

    @Override
    public View setView() {
        Bundle bundle = getArguments();
        //获取传递的参数
        if (bundle != null) {
            role = (Role) bundle.get(Constants.role);

        }
        View view = inflater.inflate(R.layout.fragment_add_role, null);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        setInitData();
        getAuthrovityList();
    }





    private void setInitData() {
        if (role != null) {
            roleName = roleName == null ? role.getTitle() : roleName;
            roleDesc = roleDesc==null? role.getDescription():roleDesc;
            principals = role.getPrincipals();
            roleId = (Double) role.getId();
//            getRolePrincipals(roleId);
        }
    }






    /**
     * 获取权限列表
     */
    public void getAuthrovityList() {
        rootActivity.getAuthrovityList(authrovites -> {
                rootActivity.addSubscription(ApiFactory.getRolePrincials(roleId), new PgSubscriber<List<Principal>>() {
                    @Override
                    public void on_Next(List<Principal> principals) {
                        EditRoleFragment.this.principals = principals;
                        setPrincipalsData(authrovites);
                    }
                });
        });
    }

    /**
    *设置哪些权限勾选
    */
    private void setPrincipalsData(List<Role> authrovites) {
        for (int i = 0; i < authrovites.size(); i++) {
            for (int j = 0; j < principals.size(); j++) {
                if (principals.get(j).getPrincipalId().equals(authrovites.get(i).getName())) {
                    authrovites.get(i).setHave(true);
//                        isHaveIndex.add(i);
                }
            }
        }
        allAuthrovity = authrovites;
        adapter = new AuthrovityListAdapter(authrovites, false);
        //本来拥有的权限取消
        adapter.setOnHaveCancleListener((position, item_authro_choise) -> new AlertDiaog(rootActivity, "确定删除？", () -> {
            double principalId = 0.0;
            String namne = allAuthrovity.get(position).getName();
            for (Principal p : principals) {
                if (p.getPrincipalId().equals(namne)) {
                    principalId = (double) p.getId();
                }
            }
            Logger.d("要删除权限的id=" + principalId);
            rootActivity.addSubscription(ApiFactory.deletePrincipal(roleId, principalId), new PgSubscriber<Object>(rootActivity) {
                @Override
                public void on_Next(Object o) {
                    adapter.setmCBFlag(position, false);
                    item_authro_choise.setEnabled(false);
                    ToastHelper.get(mCx).showShort("删除权限成功");
                }
            });
        }));

        lvAuthList.setAdapter(adapter);
    }


    /**
     *
     */
    @OnClick(R.id.save)
    void save() {
        checkDataChanges();
    }

    /**
     * 检查角色名称是否有变化
     */
    private void checkDataChanges() {
        if (!StringUtils.checkEmpty(inputRoleName.getInput(),inputRoleDesc.getInput())) {
            return;
        }

        checkRoleChanges();

    }


    private Role newRole() {
        Role role = new Role();
        role.setName(inputRoleName.getInput());
        role.setDescription(inputRoleDesc.getInput());
        return role;
    }

    private void checkRoleChanges() {
        Observable observable;
        List<Principal> principals = checkPrincipalChange();

        if (checkRoleChange() && principals.size()!=0){
            observable = putRole().concatMap(role -> putPrincilaps(principals));
        }else if(principals.size()!=0){
            observable = putPrincilaps(principals);
        }else{
            observable = putRole();
        }

      if (observable!=null){
          rootActivity.addSubscription(observable, new PgSubscriber(rootActivity) {
              @Override
              public void on_Next(Object o) {
                  EventBus.getDefault().post(new Event.reflashRoleList());
                  rootActivity.backStack();
              }
          });
      }else{
          rootActivity.backStack();
      }

    }

    private List<Principal> checkPrincipalChange() {
        choiseAuthrisIndex.clear();
        for (int i = 0; i < adapter.mCBFlag.size(); i++) {
            if (adapter.mCBFlag.get(i)) {
                choiseAuthrisIndex.add(i);
            }
        }


        List<Principal>  principalsList = new ArrayList<>();
        for (int i = 0; i < choiseAuthrisIndex.size(); i++) {
            //TODO post对应的权限
            Role role = allAuthrovity.get(choiseAuthrisIndex.get(i));
            if (!role.isHave()) {
                Principal principal = new Principal();
                principal.setPrincipalType("Role");
                principal.setPrincipalId(role.getName());
                principalsList.add(principal);
            }

        }

        return principalsList;
    }

    private Observable putPrincilaps(List<Principal> principals) {
        return ApiFactory.postPrincipal(roleId, principals);
    }

    private Observable<Role> putRole() {
        return ApiFactory.putRole(roleId, newRole());
    }

    private boolean checkRoleChange() {
        if (!roleName.equals(inputRoleName.getInput()) || !roleDesc.equals(inputRoleDesc.getInput())) {
            return true;
        }
        return false;
    }





    @Override
    public void onResume() {
        super.onResume();
        inputRoleName.setInput(TextUtils.isEmpty(roleName) ? "" : roleName);
        inputRoleDesc.setInput(TextUtils.isEmpty(roleDesc) ? "" : roleDesc);
    }
}
