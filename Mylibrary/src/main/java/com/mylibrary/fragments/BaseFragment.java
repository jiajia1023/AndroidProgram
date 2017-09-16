package com.mylibrary.fragments;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mylibrary.R;
import com.mylibrary.dialog.DialogFactory;
import com.mylibrary.dialog.IsOkDialog;
import com.mylibrary.dialog.LoadingDialog;
import com.mylibrary.manager.SystemBarTintManager;
import com.zhy.autolayout.AutoLinearLayout;


/**
 * Created by chris Zou on 2016/6/12.
 *
 * @author chris Zou
 * @date 2016/6/12
 */
public abstract class BaseFragment extends Fragment {//<VH extends BaseParentViewHolder> 暂未有更好的方式，暂不支持ViewHolder的继承
    protected View baseLayout;
    private Snackbar mSnackbar;
    private Toolbar baseToolbar;
    protected SystemBarTintManager tintManager;
    protected IsOkDialog isOkDialog;
    protected LoadingDialog loadDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (baseLayout == null) {
            if (hasTitle()) {
                baseLayout = inflater.inflate(R.layout.app_content, container, false);
                inflater.inflate(onResultLayoutResId(), (AutoLinearLayout) baseLayout);
            } else {
                baseLayout = inflater.inflate(onResultLayoutResId(), container, false);
            }

        } else {
            ViewGroup parent = (ViewGroup) baseLayout.getParent();
            if (parent != null) {
                parent.removeView(baseLayout);
            }
        }
        if (hasTitle()){
            baseToolbar = (Toolbar) baseLayout.findViewById(R.id.mainToolbar);
        }
        isOkDialog = (IsOkDialog) new DialogFactory().getDialog(DialogFactory.DialogType.IS_OK);
        loadDialog = (LoadingDialog) new DialogFactory().getDialog(DialogFactory.DialogType.LOADING);
        onInit();
        onInitView(baseLayout);
        onInitLayout();

        return baseLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onBindData();
    }


    /**
     * 是否有title
     *
     * @return
     */
    public boolean hasTitle() {
        return false;
    }

    /**
     * 绑定view
     *
     * @param view
     */
    public void onInitView(View view) {

    }

    /**
     * 设置居中的标题
     *
     * @param title
     */
    protected void setMiddleTitle(String title) {
        baseToolbar.setTitle(title);
    }


    public void setStatusTintColor(int color) {

        tintManager.setStatusBarTintColor(color);
    }


    protected void setBackValid(boolean isVisible) {

        if (isVisible) {
            this.setBackValid(0, null);
        } else {
            this.setBackValid(-1, null);
        }

    }

    protected void setBackValid() {

        this.setBackValid(0, null);
    }

    protected void setBackValid(int icon) {

        this.setBackValid(icon, null);
    }

    protected void setBackValid(View.OnClickListener onClickListener) {

        setBackValid(0, onClickListener);
    }


    protected void setBackValid(int icon, View.OnClickListener onClickListener) {

        if (icon == 0) {
            baseToolbar.setNavigationIcon(R.mipmap.ic_back_white);
        } else if (icon != -1) {
            baseToolbar.setNavigationIcon(icon);
        }

        if (onClickListener == null) {
            onClickListener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    getActivity().finish();
                }
            };
        }
        baseToolbar.setNavigationOnClickListener(onClickListener);
    }

    public void setTitle(int titleResId) {

        this.setTitle(getString(titleResId));
    }

    public void setTitle(CharSequence title) {

        baseToolbar.setTitle(title);
    }

    public String getTitle() {
        if (hasTitle()) {
            return baseToolbar.getTitle().toString();
        } else {
            return "";
        }
    }

    public void setSubtitle(@StringRes int subtitle) {
        this.setSubtitle(getString(subtitle));
    }

    public void setSubtitle(CharSequence subtitle) {
        baseToolbar.setSubtitle(subtitle);
    }

    protected void setRightView(View view) {

        baseToolbar.addView(view);
    }


    protected void setRightView(@DrawableRes int icon, View.OnClickListener onClickListener) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(icon);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        int margin = getResources().getDimensionPixelOffset(R.dimen.marginRight);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        imageView.setPadding(margin, margin, margin, margin);
        baseToolbar.addView(imageView, layoutParams);
        imageView.setOnClickListener(onClickListener);
    }

    protected Toolbar getToolbar() {
        return baseToolbar;
    }

    protected void setMenu(@MenuRes int menu, Toolbar.OnMenuItemClickListener onMenuItemClickListener) {
        baseToolbar.inflateMenu(menu);
        baseToolbar.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    protected void showTitleBar(boolean isVisible) {

        if (isVisible) {
            baseToolbar.setVisibility(View.VISIBLE);
        } else {
            baseToolbar.setVisibility(View.GONE);
        }
    }


    protected void showToast(CharSequence content) {

        if (getActivity() != null) {
            Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
        }
    }

    protected void showSnackbar(CharSequence content) {

        this.showSnackbar(baseLayout, content);
    }

    protected void showSnackbar(CharSequence content, CharSequence actionTxt, View.OnClickListener onClickListener) {

        mSnackbar = Snackbar.make(baseLayout, content, Snackbar.LENGTH_SHORT).setAction(actionTxt, onClickListener);
        mSnackbar.show();
    }

    protected void showSnackbar(View parentView, CharSequence content) {

        mSnackbar = Snackbar.make(parentView, content, Snackbar.LENGTH_SHORT);
        mSnackbar.show();
    }

    public RelativeLayout getTitleView() {

        return (RelativeLayout) baseLayout.findViewById(R.id.titleContent);
    }

    public void setBackGround(int id) {

        baseLayout.setBackgroundResource(id);
    }

    public void setBackGroundColor(int color) {

        baseLayout.setBackgroundColor(color);
    }

    public void setBackGroundContent(int id) {

        baseLayout.findViewById(R.id.appContent).setBackgroundResource(id);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setBackGroundContent(Drawable drawable) {

        baseLayout.findViewById(R.id.appContent).setBackground(drawable);
    }

    public void setBackGroundContentColor(int color) {

        baseLayout.findViewById(R.id.appContent).setBackgroundColor(color);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setBackGround(Drawable drawable) {
        baseLayout.findViewById(R.id.appContent).setBackground(drawable);
    }

    private void onBackPressed() {

        baseLayout.setFocusable(true);
        baseLayout.setFocusableInTouchMode(true);
        baseLayout.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        if (mLoadingPopupWindow != null && mLoadingPopupWindow.isShowing() &&
//                                isHandle) {
//                            mLoadingPopupWindow.dismiss();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * 获取标题尺寸
     *
     * @return int[]{width,height}
     */
    public int[] getTitleSize() {

        View titleView = getTitleView();
        titleView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return new int[]{titleView.getMeasuredWidth(), titleView.getMeasuredHeight()};
    }

    /**
     * 返回资源文件ID
     *
     * @return
     */
    public abstract
    @LayoutRes
    int onResultLayoutResId();

    public void onInit() {

    }

//    public abstract void onInitView(BaseParentViewHolder holder);

    public abstract void onBindData();

    public void onResumeBindData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        onResumeBindData();
    }

    public void onInitLayout() {
        if (hasTitle()) {
            showTitleBar(true);
            setBackValid(false);
        }

    }
}
