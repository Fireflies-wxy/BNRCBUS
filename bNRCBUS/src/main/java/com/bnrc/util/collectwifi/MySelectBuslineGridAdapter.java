package com.bnrc.util.collectwifi;

import java.util.List;
import java.util.Map;

import com.bnrc.busapp.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MySelectBuslineGridAdapter extends BaseAdapter {

	private LayoutInflater mInflater = null;
	private List<Map<String, String>> data;
	private Context context;
	private Cursor cursor;

	public MySelectBuslineGridAdapter(Context context,
			List<Map<String, String>> data) {
		// 根据context上下文加载布局，这里的是Demo17Activity本身，即this
		this.mInflater = LayoutInflater.from(context);
		this.data = data;
		this.context = context;
	}

	// public MyGridAdapter(Context context, Cursor cursor) {
	// // 根据context上下文加载布局，这里的是Demo17Activity本身，即this
	// this.mInflater = LayoutInflater.from(context);
	// this.context = context;
	// this.cursor = cursor;
	// }

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// if (cursor == null)
		return DataGetView(arg0, convertView, arg2);
		// else
		// return cursorGetView(arg0, convertView, arg2);
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
		// return cursor == null ? data.size() : cursor.getCount();
		return data.size();
	}

	public static class MyHolder {
		public TextView station, endName, listRou, distance;
		public RelativeLayout rListLayout;
		private LinearLayout lLayout;
	}

	// private View cursorGetView(int arg0, View convertView, ViewGroup arg2) {
	// // TODO Auto-generated method stub
	// MyHolder holder = null;
	// // 如果缓存convertView为空，则需要创建View
	// if (convertView == null) {
	// holder = new MyHolder();
	// // 根据自定义的Item布局加载布局
	// convertView = mInflater.inflate(R.layout.grid_item_view, null);
	// holder.listRou = (TextView) convertView.findViewById(R.id.listRou);
	// holder.endName = (TextView) convertView.findViewById(R.id.endName);
	// holder.rListLayout = (RelativeLayout) convertView
	// .findViewById(R.id.rListLayout);
	// // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
	// convertView.setTag(holder);
	// } else {
	// holder = (MyHolder) convertView.getTag();
	// }
	// cursor.moveToPosition(arg0);
	// holder.listRou.setText(cursor.getString(cursor
	// .getColumnIndex("keyName")));
	// holder.endName.setText(cursor.getString(cursor
	// .getColumnIndex("endName")));
	// TypedArray icons = context.getResources().obtainTypedArray(
	// R.array.color_select);
	// if (arg0 > 6)
	// arg0 %= 7;
	// holder.rListLayout.setBackgroundDrawable(icons.getDrawable(arg0));
	//
	// return convertView;
	//
	// }

	private View DataGetView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		MyHolder holder = null;
		// 如果缓存convertView为空，则需要创建View
		if (convertView == null) {
			holder = new MyHolder();
			// 根据自定义的Item布局加载布局
			convertView = mInflater.inflate(R.layout.grid_item_dialog, null);
			holder.listRou = (TextView) convertView.findViewById(R.id.listRou);
			holder.station = (TextView) convertView.findViewById(R.id.station);
			holder.endName = (TextView) convertView.findViewById(R.id.endName);
			holder.distance = (TextView) convertView
					.findViewById(R.id.distance);
			holder.rListLayout = (RelativeLayout) convertView
					.findViewById(R.id.rListLayout);
			holder.lLayout = (LinearLayout) convertView
					.findViewById(R.id.lLayout_griditemdialog_container);
			// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
			convertView.setTag(holder);
		} else {
			holder = (MyHolder) convertView.getTag();
		}

		holder.listRou.setText((String) data.get(arg0).get("线路"));

		holder.endName.setText((String) data.get(arg0).get("方向") == null ? ""
				: (String) data.get(arg0).get("方向"));
		if (data.get(arg0).get("车站") != null) {
			holder.station.setText((String) data.get(arg0).get("车站") + "站");
			holder.distance
					.setText("约" + (String) data.get(arg0).get("距离") == null ? ""
							: (String) data.get(arg0).get("距离") + "米");
		} else {
			holder.station.setText("车站未知");
			holder.distance.setText("距离");
		}
		TypedArray icons = context.getResources().obtainTypedArray(
				R.array.color_select);
		if (arg0 > 6)
			arg0 %= 7;
		holder.rListLayout.setBackgroundDrawable(icons.getDrawable(arg0));
		return convertView;
	}
}
