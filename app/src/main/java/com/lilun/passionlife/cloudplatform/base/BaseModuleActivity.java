package com.lilun.passionlife.cloudplatform.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.custom_view.CrumbView;
import com.lilun.passionlife.cloudplatform.ui.App;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BaseModuleActivity extends BaseNetAppcomActivity {

//    public TextView title;

    //    CrumbView crumbView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.crumb_view)
    CrumbView crumbView;

    @Bind(R.id.fr_crumb)
    FrameLayout frCrumb;

    @Bind(R.id.frag_container)
    FrameLayout fragContainer;

    @Bind(R.id.fab_add)
    FloatingActionButton fabAdd;


//    private ImageView back;
//    FrameLayout content;

    public LayoutInflater inflater;
    public Context mCx;
    public Activity mAx;
    protected TextView edit;
    private String orgiId;
//    private FloatingActionButton fba_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_module);
        //绑定
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        //控件初始化
        mCx = App.app;
        mAx = this;
        inflater = LayoutInflater.from(mCx);

        findView();
        create();
    }


//    @OnClick(R.id.)

    private void findView() {
        assert toolbar != null;
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        });
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id==R.id.module_edit){
//                Logger.d("module edit");
                EventBus.getDefault().post(new Event.EditClickEvent());
            }
            return true;
        });


        assert fabAdd != null;
        fabAdd.setOnClickListener(v -> {
            EventBus.getDefault().post(new Event.AddX());
        });

        assert crumbView != null;
        crumbView.setActivity(this);
    }

    protected void create() {

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            menu.findItem(R.id.module_edit).setVisible(true);
        }else{
            menu.findItem(R.id.module_edit).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_module_toolbar_menu, menu);
        return true;
    }

    /**
     * 设置fba是否显示
     */
    public void setFbaShow(boolean showFba) {
        fabAdd.setVisibility(showFba ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置标题
     */
    public void setTitle(String tit) {
        toolbar.setTitle(tit);
    }

    /**
     * 设置编辑框是否显示
     */
    public void setIsEditShow(boolean isShow) {
//        edit.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }


    public void setEditText(String s) {
        edit.setText(s);
    }

    public String getEditText() {
        return edit.getText().toString();
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


    public void backStack() {
        getSupportFragmentManager().popBackStack();
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
        onUnsubscribe();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }


}
