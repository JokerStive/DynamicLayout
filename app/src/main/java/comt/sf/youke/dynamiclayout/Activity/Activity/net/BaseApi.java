package comt.sf.youke.dynamiclayout.Activity.Activity.net;

import android.util.Log;

import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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


   /* public static <T> Observable<Response<T>> getRes(Observable<Response<T>> observable,ViewProxyInterface viewProxy) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("",throwable.getMessage()+"----------------------");
                    }
                })
                .filter((Func1<? super Response<T>, Boolean>) DataFilter.getFilter(viewProxy));

    }*/

    public static <T> Observable<Response<T>> getRes(Observable<Response<T>> observable, final ViewProxyInterface viewProxy) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
              .map(DataFilter.getFilter(viewProxy))
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("", throwable.getMessage() + "--------------");
                        onFailed(viewProxy);
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("", throwable.getMessage() + "--------------");
                        Timber.e(throwable, throwable.getMessage());
                    }
                })
//                .filter((Func1<? super T, Boolean>) DataFilter.getFilter(viewProxy))
                ;
    }
}
