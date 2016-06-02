package comt.sf.youke.dynamiclayout.Activity.Activity.net.rxjava;

import android.util.Log;

import comt.sf.youke.dynamiclayout.Activity.Activity.exception.ApiException;
import comt.sf.youke.dynamiclayout.Activity.Activity.utils.LogUtils;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by youke on 2016/5/25.
 * 数据预处理，如果返回码不是200，抛出自定义异常
 */
public class RetroUtil {

   public static <T> Observable<T> flatResult(Response<T> response) {
        return Observable.create(subscriber->{
            if (response.isSuccess()){
                LogUtils.D("onNext");
                subscriber.onNext(response.body());
            }else{
                Log.i("OkHttp", "onError  will execute---");
                subscriber.onError(new ApiException(response.message(),response.code()));
            }

//            subscriber.onCompleted();
        });
    }
}
