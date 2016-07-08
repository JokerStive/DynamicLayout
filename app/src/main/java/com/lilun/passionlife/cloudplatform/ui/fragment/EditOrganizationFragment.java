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

//    public AddOrganizationFragment() {
//        if (getArguments() == null)
//            setArguments(new Bundle());
//    }

    @Bind(R.id.input_orgi_name)
    RegItemView inputOrgiName;

    @Bind(R.id.exv_add_orgi)
    ExtendItem exvAddOrgi;

    @Bind(R.id.save)
    Button save;
    @Bind(R.id.input_orgi_desc)
    RegItemView inputOrgiDesc;

    private List<OrganizationAccount> data;
    private List<Organization> alreadyAddDepartments;
    private List<Organization> newAddDepartments;
    private String orgaName;
    private Organization orgna;
    private boolean index;
    private String orgaDesc;
    int dex = 0;
    private Organization newDepartment;
    private String parentOrgId;

    @Override
    public View setView() {

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
        newAddDepartments = new ArrayList<>();
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
//        inputOrgiName.setInput(TextUtils.isEmpty(organi.getName()) ? "" : organi.getName());
//        inputOrgiDesc.setInput(TextUtils.isEmpty(organi.getDescription()) ? "" : organi.getDescription());
        orgaName = orgaName==null? organi.getName():orgaName;
        orgaDesc = orgaDesc==null? organi.getDescription():orgaDesc;
//        orgaDesc = organi.getDescription();
        parentOrgId = organi.getId();
        getOrganiDepartment(parentOrgId);

    }


    /**
     * 保存
     */
    @OnClick(R.id.save)
    void save() {
        checkOrganitionIsSame();
//        checkIsAddDepartment();

    }

    /**
    *检查是否新增了部门
    */
    private void checkIsAddDepartment() {
        if (alreadyAddDepartments != null && alreadyAddDepartments.size()!=0) {
            for (Organization depart :alreadyAddDepartments){
                if (depart.isNew()){
                    Logger.d("  当前部门所属组织id = "+parentOrgId);
                    depart.setId(parentOrgId  + Constants.special_orgi_department+"/"+depart.getName());
                    depart.setParentId(parentOrgId+Constants.special_orgi_department);
                    rootActivity.addSubscription(ApiFactory.postOrganization(depart), new PgSubscriber<Organization>(rootActivity) {
                        @Override
                        public void on_Next(Organization organizationBean) {
                            then();

                        }


                    });
                }
            }
        }
    }
    private void then() {
        if (dex==alreadyAddDepartments.size()-1){
            rootActivity.backStack();
            ToastHelper.get(mCx).showShort(mCx.getString(R.string.put_department_success));
            return;
        }
        dex++;
    }


    /**
    *检查组织数据是否有变化
    */
    private void checkOrganitionIsSame() {
        //如果组织名称和描述都没有变化，return
        if (orgaName.equals(orgna.getName()) && orgaDesc.equals(orgna.getDescription())){
            checkIsAddDepartment();
            return;
        }
        Organization organizationBean = new Organization();

        //名称有变化
        if (!orgaName.equals(orgna.getName())){
            organizationBean.setName(orgaName);
            organizationBean.setId(orgiId+Constants.special_orgi_department+orgaName);
            parentOrgId = orgiId+Constants.special_orgi_department+orgaName;

        }

        //描述与变化
        if (!orgaDesc.equals(orgna.getDescription())){
            organizationBean.setDescription(orgaDesc);
        }

        //更新
        rootActivity.addSubscription(ApiFactory.putOrganization(organizationBean), new PgSubscriber<Organization>(rootActivity) {
            @Override
            public void on_Next(Organization organizationBean) {
                ToastHelper.get(mCx).showShort(mCx.getString(R.string.put_orgi_success));
//                rootActivity.backStack();
                checkIsAddDepartment();
            }


        });

    }


    /**
     * 如果新增了自己的部门，就轮询提交服务器
     */
//    private void saveDepartment() {
//        //轮询 新增部门
//        size = departments.size();
//        for(int i=0;i<departments.size();i++){
//            rootActivity.addSubscription(ApiFactory.postOrganization(departments.get(i)), new PgSubscriber<Organization>(rootActivity) {
//                @Override
//                public void on_Next(Organization organizationBean) {
//                    then();
//
//                }
//
//
//            });
//        }
//    }
//    private void then() {
//        if (dex==size){
//            rootActivity.backStack();
//            return;
//        }
//        dex++;
//    }


    /**
     * 存 新增的组织
     */
    private void saveOrgi() {
        orgna = new Organization();
        orgna.setId(orgiId + "/" + orgaName);
        orgna.setName(orgaName);
        orgna.setParentId(orgiId);
        orgna.setDescription(orgaDesc);
        //TODO 设置icon
        rootActivity.addSubscription(ApiFactory.postOrganization(orgna), new PgSubscriber<Organization>(rootActivity) {
            @Override
            public void on_Next(Organization organizationBean) {
                ToastHelper.get(mCx).showShort(mCx.getString(R.string.add_orgi_success));
                rootActivity.backStack();
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
        if (alreadyAddDepartments!=null){
            return;
        }
        String url = parentOrgId + Constants.special_orgi_department1;
        rootActivity.addSubscription(ApiFactory.getOrgiDepartment(url), new PgSubscriber<List<Organization>>(rootActivity) {
            @Override
            public void on_Next(List<Organization> organizations) {
                alreadyAddDepartments = organizations;
                if (organizations.size() == 0) {
                    ToastHelper.get(mCx).showShort(mCx.getString(R.string.empty_depart_list));
                    return;
                }

                boolean isShowDelete=false;
                //如果parentOrgId不等于当前组织，表示是继承自当前组织的父组织，则勾选chec框
                if (organizations.get(0).getParentId().equals(orgiId+Constants.special_orgi_department)){
                    exvAddOrgi.setBtnCheck(true);
                }else if(organizations.get(0).getParentId().equals(parentOrgId+Constants.special_orgi_department)){
                    //不是继承而是新增的
                    isShowDelete=true;
                    exvAddOrgi.setBtnCheck(false);
                }
                Logger.d("department  partmentid = "+organizations.get(0).getParentId());
                Logger.d("直接 父级  id = "+parentOrgId);
                Logger.d("顶层父级  id = "+orgiId);

                exvAddOrgi.setListviewData(new ParentOrgisAdapter(alreadyAddDepartments, isShowDelete, (parentOrgisAdapter, position) -> {
                    //TODO
                    deleteDeptItem(parentOrgisAdapter,position);
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
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new AddDepartmentFragment(), mCx.getString(R.string.add_department));
        EventBus.getDefault().post(event);

    }


    /**
     * get 新增部门事件
     */
    @Subscribe
    public void addDepartment(Event.addNewDepartment event) {
        newDepartment = event.getDepartment();
        if (newAddDepartments!=null){
            newAddDepartments.add(newDepartment);
        }

    }

    /**
    *删除dept item
    */
    private void deleteDeptItem(ParentOrgisAdapter parentOrgisAdapter, int position) {
            if (alreadyAddDepartments!=null  && alreadyAddDepartments.size()!=0){
                boolean isNew = alreadyAddDepartments.get(position).isNew();
                if (isNew){
                    alreadyAddDepartments.remove(position);
                    parentOrgisAdapter.notifyDataSetChanged();
                }else{
                    //删除一个部门
                    String deptId = alreadyAddDepartments.get(position).getId();
                    rootActivity.addSubscription(ApiFactory.deleteOrganization(deptId), new PgSubscriber<Object>(rootActivity) {
                        @Override
                        public void on_Next(Object o) {
                            alreadyAddDepartments.remove(position);
                            parentOrgisAdapter.notifyDataSetChanged();
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
        outState.putString("organiName", inputOrgiName.getInput());
        outState.putString("organiDesc", inputOrgiDesc.getInput());
        outState.putSerializable("newAddDepartments", (Serializable) newAddDepartments);
        outState.putSerializable("alreadyAddDepartments", (Serializable) alreadyAddDepartments);
    }


    /**
    *hui恢复数据
 */
    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        if (savedInstanceState != null) {
            orgaName = savedInstanceState.getString("organiName");

            orgaDesc = savedInstanceState.getString("organiDesc");
            newAddDepartments = (List<Organization>) savedInstanceState.get("newAddDepartments");
            alreadyAddDepartments = (List<Organization>) savedInstanceState.get("alreadyAddDepartments");
            if (newAddDepartments!=null && alreadyAddDepartments!=null){
                Logger.d("newAddDepartments size = "+newAddDepartments.size()+"------"+"alreadyAddDepartments seize = "+alreadyAddDepartments.size());
//                List<Organization> allAddDepartments = new ArrayList<>();
                alreadyAddDepartments.addAll(newAddDepartments);
//                allAddDepartments.addAll(newAddDepartments);
                newDepartment=null;
                exvAddOrgi.setBtnCheck(false);
                exvAddOrgi.setListviewData(new ParentOrgisAdapter(alreadyAddDepartments, true, (parentOrgisAdapter, position) -> {
                    //TODO
                    deleteDeptItem(parentOrgisAdapter,position);
                }));
                Logger.d("organiName = "+orgaName +"organiDesc = "+orgaDesc +"  list size =  "+alreadyAddDepartments.size());
            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        inputOrgiName.setInput(TextUtils.isEmpty(orgaName)?"":orgaName);
        inputOrgiDesc.setInput(TextUtils.isEmpty(orgaDesc)?"":orgaDesc);
    }


}
