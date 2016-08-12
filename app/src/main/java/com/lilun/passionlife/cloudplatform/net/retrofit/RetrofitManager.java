package com.lilun.passionlife.cloudplatform.net.retrofit;

import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.ui.App;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by youke on 2016/5/19.
 * 配置okhttp和retrofit
 */
public class RetrofitManager {

    private Retrofit retrofit;
    private  OkHttpClient okhttpClient;

    private RetrofitManager() {

        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        //缓存路劲
        File cacheFile = new File(App.app.getCacheDir(),"retrofitResCache");
//        //缓存大小
        Cache cache = new Cache(cacheFile, 10 * 1024 * 1024);

        okhttpClient = new OkHttpClient.Builder()
                //超时时长
                .connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                //超时重连
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(new HttpInterceptor())
                .addInterceptor(log)
                //缓存
                .cache(cache)
                .build();


        retrofit = new Retrofit.Builder()
                .client(okhttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build();

    }

    void setRetrofitUrl(String url){
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .build();
    }

    //在访问HttpMethods时创建单例

    private static class SingletonHolder {
        private static  RetrofitManager INSTANCE = new RetrofitManager();
    }

    /**
    *获取retrofit实例
    */
    public static Retrofit getRetrofit() {
        return SingletonHolder.INSTANCE.retrofit;
    }


    /**
     *获取retrofit实例
     */
    public static RetrofitManager getRetrofitManager() {
        return SingletonHolder.INSTANCE;
    }

    /**
    *获取okhttpClient实例
    */
    public static OkHttpClient getOkHttpClient(){return SingletonHolder.INSTANCE.okhttpClient;}

}
