package com.lilun.passionlife.cloudplatform.net.retrofit;

import java.io.IOException;

import retrofit2.Converter;

/**
 * Created by Administrator on 2016/7/2.
 */
public class ResponseFactory implements Converter {

    @Override
    public String convert(Object value) throws IOException {

        return value.toString();
    }
}
