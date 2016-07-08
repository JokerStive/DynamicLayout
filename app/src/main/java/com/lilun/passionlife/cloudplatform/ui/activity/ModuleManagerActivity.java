package com.lilun.passionlife.cloudplatform.ui.activity;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseModuleActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.ui.fragment.ServiceListFragment;

import org.greenrobot.eventbus.EventBus;

public class ModuleManagerActivity extends BaseModuleActivity {

    @Override
    protected void create() {
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new ServiceListFragment(), mCx.getString(R.string.module_manager));
        EventBus.getDefault().post(event);

    }


}
