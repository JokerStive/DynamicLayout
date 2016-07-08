package com.lilun.passionlife.cloudplatform.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.custom_view.CrumbView;
import com.lilun.passionlife.cloudplatform.ui.App;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BaseModuleActivity extends BaseNetActivity {

    public TextView title;

    CrumbView crumbView;
    private ImageView back;
    FrameLayout content;

    public LayoutInflater inflater;
    public Context mCx;
    public Activity mAx;
    protected TextView edit;
    private String orgiId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_module);
        //绑定
        EventBus.getDefault().register(this);

        //控件初始化
        mCx = App.app;
        mAx = this;
        inflater = LayoutInflater.from(mCx);

        title = (TextView) findViewById(R.id.title);
        edit = (TextView) findViewById(R.id.tv_edit);
        edit.setOnClickListener(listen -> {
            //编辑框被点击的事件
//            Logger.d("点击编辑框");
            EventBus.getDefault().post(new Event.EditClickEvent());
        });

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(listener -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        });


        content = (FrameLayout) findViewById(R.id.frag_container);
        crumbView = (CrumbView) findViewById(R.id.crumb_view);


        crumbView.setActivity(this);
        create();
    }

    protected void create() {
    }




    /**
     * 设置标题
     */
    public void setTitle(String tit) {
        title.setText(tit);
    }

    /**
     * 设置编辑框是否显示
     */
    public void setIsEditShow(boolean isShow) {
        edit.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setEditText(String s){
        edit.setText(s);
    }

    public String getEditText(){
        return edit.getText().toString();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     *接受token过期事件，跳转登录界面
     */
    public void login(Event.AuthoriseEvent event) {
        TokenManager.translateLogin(mAx);
    }


    /**
     * 替换fragment
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void openFragment(Event.OpenNewFragmentEvent event) {
        Bundle bundle = event.getBundle();
        openNewFragment(event.newFragment, event.cuumb_title,bundle);
    }





    protected void openNewFragment(BaseFunctionFragment newFragment, String crumbTitle, Bundle bundle) {
        if (bundle!=null){
            newFragment.setArguments(bundle);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setBreadCrumbTitle(crumbTitle);
        ft.replace(R.id.frag_container, newFragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
        setTitle(crumbTitle);

    }


    public void backStack(){
        getSupportFragmentManager().popBackStack();
    }

    /**
     *响应back键，回退fragment还是activity
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
        EventBus.getDefault().unregister(this);
    }

}
