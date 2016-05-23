package comt.sf.youke.dynamiclayout.Activity.Activity.net;

import retrofit2.Response;
import rx.functions.Func1;



/**
 * Created by Administrator on 2016/5/19.
 */
public class DataFilter<T> implements Func1<Response<T>, Boolean> {
    private ViewProxyInterface viewProxy;

   /* private DataFilter(ViewProxyInterface viewProxy) {
        this.viewProxy = viewProxy;
    }

    public static <T> DataFilter<T> getFilter(ViewProxyInterface viewProxy) {
        return new DataFilter<>(viewProxy);
    }*/

    public DataFilter() {
    }

    @Override
    public Boolean call(final Response<T> response) {
        if (!response.isSuccess()){
//           throw new Throwable();
            return false;
        }else{
            return true;
        }
    }
}
