package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.addStaff_roleListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Account;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;
import com.lilun.passionlife.cloudplatform.custom_view.ViewContainer;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/22.
 */
public class AddStaffFragment extends BaseFunctionFragment {


    @Bind(R.id.head)
    TextView head;

    @Bind(R.id.iv_head)
    CircleImageView ivHead;

    @Bind(R.id.input_staff_name)
    RegItemView inputStaffName;





    @Bind(R.id.belong_role)
    LinearLayout belongRole;

    @Bind(R.id.input_staff_username)
    RegItemView inputStaffUsername;

    @Bind(R.id.input_staff_password)
    RegItemView inputStaffPassword;

    @Bind(R.id.save)
    Button save;



    private List<String> allHaveDept = new ArrayList<>();


    private double userId;
    private int dex_dept;
    private int dex_role;

    private List<Organization> depts;
    private List<OrganizationAccount> orgaAccs = new ArrayList<>();
    private List<Role> allChoiseRole;
    private List<Map<OrganizationAccount, List<Role>>> cacheDeptAndRole = new ArrayList<>();
    private String staffName;
    private String staffUsername;
    private String staffPassword;
    private int choiseDeprCount;


    @Override
    public View setView() {
        rootActivity.setTitle(mCx.getString(R.string.staff_add));
        View view = inflater.inflate(R.layout.fragment_add_staff, null);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @OnClick(R.id.belong_dept)
    void add_belong_dept() {
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new AddStaffDepartFragment(), mCx.getString(R.string.staff_add_department));
        if (allHaveDept.size() != 0) {
            bundle = new Bundle();
            bundle.putSerializable("alreadHaveDept", (Serializable) allHaveDept);
            event.setBundle(bundle);
        }
        EventBus.getDefault().post(event);
    }


    @Subscribe
    public void choiseBelongDept(Event.choiseDepts event) {
        depts = event.getDepts();
        choiseDeprCount = depts.size();
        for (int i = 0; i < depts.size(); i++) {

            String deptId = depts.get(i).getId();
            String deptName = depts.get(i).getName();

            //每次选中一个部门就构造一个argaAccount
            OrganizationAccount oa = new OrganizationAccount();
            oa.setOrganizationId(deptId + Constants.special_orgi_staff);

            orgaAccs.add(oa);
            allHaveDept.add(deptName);
            getRoleList(i,oa, deptId);
        }
    }


    /**
     * 获取角色列表
     */
    private void getRoleList(int index, OrganizationAccount oa, String deptId) {
        rootActivity.getOrgRoleList(deptId, roles1 -> {

            Map<OrganizationAccount, List<Role>> orgAccAndRole = new HashMap<>();

            //map赋值
            orgAccAndRole.put(oa,roles1);

            //存进list
            cacheDeptAndRole.add(orgAccAndRole);

            Logger.d(" list size = " + cacheDeptAndRole.size());
            if (index == choiseDeprCount - 1) {
                setDeptAndRole(cacheDeptAndRole);
            }
        });


    }

    /**
     * 显示部门和职位
     */
    private void setDeptAndRole(List<Map<OrganizationAccount, List<Role>>> list) {
        belongRole.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            Map<OrganizationAccount, List<Role>> map = list.get(i);
            for (OrganizationAccount dept : map.keySet()) {
                List<Role> value = map.get(dept);
                String id = dept.getOrganizationId();
                String title = StringUtils.belongOgaName(id);
                addStaff_roleListAdapter adapter_role = new addStaff_roleListAdapter(value);

                final int finalI = i;
                belongRole.addView(new ViewContainer(title, adapter_role, () -> {
                    belongRole.removeViewAt(finalI);
                    list.remove(map);
                    allHaveDept.remove(title);
                    Logger.d("all dept = " + allHaveDept.size());
                }),i);
            }
        }

    }





    @OnClick(R.id.save)
    void save() {
        if (checkData()) {
            Logger.d("部门seize=" + depts.size());
            Logger.d("职位size = " + allChoiseRole.size());
            Account account = new Account();

            account.setName(inputStaffName.getInput());
            account.setUsername(inputStaffUsername.getInput());
            account.setPassword(inputStaffPassword.getInput());
            rootActivity.addSubscription(ApiFactory.register(account), new PgSubscriber<Account>(rootActivity) {
                @Override
                public void on_Next(Account account) {
                    //新增account成功之后
                    userId = (double) account.getId();
                    postDepartment(userId);
                    //挂载职位
//                    postRole(userId);
                    rootActivity.backStack();
                }

            });
        }

    }

    private List<OrganizationAccount> postDefOrga(List<OrganizationAccount> list) {
        OrganizationAccount oa = new OrganizationAccount();
        oa.setIsDefault(true);
        oa.setOrganizationId(orgiId+Constants.special_orgi_staff);
        list.add(oa);
        return list;
    }

    /**
     * 给account post 一个role
     */
    private void postRole(double userId) {
        for (Role role : allChoiseRole) {
            String roleName = role.getName();
            Logger.d(" 添加的 roleName = " + roleName);
            rootActivity.addSubscription(ApiFactory.postAccRole(userId, roleName), new PgSubscriber<Role>(rootActivity) {
                @Override
                public void on_Next(Role o) {
                    Logger.d("设置所属职位--"+roleName+"--成功");
//                    then_role(allChoiseRole.size());
                }
            });
        }
    }

//    private void then_role(int size) {
//        if (dex_role == size - 1) {
//            //把所属部门和旗下的role缓存起来，在员工编辑界面使用
//            CacheUtils.putCache(userId+"",cacheDeptAndRole);
//            return;
//        }
//        dex_role++;
//    }







    /**
     * 给account post一个depat
     *
     * @param id
     */
    private void
    postDepartment(double id) {
        List<OrganizationAccount>  organizationAccountList = new ArrayList<>();
        organizationAccountList =  postDefOrga(organizationAccountList);
        for (Map<OrganizationAccount, List<Role>> map : cacheDeptAndRole) {
            for(OrganizationAccount oa1:map.keySet()){
                organizationAccountList.add(oa1);
            }

        }

        rootActivity.addSubscription(ApiFactory.postAccOrganization(id, organizationAccountList), new PgSubscriber<List<OrganizationAccount>>(rootActivity) {
            @Override
            public void on_Next(List<OrganizationAccount> oas) {
                postRole(userId);
                for (int i=0;i<cacheDeptAndRole.size();i++) {
                    Map<OrganizationAccount, List<Role>> map = cacheDeptAndRole.get(i);
                    for(OrganizationAccount oa1:map.keySet()){
                        oa1.setId(oas.get(i).getId());
                        oa1.setBelong(true);
                    }

                }

            }

        });
    }



    /**
     *
     */
    private boolean checkData() {
        if (TextUtils.isEmpty(inputStaffName.getInput()) || TextUtils.isEmpty(inputStaffPassword.getInput()) || TextUtils.isEmpty(inputStaffUsername.getInput())) {
            ToastHelper.get(mCx).showShort("输入不能为空");
            return false;
        }


        //检查是否至少选择了一个部门

        if (depts == null || depts.size() == 0) {
            ToastHelper.get(mCx).showShort("请至少选择一个所属部门");
            return false;
        }


        //检查是否至少选择了一个所属职位
        allChoiseRole = new ArrayList<>();
        int childCount = belongRole.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewContainer viewContainer = (ViewContainer) belongRole.getChildAt(i);
            List<Role> choiseRoles = viewContainer.getChoiseRoles();
            if (choiseRoles != null && choiseRoles.size() != 0) {
                allChoiseRole.addAll(choiseRoles);
            }
        }

        if (allChoiseRole.size() == 0) {
            ToastHelper.get(mCx).showShort("请至少选择一个所属职位");
            return false;
        }

        return true;
    }


//    ==================================================================================================================================
//    保存数据


    @Override
    protected void onSaveState(Bundle outState) {
        super.onSaveState(outState);
        outState.putString("staffName", inputStaffName.getInput());
        outState.putString("staffUsername", inputStaffUsername.getInput());
        outState.putString("staffPassword", inputStaffPassword.getInput());
    }

    //
    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        staffName = savedInstanceState.getString("staffName");
        staffUsername = savedInstanceState.getString("staffUsername");
        staffPassword = savedInstanceState.getString("staffPassword");
    }


    @Override
    public void onResume() {
        super.onResume();
        inputStaffName.setInput(TextUtils.isEmpty(staffName) ? "" : staffName);
        inputStaffUsername.setInput(TextUtils.isEmpty(staffUsername) ? "" : staffUsername);
        inputStaffPassword.setInput(TextUtils.isEmpty(staffPassword) ? "" : staffPassword);
        if (cacheDeptAndRole != null) {
            setDeptAndRole(cacheDeptAndRole);
        }
    }
}
