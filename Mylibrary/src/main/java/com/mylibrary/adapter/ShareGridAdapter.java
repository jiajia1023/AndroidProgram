package com.mylibrary.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mylibrary.R;
import com.mylibrary.info.ShareInfo;

import java.util.List;

/**
 * 分享
 */
public class ShareGridAdapter extends BaseAdapter {
    private Activity mContext;
    private List<ShareInfo> mList;

    public ShareGridAdapter(Activity context, List<ShareInfo> list) {
        mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ShareInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.item_share_item, null);
            holder.ivPic = (ImageView) view.findViewById(R.id.iv_share_icon);
            holder.tvShareName = (TextView) view.findViewById(R.id.tv_share_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ShareInfo info = getItem(i);
        if (info != null) {
            holder.tvShareName.setText(info.getName());
            holder.ivPic.setImageResource(info.getImg());
        }
        return view;
    }

    class ViewHolder {
        ImageView ivPic;
        TextView tvShareName;
    }

}
