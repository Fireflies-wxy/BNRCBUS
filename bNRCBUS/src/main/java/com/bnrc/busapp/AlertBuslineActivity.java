package com.bnrc.busapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ab.view.titlebar.AbTitleBar;
import com.bnrc.adapter.MyAlertBuslineAdapter;
import com.bnrc.busapp.PollingService;
import com.bnrc.busapp.PollingUtils;
import com.bnrc.busapp.R;
import com.bnrc.ui.listviewswipedelete.BlogItem;
import com.bnrc.ui.listviewswipedelete.DelBlogItem;
import com.bnrc.ui.listviewswipedelete.ListViewCompat;
import com.bnrc.ui.listviewswipedelete.SlideAdapter;
import com.bnrc.ui.listviewswipedelete.SlideView;
import com.bnrc.ui.listviewswipedelete.SlideView.OnSlideListener;
import com.bnrc.ui.rjz.other.PullSeparateListView;
import com.bnrc.ui.rtBus.Child;
import com.bnrc.util.BeijingDBHelper;
import com.bnrc.util.DataBaseHelper;
import com.bnrc.util.UserDataDBHelper;
import com.bnrc.util.collectwifi.BaseActivity;
import com.bnrc.util.collectwifi.Constants;
import com.bnrc.util.collectwifi.ScanService;
import com.bnrc.util.collectwifi.ServiceUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AlertBuslineActivity extends BaseActivity implements
		OnItemClickListener {
	private static final String TAG = AlertBuslineActivity.class
			.getSimpleName();
	private PullSeparateListView mListView;
	private MyAlertBuslineAdapter mAdapter;
	private List<Map<String, ?>> mListData;
	private UserDataDBHelper mUserDB;
	private String stationName;
	private AbTitleBar mAbTitleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_alert_busline);
		stationName = getIntent().getStringExtra("STATION");
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(stationName);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleBarGravity(Gravity.CENTER, Gravity.CENTER);

		mListView = (PullSeparateListView) findViewById(R.id.list);
		mUserDB = UserDataDBHelper.getInstance(AlertBuslineActivity.this);
		mListData = new ArrayList<Map<String, ?>>();
		mAdapter = new MyAlertBuslineAdapter(AlertBuslineActivity.this,
				mListData, R.layout.child_alert_bottom, new String[] {
						"itemsTitle", "itemsLatitude", "itemsLongitude",
						"itemsAZ", "itemsSN" }, new int[] { R.id.iv_alerticon,
						R.id.tv_alert_line, R.id.iv_switch });
		mListView.setAdapter(mAdapter);
		mListView.setSeparateAll(true);
		// mListView.setShowDownAnim(true);
		// mListView.setOnItemClickListener(this);
		loadAlertInfo();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(AlertBuslineActivity.this,
				"onItemClick position=" + position, 0).show();

	}

	private void loadAlertInfo() {
		mListData.clear();
		// List<ArrayList<String>> data =
		// mUserDB.AcquireAlertBuslineInfo("123");
		ArrayList<Child> data = DataBaseHelper.getInstance(
				AlertBuslineActivity.this).AcquireAllBusLinesWithStation(
				stationName);
		for (Child info : data) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("itemsTitle", info.getLineName());// buslineName
			map.put("itemsLatitude", info.getLatitude());// lat
			map.put("itemsLongitude", info.getLongitude());// lon
			map.put("itemsAZ", info.getAZ());// AZ
			// map.put("itemsSN", info.getBuslineSN());// SN
			// Log.i(TAG, "SN: " + info.getBuslineSN());
			mListData.add(map);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i("BaseFragment", TAG + " onStart");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop polling service

		Log.i("BaseFragment", TAG + " onDestroy");

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("BaseFragment", TAG + " onStop");

	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen");
		MobclickAgent.onPause(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("BaseFragment", TAG + " onResume");
		loadAlertInfo();
		MobclickAgent.onPageStart("SplashScreen");
		MobclickAgent.onResume(this);
	}

}
