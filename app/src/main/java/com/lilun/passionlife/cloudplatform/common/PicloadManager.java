package com.lilun.passionlife.cloudplatform.common;

import android.graphics.Bitmap;

import com.lilun.passionlife.cloudplatform.utils.SpUtils;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;

import java.net.URLEncoder;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2016/7/18.
 */
public class PicloadManager {
    public static final int CHOOSE_PICTURE = 0;
    public static final int TAKE_PICTURE = 1;
    public static final int CROP_SMALL_PICTURE = 2;

//    ---------------------------------------------------------------------


    public static RequestBody getUploadIconRequestBody(Bitmap bitmap) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), StringUtils.getBytesFromBitmap(bitmap));
        return requestBody;
    }

    /**
     * 上传组织icon的URL
     */
    public static String orgaIconUrl(String orgaId) {

        String url = Constants.BASE_URL + "Organizations/" + toURLEncoded(orgaId) + "/icon" + "?access_token=" + SpUtils.getString(TokenManager.TOKEN);
        return url;
    }


    /**
     * 上传OrgaServicreIcon的URL
     */
    public static String orgaServiceIconUrl(String orgaServiceId) {

        String url = Constants.BASE_URL + "OrganizationServices/" + toURLEncoded(orgaServiceId) + "/icon" + "?access_token=" + SpUtils.getString(TokenManager.TOKEN);
        return url;
    }



    /**
    *url转义
    */
    public static String toURLEncoded(String target) {
        try {
            String str = new String(target.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
        }

        return "";
    }

}
