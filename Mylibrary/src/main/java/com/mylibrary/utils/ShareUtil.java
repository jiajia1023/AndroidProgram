package com.mylibrary.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.mylibrary.R;
import com.mylibrary.adapter.ShareGridAdapter;
import com.mylibrary.info.ShareInfo;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;

/**
 * author:jjj
 * time:2017/3/31 11:40
 * TODO:分享工具类
 */
public class ShareUtil {
    private Dialog shareDialog;
    private Activity mContext;


    public ShareUtil(Activity cActivity) {
        this.mContext = cActivity;
    }

    /**
     * 设置数据
     *
     * @param title    标题
     * @param content  内容
     * @param url      目标链接
     * @param shareImg 分享的资源图片
     */
    public void onShare(String title, String content, String url, int shareImg) {
        UMImage mUmImage = new UMImage(mContext, shareImg);
        UMWeb umWeb = new UMWeb(url);
        umWeb.setTitle(title);//标题
        umWeb.setThumb(mUmImage);  //缩略图
        umWeb.setDescription(content);//描述
        showShareDialog(title, content, umWeb, null);
    }


    /**
     * 设置数据
     *
     * @param title   标题
     * @param content 内容
     * @param url     目标链接
     * @param imgPath 图片链接
     */
    public void onShare(String title, String content, String url, String imgPath) {
        UMImage mUmImage = new UMImage(mContext, imgPath);
        UMWeb umWeb = new UMWeb(url);
        umWeb.setTitle(title);//标题
        umWeb.setThumb(mUmImage);  //缩略图
        umWeb.setDescription(content);//描述
        showShareDialog(title, content, umWeb, null);
    }

    /**
     * 设置数据
     *
     * @param title   标题
     * @param content 内容
     * @param img     资源图片
     */
    public void onShare(String title, String content, int img) {
        UMImage mUmImage = new UMImage(mContext, img);
        showShareDialog(title, content, null, mUmImage);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mContext, "您取消了分享", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 打开分享界面进行分享
     */
    private void showShareDialog(final String title, final String content, final UMWeb umWeb, final UMImage mUmImage) {
        PermissionUtils.requestPermission(mContext, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, (PermissionUtils.PermissionResultListener) mContext);
        PermissionUtils.requestPermission(mContext, PermissionUtils.CODE_READ_EXTERNAL_STORAGE, (PermissionUtils.PermissionResultListener) mContext);

        if (shareDialog == null) {
            List<ShareInfo> infos = new ArrayList<>();
            ShareInfo qqInfo = new ShareInfo("QQ", R.mipmap.ic_share_qq, SHARE_MEDIA.QQ);
            infos.add(qqInfo);
            ShareInfo qZoneInfo = new ShareInfo("QQ空间", R.mipmap.ic_share_qq_zore, SHARE_MEDIA.QZONE);
            infos.add(qZoneInfo);
            ShareInfo weixin = new ShareInfo("微信好友", R.mipmap.ic_share_wx_friend, SHARE_MEDIA.WEIXIN);
            infos.add(weixin);
            ShareInfo wCircle = new ShareInfo("朋友圈", R.mipmap.ic_share_wx_center, SHARE_MEDIA.WEIXIN_CIRCLE);
            infos.add(wCircle);

            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_share_posts, null);
            GridView gv = (GridView) view.findViewById(R.id.gridview);
            final ShareGridAdapter sAdapter = new ShareGridAdapter(mContext, infos);
            gv.setAdapter(sAdapter);
            gv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if (shareDialog != null && shareDialog.isShowing()) {
                        shareDialog.dismiss();
                    }
                    if (umWeb != null) {
                        new ShareAction(mContext).setPlatform(sAdapter.getItem(position).getShare_media())
                                .withMedia(umWeb)
                                .setCallback(umShareListener)
                                .share();
                    } else {
                        new ShareAction(mContext).setPlatform(sAdapter.getItem(position).getShare_media())
                                .withMedia(mUmImage)
                                .withText(content)
                                .withSubject(title)
                                .setCallback(umShareListener)
                                .share();
                    }
                }
            });
            view.findViewById(R.id.tv_cancel).setOnClickListener(
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            shareDialog.dismiss();
                        }
                    });
            shareDialog = DialogUtil.getMenuDialog(mContext, view);
            shareDialog.show();
        } else {
            shareDialog.show();
        }
    }

    /**
     * 分享需要进行回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(mContext).onActivityResult(requestCode, resultCode, data);
    }

}
