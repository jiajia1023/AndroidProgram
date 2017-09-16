package com.mylibrary.widgets;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mylibrary.R;

/**
 * Created by Administrator on 2016/6/7.
 */
public class HintPopupWindow extends PopupWindow {

    private Context mContext;
    public int width, height;
    private View contentView;
    private TextView loadingHintTv;

    public HintPopupWindow(Context context) {
        mContext = context;
        contentView = LayoutInflater.from(context).inflate(R.layout.pw_hint, null);
        loadingHintTv = (TextView) contentView.findViewById(R.id.hintTv);
        setContentView(contentView);
        setBackgroundDrawable(new ColorDrawable(0x000000));
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(false);
        setAnimationStyle(R.style.AnimFadeInFadeOut);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.mandatoryDraw();
    }

    private void mandatoryDraw() {
        this.contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        //强制刷新后拿到宽高
        this.width = contentView.getMeasuredWidth();
        this.height = contentView.getMeasuredHeight();
    }


    public void showPopupWindow(View view) {
        showPopupWindow(view, "人家还没熟呢");
    }

    public void showPopupWindow(View view, String hint) {
        if (!isShowing()) {
//            showAsDropDown ( view, ScreenUtil.getScreenWidth( mContext )/2-width/2, ScreenUtil.getScreenHeight ( mContext )/2-height/2 );
            loadingHintTv.setText(hint);
            mandatoryDraw();
            int x = -width / 2 + view.getWidth() / 2;
            int y = -height - view.getMeasuredHeight();
            showAsDropDown(view, x, y);

        }
    }

    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }
}
