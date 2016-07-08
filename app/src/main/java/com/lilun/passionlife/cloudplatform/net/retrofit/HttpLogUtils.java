package com.lilun.passionlife.cloudplatform.net.retrofit;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by youke on 2016/6/17.
 * 打印http request和reponse
 */
public class HttpLogUtils {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public static void logRequest(Request request) throws IOException {
        String requestStartMessage ="--> " + request.method() + ' ' + request.url() + ' '+"\n";

        Headers headers = request.headers();
        RequestBody requestBody = request.body();

        StringBuffer buffer=new StringBuffer();

        buffer.append(requestStartMessage);


        if (requestBody!=null){
            buffer.append("\n"+"--> requestBody  ");
            //请求体
            Buffer buffer1 = new Buffer();
            requestBody.writeTo(buffer1);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            buffer.append(buffer1.readString(charset));

        }
        Logger.d(buffer.toString());

    }

    public static void logResponse(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        String bodyMsg = "<-- " + response.code() + ' ' + response.message() + ' '+ response.request().url()+"\n";
        StringBuffer buffer=new StringBuffer();
        buffer.append(bodyMsg);
        buffer.append("<-- reponseBody  ");

        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            e.printStackTrace();
        }


        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }



        long contentLength = responseBody.contentLength();
        if (contentLength != 0) {
            String s = source.buffer().clone().readString(charset);
            buffer.append(s);
        }

        Logger.d(buffer.toString());

    }

}
