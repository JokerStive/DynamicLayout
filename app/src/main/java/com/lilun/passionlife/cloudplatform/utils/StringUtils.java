package com.lilun.passionlife.cloudplatform.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.orhanobut.logger.Logger;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by youke on 2016/6/27.
 */
public class StringUtils {
    public static final String MODULE = "模块管理";
    public static final String STAFF = "员工管理";
    public static final String DEPARTMENT = "部门管理";
    public static final String INFORMATION = "信息管理";
    public static final String ORGANIZATION = "模块管理";
    public static final String ROLE = "角色管理";
    public static final String SYSTEM = "系统配置";


    public static long IOS2ToUTC(String isoTime1) {
        String[] ss = isoTime1.split("\\.");
        String isoTime = ss[0] + "+08:00";
        DateTimeFormatter parser2 = ISODateTimeFormat.dateTimeNoMillis();
        DateTime dateTime = parser2.parseDateTime(isoTime);

        return dateTime.getMillis() / 1000;
    }


    /**
     * list数据去重复
     */
    public static List<Integer> removeRepet(List<Integer> ls) {
        List<Integer> tempList = new ArrayList<Integer>();
        for (Integer i : ls) {
            if (!tempList.contains(i)) {
                tempList.add(i);
            }
        }
        return tempList;
    }

    public static boolean checkEmpty(String... s) {
        for(String string:s){
            if (TextUtils.isEmpty(string)) {
                ToastHelper.get(App.app).showShort("输入不能为空");
                return false;
            }
        }
        return true;
    }


    /**
     * 根据account的orgaId的到所属组织的Id
     */
    public static String belongOgaId(String orgId) {
        return orgId.substring(0, orgId.length() - 7);
    }


    /**
     * 根据account的orgaId的到所属组织的Id
     */
    public static String belongOgaName(String orgId) {
        String s = belongOgaId(orgId);
        return s.substring(s.lastIndexOf("/") + 1);
    }


    /**
     * 根据serviceId得到对应的service的Name
     */
    public static String getServiceName(String serviceId) {
        if (serviceId.equals(KnownServices.Module_Service)) {
            return MODULE;
        } else if (serviceId.equals(KnownServices.Account_Service)) {
            return STAFF;
        } else if (serviceId.equals(KnownServices.Role_Service)) {
            return ROLE;
        } else if (serviceId.equals(KnownServices.Organization_Service)) {
            return ORGANIZATION;
        } else if (serviceId.equals(KnownServices.SysConfig_Service)) {
            return SYSTEM;
        } else if (serviceId.equals(KnownServices.Information_Service)) {
            return INFORMATION;
        } else if (serviceId.equals(KnownServices.Department_Service)) {
            return DEPARTMENT;
        }
        return "";
    }


//    public static File getBytesFromBitmap(Bitmap bitmap){
//        FileOutputStream fos = null;
//        File f = new File(App.app.getCacheDir(), "---");
//        try {
//            f.createNewFile();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)){
//                Logger.d(" compress success");
//            }
//            byte[] byteArray = stream.toByteArray();
//             fos =  new FileOutputStream(f);
//            fos.write(byteArray);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                fos.flush();
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return  f;
//    }


    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        Logger.d("图片占用大小==" + byteArray.length / 1024);
        return byteArray;

    }


}
