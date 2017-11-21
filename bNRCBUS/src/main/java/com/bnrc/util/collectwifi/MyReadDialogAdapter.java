package com.bnrc.util.collectwifi;

import java.util.List;
import java.util.Map;

import com.bnrc.busapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyReadDialogAdapter extends BaseAdapter {

	private LayoutInflater mInflater = null;
	private List<Map<String, String>> data;

	public MyReadDialogAdapter(Context context, List<Map<String, String>> data) {
		// 根据context上下文加载布局，这里的是Demo17Activity本身，即this
		this.mInflater = LayoutInflater.from(context);
		this.data = data;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (data.get(0).size() == 9)
			return readView(arg0, convertView, arg2);
		else
			return readSureView(arg0, convertView, arg2);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	private class MyHolder {
		public TextView readLevel, readTime, readLat, readLng, readSSID,
				readMac, readType, readRadius, readSpeed, readSureRoute;
	}

	private View readView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		MyHolder holder = null;
		// 如果缓存convertView为空，则需要创建View
		if (convertView == null) {
			holder = new MyHolder();
			// 根据自定义的Item布局加载布局
			convertView = mInflater
					.inflate(R.layout.dialog_readwifi_list, null);
			holder.readTime = (TextView) convertView
					.findViewById(R.id.readTime);
			holder.readLat = (TextView) convertView.findViewById(R.id.readLat);
			holder.readLng = (TextView) convertView.findViewById(R.id.readLng);
			holder.readLevel = (TextView) convertView
					.findViewById(R.id.readLevel);
			holder.readMac = (TextView) convertView.findViewById(R.id.readMac);
			holder.readSSID = (TextView) convertView
					.findViewById(R.id.readSSID);
			holder.readType = (TextView) convertView
					.findViewById(R.id.readType);
			holder.readRadius = (TextView) convertView
					.findViewById(R.id.readRadius);
			holder.readSpeed = (TextView) convertView
					.findViewById(R.id.readSpeed);
			// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
			convertView.setTag(holder);
		} else {
			holder = (MyHolder) convertView.getTag();
		}
		holder.readLevel.setText((String) data.get(arg0).get("Level"));
		holder.readTime.setText((String) data.get(arg0).get("时间"));
		holder.readLat.setText((String) data.get(arg0).get("纬度"));
		holder.readLng.setText((String) data.get(arg0).get("经度"));

		holder.readSSID.setText((String) data.get(arg0).get("SSID"));
		holder.readMac.setText((String) data.get(arg0).get("MAC"));
		holder.readType.setText((String) data.get(arg0).get("LocType"));
		holder.readRadius.setText((String) data.get(arg0).get("LocRadius"));
		holder.readSpeed.setText((String) data.get(arg0).get("LocSpeed"));
		return convertView;
	}

	private View readSureView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		MyHolder holder = null;
		// 如果缓存convertView为空，则需要创建View
		if (convertView == null) {
			holder = new MyHolder();
			// 根据自定义的Item布局加载布局
			convertView = mInflater.inflate(R.layout.dialog_readwifi_sure_list,
					null);
			holder.readLevel = (TextView) convertView
					.findViewById(R.id.readsureID);
			holder.readMac = (TextView) convertView
					.findViewById(R.id.readsureMac);
			holder.readSSID = (TextView) convertView
					.findViewById(R.id.readsureSSID);
			holder.readSureRoute = (TextView) convertView
					.findViewById(R.id.readsureRoute);
			// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
			convertView.setTag(holder);
		} else {
			holder = (MyHolder) convertView.getTag();
		}
		holder.readLevel.setText((String) data.get(arg0).get("id"));
		holder.readSSID.setText((String) data.get(arg0).get("SSID"));
		holder.readMac.setText((String) data.get(arg0).get("MAC"));
		holder.readSureRoute.setText((String) data.get(arg0).get("ROUTE"));
		return convertView;
	}
}
