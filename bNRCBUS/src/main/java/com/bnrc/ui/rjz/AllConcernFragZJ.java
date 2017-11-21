//package com.bnrc.ui.rjz;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.json.XML;
//
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.utils.DistanceUtil;
//import com.bnrc.busapp.MyCipher;
//import com.bnrc.busapp.R;
//import com.bnrc.network.JsonObjectRequest;
//import com.bnrc.network.JsonRequest;
//import com.bnrc.network.MyVolley;
//import com.bnrc.network.StringRequest;
//import com.bnrc.network.toolbox.AuthFailureError;
//import com.bnrc.network.toolbox.HttpHeaderParser;
//import com.bnrc.network.toolbox.ParseError;
//import com.bnrc.network.toolbox.Request.Method;
//import com.bnrc.network.toolbox.NetworkResponse;
//import com.bnrc.network.toolbox.Response;
//import com.bnrc.network.toolbox.VolleyError;
//import com.bnrc.ui.rjz.NearFragmentZJ.doRequestResult;
//import com.bnrc.ui.rtBus.Child;
//import com.bnrc.ui.rtBus.Group;
//import com.bnrc.ui.rtBus.PinnedHeaderExpandableConcernAdapter;
//import com.bnrc.ui.rtBus.PinnedHeaderExpandableConcernListView;
//import com.bnrc.ui.rtBus.PinnedHeaderExpandableLocListView;
//import com.bnrc.util.BuslineDBHelper;
//import com.bnrc.util.DataBaseHelper;
//import com.bnrc.util.LocationUtil;
//import com.bnrc.util.PCDataBaseHelper;
//import com.bnrc.util.UserDataDBHelper;
//import com.bnrc.util.collectwifi.Constants;
//
//import android.R.bool;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.database.SQLException;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import com.baidu.location.BDLocation;
//import com.google.gson.JsonObject;
//
//public class AllConcernFragZJ extends BaseFragment {
//	private static final String TAG = AllConcernFragZJ.class.getSimpleName();
//	private PinnedHeaderExpandableConcernListView mAllConcernExplistview;
//	private PinnedHeaderExpandableConcernAdapter mAllConcernAdapter;
//	private RelativeLayout mAllConcernHint;
//	private List<Group> mAllConcernGroups;
//	private Context mContext;
//	private UserDataDBHelper mUserDataDBHelper;
//	public LocationUtil mLocationUtil = null;
//	private SwipeRefreshLayout swipeRefreshLayout;
//	private ProgressDialog pd = null;
//	public BDLocation mBDLocation = null;
//	private int mChildrenSize = 0;
//	private static final int DATABASE_REFRESH = 1;
//	public static boolean isFirstLoad = true;
//	String localhost = "10.108.107.87";
//	private String url_requestStation = "http://" + localhost
//			+ ":80/index.php/home/station";
//	private String url_requestLine = "http://" + localhost
//			+ ":80/index.php/home/line";
//	private String url_update = "http://" + localhost
//			+ ":80/index.php/home/update";
//	private Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case Constants.ALL_CONCERN:
//				mAllConcernHint.setVisibility(View.GONE);
//				mAllConcernAdapter.refresh();
//				Log.i(TAG, "ALL_CONCERN " + "mChildrenSize: " + mChildrenSize);
//				if (mChildrenSize > 0)
//					mChildrenSize--;
//				if (mChildrenSize == 0) {
//					// for (Group group : mAllConcernGroups) {
//					// group.sortChild();
//					// }
//					mOnSelectBtn.onStopLoading();
//					swipeRefreshLayout.setRefreshing(false);
//				}
//				break;
//			default:
//				break;
//			}
//
//		}
//	};
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		mLocationUtil = LocationUtil.getInstance(mContext
//				.getApplicationContext());
//		mLocationUtil.startLocation();
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.activity_allconcern_frag,
//				(ViewGroup) getActivity().findViewById(R.id.content), false);
//
//		mAllConcernExplistview = (PinnedHeaderExpandableConcernListView) view
//				.findViewById(R.id.explistview_all_concern);
//		mAllConcernHint = (RelativeLayout) view
//				.findViewById(R.id.rLayout_all_concern);
//		// 设置悬浮头部VIEW
//		mAllConcernExplistview.setHeaderView(
//				0,
//				getActivity().getLayoutInflater().inflate(
//						R.layout.group_head_concern, mAllConcernExplistview,
//						false));
//		swipeRefreshLayout = (SwipeRefreshLayout) view
//				.findViewById(R.id.swipe_container);
//		// 设置刷新时动画的颜色，可以设置4个
//		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_light,
//				android.R.color.holo_red_light,
//				android.R.color.holo_orange_light,
//				android.R.color.holo_green_light);
//		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//
//			@Override
//			public void onRefresh() {
//				// pd = ProgressDialog.show(mContext, "数据加载中…", "请等待");
//				mOnSelectBtn.onStartLoading();
//				new Handler().post(new Runnable() {
//
//					@Override
//					public void run() {
//						Log.i("Volley", "onRefresh");
//						MyVolley.sharedVolley(mContext.getApplicationContext())
//								.reStart();
//						// getNearbyStationsAndBuslines();
//						// new Thread(new doDataSearchThread()).start();
//						mOnSelectBtn.onRefreshConcern();
//					}
//				});
//			}
//		});
//		mUserDataDBHelper = UserDataDBHelper.getInstance(mContext
//				.getApplicationContext());
//		// new Thread(new doDataSearchThread()).start();
//		mOnSelectBtn.onStartLoading();
//		Log.i(TAG, TAG + " onCreateView");
//		return view;
//	}
//
//	public void getFavStationsAndBuslines() {
//		Log.i(TAG, "getFavStationsAndBuslines");
//		startTimer();
//		if (mAllConcernGroups != null)
//			mAllConcernGroups.clear();
//		if (isNetworkConnected()) {
//			BDLocation mBDLocation = mLocationUtil.mLocation;
//			LatLng point = new LatLng(mBDLocation.getLatitude(),
//					mBDLocation.getLongitude());
//			mAllConcernGroups = mUserDataDBHelper.AcquireFavInfoWithLocation(
//					Constants.ALL_CONCERN, mAllConcernGroups, point);
//			for (Group gro : mAllConcernGroups)
//				mChildrenSize += gro.getChildrenCount();
//			Log.i(TAG, "getNearbyStationsAndBuslines " + "mChildrenSize: "
//					+ mChildrenSize);
//		} else {
//			mAllConcernGroups = mUserDataDBHelper.AcquireFavInfo(
//					Constants.ALL_CONCERN, mAllConcernGroups);
//			for (Group gro : mAllConcernGroups)
//				mChildrenSize += gro.getChildrenCount();
//			Log.i(TAG, "getNearbyStationsAndBuslines " + "mChildrenSize: "
//					+ mChildrenSize);
//		}
//		if (mAllConcernAdapter == null) {
//			mAllConcernAdapter = new PinnedHeaderExpandableConcernAdapter(
//					mAllConcernGroups, mContext, mAllConcernExplistview,
//					mOnSelectBtn, 0, -1);
//			mAllConcernExplistview.setAdapter(mAllConcernAdapter);
//		}
//		if (mAllConcernGroups != null && mAllConcernGroups.size() > 0) {
//			mAllConcernHint.setVisibility(View.GONE);
//			GetRtBuslineInfo(mAllConcernGroups, Constants.ALL_CONCERN);
//			int groupCount = mAllConcernGroups.size();
//			for (int i = 0; i < groupCount; i++) {
//				if (mAllConcernGroups.get(i).getChildren().size() > 0)
//					mAllConcernExplistview.expandGroup(i);
//			}
//		} else {
//			mAllConcernHint.setVisibility(View.VISIBLE);
//			mAllConcernAdapter.refresh();
//			mOnSelectBtn.onStopLoading();
//			swipeRefreshLayout.setRefreshing(false);
//		}
//
//	}
//
//	@Override
//	public void refreshConcern() {
//		Log.i("MainActivity", "AllFragment refreshConcern()");
//		startTimer();
//		if (mAllConcernGroups.size() > 0) {
//			mAllConcernHint.setVisibility(View.GONE);
//			GetRtBuslineInfo(mAllConcernGroups, Constants.WORK_CONCERN);
//			int groupCount = mAllConcernGroups.size();
//			for (int i = 0; i < groupCount; i++) {
//				if (mAllConcernGroups.get(i).getChildren().size() > 0)
//					mAllConcernExplistview.expandGroup(i);
//			}
//		} else {
//			// Group initGroup = new Group("暂无信息", "1", "1");
//			// mWorkConcernGroups.add(initGroup);
//			mAllConcernHint.setVisibility(View.VISIBLE);
//			mAllConcernAdapter.refresh();
//		}
//		mAllConcernAdapter.notifyDataSetChanged();
//	}
//
//	@Override
//	public void refresh() {
//		Log.i("MainActivity", "AllFragment refresh()");
//		getFavStationsAndBuslines();
//	}
//
//	@Override
//	public int getFirstVisiblePosition() {
//		return mAllConcernExplistview.getFirstVisiblePosition();
//	}
//
//	public boolean isNetworkConnected() {
//		if (mContext != null) {
//			ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
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
//	public void refreshDataBase() {
//		mOnSelectBtn.onStartLoading();
//		new Handler().post(new Runnable() {
//			@Override
//			public void run() {
//				Log.i("Volley", "onRefresh");
//				MyVolley.sharedVolley(mContext).reStart();
//				new Thread(new doDataSearchThread()).start();
//			}
//		});
//	}
//
//	private void startTimer() {
//		mHandler.postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				mAllConcernAdapter.refresh();
//				mOnSelectBtn.onStopLoading();
//				swipeRefreshLayout.setRefreshing(false);
//			}
//		}, 6 * 1000);
//	}
//
//	public void GetRtBuslineInfo(ArrayList<Group> groups, int sign) {
//		if (((MainActivity) getActivity()).isNetworkConnected()) {
//			Log.i(TAG, "GetRtBuslineInfo");
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
//					String stationID = buslines.get(k).getBuslineSN();
//					if (buslineArrayList.size() > 0) {
//
//						Log.i(TAG, "有公交");
//						// get_realtime_data(
//						// buslines.get(k),
//						// Integer.parseInt(buslines.get(k)
//						// .getBuslineSN().subSequence(7, 9)
//						// .toString()), sign);
//						String buslineID = buslines.get(k).getBuslineSN()
//								.subSequence(0, 7).toString();
//						String buslineID1 = buslines.get(k).getBuslineId();
//						Log.i(TAG, "buslineID: " + buslineID + " " + buslineID1);
//						int station_num = Integer.parseInt(buslines.get(k)
//								.getBuslineSN().subSequence(7, 9).toString());
//						get_server_data(buslineID, stationID, buslines.get(k),
//								station_num, sign);
//
//					} else {
//						Message msgMessage = new Message();
//						msgMessage.what = sign;
//						mHandler.sendMessage(msgMessage);
//					}
//				}
//			}
//		} else {
//			Message msgMessage = new Message();
//			msgMessage.what = sign;
//			mHandler.sendMessage(msgMessage);
//		}
//	}
//
//	private void get_server_data(final String bulineID, final String stationID,
//			final Child busline, final int station_num, final int sign) {
//		JSONObject params = new JSONObject();
//		String stationId = PCDataBaseHelper.getInstance(mContext)
//				.aquireStationId(stationID);
//		try {
//			params.put("LineID", bulineID);
//			params.put("StationID", stationId);
//			params.put("TimeInterval", System.currentTimeMillis() / 1000);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		Log.i("SERVER", "station request: " + params.toString());
//		JsonObjectRequest objRequest = new JsonObjectRequest(Method.POST,
//				url_requestStation, params,
//				new Response.Listener<JSONObject>() {
//					@Override
//					public void onResponse(JSONObject obj) {
//						Log.i("SERVER", "station request buslineID: "
//								+ bulineID + " response: " + obj.toString());
//						dealJSON(obj, bulineID, stationID, busline,
//								station_num, sign);
//					}
//				}, new Response.ErrorListener() {
//					@Override
//					public void onErrorResponse(VolleyError error) {
//						error.getMessage();
//						Log.i("SERVER", "buslineID: " + bulineID
//								+ " VolleyError: " + error.getMessage());
//						try {
//							get_realtime_data(bulineID, busline, station_num,
//									sign);
//						} catch (UnsupportedEncodingException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//
//				});
//		MyVolley.sharedVolley(mContext).getRequestQueue().add(objRequest);
//	}
//
//	private void dealJSON(JSONObject obj, final String bulineID,
//			final String stationID, final Child busline, final int station_num,
//			final int sign) {
//		try {
//			// JSONObject obj = new JSONObject(response);
//			int status = obj.getInt("status");
//			switch (status) {
//			case 1:
//			case 2:
//			case 3:
//				try {
//					get_realtime_data(bulineID, busline, station_num, sign);
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					Message msg = new Message();
//					msg.what = sign;
//					mHandler.sendMessage(msg);
//					e.printStackTrace();
//				}
//				break;
//			case 0:
//				Log.i("SERVER", "cg_server resp " + obj.toString());
//				JSONObject data = obj.getJSONObject("data");
//				String StationDistance = data.getString("StationDistance");
//				String StationArrivingTime = data
//						.getString("StationArrivingTime");
//				Map<String, Object> map = new HashMap<String, Object>();
//				busline.setRtInfo(map);
//				map.put("stationDistance", Integer.parseInt(StationDistance)
//						/ 1000 + "");
//				map.put("itemsText",
//						"<font color=\"red\">"
//								+ (Integer.parseInt(StationArrivingTime) / 60
//										+ 1 )
//								+ " 分钟" + "</font>");
//				Message msgMessage = new Message();
//				msgMessage.what = sign;
//				mHandler.sendMessage(msgMessage);
//				break;
//			default:
//				try {
//					get_realtime_data(bulineID, busline, station_num, sign);
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					Message msg = new Message();
//					msg.what = sign;
//					mHandler.sendMessage(msg);
//				}
//				break;
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			try {
//				get_realtime_data(bulineID, busline, station_num, sign);
//			} catch (UnsupportedEncodingException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//				Message msg = new Message();
//				msg.what = sign;
//				mHandler.sendMessage(msg);
//			} catch (JSONException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//				Message msg = new Message();
//				msg.what = sign;
//				mHandler.sendMessage(msg);
//			}
//		}
//	}
//
//	private void post_server_data(JSONArray mBusJsonArray,
//			final String buslineID) {
//		JSONObject json = new JSONObject();
//		JSONArray arr = new JSONArray();
//		int busArrayCount = mBusJsonArray.length();
//		for (int i = 0; i < busArrayCount; i++) {
//			JSONObject js;
//			try {
//				js = mBusJsonArray.getJSONObject(i);
//				MyCipher mCiper = new MyCipher("aibang" + js.getString("gt"));
//				String NextStationNum = mCiper.decrypt(js.getString("nsn"));// next
//				// station
//				// num
//
//				String NextStation = mCiper.decrypt(js.getString("ns"));// next
//				// station
//				// name
//				String x = mCiper.decrypt(js.getString("x"));
//				String y = mCiper.decrypt(js.getString("y"));
//				String NextStationDistance = js.getString("nsd");// next_station_distance
//				String BusID = buslineID+i+1;
//				// Log.e("SERVER", "post_server_data " + "buslineID: " +
//				// buslineID
//				// + " NextStationNum: " + NextStationNum + " BusID: "
//				// + BusID + " NextStationDistance: "
//				// + NextStationDistance);
//				JSONObject mapArr = new JSONObject();
//				mapArr.put("LineID", buslineID);
//				mapArr.put("BusID", BusID);
//				// mapArr.put("NextStation", NextStation);
//				mapArr.put("NextStationNum", NextStationNum);
//				mapArr.put("NextStationDistance", NextStationDistance);
//				mapArr.put("Latitude", x);
//				mapArr.put("Longitude", y);
//
//				mapArr.put("TimeInterval", System.currentTimeMillis()/1000);
//
//				// map.put("data[" + i + "]", mapArr.toString());
//				arr.put(mapArr);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				Log.e(TAG, "post_server_data exception!!!");
//			}
//
//		}
//		try {
//			json.put("city", "beijing");
//			json.put("data", arr);
//
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		Log.i("SERVER", "params: " + json);
//		JsonObjectRequest objRequest = new JsonObjectRequest(Method.POST,
//				url_update, json, new Response.Listener<JSONObject>() {
//					@Override
//					public void onResponse(JSONObject obj) {
//						Log.i("SERVER", "response: " + " JsonObjectRequest "
//								+ obj.toString());
//					}
//				}, new Response.ErrorListener() {
//					@Override
//					public void onErrorResponse(VolleyError error) {
//						error.getMessage();
//						Log.i("SERVER", "VolleyError: " + " JsonObjectRequest "
//								+ error.toString());
//
//					}
//
//				});
//
//		MyVolley.sharedVolley(mContext).getRequestQueue().add(objRequest);
//	}
//
//	public void get_realtime_data(final String bulineID, final Child busline,
//			final int station_num, final int sign) throws JSONException,
//			UnsupportedEncodingException {
//		ArrayList<String> buslineArrayList = BuslineDBHelper.getInstance(
//				mContext).AcquireOffLineInfoWithBuslineName(
//				busline.getBuslineFullName());
//		if (buslineArrayList.size() > 0) {
//			String JSONDataUrl = "http://bjgj.aibang.com:8899/bus.php?city="
//					+ URLEncoder.encode("北京", "utf-8") + "&id="
//					+ buslineArrayList.get(3) + "&no=" + station_num
//					+ "&type=1&encrypt=1&versionid=2";
//			Log.i(TAG, "request: " + //
//					JSONDataUrl.toString());
//			StringRequest jsonObjectRequest = new StringRequest(JSONDataUrl,
//					new Response.Listener<String>() {
//
//						@Override
//						public void onResponse(String response) {
//							try {
//								JSONObject responseJson = XML
//										.toJSONObject(response);
//
//								JSONObject rootJson = responseJson
//										.getJSONObject("root");
//								Log.i(TAG, "rootJson: " + //
//										rootJson.toString());
//
//								int status = rootJson.getInt("status");
//								if (status != 200) {
//									Log.i(TAG, busline.getBuslineFullName()
//											+ " 暂无实时公交信息");
//									Message msgMessage = new Message();
//									msgMessage.what = sign;
//									mHandler.sendMessage(msgMessage);
//									return;
//								}
//								JSONObject dataJson = rootJson
//										.getJSONObject("data");
//								Log.i(TAG, "dataJson " + dataJson.toString());
//								JSONArray busJsonArray = null;
//
//								if (dataJson.toString().indexOf("[") > 0) {
//									busJsonArray = (JSONArray) dataJson
//											.get("bus");
//									busJsonArray = dataJson.getJSONArray("bus");
//									Log.i(TAG,
//											"busJsonArray "
//													+ busJsonArray.toString());
//
//								} else {
//
//									JSONObject busJsonObject = dataJson
//											.getJSONObject("bus");
//									Log.i(TAG,
//											"busJsonObject "
//													+ busJsonObject.toString());
//									busJsonArray = new JSONArray("["
//											+ busJsonObject.toString() + "]");
//									Log.i(TAG, "busJsonObject to array: "
//											+ busJsonArray.toString());
//								}
//
//								new Thread(new doRequestResult(busJsonArray,
//										busline, station_num, sign)).start();
//								post_server_data(busJsonArray, bulineID);
//
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								Message msgMessage = new Message();
//								msgMessage.what = sign;
//								mHandler.sendMessage(msgMessage);
//								e.printStackTrace();
//							}
//
//						}
//
//					}, new Response.ErrorListener() {
//						@Override
//						public void onErrorResponse(VolleyError arg0) {
//							Message msgMessage = new Message();
//							msgMessage.what = sign;
//							mHandler.sendMessage(msgMessage);
//						}
//					});
//			MyVolley.sharedVolley(mContext).getRequestQueue()
//					.add(jsonObjectRequest);
//
//		} else {
//			Message msgMessage = new Message();
//			msgMessage.what = sign;
//			mHandler.sendMessage(msgMessage);
//		}
//	}
//
//	public class doDataSearchThread implements Runnable {
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			getFavStationsAndBuslines();
//		}
//
//	}
//
//	public class doRequestResult implements Runnable {
//		private JSONArray mBusJsonArray;
//		private Child mChild;
//		private int SIGN;
//		private int stationNum;
//
//		public doRequestResult(JSONArray mBusJsonArray, Child mChild,
//				int stationNum, int SIGN) {
//			this.mBusJsonArray = mBusJsonArray;
//			this.mChild = mChild;
//			this.stationNum = stationNum;
//			this.SIGN = SIGN;
//		}
//
//		public String TimeStampToDate(Long timestampString, String formats) {
//			if (timestampString < 0)
//				return "0";
//			String date = new java.text.SimpleDateFormat(formats)
//					.format(new java.util.Date(timestampString * 1000));
//			return date;
//		}
//
//		public int TimeStampToDelTime(Long timestampString) {
//			if (timestampString < 0)
//				return (int) 0;
//			double delTime = (timestampString * 1000 - System
//					.currentTimeMillis()) / 1000 / 60;
//			return (int) Math.ceil(delTime);
//		}
//
//		public boolean isNumeric(String str) {
//			Pattern pattern = Pattern.compile("[0-9]*");
//			Matcher isNum = pattern.matcher(str);
//			if (!isNum.matches()) {
//				return false;
//			}
//			return true;
//		}
//
//		Comparator<Map<String, Object>> comparator = new Comparator<Map<String, Object>>() {
//			public int compare(Map<String, Object> s1, Map<String, Object> s2) {
//				int num1 = Integer.parseInt(s1.get("stationNum").toString());
//				int num2 = Integer.parseInt(s2.get("stationNum").toString());
//				if (num1 >= num2) {
//					return -1;
//				} else if (num1 < num2) {
//					return 1;
//				}
//				return 0;
//			}
//		};
//
//		public void run() {
//			try {
//				try {
//					int busArrayCount = mBusJsonArray.length();
//					Log.i(TAG, "busJsonArray_count: " + busArrayCount);
//					ArrayList<ArrayList<String>> buslineStationsList = DataBaseHelper
//							.getInstance(mContext)
//							.AcquireStationsWithBuslineID(mChild.getBuslineId());// 实时查询的线路上所有站信息
//					int buslineStaCount = buslineStationsList.size();
//					int myNum = -1;
//					List<Map<String, Object>> curNumList = new ArrayList<Map<String, Object>>();
//					String myStationName = mChild.getStationName();// 当前查询站点名称keyName
//					// String myStationName = "天安门东";
//					int max = -1;
//					Map<String, Object> map = new HashMap<String, Object>();
//					for (int j = 0; j < busArrayCount; j++) {
//
//						JSONObject busJson = (JSONObject) mBusJsonArray.get(j);
//
//						MyCipher mCiper = new MyCipher("aibang"
//								+ busJson.getString("gt"));
//
//						String ns = mCiper.decrypt(busJson.getString("ns"));// next
//						// station
//						// name
//						String nsn = mCiper.decrypt(busJson.getString("nsn"));// next
//																				// station
//																				// num
//						String nsd = busJson.getString("nsd");// next_station_distance
//						String nst = busJson.getString("nst");
//						if (isNumeric(nst))
//							nst = TimeStampToDate(Long.parseLong(nst),
//									"yyyy-MM-dd HH:mm:ss");// next_station_arriving_time
//						String sd = mCiper.decrypt(busJson.getString("sd"));// station_distance
//						String st = mCiper.decrypt(busJson.getString("st"));
//						String st_c = null;
//						if (isNumeric(st))
//							// st_c = TimeStampToDate(Long.parseLong(st),
//							// "HH:mm");// station_arriving_time
//							st_c = String.valueOf(TimeStampToDelTime(Long
//									.parseLong(st)));// station_arriving_time
//						else
//							st_c = "-1";
//						String x = mCiper.decrypt(busJson.getString("x"));
//						String y = mCiper.decrypt(busJson.getString("y"));
//
//						Log.i(TAG,
//								"***********************\nnext_station_name: "
//										+ ns + "\n" + "next_station_num: "
//										+ nsn + "\n"
//										+ "next_station_distance: " + nsd
//										+ "\n" + "next_station_arriving_time: "
//										+ nst + "\n" + "station_distance: "
//										+ sd + "\n" + "station_arriving_time: "
//										+ st + "   " + st_c + "\n"
//										+ " currentTime "
//										+ System.currentTimeMillis());
//
//						if (Integer.parseInt(nsn) <= stationNum && st_c != null) {
//							if (Integer.parseInt(nsn) >= max) {
//								if (st_c.equals("-1")
//										&& Integer.parseInt(nsn) == stationNum) {
//									map.put("stationNum",
//											(Integer.parseInt(nsn)));
//									map.put("stationDistance", sd);
//									map.put("itemsText", "<font color=\"red\">"
//											+ "到站" + "</font>");
//									map.put("timeStamp", Long.parseLong(st));
//								} else if (!st_c.equals("-1")
//										&& Integer.parseInt(nsn) == stationNum) {
//									map.put("stationNum",
//											(Integer.parseInt(nsn)));
//									map.put("stationDistance", sd);
//									map.put("itemsText", "<font color=\"red\">"
//											+ "即将" + "</font>");
//									map.put("timeStamp", Long.parseLong(st));
//								} else {
//									map.put("stationNum",
//											(Integer.parseInt(nsn)));
//									map.put("stationDistance", sd);
//									map.put("itemsText", st_c + "分钟");
//									map.put("timeStamp", Long.parseLong(st));
//								}
//								max = Integer.parseInt(nsn);
//							}
//
//						}
//					}
//					if (max != -1) {
//						mChild.setRtInfo(map);
//						Message msgMessage = new Message();
//						msgMessage.what = SIGN;
//						mHandler.sendMessage(msgMessage);
//					} else {
//						Message msgMessage = new Message();
//						msgMessage.what = SIGN;
//						mHandler.sendMessage(msgMessage);
//						mChild.setRtInfo(null);
//						return;
//					}
//
//				} catch (SQLException sqle) {
//					Message msgMessage = new Message();
//					msgMessage.what = SIGN;
//					mHandler.sendMessage(msgMessage);
//					throw sqle;
//				}
//
//			} catch (JSONException e) {
//				Log.e("JSON exception", e.getMessage());
//				Message msgMessage = new Message();
//				msgMessage.what = SIGN;
//				mHandler.sendMessage(msgMessage);
//				e.printStackTrace();
//			}
//		}
//	}
//
//	IPopWindowListener mOnSelectBtn;
//
//	@Override
//	public void onAttach(Activity activity) {
//		// TODO Auto-generated method stub
//		super.onAttach(activity);
//		mContext = (Context) getActivity();
//		mOnSelectBtn = (IPopWindowListener) activity;
//		Log.i(TAG, TAG + " onAttach");
//
//	}
//
//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		Log.i(TAG, TAG + " onDestroy");
//
//	}
//
//	@Override
//	public void onDestroyView() {
//		// TODO Auto-generated method stub
//		super.onDestroyView();
//		Log.i(TAG, TAG + " onDestroyView");
//
//	}
//
//	@Override
//	public void onDetach() {
//		// TODO Auto-generated method stub
//		super.onDetach();
//		Log.i(TAG, TAG + " onDetach");
//
//	}
//
//	@Override
//	public void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		Log.i(TAG, TAG + " onPause");
//
//	}
//
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		getFavStationsAndBuslines();
//
//		Log.i(TAG, TAG + " onResume");
//
//		// getFavStationsAndBuslines();
//	}
//
//	@Override
//	public void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//		Log.i(TAG, TAG + " onStart");
//
//		// getFavStationsAndBuslines();
//	}
//
//	@Override
//	public void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		Log.i(TAG, TAG + " onStop");
//
//	}
//}
