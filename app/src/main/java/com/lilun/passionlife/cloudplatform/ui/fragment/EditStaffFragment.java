package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.addStaff_deptListAdapter;
import com.lilun.passionlife.cloudplatform.adapter.addStaff_roleListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Account;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.custom_view.AlertDiaog;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;
import com.lilun.passionlife.cloudplatform.custom_view.ViewContainer;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.utils.CacheUtils;
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
 * Created by youke on 2016/6/22.
 */
public class EditStaffFragment extends BaseFunctionFragment {


    @Bind(R.id.head)
    TextView head;

    @Bind(R.id.iv_head)
    CircleImageView ivHead;

    @Bind(R.id.input_staff_name)
    RegItemView inputStaffName;


//    @Bind(R.id.lv_role)
//    ItemContainer lvRole;


    @Bind(R.id.belong_role)
    LinearLayout belongRole;

    @Bind(R.id.input_staff_username)
    RegItemView inputStaffUsername;

    @Bind(R.id.input_staff_password)
    RegItemView inputStaffPassword;

    @Bind(R.id.save)
    Button save;

    private List<Role> roles;
    private addStaff_roleListAdapter adapter_role;
    private List<Organization> departments;

    private List<String> allBelongDeptName = new ArrayList<>();
    private List<OrganizationAccount> belongDept = new ArrayList<>();
    private List<Role> belongRoles = new ArrayList<>();


    private double userId;
    private addStaff_deptListAdapter adapter_dept;
    private int dex_dept;

    private List<Organization> depts;
    private List<Role> allChoiseRole;
    private List<Map<OrganizationAccount, List<Role>>> deptAndRoleList = new ArrayList<>();
    private String staffName;
    private String staffUsername;
    private String staffPassword;
    private int choiseDeprCount;
    private OrganizationAccount orgAccount;
    private List<Role> data;
    private boolean isRestore;
    private String tilt;


    @Override
    public View setView() {
        rootActivity.setTitle(mCx.getString(R.string.staff_edit));
        View view = inflater.inflate(R.layout.fragment_add_staff, null);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        tilt = mCx.getString(R.string.delete_belong_dept);
        Bundle bundle = getArguments();
        if (bundle != null) {
            orgAccount = (OrganizationAccount) bundle.get(Constants.orgaAccount);
            if (orgAccount != null) {
                setInitData(orgAccount);
//                getBelongdept((double) orgAccount.getAccount().getId());
                if (isRestore) {
                    return;
                }
                getBelongRole((double) orgAccount.getAccount().getId());
            }
        }
    }

    /**
     * 获取该account所属的role
     */
    private void getBelongRole(double id) {
        rootActivity.addSubscription(ApiFactory.getAccountRole(id), new PgSubscriber<List<Role>>(rootActivity) {
            @Override
            public void on_Next(List<Role> roles) {
                Logger.d("获取职位成功");
                setData(roles);
            }
        });
    }


    /**
     * 设置初始化显示数据
     */
    private void setInitData(OrganizationAccount orgAccount) {
        userId = (double) orgAccount.getAccount().getId();
        staffName = staffName == null ? orgAccount.getAccount().getName() : staffName;
        staffUsername = staffUsername == null ? orgAccount.getAccount().getUsername() : staffUsername;
        inputStaffPassword.setVisibility(View.GONE);
    }

    /**
     * 设置role显示数据
     */
    public void setData(List<Role> roles) {
        deptAndRoleList = (List<Map<OrganizationAccount, List<Role>>>) CacheUtils.getCache("deptAndRole");
        if (deptAndRoleList != null) {
            for (int i = 0; i < deptAndRoleList.size(); i++) {
                Map<OrganizationAccount, List<Role>> map = deptAndRoleList.get(i);
                for (OrganizationAccount deptAcc : map.keySet()) {
                    List<Role> value = map.get(deptAcc);
                    filtBelongRole(value, roles);
                    setDeptAndRole(deptAndRoleList);
                }
            }
        }
    }

    /**
     * 删除所属的部门
     */
    private void deleteBelongdept(int position,Role role) {
        if (userId == 0) {
            return;
        }
        double id = (double) role.getId();
        rootActivity.addSubscription(ApiFactory.deleteAccRole(userId, id), new PgSubscriber(rootActivity) {
            @Override
            public void on_Next(Object o) {
                if (adapter_role!=null && position!=0){
                    adapter_role.setEnable(position,false);
                }
            }
        });

    }

    /**
     * 删除所属的role
     *
     * @param dept
     */
    private void deleteBelongDept(OrganizationAccount dept) {
        if (userId != 0 && dept!=null) {
            double deptId = (double) dept.getId();
            rootActivity.addSubscription(ApiFactory.deleteAccOrga(userId, deptId), new PgSubscriber<Object>(rootActivity) {
                @Override
                public void on_Next(Object o) {

                }
            });

        }
    }

    /**
     * 属于某个职位，显示为绿色
     */
    public void filtBelongRole(List<Role> value, List<Role> roles) {
        for (int i = 0; i < value.size(); i++) {
            for (int j = 0; j < roles.size(); j++) {
                double id = (double) value.get(i).getId();
                double id1 = (double) roles.get(j).getId();
                if (id == id1) {
                    value.get(i).setBelong(true);
                }
            }
        }
    }


    @OnClick(R.id.belong_dept)
    void add_belong_dept() {
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new AddStaffDepartFragment(), mCx.getString(R.string.staff_add_department));
        if (allBelongDeptName.size() != 0) {
            bundle = new Bundle();
            bundle.putSerializable("alreadHaveDept", (Serializable) allBelongDeptName);
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
            allBelongDeptName.add(deptName);
            getRoleList(i, depts.get(i), deptId);
        }
    }


    /**
     * 获取角色列表
     */
    private void getRoleList(int index, Organization dept, String deptId) {
        rootActivity.getOrgRoleList(deptId, roles1 -> {
            //根据dept构造一个orgsAcc
            OrganizationAccount oa = new OrganizationAccount();
            oa.setOrganizationId(dept.getId() + "/#staff");

            Map<OrganizationAccount, List<Role>> deptAndRole = new HashMap<>();
            deptAndRole.put(oa, roles1);

            deptAndRoleList.add(deptAndRole);
            Logger.d(" list size = " + deptAndRoleList.size());
            if (index == choiseDeprCount - 1) {
                setDeptAndRole(deptAndRoleList);
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
            for (OrganizationAccount deptAcc : map.keySet()) {

                List<Role> value = map.get(deptAcc);
                String id = deptAcc.getOrganizationId();
                String title = StringUtils.belongOgaName(id);

                allBelongDeptName.add(title);

                adapter_role = new addStaff_roleListAdapter(value);
                adapter_role.setOnItemCancleListener(position -> {
                    new AlertDiaog(rootActivity, "删除", () -> {
                        deleteBelongdept(position,value.get(position));
                    });
                });
                belongRole.addView(new ViewContainer(title, adapter_role, () -> {
                    if (deptAcc.isBelong()) {
                        new AlertDiaog(rootActivity, tilt, () -> {
                            //删除本来所属的部门和role
                            deleteBelongDept(deptAcc);
                            for (Role role : value) {
                                deleteBelongdept(0,role);
                            }

                        });
                    }
                    list.remove(map);
                    allBelongDeptName.remove(title);
                    Logger.d("all dept = " + allBelongDeptName.size());
                }));
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
                    postRole(userId);
                    rootActivity.backStack();
                }

            });
        }

    }

    /**
     * 给account post 一个role
     */
    private void postRole(double userId) {
        for (Role role : allChoiseRole) {
            String roleName = role.getName();
            Logger.d(" 添加的 roleName = " + roleName);
            rootActivity.addSubscription(ApiFactory.postAccRole(userId, roleName), new PgSubscriber<Object>(rootActivity) {
                @Override
                public void on_Next(Object o) {

                }
            });
        }
    }

    /**
     * 给account post一个depat
     *
     * @param id
     */
    private void postDepartment(double id) {
        for (Organization dept : depts) {
            OrganizationAccount oa = new OrganizationAccount();
            oa.setOrganizationId(dept.getId() + "/#staff");
            rootActivity.addSubscription(ApiFactory.postAccOrganization(id, oa), new PgSubscriber<OrganizationAccount>(rootActivity) {
                @Override
                public void on_Next(OrganizationAccount organizationAccount) {
//                    then_dept(deptIndex.size());
                }

            });
        }
    }

    private void then_dept(int size) {
        if (dex_dept == size - 1) {
//            postRole();
            rootActivity.backStack();
            ToastHelper.get(mCx).showShort("员工添加成功");
            return;
        }
        dex_dept++;
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

        if (deptAndRoleList.size()==0) {
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


        //恢复数据的表示
        isRestore = true;


        staffName = savedInstanceState.getString("staffName");
        staffUsername = savedInstanceState.getString("staffUsername");
        staffPassword = savedInstanceState.getString("staffPassword");
    }


    @Override
    public void onResume() {
        super.onResume();
        inputStaffName.setInput(TextUtils.isEmpty(staffName) ? "" : staffName);
        inputStaffUsername.setInput(TextUtils.isEmpty(staffUsername) ? "" : staffUsername);
        if (deptAndRoleList != null) {
            setDeptAndRole(deptAndRoleList);
        }
    }


}
