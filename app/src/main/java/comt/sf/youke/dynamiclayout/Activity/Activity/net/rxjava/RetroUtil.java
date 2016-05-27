package comt.sf.youke.dynamiclayout.Activity.Activity.net.rxjava;

import android.util.Log;

import comt.sf.youke.dynamiclayout.Activity.Activity.exception.ApiException;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by youke on 2016/5/25.
 * 数据预处理
 */
public class RetroUtil {

   public static <T> Observable<T> flatResult(Response<T> response) {
        return Observable.create(subscriber->{
            if (response.isSuccess()){
                Log.i("OkHttp","onNext will execute---");
                subscriber.onNext(response.body());
            }else{
                Log.i("OkHttp", "onError  will execute---");
                subscriber.onError(new ApiException(response.message(),response.code()));
            }

//            subscriber.onCompleted();
        });
    }
}
