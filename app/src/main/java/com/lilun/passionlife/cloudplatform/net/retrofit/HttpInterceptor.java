package com.lilun.passionlife.cloudplatform.net.retrofit;

import android.text.TextUtils;

import com.lilun.passionlife.cloudplatform.bean.Event;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.NetUtil;
import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by youke on 2016/5/23.
 * okhttp3配置，不同的请求方式，不同的header
 */
public class HttpInterceptor implements Interceptor {

    private HttpUrl url1;

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request originalRequest = chain.request();

        String token = SpUtils.getString(TokenManager.TOKEN);
        if (!TextUtils.isEmpty(token)) {
            String ul = originalRequest.url().toString();
            String url = "";
            if (ul.contains("?")) {
                url = originalRequest.url() + "&access_token=" + token;
            } else {
                url = originalRequest.url() + "?access_token=" + token;
            }
//            String url = originalRequest.url() + "?access_token="+token ;
            url1 = HttpUrl.parse(url);
            originalRequest = originalRequest.newBuilder()
//                    .header("access_token",token)
                    .url(url1)
                    .build();
        }


        CacheControl cacheControl = originalRequest.cacheControl();

        Response response = chain.proceed(originalRequest);
        int code = response.code();
        if (code == 401) {
            if (!url1.toString().contains("Accounts/me")) {
                EventBus.getDefault().post(new Event.Event401());
            } else {
                Logger.d("post event");
                EventBus.getDefault().post(new Event.AuthoriseEvent());

            }
        }


        if (!TextUtils.isEmpty(cacheControl.toString()) && response.code() == 200) {


            if (NetUtil.checkNet(App.app)) {
                response = response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + cacheControl.maxAgeSeconds())
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 7; // 无网络时，设置超时为4周
                response = response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + cacheControl.maxStaleSeconds())
                        .removeHeader("Pragma")
                        .build();
            }

        }

        return response;
    }
}
