//package com.bnrc.ui.rjz;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.json.XML;
//
//import u.aly.aa;
//
//import com.ab.global.AbConstant;
//import com.baidu.location.BDLocation;
//import com.baidu.mapapi.model.LatLng;
//import com.bnrc.busapp.AbHorizontalScrollView;
//import com.bnrc.busapp.BuslineListView;
//import com.bnrc.busapp.MyCipher;
//import com.bnrc.busapp.MyFavoriteBuslineView;
//import com.bnrc.busapp.MyFavoriteStationView;
//import com.bnrc.busapp.NearbyStationListView;
//import com.bnrc.busapp.PollingService;
//import com.bnrc.busapp.PollingUtils;
//import com.bnrc.busapp.R;
//import com.bnrc.busapp.SearchBuslineView;
//import com.bnrc.busapp.SegmentedGroup;
//import com.bnrc.busapp.SettingView;
//import com.bnrc.network.MyVolley;
//import com.bnrc.network.StringRequest;
//import com.bnrc.network.toolbox.Response;
//import com.bnrc.network.toolbox.VolleyError;
//
//import com.bnrc.ui.rtBus.Child;
//import com.bnrc.ui.rtBus.Group;
//import com.bnrc.ui.rtBus.PinnedHeaderExpandableConcernAdapter;
//import com.bnrc.ui.rtBus.PinnedHeaderExpandableConcernListView;
//import com.bnrc.ui.rtBus.PinnedHeaderExpandableLocAdapter;
//import com.bnrc.ui.rtBus.PinnedHeaderExpandableLocListView;
//import com.bnrc.util.BuslineDBHelper;
//import com.bnrc.util.DataBaseHelper;
//import com.bnrc.util.LocationUtil;
//import com.bnrc.util.SlidingDrawerGridView;
//import com.bnrc.util.UserDataDBHelper;
//import com.bnrc.util.collectwifi.Constants;
//import com.bnrc.util.collectwifi.ScanService;
//import com.bnrc.util.collectwifi.ServiceUtils;
//import com.umeng.analytics.MobclickAgent;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.database.SQLException;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.text.Html;
//import android.util.Log;
//import android.view.Display;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.ExpandableListView;
//import android.widget.ImageView;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemLongClickListener;
//import android.widget.RadioGroup.OnCheckedChangeListener;
//
//public class ConcernFragment2 extends BaseFragment {
//	private static final String TAG = "NearFragment2";
//	private Context mContext;
//	public TextView curlocation = null;
//	private ImageView refesh = null;
//	public LocationUtil mApplication = null;
//
//	private PinnedHeaderExpandableConcernListView mConcernExplistview;
//	private PinnedHeaderExpandableConcernAdapter mConcernAdapter;
//	private PinnedHeaderExpandableLocListView mNearExplistview;
//	private PinnedHeaderExpandableLocAdapter mNearAdapter;
//	private RelativeLayout mNearHint, mConcernHint;
//	private ArrayList<Group> mConcernGroups;
//	private ArrayList<Group> mNearGroups;
//
//	private AbHorizontalScrollView mainScrollView;
//
//	private ImageView openAlertView;
//	private Handler mHandler;
//	private String isOpenAlert;
//	private SegmentedGroup segmented;
//
//	private static final int CONCERN_NOTIFY = 0;
//	private static final int NEAR_NOTIFY = 1;
//	private static final int REFRESH_HAS_NEAR = 2;
//	private static final int REFRESH_NO_NEAR = 3;
//	private ArrayList<ArrayList<String>> nearbyStations = null;
//	private ArrayList<ArrayList<String>> favStations = null;
//	private ArrayList<ArrayList<String>> favBuslines = null;
//	private ProgressDialog pd;
//	private IPopWindowListener mChooseListener;
//
//	// private NearDBRunnable mNearRunnable = new NearDBRunnable();
//	// private ConcernDBRunnable mConcernRunnable = new ConcernDBRunnable();
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		Log.i("BaseFragment", TAG + " onCreateView");
//
//		mContext = (Context) getActivity();
//		View view = LayoutInflater.from(mContext).inflate(
//				R.layout.zj_near_view, null);
//
//		mConcernExplistview = (PinnedHeaderExpandableConcernListView) view
//				.findViewById(R.id.explistview_all_concern);
//		mNearExplistview = (PinnedHeaderExpandableLocListView) view
//				.findViewById(R.id.explistview_near);
//		mNearHint = (RelativeLayout) view.findViewById(R.id.rLayout_near);
//		mConcernHint = (RelativeLayout) view
//				.findViewById(R.id.rLayout_all_concern);
//		mConcernGroups = new ArrayList<Group>();
//		Group initGroup1 = new Group("1", "2", "3");
//		mConcernGroups.add(initGroup1);
//		mNearGroups = new ArrayList<Group>();
//		Group initGroup2 = new Group("4", "5", "6");
//		mNearGroups.add(initGroup2);
//
//		// 设置悬浮头部VIEW
//		mConcernExplistview.setHeaderView(0,getActivity().getLayoutInflater()
//				.inflate(R.layout.group_head_concern, mConcernExplistview,
//						false));
//		mConcernAdapter = new PinnedHeaderExpandableConcernAdapter(
//				mConcernGroups, mContext.getApplicationContext(),
//				mConcernExplistview, mChooseListener,0,-1);
//		mConcernExplistview.setAdapter(mConcernAdapter);
//
//		// 设置悬浮头部VIEW
//		mNearExplistview.setHeaderView(getActivity().getLayoutInflater()
//				.inflate(R.layout.group_head_concern, mNearExplistview, false));
//		mNearAdapter = new PinnedHeaderExpandableLocAdapter(mNearGroups,
//				mContext.getApplicationContext(), mNearExplistview,
//				mChooseListener);
//		mNearExplistview.setAdapter(mNearAdapter);
//
//		isOpenAlert = "开启提醒功能";
//		MobclickAgent.updateOnlineConfig(mContext);
//		String value = MobclickAgent.getConfigParams(mContext,
//				"bus_data_version");
//		JSONObject jsonObj = null;
//		try {
//			jsonObj = new JSONObject(value);
//			String version = jsonObj.getString("version");
//			String ready = jsonObj.getString("ready");
//			SharedPreferences mySharedPreferences = mContext
//					.getSharedPreferences("setting", SettingView.MODE_PRIVATE);
//			String oldVersion = mySharedPreferences.getString(
//					"bus_data_version", "1");
//			if (ready.equalsIgnoreCase("YES")
//					&& (version.equalsIgnoreCase(oldVersion) == false)) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//				builder.setMessage("公交数据已经推出了新的版本，您是否要更新？").setTitle("友情提示")
//						.setNegativeButton("取消", null);
//				builder.setPositiveButton("确定",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int which) {
//								// DataBaseHelper.getInstance(mContext)
//								// .DownFileWithUrl(
//								// MobclickAgent.getConfigParams(
//								// mContext,
//								// "bus_data_url"));
//
//							}
//						});
//				builder.show();
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		value = MobclickAgent.getConfigParams(mContext,
//				"realtime_bus_data_version");
//		Log.i("realtime_bus_data_version", value);
//		try {
//			jsonObj = new JSONObject(value);
//			String version = jsonObj.getString("version");
//			String ready = jsonObj.getString("ready");
//			SharedPreferences mySharedPreferences = mContext
//					.getSharedPreferences("setting", SettingView.MODE_PRIVATE);
//			String oldVersion = mySharedPreferences.getString(
//					"realtime_bus_data_version", "1");
//			if (ready.equalsIgnoreCase("YES")
//					&& (version.equalsIgnoreCase(oldVersion) == false)) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//				builder.setMessage("实时公交数据有更新，您是否要更新？").setTitle("友情提示")
//						.setNegativeButton("取消", null);
//				builder.setPositiveButton("确定",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int which) {
//								// BuslineDBHelper
//								// .getInstance(mContext)
//								// .DownFileWithUrl(
//								// MobclickAgent
//								// .getConfigParams(
//								// mContext,
//								// "realtime_bus_data_url"));
//
//							}
//						});
//				builder.show();
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		mApplication = LocationUtil.getInstance(mContext);
//		mApplication.startLocation();
//
//		// initTitleRightLayout();
//
//		refesh = (ImageView) view.findViewById(R.id.refresh);
//		refesh.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Log.i(TAG, "NearFragment refresh: "
//						+ mApplication.addressString);
//				curlocation.setText(mApplication.addressString); // 构建一个下载进度条
//				pd = ProgressDialog.show(getActivity(), "数据加载中…", "请等待");
//				new NearThread(mNearGroups).start();
//
//			}
//		});
//
//		openAlertView = (ImageView) view.findViewById(R.id.open_alert_btn);
//		openAlertView.setBackgroundResource(R.drawable.alertoffimg);
//		openAlertView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if (isOpenAlert.equalsIgnoreCase("开启提醒功能")) {
//					isOpenAlert = "关闭提醒功能";
//					PollingUtils.startPollingService(mContext, 5,
//							PollingService.class, PollingService.ACTION);
//					openAlertView.setBackgroundResource(R.drawable.alertonimg);
//					Toast toast = Toast.makeText(
//							mContext.getApplicationContext(), "您已开启提醒功能~",
//							Toast.LENGTH_LONG);
//					toast.show();
//				} else {
//					isOpenAlert = "开启提醒功能";
//					PollingUtils.stopPollingService(mContext,
//							PollingService.class, PollingService.ACTION);
//					openAlertView.setBackgroundResource(R.drawable.alertoffimg);
//					Toast toast = Toast.makeText(
//							mContext.getApplicationContext(), "您已关闭提醒功能~",
//							Toast.LENGTH_LONG);
//					toast.show();
//				}
//			}
//		});
//
//		final int screenWidth;// 屏幕宽度
//		WindowManager windowManager = getActivity().getWindowManager();
//		Display display = windowManager.getDefaultDisplay();
//		screenWidth = display.getWidth();
//		mNearExplistview.setLayoutParams(new FrameLayout.LayoutParams(
//				screenWidth, FrameLayout.LayoutParams.MATCH_PARENT));
//		mConcernExplistview.setLayoutParams(new FrameLayout.LayoutParams(
//				screenWidth, FrameLayout.LayoutParams.MATCH_PARENT));
//
//		mainScrollView = (AbHorizontalScrollView) view
//				.findViewById(R.id.mainScrollView);
//		mainScrollView.setSmoothScrollingEnabled(false);
//
//		mApplication = LocationUtil.getInstance(mContext);
//		curlocation = (TextView) view.findViewById(R.id.curlocation);
//
//		DataBaseHelper.getInstance(mContext);
//
//		segmented = (SegmentedGroup) view.findViewById(R.id.segmentedGroup);
//
//		segmented.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(RadioGroup arg0, int arg1) {
//				// TODO Auto-generated method stub
//				switch (arg1) {
//				case R.id.radBtn_all:
//					mainScrollView.scrollTo(0, 0);
//					return;
//				case R.id.radBtn_work:
//					mainScrollView.scrollTo(screenWidth / 3, 0);
//					return;
//				case R.id.radBtn_home:
//					mainScrollView.scrollTo(screenWidth * 2 / 3, 0);
//					return;
//				}
//			}
//		});
//
//		mHandler = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//				switch (msg.what) {
//				case REFRESH_HAS_NEAR:
//					pd.dismiss();
//					// mNearExplistview.setVisibility(View.VISIBLE);
//					mNearHint.setVisibility(View.GONE);
//					mNearAdapter.notifyDataSetChanged();
//					break;
//				case REFRESH_NO_NEAR:
//					pd.dismiss();
//					mNearHint.setVisibility(View.VISIBLE);
//					mNearAdapter.notifyDataSetChanged();
//					break;
//				case CONCERN_NOTIFY:
//					Log.d(TAG, "mConcernAdapter.notifyDataSetChanged()");
//					mConcernHint.setVisibility(View.GONE);
//					for (Group group : mConcernGroups)
//						group.sortChild();
//					mConcernAdapter.notifyDataSetChanged();
//					break;
//				case NEAR_NOTIFY:
//					Log.d(TAG, "mNearAdapter.notifyDataSetChanged()");
//					mNearHint.setVisibility(View.GONE);
//
//					for (Group group : mNearGroups)
//						group.sortChild();
//					mNearAdapter.notifyDataSetChanged();
//					break;
//				default:
//					Log.d(TAG, "default.notifyDataSetChanged()");
//					for (Group group : mConcernGroups)
//						group.sortChild();
//					for (Group group : mNearGroups)
//						group.sortChild();
//					mNearAdapter.notifyDataSetChanged();
//					mConcernAdapter.notifyDataSetChanged();
//					break;
//				}
//
//			}
//		};
//		getFavStationsAndBuslines();
//		// getNearbyStationsAndBuslines();
//
//		return view;
//	}
//
//	public void refreshRealtimeBuses() {
//		// GetRtBuslineInfo(mConcernGroups, CONCERN_NOTIFY);
//		getFavStationsAndBuslines();
//		GetRtBuslineInfo(mNearGroups, NEAR_NOTIFY);
//	}
//
//	public void getNearbyStationsAndBuslines() {
//		mNearGroups.clear();
//		// 附近
//		LocationUtil mApplication = LocationUtil.getInstance(mContext);
//		;
//		BDLocation location = mApplication.mLocation;
//		nearbyStations = DataBaseHelper.getInstance(mContext)
//				.AcquireAroundStationsWithLocation(
//						new LatLng(location.getLatitude(), location
//								.getLongitude()));
//		if (nearbyStations.size() > 0) {
//			for (ArrayList<String> station : nearbyStations) {
//				int k = mNearGroups.size();
//				int i;
//				for (i = 0; i < k; i++) {
//					if (mNearGroups.get(i).getStationName().trim()
//							.equalsIgnoreCase(station.get(1).trim())) {
//						break;
//					}
//				}
//				if (k == 0 || k == i) {
//
//					Group group = new Group(station.get(1), station.get(2),
//							station.get(3));
//					mNearGroups.add(group);
//					ArrayList<ArrayList<String>> station_buslines = DataBaseHelper
//							.getInstance(mContext)
//							.getRtBusLinesWithStationName(station.get(1));
//					int size = station_buslines.size();
//					for (int j = 0; j < size; j++) {
//						Child child = new Child(station_buslines.get(j).get(0),
//								station_buslines.get(j).get(1),
//								station_buslines.get(j).get(2),
//								station_buslines.get(j).get(3),
//								station_buslines.get(j).get(4),
//								station_buslines.get(j).get(5),
//								station_buslines.get(j).get(6));
//						mNearGroups.get(i).addChildrenItem(child);
//					}
//				}
//				mNearHint.setVisibility(View.GONE);
//				GetRtBuslineInfo(mNearGroups, NEAR_NOTIFY);
//			}
//		} else {
//			Group initGroup = new Group("暂无信息", "暂无信息", "暂无信息");
//			mNearGroups.add(initGroup);
//			mNearHint.setVisibility(View.VISIBLE);
//			mNearAdapter.notifyDataSetChanged();
//		}
//	}
//
//	public void getFavStationsAndBuslines() {
//		Log.i(TAG, "getFavStationsAndBuslines");
//
//		// new Thread(new ConcernDBRunnable(mConcernGroups, favStations,
//		// favBuslines)).start();
//		favStations = UserDataDBHelper.getInstance(mContext).favStations;
//		favBuslines = UserDataDBHelper.getInstance(mContext).favBuslines;
//		mConcernGroups.clear();
//		if (favStations.size() > 0 || favBuslines.size() > 0) {
//			for (ArrayList<String> station : favStations) {
//				int k = mConcernGroups.size();
//				int i;
//				for (i = 0; i < k; i++) {
//					if (mConcernGroups.get(i).getStationName().trim()
//							.equalsIgnoreCase(station.get(0).trim())) {
//						break;
//					}
//
//				}
//				if (k == 0 || k == i) {
//					Group group = new Group(station.get(0), station.get(1),
//							station.get(2));
//					mConcernGroups.add(group);
//					// Log.i(TAG, "stationName: " + station.get(0));
//					ArrayList<ArrayList<String>> station_buslines = DataBaseHelper
//							.getInstance(mContext)
//							.getRtBusLinesWithStationName(station.get(0));
//					int size = station_buslines.size();
//					for (int j = 0; j < size; j++) {
//						Child child = new Child(station_buslines.get(j).get(0),
//								station_buslines.get(j).get(1),
//								station_buslines.get(j).get(2),
//								station_buslines.get(j).get(3),
//								station_buslines.get(j).get(4),
//								station_buslines.get(j).get(5),
//								station_buslines.get(j).get(6));
//						group.addChildrenItem(child);
//					}
//				}
//				// if (mConcernGroups.size() > 7) {
//				// break;
//				// // 想要从缓存中获取文件
//				// }
//			}
//			int preSize = mConcernGroups.size();
//			for (ArrayList<String> busline : favBuslines) {
//				int k = mConcernGroups.size();
//				int i;
//				for (i = 0; i < k; i++) {
//					if (mConcernGroups.get(i).getStationName().trim()
//							.equalsIgnoreCase(busline.get(3).trim())) {
//						Child child = new Child(busline.get(0), busline.get(1),
//								busline.get(2), busline.get(3), busline.get(4),
//								busline.get(5), busline.get(6));
//						mConcernGroups.get(i).addChildrenItemFront(child);
//					}
//				}
//				if (k == preSize || k == i) {
//					Group group = new Group(busline.get(3), busline.get(4),
//							busline.get(5));
//					mConcernGroups.add(group);
//				}
//
//			}
//			mConcernHint.setVisibility(View.GONE);
//			GetRtBuslineInfo(mConcernGroups, CONCERN_NOTIFY);
//		} else {
//
//			Group initGroup = new Group("暂无信息", "暂无信息", "暂无信息");
//			mConcernGroups.add(initGroup);
//			mConcernHint.setVisibility(View.VISIBLE);
//			mConcernAdapter.notifyDataSetChanged();
//		}
//	}
//
//	public class NearThread extends Thread {
//
//		private ArrayList<Group> mNearGroups;
//
//		public NearThread(ArrayList<Group> pNearGroups) {
//			this.mNearGroups = pNearGroups;
//		}
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			Log.w(TAG, "thread start");
//
//			mNearGroups.clear();
//			// 附近
//			LocationUtil mApplication = LocationUtil.getInstance(mContext);
//			;
//			BDLocation location = mApplication.mLocation;
//			nearbyStations = DataBaseHelper.getInstance(mContext)
//					.AcquireAroundStationsWithLocation(
//							new LatLng(location.getLatitude(), location
//									.getLongitude()));
//			if (nearbyStations.size() > 0) {
//				for (ArrayList<String> station : nearbyStations) {
//					int k = mNearGroups.size();
//					int i;
//					for (i = 0; i < k; i++) {
//						if (mNearGroups.get(i).getStationName().trim()
//								.equalsIgnoreCase(station.get(1).trim())) {
//							break;
//						}
//					}
//					if (k == 0 || k == i) {
//
//						Group group = new Group(station.get(1), station.get(2),
//								station.get(3));
//						mNearGroups.add(group);
//						ArrayList<ArrayList<String>> station_buslines = DataBaseHelper
//								.getInstance(mContext)
//								.getRtBusLinesWithStationName(station.get(1));
//						int size = station_buslines.size();
//						for (int j = 0; j < size; j++) {
//							Child child = new Child(station_buslines.get(j)
//									.get(0), station_buslines.get(j).get(1),
//									station_buslines.get(j).get(2),
//									station_buslines.get(j).get(3),
//									station_buslines.get(j).get(4),
//									station_buslines.get(j).get(5),
//									station_buslines.get(j).get(6));
//							mNearGroups.get(i).addChildrenItem(child);
//						}
//					}
//					// mNearHint.setVisibility(View.GONE);
//				}
//				Message msg = new Message();
//				msg.what = REFRESH_HAS_NEAR;
//				mHandler.sendMessage(msg);
//				Log.w(TAG, "thread has group");
//
//				GetRtBuslineInfo(mNearGroups, NEAR_NOTIFY);
//				Log.w(TAG, "thread GetRtBuslineInfo");
//			} else {
//				Log.w(TAG, "thread no group");
//				Group initGroup = new Group("暂无信息", "暂无信息", "暂无信息");
//				mNearGroups.add(initGroup);
//				Message msg = new Message();
//				msg.what = REFRESH_NO_NEAR;
//				mHandler.sendMessage(msg);
//			}
//
//		}
//
//	}
//
//	public void GetRtBuslineInfo(ArrayList<Group> groups, int sign) {
//		if (((MainActivity) getActivity()).isNetworkConnected()) {
//			Log.i(TAG, "GetRtBuslineInfo");
//
//			int j = groups.size();
//			// Log.i(TAG, "groups.size(): " + groups.size());
//			for (int i = 0; i < j; i++) {
//				ArrayList<Child> buslines = groups.get(i).getChildren();
//				// Log.i(TAG, "children.size(): " + buslines.size());
//
//				int size = buslines.size();
//				for (int k = 0; k < size; k++) {
//					Log.i(TAG, "NearFragment2: "
//							+ buslines.get(k).getBuslineFullName());
//					ArrayList<String> buslineArrayList = BuslineDBHelper
//							.getInstance(mContext)
//							.AcquireOffLineInfoWithBuslineName(
//									buslines.get(k).getBuslineFullName());
//					if (buslineArrayList.size() > 0) {
//						try {
//							Log.i(TAG, "有公交");
//							get_realtime_data(buslines.get(k), 1, sign);
//						} catch (UnsupportedEncodingException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//		}
//	}
//
//	public void get_realtime_data(final Child busline, int station_num,
//			final int sign) throws JSONException, UnsupportedEncodingException {
//		ArrayList<String> buslineArrayList = BuslineDBHelper.getInstance(
//				mContext).AcquireOffLineInfoWithBuslineName(
//				busline.getBuslineFullName());
//		if (buslineArrayList.size() > 0) {
//			String JSONDataUrl = "http://bjgj.aibang.com:8899/bus.php?city="
//					+ URLEncoder.encode("北京", "utf-8") + "&id="
//					+ buslineArrayList.get(3) + "&no=" + station_num
//					+ "&type=1&encrypt=1&versionid=2";
//			StringRequest jsonObjectRequest = new StringRequest(JSONDataUrl,
//					new Response.Listener<String>() {
//
//						@Override
//						public void onResponse(String response) {
//							JSONObject jsonObj = null;
//							try {
//								jsonObj = XML.toJSONObject(response);
//								JSONObject busJsonObject = ((JSONObject) (jsonObj
//										.getJSONObject("root")))
//										.getJSONObject("data");
//								JSONArray oj = busJsonObject
//										.getJSONArray("bus");
//								JSONArray busesArray = oj;
//								new Thread(new doRequestResult(busesArray,
//										busline, sign)).start();
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//
//						}
//
//					}, new Response.ErrorListener() {
//						@Override
//						public void onErrorResponse(VolleyError arg0) {
//						}
//					});
//			MyVolley.sharedVolley(mContext).getRequestQueue()
//					.add(jsonObjectRequest);
//
//		}
//	}
//
//	public class doRequestResult implements Runnable {
//		private JSONArray busesArray;
//		private Child curbusline;
//		private int sign;
//
//		public doRequestResult(JSONArray busesArray, Child busline, int sign) {
//			this.busesArray = busesArray;
//			this.curbusline = busline;
//			this.sign = sign;
//		}
//
//		public void run() {
//			try {
//				try {
//					// Log.i(TAG, busesArray.toString());
//					int j = busesArray.length();
//					int min = 0;
//					double distance = 0;
//					double mindistance = 1000000000.0;
//					int min2 = 0;
//					double mindistance2 = 1000000000.0;
//					LocationUtil locater = LocationUtil.getInstance(mContext);
//					String stationName = "";
//					for (int i = 0; i < j; i++) {
//
//						JSONObject jsonObject = (JSONObject) busesArray.get(i);
//						stationName = new MyCipher("aibang"
//								+ jsonObject.getString("gt"))
//								.decrypt(jsonObject.getString("ns"));
//						ArrayList<ArrayList<String>> stations = DataBaseHelper
//								.getInstance(mContext)
//								.getSpecificStationsWithBuslineName(
//										curbusline.getBuslineFullName());
//						long myNum = 0;
//						long curNum = 1000;
//						if (stations != null) {
//							int k = stations.size();
//							for (int m = 0; m < k; m++) {
//
//								ArrayList<String> arrayList = stations.get(m);
//
//								if (stationName.equalsIgnoreCase(arrayList
//										.get(0))) {
//									curNum = m;
//								}
//
//								if (curbusline.getStationName()
//										.equalsIgnoreCase(arrayList.get(0))) {
//									myNum = m;
//									break;
//								}
//							}
//						}
//
//						if (myNum - curNum >= 0) {
//							distance = myNum - curNum;
//							if (distance < mindistance) {
//								if (mindistance == 1000000000.0) {
//									mindistance = distance;
//									min = i;
//								} else {
//									mindistance2 = mindistance;
//									min2 = min;
//									mindistance = distance;
//									min = i;
//								}
//							}
//						}
//					}
//
//					ArrayList<ArrayList<String>> stations = DataBaseHelper
//							.getInstance(mContext).getSpecificStationsWithBuslineName(
//									curbusline.getBuslineFullName());
//					if ((min == min2) || (min2 == 1000)
//							|| (mindistance2 == mindistance)) {
//						JSONObject jsonObject = (JSONObject) busesArray
//								.get(min);
//
//						long myNum = 0;
//						long curNum = 1000;
//						int tag = 0;
//						double distanceTotal = 0.0;
//						if (stations != null) {
//							int k = stations.size();
//							for (int i = 0; i < k; i++) {
//
//								ArrayList<String> arrayList = stations.get(i);
//								if (tag == 1) {
//									distanceTotal += locater
//											.getDistanceWithLocations(
//													new LatLng(
//															Float.parseFloat(arrayList
//																	.get(1)),
//															Float.parseFloat(arrayList
//																	.get(2))),
//													new LatLng(
//															Float.parseFloat(stations
//																	.get(i - 1)
//																	.get(1)),
//															Float.parseFloat(stations
//																	.get(i - 1)
//																	.get(2))));
//								}
//
//								if (new MyCipher("aibang"
//										+ jsonObject.getString("gt")).decrypt(
//										jsonObject.getString("ns"))
//										.equalsIgnoreCase(arrayList.get(0))) {
//									curNum = i;
//									tag = 1;
//									distanceTotal = 0;
//								}
//
//								if (curbusline.getStationName()
//										.equalsIgnoreCase(arrayList.get(0))) {
//									myNum = i;
//									tag = 0;
//									break;
//								}
//							}
//						}
//
//						if (myNum - curNum >= 0) {
//							HashMap<String, Object> map = new HashMap<String, Object>();
//
//							map.put("itemsIcon", R.drawable.bus_img);
//							map.put("bus_title",
//									curbusline.getBuslineFullName().substring(
//											0,
//											curbusline.getBuslineFullName()
//													.indexOf("(")));
//							map.put("station_title",
//									curbusline.getStationName());
//							String nameString = curbusline.getBuslineFullName();
//							nameString = "开往</font> <font color=\"yellow\">"
//									+ nameString.substring(nameString
//											.indexOf("-") + 1);
//							nameString = nameString.substring(0,
//									nameString.length() - 1);
//
//							map.put("itemsTitle", nameString + "</font>");
//							if ((myNum - curNum) == 0) {
//								map.put("itemsText",
//										"</font> <font color=\"red\">即将到站</font> ");
//
//							} else {
//								map.put("itemsText", "<font color=\"black\">"
//										+ (myNum - curNum) + "</font> 站, "
//										+ (int) (distanceTotal / 100) / 10.0
//										+ " km");
//							}
//
//							map.put("distance", mindistance + "");
//							map.put("busline", curbusline);
//							curbusline.setRtInfo(map);
//						}
//					} else {
//						JSONObject jsonObject = (JSONObject) busesArray
//								.get(min);
//						JSONObject jsonObject2 = (JSONObject) busesArray
//								.get(min2);
//						long myNum = 0;
//						long curNum = 1000;
//						long curNum2 = 1000;
//						int tag = 0;
//						int tag2 = 0;
//						double distanceTotal = 0.0;
//						double distanceTotal2 = 0.0;
//						if (stations != null) {
//							int k = stations.size();
//							for (int i = 0; i < k; i++) {
//
//								ArrayList<String> arrayList = stations.get(i);
//								if (tag == 1) {
//									distanceTotal += locater
//											.getDistanceWithLocations(
//													new LatLng(
//															Float.parseFloat(arrayList
//																	.get(1)),
//															Float.parseFloat(arrayList
//																	.get(2))),
//													new LatLng(
//															Float.parseFloat(stations
//																	.get(i - 1)
//																	.get(1)),
//															Float.parseFloat(stations
//																	.get(i - 1)
//																	.get(2))));
//								}
//
//								if (tag2 == 1) {
//									distanceTotal2 += locater
//											.getDistanceWithLocations(
//													new LatLng(
//															Float.parseFloat(arrayList
//																	.get(1)),
//															Float.parseFloat(arrayList
//																	.get(2))),
//													new LatLng(
//															Float.parseFloat(stations
//																	.get(i - 1)
//																	.get(1)),
//															Float.parseFloat(stations
//																	.get(i - 1)
//																	.get(2))));
//								}
//
//								if (new MyCipher("aibang"
//										+ jsonObject.getString("gt")).decrypt(
//										jsonObject.getString("ns"))
//										.equalsIgnoreCase(arrayList.get(0))) {
//									curNum = i;
//									tag = 1;
//									distanceTotal = 0;
//								}
//
//								if (new MyCipher("aibang"
//										+ jsonObject2.getString("gt")).decrypt(
//										jsonObject2.getString("ns"))
//										.equalsIgnoreCase(arrayList.get(0))) {
//									curNum2 = i;
//									tag2 = 1;
//									distanceTotal2 = 0;
//								}
//
//								if (curbusline.getStationName()
//										.equalsIgnoreCase(arrayList.get(0))) {
//									myNum = i;
//									tag = 0;
//									break;
//								}
//							}
//						}
//
//						if (myNum - curNum >= 0) {
//							if (myNum - curNum2 >= 0) {
//								HashMap<String, Object> map = new HashMap<String, Object>();
//								map.put("itemsIcon", R.drawable.bus_img);
//								map.put("bus_title",
//										curbusline
//												.getBuslineFullName()
//												.substring(
//														0,
//														curbusline
//																.getBuslineFullName()
//																.indexOf("(")));
//								map.put("station_title",
//										curbusline.getStationName());
//								String nameString = curbusline
//										.getBuslineFullName();
//								nameString = "<font color=\"white\" background-color=\"red\">开往</font><font color=\"yellow\">"
//										+ nameString.substring(nameString
//												.indexOf("-") + 1);
//								nameString = nameString.substring(0,
//										nameString.length() - 1);
//								map.put("itemsTitle", nameString + "</font>");
//								if ((myNum - curNum) == 0) {
//									map.put("itemsText",
//											"<font color=\"red\">即将到站</font>， 下辆 <font color=\"black\">"
//													+ (myNum - curNum2)
//													+ "</font> 站, "
//													+ (int) (distanceTotal2 / 100)
//													/ 10.0 + "km");
//
//								} else {
//									map.put("itemsText",
//											"<font color=\"black\">"
//													+ (myNum - curNum)
//													+ "</font> 站, "
//													+ (int) (distanceTotal / 100)
//													/ 10.0
//													+ " km ;&nbsp;下辆 <font color=\"black\">"
//													+ (myNum - curNum2)
//													+ "</font> 站, "
//													+ (int) (distanceTotal2 / 100)
//													/ 10.0 + "km");
//								}
//
//								map.put("distance", mindistance + "");
//								map.put("busline", curbusline);
//								curbusline.setRtInfo(map);
//							} else {
//								HashMap<String, Object> map = new HashMap<String, Object>();
//								map.put("itemsIcon", R.drawable.bus_img);
//								map.put("bus_title",
//										curbusline
//												.getBuslineFullName()
//												.substring(
//														0,
//														curbusline
//																.getBuslineFullName()
//																.indexOf("(")));
//								map.put("station_title",
//										curbusline.getStationName());
//								String nameString = curbusline
//										.getBuslineFullName();
//								nameString = "<font color=\"white\" background-color=\"red\">开往</font><font color=\"yellow\">"
//										+ nameString.substring(nameString
//												.indexOf("-") + 1);
//								nameString = nameString.substring(0,
//										nameString.length() - 1);
//								map.put("itemsTitle", nameString);
//								if ((myNum - curNum) == 0) {
//									map.put("itemsText",
//											"<font color=\"red\">即将到站</font> ");
//
//								} else {
//									map.put("itemsText",
//											"<font color=\"black\">"
//													+ (myNum - curNum)
//													+ "</font> 站, "
//													+ (int) (distanceTotal / 100)
//													/ 10.0 + " km");
//								}
//
//								map.put("distance", mindistance + "");
//								map.put("busline", curbusline);
//								curbusline.setRtInfo(map);
//							}
//						}
//					}
//
//				} catch (SQLException sqle) {
//					throw sqle;
//				}
//
//			} catch (JSONException e) {
//				Log.e("JSON exception", e.getMessage());
//				e.printStackTrace();
//			}
//			Message msgMessage = new Message();
//			msgMessage.what = sign;
//			mHandler.sendMessage(msgMessage);
//		}
//	}
//
//	@Override
//	public void onStart() {
//		super.onStart();
//		Log.i("BaseFragment", TAG + " onStart");
//
//	}
//
//	@Override
//	public void onAttach(Activity activity) {
//		// TODO Auto-generated method stub
//		super.onAttach(activity);
//		mContext = (Context) activity;
//		mChooseListener = (IPopWindowListener) activity;
//		Log.i("BaseFragment", TAG + " onAttach");
//
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		// Stop polling service
//		System.out.println("Stop polling service...");
//		PollingUtils.stopPollingService(mContext, PollingService.class,
//				PollingService.ACTION);
//		ServiceUtils.stopPollingService(mContext.getApplicationContext(),
//				ScanService.class, Constants.SERVICE_ACTION);
//		Log.i("BaseFragment", TAG + " onDestroy");
//
//	}
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onActivityCreated(savedInstanceState);
//		Log.i("BaseFragment", TAG + " onActivityCreated");
//
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		Log.i("BaseFragment", TAG + " onCreate");
//
//	}
//
//	@Override
//	public void onDestroyView() {
//		// TODO Auto-generated method stub
//		super.onDestroyView();
//		Log.i("BaseFragment", TAG + " onDestroyView");
//
//	}
//
//	@Override
//	public void onDetach() {
//		// TODO Auto-generated method stub
//		super.onDetach();
//		Log.i("BaseFragment", TAG + " onDetach");
//
//	}
//
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		Log.i("BaseFragment", TAG + " onResume");
//
//	}
//
//	@Override
//	public void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		Log.i("BaseFragment", TAG + " onStop");
//
//	}
//}
