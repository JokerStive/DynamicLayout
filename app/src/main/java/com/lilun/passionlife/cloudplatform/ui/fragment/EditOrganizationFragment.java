package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.ExtDeptAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.IsInherited;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.PicloadManager;
import com.lilun.passionlife.cloudplatform.custom_view.AlertDiaog;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.ExtendItem;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by youke on 2016/6/22.
 * 新增组织
 */
public class EditOrganizationFragment extends BaseFunctionFragment implements ExtendItem.onClickListener {


    @Bind(R.id.head)
    TextView head;

    @Bind(R.id.iv_head)
    CircleImageView ivHead;

    @Bind(R.id.input_role_name)
    RegItemView inputOrgiName;

    @Bind(R.id.exv_add_orgi)
    ExtendItem exvAddOrgi;

    @Bind(R.id.save)
    Button save;
    @Bind(R.id.input_orgi_desc)
    RegItemView inputOrgiDesc;

    private List<Organization> parentDepts;
    private List<Organization> ownDepts=new ArrayList<>();
    private String orgaName;
    private Organization orgna;
    private String orgaDesc;
    int dex = 0;
    private Organization newDepartment;
    private String currentOrgaId;
    private boolean isRestore;
    private boolean isSaveData;
    private boolean currentCheck;
//    private String currIsInheritedid;
    private Boolean isInherited;
    private Boolean ISINHERITED;
    private String currIsInheritedid;

    @Override
    public View setView() {
        rootActivity.setTitle(mCx.getString(R.string.edit_orgi));
        View view = inflater.inflate(R.layout.fragment_add_organization, null);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onStart() {
        super.onStart();
//        newAddDepartments = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            orgna = (Organization) bundle.get("organiChildren");
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
        Picasso.with(App.app).load(PicloadManager.orgaIconUrl(organi.getId())).into(ivHead);
        orgaName = orgaName == null ? organi.getName() : orgaName;
        orgaDesc = orgaDesc == null ? organi.getDescription() : orgaDesc;
        currentOrgaId = organi.getId();
        currIsInheritedid = currentOrgaId+Constants.special_orgi_department;

        if (isRestore){return;}
        rootActivity.getIsInherited(currentOrgaId, isInherite -> {
            ISINHERITED = isInherite;
            isInherited = isInherite;
            exvAddOrgi.setBtnCheck(isInherited);
            getOrganiDepartment(currentOrgaId);
        });

    }


    /**
     * 保存
     */
    @OnClick(R.id.save)
    void save() {
        checkOrganitionIsSame();

    }

    /**
     * 检查是否新增了部门
     */
    private void checkIsAddDepartment() {
        if (exvAddOrgi.isInherited()){
            if (ownDepts!=null){
                for(int i=0;i<ownDepts.size();i++){
                    if (!ownDepts.get(i).isNew()){
                        //删除本来的部门
                        String deptId = ownDepts.get(i).getId();
                        rootActivity.addSubscription(ApiFactory.deleteOrganization(deptId), new PgSubscriber<Object>(rootActivity) {
                            @Override
                            public void on_Next(Object o) {
                           ;
                            }

                            @Override
                            public void on_Error() {
                                super.on_Error();
                                ToastHelper.get(mCx).showShort(mCx.getString(R.string.delete_dept_false));
                            }
                        });
                    }
                }
            }
            return;
        }
        if (ownDepts != null && ownDepts.size() != 0) {
            List<Organization>  depts = new ArrayList<>();
            for (Organization depart : ownDepts) {
                if (depart.isNew()) {
                    Logger.d("  当前部门所属组织id = " + currentOrgaId);
                    depart.setId(currentOrgaId + Constants.special_orgi_department + "/" + depart.getName());
                    depart.setParentId(currentOrgaId + Constants.special_orgi_department);
                    depts.add(depart);
                }
            }
            if (depts.size()==0){return;}
            rootActivity.addSubscription(ApiFactory.postOrganizations(depts), new PgSubscriber<List<Organization>>(rootActivity) {
                @Override
                public void on_Next(List<Organization> organizationBean) {
                    ToastHelper.get().showShort(App.app.getString(R.string.put_department_success));
                }
            });
        }
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
        if (inputName.equals(orgna.getName()) && inputDesc.equals(orgna.getDescription())) {
            checkIsAddDepartment();
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


//                Logger.d(" set isInherited id = "+currIsInheritedid+"---"+"isInherited = "+orga.isInherited());
                checkIsAddDepartment();
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
            if (parentDepts!=null){
                exvAddOrgi.setListviewData(new ExtDeptAdapter(parentDepts, false, (parentOrgisAdapter, position) -> {
                }));
            }else{
                getOrganiDepartment(orgiId);
            }
        } else if (ownDepts !=null){
            //
            Logger.d("展示自己的部门");
            exvAddOrgi.setListviewData(new ExtDeptAdapter(ownDepts, true, (parentOrgisAdapter, position) -> {
                deleteDeptItem(parentOrgisAdapter, position);
            }));
        }

    }

    /**
     * 获取该组织下的部门列表
     */
    private void getOrganiDepartment(String parentOrgId) {
        //如果是恢复数据，就不用从网络获取部门列表了
        String url  = parentOrgId+Constants.special_orgi_department;
        rootActivity.addSubscription(ApiFactory.getOrgiDepartment(url), new PgSubscriber<List<Organization>>(rootActivity) {
            @Override
            public void on_Next(List<Organization> organizations) {
                if (organizations.size() == 0) {
                    String s = isInherited ?mCx.getString(R.string.empty_parent_dept):mCx.getString(R.string.empty_child_dept);
                    ToastHelper.get(mCx).showShort(s);
                    return;
                }
                if (isInherited){
                    parentDepts = organizations;
                }else{
                    ownDepts = organizations;
                }
                exvAddOrgi.setListviewData(new ExtDeptAdapter(organizations, !isInherited, (parentOrgisAdapter, position) -> {
                    //TODO
                    deleteDeptItem(parentOrgisAdapter, position);
                }));
            }


        });
    }


    /**
     * 新增what
     */
    @Override
    public void onAddWhatClick() {
        Logger.d("接收到 新增部门 click");
        isSaveData=true;
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new AddDepartmentFragment(), mCx.getString(R.string.add_department));
        EventBus.getDefault().post(event);

    }


    /**
     * get 新增部门事件
     */
    @Subscribe
    public void addDepartment(Event.addNewDepartment event) {
        newDepartment = event.getDepartment();
        if (ownDepts != null) {
            ownDepts.add(newDepartment);

        }

    }

    /**
     * 删除dept item
     */
    private void deleteDeptItem(ExtDeptAdapter showDeptAdapter, int position) {
        if (ownDepts != null && ownDepts.size() != 0) {
            boolean isNew = ownDepts.get(position).isNew();
            //如果是新增的，直接从内存中删除
            if (isNew) {
                ownDepts.remove(position);
                showDeptAdapter.notifyDataSetChanged();
            }

            //如果点击的不是新增而是原来就有的，就要delete部门
            else {

                String deptId = ownDepts.get(position).getId();
                new AlertDiaog(rootActivity, "确定移除该部门？", () -> {
                    rootActivity.addSubscription(ApiFactory.deleteOrganization(deptId), new PgSubscriber<Object>(rootActivity) {
                        @Override
                        public void on_Next(Object o) {
                            ownDepts.remove(position);
                            showDeptAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void on_Error() {
                            super.on_Error();
                            ToastHelper.get(mCx).showShort(mCx.getString(R.string.delete_dept_false));
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
//            outState.putSerializable("ownDepts", (Serializable) ownDepts);
        }
    }


    /**
     * hui恢复数据
     */
    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        if (savedInstanceState != null) {

            //恢复数据的表示
            isRestore=true;

            //恢复显示的名称和描述
            orgaName = savedInstanceState.getString("organiName");
            orgaDesc = savedInstanceState.getString("organiDesc");


//            newAddDepartments = (List<Organization>) savedInstanceState.get("newAddDepartments");
            //恢复部门列表
//            ownDepts = (List<Organization>) savedInstanceState.get("ownDepts");
            if (ownDepts != null) {
//                newDepartment = null;
                exvAddOrgi.setBtnCheck(false);
                exvAddOrgi.setListviewData(new ExtDeptAdapter(ownDepts, true, (parentOrgisAdapter, position) -> {
                    //TODO
                    deleteDeptItem(parentOrgisAdapter, position);
                }));
                Logger.d("organiName = " + orgaName + "organiDesc = " + orgaDesc + "  list size =  " + ownDepts.size());
            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        inputOrgiName.setInput(TextUtils.isEmpty(orgaName) ? "" : orgaName);
        inputOrgiDesc.setInput(TextUtils.isEmpty(orgaDesc) ? "" : orgaDesc);
    }


}
