package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.AuthrovityListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Principal;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by youke on 2016/6/22.
 */
public class AddDeptRoleFragment extends BaseFunctionFragment {




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
    private List<Principal> principals = new ArrayList<>();
    private String crumb_title;
    private List<Role> roles;
    private int index;
    private AuthrovityListAdapter adapter;

    @Override
    public View setView() {
        rootActivity.setTitle(App.app.getString(R.string.add_duty));
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
        rootActivity.getAuthrovityList(authrovites -> {
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
        //TODO 这只是一个临时方案，post一个Role...,以后直接关联OrganizationRole
        if (StringUtils.checkEmpty(inputOrgiName.getInput())) {
            postRole();
        }
    }

    private List<Principal> getPrincipals() {
        if (roles!=null && roles.size()!=0){
            List<Integer> choiseAuthrovity = getChoiseAuthrovity(choiseAuthrisIndex);
            if (choiseAuthrovity!=null && choiseAuthrovity.size()!=0){
                for(Integer index:choiseAuthrovity){
                    Principal pc = new Principal();
//                    pc.setId(StringUtils.randow());
                    pc.setPrincipalType("ROLE");
                    pc.setPrincipalId(roles.get(index).getName());
                    principals.add(pc);
                }

                return principals;
            }

        }
        return principals;
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
        role.setNew(true);
        Logger.d("待添加的role title==="+role.getTitle());

        principals =  getPrincipals();

        EventBus.getDefault().post(new Event.addNewRole(principals,role));
        rootActivity.backStack();

    }



}
