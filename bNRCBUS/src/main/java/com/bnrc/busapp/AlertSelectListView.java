package com.bnrc.busapp;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.ab.global.AbConstant;
import com.ab.view.titlebar.AbTitleBar;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bnrc.adapter.AlertAdapter;
import com.bnrc.adapter.NearAdapter;
import com.bnrc.adapter.StationsAdapter;
import com.bnrc.busapp.StationListView.loadDataBaseTask;
import com.bnrc.network.JsonObjectRequest;
import com.bnrc.network.MyVolley;
import com.bnrc.network.StringRequest;
import com.bnrc.network.toolbox.Response;
import com.bnrc.network.toolbox.VolleyError;
import com.bnrc.network.toolbox.Request.Method;
import com.bnrc.ui.refresh.IPullRefresh;
import com.bnrc.ui.refresh.PullLoadMenuListView;
import com.bnrc.ui.rjz.BaseFragment;
import com.bnrc.ui.rjz.MainActivity;
import com.bnrc.ui.rjz.IPopWindowListener;
import com.bnrc.ui.rjz.SelectPicPopupWindow;
import com.bnrc.ui.rtBus.Child;
import com.bnrc.ui.rtBus.Group;
import com.bnrc.ui.rtBus.PinnedHeaderExpandableConcernAdapter;
import com.bnrc.ui.rtBus.PinnedHeaderExpandableConcernListView;
import com.bnrc.ui.swipemenu.SwipeMenu;
import com.bnrc.ui.swipemenu.SwipeMenuCreator;
import com.bnrc.ui.swipemenu.SwipeMenuExpandableAdapter;
import com.bnrc.ui.swipemenu.SwipeMenuExpandableListView;
import com.bnrc.ui.swipemenu.SwipeMenuItem;
import com.bnrc.util.AnimationUtil;
import com.bnrc.util.BuslineDBHelper;
import com.bnrc.util.DataBaseHelper;
import com.bnrc.util.LocationUtil;
import com.bnrc.util.PCDataBaseHelper;
import com.bnrc.util.PCUserDataDBHelper;
import com.bnrc.util.UserDataDBHelper;
import com.bnrc.util.VolleyNetwork;
import com.bnrc.util.VolleyNetwork.requestListener;
import com.bnrc.util.collectwifi.BaseActivity;
import com.bnrc.util.collectwifi.Constants;
import com.umeng.analytics.MobclickAgent;

public class AlertSelectListView extends BaseActivity {
	private static final String TAG = AlertSelectListView.class.getSimpleName();
	public PCDataBaseHelper mDataManager = null;
	private String StationName;
	private List<Group> mGroups = null;
	private PCUserDataDBHelper mUserDB = null;
	private PCDataBaseHelper mDataBaseHelper = null;
	private loadDataBaseTask mTask;
	private PullLoadMenuListView mAlertListView;
	private AlertAdapter mAlertAdapter;
	// 定义Handler对象
	private Handler mHandler = new Handler();
	private AbTitleBar mAbTitleBar;

	private SwipeMenuExpandableListView.OnGroupExpandListener mOnGroupExpandListener = new SwipeMenuExpandableListView.OnGroupExpandListener() {

		@Override
		public void onGroupExpand(int pos) {

		}
	};

	private SwipeMenuExpandableListView.OnChildClickListener mOnChildExpandListener = new SwipeMenuExpandableListView.OnChildClickListener() {

		@Override
		public boolean onChildClick(ExpandableListView paramExpandableListView,
				View paramView, int paramInt1, int paramInt2, long paramLong) {
			// TODO Auto-generated method stub
			Child child = mGroups.get(paramInt1).getChildItem(paramInt2);
			if (child.isAlertOpen() == Child.OPEN) {
				child.setAlertOpen(Child.CLOSE);
				// mUserDB.closeAlertBusline(LineID, StationName);
				mUserDB.closeAlertBusline(child);
			} else {
				child.setAlertOpen(Child.OPEN);
				mUserDB.openAlertBusline(child);
			}
			mAlertAdapter.notifyDataSetChanged();
			return false;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_alert_select_list);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleTextMargin(20, 0, 0, 0);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleBarGravity(Gravity.CENTER, Gravity.CENTER);
		mAbTitleBar.setTitleText("选择提醒线路");
		initbase();
		mUserDB = PCUserDataDBHelper.getInstance(this);
		mDataBaseHelper = PCDataBaseHelper.getInstance(this);
		StationName = getIntent().getStringExtra("StationName");
		mAlertListView = (PullLoadMenuListView) findViewById(R.id.explistview_alert_station);
		mAlertAdapter = new AlertAdapter(mGroups, this);
		mAlertListView.setAdapter(mAlertAdapter);
		mAlertAdapter.setType(AlertAdapter.DONTADDALERT);
		mAlertListView.setOnGroupExpandListener(mOnGroupExpandListener);
		mAlertListView.setOnChildClickListener(mOnChildExpandListener);
		mAlertListView.setPullToRefreshEnable(false);
	}

	private List<Group> loadBusline() {
		mGroups = mDataBaseHelper.acquireAllBusLinesWithStation(StationName);
		return mGroups;
	}

	private void loadDataBase() {
		if (mTask != null)
			mTask.cancel(true);
		mTask = new loadDataBaseTask(this);
		mTask.execute();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // ͳ��ҳ��
		MobclickAgent.onResume(this); // ͳ��ʱ��
		// registerReceiver(mWifiReceiver, wifiFilter);
		// registerReceiver(mActivityReceiver, activityFilter);
		loadDataBase();
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen");
		MobclickAgent.onPause(this);
		// unregisterReceiver(mWifiReceiver);
		// unregisterReceiver(mActivityReceiver);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onRestart() {
		super.onRestart();
	}

	class loadDataBaseTask extends AsyncTask<Integer, Integer, List<Group>> {
		// 后面尖括号内分别是参数（线程休息时间），进度(publishProgress用到)，返回值 类型

		private Context mContext = null;

		public loadDataBaseTask(Context context) {
			this.mContext = context;
		}

		/*
		 * 第一个执行的方法 执行时机：在执行实际的后台操作前，被UI 线程调用
		 * 作用：可以在该方法中做一些准备工作，如在界面上显示一个进度条，或者一些控件的实例化，这个方法可以不用实现。
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			Log.d(TAG, "onPreExecute");
			super.onPreExecute();
		}

		/*
		 * 执行时机：在onPreExecute 方法执行后马上执行，该方法运行在后台线程中 作用：主要负责执行那些很耗时的后台处理工作。可以调用
		 * publishProgress方法来更新实时的任务进度。该方法是抽象方法，子类必须实现。
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected List<Group> doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			Log.d(TAG, "doInBackground");
			publishProgress();
			return loadBusline();
		}

		/*
		 * 执行时机：这个函数在doInBackground调用publishProgress时被调用后，UI
		 * 线程将调用这个方法.虽然此方法只有一个参数,但此参数是一个数组，可以用values[i]来调用
		 * 作用：在界面上展示任务的进展情况，例如通过一个进度条进行展示。此实例中，该方法会被执行100次
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onProgressUpdate");
			showLoading();
			super.onProgressUpdate(values);
		}

		/*
		 * 执行时机：在doInBackground 执行完成后，将被UI 线程调用 作用：后台的计算结果将通过该方法传递到UI
		 * 线程，并且在界面上展示给用户 result:上面doInBackground执行后的返回值，所以这里是"执行完毕"
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(List<Group> result) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onPostExecute");
			// super.onPostExecute(result);
			if (result != null && result.size() > 0) {
				mAlertAdapter.updateData(result);
				mAlertAdapter.notifyDataSetChanged();
				mAlertListView.expandGroup(0);
			} else {
				// mNearHint.setVisibility(View.VISIBLE);
			}
			dismissLoading();
		}

	}

}