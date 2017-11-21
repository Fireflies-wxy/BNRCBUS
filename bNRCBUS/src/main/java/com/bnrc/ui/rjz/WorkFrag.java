//package com.bnrc.ui.rjz;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.json.XML;
//
//import com.baidu.location.BDLocation;
//import com.baidu.mapapi.model.LatLng;
//import com.bnrc.busapp.MyCipher;
//import com.bnrc.busapp.R;
//import com.bnrc.network.MyVolley;
//import com.bnrc.network.StringRequest;
//import com.bnrc.network.toolbox.Response;
//import com.bnrc.network.toolbox.VolleyError;
//import com.bnrc.ui.rtBus.Child;
//import com.bnrc.ui.rtBus.Group;
//import com.bnrc.ui.rtBus.PinnedHeaderExpandableConcernAdapter;
//import com.bnrc.ui.rtBus.PinnedHeaderExpandableConcernListView;
//import com.bnrc.ui.rtBus.PinnedHeaderExpandableLocListView;
//import com.bnrc.util.BuslineDBHelper;
//import com.bnrc.util.DataBaseHelper;
//import com.bnrc.util.LocationUtil;
//import com.bnrc.util.UserDataDBHelper;
//import com.bnrc.util.collectwifi.Constants;
//
//import android.app.Activity;
//import android.content.Context;
//import android.database.SQLException;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//public class WorkFrag extends Fragment {
//	private static final String TAG = WorkFrag.class.getSimpleName();
//	private PinnedHeaderExpandableConcernListView mWorkConcernExplistview;
//	private PinnedHeaderExpandableConcernAdapter mWorkConcernAdapter;
//	private RelativeLayout mWorkConcernHint;
//	private ArrayList<Group> mWorkConcernGroups;
//	private Context mContext;
//	private ArrayList<ArrayList<String>> favStations = null;
//	private ArrayList<ArrayList<String>> favBuslines = null;
//	public LocationUtil mLocationUtil = null;
//	private UserDataDBHelper mUserDataDBHelper;
//
//	private Handler mHandler;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		mContext = (Context) getActivity();
//		mHandler = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//				switch (msg.what) {
//				case Constants.WORK_CONCERN:
//					Log.d(TAG, "mConcernAdapter.notifyDataSetChanged()");
//					mWorkConcernHint.setVisibility(View.GONE);
//					for (Group group : mWorkConcernGroups)
//						group.sortChild();
//					mWorkConcernAdapter.notifyDataSetChanged();
//					break;
//
//				default:
//					Log.d(TAG, "default.notifyDataSetChanged()");
//
//					break;
//				}
//
//			}
//		};
//	}
//
//	IPopWindowListener mOnSelectBtn;
//
//	@Override
//	public void onAttach(Activity activity) {
//		// TODO Auto-generated method stub
//		super.onAttach(activity);
//		mOnSelectBtn = (IPopWindowListener) activity;
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.activity_work_frag,
//				(ViewGroup) getActivity().findViewById(R.id.content), false);
//		mLocationUtil = LocationUtil.getInstance(mContext);
//		mLocationUtil.startLocation();
//		mUserDataDBHelper = UserDataDBHelper.getInstance(mContext);
//
//		mWorkConcernExplistview = (PinnedHeaderExpandableConcernListView) view
//				.findViewById(R.id.explistview_work_concern);
//
//		mWorkConcernHint = (RelativeLayout) view
//				.findViewById(R.id.rLayout_work_concern);
//		mWorkConcernGroups = new ArrayList<Group>();
//
//		// 设置悬浮头部VIEW
//		mWorkConcernExplistview.setHeaderView(
//				0,
//				getActivity().getLayoutInflater().inflate(
//						R.layout.group_head_concern, mWorkConcernExplistview,
//						false));
//		mWorkConcernAdapter = new PinnedHeaderExpandableConcernAdapter(
//				mWorkConcernGroups, mContext.getApplicationContext(),
//				mWorkConcernExplistview, mOnSelectBtn, 0,Constants.TYPE_WORK);
//		mWorkConcernExplistview.setAdapter(mWorkConcernAdapter);
//		getFavStationsAndBuslines();
//		return view;
//	}
//
//	/*
//	 * private void getFavStationsAndBuslines() { Log.i(TAG,
//	 * "getFavStationsAndBuslines");
//	 * 
//	 * // new Thread(new ConcernDBRunnable(mConcernGroups, favStations, //
//	 * favBuslines)).start(); // favStations =
//	 * UserDataDBHelper.getInstance(mContext).favStations; // favBuslines =
//	 * UserDataDBHelper.getInstance(mContext).favBuslines; favStations = new
//	 * ArrayList<ArrayList<String>>(); favBuslines = new
//	 * ArrayList<ArrayList<String>>(); mWorkConcernGroups.clear(); if
//	 * (favStations.size() > 0 || favBuslines.size() > 0) { for
//	 * (ArrayList<String> station : favStations) { int k =
//	 * mWorkConcernGroups.size(); int i; for (i = 0; i < k; i++) { if
//	 * (mWorkConcernGroups.get(i).getStationTitle().trim()
//	 * .equalsIgnoreCase(station.get(0).trim())) { break; }
//	 * 
//	 * } if (k == 0 || k == i) { Group group = new Group(station.get(0),
//	 * station.get(1), station.get(2)); mWorkConcernGroups.add(group); //
//	 * Log.i(TAG, "stationName: " + station.get(0));
//	 * ArrayList<ArrayList<String>> station_buslines = DataBaseHelper
//	 * .getInstance(mContext) .getRtBusLinesWithStationName(station.get(0)); int
//	 * size = station_buslines.size(); for (int j = 0; j < size; j++) { Child
//	 * child = new Child(station_buslines.get(j).get(0),
//	 * station_buslines.get(j).get(1), station_buslines.get(j).get(2),
//	 * station_buslines.get(j).get(3), station_buslines.get(j).get(4),
//	 * station_buslines.get(j).get(5), station_buslines.get(j).get(6));
//	 * group.addChildrenItem(child); } } // if (mConcernGroups.size() > 7) { //
//	 * break; // // 想要从缓存中获取文件 // } } int preSize = mWorkConcernGroups.size();
//	 * for (ArrayList<String> busline : favBuslines) { int k =
//	 * mWorkConcernGroups.size(); int i; for (i = 0; i < k; i++) { if
//	 * (mWorkConcernGroups.get(i).getStationTitle().trim()
//	 * .equalsIgnoreCase(busline.get(3).trim())) { Child child = new
//	 * Child(busline.get(0), busline.get(1), busline.get(2), busline.get(3),
//	 * busline.get(4), busline.get(5), busline.get(6));
//	 * mWorkConcernGroups.get(i).addChildrenItemFront(child); } } if (k ==
//	 * preSize || k == i) { Group group = new Group(busline.get(3),
//	 * busline.get(4), busline.get(5)); mWorkConcernGroups.add(group); }
//	 * 
//	 * } mWorkConcernHint.setVisibility(View.GONE);
//	 * GetRtBuslineInfo(mWorkConcernGroups, WORK_CONCERN_NOTIFY); int groupCount
//	 * = mWorkConcernGroups.size(); for (int i = 0; i < groupCount; i++) { if
//	 * (mWorkConcernGroups.get(i).getChildren().size() > 0)
//	 * mWorkConcernExplistview.expandGroup(i); } } else {
//	 * 
//	 * Group initGroup = new Group("暂无信息", "1", "1");
//	 * mWorkConcernGroups.add(initGroup);
//	 * mWorkConcernHint.setVisibility(View.VISIBLE);
//	 * mWorkConcernAdapter.notifyDataSetChanged(); } }
//	 */
//	private void getFavStationsAndBuslines() {
//		Log.i(TAG, "getFavStationsAndBuslines");
//		mWorkConcernGroups.clear();
//		BDLocation mBDLocation = mLocationUtil.mLocation;
//		// mWorkConcernGroups = mUserDataDBHelper.AcquireFavInfoWithLocation(
//		// new LatLng(mBDLocation.getLatitude(), mBDLocation
//		// .getLongitude()), mWorkConcernGroups, Constants.TYPE_WORK);
//		// mWorkConcernGroups = mUserDataDBHelper.getmWorkFavGroups();
//
//		if (mWorkConcernGroups.size() > 0) {
//			mWorkConcernHint.setVisibility(View.GONE);
//			GetRtBuslineInfo(mWorkConcernGroups, Constants.WORK_CONCERN);
//			int groupCount = mWorkConcernGroups.size();
//			for (int i = 0; i < groupCount; i++) {
//				if (mWorkConcernGroups.get(i).getChildren().size() > 0)
//					mWorkConcernExplistview.expandGroup(i);
//			}
//		} else {
//			Group initGroup = new Group("暂无信息", "1", "1");
//			mWorkConcernGroups.add(initGroup);
//			mWorkConcernHint.setVisibility(View.VISIBLE);
//			mWorkConcernAdapter.notifyDataSetChanged();
//		}
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
//							.getInstance(mContext)
//							.getSpecificStationsWithBuslineName(
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
//											"<font color=\"red\">即将到站</font>"
//													+ "<br/><br/>"
//													+ "下辆 <font color=\"black\">"
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
//													+ " km"
//
//													+ "<br/>下辆 <font color=\"black\">"
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
//
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
//}
