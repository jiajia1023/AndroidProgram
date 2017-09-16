package com.mylibrary.zxing;

import android.graphics.Bitmap;

import com.google.zxing.LuminanceSource;

/**
 * author:jjj
 * time: 2017/6/27 11:38
 * TODO:二维码长按识别的
 */

public class RGBLuminanceSource extends LuminanceSource {

    private byte bitmapPixels[];

    public RGBLuminanceSource(Bitmap bitmap) {
        super(bitmap.getWidth(), bitmap.getHeight());

        // 首先，要取得该图片的像素数组内容
        int[] data = new int[bitmap.getWidth() * bitmap.getHeight()];
        this.bitmapPixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(data, 0, getWidth(), 0, 0, getWidth(), getHeight());

        // 将int数组转换为byte数组
        for (int i = 0; i < data.length; i++) {
            this.bitmapPixels[i] = (byte) data[i];
        }
    }

    @Override
    public byte[] getMatrix() {
        // 返回我们生成好的像素数据
        return bitmapPixels;
    }

    @Override
    public byte[] getRow(int y, byte[] row) {
        // 这里要得到指定行的像素数据
        System.arraycopy(bitmapPixels, y * getWidth(), row, 0, getWidth());
        return row;
    }
}
