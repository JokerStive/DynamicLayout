package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.ExtRoleAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.IsInherited;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.bean.Principal;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.KnowPermission;
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.custom_view.AlertDiaog;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.ExtendItem;
import com.lilun.passionlife.cloudplatform.custom_view.InputView;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by youke on 2016/6/22.
 * 编辑部门
 */
public class EditDeptFragment extends BaseFunctionFragment implements ExtendItem.onClickListener {


    @Bind(R.id.head)
    TextView head;

    @Bind(R.id.iv_head)
    CircleImageView ivHead;

    @Bind(R.id.input_orgi_name)
    InputView inputOrgiName;

    @Bind(R.id.exv_add_orgi)
    ExtendItem exvAddOrgi;


    @Bind(R.id.input_orgi_desc)
    InputView inputOrgiDesc;

    private String roleEditPermission = KnownServices.Role_Service + KnowPermission.editPermission;
    private List<OrganizationAccount> data;
    private List<Role> allOwnRole ;
    private List<Role> parentRole;
    private String orgaName;
    private Organization orgna;
    private String orgaDesc;
    int dex = 0;
    private Role newRole;
    private String currentOrgaId;
    private boolean isRestore;
    private boolean isSaveData;
    private Boolean isInherited;
    private Boolean ISINHERITED;
    private List<Map<Role, List<Principal>>> rolePrinMapping=new ArrayList<>();
    private String currIsInheritedid;

    @Override
    public View setView() {
        rootActivity.setTitle(mCx.getString(R.string.edit_dept));
        View view = inflater.inflate(R.layout.fragment_add_dept, null);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bundle = new Bundle();

    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.d("edit dept--onStart");
        Logger.d("isretore =  "+isRestore);
        Bundle bundle = getArguments();
        if (bundle != null) {
            orgna = (Organization) bundle.get(Constants.orgaDept);
            if (orgna != null) {
                setInitData(orgna);
            }
        }
        exvAddOrgi.setOnClickListen(this);
    }

    /**
     * 设置初始化显示数据
     */
    private void setInitData(Organization organi) {
        orgaName = orgaName == null ? organi.getName() : orgaName;
        orgaDesc = orgaDesc == null ? organi.getDescription() : orgaDesc;
        currentOrgaId = organi.getId();

        if(isRestore){return;}
        currIsInheritedid = currentOrgaId+Constants.special_orgi_role;
        rootActivity.getIsInherited(currIsInheritedid, isInherite -> {
            ISINHERITED = isInherite;
            isInherited = isInherite;
            exvAddOrgi.setBtnCheck(isInherited);
            getOrganiRole(currentOrgaId);
            Logger.d("当前部门id = "+currentOrgaId);
        });

    }


    /**
     * 保存
     */
    @Override
    protected void save() {
        checkOrganitionIsSame();

    }

    /**
     * 检查是否新增了部门
     */
    private void checkIsAddRole() {
        if (exvAddOrgi.isInherited()){
            if (allOwnRole!=null){
                for(int i=0;i<allOwnRole.size();i++){
                    Role role = allOwnRole.get(i);
                    if (!role.isNew()){
                        //删除本来的部门
                        String roleId = role.getId();
                        rootActivity.addSubscription(ApiFactory.deleteRole(roleId), new PgSubscriber<Object>(rootActivity) {
                            @Override
                            public void on_Next(Object o) {
                                Logger.d("删除"+role.getTitle()+"成功");
                            }

                            @Override
                            public void on_Error() {
                                super.on_Error();
                                Logger.d("删除"+role.getTitle()+"失败");
//                                ToastHelper.get(mCx).showShort(mCx.getString(R.string.delete_dept_role_false));
                            }
                        });
                    }
                }
            }
            return;
        }

        if (allOwnRole != null && allOwnRole.size() != 0) {
            List<Role> roleList = new ArrayList<>();
            for (int i=0;i<allOwnRole.size();i++) {
                Role role = allOwnRole.get(i);
                if (role.isNew()) {

                    Logger.d("  当前部门所属组织id = " + currentOrgaId);
                    role.setId(null);
                    role.setOrganizationId(currentOrgaId+Constants.special_orgi_role);
                    role.setName(currentOrgaId+":"+role.getName());
                    roleList.add(role);

                }
            }
            if (roleList.size()==0){return;}
            rootActivity.addSubscription(ApiFactory.postRoles(roleList), new PgSubscriber<List<Role>>(rootActivity) {
                @Override
                public void on_Next(List<Role> Roles) {
                    Logger.d("新增职位成功");
                    for(int i=0;i<Roles.size();i++){
                        Role role = Roles.get(i);
                        Map<Role, List<Principal>> roleListMap = rolePrinMapping.get(i);
                        List<Principal> principals = roleListMap.get(roleList.get(i));
                        postPrincipal(role.getId(), principals);
                    }
                }

                @Override
                public void on_Error() {
                    super.on_Error();

                }
            });
        }
    }



    /**
    *改role新增prin
    */
    private void postPrincipal(String roleId, List<Principal> lp) {
        for(Principal p:lp){
            if (p.getId()!=null){
                p.setId(null);
            }
        }
        rootActivity.addSubscription(ApiFactory.postPrincipal(roleId, lp), new PgSubscriber<Object>(rootActivity) {
            @Override
            public void on_Next(Object o) {
            }
        });

    }


    /**
     * 检查组织数据是否有变化
     */
    private void checkOrganitionIsSame() {
        //如果组织名称和描述都没有变化，return
        String inputName = inputOrgiName.getInput();
        String inputDesc = inputOrgiDesc.getInput();
        boolean b = ISINHERITED == exvAddOrgi.isInherited();
        if (!b){
            rootActivity.setIsInherited(currIsInheritedid, new IsInherited(isInherited));
        }

        if (inputName.equals(orgna.getName()) && inputDesc.equals(orgna.getDescription()) && b) {
            checkIsAddRole();
            rootActivity.backStack();
            return;
        }
        Organization organizationBean = new Organization();

        //名称有变化
        if (!inputName.equals(orgna.getName())) {
            organizationBean.setName(inputName);

        }

        //描述与变化
        if (!inputDesc.equals(orgna.getDescription())) {
            organizationBean.setDescription(inputDesc);
        }

        organizationBean.setInherited(exvAddOrgi.isInherited());

        //更新
        rootActivity.addSubscription(ApiFactory.putOrganization(currentOrgaId, organizationBean), new PgSubscriber<Organization>(rootActivity) {
            @Override
            public void on_Next(Organization orga) {
                checkIsAddRole();
                rootActivity.backStack();
            }


        });

    }



    @Override
    /**
     *显示继承自父的东西
     */
    public void onBtnChoise(boolean enabled) {
        isInherited = enabled;
        if (enabled) {
            if (parentRole!=null){
                setExData(true,parentRole);
//                exvAddOrgi.setListviewData(new ExtRoleAdapter(parentRole));
            }else{
                getOrganiRole(StringUtils.getCheckedOrgaId(orgiId));
            }
        }else{
            if (allOwnRole==null){
                allOwnRole = new ArrayList<>();
                allOwnRole = parentRole;
                for(Role role:allOwnRole){
                    Map<Role, List<Principal>> map = new HashMap<>();
                    map.put(role,role.getPrincipals());
                    rolePrinMapping.add(map);
                    role.setNew(true);
                }
            }
           setExData(false,allOwnRole);
        }


    }

    /**
     * 获取该部门下的role列表
     */
    private void getOrganiRole(String parentOrgId) {

        rootActivity.getOrgRolesWithFilter(parentOrgId, roles -> {
            if (roles.size() == 0) {
                String s = isInherited ?mCx.getString(R.string.empty_parent_role):mCx.getString(R.string.empty_child_role);
                ToastHelper.get(mCx).showShort(s);
//                return;
            }
            if (isInherited){
                parentRole = new ArrayList<>();
                parentRole = roles;
            }else{
                allOwnRole = new ArrayList<>();
                allOwnRole = roles;
            }

            setExData(isInherited,roles);

        });
    }

    /**
    *设置继承框显示数据
    */
    private void setExData(Boolean isInherited, List<Role> roles) {
        if (isInherited){
            exvAddOrgi.setListviewData(new ExtRoleAdapter(roles));
        }else{
            exvAddOrgi.setListviewData(new ExtRoleAdapter(roles, new ExtRoleAdapter.OnItemDeleteListen() {
                @Override
                public void onItemDelete(ExtRoleAdapter parentOrgisAdapter, int position) {
                    deleteDeptItem(parentOrgisAdapter,position);
                }

                @Override
                public void onItemEdit(ExtRoleAdapter parentOrgisAdapter, int position) {
                    if (isAdmin()){
                        editRole(position);
                        return;
                    }
//                    if (userId=null){return;}
                    rootActivity.checkHasPermission(userId,roleEditPermission,hasPermission -> {
                        if (hasPermission){
                            editRole(position);
                        }else{
                            ToastHelper.get().showShort(App.app.getString(R.string.no_permission));
                        }
                    });
                }
            }));
        }
    }


   /**
   *编辑职位
   */
    private void editRole(int position) {
        bundle.putSerializable(Constants.role, allOwnRole.get(position));
//        bundle.putSerializable("needGetPrinsFromNet", true);
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new EditRoleFragment(), mCx.getString(R.string.role_edit));
        event.setBundle(bundle);
        EventBus.getDefault().post(event);
    }


    /**
     * 新增what
     */
    @Override
    public void onAddWhatClick() {
        Logger.d("接收到 新增部门 click");
        isSaveData=true;
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new AddDeptRoleFragment(), mCx.getString(R.string.dept_add_role));
        EventBus.getDefault().post(event);

    }


    /**
     * get 新增role事件
     */
    @Subscribe
    public void addDepartment(Event.addNewRole event) {
        newRole = event.getRole();
        List<Principal> principals = event.getPrincipals();
        if (allOwnRole != null) {
            allOwnRole.add(newRole);
        }

        if (principals!=null){
            Map<Role,List<Principal>> mapping = new HashMap<>();
            mapping.put(newRole,principals);
            rolePrinMapping.add(mapping);
        }

    }

    /**
     * 删除role item
     */
    private void deleteDeptItem(ExtRoleAdapter showDeptAdapter, int position) {
        if (allOwnRole != null && allOwnRole.size() != 0) {
            boolean isNew = allOwnRole.get(position).isNew();
            //如果是新增的，直接从内存中删除
            if (isNew) {
                Role role = allOwnRole.get(position);
                Logger.d("parentRole  1- "+parentRole);
                allOwnRole.remove(position);
                for(Map<Role,List<Principal>> mapping:rolePrinMapping){
                    for(Role role1:mapping.keySet()){
                        if (role.getId()==role1.getId()){
                            rolePrinMapping.remove(mapping);
                        }
                    }
                }
                showDeptAdapter.notifyDataSetChanged();
                Logger.d("parentRole-- "+parentRole);
            }

            //如果点击的不是新增而是原来就有的，就要delete职位
            else {
                new AlertDiaog(rootActivity, "确定移除该职位？", () -> {
                    String roleId = allOwnRole.get(position).getId();
                    rootActivity.addSubscription(ApiFactory.deleteRole(roleId), new PgSubscriber<Object>(rootActivity) {
                        @Override
                        public void on_Next(Object o) {
                            allOwnRole.remove(position);
                            showDeptAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void on_Error() {
                            super.on_Error();
                            ToastHelper.get(mCx).showShort(mCx.getString(R.string.delete_dept_role_false));
                        }
                    });
                });

            }
        }
    }


    //  保存fragmen状态  =====================================================================================================================
//
    @Override
    protected void onSaveState(Bundle outState) {
        super.onSaveState(outState);
        if (isSaveData){
            outState.putString("organiName", inputOrgiName.getInput());
            outState.putString("organiDesc", inputOrgiDesc.getInput());
//            outState.putSerializable("allOwnRole", (Serializable) allOwnRole);
        }
    }


    /**
     * hui恢复数据
     */
    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        Logger.d("edit orga--onRestoreState");
        if (savedInstanceState != null) {

            //恢复数据的表示
            isRestore=true;

            //恢复显示的名称和描述
            orgaName = savedInstanceState.getString("organiName");
            orgaDesc = savedInstanceState.getString("organiDesc");
            exvAddOrgi.setBtnCheck(false);
            setExData(false,allOwnRole);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        inputOrgiName.setInput(TextUtils.isEmpty(orgaName) ? "" : orgaName);
        inputOrgiDesc.setInput(TextUtils.isEmpty(orgaDesc) ? "" : orgaDesc);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.d("edit dept--onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("edit dept--onDestroy");
    }

}
