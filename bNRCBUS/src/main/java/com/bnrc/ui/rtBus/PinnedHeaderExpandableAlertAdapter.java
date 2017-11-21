//package com.bnrc.ui.rtBus;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.bnrc.busapp.BuslineListView;
//import com.bnrc.busapp.R;
//import com.bnrc.busapp.StationInformationView;
//import com.bnrc.busapp.StationListView;
//import com.bnrc.busapp.StationRoutView;
//import com.bnrc.ui.rjz.IPopWindowListener;
//import com.bnrc.ui.rjz.other.Bean;
//import com.bnrc.ui.rjz.other.RandomColor;
//import com.bnrc.ui.rjz.other.SearchBuslineView;
//import com.bnrc.ui.rjz.other.TextDrawable;
//import com.bnrc.ui.rtBus.PinnedHeaderExpandableConcernListView.HeaderAdapter;
//import com.bnrc.util.AnimationUtil;
//import com.bnrc.util.UserDataDBHelper;
//import com.bnrc.util.collectwifi.Constants;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.text.Html;
//import android.util.Log;
//import android.util.SparseIntArray;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class PinnedHeaderExpandableAlertAdapter extends
//		BaseExpandableListAdapter implements HeaderAdapter {
//	private static final String TAG = PinnedHeaderExpandableAlertAdapter.class
//			.getSimpleName();
//	private List<Group> groups;
//	private Context mContext;
//	private LayoutInflater inflater;
//	
//	public PinnedHeaderExpandableAlertAdapter(List<Group> groups,
//			Context context, PinnedHeaderExpandableConcernListView listView,
//			IPopWindowListener onSelectBtn, int subtitletype, int concernType) {
//		this.groups = groups;
//		this.mContext = context;
//		inflater = LayoutInflater.from(this.mContext);
//	}
//
//	@Override
//	public Object getChild(int groupPosition, int childPosition) {
//		// Log.i(TAG, "getChild ");
//		return groups.get(groupPosition).getChildItem(childPosition);
//	}
//
//	@Override
//	public long getChildId(int groupPosition, int childPosition) {
//		// Log.i(TAG, "getChildId ");
//		return childPosition;
//	}
//
//	@Override
//	public View getChildView(int groupPosition, int childPosition,
//			boolean isLastChild, View convertView, ViewGroup parent) {
//		Log.i(TAG, "getChildView ");
//
//		ChildViewHolder holder = null;
//		if (convertView == null) {
//			if (isLastChild)
//				convertView = inflater.inflate(R.layout.child_concern_bottom,
//						null);
//			else
//				convertView = inflater.inflate(R.layout.child_concern, null);
//			holder = new ChildViewHolder();
//			holder.buslineName = (TextView) convertView
//					.findViewById(R.id.tv_buslineName);
//			holder.destination = (TextView) convertView
//					.findViewById(R.id.tv_destination);
//			holder.rtInfo = (TextView) convertView.findViewById(R.id.tv_info);
//			holder.rLayout = (RelativeLayout) convertView
//					.findViewById(R.id.rLayout);
//			holder.concernStar = (ImageView) convertView
//					.findViewById(R.id.iv_concern);
//			convertView.setTag(holder);
//
//		} else {
//			holder = (ChildViewHolder) convertView.getTag();
//		}
//		final Child child = groups.get(groupPosition).getChildItem(
//				childPosition);
//		Log.i(TAG, "getChildView " + child.getLineName());
//
//		if (child != null) {
//
//			// int type = -1;
//			// if ((type = UserDataDBHelper.getInstance(mContext)
//			// .IsWhichKindFavInfo(child.getBuslineSN())) != -1) {
//			// child.setType(type + "");
//			// if (mSubTextType == 1 || mConcernType == -1) // 0 首页查看详细 1 到这里去
//			// {
//			// holder.concernStar.setImageResource(R.drawable.select_star);
//			// } else {
//			// if (type == mConcernType)
//			// holder.concernStar
//			// .setImageResource(R.drawable.select_star);
//			// else
//			// holder.concernStar
//			// .setImageResource(R.drawable.not_select_star);
//			// }
//			// child.setType(type + "");
//			// } else {
//			// child.setType(null);
//			// holder.concernStar.setImageResource(R.drawable.not_select_star);
//			// }
//
//			// if (mSubTextType == 1 || mConcernType == -1) // 0 首页查看详细 1 到这里去
//			// holder.concernStar.setImageResource(R.drawable.select_star);
//			// else {
//			// if (type == mConcernType)
//			// holder.concernStar.setImageResource(R.drawable.select_star);
//			// else
//			// holder.concernStar
//			// .setImageResource(R.drawable.not_select_star);
//			// }
//
//			holder.buslineName.setText(child.getLineName());
//			holder.destination.setText(child.getEndStation());
//
//			if (child.getRtInfo() != null) {
//
//				// waitStation.setText(Html.fromHtml(child.getRtInfo()
//				// .get("itemsTitle").toString()));
//				holder.rtInfo.setText(Html.fromHtml((child.getRtInfo().get(
//						"itemsText").toString())));
//			} else
//				holder.rtInfo.setText(Html.fromHtml(("暂无").toString()));
//			holder.rLayout.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					// 实例化SelectPicPopupWindow
//					// Map<String, String> popInfo = new HashMap<String,
//					// String>();
//					// popInfo.put("buslineSN", child.getBuslineSN());
//					// popInfo.put("buslineKeyName", child.getBuslineKeyName());
//					// popInfo.put("stationName", child.getStationName());
//					// popInfo.put("startStation", child.getStartStation());
//					// popInfo.put("endStation", child.getEndStation());
//					// popInfo.put("startTime", child.getStartTime());
//					// popInfo.put("endTime", child.getEndTime());
//					// popInfo.put("GY", child.getLatitide());
//					// popInfo.put("GX", child.getLongitude());
//					// popInfo.put("AZ", child.getAZ());
//					// popInfo.put("Type", child.getType());
//					// Child newchild = new Child(child.getBuslineId(), child
//					// .getBuslineKeyName(), child.getBuslineFullName(),
//					// child.getStationName(), child.getLatitide(), child
//					// .getLongitude(), child.getEndStation());
//					// newchild.setAZ(child.getAZ());
//					// newchild.setBuslineSN(child.getBuslineSN());
//					// newchild.setEndTime(child.getEndTime());
//					// newchild.setStartTime(child.getStartTime());
//					// newchild.setType(child.getType());
//					// newchild.set
//					// Child newchild = child.clone();
//					mOnSelectBtn.onPopClick(child);
//				}
//			});
//
//			convertView.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Intent buslineIntent = new Intent(mContext,
//							BuslineListView.class);
//					buslineIntent.putExtra("buslineKeyName",
//							child.getLineName());
//					buslineIntent.putExtra("buslineID", child.getLineID());
//					buslineIntent.putExtra("buslineFullName",
//							child.getLineFullName());
//					buslineIntent.putExtra("startStation",
//							child.getStartStation());
//					buslineIntent.putExtra("endStation", child.getEndStation());
//					buslineIntent.putExtra("startTime", child.getStartTime());
//					buslineIntent.putExtra("endTime", child.getEndTime());
//					buslineIntent.putExtra("stationName",
//							child.getStationName());
//					// buslineIntent.putExtra("buslineSN",
//					// child.getBuslineSN());
//					// if (child.getRtInfo() != null) {
//					// buslineIntent.putExtra("firstBus", child.getRtInfo()
//					// .get("firstBus").toString());
//					// buslineIntent.putExtra("secondBus", child.getRtInfo()
//					// .get("secondBus").toString());
//					// }
//					buslineIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//					mContext.startActivity(buslineIntent);
//					Log.i("AnimationUtil",
//							"activityZoomAnimation: " + mContext.getClass());
//					AnimationUtil.activityZoomAnimation(mContext);
//
//				}
//			});
//		}
//		return convertView;
//	}
//
//	@Override
//	public int getChildrenCount(int groupPosition) {
//		// Log.i(TAG, "getChildrenCount ");
//
//		Log.i(TAG, "groupPosition: " + groupPosition);
//		if (groupPosition >= 0)
//			return groups.get(groupPosition).getChildrenCount();
//		else
//			return -1;
//
//	}
//
//	@Override
//	public Object getGroup(int groupPosition) {
//		// Log.i(TAG, "getGroup ");
//		return groups.get(groupPosition);
//	}
//
//	@Override
//	public int getGroupCount() {
//		// Log.i(TAG, "getGroupCount " + groups.size());
//		return groups.size();
//	}
//
//	@Override
//	public long getGroupId(int groupPosition) {
//		// Log.i(TAG, "getGroupId ");
//		return groupPosition;
//	}
//
//	@Override
//	public View getGroupView(int groupPosition, boolean isExpanded,
//			View convertView, ViewGroup parent) {
//		// Log.i(TAG, "getGroupView ");
//		GroupViewHolder holder;
//		if (convertView == null) {
//			convertView = inflater.inflate(R.layout.group_concern, null);
//			holder = new GroupViewHolder();
//			holder.icon = (ImageView) convertView.findViewById(R.id.groupIcon);
//			holder.text = (TextView) convertView.findViewById(R.id.tv_arrive);
//			holder.title = (TextView) convertView
//					.findViewById(R.id.tv_rtstation);
//			holder.mStationSeq = (ImageView) convertView
//					.findViewById(R.id.iv_stationSeq);
//			convertView.setTag(holder);
//		} else {
//			// view = createGroupView();
//			holder = (GroupViewHolder) convertView.getTag();
//		}
//		if (isExpanded) {
//			holder.icon.setImageResource(R.drawable.down_arrow);
//		} else {
//			holder.icon.setImageResource(R.drawable.right_arrow);
//		}
//		final Group group = groups.get(groupPosition);
//		holder.title.setText(group.getStationName());
//		Bean bean = new Bean(getSequence(groupPosition).toString());
//		holder.text.setVisibility(View.VISIBLE);
//		switch (mSubTextType) {
//		case 0:
//			holder.text.setText("查看详细");
//			holder.text.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Intent stationIntent = new Intent(mContext,
//							StationListView.class);
//
//					stationIntent.putExtra("stationName",
//							group.getStationName());
//					stationIntent.putExtra("latitude", group.getLatitide());
//					stationIntent.putExtra("longitude", group.getLongitude());
//					stationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					mContext.startActivity(stationIntent);
//					AnimationUtil.activityZoomAnimation(mContext);
//
//				}
//			});
//			holder.mStationSeq.setVisibility(View.GONE);
//
//			break;
//		case 1:
//			holder.text.setText("到这里去");
//			holder.text.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Toast.makeText(mContext, "到这里去", 1000).show();
//					if (isNetworkConnected(mContext)) {
//						Intent intent = new Intent(mContext,
//								StationRoutView.class);
//						intent.putExtra("title", group.getStationName());
//						intent.putExtra("latitude", group.getLatitide());
//						intent.putExtra("longitude", group.getLongitude());
//						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						mContext.startActivity(intent);
//						AnimationUtil.activityZoomAnimation(mContext);
//
//					} else {
//						Toast toast = Toast.makeText(mContext, "您的网络有问题，请检查~",
//								Toast.LENGTH_LONG);
//						toast.show();
//					}
//				}
//			});
//			holder.mStationSeq.setImageDrawable(TextDrawable.builder()
//					.buildRound(bean.name,
//							mColor.getColor(bean.name, groupPosition)));
//			holder.mStationSeq.setVisibility(View.VISIBLE);
//
//			break;
//		default:
//			break;
//		}
//
//		return convertView;
//	}
//
//	public boolean isNetworkConnected(Context ctx) {
//		if (ctx != null) {
//			ConnectivityManager mConnectivityManager = (ConnectivityManager) ctx
//					.getSystemService(Context.CONNECTIVITY_SERVICE);
//			NetworkInfo mNetworkInfo = mConnectivityManager
//					.getActiveNetworkInfo();
//			if (mNetworkInfo != null) {
//				return mNetworkInfo.isAvailable();
//			}
//		}
//		return false;
//	}
//
//	@Override
//	public boolean hasStableIds() {
//		// Log.i(TAG, "hasStableIds ");
//		return true;
//	}
//
//	@Override
//	public boolean isChildSelectable(int groupPosition, int childPosition) {
//		// Log.i(TAG, "isChildSelectable ");
//		return true;
//	}
//
//	private View createChildrenView() {
//		// Log.i(TAG, "createChildrenView ");
//		return inflater.inflate(R.layout.child_concern, null);
//	}
//
//	private View createChildrenViewBottom() {
//		Log.i(TAG, "createChildrenView ");
//		return inflater.inflate(R.layout.child_concern_bottom, null);
//	}
//
//	private View createGroupView() {
//		Log.i(TAG, "createGroupView ");
//		return inflater.inflate(R.layout.group_concern, null);
//	}
//
//	class GroupViewHolder {
//		ImageView icon;
//		TextView text;
//		ImageView mStationSeq;
//		TextView title;
//	}
//
//	class ChildViewHolder {
//		TextView buslineName;
//		TextView destination;
//		TextView rtInfo;
//		RelativeLayout rLayout;
//		ImageView concernStar;
//	}
//
//	@Override
//	public int getHeaderState(int groupPosition, int childPosition) {
//		final int childCount = getChildrenCount(groupPosition);
//		if (childPosition == childCount - 1) {
//			return PINNED_HEADER_PUSHED_UP;
//		} else if (childPosition == -1
//				&& !listView.isGroupExpanded(groupPosition)) {
//			return PINNED_HEADER_GONE;
//		} else {
//			return PINNED_HEADER_VISIBLE;
//		}
//	}
//
//	@Override
//	public void configureHeader(View header, int groupPosition,
//			int childPosition, int alpha) {
//		// Log.i(TAG, "configureHeader ");
//		String groupData = groups.get(groupPosition).getStationName();
//		TextView mRTStation = ((TextView) header
//				.findViewById(R.id.tv_rtstation));
//		TextView mDetailInfo = ((TextView) header.findViewById(R.id.tv_arrive));
//		ImageView mStationSeq = ((ImageView) header
//				.findViewById(R.id.iv_stationSeq));
//		ImageView mStationIcon = ((ImageView) header
//				.findViewById(R.id.groupIcon));
//		mRTStation.setText(groupData);
//		mStationIcon.setImageResource(R.drawable.down_arrow);
//		switch (mSubTextType) {
//		case 0:
//			mDetailInfo.setText("查看详细");
//			mStationSeq.setVisibility(View.GONE);
//			// mStationIcon.setImageResource(R.drawable.down_arrow);
//			break;
//		case 1:
//			mDetailInfo.setText("到这里去");
//
//			Bean bean = new Bean(getSequence(groupPosition).toString());
//			mStationSeq.setImageDrawable(TextDrawable.builder().buildRound(
//					bean.name, mColor.getColor(bean.name, groupPosition)));
//			mStationSeq.setVisibility(View.VISIBLE);
//
//			break;
//		default:
//			break;
//		}
//
//	}
//
//	private SparseIntArray groupStatusMap = new SparseIntArray();
//
//	@Override
//	public void setGroupClickStatus(int groupPosition, int status) {
//		// Log.i(TAG, "setGroupClickStatus ");
//		groupStatusMap.put(groupPosition, status);
//	}
//
//	@Override
//	public int getGroupClickStatus(int groupPosition) {
//		// Log.i(TAG, "getGroupClickStatus ");
//		if (groupStatusMap.keyAt(groupPosition) >= 0) {
//			return groupStatusMap.get(groupPosition);
//		} else {
//			return 0;
//		}
//	}
//
//	private String formatString(int RID, String info) {
//		return String.format(mContext.getString(RID), info);
//	}
//
//	private String getSequence(int position) {
//		return String.valueOf((char) (position + 65));
//	}
//
//	public void refresh() {
//		this.notifyDataSetChanged();
//	}
//}
