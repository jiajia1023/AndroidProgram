/*
 * yidingliu.com Inc. * Copyright (c) 2016 All Rights Reserved.
 */

package com.mylibrary.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * 系统组件动作管理
 *
 * @author Chris zou
 * @Date 16/9/29
 * @modifyInfo1 chriszou-16/9/29
 * @modifyContent
 */
public class SystemActionManager {

    public static final int PHOTO_ALBUM=0x1;
    public static final int CAMERA_CODE=0x2;

    /**
     * 调用系统拨号
     * @param context
     * @param telNumber 号码
     */
    public static void startActionCall ( Context context,String telNumber ){
        context.startActivity ( actionCall ( telNumber ) );
    }
    public static Intent actionCall(String telNumber){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DIAL");
        intent.setData ( Uri.parse ( "tel:"+telNumber ) );
        return intent;
    }

    /**
     *  调用系统相机
     * @param activity
     * @param savePath 保存路径
     */
    public static void startActionCamera(Activity activity, String savePath){
        activity.startActivityForResult ( actionCamera ( savePath ) ,CAMERA_CODE);
    }

    public static Intent actionCamera(String savePath){
        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
        Uri    uri    = Uri.fromFile ( new File ( savePath) );
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    /**
     *  调用系统相册
     * @param activity
     */
    public static void startActionAlbum(Activity activity){
        activity.startActivityForResult(actionAlbum(), PHOTO_ALBUM);
    }

    public static Intent actionAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }

    public static void startActionBrowser(Activity activity,String url){
        activity.startActivity ( actionBrowser(url) );
    }
    public static Intent actionBrowser(String url){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        return intent;
    }

}
