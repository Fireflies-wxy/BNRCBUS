/*package com.bnrc.busapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import u.aly.aa;
import android.R.integer;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import cn.domob.android.ads.S;

import com.ab.activity.AbActivity;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bnrc.network.MyVolley;
import com.bnrc.network.StringRequest;
import com.bnrc.network.toolbox.Response;
import com.bnrc.network.toolbox.VolleyError;
import com.bnrc.util.BuslineDBHelper;
import com.bnrc.util.DataBaseHelper;
import com.bnrc.util.LocationUtil;
import com.bnrc.util.SlidingDrawerGridView;
import com.bnrc.util.UserDataDBHelper;
import com.bnrc.util.collectwifi.BaseActivity;
import com.google.gson.JsonArray;
import com.umeng.analytics.MobclickAgent;

public class StationInformationView extends BaseActivity {

	public DataBaseHelper mUserDB = null;
	private String stationTitle;
	private String latitude, longtitude;
	private LatLng stationPoint = null;
	private ArrayList<ArrayList<ArrayList<String>>> mAllStations = null;
	private ArrayList<ArrayList<String>> curbuslines = null;
	private ArrayList<ArrayList<String>> rtbuslines = null;
	private SlidingDrawerGridView allBuslines;
	private MyGridAdapter allBuslinesAdapter;
	public LatLng mpoint = null;
	public LocationUtil mApplication = null;
	public List<HashMap<String, Object>> rtbusListData;
	public List<HashMap<String, Object>> rtbusListData2;
	private ArrayList<View> rtBusArrayList = null;
	private LinearLayout buslineContainer;
	private ScrollView mainScrollView;
	private UserDataDBHelper userdabase = null;
	private Handler mHandler;

	private int firstTimeRequest = 0;
	private Button favBtn;
	private Button alertBtn;
	private Button gotoBtn;
	private Timer timer;
	private TimerTask task;
	private ArrayList<String> curStation;
	private Button appBtn = null;

	private ProgressDialog pd;
	// 定义Handler对象
	private Handler handler = new Handler() {
		@Override
		// 当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 只要执行到这里就关闭对话框
			pd.dismiss();
			buslineContainer.setVisibility(View.VISIBLE);
			mainScrollView.setVisibility(View.VISIBLE);
			initData();

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setAbContentView(R.layout.station_infomation);
		this.setTitleTextMargin(20, 0, 0, 0);
		this.setLogo(R.drawable.button_selector_back);
		this.setTitleLayoutBackground(R.drawable.top_bg);
		this.setTitleLayoutGravity(Gravity.CENTER, Gravity.CENTER);
		initbase();
		mUserDB = DataBaseHelper.getInstance(StationInformationView.this);
		rtbuslines = new ArrayList<ArrayList<String>>();
		Intent intent = getIntent();
		this.setTitleText(intent.getStringExtra("title"));
		stationTitle = intent.getStringExtra("title");
		latitude = intent.getStringExtra("latitude");
		longtitude = intent.getStringExtra("longitude");
		stationPoint = new LatLng(Float.parseFloat(intent
				.getStringExtra("latitude")), Float.parseFloat(intent
				.getStringExtra("longitude")));
		curStation = new ArrayList<String>();
		curStation.add(stationTitle);
		curStation.add(intent.getStringExtra("latitude"));
		curStation.add(intent.getStringExtra("longitude"));
		// UserDataDBHelper.getInstance(StationInformationView.this)
		// .addSearchStaitonWithStation(curStation);
		buslineContainer = (LinearLayout) findViewById(R.id.realtime_bus);
		mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
		buslineContainer.setVisibility(View.INVISIBLE);
		mainScrollView.setVisibility(View.INVISIBLE);
		// 构建一个下载进度条
		pd = ProgressDialog.show(StationInformationView.this, "数据加载中…", "请等待");
		new Thread() {
			public void run() {
				// 在这里执行长耗时方法
				mAllStations = mUserDB
						.getBothsideBusLinesWithStation(curStation);
				// 执行完毕后给handler发送一个消息
				handler.sendEmptyMessage(0);
			}
		}.start();
		// allStations = dabase.getBothsideBusLinesWithStation(curStation);

	}

	public void initData() {
		if (mAllStations.size() > 1) {
			initTitleRightLayout();
		}
		userdabase = UserDataDBHelper.getInstance(StationInformationView.this);
		String alertString = userdabase
				.checkAlertStationWithStation(curStation) ? "删除提醒" : "下车提醒";
		// String favString = userdabase
		// .checkFavStationWithStationID(stationTitle) ? "取消收藏" : "添加收藏";
		favBtn = (Button) findViewById(R.id.lLayout_addFav);
		// favBtn.setText(favString);
		// favBtn.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// if (favBtn.getText().toString().equalsIgnoreCase("取消收藏")) {
		// userdabase.deleteFavStationWithStation(curStation);
		// Toast toast = Toast.makeText(getApplicationContext(),
		// "您已取消收藏\"" + curStation.get(0) + "\"站点",
		// Toast.LENGTH_LONG);
		// favBtn.setText("添加收藏");
		// } else {
		// userdabase.addFavStationWithStation(curStation);
		// Toast toast = Toast.makeText(getApplicationContext(),
		// "您已成功收藏\"" + curStation.get(0) + "\"站点",
		// Toast.LENGTH_LONG);
		// toast.show();
		// favBtn.setText("取消收藏");
		// }
		// }
		// });

		alertBtn = (Button) findViewById(R.id.isAlertbtn);
		alertBtn.setText(alertString);
		alertBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (alertBtn.getText().toString().equalsIgnoreCase("删除提醒")) {
					userdabase.deleteAlertStationWithStation(curStation);
					Toast toast = Toast.makeText(getApplicationContext(),
							"您已删除\"" + curStation.get(0) + "\"下车提醒站点",
							Toast.LENGTH_LONG);
					toast.show();
					alertBtn.setText("下车提醒");
				} else {
					userdabase.addAlertStationWithStation(curStation);
					Toast toast = Toast.makeText(getApplicationContext(),
							"您已添加\"" + curStation.get(0) + "\"为下车提醒站点",
							Toast.LENGTH_LONG);
					toast.show();
					alertBtn.setText("删除提醒");
				}
			}
		});

		gotoBtn = (Button) findViewById(R.id.gotobtn);
		gotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isNetworkConnected(StationInformationView.this)) {
					Intent intent = new Intent(StationInformationView.this,
							StationRoutView.class);
					intent.putExtra("title", curStation.get(0));
					intent.putExtra("latitude", curStation.get(1));
					intent.putExtra("longitude", curStation.get(2));
					startActivity(intent);
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"您的网络有问题，请检查~", Toast.LENGTH_LONG);
					toast.show();
				}

			}
		});

		allBuslines = (SlidingDrawerGridView) findViewById(R.id.allbuslines);
		curbuslines = new ArrayList<ArrayList<String>>();
		allBuslinesAdapter = new MyGridAdapter(curbuslines);
		allBuslines.setAdapter(allBuslinesAdapter);

		allBuslines.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(StationInformationView.this,
						BuslineListView.class);
				ArrayList<String> busline = new ArrayList<String>();
				busline = curbuslines.get(position);
				intent.putExtra("title", busline.get(1));
				intent.putExtra("buslineID", busline.get(0));
				intent.putExtra("buslineName", busline.get(2));
				intent.putExtra("stationName", stationTitle);
				startActivity(intent);

			}
		});

		// allBuslines.setOnItemLongClickListener(new OnItemLongClickListener()
		// {
		// @Override
		// public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
		// final int arg2, long arg3) {
		// // TODO Auto-generated method stub
		// final ArrayList<String> busline = curbuslines.get(arg2);
		//
		// final String favString = userdabase
		// .checkFavBuslineWithBuslineID(busline.get(0)) ? "取消收藏该线路"
		// : "收藏该线路";
		// final String[] arrayChoice = new String[] { "查看该线路", favString };
		// new AlertDialog.Builder(StationInformationView.this)
		// .setTitle("您可进行的操作")
		// .setItems(arrayChoice,
		// new DialogInterface.OnClickListener() { // content
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// if (which == 0) {
		// // Intent intent = null;
		// Intent intent = new Intent(
		// StationInformationView.this,
		// BuslineListView.class);
		// ArrayList<String> busline = new ArrayList<String>();
		// busline = curbuslines.get(arg2);
		// intent.putExtra("title",
		// busline.get(1));
		// intent.putExtra("buslineID",
		// busline.get(0));
		// intent.putExtra("buslineName",
		// busline.get(2));
		// intent.putExtra("stationName",
		// stationTitle);
		// startActivity(intent);
		// } else if (which == 1) {
		// if (favString
		// .equalsIgnoreCase("取消收藏该线路")) {
		// userdabase
		// .deleteFavBuslineWithBuslineAndStation(
		// busline,
		// curStation);
		// num--;
		// getChangeBuslines();
		// Toast toast = Toast
		// .makeText(
		// getApplicationContext(),
		// "您已取消收藏\""
		// + busline
		// .get(2)
		// + "路",
		// Toast.LENGTH_LONG);
		// toast.show();
		// // userdabase.deleteFavStationWithStation(curStation);
		// //
		// // favBtn.setText("添加收藏");
		//
		// } else {
		// busline.add(stationTitle);
		// busline.add(latitude);
		// busline.add(longtitude);
		// userdabase
		// .addFavBuslineWithBusline(busline);
		// num--;
		// getChangeBuslines();
		//
		// Toast toast = Toast
		// .makeText(
		// getApplicationContext(),
		// "您已成功收藏\""
		// + busline
		// .get(2)
		// + "路"
		// + stationTitle
		// + "\"站点",
		// Toast.LENGTH_LONG);
		// toast.show();
		// // userdabase
		// // .addFavStationWithStation(curStation);
		// // userdabase
		// // .addRTBusWithBuslineAndStation(
		// // busline,
		// // curStation);
		// favBtn.setText("取消收藏");
		// }
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
		// return true;
		// }
		// });

		rtbusListData = new ArrayList<HashMap<String, Object>>();
		rtbusListData2 = new ArrayList<HashMap<String, Object>>();
		mApplication = LocationUtil.getInstance(StationInformationView.this);
		BDLocation location = mApplication.mLocation;
		mpoint = new LatLng(location.getLatitude(), location.getLongitude());

		getChangeBuslines();

		PlanNode st = PlanNode.withLocation(mpoint);
		PlanNode ed = PlanNode.withLocation(stationPoint);

		double distance = DistanceUtil.getDistance(mpoint, stationPoint);
		// ʵ����SharedPreferences���󣨵�һ����
		SharedPreferences mySharedPreferences = getSharedPreferences("setting",
				SettingView.MODE_PRIVATE);
		String distanceString = mySharedPreferences.getString("searchRMode",
				"1000米");
		Integer.parseInt(distanceString.substring(0,
				distanceString.length() - 1));

		task = new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						if (isNetworkConnected(StationInformationView.this)) {
							GetAllBuses();
						} else {
						}

					}
				});
			}
		};

		timer = new Timer(true);
		String timeString = mySharedPreferences.getString("refreshMode", "30秒");
		timeString = timeString.substring(0, timeString.length() - 1);
		timer.schedule(task, 200, Integer.parseInt(timeString) * 1000); // 延时1000ms后执行，1000ms执行一次

		TimerTask task2 = new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						firstTimeRequest++;
						refreshRealtimeBuses();
					}
				});
			}
		};

		Timer timer2 = new Timer(true);
		timer2.schedule(task2, 0, Integer.parseInt(timeString) * 1000); // 延时1000ms后执行，1000ms执行一次
		// timer.cancel(); //退出计时器

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (isNetworkConnected(StationInformationView.this)) {
			map.put("itemsTitle", "暂未发现实时公交");
		} else {
			map.put("itemsTitle", "网络有问题~");
		}
		map.put("itemsIcon", R.drawable.bus_img);

		map.put("itemsText", "<font color=\"yellow\">您可尝试收藏更多线路~</font>");
		rtbusListData.add(map);
		refreshRealtimeBuses();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (firstTimeRequest >= 1) {
					rtbusListData.clear();
					for (int i = 0; i < rtbusListData2.size(); i++) {
						rtbusListData.add(rtbusListData2.get(i));
					}
					refreshRealtimeBuses();
				}
			}
		};
	}

	public void refreshRealtimeBuses() {
		if (appBtn != null) {
			appBtn.setClickable(true);
		}
		if (rtBusArrayList == null) {
			rtBusArrayList = new ArrayList<View>();
		} else {
			int m = rtBusArrayList.size();
			for (int i = 0; i < m; i++) {
				buslineContainer.removeView(rtBusArrayList.get(i));
				rtBusArrayList.remove(i);
				i--;
				m--;
			}
		}

		int j = rtbusListData.size();
		HashMap<String, Object> rtBus;
		for (int i = 0; i < j; i++) {
			rtBus = rtbusListData.get(i);
			View stationItem = View.inflate(StationInformationView.this,
					R.layout.bus_item_view, null);
			stationItem.setTag(i + "");
			TextView title = (TextView) stationItem
					.findViewById(R.id.tv_waitStation);
			TextView info = (TextView) stationItem.findViewById(R.id.tv_info);
			TextView bustitle = (TextView) stationItem
					.findViewById(R.id.tv_buslineName);
			TextView stationinfo = (TextView) stationItem
					.findViewById(R.id.station_title);
			LinearLayout itemsContainer = (LinearLayout) stationItem
					.findViewById(R.id.itemsContainer);
			LinearLayout itemsContainer2 = (LinearLayout) stationItem
					.findViewById(R.id.itemsContainer2);
			title.setText(rtBus.get("itemsTitle").toString());
			if (rtBus.get("itemsTitle").toString().equalsIgnoreCase("网络有问题~")) {
				info.setVisibility(View.GONE);
				itemsContainer.setVisibility(View.GONE);
				itemsContainer2.setVisibility(View.VISIBLE);
			} else if (rtBus.get("itemsTitle").toString()
					.equalsIgnoreCase("暂未发现实时公交")) {
				itemsContainer.setVisibility(View.GONE);
				info.setVisibility(View.VISIBLE);
				itemsContainer2.setVisibility(View.VISIBLE);
				info.setText(Html.fromHtml(rtBus.get("itemsText").toString()));
			} else {
				itemsContainer2.setVisibility(View.GONE);
				title.setText(Html.fromHtml(rtBus.get("itemsTitle").toString()));
				info.setText(Html.fromHtml(rtBus.get("itemsText").toString()));
				bustitle.setText(Html.fromHtml(rtBus.get("bus_title")
						.toString()));
				stationinfo.setText(Html.fromHtml(rtBus.get("station_title")
						.toString()));
				stationItem.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(StationInformationView.this,
								BuslineListView.class);
						@SuppressWarnings("unchecked")
						ArrayList<String> busline = (ArrayList<String>) rtbusListData
								.get(Integer.parseInt(arg0.getTag().toString()))
								.get("busline");
						intent.putExtra("title", busline.get(1));
						intent.putExtra("buslineID", busline.get(0));
						intent.putExtra("buslineName", busline.get(2));
						intent.putExtra("stationName", stationTitle);
						startActivity(intent);
					}
				});
			}
			if (rtBusArrayList.size() > 0
					&& rtBus.get("itemsTitle").toString()
							.equalsIgnoreCase("暂未发现实时公交")) {
			} else {
				rtBusArrayList.add(stationItem);
				buslineContainer.addView(stationItem);
			}

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

	public void GetAllBuses() {
		if (isNetworkConnected(StationInformationView.this)) {
			rtbusListData.clear();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemsIcon", R.drawable.bus_img);
			map.put("itemsTitle", "暂未发现实时公交");
			map.put("itemsText", "<font color=\"yellow\">您可尝试收藏更多线路~</font>");
			rtbusListData2.add(map);
			for (int i = 0; i < rtbusListData2.size(); i++) {
				rtbusListData.add(rtbusListData2.get(i));
			}
			rtbusListData2.clear();
			rtbusListData2.add(map);

			int j = rtbuslines.size();
			for (int i = 0; i < j; i++) {
				ArrayList<String> busline = new ArrayList<String>();
				busline = rtbuslines.get(i);
				try {
					get_realtime_data(busline, 1);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void get_realtime_data(final ArrayList<String> busline,
			int station_num) throws JSONException, UnsupportedEncodingException {
		Map<String,Object> buslineArrayList = BuslineDBHelper.getInstance(
				StationInformationView.this).AcquireOffLineInfoWithBuslineName(
				busline.get(2));
		if (buslineArrayList.size() > 0) {
			String JSONDataUrl = "http://bjgj.aibang.com:8899/bus.php?city="
					+ URLEncoder.encode("北京", "utf-8") + "&id="
					+ buslineArrayList.get(3) + "&no=" + station_num
					+ "&type=0&encrypt=0&versionid=2";
			String value = MobclickAgent.getConfigParams(
					StationInformationView.this, "rtbusurl");
			if (value.toString().length() > 0) {
				JSONDataUrl = value + "&" + URLEncoder.encode("北京", "utf-8")
						+ "&id=" + buslineArrayList.get(3) + "&no="
						+ station_num;
			}
			StringRequest jsonObjectRequest = new StringRequest(JSONDataUrl,
					new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							JSONObject jsonObj = null;
							try {
								jsonObj = XML.toJSONObject(response);
								JSONObject busJsonObject = ((JSONObject) (jsonObj
										.getJSONObject("root")))
										.getJSONObject("data");
								JSONArray oj = busJsonObject
										.getJSONArray("bus");
								JSONArray busesArray = oj;
								new Thread(new doRequestResult(busesArray,
										busline)).start();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError arg0) {
							Log.e("aaa", arg0.toString());
						}
					});
			MyVolley.sharedVolley(StationInformationView.this)
					.getRequestQueue().add(jsonObjectRequest);

		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // ͳ��ҳ��
		MobclickAgent.onResume(this); // ͳ��ʱ��
		registerReceiver(mWifiReceiver, wifiFilter);
		registerReceiver(mActivityReceiver, activityFilter);

	}

	protected class MyGridAdapter extends BaseAdapter {

		public ArrayList<ArrayList<String>> buslines;

		public MyGridAdapter(ArrayList<ArrayList<String>> buslines) {
			// TODO Auto-generated constructor stub
			this.buslines = buslines;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return buslines.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = View.inflate(StationInformationView.this,
					R.layout.bus_grid_item, null);
			TextView title = (TextView) view.findViewById(R.id.tv_waitStation);
			ImageView img = (ImageView) view.findViewById(R.id.iv_special);
			img.setVisibility(View.INVISIBLE);
			title.setText(buslines.get(position).get(1));

			String type = buslines.get(position).get(3);
			if (type.equalsIgnoreCase("fav")) {
				img.setVisibility(View.VISIBLE);
				title.setBackgroundColor(0xffff9234);
				img.setImageResource(R.drawable.favimgbg);
				;
			} else {
				title.setBackgroundColor(0xff00b2ae);
			}
			return view;
		}

	}

	public class doRequestResult implements Runnable {
		private JSONArray busesArray;
		private ArrayList<String> busline;

		public doRequestResult(JSONArray busesArray, ArrayList<String> busline) {
			this.busesArray = busesArray;
			this.busline = busline;
		}

		public void run() {
			try {
				try {
					int j = busesArray.length();
					int min = 0;
					double distance = 0;
					double mindistance = 1000000000.0;
					int min2 = 1000;
					double mindistance2 = 1000000000.0;
					LocationUtil locater = LocationUtil
							.getInstance(StationInformationView.this);
					String stationName = "";
					for (int i = 0; i < j; i++) {

						JSONObject jsonObject = (JSONObject) busesArray.get(i);
						stationName = new MyCipher("aibang"
								+ jsonObject.getString("gt"))
								.decrypt(jsonObject.getString("ns"));
						ArrayList<ArrayList<String>> stations = DataBaseHelper
								.getInstance(StationInformationView.this)
								.getSpecificStationsWithBuslineName(
										busline.get(2));
						long myNum = 0;
						long curNum = 1000;
						if (stations != null) {
							int k = stations.size();
							for (int m = 0; m < k; m++) {

								ArrayList<String> arrayList = stations.get(m);

								if (stationName.equalsIgnoreCase(arrayList
										.get(0))) {
									curNum = m;
								}

								if (stationTitle.equalsIgnoreCase(arrayList
										.get(0))) {
									myNum = m;
									break;
								}
							}
						}

						if (myNum - curNum >= 0) {
							distance = myNum - curNum;

							if (distance < mindistance) {
								if (mindistance == 1000000000.0) {
									mindistance = distance;
									min = i;
								} else {
									mindistance2 = mindistance;
									min2 = min;
									mindistance = distance;
									min = i;
								}
							}
						}
					}

					ArrayList<ArrayList<String>> stations = DataBaseHelper
							.getInstance(StationInformationView.this)
							.getSpecificStationsWithBuslineName(busline.get(2));
					if ((min == min2) || (min2 == 1000)
							|| (mindistance2 == mindistance)) {
						JSONObject jsonObject = (JSONObject) busesArray
								.get(min);
						long myNum = 0;
						long curNum = 1000;
						int tag = 0;
						double distanceTotal = 0.0;
						if (stations != null) {
							int k = stations.size();
							for (int i = 0; i < k; i++) {
								ArrayList<String> arrayList = stations.get(i);
								if (tag == 1) {
									distanceTotal += locater
											.getDistanceWithLocations(
													new LatLng(
															Float.parseFloat(arrayList
																	.get(1)),
															Float.parseFloat(arrayList
																	.get(2))),
													new LatLng(
															Float.parseFloat(stations
																	.get(i - 1)
																	.get(1)),
															Float.parseFloat(stations
																	.get(i - 1)
																	.get(2))));
								}

								if (new MyCipher("aibang"
										+ jsonObject.getString("gt")).decrypt(
										jsonObject.getString("ns"))
										.equalsIgnoreCase(arrayList.get(0))) {
									curNum = i;
									tag = 1;
									distanceTotal = 0;
								}

								if (stationTitle.equalsIgnoreCase(arrayList
										.get(0))) {
									myNum = i;
									tag = 0;
									break;
								}
							}
						}

						if (myNum - curNum >= 0) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("itemsIcon", R.drawable.bus_img);
							map.put("bus_title",
									busline.get(2).substring(0,
											busline.get(2).indexOf("(")));
							map.put("station_title", stationTitle);
							String nameString = busline.get(2);
							nameString = "开往<font color=\"yellow\">"
									+ nameString.substring(nameString
											.indexOf("-") + 1);
							nameString = nameString.substring(0,
									nameString.length() - 1);
							map.put("itemsTitle", nameString + "</font>");
							if ((myNum - curNum) == 0) {
								map.put("itemsText",
										"<font color=\"red\">即将到站</font>");

							} else {
								map.put("itemsText", "<font color=\"black\">"
										+ (myNum - curNum) + "</font> 站后到达, "
										+ (int) (distanceTotal / 100) / 10.0
										+ " km");
							}

							map.put("distance", mindistance + "");
							map.put("busline", busline);
							rtbusListData2.add(map);
						}
					} else {
						JSONObject jsonObject = (JSONObject) busesArray
								.get(min);
						JSONObject jsonObject2 = (JSONObject) busesArray
								.get(min2);
						long myNum = 0;
						long curNum = 1000;
						long curNum2 = 1000;
						int tag = 0;
						int tag2 = 0;
						double distanceTotal = 0.0;
						double distanceTotal2 = 0.0;
						if (stations != null) {
							int k = stations.size();
							for (int i = 0; i < k; i++) {

								ArrayList<String> arrayList = stations.get(i);
								if (tag == 1) {
									distanceTotal += locater
											.getDistanceWithLocations(
													new LatLng(
															Float.parseFloat(arrayList
																	.get(1)),
															Float.parseFloat(arrayList
																	.get(2))),
													new LatLng(
															Float.parseFloat(stations
																	.get(i - 1)
																	.get(1)),
															Float.parseFloat(stations
																	.get(i - 1)
																	.get(2))));
								}

								if (tag2 == 1) {
									distanceTotal2 += locater
											.getDistanceWithLocations(
													new LatLng(
															Float.parseFloat(arrayList
																	.get(1)),
															Float.parseFloat(arrayList
																	.get(2))),
													new LatLng(
															Float.parseFloat(stations
																	.get(i - 1)
																	.get(1)),
															Float.parseFloat(stations
																	.get(i - 1)
																	.get(2))));
								}

								if (new MyCipher("aibang"
										+ jsonObject.getString("gt")).decrypt(
										jsonObject.getString("ns"))
										.equalsIgnoreCase(arrayList.get(0))) {
									curNum = i;
									tag = 1;
									distanceTotal = 0;
								}

								if (new MyCipher("aibang"
										+ jsonObject2.getString("gt")).decrypt(
										jsonObject2.getString("ns"))
										.equalsIgnoreCase(arrayList.get(0))) {
									curNum2 = i;
									tag2 = 1;
									distanceTotal2 = 0;
								}

								if (stationTitle.equalsIgnoreCase(arrayList
										.get(0))) {
									myNum = i;
									tag = 0;
									break;
								}
							}
						}

						if (myNum - curNum >= 0) {
							if (myNum - curNum2 >= 0) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("itemsIcon", R.drawable.bus_img);
								map.put("bus_title",
										busline.get(2).substring(0,
												busline.get(2).indexOf("(")));
								map.put("station_title", stationTitle);
								String nameString = busline.get(2);
								nameString = "开往<font color=\"yellow\">"
										+ nameString.substring(nameString
												.indexOf("-") + 1);
								nameString = nameString.substring(0,
										nameString.length() - 1);
								map.put("itemsTitle", nameString + "</font>");
								if ((myNum - curNum) == 0) {
									map.put("itemsText",
											"<font color=\"red\">即将到站</font> ;&nbsp;下辆 <font color=\"black\">"
													+ (myNum - curNum2)
													+ "</font> 站, "
													+ (int) (distanceTotal2 / 100)
													/ 10.0 + "km");
								} else {
									map.put("itemsText",
											"<font color=\"black\">"
													+ (myNum - curNum)
													+ "</font> 站, "
													+ (int) (distanceTotal / 100)
													/ 10.0
													+ " km ;&nbsp;下辆 <font color=\"black\">"
													+ (myNum - curNum2)
													+ "</font> 站, "
													+ (int) (distanceTotal2 / 100)
													/ 10.0 + "km");
								}

								map.put("distance", mindistance + "");
								map.put("busline", busline);
								rtbusListData2.add(map);
							} else {
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("itemsIcon", R.drawable.bus_img);
								map.put("bus_title",
										busline.get(2).substring(0,
												busline.get(2).indexOf("(")));
								map.put("station_title", stationTitle);
								String nameString = busline.get(2);
								nameString = "开往<font color=\"yellow\">"
										+ nameString.substring(nameString
												.indexOf("-") + 1);
								nameString = nameString.substring(0,
										nameString.length() - 1);
								map.put("itemsTitle", nameString + "</font>");
								if ((myNum - curNum) == 0) {
									map.put("itemsText",
											"<font color=\"red\">即将到站</font>");

								} else {
									map.put("itemsText",
											"<font color=\"black\">"
													+ (myNum - curNum)
													+ "</font> 站, "
													+ (int) (distanceTotal / 100)
													/ 10.0 + " km");
								}

								map.put("distance", mindistance + "");
								map.put("busline", busline);
								rtbusListData2.add(map);
							}
						}
					}

					int l = rtbusListData2.size();
					HashMap<String, Object> mmap;
					for (int i = 0; i < l; i++) {
						mmap = (HashMap<String, Object>) rtbusListData2.get(i);
						if ((mmap.get("itemsTitle")).toString()
								.equalsIgnoreCase("暂未发现实时公交")) {
							rtbusListData2.remove(i);
							i--;
							l--;
						}
					}
					// 排序
					int m = rtbusListData2.size();
					HashMap<String, Object> map2;
					for (int i = m - 1; i > 0; --i) {
						for (int n = 0; n < i; ++n) {
							if (Double.parseDouble((String) rtbusListData2.get(
									n + 1).get("distance")) < Double
									.parseDouble((String) rtbusListData2.get(n)
											.get("distance"))) {
								map2 = rtbusListData2.get(n);
								rtbusListData2.remove(n);
								rtbusListData2.add(n + 1, map2);
							}
						}
					}

					if (rtbusListData2.size() == 0) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("itemsIcon", R.drawable.bus_img);
						map.put("itemsTitle", "暂未发现实时公交");
						map.put("itemsText",
								"<font color=\"yellow\">您可尝试收藏更多线路~</font>");
						rtbusListData2.add(map);
					}
				} catch (SQLException sqle) {
					throw sqle;
				}

			} catch (JSONException e) {
				Log.e("JSON exception", e.getMessage());
				e.printStackTrace();
			}
			Message msgMessage = new Message();
			mHandler.sendMessage(msgMessage);
		}
	}

	private void initTitleRightLayout() {
		clearRightView();
		View rightViewApp = mInflater.inflate(R.layout.app_btn2, null);
		appBtn = (Button) rightViewApp.findViewById(R.id.appBtn);
		appBtn.setBackgroundResource(R.drawable.changestationdirimg);
		appBtn.setTextColor(Color.WHITE);
		appBtn.setPadding(25, 5, 25, 5);
		appBtn.setTextSize(18);
		// appBtn.setCustomSelectionActionModeCallback(actionModeCallback)

		addRightView(rightViewApp);

		appBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = null;
				if (mAllStations.size() < 2) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"未找到其它站点~", Toast.LENGTH_LONG);
					toast.show();

				} else {
					getChangeBuslines();
					if (rtbuslines.size() > 0) {
						GetAllBuses();
						if (isNetworkConnected(StationInformationView.this))
							appBtn.setClickable(false);
					} else {
						appBtn.setClickable(true);
					}

				}

			}

		});
		MobclickAgent.updateOnlineConfig(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // ��֤ onPageEnd ��onPause
													// ֮ǰ����,��Ϊ onPause
													// �лᱣ����Ϣ
		MobclickAgent.onPause(this);
		if (task != null) {
			task.cancel();
		}
		if (timer != null) {
			timer.cancel();
		}
		unregisterReceiver(mWifiReceiver);
		unregisterReceiver(mActivityReceiver);

	}

	@Override
	public void onRestart() {
		super.onRestart();
		SharedPreferences mySharedPreferences = getSharedPreferences("setting",
				SettingView.MODE_PRIVATE);
		String distanceString = mySharedPreferences.getString("searchRMode",
				"1000米");
		Integer.parseInt(distanceString.substring(0,
				distanceString.length() - 1));

		num--;
		getChangeBuslines();
		task = new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						GetAllBuses();
					}
				});
			}
		};

		timer = new Timer(true);
		String timeString = mySharedPreferences.getString("refreshMode", "30秒");
		timeString = timeString.substring(0, timeString.length() - 1);
		timer.schedule(task, 200, Integer.parseInt(timeString) * 1000); // 延时1000ms后执行，1000ms执行一次
	}

	static int num = 1;

	public void getChangeBuslines() {
		ArrayList<ArrayList<String>> buslines = mAllStations.get(num
				% (mAllStations.size()));
		curbuslines.clear();
		int j = buslines.size();
		rtbuslines.clear();
		// for (int i = 0; i < j - 1; i++) {
		// buslines.get(i).add("notfav");
		// if (userdabase.checkFavBuslineWithBuslineID(buslines.get(i).get(0)))
		// {
		// curbuslines.add(0, buslines.get(i));
		// buslines.get(i).set(3, "fav");
		// ArrayList<String> buslineArrayList = BuslineDBHelper
		// .getInstance(StationInformationView.this)
		// .getBuslineInfoWithBuslineName(buslines.get(i).get(2));
		// if (buslineArrayList.size() > 0
		// && isNetworkConnected(StationInformationView.this)) {
		// rtbuslines.add(buslines.get(i));
		// }
		// } else {
		// curbuslines.add(buslines.get(i));
		// buslines.get(i).set(3, "notfav");
		// }
		// }
		allBuslinesAdapter.notifyDataSetChanged();
		stationPoint = new LatLng(Float.parseFloat(curStation.get(1)),
				Float.parseFloat(curStation.get(2)));
		String alertString = userdabase
				.checkAlertStationWithStation(curStation) ? "删除提醒" : "下车提醒";
		// String favString = userdabase
		// .checkFavStationWithStationID(stationTitle) ? "取消收藏" : "添加收藏";
		// favBtn.setText(favString);
		alertBtn.setText(alertString);
		rtbusListData2.clear();
		num++;
	}
}*/