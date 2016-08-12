package com.lilun.passionlife.cloudplatform.ui.activity;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseModuleActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.ui.fragment.ListOrgaFragment;

import org.greenrobot.eventbus.EventBus;

public class ManagerOrganizationActivity extends BaseModuleActivity {

    @Override
    protected void create() {
//        EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new ListOrgaFragment(),mCx.getString(R.string.organization)));
        EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new ListOrgaFragment(),mCx.getString(R.string.organization)));

    }


}
