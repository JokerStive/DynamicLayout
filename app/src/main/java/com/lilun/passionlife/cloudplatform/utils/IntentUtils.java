package com.lilun.passionlife.cloudplatform.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Administrator on 2016/6/3.
 */
public class IntentUtils {
    public static void startAct(Activity old,Class<?> clazz){
        Intent intent = new Intent(old,clazz);
        old.startActivity(intent);
    }
}
