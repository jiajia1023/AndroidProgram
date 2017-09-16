package com.mylibrary.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mylibrary.R;
import com.mylibrary.manager.ImgManager;
import com.mylibrary.utils.ImageUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;


/**
 * Created by Administrator on 2016/7/7.
 */
public abstract class RecyclerViewBaseParentAdapter extends RecyclerView.Adapter<RecyclerViewBaseParentAdapter.BaseViewHolder> {

    public OnItemClickListener mOnItemClickListener;
    public OnItemLongClickListener mOnItemLongClickListener;
    public OnItemActionListener mOnItemActionListener;
    public OnClickListener mOnClickListener;
    public int[] clickResId;
    public Context mContext;

    /**
     * 是否引用基础布局
     *
     * @return
     */
    public boolean isUsingBaseLayout() {
        return false;
    }

    public boolean isItemSwipeEnable() {
        return false;
    }

    public boolean isItemDragEnable() {
        return false;
    }

    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemActionListener != null) {
            mOnItemActionListener.onItemDragEnd(viewHolder, viewHolder.getAdapterPosition());
        }
    }

    @Override
    public RecyclerViewBaseParentAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        BaseViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(onResultLayoutResId(), parent, false);

        if (isUsingBaseLayout()) {
            LinearLayout parentView = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item_recycler, parent, false);
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
            parentView.setBackgroundResource(typedValue.resourceId);
            parentView.addView(view, 0);
            viewHolder = new BaseViewHolder(parentView);
        } else {
            viewHolder = new BaseViewHolder(view);
        }

        AutoUtils.auto(viewHolder.rootView);
        return viewHolder;
    }

    public void setLine(BaseViewHolder holder, boolean isVisible) {
        if (isUsingBaseLayout() && isVisible) {
            if (isVisible) {
                holder.getView(R.id.baseItemLine).setVisibility(View.VISIBLE);
            } else {
                holder.getView(R.id.baseItemLine).setVisibility(View.GONE);
            }
        }
    }

    public void setLine(BaseViewHolder holder, @ColorRes int colorRes) {
        if (isUsingBaseLayout()) {
            holder.getView(R.id.baseItemLine).setBackgroundResource(colorRes);
        }
    }

    public Context getContext() {

        return mContext;
    }

    /**
     * 布局文件
     *
     * @return
     */
    public abstract int onResultLayoutResId();

    public void setOnClickListener(OnClickListener onClickListener, int... params) {
        this.clickResId = params;
        this.mOnClickListener = onClickListener;
    }

    public void setOnItemClickListener(BaseRecyclerViewAdapter.OnItemClickListener onItemClickListener) {

        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(BaseRecyclerViewAdapter.OnItemLongClickListener onItemLongClickListener) {

        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemActionListener(OnItemActionListener onItemActionListener) {
        this.mOnItemActionListener = onItemActionListener;
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> mViews;
        public View rootView;

        public BaseViewHolder(View rootView) {

            super(rootView);
            this.rootView = rootView;
            this.mViews = new SparseArray<>();
        }

        public <T extends View> T getView(int id) {

            View view = mViews.get(id);
            if (view == null) {
                view = itemView.findViewById(id);
                mViews.put(id, view);
            }
            return (T) view;
        }

        public BaseViewHolder setText(int id, CharSequence content) {

            return setText(id, content, null);
        }

        public BaseViewHolder setText(int id, CharSequence content, View.OnClickListener onClickListener) {

            TextView textView = getView(id);
            textView.setText(content);
            if (onClickListener != null) {
                textView.setOnClickListener(onClickListener);
            }

            return this;
        }

        public BaseViewHolder setImgRes(int id, int iconId) {

            return setImg(id, "", iconId, null);
        }

        public BaseViewHolder setImgRes(int id, int iconId, View.OnClickListener onClickListener) {

            return setImg(id, "", iconId, onClickListener);
        }

        public BaseViewHolder setImgUrl(int id, CharSequence url) {

            return setImg(id, url, 0, null);
        }

        public BaseViewHolder setImgUrl(int id, CharSequence url, View.OnClickListener onClickListener) {

            return setImg(id, url, 0, onClickListener);
        }

        public BaseViewHolder setOnClickListener(int id, View.OnClickListener onClickListener) {

            getView(id).setOnClickListener(onClickListener);
            return this;
        }

        public BaseViewHolder setBackgroundResource(int id, int bgResId) {

            getView(id).setBackgroundResource(bgResId);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public BaseViewHolder setBackground(int id, Drawable background) {

            getView(id).setBackground(background);
            return this;
        }

        public BaseViewHolder setBackgroundColor(int id, int background) {

            getView(id).setBackgroundColor(background);
            return this;
        }

        private BaseViewHolder setImg(int id, CharSequence url, int iconId, View.OnClickListener onClickListener) {

            ImageView img = getView(id);
            if (!TextUtils.isEmpty(url)) {
                ImgManager.loadCircleImage(rootView.getContext(), url + "", R.mipmap.ic_default_erro_circle, R.mipmap.ic_empty_photo, img);
            }
            if (iconId != 0) {
                ImgManager.loadImage(rootView.getContext(), iconId, img);
                img.setImageResource(iconId);
            }
            if (onClickListener != null) {
                img.setOnClickListener(onClickListener);
            }
            return this;
        }
    }

    public class BaseViewHolderModel {
        private Class aClass;
        private int viewType;

        public BaseViewHolderModel(Class aClass, int viewType) {
            this.aClass = aClass;
            this.viewType = viewType;
        }
    }

    public interface OnItemClickListener {

        void onClick(View v, int position);
    }

    public interface OnItemLongClickListener {

        boolean onLongClick(View v, int position);
    }

    public interface OnItemActionListener {

        void onItemSwipeEnd(RecyclerView.ViewHolder viewHolder, int position);

        void onItemSwiping(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive);

        void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int position);
    }

    public interface OnClickListener {
        void onClicked(View view, BaseViewHolder holder, int position);
    }
}
