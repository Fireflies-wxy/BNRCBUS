package com.bnrc.ui.rtBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bnrc.busapp.BuslineListView;
import com.bnrc.busapp.R;
import com.bnrc.busapp.StationListView;
import com.bnrc.ui.rjz.IPopWindowListener;
import com.bnrc.ui.rtBus.PinnedHeaderExpandableConcernAdapter.ChildViewHolder;
import com.bnrc.ui.rtBus.PinnedHeaderExpandableConcernAdapter.GroupViewHolder;
import com.bnrc.ui.rtBus.PinnedHeaderExpandableLocListView.HeaderAdapter;
import com.bnrc.util.AnimationUtil;
import com.bnrc.util.LocationUtil;
import com.bnrc.util.UserDataDBHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PinnedHeaderExpandableLocAdapter extends BaseExpandableListAdapter
		implements HeaderAdapter {
	private static final String TAG = PinnedHeaderExpandableLocAdapter.class
			.getSimpleName();
	private ArrayList<Group> groups;
	private Context mContext;
	private PinnedHeaderExpandableLocListView listView;
	private LayoutInflater inflater;
	private LocationUtil mLocationUtil;
	private IPopWindowListener mOnSelectBtn;
	private BDLocation mBdLocation;

	public PinnedHeaderExpandableLocAdapter(ArrayList<Group> groups,
			Context context, PinnedHeaderExpandableLocListView listView,
			IPopWindowListener onSelectBtn) {
		this.groups = groups;
		this.mContext = context;
		this.listView = listView;
		inflater = LayoutInflater.from(this.mContext);
		mLocationUtil = LocationUtil.getInstance(context);
		this.mOnSelectBtn = onSelectBtn;

	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// Log.i(TAG, "getChild ");
		return groups.get(groupPosition).getChildItem(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// Log.i(TAG, "getChildId ");
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		Log.i(TAG, "getChildView ");
		ChildViewHolder holder = null;
		if (convertView == null) {
			if (isLastChild)
				convertView = inflater.inflate(R.layout.child_loc_bottom, null);
			else
				convertView = inflater.inflate(R.layout.child_loc, null);
			holder = new ChildViewHolder();
			holder.buslineName = (TextView) convertView
					.findViewById(R.id.tv_buslineName);
			holder.destination = (TextView) convertView
					.findViewById(R.id.tv_destination);
			holder.rtInfo = (TextView) convertView.findViewById(R.id.tv_info);
			holder.rLayout = (RelativeLayout) convertView
					.findViewById(R.id.rLayout);
			holder.concernStar = (ImageView) convertView
					.findViewById(R.id.iv_concern);
			convertView.setTag(holder);

		} else {
			holder = (ChildViewHolder) convertView.getTag();
		}

		final Child child = groups.get(groupPosition).getChildItem(
				childPosition);

		if (child != null) {
			int type = -1;
			// if ((type = UserDataDBHelper.getInstance(mContext)
			// .IsWhichKindFavInfo(child.getBuslineSN())) != -1) {
			//
			// child.setType(type + "");
			// holder.concernStar.setImageResource(R.drawable.select_star);
			// } else {
			// child.setType(null);
			// holder.concernStar.setImageResource(R.drawable.not_select_star);
			// }
			// child.setType(UserDataDBHelper.getInstance(context)
			// .IsWhichKindFavInfo(child.getBuslineSN()) + "");

			holder.buslineName.setText(child.getLineName());
			holder.destination.setText(child.getEndStation());

			if (child.getRtInfo() != null) {
				Log.i(TAG,
						child.getLineName() + " "
								+ child.getRtInfo().get("itemsText"));
				holder.rtInfo.setText(Html.fromHtml((child.getRtInfo().get(
						"itemsText").toString())));
			} else {
				Log.i(TAG, child.getLineName() + "null");
				holder.rtInfo.setText(Html.fromHtml(("暂无")));
			}

			holder.rLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// 实例化SelectPicPopupWindow
					// Map<String, String> popInfo = new HashMap<String,
					// String>();
					// popInfo.put("buslineSN", child.getBuslineSN());
					// popInfo.put("buslineKeyName", child.getBuslineKeyName());
					// popInfo.put("stationName", child.getStationName());
					// popInfo.put("startStation", child.getStartStation());
					// popInfo.put("endStation", child.getEndStation());
					// popInfo.put("startTime", child.getStartTime());
					// popInfo.put("endTime", child.getEndTime());
					// popInfo.put("GY", child.getLatitide());
					//
					// popInfo.put("GX", child.getLongitude());
					//
					// popInfo.put("AZ", child.getAZ());
					// popInfo.put("Type", child.getType());
					// Child newchild = child.clone();
					mOnSelectBtn.onPopClick(child);
					// PinnedHeaderExpandableLocAdapter.this.notifyDataSetChanged();

				}
			});
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent buslineIntent = new Intent(mContext,
							BuslineListView.class);
					buslineIntent.putExtra("buslineKeyName",
							child.getLineName());
					buslineIntent.putExtra("buslineID", child.getLineID());
					buslineIntent.putExtra("buslineFullName",
							child.getLineFullName());
					buslineIntent.putExtra("startStation",
							child.getStartStation());
					buslineIntent.putExtra("endStation", child.getEndStation());
					buslineIntent.putExtra("startTime", child.getStartTime());
					buslineIntent.putExtra("endTime", child.getEndTime());
					buslineIntent.putExtra("stationName",
							child.getStationName());
					// buslineIntent.putExtra("buslineSN",
					// child.getBuslineSN());
					// if (child.getRtInfo() != null) {
					// buslineIntent.putExtra("firstBus", child.getRtInfo()
					// .get("firstBus").toString());
					// buslineIntent.putExtra("secondBus", child.getRtInfo()
					// .get("secondBus").toString());
					// }
					buslineIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(buslineIntent);
					AnimationUtil.activityZoomAnimation((Activity) mContext);

				}
			});
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// Log.i(TAG, "getChildrenCount ");

		// Log.i(TAG, "groupPosition: " + groupPosition);
		if (groupPosition >= 0)
			return groups.get(groupPosition).getChildrenCount();
		else
			return -1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// Log.i(TAG, "getGroup ");
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// Log.i(TAG, "getGroupCount " + groups.size());
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// Log.i(TAG, "getGroupId ");
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		Log.i(TAG, "getGroupView " + groups.get(groupPosition).getStationName());
		GroupViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.group_concern, null);
			holder = new GroupViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.groupIcon);
			holder.distance = (TextView) convertView
					.findViewById(R.id.tv_arrive);
			holder.rtStation = (TextView) convertView
					.findViewById(R.id.tv_rtstation);
			convertView.setTag(holder);
		} else {
			// view = createGroupView();
			holder = (GroupViewHolder) convertView.getTag();
		}

		if (isExpanded) {
			holder.icon.setImageResource(R.drawable.loc_icon);
		} else {
			holder.icon.setImageResource(R.drawable.loc_icon);
		}

		holder.rtStation.setText(groups.get(groupPosition).getStationName());

		mBdLocation = mLocationUtil.getmLocation();
		LatLng stationLoc = new LatLng(groups.get(groupPosition).getLatitide(),
				groups.get(groupPosition).getLongitude());
		LatLng myLoc = null;
		if (mBdLocation != null)
			myLoc = new LatLng(mBdLocation.getLatitude(),
					mBdLocation.getLongitude());
		double dis = mLocationUtil.getDistanceWithLocations(myLoc, stationLoc);
		if (dis == Double.MAX_VALUE)
			holder.distance.setText(formatString(R.string.distance, "暂无定位信息"));
		else
			holder.distance.setText(formatString(R.string.distance, dis + ""));
		final Group group = groups.get(groupPosition);
		holder.distance.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent stationIntent = new Intent(mContext,
						StationListView.class);

				stationIntent.putExtra("stationName", group.getStationName());
				stationIntent.putExtra("latitude", group.getLatitide());
				stationIntent.putExtra("longitude", group.getLongitude());
				stationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(stationIntent);
				AnimationUtil.activityZoomAnimation(mContext);

			}
		});
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// Log.i(TAG, "hasStableIds ");
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// Log.i(TAG, "isChildSelectable ");
		return true;
	}

	class GroupViewHolder {
		ImageView icon;
		TextView rtStation;
		TextView distance;
	}

	class ChildViewHolder {
		TextView buslineName;
		TextView destination;
		TextView rtInfo;
		RelativeLayout rLayout;
		ImageView concernStar;
	}

	@Override
	public int getHeaderState(int groupPosition, int childPosition) {
		final int childCount = getChildrenCount(groupPosition);
		if (childPosition == childCount - 1) {
			return PINNED_HEADER_PUSHED_UP;
		} else if (childPosition == -1
				&& !listView.isGroupExpanded(groupPosition)) {
			return PINNED_HEADER_GONE;
		} else {
			return PINNED_HEADER_VISIBLE;
		}
	}

	@Override
	public void configureHeader(View header, int groupPosition,
			int childPosition, int alpha) {
		// Log.i(TAG, "configureHeader ");
		String groupData = groups.get(groupPosition).getStationName();
		BDLocation bdLoc = mLocationUtil.getmLocation();
		LatLng stationLoc = new LatLng(groups.get(groupPosition).getLatitide(),
				groups.get(groupPosition).getLongitude());
		LatLng myLoc = new LatLng(bdLoc.getLatitude(), bdLoc.getLongitude());
		int distance = (int) DistanceUtil.getDistance(stationLoc, myLoc);
		((TextView) header.findViewById(R.id.tv_rtstation)).setText(groupData);
		((TextView) header.findViewById(R.id.tv_distance))
				.setText(formatString(R.string.distance, distance + ""));
	}

	private SparseIntArray groupStatusMap = new SparseIntArray();

	@Override
	public void setGroupClickStatus(int groupPosition, int status) {
		// Log.i(TAG, "setGroupClickStatus ");
		groupStatusMap.put(groupPosition, status);
	}

	@Override
	public int getGroupClickStatus(int groupPosition) {
		// Log.i(TAG, "getGroupClickStatus ");
		if (groupStatusMap.keyAt(groupPosition) >= 0) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 0;
		}
	}

	private String formatString(int RID, String info) {
		return String.format(mContext.getString(RID), info);
	}

	public void refresh() {
		this.notifyDataSetChanged();
	}
}
