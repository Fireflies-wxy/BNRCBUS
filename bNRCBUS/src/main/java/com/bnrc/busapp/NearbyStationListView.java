package com.bnrc.busapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import u.aly.bu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import cn.domob.android.ads.AdEventListener;
import cn.domob.android.ads.AdView;
import cn.domob.android.ads.AdManager.ErrorCode;

import com.bnrc.util.DataBaseHelper;
import com.bnrc.util.LocationUtil;
import com.bnrc.util.MostSimilarString;
import com.bnrc.util.UserDataDBHelper;
import com.bnrc.util.collectwifi.BaseActivity;
import com.ab.activity.AbActivity;
import com.ab.global.AbConstant;
import com.ab.view.titlebar.AbTitleBar;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bnrc.activity.AboutActivity;
import com.bnrc.adapter.MyListViewAdapter;
import com.bnrc.busapp.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;

public class NearbyStationListView extends BaseActivity {

	private ImageView ivDeleteText;
	private EditText etSearch;
	private Button searchaBtn;
	public LocationClient mLocationClient = null;
	public LocationManager locationer = null;
	public DataBaseHelper dabase = null;
	public UserDataDBHelper userdabase = null;
	public LatLng mpoint = null;
	public LocationUtil mApplication = null;
	public List<Map<String, Object>> listData;
	private MyListViewAdapter myListViewAdapter;
	ArrayList<ArrayList<String>> stations;
	private ListView mListView;
	private ArrayList<View> searchArrayList = null;
	private LinearLayout buslineContainer;
	private ArrayList<ArrayList<String>> searchedStations;

	RelativeLayout mAdContainer;
	AdView mAdview;
	private AbTitleBar mAbTitleBar;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setAbContentView(R.layout.nearby_station_list_view);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("站点查询");
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleBarGravity(Gravity.CENTER, Gravity.CENTER);
		initbase();

		stations = new ArrayList<ArrayList<String>>();

		String value = MobclickAgent.getConfigParams(
				NearbyStationListView.this, "open_ad");
		Log.i("开启广告", value);
		/*if (value.equals("1")) {
			mAdContainer = (RelativeLayout) findViewById(R.id.adcontainer);
			// Create ad view
			mAdview = new AdView(this, "56OJzfwIuN7tr9LoSs",
					"16TLmHWoAp8diNUdpuAEMYfi");
			SharedPreferences mySharedPreferences = getSharedPreferences(
					"setting", UserSettingView.MODE_PRIVATE);
			String agString = mySharedPreferences.getString("userAge", "20");
			String sexString = mySharedPreferences.getString("userSex", "女");

			if (sexString.equals("女")) {
				mAdview.setUserGender("female");
			} else {
				mAdview.setUserGender("male");
			}
			// mAdview.setKeyword("game");

			Calendar mycalendar = Calendar.getInstance();// 获取现在时间
			String curYearString = String
					.valueOf(mycalendar.get(Calendar.YEAR));// 获取年份
			int age = Integer.parseInt(agString);
			int birth = Integer.parseInt(curYearString) - age;
			mAdview.setUserBirthdayStr(birth + "-08-08");
			mAdview.setUserPostcode("123456");
			mAdview.setAdEventListener(new AdEventListener() {
				@Override
				public void onAdOverlayPresented(AdView adView) {
					Log.i("DomobSDKDemo", "overlayPresented");
				}

				@Override
				public void onAdOverlayDismissed(AdView adView) {
					Log.i("DomobSDKDemo", "Overrided be dismissed");
				}

				@Override
				public void onAdClicked(AdView arg0) {
					Log.i("DomobSDKDemo", "onDomobAdClicked");
				}

				@Override
				public void onLeaveApplication(AdView arg0) {
					Log.i("DomobSDKDemo", "onDomobLeaveApplication");
				}

				@Override
				public Context onAdRequiresCurrentContext() {
					return NearbyStationListView.this;
				}

				@Override
				public void onAdFailed(AdView arg0, ErrorCode arg1) {
					Log.i("DomobSDKDemo", "onDomobAdFailed");
				}

				@Override
				public void onEventAdReturned(AdView arg0) {
					Log.i("DomobSDKDemo", "onDomobAdReturned");
				}
			});
			RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
			mAdview.setLayoutParams(layout);
			mAdContainer.addView(mAdview);
		}*/

		if (isNetworkConnected(NearbyStationListView.this)) {
			initTitleRightLayout();
		} else {

		}

		mApplication = LocationUtil.getInstance(NearbyStationListView.this);
		ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
		etSearch = (EditText) findViewById(R.id.etSearch);
		searchaBtn = (Button) findViewById(R.id.btnSearch);

		dabase = DataBaseHelper.getInstance(NearbyStationListView.this);
		userdabase = UserDataDBHelper.getInstance(NearbyStationListView.this);

		ivDeleteText.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				etSearch.setText("");
			}
		});

		etSearch.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() > 1) {
					listData.clear();
					LocationUtil mApplication = LocationUtil
							.getInstance(NearbyStationListView.this);
					;
					BDLocation location = mApplication.getmLocation();
					mpoint = new LatLng(location.getLatitude(), location
							.getLongitude());
					stations.clear();
					stations = dabase.getStationsWithStationKeyword(etSearch
							.getText().toString());
					Toast toast = Toast.makeText(getApplicationContext(),
							"查找到关于\"" + etSearch.getText().toString() + "\"的"
									+ stations.size() + "个站点",
							Toast.LENGTH_LONG);
					toast.show();
					int j = stations.size();
					Map<String, Object> map = new HashMap<String, Object>();
					for (int i = 0; i < j; i++) {
						ArrayList<String> station = new ArrayList<String>();

						station = stations.get(i);
						map = new HashMap<String, Object>();
						// if (userdabase.checkFavStationWithStationID(station
						// .get(1))) {
						// map.put("itemsTitle", station.get(1));
						// } else {
						// map.put("itemsTitle", station.get(1));
						// }
						map.put("itemsIcon", R.drawable.station);
						LatLng stationPoint = new LatLng(Float
								.parseFloat(station.get(2)), Float
								.parseFloat(station.get(3)));
						double distance = DistanceUtil.getDistance(mpoint,
								stationPoint);
						map.put("itemsText",
								"目前距离我 " + (int) (Math.floor(distance + 0.5))
										+ " 米");
						map.put("distance", (int) (Math.floor(distance + 0.5))
								+ "");
						listData.add(map);

					}

					myListViewAdapter.notifyDataSetChanged();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					ivDeleteText.setVisibility(View.GONE);
				} else {
					ivDeleteText.setVisibility(View.VISIBLE);
				}
			}
		});

		searchaBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				InputMethodManager imm = (InputMethodManager) getSystemService(NearbyStationListView.this.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

				if (etSearch.getText().toString().equalsIgnoreCase("我的位置")
						|| etSearch.getText().toString().length() < 1) {
					getAroundStation();
				} else {
					listData.clear();
					LocationUtil mApplication = LocationUtil
							.getInstance(NearbyStationListView.this);
					;
					BDLocation location = mApplication.getmLocation();
					mpoint = new LatLng(location.getLatitude(), location
							.getLongitude());
					stations.clear();
					stations = dabase.getStationsWithStationKeyword(etSearch
							.getText().toString());
					Toast toast = Toast.makeText(getApplicationContext(),
							"查找到关于\"" + etSearch.getText().toString() + "\"的"
									+ stations.size() + "个站点",
							Toast.LENGTH_LONG);
					toast.show();
					int j = stations.size();
					Map<String, Object> map = new HashMap<String, Object>();
					for (int i = 0; i < j; i++) {
						ArrayList<String> station = new ArrayList<String>();

						station = stations.get(i);
						map = new HashMap<String, Object>();
						// if (userdabase.checkFavStationWithStationID(station
						// .get(1))) {
						// map.put("itemsTitle", station.get(1));
						// } else {
						// map.put("itemsTitle", station.get(1));
						// }
						map.put("itemsIcon", R.drawable.station);
						LatLng stationPoint = new LatLng(Float
								.parseFloat(station.get(2)), Float
								.parseFloat(station.get(3)));
						double distance = DistanceUtil.getDistance(mpoint,
								stationPoint);
						map.put("itemsText",
								"目前距离我 " + (int) (Math.floor(distance + 0.5))
										+ " 米");
						map.put("distance", (int) (Math.floor(distance + 0.5))
								+ "");
						listData.add(map);

					}

					myListViewAdapter.notifyDataSetChanged();
				}
			}
		});

		mListView = (ListView) this.findViewById(R.id.mStationsListView);
		listData = new ArrayList<Map<String, Object>>();
		myListViewAdapter = new MyListViewAdapter(this, listData,
				R.layout.list_items, new String[] { "itemsIcon", "itemsTitle",
						"itemsText" }, new int[] { R.id.itemsIcon,
						R.id.tv_stationName, R.id.itemsText });
		mListView.setAdapter(myListViewAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// Intent intent = null;

				// Intent intent = new Intent(NearbyStationListView.this,
				// StationInformationView.class);
				// // ����ͼ�д�������
				// ArrayList<String> station = new ArrayList<String>();
				// station = stations.get(position);
				// intent.putExtra("title", station.get(1));
				// intent.putExtra("latitude", station.get(2));
				// intent.putExtra("longitude", station.get(3));
				// // ������ͼ
				// startActivity(intent);
			}
		});

		getAroundStation();
		getSearchList();

		// 默认软键盘不弹出
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	public void getSearchList() {
		buslineContainer = (LinearLayout) findViewById(R.id.searchList);

		if (searchArrayList == null) {
			searchArrayList = new ArrayList<View>();
		} else {
			int m = searchArrayList.size();
			for (int i = 0; i < m; i++) {
				searchArrayList.get(i).setVisibility(View.GONE);
				searchArrayList.remove(i);
				i--;
				m--;
			}
		}
		userdabase = UserDataDBHelper.getInstance(NearbyStationListView.this);
		// searchedStations = userdabase.getLatestSearchStations();
		int j = searchedStations.size();
		ArrayList<String> busline;
		for (int i = 0; i < j; i++) {
			busline = searchedStations.get(i);
			View stationItem = View.inflate(NearbyStationListView.this,
					R.layout.search_item_view, null);
			if (i == 0) {
				int w = View.MeasureSpec.makeMeasureSpec(0,
						View.MeasureSpec.UNSPECIFIED);
				int h = View.MeasureSpec.makeMeasureSpec(0,
						View.MeasureSpec.UNSPECIFIED);
				stationItem.measure(w, h);
			}
			TextView title = (TextView) stationItem
					.findViewById(R.id.tv_waitStation);
			title.setText(busline.get(0));
			title.setTextColor(Color.BLACK);
			searchArrayList.add(stationItem);
			buslineContainer.addView(stationItem);
			title.setTag((i + 1024) + "");

			title.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int tag = Integer.parseInt(arg0.getTag().toString()) - 1024;
					// Intent intent = null;
					// ArrayList<String> curstation = new ArrayList<String>();
					// curstation = searchedStations.get(tag);
					// Intent intent = new Intent(NearbyStationListView.this,
					// StationInformationView.class);
					// intent.putExtra("title", curstation.get(0));
					// intent.putExtra("latitude", curstation.get(1));
					// intent.putExtra("longitude", curstation.get(2));
					// // userdabase.addSearchStaitonWithStation(curstation);
					// startActivity(intent);
				}
			});
		}
		if (j == 0) {
			View stationItem = View.inflate(NearbyStationListView.this,
					R.layout.search_item_view, null);
			int w = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			stationItem.measure(w, h);
			TextView title = (TextView) stationItem
					.findViewById(R.id.tv_waitStation);
			title.setText("您还没有搜索过任何站点");
			searchArrayList.add(stationItem);
			buslineContainer.addView(stationItem);
		} else {
			View stationItem = View.inflate(NearbyStationListView.this,
					R.layout.search_item_view, null);
			int w = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			stationItem.measure(w, h);
			TextView title = (TextView) stationItem
					.findViewById(R.id.tv_waitStation);
			title.setText("清空记录");
			searchArrayList.add(stationItem);
			buslineContainer.addView(stationItem);

			title.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// userdabase.deleteAllStationSearchHistory();
					getSearchList();
				}
			});
		}
	}

	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public void getAroundStation() {

		listData.clear();
		LocationUtil mApplication = LocationUtil
				.getInstance(NearbyStationListView.this);
		;
		BDLocation location = mApplication.getmLocation();
		mpoint = new LatLng(location.getLatitude(), location.getLongitude());
		stations.clear();
		stations = dabase.AcquireAroundStationsWithLocation(mpoint);
		Toast toast = Toast.makeText(getApplicationContext(), "在附近查找到"
				+ stations.size() + "个站点", Toast.LENGTH_LONG);
		toast.show();

		int j = stations.size();
		Map<String, Object> map = new HashMap<String, Object>();

		for (int i = 0; i < j; i++) {
			ArrayList<String> station = new ArrayList<String>();

			station = stations.get(i);
			map = new HashMap<String, Object>();
			// if (userdabase.checkFavStationWithStationID(station.get(1))) {
			// map.put("itemsTitle", station.get(1));
			// } else {
			// map.put("itemsTitle", station.get(1));
			// }
			map.put("itemsIcon", R.drawable.station);
			LatLng stationPoint = new LatLng(Float.parseFloat(station.get(2)),
					Float.parseFloat(station.get(3)));
			double distance = DistanceUtil.getDistance(mpoint, stationPoint);
			map.put("itemsText", "目前距离我 " + (int) (Math.floor(distance + 0.5))
					+ " 米");
			map.put("distance", (int) (Math.floor(distance + 0.5)) + "");
			listData.add(map);

		}

		myListViewAdapter.notifyDataSetChanged();

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
//		registerReceiver(mWifiReceiver, wifiFilter);
//		registerReceiver(mActivityReceiver, activityFilter);

	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // 保证 onPageEnd 在onPause
													// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
//		unregisterReceiver(mWifiReceiver);
//		unregisterReceiver(mActivityReceiver);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理

	}

	@Override
	public void onRestart() {
		super.onRestart();
		getSearchList();
	}

	private void initTitleRightLayout() {
		mAbTitleBar.clearRightView();
		View rightViewApp = mInflater.inflate(R.layout.app_btn, null);
		Button appBtn = (Button) rightViewApp.findViewById(R.id.appBtn);
		appBtn.setBackgroundDrawable(null);
		appBtn.setTextColor(Color.WHITE);
		appBtn.setText(" 地图显示 ");
		appBtn.setPadding(25, 5, 25, 5);
		appBtn.setTextSize(18);

		mAbTitleBar.addRightView(rightViewApp);

		appBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NearbyStationListView.this,
						SearchStationView.class);
				intent.putExtra(
						"TEXT",
						NearbyStationListView.this.getResources().getString(
								R.string.title_transparent_desc));
				// ���ñ�����͸��
				intent.putExtra("stationName", etSearch.getText().toString());
				startActivity(intent);
			}
		});

		MobclickAgent.updateOnlineConfig(this);
	}
}
