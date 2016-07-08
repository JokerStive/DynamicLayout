package com.lilun.passionlife.cloudplatform.ui.activity;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseModuleActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.ui.fragment.StaffListFragment;

import org.greenrobot.eventbus.EventBus;

public class StaffManagerActivity extends BaseModuleActivity {


    @Override
    protected void create() {
        EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new StaffListFragment(),mCx.getString(R.string.staff_manager)));
    }
}
