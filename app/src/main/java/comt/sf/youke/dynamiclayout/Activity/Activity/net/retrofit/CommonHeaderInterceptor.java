package comt.sf.youke.dynamiclayout.Activity.Activity.net.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by youke on 2016/5/23.
 */
public class CommonHeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        //根据请求方式添加header
        String method = request.method();
        if(method.equals("POST")|| method.equals("PUT")){
            request.newBuilder()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .build();
        }else if(method.equals("GET")|| method.equals("DELETE")){
            request.newBuilder()
                    .header("Accept", "application/json")
                    .build();
        }


        return chain.proceed(request);
    }
}
