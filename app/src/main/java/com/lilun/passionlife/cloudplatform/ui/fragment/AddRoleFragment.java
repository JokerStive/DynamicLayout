package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.AuthrovityListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Principal;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/22.
 */
public class AddRoleFragment extends BaseFunctionFragment{


    @Bind(R.id.head)
    TextView head;
    @Bind(R.id.iv_head)
    CircleImageView ivHead;
    @Bind(R.id.input_role_name)
    RegItemView inputOrgiName;
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

    @Override
    public View setView() {
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
        getAuthrovityList();
    }

    /**
     * 获取权限列表
     */
    public void getAuthrovityList() {
        rootActivity.getAuthrovityList(orgiId, authrovites -> {
            adapter = new AuthrovityListAdapter(authrovites, false, (authrovityListAdapter, position) -> {

            });
            roles = authrovites;
            lvAuthList.setAdapter(adapter);
        });
    }



    /**
     *
     */
    @OnClick(R.id.save)
    void save() {
        //TODO 这只是一个临时方案，post一个Role...,以后直接关联OrganizationRole
        if (StringUtils.checkEmpty(inputOrgiName.getInput())) {
            postRole();
        }
    }

    /**
     * 临时方案：post一个Principa让权限和Role关联
     * @param roleId
     */
    private void postPrincipa(int roleId) {
        if (roles!=null && roles.size()!=0){
            List<Integer> choiseAuthrovity = getChoiseAuthrovity(choiseAuthrisIndex);
            if (choiseAuthrovity!=null && choiseAuthrovity.size()!=0){
                for(Integer index:choiseAuthrovity){
                    Principal pc = new Principal();
                    pc.setId(StringUtils.randow());
                    pc.setPrincipalType("ROLE");
                    pc.setPrincipalId(roles.get(index).getName());
                    rootActivity.addSubscription(ApiFactory.postPrincipal(roleId, pc), new PgSubscriber<Principal>(rootActivity) {
                        @Override
                        public void on_Next(Principal principal) {
                            then(choiseAuthrovity);
                        }

                    });
                }

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
        String name = orgiId + ":" + inputOrgiName.getInput();
        role.setName(name);
        role.setTitle(inputOrgiName.getInput());
        role.setId(StringUtils.randow());
        rootActivity.addSubscription(ApiFactory.postRole(role), new PgSubscriber<Role>(rootActivity) {
            @Override
            public void on_Next(Role role) {
                int roleId = role.getId();
                postPrincipa(roleId);
            }
        });
    }
}
