package comt.sf.youke.dynamiclayout.Activity.Activity.net;

import java.util.concurrent.TimeUnit;

import comt.sf.youke.dynamiclayout.Activity.Activity.net.retrofit.CommonHeaderInterceptor;
import okhttp3.OkHttpClient;
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

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(new CommonHeaderInterceptor())
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
