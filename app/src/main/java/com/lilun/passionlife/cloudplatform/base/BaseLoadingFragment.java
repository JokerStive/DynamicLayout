package com.lilun.passionlife.cloudplatform.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.StatedFragment;
import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.custom_view.LoadingPager;

/**
 * Created by youke on 2016/8/17.
 */
public abstract class BaseLoadingFragment extends StatedFragment {

    protected LoadingPager loadingPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (loadingPager==null){
            loadingPager = new LoadingPager(R.layout.page_empty,R.layout.page_error,R.layout.page_loading) {
                @Override
                protected View createSuccessView() {
                    return BaseLoadingFragment.this.createSuccessView();
                }
            };
            loadingPager.setOnErrorPageClickListener(this::onErrorPageClick);
        }

        loadingPager.showPagerView();
        return loadingPager;
    }


    protected void onErrorPageClick(){}

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
        if (loadingPager!=null){
            loadingPager.showSuccess();
        }
    }


    /**
    *加载成功的页面，让子类去实现
    */
    protected abstract View createSuccessView();



//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        Logger.d("fragment onAttach-- ");
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Logger.d("fragment onCreate-- ");
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Logger.d("fragment onActivityCreated-- ");
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        Logger.d("fragment onDestroyView-- ");
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Logger.d("fragment onDestroy-- ");
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        Logger.d("fragment onDestroy-- ");
//    }
}
