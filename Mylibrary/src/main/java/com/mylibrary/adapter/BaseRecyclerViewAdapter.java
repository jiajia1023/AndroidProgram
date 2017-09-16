package com.mylibrary.adapter;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.mylibrary.R;
import com.mylibrary.manager.Log;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础适配器,继承便能快速生成adapter
 * 需要分割线，需要继承引用基类布局
 * Created by chris Zou on 2016/6/12.
 *
 * @author chris Zou
 * @Date 2016/6/12
 */
public abstract class BaseRecyclerViewAdapter<M> extends RecyclerViewBaseParentAdapter {//<M extends BaseRecyclerViewAdapter.BaseBean>
    private List<M> dataList = new ArrayList<>();

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public boolean isShowLine() {
        return false;
    }

    public boolean isShowEndLine() {
        return false;
    }

    /**
     * 上下左右的padding
     *
     * @return
     */
    public boolean isPadding() {
        return false;
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(v, position);
                }

            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    return mOnItemLongClickListener.onLongClick(v, position);
                }
            });
        }
        if (isShowLine()) {
            if (!isShowEndLine() && position == getDataList().size() - 1) {
                setLine(holder, false);
            } else {
                setLine(holder, true);
            }
        } else {
            setLine(holder, false);
        }
        if (mOnClickListener != null && clickResId != null && clickResId.length > 0) {
            for (int resId : clickResId) {
                try {
                    holder.setOnClickListener(resId, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnClickListener.onClicked(v, holder, position);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //解决item项最顶上与最底部贴边的问题
        if (isUsingBaseLayout() && isPadding()) {
            int padding = AutoUtils.getPercentHeightSize((int) getContext().getResources().getDimension(R.dimen.borderWidth));
            ((LinearLayout.LayoutParams) holder.getView(R.id.baseItemLine).getLayoutParams()).topMargin = padding / 2;
            holder.rootView.setPadding(padding, padding / 2, padding, 0);

            if (position == 0) {
                holder.rootView.setPadding(padding, padding, padding, padding);
            }
            if (position == getItemCount() - 1) {
                holder.rootView.setPadding(padding, padding, padding, padding);
            }
        } else {
            if (isSetMargin()) {
                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
                if (position == getItemCount() - 1) {
                    params.setMargins(getContext().getResources().getDimensionPixelOffset(R.dimen.margin_all), getContext().getResources().getDimensionPixelOffset(R.dimen.margin_all), getContext().getResources().getDimensionPixelOffset(R.dimen.margin_all), getContext().getResources().getDimensionPixelOffset(R.dimen.margin_all));
                } else {
                    params.setMargins(getContext().getResources().getDimensionPixelOffset(R.dimen.margin_all), getContext().getResources().getDimensionPixelOffset(R.dimen.margin_all), getContext().getResources().getDimensionPixelOffset(R.dimen.margin_all), 0);
                }
                holder.rootView.setLayoutParams(params);
            }
        }
        onBindData(holder, position, dataList.get(position));
    }

    /**
     * 是否动态设置margrin
     * 适用于不使用基本布局并且不设置padding
     *
     * @return
     */
    protected boolean isSetMargin() {
        return false;
    }

    public abstract void onBindData(BaseViewHolder holder, int position, M itemData);

    public void setDataList(List<M> dataList) {
        if (dataList == null) {//容错
            Log.e("数据为null", "The adapter not set null list");
            dataList = new ArrayList<>();
        }
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void remove(int position) {
        getDataList().remove(position);
    }

    public List<M> getDataList() {
        return dataList;
    }

    public M getItem(int position) {
        return getDataList().get(position);
    }

    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder source, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
//        Collections.swap(getDataList(), fromPos-1, toPos-1);
//        remove(source.getAdapterPosition());
//        notifyItemMoved(fromPos,toPos);
    }

    public void onItemSwiping(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (mOnItemActionListener != null) {
            mOnItemActionListener.onItemSwiping(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    public void onItemSwipeEnd(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        if (mOnItemActionListener != null) {
            mOnItemActionListener.onItemSwipeEnd(viewHolder, position);
        }
        remove(position - 1);
        notifyItemRemoved(position);
    }
}
