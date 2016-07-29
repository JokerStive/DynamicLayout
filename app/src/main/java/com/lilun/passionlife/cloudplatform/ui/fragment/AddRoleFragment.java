package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.view.View;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/22.
 */
public class AddRoleFragment extends BaseFunctionFragment{



    @Bind(R.id.iv_head)
    CircleImageView ivHead;

    @Bind(R.id.input_role_name)
    RegItemView inputOrgiName;

    @Bind(R.id.input_role_desc)
    RegItemView inputOrgaDesc;


    @Bind(R.id.lv_auth_list)
    ListView lvAuthList;

/*
* 1.post一个Role
* 2.轮询post选中的权限
*
* */

    private List<Integer> choiseAuthrisIndex = new ArrayList<>();
    private String crumb_title;
    private List<Role> roles;
    private int index;
    private AuthrovityListAdapter adapter;
    private View view;

    @Override
    public View setView() {
        view = inflater.inflate(R.layout.fragment_add_role, null);
        return view;
    }




    @Override
    public void onStart() {
        super.onStart();
        getAuthrovityList();
    }





    /**
     * 获取权限列表
     */
    public void getAuthrovityList() {
        rootActivity.getAuthrovityList(orgiId, authrovites -> {
            adapter = new AuthrovityListAdapter(authrovites, false);
            roles = authrovites;
            lvAuthList.setAdapter(adapter);
        });
    }



    /**
     *
     */
    @OnClick(R.id.save)
    void save() {
        if (StringUtils.checkEmpty(inputOrgiName.getInput(),inputOrgaDesc.getInput()) ) {
            postRole();
        }
    }




    private void postPrincipa(Double roleId) {
        if (roles!=null && roles.size()!=0){
            List<Integer> choiseAuthrovity = getChoiseAuthrovity(choiseAuthrisIndex);
            List<Principal>  principals = new ArrayList<>();
            if (choiseAuthrovity!=null && choiseAuthrovity.size()!=0){
                for(Integer index:choiseAuthrovity){
                    Principal pc = new Principal();
                    pc.setPrincipalType("ROLE");
                    pc.setPrincipalId(roles.get(index).getName());
                    principals.add(pc);
                }
                rootActivity.addSubscription(ApiFactory.postPrincipal(roleId, principals), new PgSubscriber<Object>(rootActivity) {
                    @Override
                    public void on_Next(Object principal) {
                        rootActivity.backStack();
                    }

                });

            }else{
                rootActivity.backStack();
            }

        }else{
            //如果没有选择权限就直接返回
            rootActivity.backStack();
        }
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



    /**
     * 临时方案：post一个Role
     */
    private void postRole() {
        Role role = new Role();
        String name = orgiId + ":" + inputOrgiName.getInput();
        role.setName(name);
        role.setDescription(inputOrgaDesc.getInput());
        role.setTitle(inputOrgiName.getInput());
        role.setOrganizationId(orgiId+ Constants.special_orgi_role);
        rootActivity.addSubscription(ApiFactory.postRole(role), new PgSubscriber<Role>(rootActivity) {
            @Override
            public void on_Next(Role role) {
                Double roleId = (Double) role.getId();
                postPrincipa(roleId);
            }
        });
    }
}
