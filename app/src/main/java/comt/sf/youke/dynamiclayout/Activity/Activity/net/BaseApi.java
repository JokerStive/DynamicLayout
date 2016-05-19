package comt.sf.youke.dynamiclayout.Activity.Activity.net;

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


    public static <T> Observable<T> getRes(Observable<Response<T>> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(DataFilter.getFilter())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e(throwable, throwable.getMessage());
                    }
                });

    }
}
