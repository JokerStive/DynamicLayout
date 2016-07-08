package com.lilun.passionlife.cloudplatform.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by Administrator on 2016/6/29.
 */
public class PropertiesUtils {
    private static String TAG = PropertiesUtils.class.getCanonicalName();
    private static final String path = "/data/data/com.lilun.passionlife/userinfo.properties";

    private static String keyValue = "";


    public static String getProperties(String keyName) {
        Properties props = new Properties();
        try {
            InputStream in = new FileInputStream(getSettingFile());
            props.load(in);
            keyValue = props.getProperty (keyName);

//			Log.i(TAG, "获取的值：：：：：keyValue===="+keyValue);


        } catch (Exception e1) {

            e1.printStackTrace();
        }
        return keyValue;
    }

    public static void setProperties(String keyName, String keyValue) {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(getSettingFile()));
            OutputStream out = new FileOutputStream(getSettingFile());

            Enumeration<?> e = props.propertyNames();
            if (e.hasMoreElements()) {
                while (e.hasMoreElements()) {
                    String s = (String) e.nextElement();
                    if (!s.equals(keyName))
                        props.setProperty(s, props.getProperty(s));
                }
            }
            props.setProperty(keyName, null == keyValue ? "":keyValue);
            props.store(out, null);

//			Log.i(TAG, "将数据写入文件中====================="+keyValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File getSettingFile() {
        File setting = new File(path);
        if (!setting.exists())
            try {
                setting.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return setting;
    }
}
