package com.mylibrary.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * @blog http://blog.csdn.net/mcy478643968/article/details/19609407
 * @blog http://blog.csdn.net/leewenjin/article/details/21011841
 * @author zoulx
 *
 */
public class CustomCompatViewPager extends ViewPager {

	float curX = 0f;
	float downX = 0f;

	public CustomCompatViewPager(Context context) {
		super(context);
	}

	public CustomCompatViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		curX = ev.getX();
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			downX = curX;
		}
		int curIndex = getCurrentItem();
		if (curIndex == 0) {
			if (downX <= curX) {
				getParent().requestDisallowInterceptTouchEvent(false);//false为拦截
			} else {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
		} else if (curIndex == getAdapter().getCount() - 1) {
			if (downX >= curX) {
				getParent().requestDisallowInterceptTouchEvent(false);
			} else {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
		} else {
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		return super.onTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		return false;
	}
}
