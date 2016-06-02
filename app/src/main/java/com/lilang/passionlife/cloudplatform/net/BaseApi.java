package com.lilang.passionlife.cloudplatform.net;

import com.lilang.passionlife.cloudplatform.net.retrofit.HttpMethods;
import com.lilang.passionlife.cloudplatform.net.rxjava.RetroUtil;

import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/19.
 */
public class BaseApi {
    public Retrofit retrofit;

    public BaseApi() {
        retrofit = HttpMethods.getInstance();
    }


    public static void onLoading(ViewProxyInterface viewProxy) {
        if (viewProxy != null) {
            viewProxy.onLoading();
        }
    }

    public static void onFailed(ViewProxyInterface viewProxy) {
        if (viewProxy != null) {
            viewProxy.onFailed();
        }
    }

    public static void onSuccess(ViewProxyInterface viewProxy) {
        if (viewProxy != null) {
            viewProxy.onSuccess();
        }
    }

    public static void onEmpty(ViewProxyInterface viewProxy) {
        if (viewProxy != null) {
            viewProxy.onEmpty();
        }
    }

    public static void onNoMore(ViewProxyInterface viewProxy) {
        if (viewProxy != null) {
            viewProxy.onNoMore();
        }
    }



    public static <T> Observable getRes(Observable<Response<T>> observable, final ViewProxyInterface viewProxy) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .flatMap(RetroUtil::flatResult);


    }


}
