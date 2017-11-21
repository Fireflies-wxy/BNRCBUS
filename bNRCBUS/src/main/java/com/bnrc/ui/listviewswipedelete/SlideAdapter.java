package com.bnrc.ui.listviewswipedelete;

import java.util.List;

import com.bnrc.busapp.R;
import com.bnrc.ui.listviewswipedelete.SlideView.OnSlideListener;
import com.umeng.fb.util.Log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SlideAdapter extends BaseAdapter {
	private static final String TAG = SlideAdapter.class.getSimpleName();
	private LayoutInflater mInflater;
	private Context mContext;
	private SlideView mLastSlideViewWithStatusOn;

	List<DelBlogItem> mBlogItems;

	public SlideAdapter(Context context, List<DelBlogItem> mBlogItems) {
		super();
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		this.mBlogItems = mBlogItems;
	}

	@Override
	public int getCount() {
		return mBlogItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mBlogItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		SlideView slideView = (SlideView) convertView;
		if (slideView == null) {
			View itemView = mInflater.inflate(R.layout.child_alert_bottom,
					null);

			slideView = new SlideView(mContext);
			slideView.setContentView(itemView);

			holder = new ViewHolder(slideView);
			slideView.setOnSlideListener(mSlideListener);
			slideView.setTag(holder);
		} else {
			holder = (ViewHolder) slideView.getTag();
		}
		DelBlogItem item = mBlogItems.get(position);
		item.slideView = slideView;
		item.slideView.shrink();
		holder.mAlertStation.setText(item.getStation());
		holder.mAlertState.setText(item.getAlertState());
		holder.deleteHolder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mBlogItems.remove(position);
				notifyDataSetChanged();
				Toast.makeText(mContext, "onClick" + position, 0).show();
			}
		});
		holder.mSwitchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((ImageView) arg0).setImageResource(R.drawable.switchbtn_open);

			}
		});
		return slideView;
	}

	private OnSlideListener mSlideListener = new OnSlideListener() {

		@Override
		public void onSlide(View view, int status) {
			if (mLastSlideViewWithStatusOn != null
					&& mLastSlideViewWithStatusOn != view) {
				mLastSlideViewWithStatusOn.shrink();
			}

			if (status == SLIDE_STATUS_ON) {
				mLastSlideViewWithStatusOn = (SlideView) view;
			}
		}
	};

	class ViewHolder {

		public TextView mAlertStation;
		public TextView mAlertState;
		public ViewGroup deleteHolder;
		public ImageView mSwitchBtn;
		public RelativeLayout mContainer;

		ViewHolder(View view) {
			mAlertStation = (TextView) view.findViewById(R.id.tv_alert_line);
			mAlertState = (TextView) view.findViewById(R.id.tv_alert_state);
			deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
			mSwitchBtn = (ImageView) view.findViewById(R.id.iv_switch);
			mContainer = (RelativeLayout) view
					.findViewById(R.id.rLayout_container);

		}
	}
}
