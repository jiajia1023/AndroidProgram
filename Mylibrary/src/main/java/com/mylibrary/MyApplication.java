package com.mylibrary;

import android.app.Activity;
import android.os.Build;
import android.support.multidex.MultiDexApplication;

import com.mylibrary.common.AppConfig;
import com.mylibrary.manager.Log;
import com.mylibrary.utils.DevicesUtils;
import com.mylibrary.utils.ScreenUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.zhy.autolayout.config.AutoLayoutConifg;

import org.litepal.LitePal;

import java.util.LinkedList;
import java.util.List;

/**
 * author:jjj
 * time: 2017/3/8 9:56
 * TODO:
 */

public class MyApplication extends MultiDexApplication {
    private static MyApplication instance;
    public static int width;
    public static int height;
    public static int maxMemory;
    public static String phoneModel;
    public static String phoneRelease;
    public static String appVersion;
    public static final String BASE_KEY_NAME = "HappyTreeKeyName";

    public static final String OPPO = "OPPO";

    private List<Activity> activityList = new LinkedList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.setDebug(true);
        initPlatfrom();
        CrashReport.initCrashReport(getApplicationContext(), "ec1166c918", true);

          /* 数据库初始化 */
        LitePal.initialize(this);
        /* 自动适配 */
        AutoLayoutConifg.getInstance().useDeviceSize();
        instance = this;
        initDevices();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * 初始化友盟里面的东西
     */
    private void initPlatfrom() {
        UMShareAPI.get(this);
        PlatformConfig.setWeixin(AppConfig.WX_APPID, AppConfig.WX_APPSECRET);
        PlatformConfig.setQQZone(AppConfig.QQ_APPID, AppConfig.QQ_APPKEY);
//        PlatformConfig.setSinaWeibo(AppConfig.SINA_APPID, AppConfig.SINA_APPKEY, AppConfig.SINA_NETURL);
    }

    public void addActivity(Activity activity) {

        if (activity != null) {
            this.activityList.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            this.activityList.remove(activity);
        }
//        if (activity != null) {
//            for (Activity mActivity : this.activityList) {
//                if (mActivity == activity) {
//                    this.activityList.remove(activity);
//                    if (!mActivity.isFinishing()) {
//                        mActivity.finish();
//                    }
//                    break;
//                }
//            }
//        }
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void clearActivity() {

        for (Activity mActivity : activityList) {
            if (mActivity != null) {
                if (!mActivity.isFinishing()) {
                    mActivity.finish();
                }
            }
        }
    }

    public void exit() {
        try {
            for (Activity activity : activityList) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    private void initDevices() {
        width = ScreenUtil.getScreenWidth(this);
        height = ScreenUtil.getScreenHeight(this);
        maxMemory = DevicesUtils.getMaxMemory();
        phoneModel = DevicesUtils.getDeviceModel();
        phoneRelease = DevicesUtils.getDeviceLevel();

        inputDeviceInfo();
    }

    private void inputDeviceInfo() {
        if (!Log.isDebug()) {
            return;
        }
        Log.v(getClass().getSimpleName(), "--------------------------------设备信息 Input Start----------------------------");
        Log.v(getClass().getSimpleName(), "----------------------------width:" + width + " height:" + height);
        Log.v(getClass().getSimpleName(), "----------------------------maxMemory:" + maxMemory);
        Log.v(getClass().getSimpleName(), "----------------------------DPI:" + DevicesUtils.getScreenDensityDPI());
        Log.v(getClass().getSimpleName(), "----------------------------手机型号:" + phoneModel);
        Log.v(getClass().getSimpleName(), "----------------------------手机厂商:" + Build.MANUFACTURER);
        Log.v(getClass().getSimpleName(), "----------------------------level:" + DevicesUtils.getDeviceLevel());
        Log.v(getClass().getSimpleName(), "--------------------------------设备信息 Input End----------------------------");
    }
}
