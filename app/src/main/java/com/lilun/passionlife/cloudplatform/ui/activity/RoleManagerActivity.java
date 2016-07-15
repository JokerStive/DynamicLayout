package com.lilun.passionlife.cloudplatform.ui.activity;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseModuleActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.ui.fragment.ListRoleFragmentCopy;

import org.greenrobot.eventbus.EventBus;

public class RoleManagerActivity extends BaseModuleActivity {


    @Override
    protected void create() {
        //角色列表
        EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new ListRoleFragmentCopy(),mCx.getString(R.string.role_manager)));
    }
}
