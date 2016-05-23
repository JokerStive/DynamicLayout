package comt.sf.youke.dynamiclayout.Activity.Activity.net;

import java.util.List;

import comt.sf.youke.dynamiclayout.Activity.Activity.bean.Account;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2016/5/19.
 */
public interface UserService {

    /*@Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })*/
    @GET("Accounts")
    Observable<Response<List<Account>>>  getAll();


   /* @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })*/
    @POST("Accounts")
    Observable<Response<Account>>  register(@Body Account user );

    @DELETE("Accounts/{id}")
    Observable<Response<Integer>> delete(@Path("id") String id);
}
