package com.lilun.passionlife.cloudplatform.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.custom_view.LoadingPager;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2016/8/23.
 */
public abstract class BaseModuleLoadingFragment extends Fragment{

    protected LoadingPager loadingPager;
    protected String orgiId;
    protected double userId;
    protected BaseModuleActivity rootActivity;
    protected boolean hasCheckAddPermission;
    protected boolean hasCheckEditPermission;
    protected boolean hasCheckDeletePermission;
    protected LayoutInflater inflater;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        inflater = LayoutInflater.from(App.app);
        orgiId= SpUtils.getString(Constants.key_currentOrgaId);
        userId = (double)SpUtils.getInt(TokenManager.USERID);
        rootActivity= (BaseModuleActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        if (loadingPager==null){
            loadingPager = new LoadingPager(R.layout.page_empty,R.layout.page_error,R.layout.page_loading) {
                @Override
                protected View createSuccessView() {
                    return BaseModuleLoadingFragment.this.createSuccessView();
                }
            };
            loadingPager.setOnErrorPageClickListener(this::onErrorPageClick);
        }


        loadingPager.showPagerView();
        return loadingPager;
    }

    protected void onErrorPageClick(){};

    protected abstract View createSuccessView();

    public boolean isAdmin(){
        return SpUtils.getBoolean(Constants.ADMIN);
    }

    protected void showEmpty(){
        loadingPager.showEmpty();
    }

    protected void showLoading(){
        loadingPager.showLoading();
    }

    protected void showError(){
        loadingPager.showError();
    }

    protected void showSuccess(){
        loadingPager.showSuccess();
    }

    protected void setGvModuleColumns(GridView gv,List data){
        gv.setNumColumns(data.size()>1?2:1);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (loadingPager.isLoadingShow()){
            rootActivity.onUnsubscribe();
            showError();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

}
