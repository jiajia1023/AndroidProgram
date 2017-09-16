package com.mylibrary.info;

import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.Serializable;
import java.util.Map;

/**
 * author:jjj
 * time:2017/3/8 10:15
 * TODO:三方用户信息
 */
public class ThirdUser implements Serializable {
    private static final long serialVersionUID = 1L;
    public String accessToken;
    public String expiration;//过期时间
    public String refreshtoken;
    public String uid;//用户id
    public String name;//用户名
    public String iconurl;//头像
    public String gender;//性别
    //weixin
    public String openid;
    public String unionid;

    public int extType = 1;//  1-QQ 2-微信

    public ThirdUser(SHARE_MEDIA share_media, Map<String, String> map) {
        if (share_media == SHARE_MEDIA.QQ) {//QQ
            uid = map.get("uid");
            extType = 1;

        } else if (share_media == SHARE_MEDIA.WEIXIN) {//weixin
            openid = map.get("openid");
            unionid = map.get("unionid");
            refreshtoken = map.get("refreshtoken");
            extType = 2;

        } else if (share_media == SHARE_MEDIA.SINA) {//sina
            refreshtoken = map.get("refreshtoken");
            uid = map.get("uid");
        }

        accessToken = map.get("accessToken");
        iconurl = map.get("iconurl");
        expiration = map.get("expiration");
        name = map.get("name");
        gender = map.get("gender");
    }

}
