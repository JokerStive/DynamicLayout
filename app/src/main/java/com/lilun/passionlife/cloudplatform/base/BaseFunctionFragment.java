package com.lilun.passionlife.cloudplatform.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.StatedFragment;
import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.common.Constants;
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


    @Override
    public void onCreate( Bundle savedInstanceState) {
//        Logger.d("fragment oncreate");
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        fragManager = getActivity().getSupportFragmentManager();
        rootActivity= (BaseModuleActivity) getActivity();
        mCx= App.app;
//        bundle=savedInstanceState;
        orgiId= SpUtils.getString(Constants.key_currentOrgaId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        this.inflater=inflater;
        view = setView();
        ButterKnife.bind(this, view);
        return view;
    }

    public abstract View setView();


    @Subscribe
    public void def(Event.postService event){

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
