package com.lilun.passionlife.cloudplatform.utils;

import com.lilun.passionlife.cloudplatform.ui.App;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import st.lowlevel.storo.Storo;

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

    public static boolean isEffective(String key){
       return Storo.contains(key) && !Storo.hasExpired(key).execute();
    }

    /**
    *缓存超时 ---分钟
    */
    public static void putCacheExpired(String key,Object value,int time){
        Storo.put(key, value).setExpiry(time, TimeUnit.MINUTES).execute();
    }
}
