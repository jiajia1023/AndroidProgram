package com.mylibrary.utils;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.mylibrary.manager.Log;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by work on 2017/3/28.
 * @from http://www.cnblogs.com/niray/p/5153530.html
 * @author chris zou
 * @mail chrisSpringSmell@gmail.com
 */

public class MIUIFlymeUtils {

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    /*
    * 只支持MIUI V6
    * @param context
    * @param type 0--只需要状态栏透明 1-状态栏透明且黑色字体 2-清除黑色字体
    */
    public static void setStatusBarTextColor(Activity context) {
        int type = 1;
        if (!isMIUI()) {
            Log.d("ActionBar", "isMiUIV6:" + false);
            return;
        }
        Log.d("ActionBar", "isMiUIV6:" + true);
        Window window = context.getWindow();
        Class clazz = window.getClass();
        try {
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);
            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (type == 0) {
                extraFlagField.invoke(window, tranceFlag, tranceFlag);//只需要状态栏透明
            } else if (type == 1) {
                extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);//状态栏透明且黑色字体
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
            }
        } catch (Exception e) {

        }
    }

    //魅族:
    public static boolean setStatusBarDarkIcon(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                Log.e("MeiZu", "setStatusBarDarkIcon: failed");
            }
        }
        return result;
    }
    private static boolean isMiUIV6() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            String name = prop.getProperty(KEY_MIUI_VERSION_NAME, "");
            if ("V6".equals(name)) {
                return true;
            } else {
                return false;
            }
//            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
//                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
//                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }


    public static boolean isMIUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }


    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }


    }
}
