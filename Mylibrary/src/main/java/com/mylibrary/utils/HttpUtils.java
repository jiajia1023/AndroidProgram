package com.mylibrary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.Set;

/**
 * Created by Administrator on 2016/6/6.
 */
public class HttpUtils {

    /**
     * 格式化Get网络请求的URL
     * @param url
     * @param bundle
     * */
    public static String httpEventGetFormat(String url, Bundle bundle) {
        if (null != bundle) {
            url += "?";
            Set<String> keySet = bundle.keySet();
            for (String key : keySet) {
                String value = bundle.get(key) + "";
                url += String.format("%s=%s&", key, value);
            }
            url.substring(0, url.length() - 1);
        }
        return url;
    }

    /**
     * 图片链接是否正确
     * The image link correct or not
     * @param url
     * @return
     */
    public static Boolean isPicUrlFormatRight(String url) {
        url = url.toLowerCase();
        String[] postfixs = new String[]{"png", "jpg", "jpg", "gif"};
        if (url.startsWith("http://") || url.startsWith("https://")) {
            for (String postfix : postfixs) {
                if (url.endsWith(postfix)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 文件链接是否正确
     * The file link correct or not
     * @param url
     * @return
     */
    public static Boolean isFileUrlFormRight(String url) {
        if (!TextUtils.isEmpty(url)) {
            url = url.toLowerCase();
            String[] postfixs = new String[]{".png", ".jpg", ".jpg", ".gif", ".mp3", ".mp4", ".zip"};
            if (url.startsWith("http://") || url.startsWith("https://")) {
                for (String postfix : postfixs) {
                    if (url.endsWith(postfix)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 网络链接是否正确
     * The http link is correct or not
     * @param url
     * @return
     */
    public static Boolean isHttpUrlFormRight(String url) {
        if (!TextUtils.isEmpty(url)) {
            url = url.toLowerCase();
            if (url.startsWith("http://") || url.startsWith("https://")) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param imgurl 图片链接
     * @return
     * @describe 返回图片链接是否有效
     */
    public static Boolean isImgUrlValid(String imgurl) {
        Boolean flag = false;

        if (null != imgurl && !"".equalsIgnoreCase(imgurl) && (imgurl.endsWith(".png") || imgurl.endsWith(".PNG")
                || imgurl.endsWith(".jpg") || imgurl.endsWith(".JPG")
                || imgurl.endsWith(".gif") || imgurl.endsWith(".GIF")
                || imgurl.endsWith(".jpge") || imgurl.endsWith(".JPGE"))) {
            flag = true;
        }

        return flag;
    }

    /**
     * @return 返回是否有网
     */
    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager connectivitymanager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivitymanager == null) {
            return false;
        }
        NetworkInfo info = connectivitymanager.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            return false;
        }
        return true;
    }


}
