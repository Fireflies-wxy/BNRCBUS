package com.bnrc.adapter;

import java.util.List;
import java.util.Map;

import u.aly.co;

import com.bnrc.busapp.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Copyright (c) 2011 All rights reserved ���ƣ�MyListViewAdapter
 * ������ListView�Զ���Adapter����
 * 
 * @author zhaoqp
 * @date 2011-11-8
 * @version
 */
public class MyBusListViewAdapter extends BaseAdapter {

	private Context mContext;
	// ���еĲ���
	private int mResource;
	// �б�չ�ֵ�����
	private List<? extends Map<String, ?>> mData;
	// Map�е�key
	private String[] mFrom;
	// view��id
	private int[] mTo;

	/**
	 * ���췽��
	 * 
	 * @param context
	 * @param data
	 *            �б�չ�ֵ�����
	 * @param resource
	 *            ���еĲ���
	 * @param from
	 *            Map�е�key
	 * @param to
	 *            view��id
	 */
	public MyBusListViewAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		mContext = context;
		mData = data;
		mResource = resource;
		mFrom = from;
		mTo = to;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			// ʹ���Զ����list_items��ΪLayout
			convertView = LayoutInflater.from(mContext).inflate(mResource,
					parent, false);
			// ʹ�ü���findView�Ĵ���
			holder = new ViewHolder();
			holder.itemsContainer = ((LinearLayout) convertView
					.findViewById(mTo[0]));
			holder.itemsTitle = ((TextView) convertView.findViewById(mTo[1]));
			holder.busIcon = ((ImageView) convertView.findViewById(mTo[3]));
			holder.imagName = ((ImageView) convertView.findViewById(mTo[4]));
			holder.buslineIcon = ((ImageView) convertView
					.findViewById(R.id.buslineIcon));
			// ���ñ��
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// ��������
		final Map<String, ?> dataSet = mData.get(position);
		if (dataSet == null) {
			return null;
		}
		// ��ȡ��������
		final Object data0 = dataSet.get(mFrom[0]);
		final Object data1 = dataSet.get(mFrom[1]);
		final Object data2 = dataSet.get(mFrom[2]);
		final Object data3 = dataSet.get(mFrom[3]);
		final Object data4 = dataSet.get("isalert");
		final Object data5 = dataSet.get("buslineIcon");
		final Object data6 = dataSet.get("isFav");
		final Object data7 = dataSet.get("isCurStation");
		holder.itemsTitle.setText(data1.toString());

		holder.busIcon.setBackgroundResource((Integer) data3);
		holder.buslineIcon.setImageResource((Integer) data5);
		holder.imagName.setVisibility(View.INVISIBLE);
		holder.itemsContainer.setBackgroundColor(Color.parseColor("#dd00b2ae"));
		if (data6.toString().equalsIgnoreCase("Yes") == true) {
			holder.imagName.setVisibility(View.VISIBLE);
			holder.imagName.setImageResource(R.drawable.favimgbg2);
		} else if (data4.toString().equalsIgnoreCase("Yes") == true) {
			holder.imagName.setVisibility(View.VISIBLE);
			holder.imagName.setImageResource(R.drawable.alertimgbg2);
		} else {
			holder.imagName.setVisibility(View.INVISIBLE);
		}

		if (data7.toString().equalsIgnoreCase("Yes") == true) {
			holder.itemsContainer.setBackgroundColor(Color
					.parseColor("#ff9234"));

		}

		// holder.itemsIcon.setVisibility(View.INVISIBLE);
		return convertView;
	}

	/**
	 * ViewHolder��
	 */
	static class ViewHolder {
		ImageView buslineIcon;
		LinearLayout itemsContainer;
		TextView itemsTitle;

		ImageView busIcon;
		ImageView imagName;
	}
}