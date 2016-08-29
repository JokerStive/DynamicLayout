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
//
//    public static String BASE_URL = "";
//    private Retrofit retrofit;

    private RetrofitManager() {

//        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
//        log.setLevel(HttpLoggingInterceptor.Level.BODY);
////
////        //缓存路劲
//        File cacheFile = new F ile(App.app.getCacheDir(),"retrofitResCache");
////        //缓存大小
//        Cache cache = new Cache(cacheFile, 10 * 1024 * 1024);
//
//        okhttpClient = new OkHttpClient.Builder()
//                //超时时长
//                .connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                .readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                //超时重连
//                .retryOnConnectionFailure(true)
//                .addNetworkInterceptor(new HttpInterceptor())
//                .addInterceptor(log)
//                //缓存
//                .cache(cache)
//                .build();

//
//        retrofit = new Retrofit.Builder()
//                .client(okhttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .baseUrl(Constants.BASE_URL)
//                .build();

    }


//
//    //在访问HttpMethods时创建单例
//
//    private static class SingletonHolder {
//        private static  RetrofitManager INSTANCE = new RetrofitManager();
//    }

    /**
    *获取retrofit实例
    */
    public static <S> S createService(String url,Class<S> serviceClass) {
        return RetrofitBuilder(url).build().create(serviceClass);
    }

    private static OkHttpClient.Builder httpClientBuilder(){
        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        //缓存路劲
        File cacheFile = new File(App.app.getCacheDir(),"retrofitResCache");
//        //缓存大小
        Cache cache = new Cache(cacheFile, 10 * 1024 * 1024);

      return new OkHttpClient.Builder()
                //超时时长
                .connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                //超时重连
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(new HttpInterceptor())
                .addInterceptor(log)
                //缓存
                .cache(cache);
    }


    private static Retrofit.Builder RetrofitBuilder(String url){
        return new Retrofit.Builder()
                .client(httpClientBuilder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constants.BASE_URL);
    }

}
