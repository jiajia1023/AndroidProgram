package com.mylibrary.dialog;

import android.app.Activity;
import android.content.Context;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mylibrary.R;

/**
 * Created by work on 2017/3/21.
 *
 * @author chris zou
 * @mail chrisSpringSmell@gmail.com
 */

public class IsOkDialog implements IDialog, View.OnClickListener {

    private Context context;
    private AlertDialog mIsOkDialog;
    private DialogViewHolder holder;
    private OnClickListener mOnClickListener;

    /**
     * 没有标题与按钮（隐藏），只有内容，点击框外不消失
     *
     * @param context
     * @param msgContent 内容
     */
    public void alertIsOkDialog(Context context, CharSequence msgContent) {

        this.alertIsOkDialog(context, msgContent, "", "确定", null);
    }

    /**
     * 有标题内容与按钮，按钮文字默认左边取消、右边确定，点击框外不消失
     *
     * @param context
     * @param msgTitle
     * @param msgContent
     */
    public void alertIsOkDialog(Context context, CharSequence msgTitle, CharSequence msgContent) {

        this.alertIsOkDialog(context, msgTitle, msgContent, null, "确定", null);
    }

    /**
     * 有标题内容与按钮，按钮文字默认左边取消、右边确定，点击框外不消失
     *
     * @param context
     * @param msgTitle
     * @param msgContent
     */
    public void alertIsOkDialog(Context context, CharSequence msgTitle, CharSequence msgContent, CharSequence btnHint) {

        this.alertIsOkDialog(context, msgTitle, msgContent, null, btnHint, null);
    }

    /**
     * 没有标题，有按钮与内容，点击框外不消失
     *
     * @param context
     * @param msgContent
     * @param onClickListener
     */
    public void alertIsOkDialog(Context context, CharSequence msgContent, OnClickListener onClickListener) {

        this.alertIsOkDialog(context, msgContent, "取消", "确定", onClickListener);
    }


    /**
     * 有标题内容与按钮，按钮文字默认左边取消、右边确定，点击框外不消失
     *
     * @param context
     * @param msgTitle
     * @param msgContent
     * @param onClickListener
     */
    public void alertIsOkDialog(Context context, CharSequence msgTitle, CharSequence msgContent, OnClickListener onClickListener) {

        this.alertIsOkDialog(context, msgTitle, msgContent, "取消", "确定", onClickListener);
    }

    /**
     * 除了标题，其他全都有，按钮自定义，点击框外不消失
     *
     * @param context
     * @param msgContent
     * @param leftBtnTxt
     * @param rightBtnTxt
     * @param onClickListener
     */
    public void alertIsOkDialog(Context context, CharSequence msgContent, CharSequence leftBtnTxt, CharSequence rightBtnTxt, OnClickListener onClickListener) {

        this.alertIsOkDialog(context, "", msgContent, leftBtnTxt, rightBtnTxt, onClickListener);
    }

    /**
     * 啥都有，点击屏幕之外不消息
     *
     * @param context
     * @param msgTitle
     * @param msgContent
     * @param leftBtnTxt
     * @param rightBtnTxt
     * @param onClickListener
     */
    public void alertIsOkDialog(Context context, CharSequence msgTitle, CharSequence msgContent, CharSequence leftBtnTxt, CharSequence rightBtnTxt, OnClickListener onClickListener) {

        this.alertIsOkDialog(context, false, msgTitle, msgContent, leftBtnTxt, rightBtnTxt, onClickListener);
    }

    /**
     * 啥都有，点击框外自定义
     *
     * @param context
     * @param isHandle
     * @param msgTitle
     * @param msgContent
     * @param leftBtnTxt
     * @param rightBtnTxt
     * @param onClickListener
     */
    public void alertIsOkDialog(Context context, boolean isHandle, CharSequence msgTitle, CharSequence msgContent, CharSequence leftBtnTxt, CharSequence rightBtnTxt, OnClickListener onClickListener) {
        this.context = context;
        this.mOnClickListener = onClickListener;
        if (mIsOkDialog == null) {
            initDialog(context);
        }

        if (!TextUtils.isEmpty(msgTitle)) {
            holder.msgTitleTv.setVisibility(View.VISIBLE);
            holder.msgTitleTv.setText(msgTitle);
        } else {
            holder.msgTitleTv.setVisibility(View.GONE);
        }

        holder.msgContentTv.setText(msgContent);

        if (TextUtils.isEmpty(rightBtnTxt)) {
            holder.msgOkBtn.setVisibility(View.GONE);
        } else {
            holder.msgOkBtn.setVisibility(View.VISIBLE);
            holder.msgOkBtn.setText(rightBtnTxt);
        }

        if (TextUtils.isEmpty(leftBtnTxt)) {
            holder.msgCancelBtn.setVisibility(View.GONE);
        } else {
            holder.msgCancelBtn.setVisibility(View.VISIBLE);
            holder.msgCancelBtn.setText(leftBtnTxt);
        }

        mIsOkDialog.setCanceledOnTouchOutside(isHandle);
        mIsOkDialog.show();
    }

    @Override
    public void onClick(View v) {
        mIsOkDialog.dismiss();
        if (mOnClickListener == null) {
            return;
        }
        int tag = (int) v.getTag();
        switch (tag) {
            case 0x1://ok
                mOnClickListener.onClicked(v, true);
                break;
            case 0x2://false
                mOnClickListener.onClicked(v, false);
                break;
        }
    }

    @Override
    public void show(Activity activity) {
        if (mIsOkDialog != null && !mIsOkDialog.isShowing()) {
            mIsOkDialog.show();
        }
    }

    @Override
    public void dismiss() {
        if (mIsOkDialog != null && mIsOkDialog.isShowing()) {
            mIsOkDialog.dismiss();
        }
    }

    @Override
    public void initDialog(Activity context) {
        initDialog(context.getApplicationContext());
    }

    public void initDialog(Context context) {
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_is_ok, null);
        builder.setView(view);
        mIsOkDialog = builder.create();
        holder = new DialogViewHolder(view);
        holder.msgOkBtn.setTag(0x1);
        holder.msgOkBtn.setOnClickListener(this);
        holder.msgCancelBtn.setTag(0x2);
        holder.msgCancelBtn.setOnClickListener(this);
    }

    @Override
    public AlertDialog getDialog() {
        return mIsOkDialog;
    }

    public class DialogViewHolder {

        public TextView msgTitleTv;
        public TextView msgContentTv;
        public Button msgOkBtn;
        public Button msgCancelBtn;

        public DialogViewHolder(View view) {

            msgTitleTv = (TextView) view.findViewById(R.id.msgTitleTv);
            msgContentTv = (TextView) view.findViewById(R.id.msgContentTv);
            msgCancelBtn = (Button) view.findViewById(R.id.msgCancelBtn);
            msgOkBtn = (Button) view.findViewById(R.id.msgOkBtn);
        }
    }

    public interface OnClickListener {

        void onClicked(View view, boolean isRight);
    }
}
