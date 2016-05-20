package comt.sf.youke.dynamiclayout.Activity.Activity.net;

import java.util.List;

import comt.sf.youke.dynamiclayout.Activity.Activity.bean.AclUser;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/5/19.
 */
public interface UserService {
    @GET("Accounts")
    Observable<Response<List<AclUser>>>  getAll();

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @POST("/Accounts")
    Observable<Response<AclUser>>  register(@Body AclUser user );

}
