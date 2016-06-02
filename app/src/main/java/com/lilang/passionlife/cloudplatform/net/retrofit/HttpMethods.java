package com.lilang.passionlife.cloudplatform.net.retrofit;

import com.lilang.passionlife.cloudplatform.common.AccountManager;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/5/19.
 */
public class HttpMethods {
    public static final String BASE_URL = "http://222.182.202.94:3000/api/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
//    private MovieService movieService;

    private HttpMethods() {

        //log拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //token拦截器
        Interceptor mTokenInterceptor = chain -> {
            Request originalRequest = chain.request();
            if (AccountManager.sToken == null ) {
                return chain.proceed(originalRequest);
            }
            Request authorised = originalRequest.newBuilder()
                    .header("Authorization", AccountManager.sToken)
                    .build();
            return chain.proceed(authorised);
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(new CommonHeaderInterceptor())
                //设置token拦截器
                .addNetworkInterceptor(mTokenInterceptor)
                .addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

    }

    //在访问HttpMethods时创建单例

    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static Retrofit getInstance() {
        return SingletonHolder.INSTANCE.retrofit;
    }


}
