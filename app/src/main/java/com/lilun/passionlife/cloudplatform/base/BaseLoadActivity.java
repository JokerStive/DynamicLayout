package com.lilun.passionlife.cloudplatform.base;

import android.os.Bundle;
import android.view.View;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.User;
import com.lilun.passionlife.cloudplatform.custom_view.LoadingPager;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by youke on 2016/8/25.
 */
public abstract class BaseLoadActivity extends   BaseNetAppcomActivity{

    protected LoadingPager loadingPager;
    protected String orgaId;
    protected double userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadingPager = new LoadingPager(R.layout.page_empty,R.layout.page_error,R.layout.page_loading) {
            @Override
            protected View createSuccessView() {
                return BaseLoadActivity.this.createSuccessView();
            }
        };
        loadingPager.showPagerView();
        loadingPager.setOnErrorPageClickListener(this::onErrorPageClick);
        setContentView(loadingPager);
        orgaId = SpUtils.getString(Constants.key_currentOrgaId);
        userId = (double)User.getId();
        Logger.d("main onCreate  userId = "+userId);
    }

    protected void onErrorPageClick(){}

    protected void showEmpty(){
        if (loadingPager!=null){
            loadingPager.showEmpty();
        }
    }

    protected void showLoading(){
        if(loadingPager!=null){
            loadingPager.showLoading();
        }
    }

    protected void showError(){
        if(loadingPager!=null){
            loadingPager.showError();
        }
    }

    protected void showSuccess(){
        if (loadingPager!=null){
            loadingPager.showSuccess();
        }
    }


    protected abstract View createSuccessView();
}
