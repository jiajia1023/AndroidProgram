/*
 * yidingliu.com Inc. * Copyright (c) 2016 All Rights Reserved.
 */

package com.mylibrary.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mylibrary.R;
import com.mylibrary.manager.ImgManager;
import com.mylibrary.utils.ImageUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * PhotoWall中GridView的适配器
 *
 * @author hanj
 */

public class PhotoWallAdapter extends BaseAdapter {

    private Context context;
    private int lastPosition;//上一次选择的位置
    private CheckBox lastCheckBox;
    private ArrayList<String> imagePathList = null;
    private ArrayList<String> alreadyList = null;

    //记录是否被选择
    private SparseBooleanArray selectionMap;
    private OnCheckChangeListener mOnCheckChangeListener;

    public PhotoWallAdapter(Context context, ArrayList<String> imagePathList) {

        this(context, imagePathList, new ArrayList<String>());
    }

    public PhotoWallAdapter(Context context, ArrayList<String> imagePathList, ArrayList<String> alreadyList) {

        this.context = context;
        this.imagePathList = imagePathList;
        this.alreadyList = alreadyList;
        selectionMap = new SparseBooleanArray();

    }

    @Override
    public int getCount() {

        return imagePathList == null ? 0 : imagePathList.size();
    }

    @Override
    public Object getItem(int position) {

        return imagePathList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_photo_wall_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position) != null) {
            String filePath = (String) getItem(position);
            for (String alreadyPath : alreadyList) {
                if (!TextUtils.isEmpty(alreadyPath) && alreadyPath.equalsIgnoreCase(filePath)) {
                    selectionMap.put(position, true);
                    break;
                }
            }
            //tag的key必须使用id的方式定义以保证唯一，否则会出现IllegalArgumentException.
            holder.checkBox.setTag(R.id.tag_first, position);
            holder.checkBox.setTag(R.id.tag_second, holder.imageView);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Integer position = (Integer) buttonView.getTag(R.id.tag_first);
                    ImageView image = (ImageView) buttonView.getTag(R.id.tag_second);
                    selectionMap.put(position, isChecked);
                    if (mOnCheckChangeListener != null) {
                        if (mOnCheckChangeListener.onCheckChange(position, isChecked, imagePathList.get(position)) && isChecked) {
                            image.setColorFilter(context.getResources().getColor(R.color.colorBlack90));
                            buttonView.setChecked(true);
                            lastPosition = position;
                            lastCheckBox = holder.checkBox;
                        } else {
                            image.setColorFilter(null);
                            buttonView.setChecked(false);
                        }
                    } else {
                        if (isChecked) {
                            image.setColorFilter(context.getResources().getColor(R.color.colorBlack90));
                            buttonView.setChecked(true);
                            lastPosition = position;
                            lastCheckBox = holder.checkBox;
                        } else {
                            image.setColorFilter(null);
                            buttonView.setChecked(false);
                        }
                    }
                }
            });
            holder.selectorContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.checkBox.setChecked(!holder.checkBox.isChecked());
                }
            });
            holder.checkBox.setChecked(selectionMap.get(position));
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImgManager.loadImage(parent.getContext(), new File(filePath), holder.imageView);
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            ImgManager.loadImage(context, R.mipmap.ic_camera, holder.imageView);
        }
        return convertView;
    }

    public int getLastPosition() {
        return lastPosition;
    }

    public void setLastIsSelect(boolean isChecked) {
        if (lastCheckBox != null)
            lastCheckBox.setChecked(isChecked);
    }

    private class ViewHolder {

        ImageView imageView;
        CheckBox checkBox;
        FrameLayout selectorContainer;

        public ViewHolder(View convertView) {

            imageView = (ImageView) convertView.findViewById(R.id.photo_wall_item_photo);
            checkBox = (CheckBox) convertView.findViewById(R.id.photo_wall_item_cb);
            selectorContainer = (FrameLayout) convertView.findViewById(R.id.selectorContainer);
        }
    }

    public SparseBooleanArray getSelectionMap() {

        return selectionMap;
    }

    public void setAlreadyList(ArrayList<String> alreadyList) {

        if (alreadyList == null) {
            alreadyList = new ArrayList<>();
        }
        this.alreadyList = alreadyList;
        notifyDataSetChanged();
    }

    public void clearSelectionMap() {

        selectionMap.clear();
        notifyDataSetChanged();
    }

    public void setOnCheckChangeListener(OnCheckChangeListener onCheckChangeListener) {

        mOnCheckChangeListener = onCheckChangeListener;
    }

    public interface OnCheckChangeListener {

        boolean onCheckChange(int position, boolean isChecked, String path);
    }
}
