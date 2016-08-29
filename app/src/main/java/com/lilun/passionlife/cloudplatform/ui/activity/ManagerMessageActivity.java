package com.lilun.passionlife.cloudplatform.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.FrameLayout;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseNetAppcomActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.common.KnowPermission;
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.custom_view.CrumbView;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.ui.fragment.ListMessageFragment;
import com.lilun.passionlife.cloudplatform.utils.IntentUtils;
import com.lilun.passionlife.cloudplatform.utils.SnackbarHelper;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;

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

    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private double userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_message);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        userId = (double) SpUtils.getInt(TokenManager.USERID);

        assert toolbar != null;
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
//        toolbar.set
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.message_delete:
//                    SnackbarHelper.makeShort(coordinatorLayout,App.app.getString(R.string.no_permission), Color.RED);
                    deleteMessage();
                    break;
                case R.id.message_main:
                    //我的信息
                    break;
                case R.id.message_about:
                    //关于
                    break;
            }
            return true;
        });


        crumbView.setActivity(this);
        EventBus.getDefault().post(new Event.OpenNewFragmentEvent(new ListMessageFragment(), App.app.getString(R.string.message_info)));

    }

    /**
     * 删除信息
     */
    private void deleteMessage() {
        if (isAdmin()) {
            EventBus.getDefault().post(new Event.setMessageDeleteShow());
        } else {
            addSubscription(ApiFactory.hasPermission(userId, KnownServices.Information_Service + KnowPermission.deletePermission), new PgSubscriber<Boolean>() {
                @Override
                public void on_Next(Boolean hasPermission) {
                    if (hasPermission) {
                        EventBus.getDefault().post(new Event.setMessageDeleteShow());
                    }else{
                        SnackbarHelper.makeShort(coordinatorLayout,App.app.getString(R.string.no_permission), Color.RED);
                    }
                }
            });
        }
    }


    @OnClick(R.id.fab)
    void addNewMessage() {
        addNewInfo();
    }


    public CoordinatorLayout getCoordinatorLayout(){
        return coordinatorLayout;
    }

    /**
     * 新增信息
     */
    private void addNewInfo() {
        if (isAdmin()) {
            IntentUtils.startAct(this, MessageIssueActivity.class);
        } else {

            addSubscription(ApiFactory.hasPermission(userId, KnownServices.Information_Service + KnowPermission.addPermission), new PgSubscriber<Boolean>() {
                @Override
                public void on_Next(Boolean hasPermission) {
                    if (hasPermission) {
                        IntentUtils.startAct(ManagerMessageActivity.this, MessageIssueActivity.class);
                    }else {
                        SnackbarHelper.makeShort(coordinatorLayout,App.app.getString(R.string.no_permission), Color.RED);
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage_message_toolbar_menu, menu);
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
