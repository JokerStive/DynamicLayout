package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.graphics.Bitmap;
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
import com.lilun.passionlife.cloudplatform.common.PicloadManager;
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
import okhttp3.RequestBody;
import rx.Observable;

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
    private List<OrganizationAccount> accountDepts = new ArrayList<>();
    private List<Role> allChoiseRole;
    private List<Map<OrganizationAccount, List<Role>>> cacheDeptAndRole = new ArrayList<>();
    private String staffName;
    private String staffUsername;
    private String staffPassword;
    private int choiseDeprCount;
    private View view;
    private Bitmap icon;


    @Override
    public View setView() {
        rootActivity.setTitle(mCx.getString(R.string.staff_add));
        view = inflater.inflate(R.layout.fragment_add_staff, null);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }



    @OnClick(R.id.iv_head)
    void headPic() {
        Logger.d("选择头像");
        rootActivity.choiseHeadPic(view);
    }

    @Subscribe
    public void choisHead(Event.choiseHeadPic event) {
        Bitmap bitmap = event.getBitmap();
        if (bitmap != null) {
            icon = bitmap;
            ivHead.setImageBitmap(bitmap);

        }
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

            accountDepts.add(oa);
            allHaveDept.add(deptName);
            getRoleList(i, oa, deptId);
        }
    }


    /**
     * 获取角色列表
     */
    private void getRoleList(int index, OrganizationAccount oa, String deptId) {
        rootActivity.getOrgRoleList(deptId, roles1 -> {

            Map<OrganizationAccount, List<Role>> orgAccAndRole = new HashMap<>();

            //map赋值
            orgAccAndRole.put(oa, roles1);

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
                }), i);
            }
        }

    }


    @OnClick(R.id.save)
    void save() {
        if (checkData()) {
            Logger.d("size = "+cacheDeptAndRole.size());
            Observable observable = ApiFactory.register(newAccount()).concatMap(account -> postDefOrga(account));
            rootActivity.addSubscription(observable, new PgSubscriber<Object>(rootActivity) {
                @Override
                public void on_Next(Object o) {
                    postDeptAndRole();
                    saveIcon();
                    EventBus.getDefault().post(new Event.reflashStaffList());
                    rootActivity.backStack();
                }
            });

        }


    }

    private void postDeptAndRole() {
        Observable observable = null;
        if (checkHasDept()) {
            if(checkHasRole()){
                observable =Observable.concat(postDept(),postRole());
            }
        }


        if (observable==null){
            return;
        }

        rootActivity.addSubscription(observable, new PgSubscriber() {
            @Override
            public void on_Next(Object o) {
                Logger.d("员工添加成功");
            }
        });
    }

    private void saveIcon() {
        if (icon!=null){
            RequestBody requestBody = PicloadManager.getUploadIconRequestBody(icon);
            rootActivity.addSubscription(ApiFactory.postAccountIcon(userId, requestBody), new PgSubscriber() {
                @Override
                public void on_Next(Object o) {
                    Logger.d("员工图标上传成功");
                }
            });
        }
    }


    /**
    *新增role
    */
    private Observable<Object> postRole() {
        List<Observable<Object>> observables = new ArrayList<>();
        for (Role role : allChoiseRole) {
            String roleName = role.getName();
            observables.add(ApiFactory.postAccRole(userId, roleName));
        }

        return Observable.merge(observables);
    }

    /**
     * 新增depts
     */
    private Observable<List<OrganizationAccount>> postDept() {

//        accountDepts.add(newDefOrga());

        return ApiFactory.postAccOrganization(userId, accountDepts);
    }

    /**
    *新增默认组织
    */
    private Observable<List<OrganizationAccount>> postDefOrga(Account account) {
        userId = (double) account.getId();
        List<OrganizationAccount> list = new ArrayList<>();
        list.add(newDefOrga());
        return ApiFactory.postAccOrganization(userId, list);

    }

   /**
   *
   */
    private OrganizationAccount newDefOrga() {
        OrganizationAccount oa = new OrganizationAccount();
        oa.setIsDefault(true);
        oa.setOrganizationId(StringUtils.getCheckedOrgaId(orgiId) + Constants.special_orgi_staff);
        return oa;
    }

    private Account newAccount() {
        Account account = new Account();
        account.setName(inputStaffName.getInput());
        account.setUsername(inputStaffUsername.getInput());
        account.setPassword(inputStaffPassword.getInput());
        return account;
    }





    /**
     *
     */
    private boolean checkData() {
        if (TextUtils.isEmpty(inputStaffName.getInput()) || TextUtils.isEmpty(inputStaffPassword.getInput()) || TextUtils.isEmpty(inputStaffUsername.getInput())) {
            ToastHelper.get(mCx).showShort("输入不能为空");
            return false;
        }
        return true;
    }


    /**
     * 检查是否选择了一个部门
     */
    private boolean checkHasRole() {
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
            ToastHelper.get().showShort("请至少选择一个职位");
            return false;
        }
        return true;
    }

    /**
     * 检查是否选择了一个职位
     */
    private boolean checkHasDept() {
        if (depts == null || depts.size() == 0) {
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
        ivHead.setImageBitmap(icon);
    }
}
