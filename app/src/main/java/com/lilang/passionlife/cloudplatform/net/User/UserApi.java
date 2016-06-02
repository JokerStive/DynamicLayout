package com.lilang.passionlife.cloudplatform.net.User;

import com.lilang.passionlife.cloudplatform.bean.Account;
import com.lilang.passionlife.cloudplatform.net.BaseApi;

import java.util.List;
import rx.Observable;

/**
 * Created by Administrator on 2016/5/19.
 */
public class UserApi extends BaseApi {

    private UserService userService;

    public UserApi() {
        userService = retrofit.create(UserService.class);
    }

    public Observable<List<Account>> getAllUser(){

        return getRes(userService.getAll(),null);


    }

    public Observable<String> delete(String id) {
        return getRes(userService.delete(id),null);
    }

    public Observable<Account> register(Account user){
        return getRes(userService.register(user), null);
    }
}
