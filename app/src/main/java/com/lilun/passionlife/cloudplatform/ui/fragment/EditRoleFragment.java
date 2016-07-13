package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.AuthrovityListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Principal;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/22.
 */
public class EditRoleFragment extends BaseFunctionFragment{


    @Bind(R.id.head)
    TextView head;
    @Bind(R.id.iv_head)
    CircleImageView ivHead;
    @Bind(R.id.input_role_name)
    RegItemView inputRoleName;
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
    private List<Integer> isHaveIndex=new ArrayList<>();
    private int roleId;

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
        if (role!=null){
            roleName = roleName==null? role.getTitle():roleName;
//            roleDesc = roleDesc==null? role.getDescription():roleDesc;
            principals = role.getPrincipals();
            roleId = role.getId();
//            Log
        }
    }

    /**
     * 获取权限列表
     */
    public void getAuthrovityList() {
       rootActivity.getAuthrovityList(orgiId, authrovites -> {
            for(int i=0;i<authrovites.size();i++){
                for(int j=0;j<principals.size();j++){
                    if (principals.get(j).getPrincipalId().equals(authrovites.get(i).getName())){
                        authrovites.get(i).setHave(true);
//                        isHaveIndex.add(i);
                    }
                }
            }
           allAuthrovity = authrovites;
           adapter = new AuthrovityListAdapter(authrovites, false, new AuthrovityListAdapter.OnItemClickListen() {
               @Override
               public void onChoiseItemCancle(AuthrovityListAdapter authrovityListAdapter, int position) {
                   //删除对应的权限
//                   int principalId = 0;
//                   String principalName = authrovites.get(position).getName();
//                   for (int i=0;i<principals.size();i++){
//                       if (principals.get(i).getPrincipalId().equals(principalName)){
//                           principalId=principals.get(i).getId();
//                           principals.remove(i);
//                       }
//                   }
//
//                   Logger.d("prin id = "+principalId);
//                   rootActivity.addSubscription(ApiFactory.deletePrincipal(roleId, principalId), new PgSubscriber<Object>(rootActivity) {
//                       @Override
//                       public void on_Next(Object o) {
////                           then();
////                           adapter.mCBFlag.put(position,false);
////                           adapter.notifyDataSetChanged();
//                           ToastHelper.get(mCx).showShort("删除权限成功");
//                       }
//                   });
               }

           });

           lvAuthList.setAdapter(adapter);
       });
    }






    /**
     *
     */
    @OnClick(R.id.save)
    void save() {
        checkDataChanges();
    }

    /**
    *检查角色名称是否有变化
    */
    private void checkDataChanges() {
        if (TextUtils.isEmpty(inputRoleName.getInput())){
            ToastHelper.get(mCx).showShort("角色名不能为空");
            return;
        }

        if (roleName.equals(inputRoleName.getInput())){
            //检查权限是否改变
            checkAuthChanges();
        }else{
            //更新role
            Role role = new Role();
            role.setName(inputRoleName.getInput());
            rootActivity.addSubscription(ApiFactory.putRole(roleId, role), new PgSubscriber<Role>(rootActivity) {
                @Override
                public void on_Next(Role role) {
                    checkAuthChanges();

                }

            });
        }
    }

    /**
    *检查权限是否发生了变化
    */
    private void checkAuthChanges() {
        rootActivity.backStack();
        choiseAuthrisIndex.clear();
        for(int i=0;i<adapter.mCBFlag.size();i++){
            if (adapter.mCBFlag.get(i)){
                choiseAuthrisIndex.add(i);
            }
        }

        Logger.d("ps size="+principals.size());
        for (int i=0;i<choiseAuthrisIndex.size();i++){
            for (int j=0;j<principals.size();j++){
                String name = allAuthrovity.get(choiseAuthrisIndex.get(i)).getName();
                String principalId = principals.get(j).getPrincipalId();
                if (name.equals(principalId)){
                    choiseAuthrisIndex.remove(i);
                    principals.remove(j);
                }
            }
        }



        for (int i=0;i<principals.size();i++){
           int  principalId = principals.get(i).getId();
            rootActivity.addSubscription(ApiFactory.deletePrincipal(roleId, principalId), new PgSubscriber<Object>(rootActivity) {
                @Override
                public void on_Next(Object o) {
                    ToastHelper.get(mCx).showShort("删除权限成功");
                }
            });
        }





        Logger.d("应该post的权限"+choiseAuthrisIndex);
        Logger.d("应该删除的权限 size = "+principals.size());
        for(int i=0;i<choiseAuthrisIndex.size();i++){
            //TODO post对应的权限
            String principalId = allAuthrovity.get(choiseAuthrisIndex.get(i)).getName();
            Logger.d(" prinpical id="+principalId);
            Principal principal = new Principal();
            principal.setId(StringUtils.randow());
            principal.setPrincipalType("Role");
            principal.setPrincipalId(principalId);
            rootActivity.addSubscription(ApiFactory.postPrincipal(roleId, principal), new PgSubscriber<Principal>(rootActivity) {
                @Override
                public void on_Next(Principal principal) {

                }
            });

        }


    }
//
//    private int index=0;
//    private void then() {
//        if (index==choiseAuthrisIndex.size()-1){
//            rootActivity.backStack();
//            return;
//        }
//        index++;
//    }


    @Override
    public void onResume() {
        super.onResume();
        inputRoleName.setInput(TextUtils.isEmpty(roleName)?"":roleName);
    }
}
