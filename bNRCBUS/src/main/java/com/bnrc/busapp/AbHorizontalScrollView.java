package com.bnrc.busapp;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.HorizontalScrollView;

public class AbHorizontalScrollView extends HorizontalScrollView {
	private static final String TAG = AbHorizontalScrollView.class
			.getSimpleName();

	private int intitPosition;
	private int childWidth = 0;
	private AbOnScrollListener onScrollListner;
	private boolean isOtherFragment;

	public AbHorizontalScrollView(Context context) {
		super(context);
	}

	public AbHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i(TAG,
				" public AbHorizontalScrollView(Context context, AttributeSet attrs)");
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// super.onScrollChanged(l, t, oldl, oldt);
		//
		// Log.i(TAG, "void onScrollChanged");
		// int newPosition = getScrollX();
		// Log.i(TAG, " newPosition " + newPosition);
		// Log.i(TAG, " intitPosition " + intitPosition);
		//
		// if (intitPosition - newPosition == 0) {
		// if (onScrollListner == null) {
		// return;
		// }
		// onScrollListner.onScrollStoped();
		// new Handler().postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// Rect outRect = new Rect();
		// getDrawingRect(outRect);
		// if (getScrollX() <= outRect.width() / 2) {
		// onScrollListner.onScroll(0);
		// onScrollListner.onScrollToLeft();
		// return;
		// } else {
		// onScrollListner.onScroll(getScrollX());
		// onScrollListner.onScrollToRight();
		// return;
		// }
		// }
		// }, 200);
		//
		// } else {
		// Log.i(TAG, "else");
		//
		// intitPosition = getScrollX();
		// Log.i(TAG, "else " + intitPosition);
		//
		// checkTotalWidth();
		// }

		super.onScrollChanged(l, t, oldl, oldt);

		int newPosition = getScrollX();
		Log.i(TAG, " newPosition " + newPosition);
		Log.i(TAG, " intitPosition " + intitPosition);
		// intitPosition = oldl;
		if (intitPosition - newPosition >= 0) {
			Log.i(TAG, "intitPosition - newPosition > 0");
			if (onScrollListner == null) {
				return;
			}
			onScrollListner.onScrollStoped();
			Log.i(TAG, " onScrollStoped ");

			this.postDelayed(new Runnable() {
				@Override
				public void run() {
					Log.i(TAG, " run ");

					Rect outRect = new Rect();
					getDrawingRect(outRect);
					if (getScrollX() <= outRect.width() / 3) {
						Log.i(TAG, "onScrollToLeft();");

						onScrollListner.onScroll(0);
						onScrollListner.onScrollToLeft();
						return;
					} else if (getScrollX() >= outRect.width() * 2 / 3) {
						Log.i(TAG, " onScrollToRight");

						onScrollListner.onScroll(childWidth * 2 / 3);
						onScrollListner.onScrollToRight();
						return;
					} else {
						onScrollListner.onScroll(childWidth / 3);
						onScrollListner.onScrollToMiddle();
						return;
					}
				}
			}, 200);

		} else {
			// Log.i(TAG, "else");
			//
			intitPosition = getScrollX();
			// Log.i(TAG, "else " + intitPosition);

			checkTotalWidth();
		}

	}

	/**
	 * 
	 * 描述：设置监听器
	 * 
	 * @param listner
	 * @throws
	 */
	public void setOnScrollListener(AbOnScrollListener listner) {
		onScrollListner = listner;
	}

	/**
	 * 计算总宽.
	 */
	private void checkTotalWidth() {
		if (childWidth > 0) {
			return;
		}
		for (int i = 0; i < getChildCount(); i++) {
			childWidth += getChildAt(i).getWidth();
		}
	}

	public interface AbOnScrollListener {

		/**
		 * 滚动.
		 * 
		 * @param arg1
		 *            返回参数
		 */
		public void onScroll(int arg1);

		/**
		 * 滚动停止.
		 */
		public void onScrollStoped();

		/**
		 * 滚到了最左边.
		 */
		public void onScrollToLeft();

		/**
		 * 滚到了最右边.
		 */
		public void onScrollToRight();

		/**
		 * 滚到了中间.
		 */
		public void onScrollToMiddle();

	}

	@Override
	public void fling(int velocityY) {
		// 此处改变速度，可根据需要变快或变慢。
		super.fling(velocityY * 3);
	}
}
