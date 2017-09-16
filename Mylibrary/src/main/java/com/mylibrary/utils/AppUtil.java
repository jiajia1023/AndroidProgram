package com.mylibrary.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created jjj on 2017/3/7.
 */

public class AppUtil {
    /**
     * 判断非空 出去空格
     *
     * @param string
     * @return
     */
    public static boolean isEmptyForTrim(String string) {
        if (string != null && !TextUtils.isEmpty(string.trim())) {
            return false;
        }
        return true;
    }

    public static boolean isEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else if (email.matches("^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$")) {
            return true;
        } else {
            return false;
        }
    }

    public static void showToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取设备最大内存,单位为字节(B)
     *
     * @return
     */
    public static int getMaxMemory() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        return maxMemory;
    }

    /**
     * 关闭键盘事件
     *
     * @param activity
     */
    public static void closeSoftInput(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 当前软键盘是否打开
     *
     * @param activity
     * @return
     */
    public static boolean isShowSoftInput(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputMethodManager.isActive();
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName 包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 返回当前程序版本名
     */
    public static String[] getAppVersionName(Context context) {
        String[] vObjects = new String[2];
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            vObjects[0] = pi.versionName;
            vObjects[1] = String.valueOf(pi.versionCode);
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return vObjects;
    }

    /**
     * 判断服务是否在运行
     *
     * @param context
     * @param className
     * @param maxServiceNum
     * @return
     */
    public static  boolean isServiceRunning(Context context, String className, int maxServiceNum) {
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = ((ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE))
                .getRunningServices(maxServiceNum);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServiceInfos) {
            if (runningServiceInfo.service.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }
}
