package com.lilun.passionlife.cloudplatform.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseNetActivity;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.constans.BelongOass;
import com.lilun.passionlife.cloudplatform.custom_view.CrumbView;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.ui.fragment.ChangeOrganizationFragment;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.UIUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ChangeOrganizationActivity extends BaseNetActivity {

    private FrameLayout content;
    private CrumbView crumbView;
    private Bundle bundle;
    private int marginTop;
    private TextView save;
    private TextView current_orga;
    private String change_orga_id;
    private String change_orga_name;
    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_organization);

        //绑定
        EventBus.getDefault().register(this);


        rl = (RelativeLayout) findViewById(R.id.rl);
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) UIUtils.getScreenHeight());
//        rl.setLayoutParams(lp);
        crumbView = (CrumbView) findViewById(R.id.crumb_view);
        content = (FrameLayout) findViewById(R.id.frag_container);
        current_orga = (TextView) findViewById(R.id.current_orga);
        save = (TextView) findViewById(R.id.save);
        save.setOnClickListener(v -> {
            Logger.d(change_orga_id +"---"+change_orga_name);
            String currentOrgaId = SpUtils.getString(Constants.key_currentOrgaId);
            //切换的组织跟当前操作的组织不一致才刷新视图
            if (!currentOrgaId.equals(change_orga_id)){
                EventBus.getDefault().post(new Event.ChangeChildOrganization(change_orga_id,change_orga_name));
            }
            finish();
        });
        crumbView.setActivity(this);




        Intent intent = getIntent();
//        String id = intent.getStringExtra("id");
//        String name = intent.getStringExtra("name");
        marginTop = intent.getIntExtra("height", UIUtils.dip2px(App.app, 50));

        String currentId = SpUtils.getString(Constants.key_currentOrgaId);
        String currentName = SpUtils.getString(Constants.key_currentOrgaName);

//        setCurrentOrga(currentName, currentId);


        //
        String belongId = SpUtils.getString(BelongOass.oas_id);
        String belongName = SpUtils.getString(BelongOass.oas_name);
        if (!TextUtils.isEmpty(belongId) && !TextUtils.isEmpty(belongName)){
            openNewFragment(belongName, belongId);
            current_orga.setText(belongName);
        }

    }

    public void setCurrentOrga(String title, String id) {
        change_orga_id = id;
        change_orga_name = title;
        current_orga.setText(title);
    }

    public void setSaveShow(boolean show) {
        save.setVisibility(show ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        View view = this.getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.y = marginTop;
        lp.gravity = Gravity.TOP;
        getWindowManager().updateViewLayout(view, lp);

    }


    private void openNewFragment(String name, String id) {
        bundle = new Bundle();
        bundle.putSerializable("id", id);
        bundle.putSerializable("name", name);
        Event.OpenNewFragmentEvent event = new Event.OpenNewFragmentEvent(new ChangeOrganizationFragment(), name);
        event.setBundle(bundle);
        EventBus.getDefault().post(event);
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
