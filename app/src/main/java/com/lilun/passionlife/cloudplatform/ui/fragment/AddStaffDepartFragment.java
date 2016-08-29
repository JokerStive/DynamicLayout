package com.lilun.passionlife.cloudplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.adapter.addStaff_deptListAdapter;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.custom_view.FlowLayout;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.CacheUtils;
import com.lilun.passionlife.cloudplatform.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Iterator;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/22.
 */
public class AddStaffDepartFragment extends BaseFunctionFragment {


    @Bind(R.id.fl_dept)
    FlowLayout belongDept;

    private addStaff_deptListAdapter adapter_dept;
    private List<String> alreadHaveDept;


    @Override
    public View setView() {
        View view = inflater.inflate(R.layout.fragment_add_authority, null);
        Bundle bundle = getArguments();
        if (bundle != null) {
            alreadHaveDept = (List<String>) bundle.get("alreadHaveDept");

        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        belongDept.setHorizontalSpacing(UIUtils.dip2px(App.app, 10));
        belongDept.setVerticalSpacing(UIUtils.dip2px(App.app, 10));
        getDeptList();

    }

    /**
     * 获取组织下的部门列表
     */
    public void getDeptList() {
        List<Organization> depts = (List<Organization>) CacheUtils.getCache(Constants.cacheKey_department);
        if (depts != null) {
            EventBus.getDefault().post(new getDepts_ok(depts));
            return;
        }

        rootActivity.getOrgaDepartment(orgiId, depts1 -> {
            EventBus.getDefault().post(new getDepts_ok(depts1));
        });

    }

    @Subscribe
    public void getDept_oK(getDepts_ok event) {
        List<Organization> depts = event.getDepts();
        if (alreadHaveDept != null) {
            for (Iterator<Organization> itA = depts.iterator(); itA.hasNext(); ) {
                Organization orga = itA.next();
                for (int i = 0; i < alreadHaveDept.size(); i++) {
                    if (orga.getName().equals(alreadHaveDept.get(i))) {
                        itA.remove();
                    }
                }
            }
        }


        adapter_dept = new addStaff_deptListAdapter(depts);
        belongDept.setAdapter(adapter_dept);
    }

    @Override
    protected void save() {
        List<Organization> choiseDepts = adapter_dept.getChoiseDepts();
//        Logger.d("chois dept size=="+choiseDepts.size());
        if (adapter_dept.getChoiseDepts().size() != 0) {
            EventBus.getDefault().post(new Event.choiseDepts(choiseDepts));
            rootActivity.backStack();
        }
    }


    class getDepts_ok {
        private List<Organization> depts;

        public getDepts_ok(List<Organization> depts) {
            this.depts = depts;
        }

        public List<Organization> getDepts() {
            return depts;
        }


    }


}
