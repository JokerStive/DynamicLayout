package com.lilang.passionlife.cloudplatform.net.User;

import com.lilang.passionlife.cloudplatform.bean.Account;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by youke on 2016/5/19.
 */
public interface UserService {


    @GET("Accounts")
    Observable<Response<List<Account>>>  getAll();

    @POST("Accounts")
    Observable<Response<Account>>  register(@Body Account user);

    @DELETE("Accounts/{id}")
    Observable<Response<String>> delete(@Path("id") String id);


}
