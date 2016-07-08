package com.lilun.passionlife.cloudplatform.exception;

/**
 * Created by Administrator on 2016/6/2.
 */
public class NetException  extends Exception{
    private String message="网络异常";
    public String getMessage(){
        return message;
    }
}
