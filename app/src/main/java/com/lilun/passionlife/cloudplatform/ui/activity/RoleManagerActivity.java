package com.lilun.passionlife.cloudplatform.ui.activity;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseModuleActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.ui.fragment.ListRoleFragment;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

public class RoleManagerActivity extends BaseModuleActivity {


    @Override
    protected void create() {
        //角色列表
        Logger.d("角色列表");
        EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new ListRoleFragment(),mCx.getString(R.string.role_manager)));
    }
}
