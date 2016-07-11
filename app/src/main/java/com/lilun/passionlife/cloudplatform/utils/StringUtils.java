package com.lilun.passionlife.cloudplatform.utils;

import android.text.TextUtils;

import com.lilun.passionlife.cloudplatform.common.Constants;
import com.lilun.passionlife.cloudplatform.common.TokenManager;
import com.lilun.passionlife.cloudplatform.ui.App;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youke on 2016/6/27.
 */
public class StringUtils {
    public static long  IOS2ToUTC(String isoTime1){
        String[] ss = isoTime1.split("\\.");
        String isoTime = ss[0]+"+08:00";
        DateTimeFormatter parser2 = ISODateTimeFormat.dateTimeNoMillis();
        DateTime dateTime = parser2.parseDateTime(isoTime);

        return dateTime.getMillis()/1000;
    }


    //    ttp://222.182.202.94:3000/api/Accounts/1/hasPermission?role=SysConfig.view&access_token=
    public static String permissionUrl(int userid,String permission) {
        String token = SpUtils.getString(TokenManager.TOKEN);
        if (TextUtils.isEmpty(token)) {
            return "";
        }
        return  Constants.BASE_URL + "Accounts/" + userid + "/hasPermission?role=" + permission + "&access_token="+token;
    }


    /**
    *list数据去重复
    */
    public static List<Integer> removeRepet(List<Integer> ls){
        List<Integer> tempList= new ArrayList<Integer>();
        for(Integer i:ls){
            if(!tempList.contains(i)){
                tempList.add(i);
            }
        }
        return tempList;
    }

    public static boolean checkEmpty(String  s){
        if (TextUtils.isEmpty(s)){
            ToastHelper.get(App.app).showShort("输入不能为空");
            return false;
        }
        return true;
    }


    public static int randow(){
        String s = System.currentTimeMillis() + "";
        String s1 = s.substring(s.length() - 4);
        return Integer.parseInt(s1);
    }
}
