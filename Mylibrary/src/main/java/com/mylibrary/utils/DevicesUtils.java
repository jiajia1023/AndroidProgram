/*
 * yidingliu.com Inc. * Copyright (c) 2016 All Rights Reserved.
 */

package com.mylibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.mylibrary.manager.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * Created by chriszou on 16/9/28.
 */

public class DevicesUtils {

    /**
     * get window manager
     *
     * @param context
     * @return 返回窗口管理信息类，通过其可获得设备信息，example:屏幕高宽
     */
    public static Display getScreenDisplay(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display mDisplay = wm.getDefaultDisplay();
        return mDisplay;
    }

    /**
     * 获取屏幕宽度,单位：PX
     *
     * @param context
     * @return The screen width
     */
    public static int getScreenWidth(Context context) {
        int width = getScreenDisplay(context).getWidth();
        return width;
    }

    /**
     * 获取屏幕高度，单位：px
     *
     * @param context
     * @return The screen height
     */
    public static int getScreenHeight(Context context) {
        int height = getScreenDisplay(context).getHeight();
        return height;
    }

    /**
     * 获取屏幕密度（DPI）
     *
     * @return 屏幕密度
     */
    public static int getScreenDensityDPI() {
        DisplayMetrics dm = new DisplayMetrics();
        int density = dm.densityDpi;
        return density;
    }

    /**
     * 获取状态栏高度,单位：PX
     *
     * @param activity
     * @return 状态栏高度
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = activity.getResources()
                        .getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
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

    public static boolean installAPK(Context context, File file) {
        if (file.exists()) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(268435456);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
            Process.killProcess(Process.myPid());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断SD卡是否可用
     */
    public static boolean isSDcardOK() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SHA1码
     *
     * @param context
     * @return
     */
    public static String getSHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
            }
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取设备版本号
     *
     * @return
     */
    public static String getDeviceLevel() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备手机型号
     *
     * @return
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * 获取设备版本号
     *
     * @return
     */
    public static int getDeviceSDK() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
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

    public static void setImmerseLayout(View topBgView, Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //设置为沉浸式
            act.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //如果是MIUI主体,则设置为黑色的通知字体, 因为MIUI没有遮罩
            MIUIFlymeUtils.setStatusBarTextColor(act);
            MIUIFlymeUtils.setStatusBarDarkIcon(act.getWindow(), true);
            //状态栏高度
            int statusBarHeight = getStatusHeight(act);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) topBgView.getLayoutParams();
            params.height = statusBarHeight;
            topBgView.setLayoutParams(params);
        }
    }

    /**
     * 获取navigationBar高度
     *
     * @param activity
     * @return
     */
    public static int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int height = 0;
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        if (resourceId > 0 && checkDeviceHasNavigationBar(activity)) {
            height = resources.getDimensionPixelSize(resourceId);
        }
        return height;
    }

//    /**
//     * 检查设备是否包含navigationBar
//     * @param activity
//     * @return
//     */
//    @SuppressLint("NewApi")
//    public static boolean checkDeviceHasNavigationBar(Context activity) {
//
//        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
//        boolean hasMenuKey = ViewConfiguration.get(activity)
//                .hasPermanentMenuKey();
//        boolean hasBackKey = KeyCharacterMap
//                .deviceHasKey(KeyEvent.KEYCODE_BACK);
//
//        if (!hasMenuKey && !hasBackKey) {
//            // 做任何你需要做的,这个设备有一个导航栏
//            return true;
//        }
//        return false;
//    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            Log.w(context.getClass().getSimpleName(), "" + e.getMessage());
        }

        return hasNavigationBar;

    }
}
