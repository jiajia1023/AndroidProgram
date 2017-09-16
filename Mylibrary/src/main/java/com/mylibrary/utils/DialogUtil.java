package com.mylibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mylibrary.R;

public class DialogUtil {

    /**
     * 底部弹出式
     *
     * @param context
     * @param view
     * @return
     */
    @SuppressLint("NewApi")
    public static Dialog getMenuDialog(Activity context, View view) {

        final Dialog dialog = new Dialog(context, R.style.MenuDialogStyle);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        dialog.setCanceledOnTouchOutside(true);

        int screenW = ScreenUtil.getScreenWH(context)[0];
        lp.width = screenW;
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.MenuDialogAnimation); // 添加动画
        return dialog;
    }

    /**
     * 底部弹出式
     *
     * @param context
     * @param view
     * @return
     */
    @SuppressLint("NewApi")
    public static Dialog getMenuDialog(Activity context, View view, @StyleRes int themeResId, int width, int height) {

        final Dialog dialog = new Dialog(context, themeResId);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        dialog.setCanceledOnTouchOutside(true);
        lp.width = width;
        lp.height = height;
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.MenuDialogAnimation); // 添加动画
        return dialog;
    }

    /**
     * 底部弹出式
     *
     * @param context
     * @param view
     * @return
     */
    @SuppressLint("NewApi")
    public static Dialog getMenuDialog(Activity context, View view, @StyleRes int themeResId) {

        final Dialog dialog = new Dialog(context, themeResId);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        dialog.setCanceledOnTouchOutside(true);

        int screenW = ScreenUtil.getScreenWH(context)[0];
        lp.width = screenW;
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.MenuDialogAnimation); // 添加动画
        return dialog;
    }

    /**
     * 底部弹出式,自定义高度
     *
     * @param context
     * @param view
     * @return
     */
    @SuppressLint("NewApi")
    public static Dialog getMenuDialog2(Activity context, View view, int height) {

        final Dialog dialog = new Dialog(context, R.style.MenuDialogStyle);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();

        lp.width = ScreenUtil.getScreenWH(context)[0];
        lp.height = height;
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.MenuDialogAnimation); // 添加动画
        return dialog;
    }

    /**
     * 自定义 other样式
     *
     * @param context
     * @param view
     * @return
     */
    public static Dialog getCenterDialog(Activity context, View view) {
        final Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();

        int screenW = ScreenUtil.getScreenWH(context)[0];
        lp.width = screenW;
        return dialog;
    }

    public static Dialog getCenterDialog2(Activity context, View view, int screenW, int screenH) {
        final Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = screenW;
        lp.height = screenH;
        return dialog;
    }

    public static void showDialog(Dialog dialog) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public static void dismissDialog(Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
