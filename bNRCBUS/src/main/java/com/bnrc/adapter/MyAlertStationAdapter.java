package com.bnrc.adapter;

import java.util.List;
import java.util.Map;

import com.bnrc.adapter.MyListViewAdapter.ViewHolder;
import com.bnrc.busapp.R;
import com.bnrc.util.DataBaseHelper;
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

public class MyAlertStationAdapter extends BaseAdapter {
	private Context mContext;
	private int mResource;
	private List<? extends Map<String, ?>> mData;
	private String[] mFrom;
	private int[] mTo;
	private UserDataDBHelper mUserDB;

	public MyAlertStationAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
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
		final Object data0 = dataSet.get(mFrom[0]);// stationName
		holder.itemsTitle.setText(data0.toString());
		boolean open = UserDataDBHelper.getInstance(mContext)
				.IsAlertOpenStation(data0.toString());
		if (!open)
			holder.itemsAlertBtn.setImageResource(R.drawable.switchbtn_closed);
		else
			holder.itemsAlertBtn.setImageResource(R.drawable.switchbtn_open);

		// holder.itemsAlertBtn
		// .setImageResource(Integer.parseInt(data2.toString()));
		holder.itemsAlertBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String stationName = mData.get(position).get("itemsTitle")
						.toString();
				if (mUserDB.IsAlertOpenStation(data0.toString()))// 关闭
				{
					holder.itemsAlertBtn
							.setImageResource(R.drawable.switchbtn_closed);
					mUserDB.closeAlertStation(data0.toString());
					Toast.makeText(mContext, "close " + stationName, 1000)
							.show();

				} else {
					holder.itemsAlertBtn
							.setImageResource(R.drawable.switchbtn_open);
					mUserDB.openAlertStation(data0.toString());
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
