package comt.sf.youke.dynamiclayout.Activity.Activity.net.User;

import java.util.List;

import comt.sf.youke.dynamiclayout.Activity.Activity.bean.AclUser;
import comt.sf.youke.dynamiclayout.Activity.Activity.net.BaseApi;
import comt.sf.youke.dynamiclayout.Activity.Activity.net.UserService;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by Administrator on 2016/5/19.
 */
public class UserApi extends BaseApi{

    private UserService userService;

    public UserApi() {
        userService = retrofit.create(UserService.class);
    }

    public Observable<Response<List<AclUser>>> getAllUser(){
        return getRes(userService.getAllUser());
    }
}
