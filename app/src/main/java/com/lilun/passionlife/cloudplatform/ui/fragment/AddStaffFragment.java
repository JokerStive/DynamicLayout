package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.AuthrovityListAdapter;
import com.lilun.passionlife.cloudplatform.adapter.addStaff_deptListAdapter;
import com.lilun.passionlife.cloudplatform.adapter.addStaff_roleListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Account;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;
import com.lilun.passionlife.cloudplatform.custom_view.ViewContainer;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
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
public class AddStaffFragment extends BaseFunctionFragment implements AuthrovityListAdapter.OnItemClickListen {


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

    private List<String> allHaveDept = new ArrayList<>();


    private int userId;
    private addStaff_deptListAdapter adapter_dept;
    private int dex_dept;

    private List<Organization> depts;
    private List<Role> allChoiseRole;
    private List<Map<String,List<Role>>> deptAndRoleList = new ArrayList<>();
    private String staffName;
    private String staffUsername;
    private String staffPassword;
    private int choiseDeprCount;


    @Override
    public View setView() {
        View view = inflater.inflate(R.layout.fragment_add_staff, null);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @OnClick(R.id.belong_dept)
    void add_belong_dept(){
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new AddStaffDepartFragment(), mCx.getString(R.string.staff_add_department));
        if (allHaveDept.size()!=0){
            bundle = new Bundle();
            bundle.putSerializable("alreadHaveDept", (Serializable) allHaveDept);
            event.setBundle(bundle);
        }
        EventBus.getDefault().post(event);
    }


    @Subscribe
    public void choiseBelongDept(Event.choiseDepts event){
        depts = event.getDepts();
        choiseDeprCount = depts.size();
        for(int i = 0; i< depts.size(); i++){
            String deptId = depts.get(i).getId();
            String deptName = depts.get(i).getName();
            allHaveDept.add(deptName);
            getRoleList(i,deptName,deptId);
        }
    }


    /**
     * 获取角色列表
     */
    private void getRoleList(int index,String deptName,String deptId) {
        rootActivity.getOrgRoleList(deptId, roles1 -> {
            Map<String,List<Role>> deptAndRole = new HashMap<>();
            deptAndRole.put(deptName,roles1);
            deptAndRoleList.add(deptAndRole);
            Logger.d(" list size = "+deptAndRoleList.size());
            if (index==choiseDeprCount-1){
                setDeptAndRole(deptAndRoleList);
            }
        });


    }

    /**
    *显示部门和职位
    */
    private void setDeptAndRole(List<Map<String,List<Role>>> list) {
        belongRole.removeAllViews();
        for(int i=0;i<list.size();i++){
            Map<String, List<Role>> map = list.get(i);
            for (String title:map.keySet()){
                List<Role> value = map.get(title);
                addStaff_roleListAdapter  adapter_role = new addStaff_roleListAdapter(value);
                belongRole.addView(new ViewContainer(title, adapter_role, () -> {
                    list.remove(map);
                    allHaveDept.remove(title);
                    Logger.d("all dept = "+allHaveDept.size());
                }));
            }
        }

    }


    @Override
    public void onChoiseItemCancle(AuthrovityListAdapter authrovityListAdapter, int position) {

    }




    @OnClick(R.id.save)
    void save() {
        if (checkData()) {
            Logger.d("部门seize="+depts.size());
            Logger.d("职位size = "+allChoiseRole.size());
            Account account = new Account();

            account.setName(inputStaffName.getInput());
            account.setUsername(inputStaffUsername.getInput());
            account.setPassword(inputStaffPassword.getInput());
            rootActivity.addSubscription(ApiFactory.register(account), new PgSubscriber<Account>(rootActivity) {
                @Override
                public void on_Next(Account account) {
                    //新增account成功之后
                    userId = (int) account.getId();
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
            Logger.d(" 添加的 roleName = "+roleName);
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

            if (depts==null || depts.size()==0){
                ToastHelper.get(mCx).showShort("请至少选择一个所属部门");
                return false;
            }


        //检查是否至少选择了一个所属职位
        allChoiseRole = new ArrayList<>();
        int childCount = belongRole.getChildCount();
        for (int i=0;i<childCount;i++){
            ViewContainer viewContainer = (ViewContainer) belongRole.getChildAt(i);
            List<Role> choiseRoles = viewContainer.getChoiseRoles();
            if (choiseRoles!=null && choiseRoles.size()!=0){
                allChoiseRole.addAll(choiseRoles);
            }
        }

        if (allChoiseRole.size()==0){
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
        inputStaffName.setInput(TextUtils.isEmpty(staffName)?"":staffName);
        inputStaffUsername.setInput(TextUtils.isEmpty(staffUsername)?"":staffUsername);
        inputStaffPassword.setInput(TextUtils.isEmpty(staffPassword)?"":staffPassword);
        if(deptAndRoleList!=null){
            setDeptAndRole(deptAndRoleList);
        }
    }
}
