package com.lilun.passionlife.cloudplatform.ui.home;

/**
 * Created by Administrator on 2016/8/26.
 */
public interface BaseView<T> {
    void setPresenter(T presenter);

    void showLoading();

    void hintLoading();

    void showErrorSnack(String s);

    void showNormalSnack(String s);

}


