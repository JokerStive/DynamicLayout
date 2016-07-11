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
    private List<Integer> choiseAuthris = new ArrayList<>();
    private String crumb_title;
    private List<Role> roles;
    private int index;
    private AuthrovityListAdapter adapter;
    private Role role;
    private String roleName;
    private String roleDesc;
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

    @OnClick(R.id.btn_auth_add)
    /**
     *角色--新增权限
     */
    void role_auth_add() {
//        choiseAuthrisIndex = StringUtils.removeRepet(choiseAuthrisIndex);
//        Logger.d("---"+choiseAuthrisIndex);
//        EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddAuthrovityFragment(), mCx.getString(R.string.authority_add)));
    }


    @Override
    public void onStart() {
        super.onStart();
        bundle = new Bundle();
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
           if (principals==null || principals.size()==0){return;}
            for(int i=0;i<authrovites.size();i++){
                for(int j=0;j<principals.size();j++){
                    if (principals.get(j).getPrincipalId().equals(authrovites.get(i).getName())){
                        Logger.d("has");
                        authrovites.get(i).setHave(true);
                        isHaveIndex.add(i);
                    }
                }
            }
           allAuthrovity = authrovites;
           adapter = new AuthrovityListAdapter(allAuthrovity, false, (authrovityListAdapter, position) -> {

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
        }
    }

    /**
    *检查权限是否发生了变化
    */
    private void checkAuthChanges() {
        choiseAuthris.clear();
        for(int i=0;i<adapter.mCBFlag.size();i++){
            if (adapter.mCBFlag.get(i)){
                choiseAuthris.add(i);
            }
        }

        Logger.d(" 1"+choiseAuthris);
        for(int i=0;0<choiseAuthris.size();i++){
            for(int j=0;j<isHaveIndex.size();j++){
                Logger.d("i = "+i);
                Logger.d("j = "+j);
                Integer ch = choiseAuthris.get(i);

                Integer is = isHaveIndex.get(j);
                if (ch==is){
                    isHaveIndex.remove(j);
                }
            }
        }

        choiseAuthris.removeAll(isHaveIndex);
        Logger.d(" 2"+choiseAuthris);
        Logger.d(" 3"+isHaveIndex);
//
//        Logger.d(" 1"+choiseAuthrisIndex);
//        if (choiseAuthrisIndex.containsAll(isHaveIndex)){
//            choiseAuthrisIndex.removeAll(isHaveIndex);
//        }else{
//            //如果不包含，说明用户想删除所有的
//            Logger.d("删除原有的权限"+isHaveIndex);
//            for(Integer index:isHaveIndex){
//
//            }
//        }
//        Logger.d(" 2"+choiseAuthrisIndex);






    }

    /**
     * 临时方案：post一个Principa让权限和Role关联
     * @param roleId
     */
    private void postPrincipa(int roleId) {
        if (roles!=null && roles.size()!=0){
            List<Integer> choiseAuthrovity = getChoiseAuthrovity(choiseAuthrisIndex);
            if (choiseAuthrovity!=null && choiseAuthrovity.size()!=0){
                Logger.d("---"+choiseAuthrovity);
                for(Integer index:choiseAuthrovity){
                    Principal pc = new Principal();
                    pc.setPrincipalType("ROLE");
                    pc.setPrincipalId(roles.get(index).getName());
                    rootActivity.addSubscription(ApiFactory.postPrincipal(roleId, pc), new PgSubscriber<Principal>(rootActivity) {
                        @Override
                        public void on_Next(Principal principal) {
                            then(choiseAuthrovity);
                        }

                    });
                }

            }

        }
        //如果没有选择权限就直接返回
        rootActivity.backStack();
    }

    /**
    *勾选了哪些权限
    */
    private List<Integer> getChoiseAuthrovity(List<Integer> choiseAuthrisIndex) {
        for(int i=0;i<adapter.mCBFlag.size();i++){
            if (adapter.mCBFlag.get(i)){
                choiseAuthrisIndex.add(i);
            }
        }
        return choiseAuthrisIndex;
    }

    private void then(List<Integer> integers) {
        if (index ==integers.size()-1){
            ToastHelper.get(mCx).showShort(mCx.getString(R.string.add_orgRole_success));
            rootActivity.backStack();
            return;
        }
        index++;
    }

    /**
     * 临时方案：post一个Role
     */
    private void postRole() {
        Role role = new Role();
        String name = orgiId + ":" + inputRoleName.getInput();
        role.setName(name);
        role.setTitle(inputRoleName.getInput());
        role.setId(StringUtils.randow());
        rootActivity.addSubscription(ApiFactory.postRole(role), new PgSubscriber<Role>(rootActivity) {
            @Override
            public void on_Next(Role role) {
                int roleId = role.getId();
                postPrincipa(roleId);
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        inputRoleName.setInput(TextUtils.isEmpty(roleName)?"":roleName);
    }
}
