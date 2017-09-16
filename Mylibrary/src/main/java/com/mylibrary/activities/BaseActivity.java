package com.mylibrary.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mylibrary.MyApplication;
import com.mylibrary.R;
import com.mylibrary.dialog.DialogFactory;
import com.mylibrary.dialog.IsOkDialog;
import com.mylibrary.dialog.LoadingDialog;
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
public abstract class BaseActivity extends AutoLayoutActivity {

    protected InputMethodManager manager;
    public LinearLayout baseLayout;
    public Toolbar baseToolbar;
    public FrameLayout appContentFl;
    public static final String TITLE = "title";
    public static final String DATA = "data";
    private boolean hasTitle = true;
    private int backIcon = 0;//默认的返回按钮--必须有title的才能有
    private View.OnClickListener backClickListener;
    protected IsOkDialog isOkDialog;
    protected LoadingDialog loadDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        /**
         * 基类生命周期会被重写，只能放在此处实例化最合适，后期应当优化
         */
        isOkDialog = (IsOkDialog) new DialogFactory().getDialog(DialogFactory.DialogType.IS_OK);
        loadDialog = (LoadingDialog) new DialogFactory().getDialog(DialogFactory.DialogType.LOADING);

        MyApplication.getInstance().addActivity(this);
        hasTitle = hasTitle();
        setBaseView();
        onInitLayout();
        onInit();
        onBindData();

        if (getIntent().hasExtra(TITLE)) {
            setTitle(getIntent().getStringExtra(TITLE));
        }
        if (baseToolbar != null) {
            baseToolbar.setTitleTextColor(Color.WHITE);
        }
    }

    /**
     * 修改返回按钮
     *
     * @param backIcon
     */
    public void setBackIcon(int backIcon, View.OnClickListener onClickListener) {
        this.backIcon = backIcon;
        this.backClickListener = onClickListener;
    }

    /**
     * 是否有title
     *
     * @return
     */
    public boolean hasTitle() {
        return true;
    }

    /**
     * 初始化关于布局的
     */
    public void onInitLayout() {
        if (hasTitle()) {
            setBackValid();
        }
    }

    private void setBaseView() {
        if (hasTitle()) {
            setContentView(R.layout.app_content);
            appContentFl = (FrameLayout) findViewById(R.id.appContent);
            baseLayout = (LinearLayout) findViewById(R.id.parentGroupBorder);
            //替换Action
            baseToolbar = (Toolbar) findViewById(com.mylibrary.R.id.mainToolbar);
            setSupportActionBar(baseToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowHomeEnabled(true);
            }
            LayoutInflater.from(this).inflate(onResultLayoutResId(), appContentFl);
        } else {
            setContentView(onResultLayoutResId());
        }
    }

    protected void setRightView(View view) {
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        baseToolbar.addView(view, layoutParams);
    }

    protected void setRightView(@DrawableRes int icon, View.OnClickListener onClickListener) {
        AppCompatImageButton imageView = new AppCompatImageButton(this, null, R.attr.toolbarNavigationButtonStyle);
        imageView.setImageResource(icon);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        layoutParams.rightMargin = getResources().getDimensionPixelOffset(com.mylibrary.R.dimen.marginRight);
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

    protected void showTitleBar(boolean isVisible) {
        if (isVisible) {
            baseToolbar.setVisibility(View.VISIBLE);
        } else {
            baseToolbar.setVisibility(View.GONE);
        }
    }

    /**
     * 设置居中的标题
     *
     * @param title
     */
    protected void setMiddleTitle(String title) {

        baseToolbar.setTitle(title);
    }

    protected void showToast(CharSequence content) {
        DevicesUtils.closeSoftInput(this);
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    protected void showSnackbar(CharSequence content) {

        showSnackbar(content, null, null);
    }

    protected void showSnackbar(View view,CharSequence content) {

        showSnackbar(view,content, null, null);
    }

    protected void showSnackbar(CharSequence content, View.OnClickListener onClickListener) {

        showSnackbar(content, getString(R.string.sure), onClickListener);
    }

    protected void showSnackbar(View view,CharSequence content, View.OnClickListener onClickListener) {

        showSnackbar(view,content, getString(R.string.sure), onClickListener);
    }

    protected void showSnackbar(CharSequence content, CharSequence actionTxt, View.OnClickListener onClickListener) {
        showSnackbar(null,content,actionTxt,onClickListener);
    }

    protected void showSnackbar(View view,CharSequence content, CharSequence actionTxt, View.OnClickListener onClickListener) {
        DevicesUtils.closeSoftInput(this);
        if(view==null){
            view=getWindow().getDecorView();
        }
        Snackbar.make(view, content, Snackbar.LENGTH_SHORT).setAction(actionTxt, onClickListener).setActionTextColor(getResources().getColor(R.color.colorBlueShallow)).show();
    }

    /**
     * 初始化操作
     */
    public void onInit() {
    }

    /**
     * 返回资源文件ID
     *
     * @return
     */
    public abstract @LayoutRes int onResultLayoutResId();

    /**
     * 绑定数据
     */
    public abstract void onBindData();

    @CallSuper
    public void onResumeBindData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        onResumeBindData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
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
