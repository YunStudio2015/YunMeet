/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PullToRefreshHorizontalScrollView extends PullToRefreshBase<HorizontalScrollView> {


	ViewPager viewpager = null;

	public PullToRefreshHorizontalScrollView(Context context) {
		super(context);
	}

	public PullToRefreshHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshHorizontalScrollView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshHorizontalScrollView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.HORIZONTAL;
	}

	@Override
	protected HorizontalScrollView createRefreshableView(Context context, AttributeSet attrs) {
		HorizontalScrollView scrollView;

		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			scrollView = new InternalHorizontalScrollViewSDK9(context, attrs);
		} else {
			scrollView = new HorizontalScrollView(context, attrs);
		}

		scrollView.setOnTouchListener(null);
		scrollView.setId(R.id.scrollview);
		return scrollView;
	}


	@Override
	protected boolean isReadyForPullStart() {

		// either the page is the first, and the getscrollx == 0
		if (viewpager != null) {
			return (mRefreshableView.getScrollX() == 0 && viewpager.getCurrentItem() == 0);
		} else
			return mRefreshableView.getScrollX() == 0;
	}

	public void setInnerViewpager (ViewPager viewpager) {
		this.viewpager = viewpager;
	}

	@Override
	protected boolean isReadyForPullEnd() {
		View scrollViewChild = mRefreshableView.getChildAt(0);
		if (null != scrollViewChild) {
			return mRefreshableView.getScrollX() >= (scrollViewChild.getWidth() - getWidth());
		}
		return false;
	}

	@TargetApi(9)
	final class InternalHorizontalScrollViewSDK9 extends HorizontalScrollView {

		private GestureDetector mGestureDetector;
		private int Scroll_height = 0;
		private int view_height = 0;
		protected Field scrollView_mScroller;
		private static final String TAG = "CustomScrollView";

		public InternalHorizontalScrollViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
			mGestureDetector = new GestureDetector(context, new YScrollDetector());
			setFadingEdgeLength(0);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
									   int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(PullToRefreshHorizontalScrollView.this, deltaX, scrollX, deltaY, scrollY,
					getScrollRange(), isTouchEvent);

			return returnValue;
		}

		/**
		 * Taken from the AOSP ScrollView source
		 */
		private int getScrollRange() {
			int scrollRange = 0;
			if (getChildCount() > 0) {
				View child = getChildAt(0);
				scrollRange = Math.max(0, child.getWidth() - (getWidth() - getPaddingLeft() - getPaddingRight()));
			}
			return scrollRange;
		}

		/* lets do this */
		@Override
		public boolean onInterceptTouchEvent(MotionEvent ev) {
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				stopAnim();
			}
			boolean ret = super.onInterceptTouchEvent(ev);
			boolean ret2 = mGestureDetector.onTouchEvent(ev);
			return ret && ret2;
		}
		// Return false if we're scrolling in the x direction
		class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
									float distanceX, float distanceY) {
				if (Math.abs(distanceY) > Math.abs(distanceX)) {
					return true;
				}
				return false;
			}
		}
		@Override
		protected void onScrollChanged(int l, int t, int oldl, int oldt) {
			boolean stop = false;
			if (Scroll_height - view_height == t) {
				stop = true;
			}
			if (t == 0 || stop == true) {
				try {
					if (scrollView_mScroller == null) {
						scrollView_mScroller = getDeclaredField(this, "mScroller");
					}
					Object ob = scrollView_mScroller.get(this);
					if (ob == null || !(ob instanceof Scroller)) {
						return;
					}
					Scroller sc = (Scroller) ob;
					sc.abortAnimation();
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
			}
			super.onScrollChanged(l, t, oldl, oldt);
		}
		private void stopAnim() {
			try {
				if (scrollView_mScroller == null) {
					scrollView_mScroller = getDeclaredField(this, "mScroller");
				}
				Object ob = scrollView_mScroller.get(this);
				if (ob == null) {
					return;
				}
				Method method = ob.getClass().getMethod("abortAnimation");
				method.invoke(ob);
			} catch (Exception ex) {
				Log.e(TAG, ex.getMessage());
			}
		}
		@Override
		protected int computeVerticalScrollRange() {
			Scroll_height = super.computeVerticalScrollRange();
			return Scroll_height;
		}
		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			super.onLayout(changed, l, t, r, b);
			if (changed == true) {
				view_height = b - t;
			}
		}
		@Override
		public void requestChildFocus(View child, View focused) {
			if (focused != null && focused instanceof WebView) {
				return;
			}
			super.requestChildFocus(child, focused);
		}
		/**
		 * 获取一个对象隐藏的属性，并设置属性为public属性允许直接访问
		 *
		 * @return {@link Field} 如果无法读取，返回null；返回的Field需要使用者自己缓存，本方法不做缓存�?
		 */
		public Field getDeclaredField(Object object, String field_name) {
			Class<?> cla = object.getClass();
			Field field = null;
			for (; cla != Object.class; cla = cla.getSuperclass()) {
				try {
					field = cla.getDeclaredField(field_name);
					field.setAccessible(true);
					return field;
				} catch (Exception e) {
				}
			}
			return null;
		}



	}
}
