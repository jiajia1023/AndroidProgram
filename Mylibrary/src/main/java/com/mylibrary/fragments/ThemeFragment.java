package com.mylibrary.fragments;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mylibrary.MyApplication;
import com.mylibrary.R;
import com.mylibrary.adapter.BaseParentViewHolder;
import com.mylibrary.manager.SystemBarTintManager;
import com.mylibrary.widgets.LoadingPopupWindow;

/**
 * 请填写方法内容
 *
 * @author Chris zou
 * @Date 16/10/11
 * @modifyInfo1 chriszou-16/10/11
 * @modifyContent
 */
public abstract class ThemeFragment extends Fragment {

    private static final String TAG = "ThemeFragment";

    private Snackbar mSnackbar;
    private AlertDialog mAlertDialog;
    private LoadingPopupWindow mLoadingPopupWindow;
    /**
     * popupWindow是否可操作
     */
    private boolean isHandle = true;
    /**
     * 主布局宽高
     */
    protected int contentWidth, contentHeight;
    /**
     * 基础holder，没有更好的传入方式，暂不支持扩展
     */
    protected BaseParentViewHolder mViewHolder;
    protected SystemBarTintManager tintManager;
    protected View rootView;
    private Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mViewHolder == null || mViewHolder.rootView == null) {
            mViewHolder = onCreateViewHolder(inflater, container);
            //替换Action
            mToolbar = mViewHolder.getView(R.id.mainToolbar);
            onBackPressed();
        }

        ViewGroup parent = (ViewGroup) mViewHolder.rootView.getParent();
        if (parent != null) {
            parent.removeView(mViewHolder.rootView);
        }
        return mViewHolder.rootView;
    }

    private BaseParentViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup container) {

        rootView = inflater.inflate(R.layout.app_content, container, false);
        mViewHolder = new BaseParentViewHolder(rootView);

        return mViewHolder;
    }

    public void setContentView(@LayoutRes int resId, LayoutInflater inflater, ViewGroup container) {

        FrameLayout appContent = mViewHolder.getView(R.id.appContent);
        if (appContent.getChildCount() <= 0) {
            View childView = inflater.inflate(resId, container, false);
            appContent.addView(childView);
        }
    }

    public void setStatusTintColor(int color) {

        tintManager.setStatusBarTintColor(color);
    }


    private void initDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                R.style.AppTheme_Dialog);
        builder.setView(
                LayoutInflater.from(getActivity()).inflate(R.layout.pw_loading, null));
        builder.setInverseBackgroundForced(true);
        mAlertDialog = builder.create();
        Window window = mAlertDialog.getWindow();
        window.setGravity(Gravity.CENTER);

        mAlertDialog.setCanceledOnTouchOutside(true);
        mAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    if (mAlertDialog != null && mAlertDialog.isShowing() && isHandle) {
                        mAlertDialog.dismiss();
                    } else {
                        mAlertDialog.dismiss();
                        MyApplication.getInstance().removeActivity(getActivity());
                        return true;
                    }
                }
                return false;
            }
        });
    }

    protected void alertLoadingDialog() {

        alertLoadingDialog(true);
    }

    protected void alertLoadingDialog(boolean isHandle) {

        this.isHandle = isHandle;
        if (mAlertDialog == null) {
            initDialog();
        }

        mAlertDialog.setCanceledOnTouchOutside(isHandle);
        mAlertDialog.show();
    }

    protected void dismissDialog() {

        if (mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    private void initPopupWindow() {

        mLoadingPopupWindow = LoadingPopupWindow.LoadingPWBuilder.getInstance(getActivity())
                .getPopupWindow();
    }


    protected void alertPopupWindow() {

        this.alertPopupWindow(true);
    }

    /**
     * 弹出加载提示框
     *
     * @param isHandle true：点击及返回键可消失 false:用户不可操作
     */
    protected void alertPopupWindow(boolean isHandle) {

        this.isHandle = isHandle;
        if (mLoadingPopupWindow == null) {
            initPopupWindow();
        }
        mLoadingPopupWindow.setOutsideTouchable(isHandle);
        mLoadingPopupWindow.showPopupWindow(mViewHolder.rootView);
    }

    protected void dismissPopupWindow() {

        if (mLoadingPopupWindow.isShowing()) {
            mLoadingPopupWindow.dismiss();
        }
    }


    protected <V> V getView(@IdRes int id) {

        return (V) mViewHolder.getView(id);
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
            mToolbar.setNavigationIcon(R.mipmap.ic_back_white);
        } else if (icon != -1) {
            mToolbar.setNavigationIcon(icon);
        }

        if (onClickListener == null) {
            onClickListener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    getActivity().finish();
                }
            };
        }
        mToolbar.setNavigationOnClickListener(onClickListener);
    }

    public void setTitle(int titleResId) {

        this.setTitle(getString(titleResId));
    }

    public void setTitle(CharSequence title) {

        mToolbar.setTitle(title);
    }

    public void setSubtitle(@StringRes int subtitle) {
        this.setSubtitle(getString(subtitle));
    }

    public void setSubtitle(CharSequence subtitle) {
        mToolbar.setSubtitle(subtitle);
    }

    protected void setRightView(View view) {

        mToolbar.addView(view);
    }

    protected void setRightView(@DrawableRes int icon, View.OnClickListener onClickListener) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(icon);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        layoutParams.rightMargin = getResources().getDimensionPixelOffset(R.dimen.marginRight);
        mToolbar.addView(imageView, layoutParams);
        imageView.setOnClickListener(onClickListener);
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    protected void setMenu(@MenuRes int menu, Toolbar.OnMenuItemClickListener onMenuItemClickListener) {
        mToolbar.inflateMenu(menu);
        mToolbar.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    protected void showTitleBar(boolean isVisible) {

        if (isVisible) {
            mToolbar.setVisibility(View.VISIBLE);
        } else {
            mToolbar.setVisibility(View.GONE);
        }
    }

    protected <T> T setOnClick(@IdRes @NonNull int resId, View.OnClickListener onClickListener) {
        View view = getView(resId);
        view.setOnClickListener(onClickListener);
        return (T) view;
    }

    protected void setOnClick(@IdRes @NonNull int[] resIds, View.OnClickListener onClickListener) {
        for (int resId : resIds) {
            ((View) getView(resId)).setOnClickListener(onClickListener);
        }
    }

    protected void showToast(CharSequence content) {

        if (getActivity() != null) {
            Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
        }
    }

    protected void showSnackbar(CharSequence content) {

        this.showSnackbar(mViewHolder.rootView, content);
    }

    protected void showSnackbar(CharSequence content, CharSequence actionTxt, View.OnClickListener onClickListener) {

        mSnackbar = Snackbar.make(mViewHolder.rootView, content, Snackbar.LENGTH_SHORT)
                .setAction(actionTxt, onClickListener);
        mSnackbar.show();
    }

    protected void showSnackbar(View parentView, CharSequence content) {

        mSnackbar = Snackbar.make(parentView, content, Snackbar.LENGTH_SHORT);
        mSnackbar.show();
    }

    public BaseParentViewHolder getViewHolder() {

        return this.mViewHolder;
    }

    public RelativeLayout getTitleView() {

        return this.mViewHolder.getView(R.id.titleContent);
    }

    public <T> T getMainView() {

        return (T) this.mViewHolder.rootView;
    }

    public CharSequence getTitle() {

        TextView titleView = mViewHolder.getView(R.id.titleName);
        return titleView.getText();
    }

    public void setBackGround(int id) {

        mViewHolder.rootView.setBackgroundResource(id);
    }

    public void setBackGroundColor(int color) {

        mViewHolder.rootView.setBackgroundColor(color);
    }

    public void setBackGroundContent(int id) {

        mViewHolder.getView(R.id.appContent).setBackgroundResource(id);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setBackGroundContent(Drawable drawable) {

        mViewHolder.getView(R.id.appContent).setBackground(drawable);
    }

    public void setBackGroundContentColor(int color) {

        mViewHolder.getView(R.id.appContent).setBackgroundColor(color);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setBackGround(Drawable drawable) {

        mViewHolder.rootView.setBackground(drawable);
    }

    public void onBackPressed() {

        mViewHolder.rootView.setFocusable(true);
        mViewHolder.rootView.setFocusableInTouchMode(true);
        mViewHolder.rootView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (mLoadingPopupWindow != null && mLoadingPopupWindow.isShowing() &&
                                isHandle) {
                            mLoadingPopupWindow.dismiss();
                            return true;
                        }
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


    public AppCompatImageButton getBackView() {

        return mViewHolder.getView(R.id.titleLeft);
    }

    public TextView getRightView() {

        return mViewHolder.getView(R.id.titleRight);
    }
}
