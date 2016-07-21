package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
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
public class AddOrganizationFragment extends BaseFunctionFragment implements ExtendItem.onClickListener {


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

    private List<Organization> departments;
    private String orgaName;
    private Organization orgna;
    private String orgaDesc;
    int dex = 0;
    private int size;
    private Organization newDepartment;
    private View view;

    @Override
    public View setView() {

        view = inflater.inflate(R.layout.fragment_add_organization, null);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        departments = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        exvAddOrgi.setOnClickListen(this);
    }


    /**
     * 更新组织，需要设置初始化数据
     */
    private void setInitData() {
        inputOrgiName.setInput(TextUtils.isEmpty(orgna.getName()) ? "" : orgna.getName());
        inputOrgiDesc.setInput(TextUtils.isEmpty(orgna.getDescription()) ? "" : orgna.getDescription());
    }


    /**
     * 保存
     */
    @OnClick(R.id.save)
    void save() {
        orgaName = inputOrgiName.getInput();
        orgaDesc = inputOrgiDesc.getInput();
        if (TextUtils.isEmpty(orgaName) || TextUtils.isEmpty(orgaDesc)) {
            ToastHelper.get(mCx).showShort(mCx.getString(R.string.not_orginame_empty));
            return;
        }
        saveOrgi();

    }


    @OnClick(R.id.iv_head)
    void choiseHead(){
        Logger.d("选择头像");
        rootActivity.choiseHeadPic(view);
    }

    @Subscribe
    public void choisHead(Event.choiseHeadPic event){
        Bitmap bitmap = event.getBitmap();
        if (bitmap!=null){
            ivHead.setImageBitmap(bitmap);
        }
    }


    /**
     * 如果新增了自己的部门，就轮询提交服务器
     *
     * @param id
     */
    private void saveDepartment(String id) {
        size = departments.size();
        for (int i = 0; i < departments.size(); i++) {
            Organization depart = departments.get(i);
            depart.setId(id + Constants.special_orgi_department + "/" + depart.getName());
            depart.setParentId(id + Constants.special_orgi_department);
            Logger.d("department id  = " + id + Constants.special_orgi_department + "/" + depart.getName());
            Logger.d("department parentid  = " + id + "/" + depart.getName() + Constants.special_orgi_department);
            rootActivity.addSubscription(ApiFactory.postOrganization(departments.get(i)), new PgSubscriber<Organization>(rootActivity) {
                @Override
                public void on_Next(Organization organizationBean) {
                    then();

                }


            });
        }
    }

    private void then() {
        if (dex == size - 1) {
            rootActivity.backStack();
            return;
        }
        dex++;
    }


    /**
     * 存 新增的组织
     */
    private void saveOrgi() {
        orgna = new Organization();
        orgna.setInherited(exvAddOrgi.isInherited());
        orgna.setId(orgiId + "/" + orgaName);
        orgna.setName(orgaName);
        orgna.setParentId(orgiId);
        Logger.d("orgi name = " + orgaName);
        Logger.d("orgi desc = " + orgaDesc);
        orgna.setDescription(orgaDesc);
        //TODO 设置icon
        rootActivity.addSubscription(ApiFactory.postOrganization(orgna), new PgSubscriber<Organization>(rootActivity) {
            @Override
            public void on_Next(Organization orga) {

                if (departments != null && departments.size() != 0) {
                    String id = orga.getId() + Constants.special_orgi_department;
                    rootActivity.setIsInherited(id, new IsInherited(orga.isInherited()));
                    Logger.d(" set isInherited id = "+id+"---"+"isInherited = "+orga.isInherited());
                    saveDepartment(orga.getId());
                } else {
                    rootActivity.backStack();
                }
            }


        });
    }


    /**
     * 显示继承自父的东西
     */
    @Override
    public void onBtnChoise(boolean enabled) {
        if (enabled) {
            getExtendsDepartment(orgiId);
        } else if (departments != null) {
            //
            Logger.d("展示自己的部门");
            exvAddOrgi.setListviewData(new ExtDeptAdapter(departments, true, (parentOrgisAdapter, position) -> {
                deleteItem(parentOrgisAdapter, position);
            }));
        }

    }

    /**
     * 从内存中删除一个dep  并刷新视图
     */
    private void deleteItem(ExtDeptAdapter parentOrgisAdapter, int position) {
        departments.remove(position);
        parentOrgisAdapter.notifyDataSetChanged();
    }


    /**
     * 获取继承自父组织的部门
     */
    private void getExtendsDepartment(String organiId) {
        rootActivity.getOrgaDepartment(organiId, organizations -> {
            if (organizations.size() == 0) {
                ToastHelper.get(mCx).showShort(mCx.getString(R.string.empty_depart_list));
            }
            exvAddOrgi.setListviewData(new ExtDeptAdapter(organizations, false, (parentOrgisAdapter, position) -> {

            }));
        });
    }

    @Override
    /**
     *新增what
     */
    public void onAddWhatClick() {
//        saveStateToArguments();
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new AddDepartmentFragment(), mCx.getString(R.string.add_department));
//        event.setBundle(savedState);
        EventBus.getDefault().post(event);

    }


    /**
     * 新增了一个部门
     */
    @Subscribe
    public void addDepartment(Event.addNewDepartment event) {
        Logger.d("   get  a new depart");
        newDepartment = event.getDepartment();
//        departments.add(department);
//        Logger.d(" lis size = " +departments.size());
//        exvAddOrgi.setListviewData(new ExtDeptAdapter(departments, mCx));
    }


//  保存fragmen状态  =====================================================================================================================

    @Override
    protected void onSaveState(Bundle outState) {
        super.onSaveState(outState);
        outState.putString("organiName", inputOrgiName.getInput());
        outState.putString("organiDesc", inputOrgiDesc.getInput());
        outState.putSerializable("listData", (Serializable) departments);
    }

    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        if (savedInstanceState != null) {
            orgaName = savedInstanceState.getString("organiName");

            orgaDesc = savedInstanceState.getString("organiDesc");
            departments = (List<Organization>) savedInstanceState.get("listData");
            if (newDepartment != null) {
                departments.add(newDepartment);
                newDepartment = null;
            }
        }

        Logger.d("organiName = " + orgaName + "organiDesc = " + orgaDesc + "  list size =  " + departments.size());

        exvAddOrgi.setListviewData(new ExtDeptAdapter(departments, true, (parentOrgisAdapter, position) -> {
            deleteItem(parentOrgisAdapter, position);
        }));

//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        inputOrgiName.setInput(TextUtils.isEmpty(orgaName) ? "" : orgaName);
        inputOrgiDesc.setInput(TextUtils.isEmpty(orgaDesc) ? "" : orgaDesc);
    }


}
