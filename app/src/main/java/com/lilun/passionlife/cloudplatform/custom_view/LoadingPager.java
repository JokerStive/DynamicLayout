package com.lilun.passionlife.cloudplatform.custom_view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.UIUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by Administrator on 2016/8/17.
 */
public abstract class LoadingPager extends FrameLayout{

    // 加载默认的状态
    private static final int STATE_UNLOADED = 1;
    // 加载的状态
    private static final int STATE_LOADING = 2;
    // 加载失败的状态
    private static final int STATE_ERROR = 3;
    // 加载空的状态
    private static final int STATE_EMPTY = 4;
    // 加载成功的状态
    private static final int STATE_SUCCEED = 5;


    private View mLoadingView;// 转圈的view
    private View mErrorView;// 错误的view
    private View mEmptyView;// 空的view
    private View mSucceedView;// 成功的view

    private int mState;// 默认的状态


    private int loadpage_empty;
    private int loadpage_error;
    private int loadpage_loading;
    private AVLoadingIndicatorView avi_loading;
    private OnErrorPageClickListener listen;

    public LoadingPager( int empty, int error, int loading) {
        super(App.app);
        loadpage_empty = empty;
        loadpage_error = error;
        loadpage_loading = loading;
        init();
    }

    private   void init(){
        mState = STATE_UNLOADED;

        //初始化loading页面
        mLoadingView = createLoadingView();
        if (mLoadingView!=null){
            addView(mLoadingView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }


        //初始化 加载错误 的页面
        mErrorView = createErrorView();
        if (null != mErrorView) {
            addView(mErrorView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        }


        //初始化 数据为空  的页面
        mEmptyView = createEmptyView();
        if (null != mEmptyView) {
            addView(mEmptyView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        }


        mSucceedView=createSuccessView();
        if (null!=mSucceedView){
            addView(mSucceedView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        }

//        showSafePagerView();


    }



    /**
    *展示页面
    */
    public void showPagerView() {
        if (null != mLoadingView) {
            if (mState == STATE_UNLOADED|| mState == STATE_LOADING ){
                mLoadingView.setVisibility(VISIBLE);
                Logger.d("startAnim");
                startAnim();
            }else{
                mLoadingView.setVisibility(INVISIBLE);
                stopAnim();
                Logger.d("stopAnim");
            }
//            mLoadingView.setVisibility(mState == STATE_UNLOADED|| mState == STATE_LOADING ?VISIBLE :INVISIBLE);
        }

        if (null!=mEmptyView){
            mEmptyView.setVisibility(mState==STATE_EMPTY?VISIBLE:INVISIBLE);
        }

        if (null!=mErrorView){
            mErrorView.setVisibility(mState==STATE_ERROR?VISIBLE:INVISIBLE);
        }

//        if (mState==STATE_SUCCEED && mSucceedView==null){
//            mSucceedView = createSuccessView();
//            addView(mSucceedView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        }

        if (null!=mSucceedView){
            mSucceedView.setVisibility(mState==STATE_SUCCEED?VISIBLE:INVISIBLE);
        }
    }



    public int getState(){
        return mState;
    }

    public boolean isLoadingShow(){
        return mLoadingView.getVisibility() == VISIBLE;
    }

    public void showEmpty(){
        mState = STATE_EMPTY;
        showPagerView();
    }

    public void showError(){
        mState = STATE_ERROR;
        showPagerView();
    }

    public void showSuccess(){
        mState =STATE_SUCCEED;
        showPagerView();
    }

    public void showLoading() {
        mState = STATE_LOADING;
        showPagerView();
    }




    /**
    *成功的页面需要子类去实现
    */
    protected abstract View createSuccessView();


    public void setOnErrorPageClickListener(OnErrorPageClickListener listener){
        this.listen =listener;
    }

    public interface OnErrorPageClickListener{
        void onErrorPageClick();
    }



    /**
    *数据为空
    */
    private View createEmptyView() {
        if (loadpage_empty!=0){
            return UIUtils.inflate(loadpage_empty);
        }
        return null;
    }

    /**
    *page_error。。
    */
    private View createErrorView() {
        if (loadpage_error!=0){
            View errorView = UIUtils.inflate(loadpage_error);
            ImageView ivError = (ImageView) errorView.findViewById(R.id.iv_error);
            ivError.setOnClickListener(v -> {
                if (listen!=null){
                    listen.onErrorPageClick();
                }
            });
            return errorView;
        }
        return null;
    }


    /**
     * 加载中。。。
     *
     * @return
     */
    public View createLoadingView() {
        if (loadpage_empty != 0) {
            View view = UIUtils.inflate(loadpage_loading);
            avi_loading = (AVLoadingIndicatorView) view.findViewById(R.id.avi_loading);
            return view;
        }
        return null;
    }


    void startAnim(){
        avi_loading.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avi_loading.hide();
        // or avi.smoothToHide();
    }




//    public enum LoadResult {
//        ERROR(3), EMPTY(4), SUCCESS(5);
//        int value;
//
//        LoadResult(int value) {
//            this.value = value;
//        }
//
//        public int getValue() {
//            return value;
//        }
//    }
}
