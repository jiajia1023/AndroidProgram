package com.mylibrary.info;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * author:jjj
 * time: 2017/3/31 10:43
 * TODO:分享
 */

public class ShareInfo {
    private String name;
    private int icon;
    private SHARE_MEDIA share_media;

    public ShareInfo(String name, int icon, SHARE_MEDIA share_media) {
        this.name = name;
        this.icon = icon;
        this.share_media = share_media;
    }

    public String getName() {
        return name;
    }

    public int getImg() {
        return icon;
    }

    public SHARE_MEDIA getShare_media() {
        return share_media;
    }
}
