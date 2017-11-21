package com.bnrc.util.collectwifi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.bnrc.busapp.R;
import com.bnrc.util.DataBaseHelper;
import com.bnrc.util.LocationUtil;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MyDialogSelectBuslineActivity extends Activity {

	private EditText edt_input;
	private ImageView iv_delete;
	private Button btn_switch;
	private GridView gv_buslines;
	private List<Map<String, String>> mNearBuslineList;// 公交列表适配数据
	private MySelectBuslineGridAdapter mGridAdapter = null;
	// private BeijingDBHelper mBeijingDBHelperInstance;
	private DataBaseHelper mDataBaseHelper = null;
	private CollectWifiDBHelper mCollectWifiDBHelperInstance = null;
	private LocationUtil mLocationUtilInstance = null;
	private BDLocation mBDLocation = null;
	private boolean IsWhole = false;
	private String sureMac = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_selectbusline);
		System.out.println("onCreate");

		Intent intent = getIntent();
		sureMac = intent.getStringExtra("sureMac");
		edt_input = (EditText) findViewById(R.id.edt_selectbusline_input);
		iv_delete = (ImageView) findViewById(R.id.iv_selectbusline_delete);
		btn_switch = (Button) findViewById(R.id.btn_selectbusline_search);
		gv_buslines = (GridView) findViewById(R.id.gv_selectbusline_buslinetable);

		mDataBaseHelper = DataBaseHelper.getInstance(getApplicationContext());
		mCollectWifiDBHelperInstance = CollectWifiDBHelper
				.getInstance(getApplicationContext());
		mLocationUtilInstance = LocationUtil
				.getInstance(getApplicationContext());
		mBDLocation = mLocationUtilInstance.getmLocation();

		mNearBuslineList = new ArrayList<Map<String, String>>();
		mGridAdapter = new MySelectBuslineGridAdapter(
				MyDialogSelectBuslineActivity.this, mNearBuslineList);
		gv_buslines.setAdapter(mGridAdapter);
		btn_switch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				if (IsWhole) {
					btn_switch.setText("全部公交");
					IsWhole = false;
				} else {
					btn_switch.setText("附近公交");
					IsWhole = true;
				}
			}
		});
		iv_delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				edt_input.setText("");

			}
		});
		edt_input.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable s) {
				String contentStr = s.toString().trim();
				if (contentStr == null || contentStr.length() <= 0)// 判断contentStr是否为空,判断字符串是否为空典型写法
				{
					Log.i("AUTO", "afterTextChanged null");
					if (!IsWhole) {
						mNearBuslineList.clear();
						LatLng myPoint = new LatLng(mBDLocation.getLatitude(),
								mBDLocation.getLongitude());
						ArrayList<Map<String, String>> listData = mDataBaseHelper
								.getNearBusInfo(myPoint);
						for (Map<String, String> map : listData) {
							mNearBuslineList.add(map);
						}
						// gridAdapter.notifyDataSetChanged();
						Log.i("TEXTCHANGE", "空+附近");
						for (Map<String, String> map : mNearBuslineList)
							Log.i("TEXTCHANGE", map.get("线路") + "\n");
					} else {
						mNearBuslineList.clear();
						// gridAdapter.notifyDataSetChanged();
						Log.i("TEXTCHANGE", "空+全局");

					}
				} else {
					Log.i("AUTO", "afterTextChanged not null");
					Cursor cursor = mDataBaseHelper
							.FindBusByKeyname(contentStr);
					if (IsWhole) {
						mNearBuslineList.clear();
						while (cursor.moveToNext()) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("线路", cursor.getString(cursor
									.getColumnIndex("NAME")));
							map.put("方向", cursor.getString(cursor
									.getColumnIndex("S_END")));
							mNearBuslineList.add(map);
						}
					} else {
						mNearBuslineList.clear();
						Pattern p = Pattern.compile("^[\u4e00-\u9fa5]*"
								+ contentStr + "(.*)\\S{0,}");
						LatLng myPoint = new LatLng(mBDLocation.getLatitude(),
								mBDLocation.getLongitude());
						ArrayList<Map<String, String>> listData = mDataBaseHelper
								.getNearBusInfo(myPoint);
						for (Map<String, String> map : listData) {
							Matcher m = p.matcher(map.get("线路"));
							if (m.find()) {
								mNearBuslineList.add(map);
							}
						}

					}

				}
				mGridAdapter.notifyDataSetChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// sure.setEnabled(false);// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}
		});

		gv_buslines.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Map<String, String> sureData = new HashMap<String, String>();
				String[] wifiInfo = sureMac.split(";");
				sureData.put("线路", mNearBuslineList.get(arg2).get("线路"));
				sureData.put("SSID", wifiInfo[1]);
				sureData.put("MAC", wifiInfo[0]);
				mCollectWifiDBHelperInstance.InsertSureData(sureData);
				NotifyAdmin.IsShow = false;
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				MyDialogSelectBuslineActivity.this.finish();

			}
		});
		loadBuslineInfo();
	}

	private void loadBuslineInfo() {
		mNearBuslineList.clear();
		LatLng myPoint = new LatLng(mBDLocation.getLatitude(),
				mBDLocation.getLongitude());
		ArrayList<Map<String, String>> listData = mDataBaseHelper
				.getNearBusInfo(myPoint);
		for (Map<String, String> map : listData) {
			mNearBuslineList.add(map);
		}
		Log.i("CURSOR", mNearBuslineList.toString());
		mGridAdapter.notifyDataSetChanged();
		Toast.makeText(MyDialogSelectBuslineActivity.this,
				"系统确定到您正在乘坐公交车，请选择所乘坐的线路！！！！", 1000).show();
		if (mNearBuslineList.size() != 0) {
			IsWhole = false;
			btn_switch.setEnabled(true);
			btn_switch.setText("所有公交");
		} else
			IsWhole = true;
	}

	public void onResume() {
		super.onResume();
		System.out.println("onResume");

		MobclickAgent.onResume(this);

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		System.out.println("onRestart");

	}

	public void onStart() {
		super.onStart();
		System.out.println("onStart");

	}

	public void onStop() {
		super.onStop();
		System.out.println("onStop");

	}

	public void onPause() {
		super.onPause();
		System.out.println("onPause");

	}

	public void onDestroy() {
		super.onDestroy();
		System.out.println("onDestroy");
		NotifyAdmin.IsShow = false;

	}
}
