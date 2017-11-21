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

public class MyFavoriteBuslineView extends BaseActivity {

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
		setAbContentView(R.layout.my_favorite_busline_view);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("线路收藏");
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleBarGravity(Gravity.CENTER, Gravity.CENTER);
		initbase();

		initTitleRightLayout();

		String value = MobclickAgent.getConfigParams(
				MyFavoriteBuslineView.this, "open_ad");

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
		 * MyFavoriteBuslineView.this; }
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

		dabase = DataBaseHelper.getInstance(MyFavoriteBuslineView.this);
		userdabase = UserDataDBHelper.getInstance(MyFavoriteBuslineView.this);
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
		// final ArrayList<String> busline = userdabase.favBuslines
		// .get(position);
		// final String favString = userdabase
		// .checkFavBuslineWithBuslineID(busline.get(0)) ? "取消收藏线路"
		// : "添加收藏线路";
		// final String[] arrayChoice = new String[] { "查看站点详情", favString };
		// new AlertDialog.Builder(MyFavoriteBuslineView.this)
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
		// MyFavoriteBuslineView.this,
		// BuslineListView.class);
		// intent.putExtra("title",
		// busline.get(1));
		// intent.putExtra("buslineID",
		// busline.get(0));
		// intent.putExtra("buslineName",
		// busline.get(2));
		// intent.putExtra("stationName", "");
		// startActivity(intent);
		// } else if (which == 1) {
		// if (favString
		// .equalsIgnoreCase("取消收藏线路")) {
		// userdabase
		// .deleteFavBuslineWithBusline(busline);
		// loadFavStationData();
		// Toast toast = Toast
		// .makeText(
		// getApplicationContext(),
		// "您已删除\""
		// + busline
		// .get(2)
		// + "\"收藏",
		// Toast.LENGTH_LONG);
		// toast.show();
		// }
		// // else {
		// // userdabase
		// // .addFavBuslineWithBusline(busline);
		// // loadFavStationData();
		// // Toast toast = Toast
		// // .makeText(
		// // getApplicationContext(),
		// // "您已成功收藏\""
		// // + busline
		// // .get(2)
		// // + "\"线路",
		// // Toast.LENGTH_LONG);
		// // toast.show();
		// // }
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
		// userdabase.getAllFavBuslines();
		listData.clear();
		int j = userdabase.favBuslines.size();
		Map<String, Object> map = new HashMap<String, Object>();

		for (int i = 0; i < j; i++) {
			ArrayList<String> station = new ArrayList<String>();

			station = userdabase.favBuslines.get(i);
			map = new HashMap<String, Object>();
			map.put("itemsIcon", R.drawable.bus_img);
			map.put("itemsTitle", station.get(1));
			map.put("itemsText", station.get(2));
			listData.add(map);
		}

		myListViewAdapter.notifyDataSetChanged();
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
				Intent intent = new Intent(MyFavoriteBuslineView.this,
						SearchBuslineView.class);
				intent.putExtra("keyword", "");
				intent.putExtra(
						"TEXT",
						MyFavoriteBuslineView.this.getResources().getString(
								R.string.title_transparent_desc));
				startActivity(intent);
			}

		});

		MobclickAgent.updateOnlineConfig(this);
	}

}
