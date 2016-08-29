package com.lilun.passionlife.cloudplatform.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.StatedFragment;
import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/22.
 */
public abstract class BaseFunctionFragment  extends StatedFragment {

    protected Context mCx;
    protected BaseModuleActivity rootActivity ;
    protected FragmentManager fragManager;
    protected LayoutInflater inflater;
    private View view;
    protected  Bundle bundle;
    protected String orgiId;
    public double userId;
    protected boolean hasCheckAddPermission;
    protected boolean hasCheckEditPermission;
    protected boolean hasCheckDeletePermission;
    private boolean isVisibleToUser=true;
    private Button save;

    protected void setHasAddCheckPermission() {
        this.hasCheckAddPermission = true;
    }
    protected void setHasEditCheckPermission() {
        this.hasCheckEditPermission = true;
    }
    protected void setHasDeleteCheckPermission() {
        this.hasCheckDeletePermission = true;
    }


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        fragManager = getActivity().getSupportFragmentManager();
        rootActivity= (BaseModuleActivity) getActivity();
        mCx= App.app;
        orgiId= SpUtils.getString(Constants.key_currentOrgaId);
        userId = (double)SpUtils.getInt(TokenManager.USERID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        this.inflater=inflater;
        View baseView = inflater.inflate(R.layout.fragment_base_function, null);
        FrameLayout frameLayout = (FrameLayout) baseView.findViewById(R.id.fm_container);
        save = (Button) baseView.findViewById(R.id.save);
        save.setOnClickListener(v -> {
            save();
        });
        view = setView();
        frameLayout.addView(view);
        ButterKnife.bind(this, view);
        return baseView;
    }

    public abstract View setView();

    @Subscribe
    public void no(Event.OnSaveClick event){}


    protected void setIsSaveShow(boolean isSaveShow){
        save.setVisibility(isSaveShow?View.INVISIBLE:View.GONE);
    }


    /**
    *保存按钮被点击的回调
    */
    protected void save() {

    }


    public boolean isAdmin(){
        return SpUtils.getBoolean(Constants.ADMIN);
    }
    /**
    *
     *  设置编辑框
     */
    public void setEdText(BaseModuleListAdapter adapter){
        String editText = rootActivity.getEditText();
//        Logger.d(" 编辑框  =  "+editText);
        if (editText.equals("编辑")){
            rootActivity.setEditText("完成");
            adapter.setDeleteShow(true);
        }else{
            rootActivity.setEditText("编辑");
            adapter.setDeleteShow(false);
        }

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Logger.d("fragment destory");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }
}
