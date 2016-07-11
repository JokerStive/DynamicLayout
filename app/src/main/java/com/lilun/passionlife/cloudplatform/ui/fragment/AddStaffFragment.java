package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.AuthrovityListAdapter;
import com.lilun.passionlife.cloudplatform.adapter.addStaff_deptListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Account;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.PullChoiseView;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;
import com.lilun.passionlife.cloudplatform.custom_view.XListView;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.utils.CacheUtils;
import com.lilun.passionlife.cloudplatform.utils.FilterUtils;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
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
public class AddStaffFragment extends BaseFunctionFragment implements AuthrovityListAdapter.OnItemClickListen {


    @Bind(R.id.head)
    TextView head;

    @Bind(R.id.iv_head)
    CircleImageView ivHead;

    @Bind(R.id.input_staff_name)
    RegItemView inputStaffName;

    @Bind(R.id.belong_dept)
    PullChoiseView belongDept;

    @Bind(R.id.lv_role)
    XListView lvRole;

    @Bind(R.id.input_staff_username)
    RegItemView inputStaffUsername;

    @Bind(R.id.input_staff_password)
    RegItemView inputStaffPassword;

    @Bind(R.id.save)
    Button save;

    private List<Role> roles;
    private AuthrovityListAdapter adapter_role;
    private List<Organization> departments;

    private List<Integer> roleIndex = new ArrayList<>();
    private List<Integer> deptIndex = new ArrayList<>();


    private int userId;
    private addStaff_deptListAdapter adapter_dept;
    private int dex_dept;

    @Override
    public View setView() {
        View view = inflater.inflate(R.layout.fragment_add_staff, null);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        getRoleList();
        getDepartmentList();
    }

    /**
     * 获取部门列表
     */
    private void getDepartmentList() {
        rootActivity.getOrgaDepartment(SpUtils.getString(Constants.key_defOrginaId), depts -> {
            departments = depts;
            if (depts.size() == 0) {
                ToastHelper.get(mCx).showShort("部门列表为空");
                return;
            }
            //当所属部门被选择的时候记下来
            adapter_dept = new addStaff_deptListAdapter(depts, false, new addStaff_deptListAdapter.OnItemClickListen() {
                @Override
                public void onItemDelete(addStaff_deptListAdapter authrovityListAdapter, int position) {

                }
            });

            belongDept.init(position -> {
                Logger.d("--"+depts.get(position).getName());
                belongDept.setShow_data(depts.get(position).getName());
            }, adapter_dept);

        });
    }

    /**
     * 获取角色列表
     */
    private void getRoleList() {
        List<Role> cache = (List<Role>) CacheUtils.getCache(Constants.cacheKey_role);
        if (cache != null && cache.size() != 0) {
            adapter_role = new AuthrovityListAdapter(cache, false, this);
            lvRole.setAdapter(adapter_role);
            return;
        }
        String filter = FilterUtils.roleFilter(orgiId);
        rootActivity.addSubscription(ApiFactory.getRoleListFilter(filter), new PgSubscriber<List<Role>>(rootActivity) {
            @Override
            public void on_Next(List<Role> roless) {
                roles = roless;
                CacheUtils.putCache(Constants.cacheKey_role, roles);
                Logger.d(roless.size() + "");
                adapter_role = new AuthrovityListAdapter(roles, false, AddStaffFragment.this);
                lvRole.setAdapter(adapter_role);
//                UIUtils.setListViewHeightBasedOnChildren(lvRole);
            }
        });
    }


    @Override
    public void onItemDelete(AuthrovityListAdapter authrovityListAdapter, int position) {

    }




    @OnClick(R.id.save)
    void save() {
        if (checkData()) {
            Account account = new Account();
            account.setId(StringUtils.randow());
            account.setName(inputStaffName.getInput());
            account.setUsername(inputStaffUsername.getInput());
            account.setPassword(inputStaffPassword.getInput());
            rootActivity.addSubscription(ApiFactory.register(account), new PgSubscriber<Account>(rootActivity) {
                @Override
                public void on_Next(Account account) {
                    //新增account成功之后
                    userId = account.getId();
                    postDepartment(account.getId());
                    postRole();

                }

            });
        }

    }

    /**
     * 给account post 一个role
     */
    private void postRole() {

    }

    /**
     * 给account post一个depat
     *
     * @param id
     */
    private void postDepartment(int id) {
        for (Integer index : deptIndex) {
            OrganizationAccount oa = new OrganizationAccount();
            oa.setId(StringUtils.randow());
            oa.setOrganizationId(departments.get(index).getId() + "/#staff");
            Logger.d(" orig =" + departments.get(index).getId());
            rootActivity.addSubscription(ApiFactory.postAccOrganization(id, oa), new PgSubscriber<OrganizationAccount>(rootActivity) {
                @Override
                public void on_Next(OrganizationAccount organizationAccount) {
                    then_dept(deptIndex.size());
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
        if (adapter_dept != null && adapter_dept.mCBFlag.size() != 0) {
            deptIndex.clear();
            for (int i = 0; i < adapter_dept.mCBFlag.size(); i++) {
                if (adapter_dept.mCBFlag.get(i)) {
                    deptIndex.add(i);
                }
            }
            if (deptIndex.size()==0){
                ToastHelper.get(mCx).showShort("请至少选择一个所属部门");
                return false;
            }
        }

        //检查是否至少选择了一个所属职位
        if (adapter_role != null && adapter_role.mCBFlag.size() != 0) {
            roleIndex.clear();
            for (int i = 0; i < adapter_role.mCBFlag.size(); i++) {
                if (adapter_role.mCBFlag.get(i)) {
                    roleIndex.add(i);
                }
            }
            if (roleIndex.size()==0){
                ToastHelper.get(mCx).showShort("请至少选择一个所属职位");
                return false;
            }
        }
        return true;
    }


}
