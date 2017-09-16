package com.mylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.Process;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * Created by Administrator on 2016/5/24.
 */
public class CommonUtils {

    /**
     * 获取SHA1码
     * @param context
     * @return
     */
    public static String getSHA1(Context context){
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
     * 获得字体宽
     */
    public int getFontWidth(Paint paint, String str) {
        if (str == null || str.equals(""))
            return 0;
        Rect rect = new Rect();
        int length = str.length();
        paint.getTextBounds(str, 0, length, rect);
        return rect.width();
    }

    /**
     * 获得字体高度
     */
    public static int getFontHeight(Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds("正", 0, 1, rect);
        return rect.height();
    }

    /**
     * 获得字体高度
     */
    public static int getFontHeight(Paint paint,String txt) {
        Rect rect = new Rect();
        paint.getTextBounds(txt, 0, 1, rect);
        return rect.height();
    }

    /**
     * @param listView
     * @scene 使用场景：当与其他父控件冲突不能正常计算大小时。example:ScrollView嵌套ListView
     * @description 设置指定listView的高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null || listAdapter.getCount() <= 0) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

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

    public static boolean installAPK(Context context,File file) {
        if(file.exists()) {
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
        return Environment.getExternalStorageState ().equals ( Environment.MEDIA_MOUNTED );
    }

    /**
     * 获取两点间距离,单位：px
     *
     * @param x1 第一个点
     * @param x2 第二个点
     * @return
     * @formula |AB| = sqrt((X1-X2)^2 + (Y1-Y2)^2)
     */
    public static float getDistance ( Point x1, Point x2 ) {
        return getDistance(x1.x,x1.y,x2.x,x2.y);
    }

    public static float getDistance(float x1,float y1,float x2,float y2){

        return getDistance(Math.abs(x1 - x2),Math.abs(y1 - y2));
    }

    /**
     * The distance between two points
     *
     * @param x |x1-x2|
     * @param y |y1-y2|
     * @return
     */
    public static float getDistance(float x, float y) {
        float distance = ( float ) Math.sqrt ( x * x + y * y );
        return distance;
    }

}
