package com.lilun.passionlife.cloudplatform.utils;

import com.lilun.passionlife.cloudplatform.ui.App;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/1.
 */
public class CacheUtils {
    public static void putCache(String key,Object value){
        ACache.get(App.app).put(key, (Serializable) value);
    }

    public static Object getCache(String key){

        return  ACache.get(App.app).getAsObject(key);
    }


    public static void putCacheExpri(String key,Object value,int time){
        ACache.get(App.app).put(key, (Serializable) value,time);
    }

}
