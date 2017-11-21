//package com.bnrc.busapp;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.json.XML;
//import android.app.ActionBar;
//import android.app.AlertDialog;
//import android.app.ActionBar.OnNavigationListener;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.Html;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemLongClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RadioGroup;
//import android.widget.RadioGroup.OnCheckedChangeListener;
//import android.widget.Spinner;
//import android.widget.SpinnerAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.ab.activity.AbActivity;
//import com.ab.global.AbConstant;
//import com.baidu.location.BDLocation;
//import com.baidu.mapapi.model.LatLng;
//import com.bnrc.network.MyVolley;
//import com.bnrc.network.StringRequest;
//import com.bnrc.network.toolbox.Response;
//import com.bnrc.network.toolbox.VolleyError;
//import com.bnrc.busapp.R;
//import com.umeng.analytics.MobclickAgent;
//import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;
//import com.umeng.fb.FeedbackAgent;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
//import com.umeng.socialize.sso.QZoneSsoHandler;
//import com.umeng.socialize.sso.UMQQSsoHandler;
//import com.umeng.socialize.weixin.controller.UMWXHandler;
//import com.bnrc.util.AbBase64;
//import com.bnrc.util.BuslineDBHelper;
//import com.bnrc.util.DataBaseHelper;
//import com.bnrc.util.LocationUtil;
//import com.bnrc.util.SlidingDrawerGridView;
//import com.bnrc.util.UserDataDBHelper;
//import com.bnrc.util.collectwifi.BaseActivity;
//
//import android.database.SQLException;
//import android.graphics.Color;
//
//public class RootView extends BaseActivity {
//
//	private TextView curlocation = null;
//	private TextView viewAllStation = null;
//	private TextView viewAllBuses = null;
//	private LinearLayout bottomview = null;
//	public LocationUtil mApplication = null;
//
//	private SlidingDrawerGridView mtAllStations;
//	private MyGridAdapter mtAllStationsAdapter;
//	private SlidingDrawerGridView mtAllBuslines;
//	private MyGridAdapter mtAllBuslinesAdapter;
//	private LinearLayout buslineContainer;
//
//	private Button settingBtn;
//	private Button dianyingBtn;
//	private Button cantingBtn;
//	private Button yinhangBtn;
//	private Button chaoshiBtn;
//	private ImageView openAlertView;
//	private Handler mHandler;
//	private String isOpenAlert;
//	private SegmentedGroup segmented;
//
//	private int curStation = 0;
//	private int firstTimeRequest = 0;
//	private int hasClearRequest = 0;
//
//	private Timer timer2;
//	private TimerTask task2;
//
//	public UserDataDBHelper userdabase = null;
//	private ArrayList<ArrayList<String>> nearbyStations = null;
//	private ArrayList<ArrayList<String>> favStations = null;
//	private ArrayList<ArrayList<String>> alertStations = null;
//	private ArrayList<ArrayList<String>> searchedStations = null;
//	private ArrayList<ArrayList<String>> allStations = null;
//	public List<HashMap<String, Object>> rtbusListData;
//	public List<HashMap<String, Object>> rtbusListData2;
//	public ArrayList<ArrayList<String>> nearbyBuslines;
//	public ArrayList<ArrayList<String>> allRtBuses;
//	private ArrayList<ArrayList<String>> favBuslines = null;
//	private ArrayList<ArrayList<String>> searchedBuslines = null;
//	private ArrayList<ArrayList<String>> allBuslines = null;
//	private ArrayList<View> rtBusArrayList = null;
//	private PopMenu popMenu;
//
//	OnItemClickListener popmenuItemClickListener = new OnItemClickListener() {
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			popMenu.dismiss();
//			if (position == 0) {
//				Intent intent = new Intent(RootView.this,
//						MyAlertStationView.class);
//				intent.putExtra("keyword", "");
//				intent.putExtra(
//						"TEXT",
//						RootView.this.getResources().getString(
//								R.string.title_transparent_desc));
//				intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//						AbConstant.TITLE_TRANSPARENT);
//				startActivity(intent);
//				// overridePendingTransition(R.anim.out_alpha,
//				// R.anim.enter_alpha);
//			} else if (position == 1) {
//				Intent intent = new Intent(RootView.this,
//						MyFavoriteStationView.class);
//				intent.putExtra("keyword", "");
//				intent.putExtra(
//						"TEXT",
//						RootView.this.getResources().getString(
//								R.string.title_transparent_desc));
//				intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//						AbConstant.TITLE_TRANSPARENT);
//				startActivity(intent);
//				// overridePendingTransition(R.anim.out_alpha,
//				// R.anim.enter_alpha);
//			} else if (position == 2) {
//				Intent intent = new Intent(RootView.this,
//						MyFavoriteBuslineView.class);
//				intent.putExtra("keyword", "");
//				intent.putExtra(
//						"TEXT",
//						RootView.this.getResources().getString(
//								R.string.title_transparent_desc));
//				intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//						AbConstant.TITLE_TRANSPARENT);
//				startActivity(intent);
//				// overridePendingTransition(R.anim.out_alpha,
//				// R.anim.enter_alpha);
//			} else if (position == 3) {
//				UserDataDBHelper.getInstance(RootView.this)
//						.deleteAllSearchHistory();
//				Toast toast = Toast.makeText(getApplicationContext(),
//						"您已清空所有搜索数据~", Toast.LENGTH_LONG);
//				toast.show();
//			}
//		}
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setAbContentView(R.layout.root_view);
//
//		isOpenAlert = "开启提醒功能";
//		MobclickAgent.updateOnlineConfig(this);
//		String value = MobclickAgent.getConfigParams(RootView.this,
//				"bus_data_version");
//		JSONObject jsonObj = null;
//		try {
//			jsonObj = new JSONObject(value);
//			String version = jsonObj.getString("version");
//			String ready = jsonObj.getString("ready");
//			SharedPreferences mySharedPreferences = getSharedPreferences(
//					"setting", SettingView.MODE_PRIVATE);
//			String oldVersion = mySharedPreferences.getString(
//					"bus_data_version", "1");
//			if (ready.equalsIgnoreCase("YES")
//					&& (version.equalsIgnoreCase(oldVersion) == false)) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(
//						RootView.this);
//				builder.setMessage("公交数据已经推出了新的版本，您是否要更新？").setTitle("友情提示")
//						.setNegativeButton("取消", null);
//				builder.setPositiveButton("确定",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int which) {
////								DataBaseHelper.getInstance(RootView.this)
////										.DownFileWithUrl(
////												MobclickAgent.getConfigParams(
////														RootView.this,
////														"bus_data_url"));
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
//		value = MobclickAgent.getConfigParams(RootView.this,
//				"realtime_bus_data_version");
//		Log.i("realtime_bus_data_version", value);
//		try {
//			jsonObj = new JSONObject(value);
//			String version = jsonObj.getString("version");
//			String ready = jsonObj.getString("ready");
//			SharedPreferences mySharedPreferences = getSharedPreferences(
//					"setting", SettingView.MODE_PRIVATE);
//			String oldVersion = mySharedPreferences.getString(
//					"realtime_bus_data_version", "1");
//			if (ready.equalsIgnoreCase("YES")
//					&& (version.equalsIgnoreCase(oldVersion) == false)) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(
//						RootView.this);
//				builder.setMessage("实时公交数据有更新，您是否要更新？").setTitle("友情提示")
//						.setNegativeButton("取消", null);
//				builder.setPositiveButton("确定",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int which) {
////								BuslineDBHelper
////										.getInstance(RootView.this)
////										.DownFileWithUrl(
////												MobclickAgent
////														.getConfigParams(
////																RootView.this,
////																"realtime_bus_data_url"));
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
//		mApplication = LocationUtil.getInstance(RootView.this);
//		mApplication.startLocation();
//		initbase();
//		// this.setTitleText("首页");
//		// this.setTitleLayoutBackground(R.drawable.top_bg);
//		// this.setLogo(R.drawable.button_selector_back);
//		// this.titleLayout.setPadding(25, 0, 0, 0);
//		// this.setTitleLayoutGravity(Gravity.CENTER, Gravity.CENTER);
//		popMenu = new PopMenu(RootView.this);
//		popMenu.addItems(new String[] { "下车提醒站点", "我收藏的站点", "我收藏的线路", "清空搜索记录" });
//		popMenu.setOnItemClickListener(popmenuItemClickListener);
//		// initTitleRightLayout();
//
//		nearbyStations = new ArrayList<ArrayList<String>>();
//		favStations = new ArrayList<ArrayList<String>>();
//		favBuslines = new ArrayList<ArrayList<String>>();
//		alertStations = new ArrayList<ArrayList<String>>();
//		searchedStations = new ArrayList<ArrayList<String>>();
//		searchedBuslines = new ArrayList<ArrayList<String>>();
//		allStations = new ArrayList<ArrayList<String>>();
//		allBuslines = new ArrayList<ArrayList<String>>();
//
//		openAlertView = (ImageView) findViewById(R.id.open_alert_btn);
//		openAlertView.setBackgroundResource(R.drawable.alertoffimg);
//		openAlertView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if (isOpenAlert.equalsIgnoreCase("开启提醒功能")) {
//					isOpenAlert = "关闭提醒功能";
//					PollingUtils.startPollingService(RootView.this, 5,
//							PollingService.class, PollingService.ACTION);
//					openAlertView.setBackgroundResource(R.drawable.alertonimg);
//					Toast toast = Toast.makeText(getApplicationContext(),
//							"您已开启提醒功能~", Toast.LENGTH_LONG);
//					toast.show();
//				} else {
//					isOpenAlert = "开启提醒功能";
//					PollingUtils.stopPollingService(RootView.this,
//							PollingService.class, PollingService.ACTION);
//					openAlertView.setBackgroundResource(R.drawable.alertoffimg);
//					Toast toast = Toast.makeText(getApplicationContext(),
//							"您已关闭提醒功能~", Toast.LENGTH_LONG);
//					toast.show();
//				}
//			}
//		});
//
//		mtAllStations = (SlidingDrawerGridView) findViewById(R.id.all_bus);
//		mtAllStationsAdapter = new MyGridAdapter(allStations, 0);
//		mtAllStations.setAdapter(mtAllStationsAdapter);
//
//		mtAllBuslines = (SlidingDrawerGridView) findViewById(R.id.mtmyBuslines);
//		mtAllBuslinesAdapter = new MyGridAdapter(allBuslines, 1);
//		mtAllBuslines.setAdapter(mtAllBuslinesAdapter);
//
//		mtAllStations.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				if (position < allStations.size()) {
//					Intent intent = new Intent(RootView.this,
//							StationInformationView.class);
//					ArrayList<String> station = new ArrayList<String>();
//					station = allStations.get(position);
//					intent.putExtra("title", station.get(0));
//					intent.putExtra("latitude", station.get(1));
//					intent.putExtra("longitude", station.get(2));
//					startActivity(intent);
//					overridePendingTransition(android.R.anim.fade_in,
//							android.R.anim.fade_out);
//				} else {
//					Intent intent = new Intent(RootView.this,
//							NearbyStationListView.class);
//					intent.putExtra("TEXT", RootView.this.getResources()
//							.getString(R.string.title_transparent_desc));
//					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//							AbConstant.TITLE_TRANSPARENT);
//					startActivity(intent);
//					overridePendingTransition(android.R.anim.fade_in,
//							android.R.anim.fade_out);
//				}
//			}
//		});
//
//		// item
//		mtAllStations.setOnItemLongClickListener(new OnItemLongClickListener() {
//			@Override
//			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//					final int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				final ArrayList<String> station = allStations.get(arg2);
//
//				final String alertString = userdabase
//						.checkAlertStationWithStation(station) ? "删除下车提醒"
//						: "添加到下车提醒";
//				final String favString = userdabase
//						.checkFavStationWithStationID(station.get(0)) ? "取消收藏站点"
//						: "添加收藏站点";
//				final String[] arrayChoice = new String[] { "查看站点详情",
//						alertString, favString };
//				new AlertDialog.Builder(RootView.this)
//						// build AlertDialog
//						.setTitle("您可进行的操作")
//						// title
//						.setItems(arrayChoice,
//								new DialogInterface.OnClickListener() { // content
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										if (which == 0) {
//											// Intent intent = null;
//											Intent intent = new Intent(
//													RootView.this,
//													StationInformationView.class);
//											ArrayList<String> station = new ArrayList<String>();
//											station = allStations.get(which);
//											intent.putExtra("title",
//													station.get(0));
//											intent.putExtra("latitude",
//													station.get(1));
//											intent.putExtra("longitude",
//													station.get(2));
//											startActivity(intent);
//										} else if (which == 1) {
//											if (alertString
//													.equalsIgnoreCase("删除下车提醒")) {
//												userdabase
//														.deleteAlertStationWithStation(station);
//												refreshData();
//												Toast toast = Toast
//														.makeText(
//																getApplicationContext(),
//																"您已删除\""
//																		+ station
//																				.get(0)
//																		+ "\"下车提醒站点",
//																Toast.LENGTH_LONG);
//												toast.show();
//											} else {
//												userdabase
//														.addAlertStationWithStation(station);
//												refreshData();
//												Toast toast = Toast
//														.makeText(
//																getApplicationContext(),
//																"您已添加\""
//																		+ station
//																				.get(0)
//																		+ "\"为下车提醒站点",
//																Toast.LENGTH_LONG);
//												toast.show();
//											}
//
//										} else if (which == 2) {
//											if (favString
//													.equalsIgnoreCase("取消收藏站点")) {
//												userdabase
//														.deleteFavStationWithStation(station);
//												refreshData();
//												Toast toast = Toast
//														.makeText(
//																getApplicationContext(),
//																"您已取消收藏\""
//																		+ station
//																				.get(0)
//																		+ "\"站点",
//																Toast.LENGTH_LONG);
//												toast.show();
//
//											} else {
//												userdabase
//														.addFavStationWithStation(station);
//												refreshData();
//												Toast toast = Toast
//														.makeText(
//																getApplicationContext(),
//																"您已成功收藏\""
//																		+ station
//																				.get(0)
//																		+ "\"站点",
//																Toast.LENGTH_LONG);
//												toast.show();
//											}
//										}
//									}
//								})
//						.setNegativeButton("取消",
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										dialog.dismiss(); // �ر�alertDialog
//									}
//								}).show();
//				return true;
//			}
//		});
//
//		mtAllBuslines.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//
//				if (position < allBuslines.size()) {
//					Intent intent = new Intent(RootView.this,
//							BuslineListView.class);
//					// 在意图中传递数据
//					ArrayList<String> busline = new ArrayList<String>();
//					busline = allBuslines.get(position);
//					intent.putExtra("title", busline.get(1));
//					intent.putExtra("buslineID", busline.get(0));
//					intent.putExtra("buslineName", busline.get(2));
//					intent.putExtra("stationName", "");
//					startActivity(intent);
//					// overridePendingTransition(R.anim.out_alpha,
//					// R.anim.enter_alpha);
//				} else {
//					Intent intent = new Intent(RootView.this,
//							SearchBuslineView.class);
//					intent.putExtra("keyword", "");
//					intent.putExtra("TEXT", RootView.this.getResources()
//							.getString(R.string.title_transparent_desc));
//					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//							AbConstant.TITLE_TRANSPARENT);
//					startActivity(intent);
//					// overridePendingTransition(R.anim.out_alpha,
//					// R.anim.enter_alpha);
//				}
//			}
//		});
//
//		mtAllBuslines.setOnItemLongClickListener(new OnItemLongClickListener() {
//			@Override
//			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//					final int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				final ArrayList<String> busline = allBuslines.get(arg2);
//
//				final String favString = userdabase
//						.checkFavBuslineWithBuslineID(busline.get(0)) ? "取消收藏该线路"
//						: "收藏该线路";
//				final String[] arrayChoice = new String[] { "查看该线路", favString };
//				new AlertDialog.Builder(RootView.this)
//						.setTitle("您可进行的操作")
//						.setItems(arrayChoice,
//								new DialogInterface.OnClickListener() { // content
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										if (which == 0) {
//											// Intent intent = null;
//											Intent intent = new Intent(
//													RootView.this,
//													BuslineListView.class);
//											ArrayList<String> busline = new ArrayList<String>();
//											busline = allBuslines.get(arg2);
//											intent.putExtra("title",
//													busline.get(1));
//											intent.putExtra("buslineID",
//													busline.get(0));
//											intent.putExtra("buslineName",
//													busline.get(2));
//											intent.putExtra("stationName", "");
//											startActivity(intent);
//										} else if (which == 1) {
//											if (favString
//													.equalsIgnoreCase("取消收藏该线路")) {
//												userdabase
//														.deleteFavBuslineWithBusline(busline);
//												refreshData();
//												Toast toast = Toast
//														.makeText(
//																getApplicationContext(),
//																"您已取消收藏\""
//																		+ busline
//																				.get(2)
//																		+ "路",
//																Toast.LENGTH_LONG);
//												toast.show();
//
//											} else {
//												userdabase
//														.addFavBuslineWithBusline(busline);
//												refreshData();
//												Toast toast = Toast
//														.makeText(
//																getApplicationContext(),
//																"您已成功收藏\""
//																		+ busline
//																				.get(2)
//																		+ "路",
//																Toast.LENGTH_LONG);
//												toast.show();
//											}
//										}
//									}
//								})
//						.setNegativeButton("取消",
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										dialog.dismiss(); // �ر�alertDialog
//									}
//								}).show();
//				return true;
//			}
//		});
//
//		mApplication = LocationUtil.getInstance(RootView.this);
//		curlocation = (TextView) findViewById(R.id.curlocation);
//		viewAllStation = (TextView) findViewById(R.id.all_fav_stations);
//		viewAllStation.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if (segmented.getCheckedRadioButtonId() == R.id.radBtn_all) {
//					Intent intent = new Intent(RootView.this,
//							MyFavoriteStationView.class);
//					intent.putExtra("keyword", "");
//					intent.putExtra("TEXT", RootView.this.getResources()
//							.getString(R.string.title_transparent_desc));
//					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//							AbConstant.TITLE_TRANSPARENT);
//					startActivity(intent);
//				} else {
//					Intent intent = new Intent(RootView.this,
//							NearbyStationListView.class);
//					intent.putExtra("TEXT", RootView.this.getResources()
//							.getString(R.string.title_transparent_desc));
//					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//							AbConstant.TITLE_TRANSPARENT);
//					startActivity(intent);
//					overridePendingTransition(android.R.anim.fade_in,
//							android.R.anim.fade_out);
//				}
//
//				// overridePendingTransition(R.anim.out_alpha,
//				// R.anim.enter_alpha);
//			}
//		});
//		viewAllBuses = (TextView) findViewById(R.id.all_fav_buses);
//		viewAllBuses.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if (segmented.getCheckedRadioButtonId() == R.id.radBtn_all) {
//					Intent intent = new Intent(RootView.this,
//							MyFavoriteBuslineView.class);
//					intent.putExtra("keyword", "");
//					intent.putExtra("TEXT", RootView.this.getResources()
//							.getString(R.string.title_transparent_desc));
//					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//							AbConstant.TITLE_TRANSPARENT);
//					startActivity(intent);
//				} else {
//					Intent intent = new Intent(RootView.this,
//							SearchBuslineView.class);
//					intent.putExtra("keyword", "");
//					intent.putExtra("TEXT", RootView.this.getResources()
//							.getString(R.string.title_transparent_desc));
//					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//							AbConstant.TITLE_TRANSPARENT);
//					startActivity(intent);
//				}
//
//				// overridePendingTransition(R.anim.out_alpha,
//				// R.anim.enter_alpha);
//			}
//		});
//
//		buslineContainer = (LinearLayout) findViewById(R.id.realtime_bus);
//		TimerTask task = new TimerTask() {
//			public void run() {
//				runOnUiThread(new Runnable() {
//					public void run() {
//						curlocation.setText(mApplication.addressString);
//						if (isNetworkConnected(RootView.this)) {
//							buslineContainer.setVisibility(View.VISIBLE);
//						} else {
//							buslineContainer.setVisibility(View.GONE);
//						}
//					}
//				});
//				// curlocation.setText("当前位置："+mApplication.addressString);
//			}
//		};
//		Timer timer = new Timer(true);
//		timer.schedule(task, 2000, 5000); // 延时1000ms后执行，1000ms执行一次
//		// timer.cancel(); //退出计时器
//		rtbusListData = new ArrayList<HashMap<String, Object>>();
//		rtbusListData2 = new ArrayList<HashMap<String, Object>>();
//		rtBusArrayList = new ArrayList<View>();
//		// 更新
//		// refreshRealtimeBuses();
//		settingBtn = (Button) findViewById(R.id.setting_home);
//		DataBaseHelper.getInstance(RootView.this);
//
//		settingBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(RootView.this, SettingView.class);
//				intent.putExtra(
//						"TEXT",
//						RootView.this.getResources().getString(
//								R.string.title_transparent_desc));
//				intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//						AbConstant.TITLE_TRANSPARENT);
//				startActivity(intent);
//				// overridePendingTransition(R.anim.out_alpha,
//				// R.anim.enter_alpha);
//			}
//		});
//
//		dianyingBtn = (Button) findViewById(R.id.hotel);
//		cantingBtn = (Button) findViewById(R.id.canting);
//		yinhangBtn = (Button) findViewById(R.id.yinhang);
//		chaoshiBtn = (Button) findViewById(R.id.chaoshi);
//
//		dianyingBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				// �Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ
//				if (isNetworkConnected(RootView.this)) {
//					Intent intent = new Intent(RootView.this,
//							SearchSomethingView.class);
//					intent.putExtra("TEXT", RootView.this.getResources()
//							.getString(R.string.title_transparent_desc));
//					// ���ñ�����͸��
//					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//							AbConstant.TITLE_TRANSPARENT);
//					intent.putExtra("keyword", "酒店");
//					startActivity(intent);
//					// overridePendingTransition(R.anim.out_alpha,
//					// R.anim.enter_alpha);
//				} else {
//					Toast toast = Toast.makeText(getApplicationContext(),
//							"您的网络有问题，请检查~", Toast.LENGTH_LONG);
//					toast.show();
//				}
//
//			}
//		});
//
//		cantingBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if (isNetworkConnected(RootView.this)) {
//					Intent intent = new Intent(RootView.this,
//							SearchSomethingView.class);
//					intent.putExtra("TEXT", RootView.this.getResources()
//							.getString(R.string.title_transparent_desc));
//					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//							AbConstant.TITLE_TRANSPARENT);
//					intent.putExtra("keyword", "小吃");
//					startActivity(intent);
//					// overridePendingTransition(R.anim.out_alpha,
//					// R.anim.enter_alpha);
//				} else {
//					Toast toast = Toast.makeText(getApplicationContext(),
//							"您的网络有问题，请检查~", Toast.LENGTH_LONG);
//					toast.show();
//				}
//
//			}
//		});
//
//		yinhangBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				// �Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ
//				if (isNetworkConnected(RootView.this)) {
//					Intent intent = new Intent(RootView.this,
//							SearchSomethingView.class);
//					intent.putExtra("TEXT", RootView.this.getResources()
//							.getString(R.string.title_transparent_desc));
//					// ���ñ�����͸��
//					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//							AbConstant.TITLE_TRANSPARENT);
//					intent.putExtra("keyword", "银行");
//					startActivity(intent);
//					// overridePendingTransition(R.anim.out_alpha,
//					// R.anim.enter_alpha);
//				} else {
//					Toast toast = Toast.makeText(getApplicationContext(),
//							"您的网络有问题，请检查~", Toast.LENGTH_LONG);
//					toast.show();
//				}
//			}
//		});
//
//		chaoshiBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				// �Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ
//				if (isNetworkConnected(RootView.this)) {
//					Intent intent = new Intent(RootView.this,
//							SearchSomethingView.class);
//					intent.putExtra("TEXT", RootView.this.getResources()
//							.getString(R.string.title_transparent_desc));
//					// ���ñ�����͸��
//					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//							AbConstant.TITLE_TRANSPARENT);
//					intent.putExtra("keyword", "超市");
//					startActivity(intent);
//					// overridePendingTransition(R.anim.out_alpha,
//					// R.anim.enter_alpha);
//				} else {
//					Toast toast = Toast.makeText(getApplicationContext(),
//							"您的网络有问题，请检查~", Toast.LENGTH_LONG);
//					toast.show();
//				}
//
//			}
//		});
//
//		bottomview = (LinearLayout) findViewById(R.id.bottomview);
//		// allRtBuses = UserDataDBHelper.getInstance(RootView.this)
//		// .getMyAllFavRTBus();
//		allRtBuses = new ArrayList<ArrayList<String>>();
//		segmented = (SegmentedGroup) findViewById(R.id.segmentedGroup);
//		segmented.check(R.id.radBtn_all);
//		bottomview.setVisibility(View.GONE);
//		refreshData();
//		if (favStations.size() == 0 && favBuslines.size() == 0) {
//			segmented.check(R.id.radBtn_work);
//			bottomview.setVisibility(View.VISIBLE);
//			refreshData();
//		}
//
//		segmented.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(RadioGroup arg0, int arg1) {
//				// TODO Auto-generated method stub
//				HashMap<String, Object> map;
//				switch (arg1) {
//				case R.id.radBtn_all:
//					firstTimeRequest = 1;
//					bottomview.setVisibility(View.GONE);
//					refreshData();
//					rtbusListData.clear();
//					map = new HashMap<String, Object>();
//					map.put("itemsIcon", R.drawable.bus_img);
//					map.put("itemsTitle", "暂未发现实时公交");
//					map.put("itemsText", "敬请关注");
//					rtbusListData.add(map);
//					rtbusListData2.clear();
//					hasClearRequest = 1;
//					MyVolley.sharedVolley(RootView.this).getRequestQueue()
//							.cancelAll(this);
//					refreshRealtimeBuses();
//					GetAllBuses();
//					return;
//				case R.id.radBtn_work:
//					firstTimeRequest = 1;
//					bottomview.setVisibility(View.VISIBLE);
//					refreshData();
//					rtbusListData.clear();
//					map = new HashMap<String, Object>();
//					map.put("itemsIcon", R.drawable.bus_img);
//					map.put("itemsTitle", "暂未发现实时公交");
//					map.put("itemsText", "敬请关注");
//					rtbusListData.add(map);
//					rtbusListData2.clear();
//					hasClearRequest = 1;
//					MyVolley.sharedVolley(RootView.this).getRequestQueue()
//							.cancelAll(this);
//					refreshRealtimeBuses();
//					GetAllBuses();
//					return;
//				}
//			}
//		});
//
//		task2 = new TimerTask() {
//			public void run() {
//				runOnUiThread(new Runnable() {
//					public void run() {
//
//						if (isNetworkConnected(RootView.this)) {
//							GetAllBuses();
//						} else {
//							Toast toast = Toast.makeText(
//									getApplicationContext(), "您的网络有问题哦~",
//									Toast.LENGTH_LONG);
//							toast.show();
//						}
//
//					}
//				});
//			}
//		};
//
//		timer2 = new Timer(true);
//		SharedPreferences mySharedPreferences = getSharedPreferences("setting",
//				SettingView.MODE_PRIVATE);
//		String timeString = mySharedPreferences.getString("refreshMode", "30秒");
//		timeString = timeString.substring(0, timeString.length() - 1);
//		timer2.schedule(task2, 200, Integer.parseInt(timeString) * 1000);
//
//		TimerTask task4 = new TimerTask() {
//			public void run() {
//				runOnUiThread(new Runnable() {
//					public void run() {
//						firstTimeRequest++;
//						refreshRealtimeBuses();
//					}
//				});
//			}
//		};
//
//		Timer timer4 = new Timer(true);
//		timer4.schedule(task4, 0, Integer.parseInt(timeString) * 1000); // 延时1000ms后执行，1000ms执行一次
//		// timer.cancel(); //退出计时器
//
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("itemsIcon", R.drawable.bus_img);
//		map.put("itemsTitle", "暂未发现实时公交");
//		map.put("itemsText", "敬请关注");
//		rtbusListData.add(map);
//		refreshRealtimeBuses();
//
//		getSearchList();
//
//		mHandler = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//				if (firstTimeRequest == 1) {
//					rtbusListData.clear();
//					for (int i = 0; i < rtbusListData2.size(); i++) {
//						rtbusListData.add(rtbusListData2.get(i));
//					}
//					refreshRealtimeBuses();
//				} else {
//					hasClearRequest = 0;
//				}
//			}
//		};
//
//	}
//
//	public boolean isNetworkConnected(Context context) {
//		if (context != null) {
//			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
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
//	@Override
//	public void onResume() {
//		super.onResume();
//
//		PollingService.hasKnown = true;
//
//		MobclickAgent.onPageStart("SplashScreen"); // ͳ��ҳ��
//		MobclickAgent.onResume(this); // ͳ��ʱ��
//		registerReceiver(mWifiReceiver, wifiFilter);
//		registerReceiver(mActivityReceiver, activityFilter);
//
//	}
//
//	public void getSearchList() {
//		userdabase = UserDataDBHelper.getInstance(RootView.this);
//		searchedBuslines = userdabase.getLatestSearchBuslines();
//	}
//
//	public void refreshRealtimeBuses() {
//		if (rtBusArrayList == null) {
//			rtBusArrayList = new ArrayList<View>();
//		} else {
//			int m = rtBusArrayList.size();
//			for (int i = 0; i < m; i++) {
//				buslineContainer.removeView(rtBusArrayList.get(i));
//				rtBusArrayList.remove(i);
//				i--;
//				m--;
//			}
//		}
//
//		int j = rtbusListData.size();
//		HashMap<String, Object> rtBus;
//		for (int i = 0; (i < j) && (i < 5); i++) {
//			rtBus = rtbusListData.get(i);
//			View stationItem = View.inflate(RootView.this,
//					R.layout.bus_item_view, null);
//			stationItem.setTag(i + "");
//			TextView title = (TextView) stationItem.findViewById(R.id.tv_waitStation);
//			TextView info = (TextView) stationItem.findViewById(R.id.tv_info);
//			title.setText(rtBus.get("itemsTitle").toString());
//			if (rtBus.get("itemsTitle").toString().equalsIgnoreCase("暂未发现实时公交")) {
//				info.setVisibility(View.GONE);
//			} else {
//				title.setText(Html.fromHtml(rtBus.get("itemsTitle").toString()));
//				info.setText(Html.fromHtml(rtBus.get("itemsText").toString()));
//				stationItem.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						Intent intent = new Intent(RootView.this,
//								BuslineListView.class);
//						@SuppressWarnings("unchecked")
//						ArrayList<String> busline = (ArrayList<String>) rtbusListData
//								.get(Integer.parseInt(arg0.getTag().toString()))
//								.get("busline");
//						intent.putExtra("title", busline.get(1));
//						intent.putExtra("buslineID", busline.get(0));
//						intent.putExtra("buslineName", busline.get(2));
//						intent.putExtra("stationName", "");
//						startActivity(intent);
//					}
//				});
//			}
//			if (rtBusArrayList.size() > 0
//					&& rtBus.get("itemsTitle").toString()
//							.equalsIgnoreCase("暂未发现实时公交")) {
//			} else {
//				rtBusArrayList.add(stationItem);
//				buslineContainer.addView(stationItem);
//			}
//		}
//	}
//
//	public void refreshData() {
//
//		favStations = UserDataDBHelper.getInstance(RootView.this).favStations;
//		favBuslines = UserDataDBHelper.getInstance(RootView.this).favBuslines;
//		alertStations = UserDataDBHelper.getInstance(RootView.this).alertStations;
//
//		searchedStations = UserDataDBHelper.getInstance(RootView.this)
//				.getLatestSearchStations();
//
//		allStations.clear();
//		ArrayList<String> curItem;
//		int m = 0;
//		// 收藏部分
//		if (segmented.getCheckedRadioButtonId() == R.id.radBtn_all) {
//			// allRtBuses = UserDataDBHelper.getInstance(RootView.this)
//			// .getMyAllFavRTBus();
//			allRtBuses = new ArrayList<ArrayList<String>>();
//			for (ArrayList<String> station : favStations) {
//				int k = allStations.size();
//				int i;
//				for (i = 0; i < k; i++) {
//					if (allStations.get(i).get(0)
//							.equalsIgnoreCase(station.get(0))) {
//						curItem = allStations.get(i);
//						curItem.set(3, "fav");
//						break;
//					}
//				}
//				if (k == 0 || k == i) {
//					curItem = new ArrayList<String>();
//					curItem.add(station.get(0));
//					curItem.add(station.get(1));
//					curItem.add(station.get(2));
//					curItem.add("fav");
//					allStations.add(curItem);
//				}
//				if (allStations.size() > 4) {
//					viewAllStation.setVisibility(View.VISIBLE);
//					viewAllStation.setText("查看所有收藏站点");
//					break;
//					// 想要从缓存中获取文件
//				} else {
//					viewAllStation.setVisibility(View.GONE);
//				}
//			}
//
//			for (ArrayList<String> station : searchedStations) {
//				int k = allStations.size();
//				int i;
//				for (i = 0; i < k; i++) {
//					if (allStations.get(i).get(0)
//							.equalsIgnoreCase(station.get(0))) {
//						curItem = allStations.get(i);
//						if (curItem.get(3).equalsIgnoreCase("fav") == false)
//							curItem.set(3, "search");
//						break;
//					}
//				}
//				if (k == 0 || k == i) {
//					curItem = new ArrayList<String>();
//					curItem.add(station.get(0));
//					curItem.add(station.get(1));
//					curItem.add(station.get(2));
//					curItem.add("search");
//					allStations.add(curItem);
//				}
//				if (allStations.size() > 7) {
//					break;
//				}
//			}
//
//			for (ArrayList<String> station : alertStations) {
//				int k = allStations.size();
//				int i;
//				for (i = 0; i < k; i++) {
//					if (allStations.get(i).get(0)
//							.equalsIgnoreCase(station.get(0))) {
//						curItem = allStations.get(i);
//						if (curItem.get(3).equalsIgnoreCase("search"))
//							curItem.set(3, "alert1");
//						if (curItem.get(3).equalsIgnoreCase("fav"))
//							curItem.set(3, "alert1");
//						if (curItem.get(3).equalsIgnoreCase("nearby"))
//							curItem.set(3, "alert2");
//
//						break;
//					}
//				}
//			}
//
//			mtAllStationsAdapter.notifyDataSetChanged();
//
//			userdabase = UserDataDBHelper.getInstance(RootView.this);
//			allBuslines.clear();
//
//			for (ArrayList<String> station : favBuslines) {
//				int k = allBuslines.size();
//				int i;
//				for (i = 0; i < k; i++) {
//					if (allBuslines.get(i).get(1)
//							.equalsIgnoreCase(station.get(1))) {
//						curItem = allBuslines.get(i);
//						curItem.set(3, "fav");
//						break;
//					}
//				}
//				if (k == 0 || k == i) {
//					curItem = new ArrayList<String>();
//					curItem.add(station.get(0));
//					curItem.add(station.get(1));
//					curItem.add(station.get(2));
//					curItem.add("fav");
//					allBuslines.add(curItem);
//				}
//				if (allBuslines.size() > 5) {
//					viewAllBuses.setVisibility(View.VISIBLE);
//					viewAllBuses.setText("查看所有收藏线路");
//					break;
//				} else {
//					viewAllBuses.setVisibility(View.GONE);
//				}
//			}
//
//			m = 0;
//			for (ArrayList<String> busline : userdabase
//					.getLatestSearchBuslines()) {
//				if (m++ == 5) {
//					break;
//				}
//				int k = allBuslines.size();
//				int i;
//				for (i = 0; i < k; i++) {
//					if (allBuslines.get(i).get(1)
//							.equalsIgnoreCase(busline.get(1))) {
//						curItem = allBuslines.get(i);
//						if (curItem.get(3).equalsIgnoreCase("fav") == false)
//							curItem.set(3, "search");
//						break;
//					}
//				}
//				if (k == 0 || k == i) {
//					curItem = new ArrayList<String>();
//					curItem.add(busline.get(0));
//					curItem.add(busline.get(1));
//					curItem.add(busline.get(2));
//					curItem.add("search");
//					allBuslines.add(curItem);
//				}
//				if (allBuslines.size() > 10) {
//					break;
//				}
//			}
//
//			mtAllBuslinesAdapter.notifyDataSetChanged();
//
//			if (isNetworkConnected(RootView.this)) {
//				buslineContainer.setVisibility(View.VISIBLE);
//			} else {
//				buslineContainer.setVisibility(View.GONE);
//			}
//		} else if (segmented.getCheckedRadioButtonId() == R.id.radBtn_work) {
//			// 附近
//			LocationUtil mApplication = LocationUtil.getInstance(RootView.this);
//			;
//			BDLocation location = mApplication.mLocation;
//			nearbyStations = DataBaseHelper.getInstance(RootView.this)
//					.getAroundStationsWithLocation(
//							new LatLng(location.getLatitude(), location
//									.getLongitude()));
//			for (ArrayList<String> station : nearbyStations) {
//				int k = allStations.size();
//				int i;
//				for (i = 0; i < k; i++) {
//					if (allStations.get(i).get(0).trim()
//							.equalsIgnoreCase(station.get(1).trim())) {
//						curItem = allStations.get(i);
//						if (curItem.get(3).equalsIgnoreCase("fav") == false)
//							if (curItem.get(3).equalsIgnoreCase("search") == false)
//								curItem.set(3, "nearby");
//						break;
//					}
//				}
//				if (k == 0 || k == i) {
//					curItem = new ArrayList<String>();
//					curItem.add(station.get(1));
//					curItem.add(station.get(2));
//					curItem.add(station.get(3));
//					curItem.add("nearby");
//					allStations.add(curItem);
//				}
//				if (allStations.size() > 7) {
//					viewAllStation.setVisibility(View.VISIBLE);
//					viewAllStation.setText("查看附近所有站点");
//					break;
//					// 想要从缓存中获取文件
//				} else {
//					viewAllStation.setVisibility(View.GONE);
//				}
//			}
//
//			for (ArrayList<String> station : alertStations) {
//				int k = allStations.size();
//				int i;
//				for (i = 0; i < k; i++) {
//					if (allStations.get(i).get(0)
//							.equalsIgnoreCase(station.get(0))) {
//						curItem = allStations.get(i);
//						if (curItem.get(3).equalsIgnoreCase("search"))
//							curItem.set(3, "alert1");
//						if (curItem.get(3).equalsIgnoreCase("fav"))
//							curItem.set(3, "alert1");
//						if (curItem.get(3).equalsIgnoreCase("nearby"))
//							curItem.set(3, "alert2");
//						break;
//					}
//				}
//			}
//
//			mtAllStationsAdapter.notifyDataSetChanged();
//
//			userdabase = UserDataDBHelper.getInstance(RootView.this);
//			allBuslines.clear();
//
//			nearbyBuslines = DataBaseHelper.getInstance(RootView.this)
//					.getNearbyBuslineWithLocation(
//							new LatLng(mApplication.mLocation.getLatitude(),
//									mApplication.mLocation.getLongitude()));
//			for (ArrayList<String> busline : nearbyBuslines) {
//				int k = allBuslines.size();
//				int i;
//				for (i = 0; i < k; i++) {
//					if (allBuslines.get(i).get(1)
//							.equalsIgnoreCase(busline.get(1))) {
//						curItem = allBuslines.get(i);
//						if (curItem.get(3).equalsIgnoreCase("fav") == false)
//							if (curItem.get(3).equalsIgnoreCase("search") == false)
//								curItem.set(3, "nearby");
//						break;
//					}
//				}
//				if (k == 0 || k == i) {
//					curItem = new ArrayList<String>();
//					curItem.add(busline.get(0));
//					curItem.add(busline.get(1));
//					curItem.add(busline.get(2));
//					curItem.add("nearby");
//					allBuslines.add(curItem);
//				}
//				if (allBuslines.size() > 10) {
//					viewAllBuses.setVisibility(View.VISIBLE);
//					viewAllBuses.setText("查看附近所有线路");
//					break;
//				} else {
//					viewAllBuses.setVisibility(View.GONE);
//				}
//			}
//
//			mtAllBuslinesAdapter.notifyDataSetChanged();
//
//			if (isNetworkConnected(RootView.this)) {
//				buslineContainer.setVisibility(View.VISIBLE);
//			} else {
//				buslineContainer.setVisibility(View.GONE);
//			}
//		}
//
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		// Stop polling service
//		System.out.println("Stop polling service...");
//		PollingUtils.stopPollingService(RootView.this, PollingService.class,
//				PollingService.ACTION);
//	}
//
//	public void GetAllBuses() {
//		if (isNetworkConnected(RootView.this)) {
//			rtbusListData.clear();
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("itemsIcon", R.drawable.bus_img);
//			map.put("itemsTitle", "暂未发现实时公交");
//			map.put("itemsText", "敬请关注");
//			rtbusListData2.add(map);
//
//			for (int i = 0; i < rtbusListData2.size(); i++) {
//				rtbusListData.add(rtbusListData2.get(i));
//			}
//			rtbusListData2.clear();
//			rtbusListData2.add(map);
//
//			if (segmented.getCheckedRadioButtonId() == R.id.radBtn_all) {
//				int j = allRtBuses.size();
//				for (int i = 0; i < j; i++) {
//					ArrayList<String> busline = new ArrayList<String>();
//					busline = allRtBuses.get(i);
//					ArrayList<String> buslineArrayList = BuslineDBHelper
//							.getInstance(RootView.this)
//							.getBuslineInfoWithBuslineName(busline.get(2));
//					if (buslineArrayList.size() > 0) {
//						try {
//							get_realtime_data(busline, 1);
//						} catch (UnsupportedEncodingException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}
//			} else {
//				nearbyBuslines = DataBaseHelper.getInstance(RootView.this)
//						.getNearbyBuslineWithLocation(
//								new LatLng(
//										mApplication.mLocation.getLatitude(),
//										mApplication.mLocation.getLongitude()));
//
//				int j = nearbyBuslines.size();
//				for (int i = 0; i < j; i++) {
//					ArrayList<String> busline = new ArrayList<String>();
//					busline = nearbyBuslines.get(i);
//					ArrayList<String> buslineArrayList = BuslineDBHelper
//							.getInstance(RootView.this)
//							.getBuslineInfoWithBuslineName(busline.get(2));
//					if (buslineArrayList.size() > 0) {
//						try {
//							get_realtime_data(busline, 1);
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
//
//		}
//	}
//
//	public void get_realtime_data(final ArrayList<String> busline,
//			int station_num) throws JSONException, UnsupportedEncodingException {
//		ArrayList<String> buslineArrayList = BuslineDBHelper.getInstance(
//				RootView.this).getBuslineInfoWithBuslineName(busline.get(2));
//		if (buslineArrayList.size() > 0) {
//			String JSONDataUrl = "http://bjgj.aibang.com:8899/bus.php?city="
//					+ URLEncoder.encode("北京", "utf-8") + "&id="
//					+ buslineArrayList.get(3) + "&no=" + station_num
//					+ "&type=0&encrypt=1&versionid=2";
//			String value = MobclickAgent.getConfigParams(RootView.this,
//					"rtbusurl");
//			if (value.toString().length() > 0) {
//				JSONDataUrl = value + "&" + URLEncoder.encode("北京", "utf-8")
//						+ "&id=" + buslineArrayList.get(3) + "&no="
//						+ station_num;
//			}
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
//										busline)).start();
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
//			MyVolley.sharedVolley(RootView.this).getRequestQueue()
//					.add(jsonObjectRequest);
//
//		}
//	}
//
//	public class doRequestResult implements Runnable {
//		private JSONArray busesArray;
//		private ArrayList<String> curbusline;
//
//		public doRequestResult(JSONArray busesArray, ArrayList<String> busline) {
//			this.busesArray = busesArray;
//			this.curbusline = busline;
//		}
//
//		public void run() {
//			try {
//				try {
//					int j = busesArray.length();
//					int min = 0;
//					double distance = 0;
//					double mindistance = 1000000000.0;
//					int min2 = 0;
//					double mindistance2 = 1000000000.0;
//					LocationUtil locater = LocationUtil
//							.getInstance(RootView.this);
//					String stationName = "";
//					for (int i = 0; i < j; i++) {
//
//						JSONObject jsonObject = (JSONObject) busesArray.get(i);
//						stationName = new MyCipher("aibang"
//								+ jsonObject.getString("gt"))
//								.decrypt(jsonObject.getString("ns"));
//						ArrayList<ArrayList<String>> stations = DataBaseHelper
//								.getInstance(RootView.this)
//								.getSpecificStationsWithBuslineName(curbusline.get(2));
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
//								if (curbusline.get(3).equalsIgnoreCase(
//										arrayList.get(0))) {
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
//							.getInstance(RootView.this)
//							.getSpecificStationsWithBuslineName(curbusline.get(2));
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
//								if (curbusline.get(3).equalsIgnoreCase(
//										arrayList.get(0))) {
//									myNum = i;
//									tag = 0;
//									break;
//								}
//							}
//						}
//
//						if (myNum - curNum >= 0) {
//							HashMap<String, Object> map = new HashMap<String, Object>();
//							map.put("itemsIcon", R.drawable.bus_img);
//							String nameString = curbusline.get(2);
//							nameString = "<font color=\"red\">"
//									+ nameString.substring(0,
//											nameString.indexOf("("))
//									+ "</font><font color=\"white\" background-color=\"red\">开往</font> <font color=\"yellow\">"
//									+ nameString.substring(nameString
//											.indexOf("-") + 1);
//							nameString = nameString.substring(0,
//									nameString.length() - 1);
//							map.put("itemsTitle",
//									nameString
//											+ "</font>"
//											+ " 即将到达<font color=\"red\"> "
//											+ new MyCipher("aibang"
//													+ jsonObject
//															.getString("gt"))
//													.decrypt(jsonObject
//															.getString("ns"))
//											+ "</font>");
//							if ((myNum - curNum) == 0) {
//								map.put("itemsTitle", nameString
//										+ "</font> <font color=\"red\">即将到站("
//										+ curbusline.get(3) + ")</font> ");
//								map.put("itemsText", "请做好接车准备");
//
//							} else {
//								map.put("itemsText", "<font color=\"black\">"
//										+ (myNum - curNum)
//										+ "</font> 站到<font color=\"red\">"
//										+ curbusline.get(3) + "</font> , "
//										+ (int) (distanceTotal / 100) / 10.0
//										+ " km");
//							}
//
//							map.put("distance", mindistance + "");
//							map.put("busline", curbusline);
//							rtbusListData2.add(map);
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
//								if (curbusline.get(3).equalsIgnoreCase(
//										arrayList.get(0))) {
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
//								String nameString = curbusline.get(2);
//								nameString = "<font color=\"red\">"
//										+ nameString.substring(0,
//												nameString.indexOf("("))
//										+ "</font><font color=\"white\" background-color=\"red\">开往</font><font color=\"yellow\">"
//										+ nameString.substring(nameString
//												.indexOf("-") + 1);
//								nameString = nameString.substring(0,
//										nameString.length() - 1);
//								map.put("itemsTitle", nameString + "</font>"
//										+ " 即将到达  <font color=\"red\">"
//										+ stationName + "</font>");
//								if ((myNum - curNum) == 0) {
//									map.put("itemsTitle",
//											nameString
//													+ "</font>  <font color=\"red\">即将到站("
//													+ curbusline.get(3)
//													+ ")</font> ");
//									map.put("itemsText",
//											"下辆 <font color=\"black\">"
//													+ (myNum - curNum2)
//													+ "</font> 站, "
//													+ (int) (distanceTotal2 / 100)
//													/ 10.0 + "km");
//
//								} else {
//									map.put("itemsText",
//											"<font color=\"black\">"
//													+ (myNum - curNum)
//													+ "</font> 站到<font color=\"red\">"
//													+ curbusline.get(3)
//													+ "</font> , "
//													+ (int) (distanceTotal / 100)
//													/ 10.0
//													+ " km   &nbsp;&nbsp;&nbsp;&nbsp;下辆 <font color=\"black\">"
//													+ (myNum - curNum2)
//													+ "</font> 站, "
//													+ (int) (distanceTotal2 / 100)
//													/ 10.0 + "km");
//								}
//
//								map.put("distance", mindistance + "");
//								map.put("busline", curbusline);
//								rtbusListData2.add(map);
//							} else {
//								HashMap<String, Object> map = new HashMap<String, Object>();
//								map.put("itemsIcon", R.drawable.bus_img);
//								String nameString = curbusline.get(2);
//								nameString = "<font color=\"red\">"
//										+ nameString.substring(0,
//												nameString.indexOf("("))
//										+ "</font><font color=\"white\" background-color=\"red\">开往</font><font color=\"yellow\">"
//										+ nameString.substring(nameString
//												.indexOf("-") + 1);
//								nameString = nameString.substring(0,
//										nameString.length() - 1);
//								map.put("itemsTitle", nameString + "</font>"
//										+ " 即将到达  <font color=\"red\">"
//										+ stationName + "</font>");
//								if ((myNum - curNum) == 0) {
//									map.put("itemsTitle",
//											nameString
//													+ "</font> <font color=\"red\">即将到站("
//													+ curbusline.get(3)
//													+ ")</font> ");
//									map.put("itemsText", "请做好接车准备");
//
//								} else {
//									map.put("itemsText",
//											"<font color=\"black\">"
//													+ (myNum - curNum)
//													+ "</font> 站到<font color=\"red\">"
//													+ curbusline.get(3)
//													+ "</font> , "
//													+ (int) (distanceTotal / 100)
//													/ 10.0 + " km");
//								}
//
//								map.put("distance", mindistance + "");
//								map.put("busline", curbusline);
//								rtbusListData2.add(map);
//							}
//						}
//					}
//
//					int l = rtbusListData2.size();
//					HashMap<String, Object> mmap;
//					for (int i = 0; i < l; i++) {
//						mmap = (HashMap<String, Object>) rtbusListData2.get(i);
//						if ((mmap.get("itemsTitle")).toString()
//								.equalsIgnoreCase("暂未发现实时公交")) {
//							rtbusListData2.remove(i);
//							i--;
//							l--;
//						}
//					}
//					// 排序
//					int m = rtbusListData2.size();
//					HashMap<String, Object> map2;
//					for (int i = m - 1; i > 0; --i) {
//						for (int n = 0; n < i; ++n) {
//							if (Double.parseDouble((String) rtbusListData2.get(
//									n + 1).get("distance")) < Double
//									.parseDouble((String) rtbusListData2.get(n)
//											.get("distance"))) {
//								map2 = rtbusListData2.get(n);
//								rtbusListData2.remove(n);
//								rtbusListData2.add(n + 1, map2);
//							}
//						}
//					}
//
//					if (rtbusListData2.size() == 0) {
//						HashMap<String, Object> map = new HashMap<String, Object>();
//						map.put("itemsIcon", R.drawable.bus_img);
//						map.put("itemsTitle", "暂未发现实时公交");
//						map.put("itemsText", "敬请关注");
//						rtbusListData2.add(map);
//					}
//				} catch (SQLException sqle) {
//					throw sqle;
//				}
//
//			} catch (JSONException e) {
//				Log.e("JSON exception", e.getMessage());
//				e.printStackTrace();
//			}
//			Message msgMessage = new Message();
//			mHandler.sendMessage(msgMessage);
//		}
//	}
//
//	protected class MyGridAdapter extends BaseAdapter {
//
//		public ArrayList<ArrayList<String>> stations;
//		public int type;
//
//		public MyGridAdapter(ArrayList<ArrayList<String>> stations, int type) {
//			// TODO Auto-generated constructor stub
//			this.stations = stations;
//			this.type = type;
//		}
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return stations.size() + 1;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			View view = View.inflate(RootView.this, R.layout.bus_grid_item,
//					null);
//			TextView title = (TextView) view.findViewById(R.id.tv_waitStation);
//			ImageView img = (ImageView) view.findViewById(R.id.img_name);
//			img.setVisibility(View.INVISIBLE);
//			if (position == stations.size()) {
//				title.setText("");
//				title.setBackgroundResource(R.drawable.searchmore);
//			} else if (type == 0) {
//				String type = stations.get(position).get(3);
//				if (type.equalsIgnoreCase("search")) {
//					title.setBackgroundColor(0xffff9234);
//				} else if (type.equalsIgnoreCase("nearby")) {
//					title.setBackgroundColor(0xffffca54);
//				} else if (type.equalsIgnoreCase("fav")) {
//					img.setVisibility(View.VISIBLE);
//					title.setBackgroundColor(0xffff9234);
//					img.setImageResource(R.drawable.favimgbg);
//					;
//				} else if (type.equalsIgnoreCase("alert1")) {
//					img.setVisibility(View.VISIBLE);
//					title.setBackgroundColor(0xffff9234);
//					img.setImageResource(R.drawable.alertimgbg);
//				} else if (type.equalsIgnoreCase("alert2")) {
//					title.setBackgroundColor(0xffffca54);
//					img.setVisibility(View.VISIBLE);
//					img.setImageResource(R.drawable.alertimgbg);
//				}
//				title.setText(stations.get(position).get(0));
//			} else if (type == 1) {
//				String type = stations.get(position).get(3);
//				if (type.equalsIgnoreCase("search")) {
//					title.setBackgroundColor(0xffff9234);
//				}
//				if (type.equalsIgnoreCase("fav")) {
//					img.setVisibility(View.VISIBLE);
//					title.setBackgroundColor(0xffff9234);
//					img.setImageResource(R.drawable.favimgbg);
//					;
//				} else if (type.equalsIgnoreCase("nearby")) {
//					title.setBackgroundColor(0xffffca54);
//				}
//
//				title.setText(stations.get(position).get(1));
//			}
//			return view;
//		}
//
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//
//		MobclickAgent.onPageEnd("SplashScreen"); // ��֤ onPageEnd ��onPause
//		// ֮ǰ����,��Ϊ onPause
//		// �лᱣ����Ϣ
//		MobclickAgent.onPause(this);
//		task2.cancel();
//		timer2.cancel();
//		unregisterReceiver(mWifiReceiver);
//		unregisterReceiver(mActivityReceiver);
//
//	}
//
//	@Override
//	public void onStart() {
//		super.onStart();
//
//	}
//
//	@Override
//	public void onRestart() {
//		super.onRestart();
//
//		refreshData();
//
//		getSearchList();
//		timer2 = new Timer();
//		task2 = new TimerTask() {
//			public void run() {
//				runOnUiThread(new Runnable() {
//					public void run() {
//						// GetAllBuses();
//						if (isNetworkConnected(RootView.this)) {
//							GetAllBuses();
//						}
//
//					}
//				});
//			}
//		};
//		if (isNetworkConnected(RootView.this)) {
//			buslineContainer.setVisibility(View.VISIBLE);
//		} else {
//			buslineContainer.setVisibility(View.GONE);
//		}
//		SharedPreferences mySharedPreferences = getSharedPreferences("setting",
//				SettingView.MODE_PRIVATE);
//		String timeString = mySharedPreferences.getString("refreshMode", "30秒");
//		timeString = timeString.substring(0, timeString.length() - 1);
//		timer2.schedule(task2, 1000, Integer.parseInt(timeString) * 1000);
//
//	}
//
//	/**
//	 * * 实现 ActionBar.OnNavigationListener接口
//	 */
//	class DropDownListenser implements OnNavigationListener {
//		// 得到和SpinnerAdapter里一致的字符数组
//		String[] listNames = getResources().getStringArray(R.array.function);
//
//		 当选择下拉菜单项的时候，将Activity中的内容置换为对应的Fragment 
//		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
//			// 生成自定的Fragment
//
//			return true;
//		}
//	}
//
//	private void initTitleRightLayout() {
//		clearRightView();
//		View rightViewApp = mInflater.inflate(R.layout.app_btn, null);
//		Button appBtn = (Button) rightViewApp.findViewById(R.id.appBtn);
//		appBtn.setBackgroundDrawable(null);
//		appBtn.setTextColor(Color.WHITE);
//		appBtn.setText("功能菜单");
//		appBtn.setPadding(25, 5, 25, 5);
//		appBtn.setTextSize(18);
//
//		addRightView(rightViewApp);
//
//		appBtn.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// Intent intent = null;
//				popMenu.showAsDropDown(v);
//			}
//
//		});
//	}
//
//}
