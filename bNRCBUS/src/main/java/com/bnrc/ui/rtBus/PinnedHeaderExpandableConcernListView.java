package com.bnrc.ui.rtBus;

import com.bnrc.busapp.R;
import com.bnrc.ui.rjz.other.Bean;
import com.bnrc.ui.rjz.other.RandomColor;
import com.bnrc.ui.rjz.other.TextDrawable;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;

public class PinnedHeaderExpandableConcernListView extends ExpandableListView
		implements OnScrollListener, OnGroupClickListener {
	public PinnedHeaderExpandableConcernListView(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		registerListener();
	}

	public PinnedHeaderExpandableConcernListView(Context context,
			AttributeSet attrs) {
		super(context, attrs);
		registerListener();
	}

	public PinnedHeaderExpandableConcernListView(Context context) {
		super(context);
		registerListener();
	}

	/**
	 * Adapter 鎺ュ彛 . 鍒楄〃蹇呴』瀹炵幇姝ゆ帴鍙� .
	 */
	public interface HeaderAdapter {
		public static final int PINNED_HEADER_GONE = 0;
		public static final int PINNED_HEADER_VISIBLE = 1;
		public static final int PINNED_HEADER_PUSHED_UP = 2;

		/**
		 * 鑾峰彇 Header 鐨勭姸鎬�
		 * 
		 * @param groupPosition
		 * @param childPosition
		 * @return 
		 *         PINNED_HEADER_GONE,PINNED_HEADER_VISIBLE,PINNED_HEADER_PUSHED_UP
		 *         鍏朵腑涔嬩竴
		 */
		int getHeaderState(int groupPosition, int childPosition);

		/**
		 * 閰嶇疆 Header, 璁� Header 鐭ラ亾鏄剧ず鐨勫唴瀹�
		 * 
		 * @param header
		 * @param groupPosition
		 * @param childPosition
		 * @param alpha
		 */
		void configureHeader(View header, int groupPosition, int childPosition,
				int alpha);

		/**
		 * 璁剧疆缁勬寜涓嬬殑鐘舵��
		 * 
		 * @param groupPosition
		 * @param status
		 */
		void setGroupClickStatus(int groupPosition, int status);

		/**
		 * 鑾峰彇缁勬寜涓嬬殑鐘舵��
		 * 
		 * @param groupPosition
		 * @return
		 */
		int getGroupClickStatus(int groupPosition);

	}

	private static final int MAX_ALPHA = 255;

	private HeaderAdapter mAdapter;

	/**
	 * 鐢ㄤ簬鍦ㄥ垪琛ㄥご鏄剧ず鐨� View,mHeaderViewVisible 涓� true 鎵嶅彲瑙�
	 */
	private View mHeaderView;

	/**
	 * 鍒楄〃澶存槸鍚﹀彲瑙�
	 */
	private boolean mHeaderViewVisible;

	private int mHeaderViewWidth;

	private int mHeaderViewHeight;
	private RandomColor mColor = RandomColor.MATERIAL;

	public void setHeaderView(int type, View view) {
		mHeaderView = view;
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);

		if (mHeaderView != null) {
			setFadingEdgeLength(0);
		}
		TextView tv = (TextView) view.findViewById(R.id.tv_arrive);
		ImageView mStationSeq = (ImageView) view
				.findViewById(R.id.iv_stationSeq);
		ImageView mStationIcon = ((ImageView) view.findViewById(R.id.groupIcon));
		mStationIcon.setImageResource(R.drawable.down_arrow);
		switch (type) {
		case 0:
			tv.setText("查看详细");
			mStationSeq.setVisibility(View.INVISIBLE);

			break;
		case 1:
			tv.setText("到这里去");
			Bean bean = new Bean(getSequence(0).toString());
			mStationSeq.setImageDrawable(TextDrawable.builder().buildRound(
					bean.name, mColor.getColor(bean.name, 0)));
			mStationSeq.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		requestLayout();
	}

	private String getSequence(int position) {
		return String.valueOf((char) (position + 65));
	}

	private void registerListener() {
		setOnScrollListener(this);
		setOnGroupClickListener(this);
	}

	/**
	 * 鐐瑰嚮 HeaderView 瑙﹀彂鐨勪簨浠�
	 */
	private void headerViewClick() {
		long packedPosition = getExpandableListPosition(this
				.getFirstVisiblePosition());

		int groupPosition = ExpandableListView
				.getPackedPositionGroup(packedPosition);

		if (mAdapter.getGroupClickStatus(groupPosition) == 1) {
			this.collapseGroup(groupPosition);
			mAdapter.setGroupClickStatus(groupPosition, 0);
		} else {
			this.expandGroup(groupPosition);
			mAdapter.setGroupClickStatus(groupPosition, 1);
		}

		this.setSelectedGroup(groupPosition);
	}

	private float mDownX;
	private float mDownY;

	/**
	 * 濡傛灉 HeaderView 鏄彲瑙佺殑 , 姝ゅ嚱鏁扮敤浜庡垽鏂槸鍚︾偣鍑讳簡 HeaderView, 骞跺鍋氱浉搴旂殑澶勭悊 , 鍥犱负
	 * HeaderView 鏄敾涓婂幓鐨� , 鎵�浠ヨ缃簨浠剁洃鍚槸鏃犳晥鐨� , 鍙湁鑷鎺у埗 .
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mHeaderViewVisible) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mDownX = ev.getX();
				mDownY = ev.getY();
				if (mDownX <= mHeaderViewWidth && mDownY <= mHeaderViewHeight) {
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				float x = ev.getX();
				float y = ev.getY();
				float offsetX = Math.abs(x - mDownX);
				float offsetY = Math.abs(y - mDownY);
				// 濡傛灉 HeaderView 鏄彲瑙佺殑 , 鐐瑰嚮鍦� HeaderView 鍐� , 閭ｄ箞瑙﹀彂
				// headerClick()
				if (x <= mHeaderViewWidth && y <= mHeaderViewHeight
						&& offsetX <= mHeaderViewWidth
						&& offsetY <= mHeaderViewHeight) {
					if (mHeaderView != null) {
						headerViewClick();
					}

					return true;
				}
				break;
			default:
				break;
			}
		}

		return super.onTouchEvent(ev);

	}

	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		super.setAdapter(adapter);
		mAdapter = (HeaderAdapter) adapter;
	}

	/**
	 * 
	 * 鐐瑰嚮浜� Group 瑙﹀彂鐨勪簨浠� , 瑕佹牴鎹牴鎹綋鍓嶇偣鍑� Group 鐨勭姸鎬佹潵
	 */
	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		if (mAdapter.getGroupClickStatus(groupPosition) == 0) {
			mAdapter.setGroupClickStatus(groupPosition, 1);
			parent.expandGroup(groupPosition);
			// Header鑷姩缃《
			// parent.setSelectedGroup(groupPosition);

		} else if (mAdapter.getGroupClickStatus(groupPosition) == 1) {
			mAdapter.setGroupClickStatus(groupPosition, 0);
			parent.collapseGroup(groupPosition);
		}

		// 杩斿洖 true 鎵嶅彲浠ュ脊鍥炵涓�琛� , 涓嶇煡閬撲负浠�涔�
		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mHeaderView != null) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		}
	}

	private int mOldState = -1;

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		final long flatPostion = getExpandableListPosition(getFirstVisiblePosition());
		final int groupPos = ExpandableListView
				.getPackedPositionGroup(flatPostion);
		final int childPos = ExpandableListView
				.getPackedPositionChild(flatPostion);
		if (mAdapter != null) {
			int state = mAdapter.getHeaderState(groupPos, childPos);
			if (mHeaderView != null && mAdapter != null && state != mOldState) {
				mOldState = state;
				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			}

			configureHeaderView(groupPos, childPos);
		}
	}

	public void configureHeaderView(int groupPosition, int childPosition) {
		if (mHeaderView == null || mAdapter == null
				|| ((ExpandableListAdapter) mAdapter).getGroupCount() == 0) {
			return;
		}

		int state = mAdapter.getHeaderState(groupPosition, childPosition);

		switch (state) {
		case HeaderAdapter.PINNED_HEADER_GONE: {
			mHeaderViewVisible = false;
			break;
		}

		case HeaderAdapter.PINNED_HEADER_VISIBLE: {
			mAdapter.configureHeader(mHeaderView, groupPosition, childPosition,
					MAX_ALPHA);

			if (mHeaderView.getTop() != 0) {
				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			}

			mHeaderViewVisible = true;

			break;
		}

		case HeaderAdapter.PINNED_HEADER_PUSHED_UP: {
			View firstView = getChildAt(0);
			int bottom = firstView.getBottom();

			// intitemHeight = firstView.getHeight();
			int headerHeight = mHeaderView.getHeight();

			int y;

			int alpha;

			if (bottom < headerHeight) {
				y = (bottom - headerHeight);
				alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
			} else {
				y = 0;
				alpha = MAX_ALPHA;
			}

			mAdapter.configureHeader(mHeaderView, groupPosition, childPosition,
					alpha);

			if (mHeaderView.getTop() != y) {
				mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight
						+ y);
			}

			mHeaderViewVisible = true;
			break;
		}
		}
	}

	@Override
	/**
	 * 鍒楄〃鐣岄潰鏇存柊鏃惰皟鐢ㄨ鏂规硶(濡傛粴鍔ㄦ椂)
	 */
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mHeaderViewVisible) {
			// 鍒嗙粍鏍忔槸鐩存帴缁樺埗鍒扮晫闈腑锛岃�屼笉鏄姞鍏ュ埌ViewGroup涓�
			drawChild(canvas, mHeaderView, getDrawingTime());
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		final long flatPos = getExpandableListPosition(firstVisibleItem);
		int groupPosition = ExpandableListView.getPackedPositionGroup(flatPos);
		int childPosition = ExpandableListView.getPackedPositionChild(flatPos);

		configureHeaderView(groupPosition, childPosition);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
}
