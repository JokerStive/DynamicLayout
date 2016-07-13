package com.lilun.passionlife.cloudplatform.ui.fragment;

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
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;
import com.lilun.passionlife.cloudplatform.custom_view.ViewContainer;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.utils.CacheUtils;
import com.lilun.passionlife.cloudplatform.utils.FilterUtils;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    private List<Integer> roleIndex = new ArrayList<>();
    private List<Integer> deptIndex = new ArrayList<>();


    private int userId;
    private addStaff_deptListAdapter adapter_dept;
    private int dex_dept;

    private String choiseDeptId;

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
        EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new AddStaffDepartFragment(),mCx.getString(R.string.staff_add_department)));
    }


    @Subscribe
    public void choiseBelongDept(Event.choiseDepts event){
        List<Organization> depts = event.getDepts();
        for(int i=0;i<depts.size();i++){
            String deptId = depts.get(i).getId();
            String deptName = depts.get(i).getName();
            getRoleList(i,deptName,deptId);
        }
    }


    /**
     * 获取角色列表
     * @param position
     */
    private void getRoleList(int position,String deptName,String deptId) {

        String filter = FilterUtils.roleFilter(orgiId);
        rootActivity.addSubscription(ApiFactory.getRoleListFilter(filter), new PgSubscriber<List<Role>>(rootActivity) {
            @Override
            public void on_Next(List<Role> roless) {
                roles = roless;
                CacheUtils.putCache(Constants.cacheKey_role, roles);
//                Logger.d(roless.size() + "");
//                adapter_role = new AuthrovityListAdapter(roles, false, AddStaffFragment.this);
                adapter_role = new addStaff_roleListAdapter(roles);


                belongRole.addView(new ViewContainer(mCx,"研发部",adapter_role),position);
//                belongRole.addView(new ViewContainer(mCx,"研发部",adapter_role),position);
//
            }
        });
    }


    private void removeRole(int position){
        belongRole.removeViewAt(position);
    }


    @Override
    public void onChoiseItemCancle(AuthrovityListAdapter authrovityListAdapter, int position) {

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
        int childCount = belongRole.getChildCount();
        for (int i=0;i<childCount;i++){

        }

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
