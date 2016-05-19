package comt.sf.youke.dynamiclayout.Activity.Activity.net;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;



/**
 * Created by Administrator on 2016/5/19.
 */
public class DataFilter<T> implements Func1<Response<T>, Observable<T>> {


    public static <T> DataFilter<T> getFilter() {
        return new DataFilter<>();
    }
    @Override
    public Observable<T> call(Response response) {
        if (response.isSuccess()){
//            return Observable.from(response.body());
        }
        return null;
    }
}
