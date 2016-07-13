package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.ParentOrgisAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.custom_view.CircleImageView;
import com.lilun.passionlife.cloudplatform.custom_view.ExtendItem;
import com.lilun.passionlife.cloudplatform.custom_view.RegItemView;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.utils.ToastHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
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

    private List<OrganizationAccount> data;
    private List<Organization> allDept;
    private String orgaName;
    private Organization orgna;
    private String orgaDesc;
    int dex = 0;
    private Organization newDepartment;
    private String currentOrgaId;
    private boolean isRestore;
    private boolean isSaveData;

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
        //如果bundle为null,是新增组织，不是null，编辑组织
        exvAddOrgi.setOnClickListen(this);
        exvAddOrgi.setLlenable(false);
    }

    /**
     * 设置初始化显示数据
     */
    private void setInitData(Organization organi) {
        orgaName = orgaName == null ? organi.getName() : orgaName;
        orgaDesc = orgaDesc == null ? organi.getDescription() : orgaDesc;
        currentOrgaId = organi.getId();
        getOrganiDepartment(currentOrgaId);

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
        if (allDept != null && allDept.size() != 0) {
            for (Organization depart : allDept) {
                if (depart.isNew()) {
                    Logger.d("  当前部门所属组织id = " + currentOrgaId);
                    depart.setId(currentOrgaId + Constants.special_orgi_department + "/" + depart.getName());
                    depart.setParentId(currentOrgaId + Constants.special_orgi_department);
                    rootActivity.addSubscription(ApiFactory.postOrganization(depart), new PgSubscriber<Organization>(rootActivity) {
                        @Override
                        public void on_Next(Organization organizationBean) {
                            then();

                        }

                        @Override
                        public void on_Error() {
                            super.on_Error();
                            ToastHelper.get(mCx).showShort("更新部门失败");
                        }
                    });
                }
            }
//            rootActivity.backStack();
        } else {
            //如果本来就没有部门，并且没有新增
//            rootActivity.backStack();
        }
    }

    private void then() {
        if (dex == allDept.size() - 1) {
            ToastHelper.get(mCx).showShort(mCx.getString(R.string.put_department_success));
//            rootActivity.backStack();
            return;
        }
        dex++;
    }


    /**
     * 检查组织数据是否有变化
     */
    private void checkOrganitionIsSame() {
        //如果组织名称和描述都没有变化，return
        String inputName = inputOrgiName.getInput();
        String inputDesc = inputOrgiDesc.getInput();
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

        //更新
        rootActivity.addSubscription(ApiFactory.putOrganization(currentOrgaId, organizationBean), new PgSubscriber<Organization>(rootActivity) {
            @Override
            public void on_Next(Organization organizationBean) {
                ToastHelper.get(mCx).showShort(mCx.getString(R.string.put_orgi_success));
                rootActivity.backStack();
                checkIsAddDepartment();
            }


        });

    }



    @Override
    /**
     *显示继承自父的东西
     */
    public void onBtnChoise(boolean enabled) {
    }

    /**
     * 获取该组织下的部门列表
     */
    private void getOrganiDepartment(String parentOrgId) {
        //如果是恢复数据，就不用从网络获取部门列表了
        if (isRestore) {
            return;
        }
        String url = parentOrgId + Constants.special_orgi_department1;
        rootActivity.addSubscription(ApiFactory.getOrgiDepartment(url), new PgSubscriber<List<Organization>>(rootActivity) {
            @Override
            public void on_Next(List<Organization> organizations) {
                allDept = organizations;
                if (organizations.size() == 0) {
                    ToastHelper.get(mCx).showShort(mCx.getString(R.string.empty_depart_list));
                    return;
                }

                boolean isShowDelete = false;
                //如果parentOrgId不等于当前组织，表示是继承自当前组织的父组织，则勾选chec框
                if (organizations.get(0).getParentId().equals(orgiId + Constants.special_orgi_department)) {
                    exvAddOrgi.setBtnCheck(true);
                } else if (organizations.get(0).getParentId().equals(parentOrgId + Constants.special_orgi_department)) {
                    //不是继承而是新增的
                    isShowDelete = true;
                    exvAddOrgi.setBtnCheck(false);
                }
                Logger.d("department  partmentid = " + organizations.get(0).getParentId());
                Logger.d("直接 父级  id = " + parentOrgId);
                Logger.d("顶层父级  id = " + orgiId);

                exvAddOrgi.setListviewData(new ParentOrgisAdapter(allDept, isShowDelete, (parentOrgisAdapter, position) -> {
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
        if (allDept != null) {
            allDept.add(newDepartment);

        }

    }

    /**
     * 删除dept item
     */
    private void deleteDeptItem(ParentOrgisAdapter showDeptAdapter, int position) {
        if (allDept != null && allDept.size() != 0) {
            boolean isNew = allDept.get(position).isNew();
            //如果是新增的，直接从内存中删除
            if (isNew) {
                allDept.remove(position);
                showDeptAdapter.notifyDataSetChanged();
            }

            //如果点击的不是新增而是原来就有的，就要delete部门
            else {
                String deptId = allDept.get(position).getId();
                rootActivity.addSubscription(ApiFactory.deleteOrganization(deptId), new PgSubscriber<Object>(rootActivity) {
                    @Override
                    public void on_Next(Object o) {
                        allDept.remove(position);
                        showDeptAdapter.notifyDataSetChanged();
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


    //  保存fragmen状态  =====================================================================================================================
//
    @Override
    protected void onSaveState(Bundle outState) {
        super.onSaveState(outState);
        if (isSaveData){
            outState.putString("organiName", inputOrgiName.getInput());
            outState.putString("organiDesc", inputOrgiDesc.getInput());
            outState.putSerializable("allDept", (Serializable) allDept);
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
            allDept = (List<Organization>) savedInstanceState.get("allDept");
            if (allDept != null) {
                newDepartment = null;
                exvAddOrgi.setBtnCheck(false);
                exvAddOrgi.setListviewData(new ParentOrgisAdapter(allDept, true, (parentOrgisAdapter, position) -> {
                    //TODO
                    deleteDeptItem(parentOrgisAdapter, position);
                }));
                Logger.d("organiName = " + orgaName + "organiDesc = " + orgaDesc + "  list size =  " + allDept.size());
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
