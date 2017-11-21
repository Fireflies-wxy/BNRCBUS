package com.bnrc.adapter;

import java.util.List;
import java.util.Map;

import com.bnrc.adapter.MyListViewAdapter.ViewHolder;
import com.bnrc.busapp.R;
import com.bnrc.util.UserDataDBHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyAlertAdapter extends BaseAdapter {
	private Context mContext;
	private int mResource;
	private List<? extends Map<String, ?>> mData;
	private String[] mFrom;
	private int[] mTo;
	private UserDataDBHelper mUserDB;

	public MyAlertAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mData = data;
		mResource = resource;
		mFrom = from;
		mTo = to;
		mUserDB = UserDataDBHelper.getInstance(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(mResource,
					parent, false);
			holder = new ViewHolder();
			holder.itemsIcon = ((ImageView) convertView.findViewById(mTo[0]));
			holder.itemsTitle = ((TextView) convertView.findViewById(mTo[1]));
			holder.itemsAlertBtn = ((ImageView) convertView
					.findViewById(mTo[2]));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Map<String, ?> dataSet = mData.get(position);
		if (dataSet == null) {
			return null;
		}
		final Object data0 = dataSet.get(mFrom[0]);// icon
		final Object data1 = dataSet.get(mFrom[1]);// stationName
		final Object data2 = dataSet.get(mFrom[5]);// isOpen

		holder.itemsIcon.setImageResource((Integer) data0);
		holder.itemsTitle.setText(data1.toString());
		switch (Integer.parseInt(data2.toString())) {
		case 0:// 提醒关闭
			holder.itemsAlertBtn.setImageResource(R.drawable.switchbtn_closed);
			break;
		case 1:// 提醒打开
			holder.itemsAlertBtn.setImageResource(R.drawable.switchbtn_open);
			break;
		default:
			holder.itemsAlertBtn.setImageResource(R.drawable.switchbtn_open);
			break;
		}
		// holder.itemsAlertBtn
		// .setImageResource(Integer.parseInt(data2.toString()));
		holder.itemsAlertBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String stationName = mData.get(position).get("itemsTitle")
						.toString();
				if (mUserDB.IsAlertOpenStation(stationName))// 关闭
				{
					holder.itemsAlertBtn
							.setImageResource(R.drawable.switchbtn_closed);
					mUserDB.closeAlertStation(stationName);
					Toast.makeText(mContext, "close " + stationName, 1000)
							.show();

				} else {
					holder.itemsAlertBtn
							.setImageResource(R.drawable.switchbtn_open);
					mUserDB.openAlertStation(stationName);
					Toast.makeText(mContext, "open " + stationName, 1000)
							.show();
				}
			}
		});
		return convertView;
	}

	static class ViewHolder {
		ImageView itemsIcon;
		TextView itemsTitle;
		ImageView itemsAlertBtn;
	}
}
