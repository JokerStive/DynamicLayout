package comt.sf.youke.dynamiclayout.Activity.Activity.net.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2016/5/23.
 */
public class CommonHeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        //获取请求方式，如果是POST、PUT就添加请求头
        String method = request.method();
        if(method.equals("POST")|| method.equals("PUT")){
            request.newBuilder()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .build();
        }

        String requestUrl = request.url().toString(); //获取请求url地址
        RequestBody body = request.body(); //获取请求body
        String bodyStr = (body==null?"":body.toString());
        //打印Request数据
        System.out.println("Request Url is :" + requestUrl + "\nMethod is : " + method + "\nRequest Body is :" + bodyStr + "\n");

        Response response = chain.proceed(request);
        ResponseBody body1 = response.body();
        System.out.println("code is : " + response.code() + "\nBody id :" + body1+"");

        return response;
    }
}
