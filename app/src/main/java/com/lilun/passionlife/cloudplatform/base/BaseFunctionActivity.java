package com.lilun.passionlife.cloudplatform.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.ui.App;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

public abstract class BaseFunctionActivity extends BaseNetActivity {

    public TextView title;
    private ImageView back;
    FrameLayout content;

    public LayoutInflater  inflater;
    public Context mCx;
    public Activity mAc;
    public TextView def_org;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mCx= App.app;
        mAc =this;
        inflater=LayoutInflater.from(mCx);

        title = (TextView) findViewById(R.id.title);
        def_org = (TextView) findViewById(R.id.def_orgi);
        back = (ImageView) findViewById(R.id.back);
        content= (FrameLayout) findViewById(R.id.frag_container);
        back.setOnClickListener(v -> {
            finish();
        });

        content.addView(setContent());
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        onCreate();

    }

    public void onCreate() {

    }


    public void setTitle(String tit){
       title.setText(tit);
    }

    /**
    *添加content View
    */
    public abstract View setContent();




    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        onUnsubscribe();
        EventBus.getDefault().unregister(this);
    }
}
