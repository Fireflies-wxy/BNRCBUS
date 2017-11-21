package com.bnrc.busapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import cn.domob.android.ads.AdEventListener;
import cn.domob.android.ads.AdView;
import cn.domob.android.ads.AdManager.ErrorCode;

import com.ab.activity.AbActivity;
import com.ab.global.AbConstant;
import com.ab.view.titlebar.AbTitleBar;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bnrc.activity.AboutActivity;
import com.bnrc.adapter.MyListViewAdapter;
import com.bnrc.busapp.R;
import com.bnrc.util.DataBaseHelper;
import com.bnrc.util.LocationUtil;
import com.bnrc.util.UserDataDBHelper;
import com.bnrc.util.collectwifi.BaseActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;

public class MyFavoriteStationView extends BaseActivity {

	private ListView mListView;
	public DataBaseHelper dabase = null;
	public UserDataDBHelper userdabase = null;
	public List<Map<String, Object>> listData;
	private MyListViewAdapter myListViewAdapter;

	RelativeLayout mAdContainer;
	AdView mAdview;
	private AbTitleBar mAbTitleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setAbContentView(R.layout.my_favorite_station_view);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("站点收藏");
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleBarGravity(Gravity.CENTER, Gravity.CENTER);
		initbase();

		initTitleRightLayout();

		String value = MobclickAgent.getConfigParams(
				MyFavoriteStationView.this, "open_ad");

		/*
		 * if (value.equals("1")) { mAdContainer = (RelativeLayout)
		 * findViewById(R.id.adcontainer); // Create ad view mAdview = new
		 * AdView(this, "56OJzfwIuN7tr9LoSs", "16TLmHWoAp8diNUdpuAEMYfi");
		 * SharedPreferences mySharedPreferences = getSharedPreferences(
		 * "setting", UserSettingView.MODE_PRIVATE); String agString =
		 * mySharedPreferences.getString("userAge", "20"); String sexString =
		 * mySharedPreferences.getString("userSex", "女");
		 * 
		 * if (sexString.equals("女")) { mAdview.setUserGender("female"); } else
		 * { mAdview.setUserGender("male"); } // mAdview.setKeyword("game");
		 * 
		 * Calendar mycalendar = Calendar.getInstance();// ��ȡ����ʱ�� String
		 * curYearString = String .valueOf(mycalendar.get(Calendar.YEAR));//
		 * ��ȡ��� int age = Integer.parseInt(agString); int birth =
		 * Integer.parseInt(curYearString) - age;
		 * mAdview.setUserBirthdayStr(birth + "-08-08");
		 * mAdview.setUserPostcode("123456"); mAdview.setAdEventListener(new
		 * AdEventListener() {
		 * 
		 * @Override public void onAdOverlayPresented(AdView adView) {
		 * Log.i("DomobSDKDemo", "overlayPresented"); }
		 * 
		 * @Override public void onAdOverlayDismissed(AdView adView) {
		 * Log.i("DomobSDKDemo", "Overrided be dismissed"); }
		 * 
		 * @Override public void onAdClicked(AdView arg0) {
		 * Log.i("DomobSDKDemo", "onDomobAdClicked"); }
		 * 
		 * @Override public void onLeaveApplication(AdView arg0) {
		 * Log.i("DomobSDKDemo", "onDomobLeaveApplication"); }
		 * 
		 * @Override public Context onAdRequiresCurrentContext() { return
		 * MyFavoriteStationView.this; }
		 * 
		 * @Override public void onAdFailed(AdView arg0, ErrorCode arg1) {
		 * Log.i("DomobSDKDemo", "onDomobAdFailed"); }
		 * 
		 * @Override public void onEventAdReturned(AdView arg0) {
		 * Log.i("DomobSDKDemo", "onDomobAdReturned"); } });
		 * RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
		 * LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		 * layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
		 * mAdview.setLayoutParams(layout); mAdContainer.addView(mAdview); }
		 */
		dabase = DataBaseHelper.getInstance(MyFavoriteStationView.this);
		userdabase = UserDataDBHelper.getInstance(MyFavoriteStationView.this);
		mListView = (ListView) this.findViewById(R.id.mStationListView);
		listData = new ArrayList<Map<String, Object>>();
		myListViewAdapter = new MyListViewAdapter(this, listData,
				R.layout.list_items, new String[] { "itemsIcon", "itemsTitle",
						"itemsText" }, new int[] { R.id.itemsIcon,
						R.id.tv_stationName, R.id.itemsText });
		mListView.setAdapter(myListViewAdapter);

		// mListView.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// final ArrayList<String> station = userdabase.favStations
		// .get(position);
		// final String alertString = userdabase
		// .checkAlertStationWithStation(station) ? "删除下车提醒"
		// : "添加到下车提醒";

		// final String favString = userdabase
		// .checkFavStationWithStationID(station.get(0)) ? "取消收藏站点"
		// : "添加收藏站点";
		// final String[] arrayChoice = new String[] { "查看站点详情",
		// alertString, favString };
		// new AlertDialog.Builder(MyFavoriteStationView.this)
		// // build AlertDialog
		// .setTitle("您可进行的操作")
		// // title
		// .setItems(arrayChoice,
		// new DialogInterface.OnClickListener() { // content
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// if (which == 0) {
		// Intent intent = new Intent(
		// MyFavoriteStationView.this,
		// StationInformationView.class);
		// intent.putExtra("title",
		// station.get(0));
		// intent.putExtra("latitude",
		// station.get(1));
		// intent.putExtra("longitude",
		// station.get(2));
		// startActivity(intent);
		// } else if (which == 1) {
		// if (alertString
		// .equalsIgnoreCase("删除下车提醒")) {
		// userdabase
		// .deleteAlertStationWithStation(station);
		// loadFavStationData();
		// Toast toast = Toast
		// .makeText(
		// getApplicationContext(),
		// "您已删除\""
		// + station
		// .get(0)
		// + "\"下车提醒站点",
		// Toast.LENGTH_LONG);
		// toast.show();
		// } else {
		// userdabase
		// .addAlertStationWithStation(station);
		// loadFavStationData();
		// Toast toast = Toast
		// .makeText(
		// getApplicationContext(),
		// "您已添加\""
		// + station
		// .get(0)
		// + "\"为下车提醒站点",
		// Toast.LENGTH_LONG);
		// toast.show();
		// }
		//
		// } else if (which == 2) {
		// if (favString
		// .equalsIgnoreCase("取消收藏站点")) {
		// userdabase
		// .deleteFavStationWithStation(station);
		// loadFavStationData();
		// Toast toast = Toast
		// .makeText(
		// getApplicationContext(),
		// "您已删除\""
		// + station
		// .get(0)
		// + "\"收藏站点",
		// Toast.LENGTH_LONG);
		// toast.show();
		// } else {
		// userdabase
		// .addFavStationWithStation(station);
		// loadFavStationData();
		// Toast toast = Toast
		// .makeText(
		// getApplicationContext(),
		// "您已成功收藏\""
		// + station
		// .get(0)
		// + "\"站点",
		// Toast.LENGTH_LONG);
		// toast.show();
		// }
		//
		// }
		// }
		// })
		// .setNegativeButton("取消",
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// dialog.dismiss(); // �ر�alertDialog
		// }
		// }).show();
		// }
		// });
		loadFavStationData();

	}

	public void loadFavStationData() {
		// userdabase.getFavStations();
		listData.clear();
		LocationUtil mApplication = LocationUtil
				.getInstance(MyFavoriteStationView.this);
		;
		BDLocation location = mApplication.getmLocation();
		final LatLng mypoint = new LatLng(location.getLatitude(),
				location.getLongitude());
		Comparator<ArrayList<String>> comparator = new Comparator<ArrayList<String>>() {
			public int compare(ArrayList<String> s1, ArrayList<String> s2) {
				LatLng stationPoint1 = new LatLng(Float.parseFloat(s1.get(1)),
						Float.parseFloat(s1.get(2)));
				double distance1 = DistanceUtil.getDistance(mypoint,
						stationPoint1);
				LatLng stationPoint2 = new LatLng(Float.parseFloat(s2.get(1)),
						Float.parseFloat(s2.get(2)));
				double distance2 = DistanceUtil.getDistance(mypoint,
						stationPoint2);
				if (distance1 > distance2) {
					return 1;
				} else if (distance1 < distance2) {
					return -1;
				}
				return 0;
			}
		};

		Collections.sort(userdabase.favStations, comparator);
		int j = userdabase.favStations.size();
		Map<String, Object> map = new HashMap<String, Object>();

		for (int i = 0; i < j; i++) {
			ArrayList<String> station = new ArrayList<String>();

			station = userdabase.favStations.get(i);
			map = new HashMap<String, Object>();

			if (userdabase.checkAlertStationWithStation(station)) {
				map.put("itemsIcon", R.drawable.alert_img);
			} else {
				map.put("itemsIcon", R.drawable.station);
			}

			map.put("itemsTitle", station.get(0));
			LatLng stationPoint = new LatLng(Float.parseFloat(station.get(1)),
					Float.parseFloat(station.get(2)));
			double distance = DistanceUtil.getDistance(mypoint, stationPoint);
			map.put("itemsText", "目前距离我 " + (int) (Math.floor(distance + 0.5))
					+ " 米");
			map.put("distance", (int) (Math.floor(distance + 0.5)) + "");
			listData.add(map);
			// Collections.sort(listData, new TestComparator());
			// for (Map<String, Object> m : listData) {
			// for (Map.Entry<String, Object> en : m.entrySet()) {
			// System.out.println(en.getKey() + " , " + en.getValue());
			// }
			// }
		}

		myListViewAdapter.notifyDataSetChanged();
	}

	public void searchStationWithStationName(String stationName) {

		int j = userdabase.favStations.size();
		for (int i = 0; i < j; i++) {
			ArrayList<String> station = new ArrayList<String>();
			station = userdabase.favStations.get(i);
			if (station.get(0).indexOf(stationName) == -1) {
				userdabase.favStations.remove(i);
				i--;
				j--;
			}
		}

		listData.clear();
		Map<String, Object> map = new HashMap<String, Object>();
		LocationUtil mApplication = LocationUtil
				.getInstance(MyFavoriteStationView.this);
		;
		BDLocation location = mApplication.getmLocation();
		final LatLng mypoint = new LatLng(location.getLatitude(),
				location.getLongitude());
		for (int i = 0; i < j; i++) {
			ArrayList<String> station = new ArrayList<String>();

			station = userdabase.favStations.get(i);
			map = new HashMap<String, Object>();
			map.put("itemsIcon", R.drawable.station);
			map.put("itemsTitle", station.get(0));
			LatLng stationPoint = new LatLng(Float.parseFloat(station.get(1)),
					Float.parseFloat(station.get(2)));
			double distance = DistanceUtil.getDistance(mypoint, stationPoint);
			map.put("itemsText", "目前距离我 " + (int) (Math.floor(distance + 0.5))
					+ " 米");
			map.put("distance", (int) (Math.floor(distance + 0.5)) + "");
			listData.add(map);
		}
		myListViewAdapter.notifyDataSetChanged();
	}

	public class TestComparator implements Comparator<Map<String, Object>> {

		@Override
		public int compare(Map<String, Object> o1, Map<String, Object> o2) {

			if (Float.parseFloat((String) o1.get("distance")) > Float
					.parseFloat((String) o2.get("distance"))) {
				return 1;
			} else if (Float.parseFloat((String) o1.get("distance")) < Float
					.parseFloat((String) o2.get("distance"))) {
				return -1;
			} else {
				return 0;
			}
		}

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // ͳ��ҳ��
		MobclickAgent.onResume(this); // ͳ��ʱ��
//		registerReceiver(mWifiReceiver, wifiFilter);
//		registerReceiver(mActivityReceiver, activityFilter);

	}

	public void onRestart() {
		super.onRestart();
		loadFavStationData();
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // ��֤ onPageEnd ��onPause
													// ֮ǰ����,��Ϊ onPause
													// �лᱣ����Ϣ
		MobclickAgent.onPause(this);
//		unregisterReceiver(mWifiReceiver);
//		unregisterReceiver(mActivityReceiver);

	}

	private void initTitleRightLayout() {
		mAbTitleBar.clearRightView();
		View rightViewApp = mInflater.inflate(R.layout.app_btn, null);
		Button appBtn = (Button) rightViewApp.findViewById(R.id.appBtn);
		appBtn.setBackgroundDrawable(null);
		appBtn.setTextColor(Color.WHITE);
		appBtn.setText(" 添加 ");
		appBtn.setPadding(25, 5, 25, 5);
		appBtn.setTextSize(18);

		mAbTitleBar.addRightView(rightViewApp);

		appBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = null;
				Intent intent = new Intent(MyFavoriteStationView.this,
						NearbyStationListView.class);
				intent.putExtra(
						"TEXT",
						MyFavoriteStationView.this.getResources().getString(
								R.string.title_transparent_desc));
				startActivity(intent);
			}

		});

		MobclickAgent.updateOnlineConfig(this);
	}

}
