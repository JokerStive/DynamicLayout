package comt.sf.youke.dynamiclayout.Activity.Activity.net;

import java.util.List;

import comt.sf.youke.dynamiclayout.Activity.Activity.bean.AclUser;
import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Administrator on 2016/5/19.
 */
public interface UserService {
    @GET("AclUsers")
    Observable<Response<List<AclUser>>>  getAllUser();


}
