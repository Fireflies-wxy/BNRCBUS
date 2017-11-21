//package com.bnrc.multipallist;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import com.bnrc.busapp.R;
//import com.bnrc.ui.rtBus.Child;
//import com.bnrc.util.UserDataDBHelper;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.TextView;
//
//public class DemoAdapter extends BaseAdapter {
//
//	private Context context = null;
//
//	private List<Child> datas = null;
//
//	private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();
//
//	public DemoAdapter(Context context, List<Child> datas) {
//		this.datas = datas;
//		this.context = context;
//		configCheckMap(false);
//	}
//
//	public void configCheckMap(boolean bool) {
//
//		for (int i = 0; i < datas.size(); i++) {
//			isCheckMap.put(i, bool);
//		}
//
//	}
//
//	@Override
//	public int getCount() {
//		return datas == null ? 0 : datas.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return datas.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return 0;
//	}
//
//	@Override
//	public View getView(final int position, View convertView, ViewGroup parent) {
//		ViewHolder holder = null;
//		if (convertView == null) {
//			convertView = LayoutInflater.from(context).inflate(R.layout.multi_list_item_layout, null);
//			holder = new ViewHolder();
//			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
//			holder.cbCheck = (CheckBox) convertView.findViewById(R.id.cbCheckBox);
//			convertView.setTag(holder);
//
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		Child child = datas.get(position);
//		holder.tvTitle.setText(child.getLineName());
//		holder.cbCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				isCheckMap.put(position, isChecked);
//			}
//		});
//		boolean isAlert = isCheckMap.get(position);
//		if (!isAlert) {
//			// cbCheck.setVisibility(View.GONE);
//			holder.cbCheck.setChecked(false);
//			isCheckMap.put(position, false);
//		} else {
//			// cbCheck.setVisibility(View.VISIBLE);
//			if (isCheckMap.get(position) == null) {
//				isCheckMap.put(position, true);
//			}
//			holder.cbCheck.setChecked(true);
//		}
//		return convertView;
//	}
//
//	public void add(Child bean) {
//		this.datas.add(0, bean);
//		configCheckMap(false);
//	}
//
//	public void remove(int position) {
//		this.datas.remove(position);
//	}
//
//	public Map<Integer, Boolean> getCheckMap() {
//		return this.isCheckMap;
//	}
//
//	public static class ViewHolder {
//
//		public TextView tvTitle = null;
//
//		public CheckBox cbCheck = null;
//		public Object data = null;
//
//	}
//
//	public List<Child> getDatas() {
//		return datas;
//	}
//
//}
