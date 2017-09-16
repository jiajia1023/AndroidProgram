package com.mylibrary.activities.selectorimg;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mylibrary.MyApplication;
import com.mylibrary.R;
import com.mylibrary.utils.DevicesUtils;
import com.zhy.autolayout.AutoLayoutActivity;

/**
 * 对外开放的类，请继承该类
 * 使用该类须隐藏title，主题城需使用兼容的风格，详情请查看Demo的mainifests
 * Created by chris on 2016/6/8.
 *
 * @author chris Zou
 * @date 2016/6/8.
 */
public abstract class ThemeActivity extends AutoLayoutActivity {

    public LinearLayout baseLayout;
    public Toolbar baseToolbar;
    public FrameLayout appContentContainer;
    public static final String TITLE = "title";
    public static final String ID = "id";
    public static final String DATA = "data";
    private View.OnClickListener backClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        initView();

        if (baseToolbar != null) {
            baseToolbar.setTitleTextColor(Color.WHITE);
        }

        init();
    }

    private void initView() {
        if (isUsingBaseLayout()) {
            setContentView(R.layout.app_content);
            appContentContainer = (FrameLayout) findViewById(R.id.appContent);
            baseLayout = (LinearLayout) findViewById(R.id.parentGroupBorder);
            LayoutInflater.from(this).inflate(onResultLayoutResId(), appContentContainer);
        } else {
            setContentView(onResultLayoutResId());
        }
        if (getToolbar() != null) {
            //替换Action
            baseToolbar = getToolbar();
            setSupportActionBar(baseToolbar);
            if (getIntent().hasExtra(TITLE)) {
                setTitle(getIntent().getStringExtra(TITLE));
            }
            if (!hasTitle()) {
                baseToolbar.setVisibility(View.GONE);
            }
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowHomeEnabled(true);
            }
        }
    }

    /**
     * 是否有title
     *
     * @return
     */
    public boolean isUsingBaseLayout() {
        return true;
    }

    public Toolbar getToolbar() {
        if (isUsingBaseLayout() && baseToolbar == null)
            baseToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        return baseToolbar;
    }

    public boolean hasTitle() {
        return true;
    }

    protected void setRightView(View view) {
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        baseToolbar.addView(view, layoutParams);
    }

    protected void setRightView(@DrawableRes int icon, View.OnClickListener onClickListener) {
        ImageView imageView = new ImageView(this, null, R.attr.toolbarNavigationButtonStyle);
        imageView.setImageResource(icon);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        layoutParams.rightMargin = getResources().getDimensionPixelOffset(R.dimen.marginRight);
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        imageView.setBackgroundResource(typedValue.resourceId);
        baseToolbar.addView(imageView, layoutParams);
        imageView.setOnClickListener(onClickListener);
    }

    protected void setBackValid() {
        setBackValid(0, null);
    }

    protected void setBackValid(int backIcon) {
        setBackValid(backIcon, null);
    }

    protected void setBackValid(View.OnClickListener onClickListener) {
        setBackValid(0, onClickListener);
    }

    protected void setBackValid(int backIcon, View.OnClickListener onClickListener) {
        if (backIcon == 0) {
            baseToolbar.setNavigationIcon(R.mipmap.ic_back_white);
        } else if (backIcon != -1) {
            baseToolbar.setNavigationIcon(backIcon);
        } else if (backIcon == -1) {
            baseToolbar.setNavigationIcon(0);
        }
        if (onClickListener == null) {
            onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            };
        }
        backClickListener = onClickListener;
        baseToolbar.setNavigationOnClickListener(backClickListener);
    }

    protected void showToast(CharSequence content) {
        showToast(content, Gravity.BOTTOM, Toast.LENGTH_SHORT);
    }

    protected void showToast(CharSequence content, int gravity) {
        showToast(content, gravity, Toast.LENGTH_SHORT);
    }

    protected void showToast(CharSequence content, int gravity, int duration) {
        Toast toast = Toast.makeText(this, content, duration);
        toast.setGravity(gravity, 0, 0);
        DevicesUtils.closeSoftInput(this);
        toast.show();
    }

    protected void showSnackbar(CharSequence content) {

        showSnackbar(content, null, null);
    }

    protected void showSnackbar(View view, CharSequence content) {

        showSnackbar(view, content, null, null);
    }

    protected void showSnackbar(CharSequence content, View.OnClickListener onClickListener) {

        showSnackbar(content, getString(R.string.sure), onClickListener);
    }

    protected void showSnackbar(View view, CharSequence content, View.OnClickListener onClickListener) {

        showSnackbar(view, content, getString(R.string.sure), onClickListener);
    }

    protected void showSnackbar(CharSequence content, CharSequence actionTxt, View.OnClickListener onClickListener) {
        showSnackbar(null, content, actionTxt, onClickListener);
    }

    protected void showSnackbar(View view, CharSequence content, CharSequence actionTxt, View.OnClickListener onClickListener) {
        DevicesUtils.closeSoftInput(this);
        if (view == null) {
            view = getWindow().getDecorView();
        }
        Snackbar.make(view, content, Snackbar.LENGTH_SHORT).setAction(actionTxt, onClickListener).setActionTextColor(getResources().getColor(R.color.colorBlueShallow)).show();
    }

    /**
     * 初始化操作
     */
    public abstract void init();

    /**
     * 返回资源文件ID
     *
     * @return
     */
    public abstract
    @LayoutRes
    int onResultLayoutResId();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    /**
     * 这只是一个简易的跳转方法，参数多时建议重写此方法
     *
     * @param activity
     * @param activityClass
     * @param title
     */
    public static void startAction(Activity activity, Class activityClass, String title) {

        Intent intent = new Intent(activity, activityClass);
        intent.putExtra(TITLE, title);
        activity.startActivity(intent);
    }

}
