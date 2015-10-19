package com.kugou.framework.component.base;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ScrollableViewPager extends ViewPager {

	private boolean isCanScroll = true;

	public ScrollableViewPager(Context context) {
		super(context);
	}

	public ScrollableViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isCanScroll) {
			if (ignoreViews != null && getScrollX() <= 0) {
				for (View ignoreView : ignoreViews) {
					if (ignoreView != null && checkArea(ignoreView, ev)) {
						return false;
					}
				}
			}
			try {
				return super.onTouchEvent(ev);
			} catch (IllegalArgumentException ex) {
			}
		}
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isCanScroll) {
			if (ignoreViews != null && getScrollX() <= 0) {
				for (View ignoreView : ignoreViews) {
					if (ignoreView != null && checkArea(ignoreView, ev)) {
						return false;
					}
				}
			}
			try {
				return super.onInterceptTouchEvent(ev);
			} catch (IllegalArgumentException ex) {
			}
		}
		return false;
	}

	private List<View> ignoreViews;

	public List<View> getIgnoreViews() {
		return ignoreViews;
	}

	public void setIgnoreView(View ignoreView) {
		if (ignoreViews == null) {
			ignoreViews = new ArrayList<View>();
		}
		this.ignoreViews.add(ignoreView);
	}

	/**
	 * 检测view是否在点击范围内
	 * 
	 * @param locate
	 * @param v
	 * @return
	 */
	private boolean checkArea(View v, MotionEvent event) {
		float x = event.getRawX();
		float y = event.getRawY();
		int[] locate = new int[2];
		v.getLocationOnScreen(locate);
		int l = locate[0];
		int r = l + v.getWidth();
		int t = locate[1];
		int b = t + v.getHeight();
		if (l < x && x < r && t < y && y < b) {
			return true;
		}
		return false;
	}


}
