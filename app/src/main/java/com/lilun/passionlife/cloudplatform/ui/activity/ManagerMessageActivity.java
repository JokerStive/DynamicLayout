package com.lilun.passionlife.cloudplatform.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.FrameLayout;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseNetAppcomActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.custom_view.CrumbView;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.ui.fragment.ListMessageFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManagerMessageActivity extends BaseNetAppcomActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.crumb_view)
    CrumbView crumbView;
    @Bind(R.id.frag_container)
    FrameLayout fragContainer;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_message);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        assert toolbar != null;
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.message_delete:
                    //删除
                    break;
                case R.id.message_main:
                    //删除
                    break;
                case R.id.message_about:
                    //删除
                    break;
            }
            return true;
        });


        crumbView.setActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new ListMessageFragment(), App.app.getString(R.string.message_info)));
    }

    @OnClick(R.id.fab)
    void addNewMessage(){
        //TODO 新增一个消息
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_toolbar_menu, menu);
        return true;
    }


    /**
     * 替换fragment
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void openFragment(Event.OpenNewFragmentEvent event) {
        Bundle bundle = event.getBundle();
        openNewFragment(event.newFragment, event.cuumb_title, bundle);
    }


    protected void openNewFragment(Fragment newFragment, String crumbTitle, Bundle bundle) {
        if (bundle != null) {
            newFragment.setArguments(bundle);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setBreadCrumbTitle(crumbTitle);
        ft.replace(R.id.frag_container, newFragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
        setTitle(crumbTitle);
    }


    /**
     * 响应back键，回退fragment还是activity
     */
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑
        EventBus.getDefault().unregister(this);
    }

}
