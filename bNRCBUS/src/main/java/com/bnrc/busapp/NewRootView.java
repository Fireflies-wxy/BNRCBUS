/*package com.bnrc.busapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import android.app.AlertDialog;
import android.app.ActionBar.OnNavigationListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.global.AbConstant;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.bnrc.network.MyVolley;
import com.bnrc.network.StringRequest;
import com.bnrc.network.toolbox.Response;
import com.bnrc.network.toolbox.VolleyError;
import com.bnrc.busapp.R;
import com.umeng.analytics.MobclickAgent;
import com.bnrc.util.BuslineDBHelper;
import com.bnrc.util.DataBaseHelper;
import com.bnrc.util.LocationUtil;
import com.bnrc.util.SlidingDrawerGridView;
import com.bnrc.util.UserDataDBHelper;
import com.bnrc.util.collectwifi.BaseActivity;
import com.bnrc.util.collectwifi.Constants;
import com.bnrc.util.collectwifi.ScanService;
import com.bnrc.util.collectwifi.ServiceUtils;

import android.database.SQLException;

public class NewRootView extends BaseActivity {

	private TextView curlocation = null;
	private ImageView refesh = null;
	public LocationUtil mApplication = null;

	private SlidingDrawerGridView mtAllStations;
	private MyGridAdapter mtAllStationsAdapter;
	private SlidingDrawerGridView mtAllBuslines;
	private MyGridAdapter mtAllBuslinesAdapter;

	private SlidingDrawerGridView mtSearchedStations;
	private MyGridAdapter mtSearchedStationsAdapter;
	private SlidingDrawerGridView mtSearchedBuslines;
	private MyGridAdapter mtSearchedBuslinesAdapter;

	private LinearLayout buslineContainer;
	private ScrollView scrollView1;
	private ScrollView scrollView2;
	private AbHorizontalScrollView mainScrollView;

	private Button settingBtn;
	private Button dianyingBtn;
	private Button cantingBtn;
	private Button yinhangBtn;
	private Button chaoshiBtn;
	private Button tixingBtn;
	private Button favStationBtn;
	private Button favBuslineBtn;
	private ImageView openAlertView;
	private Handler mHandler;
	private String isOpenAlert;
	private SegmentedGroup segmented;

	private int firstTimeRequest = 0;
	private Timer timer3;
	private TimerTask task3;
	private Timer timer2;
	private TimerTask task2;
	public UserDataDBHelper userdabase = null;
	private ArrayList<ArrayList<String>> nearbyStations = null;
	private ArrayList<ArrayList<String>> favStations = null;
	private ArrayList<ArrayList<String>> alertStations = null;
	private ArrayList<ArrayList<String>> searchedStations = null;
	private ArrayList<ArrayList<String>> allStations = null;
	public List<HashMap<String, Object>> rtbusListData;
	public List<HashMap<String, Object>> rtbusListData2;
	public ArrayList<ArrayList<String>> nearbyBuslines;
	public ArrayList<ArrayList<String>> allRtBuses;
	private ArrayList<ArrayList<String>> favBuslines = null;
	private ArrayList<ArrayList<String>> searchedBuslines = null;
	private ArrayList<ArrayList<String>> allBuslines = null;
	private ArrayList<View> rtBusArrayList = null;
	private PopMenu popMenu;

	OnItemClickListener popmenuItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			popMenu.dismiss();
			if (position == 0) {
				Intent intent = new Intent(NewRootView.this,
						MyAlertStationView.class);
				intent.putExtra("keyword", "");
				intent.putExtra("TEXT", NewRootView.this.getResources()
						.getString(R.string.title_transparent_desc));
				intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
						AbConstant.TITLE_TRANSPARENT);
				startActivity(intent);
				// overridePendingTransition(R.anim.out_alpha,
				// R.anim.enter_alpha);
			} else if (position == 1) {
				Intent intent = new Intent(NewRootView.this,
						MyFavoriteStationView.class);
				intent.putExtra("keyword", "");
				intent.putExtra("TEXT", NewRootView.this.getResources()
						.getString(R.string.title_transparent_desc));
				intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
						AbConstant.TITLE_TRANSPARENT);
				startActivity(intent);
			} else if (position == 2) {
				Intent intent = new Intent(NewRootView.this,
						MyFavoriteBuslineView.class);
				intent.putExtra("keyword", "");
				intent.putExtra("TEXT", NewRootView.this.getResources()
						.getString(R.string.title_transparent_desc));
				intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
						AbConstant.TITLE_TRANSPARENT);
				startActivity(intent);
			} else if (position == 3) {
				UserDataDBHelper.getInstance(NewRootView.this)
						.deleteAllSearchHistory();
				Toast toast = Toast.makeText(getApplicationContext(),
						"您已清空所有搜索数据~", Toast.LENGTH_LONG);
				toast.show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.new_root_view);

		isOpenAlert = "开启提醒功能";
		MobclickAgent.updateOnlineConfig(this);
		String value = MobclickAgent.getConfigParams(NewRootView.this,
				"bus_data_version");
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(value);
			String version = jsonObj.getString("version");
			String ready = jsonObj.getString("ready");
			SharedPreferences mySharedPreferences = getSharedPreferences(
					"setting", SettingView.MODE_PRIVATE);
			String oldVersion = mySharedPreferences.getString(
					"bus_data_version", "1");
			if (ready.equalsIgnoreCase("YES")
					&& (version.equalsIgnoreCase(oldVersion) == false)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						NewRootView.this);
				builder.setMessage("公交数据已经推出了新的版本，您是否要更新？").setTitle("友情提示")
						.setNegativeButton("取消", null);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
//								DataBaseHelper.getInstance(NewRootView.this)
//										.DownFileWithUrl(
//												MobclickAgent.getConfigParams(
//														NewRootView.this,
//														"bus_data_url"));

							}
						});
				builder.show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		value = MobclickAgent.getConfigParams(NewRootView.this,
				"realtime_bus_data_version");
		Log.i("realtime_bus_data_version", value);
		try {
			jsonObj = new JSONObject(value);
			String version = jsonObj.getString("version");
			String ready = jsonObj.getString("ready");
			SharedPreferences mySharedPreferences = getSharedPreferences(
					"setting", SettingView.MODE_PRIVATE);
			String oldVersion = mySharedPreferences.getString(
					"realtime_bus_data_version", "1");
			if (ready.equalsIgnoreCase("YES")
					&& (version.equalsIgnoreCase(oldVersion) == false)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						NewRootView.this);
				builder.setMessage("实时公交数据有更新，您是否要更新？").setTitle("友情提示")
						.setNegativeButton("取消", null);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
//								BuslineDBHelper
//										.getInstance(NewRootView.this)
//										.DownFileWithUrl(
//												MobclickAgent
//														.getConfigParams(
//																NewRootView.this,
//																"realtime_bus_data_url"));

							}
						});
				builder.show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mApplication = LocationUtil.getInstance(NewRootView.this);
		mApplication.startLocation();
		initbase();
		popMenu = new PopMenu(NewRootView.this);
		popMenu.addItems(new String[] { "下车提醒站点", "我收藏的站点", "我收藏的线路", "清空搜索记录" });
		popMenu.setOnItemClickListener(popmenuItemClickListener);
		// initTitleRightLayout();

		nearbyStations = new ArrayList<ArrayList<String>>();
		favStations = new ArrayList<ArrayList<String>>();
		favBuslines = new ArrayList<ArrayList<String>>();
		alertStations = new ArrayList<ArrayList<String>>();
		searchedStations = new ArrayList<ArrayList<String>>();
		searchedBuslines = new ArrayList<ArrayList<String>>();
		allStations = new ArrayList<ArrayList<String>>();
		allBuslines = new ArrayList<ArrayList<String>>();

		refesh = (ImageView) findViewById(R.id.refresh);
		refesh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				curlocation.setText(mApplication.addressString);
				if (allBuslines.size() == 0 && allStations.size() == 0) {
					getNearbyStationsAndBuslines();
				}

			}
		});

		openAlertView = (ImageView) findViewById(R.id.open_alert_btn);
		openAlertView.setBackgroundResource(R.drawable.alertoffimg);
		openAlertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isOpenAlert.equalsIgnoreCase("开启提醒功能")) {
					isOpenAlert = "关闭提醒功能";
					PollingUtils.startPollingService(NewRootView.this, 5,
							PollingService.class, PollingService.ACTION);
					openAlertView.setBackgroundResource(R.drawable.alertonimg);
					Toast toast = Toast.makeText(getApplicationContext(),
							"您已开启提醒功能~", Toast.LENGTH_LONG);
					toast.show();
				} else {
					isOpenAlert = "开启提醒功能";
					PollingUtils.stopPollingService(NewRootView.this,
							PollingService.class, PollingService.ACTION);
					openAlertView.setBackgroundResource(R.drawable.alertoffimg);
					Toast toast = Toast.makeText(getApplicationContext(),
							"您已关闭提醒功能~", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});

		final int screenWidth;// 屏幕宽度
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		screenWidth = display.getWidth();
		scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
		scrollView2 = (ScrollView) findViewById(R.id.scrollView2);
		scrollView1.setLayoutParams(new LinearLayout.LayoutParams(screenWidth,
				LinearLayout.LayoutParams.MATCH_PARENT));
		scrollView2.setLayoutParams(new LinearLayout.LayoutParams(screenWidth,
				LinearLayout.LayoutParams.MATCH_PARENT));
		mainScrollView = (AbHorizontalScrollView) findViewById(R.id.mainScrollView);
		mainScrollView.setSmoothScrollingEnabled(true);

		mtAllStations = (SlidingDrawerGridView) findViewById(R.id.all_bus);
		mtAllStationsAdapter = new MyGridAdapter(allStations, 0);
		mtAllStations.setAdapter(mtAllStationsAdapter);

		mtAllBuslines = (SlidingDrawerGridView) findViewById(R.id.mtmyBuslines);
		mtAllBuslinesAdapter = new MyGridAdapter(allBuslines, 1);
		mtAllBuslines.setAdapter(mtAllBuslinesAdapter);

		mtSearchedStations = (SlidingDrawerGridView) findViewById(R.id.searched_station);
		mtSearchedStationsAdapter = new MyGridAdapter(searchedStations, 2);
		mtSearchedStations.setAdapter(mtSearchedStationsAdapter);

		mtSearchedBuslines = (SlidingDrawerGridView) findViewById(R.id.searched_bus);
		mtSearchedBuslinesAdapter = new MyGridAdapter(searchedBuslines, 3);
		mtSearchedBuslines.setAdapter(mtSearchedBuslinesAdapter);

		mtAllStations.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position < allStations.size()) {
					Intent intent = new Intent(NewRootView.this,
							StationInformationView.class);
					ArrayList<String> station = new ArrayList<String>();
					station = allStations.get(position);
					intent.putExtra("title", station.get(0));
					intent.putExtra("latitude", station.get(1));
					intent.putExtra("longitude", station.get(2));
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in,
							android.R.anim.fade_out);
				} else {
					if (segmented.getCheckedRadioButtonId() == R.id.in_month
							&& favBuslines.size() > 0) {
						Intent intent = new Intent(NewRootView.this,
								MyFavoriteStationView.class);
						intent.putExtra("keyword", "");
						intent.putExtra("TEXT", NewRootView.this.getResources()
								.getString(R.string.title_transparent_desc));
						intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
								AbConstant.TITLE_TRANSPARENT);
						startActivity(intent);
					} else {
						Intent intent = new Intent(NewRootView.this,
								NearbyStationListView.class);
						intent.putExtra("TEXT", NewRootView.this.getResources()
								.getString(R.string.title_transparent_desc));
						intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
								AbConstant.TITLE_TRANSPARENT);
						startActivity(intent);
						overridePendingTransition(android.R.anim.fade_in,
								android.R.anim.fade_out);
					}
				}
			}
		});

		mtSearchedStations.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position < searchedStations.size()) {
					Intent intent = new Intent(NewRootView.this,
							StationInformationView.class);
					ArrayList<String> station = new ArrayList<String>();
					station = searchedStations.get(position);
					intent.putExtra("title", station.get(0));
					intent.putExtra("latitude", station.get(1));
					intent.putExtra("longitude", station.get(2));
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in,
							android.R.anim.fade_out);
				} else {
					Intent intent = new Intent(NewRootView.this,
							NearbyStationListView.class);
					intent.putExtra("TEXT", NewRootView.this.getResources()
							.getString(R.string.title_transparent_desc));
					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
							AbConstant.TITLE_TRANSPARENT);
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in,
							android.R.anim.fade_out);
				}

			}
		});

		// item
		mtAllStations.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				final ArrayList<String> station = allStations.get(arg2);

				final String alertString = userdabase
						.checkAlertStationWithStation(station) ? "删除下车提醒"
						: "添加到下车提醒";
				final String favString = userdabase
						.checkFavStationWithStationID(station.get(0)) ? "取消收藏站点"
						: "添加收藏站点";
				final String[] arrayChoice = new String[] { "查看站点详情",
						alertString, favString };
				new AlertDialog.Builder(NewRootView.this)
						// build AlertDialog
						.setTitle("您可进行的操作")
						// title
						.setItems(arrayChoice,
								new DialogInterface.OnClickListener() { // content
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (which == 0) {
											// Intent intent = null;
											Intent intent = new Intent(
													NewRootView.this,
													StationInformationView.class);
											ArrayList<String> station = new ArrayList<String>();
											station = allStations.get(which);
											intent.putExtra("title",
													station.get(0));
											intent.putExtra("latitude",
													station.get(1));
											intent.putExtra("longitude",
													station.get(2));
											startActivity(intent);
										} else if (which == 1) {
											if (alertString
													.equalsIgnoreCase("删除下车提醒")) {
												userdabase
														.deleteAlertStationWithStation(station);
												refreshData();
												Toast toast = Toast
														.makeText(
																getApplicationContext(),
																"您已删除\""
																		+ station
																				.get(0)
																		+ "\"下车提醒站点",
																Toast.LENGTH_LONG);
												toast.show();
											} else {
												userdabase
														.addAlertStationWithStation(station);
												refreshData();
												Toast toast = Toast
														.makeText(
																getApplicationContext(),
																"您已添加\""
																		+ station
																				.get(0)
																		+ "\"为下车提醒站点",
																Toast.LENGTH_LONG);
												toast.show();
											}

										} else if (which == 2) {
											if (favString
													.equalsIgnoreCase("取消收藏站点")) {
												userdabase
														.deleteFavStationWithStation(station);
												refreshData();
												Toast toast = Toast
														.makeText(
																getApplicationContext(),
																"您已取消收藏\""
																		+ station
																				.get(0)
																		+ "\"站点",
																Toast.LENGTH_LONG);
												toast.show();

											} else {
												userdabase
														.addFavStationWithStation(station);
												refreshData();
												Toast toast = Toast
														.makeText(
																getApplicationContext(),
																"您已成功收藏\""
																		+ station
																				.get(0)
																		+ "\"站点",
																Toast.LENGTH_LONG);
												toast.show();
											}
										}
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss(); // �ر�alertDialog
									}
								}).show();
				return true;
			}
		});

		// item
		mtSearchedStations
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, final int arg2, long arg3) {
						// TODO Auto-generated method stub
						final ArrayList<String> station = searchedStations
								.get(arg2);

						final String alertString = userdabase
								.checkAlertStationWithStation(station) ? "删除下车提醒"
								: "添加到下车提醒";
						final String favString = userdabase
								.checkFavStationWithStationID(station.get(0)) ? "取消收藏站点"
								: "添加收藏站点";
						final String[] arrayChoice = new String[] { "查看站点详情",
								alertString, favString };
						new AlertDialog.Builder(NewRootView.this)
								// build AlertDialog
								.setTitle("您可进行的操作")
								// title
								.setItems(arrayChoice,
										new DialogInterface.OnClickListener() { // content
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												if (which == 0) {
													// Intent intent = null;
													Intent intent = new Intent(
															NewRootView.this,
															StationInformationView.class);
													ArrayList<String> station = new ArrayList<String>();
													station = searchedStations
															.get(which);
													intent.putExtra("title",
															station.get(0));
													intent.putExtra("latitude",
															station.get(1));
													intent.putExtra(
															"longitude",
															station.get(2));
													startActivity(intent);
												} else if (which == 1) {
													if (alertString
															.equalsIgnoreCase("删除下车提醒")) {
														userdabase
																.deleteAlertStationWithStation(station);
														refreshData();
														Toast toast = Toast
																.makeText(
																		getApplicationContext(),
																		"您已删除\""
																				+ station
																						.get(0)
																				+ "\"下车提醒站点",
																		Toast.LENGTH_LONG);
														toast.show();
													} else {
														userdabase
																.addAlertStationWithStation(station);
														refreshData();
														Toast toast = Toast
																.makeText(
																		getApplicationContext(),
																		"您已添加\""
																				+ station
																						.get(0)
																				+ "\"为下车提醒站点",
																		Toast.LENGTH_LONG);
														toast.show();
													}

												} else if (which == 2) {
													if (favString
															.equalsIgnoreCase("取消收藏站点")) {
														userdabase
																.deleteFavStationWithStation(station);
														refreshData();
														Toast toast = Toast
																.makeText(
																		getApplicationContext(),
																		"您已取消收藏\""
																				+ station
																						.get(0)
																				+ "\"站点",
																		Toast.LENGTH_LONG);
														toast.show();

													} else {
														userdabase
																.addFavStationWithStation(station);
														refreshData();
														Toast toast = Toast
																.makeText(
																		getApplicationContext(),
																		"您已成功收藏\""
																				+ station
																						.get(0)
																				+ "\"站点",
																		Toast.LENGTH_LONG);
														toast.show();
													}
												}
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss(); // �ر�alertDialog
											}
										}).show();
						return true;
					}
				});

		mtAllBuslines.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if (position < allBuslines.size()) {
					Intent intent = new Intent(NewRootView.this,
							BuslineListView.class);
					// 在意图中传递数据
					ArrayList<String> busline = new ArrayList<String>();
					busline = allBuslines.get(position);
					intent.putExtra("title", busline.get(1));
					intent.putExtra("buslineID", busline.get(0));
					intent.putExtra("buslineName", busline.get(2));
					intent.putExtra("stationName", "");
					startActivity(intent);
					// overridePendingTransition(R.anim.out_alpha,
					// R.anim.enter_alpha);
				} else {
					if (segmented.getCheckedRadioButtonId() == R.id.in_month
							&& favBuslines.size() > 0) {
						Intent intent = new Intent(NewRootView.this,
								MyFavoriteBuslineView.class);
						intent.putExtra("keyword", "");
						intent.putExtra("TEXT", NewRootView.this.getResources()
								.getString(R.string.title_transparent_desc));
						intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
								AbConstant.TITLE_TRANSPARENT);
						startActivity(intent);
					} else {
						Intent intent = new Intent(NewRootView.this,
								SearchBuslineView.class);
						intent.putExtra("keyword", "");
						intent.putExtra("TEXT", NewRootView.this.getResources()
								.getString(R.string.title_transparent_desc));
						intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
								AbConstant.TITLE_TRANSPARENT);
						startActivity(intent);
					}
				}
			}
		});

		mtSearchedBuslines.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position < searchedBuslines.size()) {
					Intent intent = new Intent(NewRootView.this,
							BuslineListView.class);
					// 在意图中传递数据
					ArrayList<String> busline = new ArrayList<String>();
					busline = searchedBuslines.get(position);
					intent.putExtra("title", busline.get(1));
					intent.putExtra("buslineID", busline.get(0));
					intent.putExtra("buslineName", busline.get(2));
					intent.putExtra("stationName", "");
					startActivity(intent);
				} else {
					Intent intent = new Intent(NewRootView.this,
							SearchBuslineView.class);
					intent.putExtra("keyword", "");
					intent.putExtra("TEXT", NewRootView.this.getResources()
							.getString(R.string.title_transparent_desc));
					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
							AbConstant.TITLE_TRANSPARENT);
					startActivity(intent);
				}

			}
		});

		mtAllBuslines.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				final ArrayList<String> busline = allBuslines.get(arg2);

				final String favString = userdabase
						.checkFavBuslineWithBuslineID(busline.get(0)) ? "取消收藏该线路"
						: "收藏该线路";
				final String[] arrayChoice = new String[] { "查看该线路", favString };
				new AlertDialog.Builder(NewRootView.this)
						.setTitle("您可进行的操作")
						.setItems(arrayChoice,
								new DialogInterface.OnClickListener() { // content
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (which == 0) {
											// Intent intent = null;
											Intent intent = new Intent(
													NewRootView.this,
													BuslineListView.class);
											ArrayList<String> busline = new ArrayList<String>();
											busline = allBuslines.get(arg2);
											intent.putExtra("title",
													busline.get(1));
											intent.putExtra("buslineID",
													busline.get(0));
											intent.putExtra("buslineName",
													busline.get(2));
											intent.putExtra("stationName", "");
											startActivity(intent);
										} else if (which == 1) {
											if (favString
													.equalsIgnoreCase("取消收藏该线路")) {
												userdabase
														.deleteFavBuslineWithBusline(busline);
												refreshData();
												Toast toast = Toast
														.makeText(
																getApplicationContext(),
																"您已取消收藏\""
																		+ busline
																				.get(2)
																		+ "路",
																Toast.LENGTH_LONG);
												toast.show();

											} else {
												userdabase
														.addFavBuslineWithBusline(busline);
												refreshData();
												Toast toast = Toast
														.makeText(
																getApplicationContext(),
																"您已成功收藏\""
																		+ busline
																				.get(2)
																		+ "路",
																Toast.LENGTH_LONG);
												toast.show();
											}
										}
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss(); // �ر�alertDialog
									}
								}).show();
				return true;
			}
		});

		mtSearchedBuslines
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, final int arg2, long arg3) {
						// TODO Auto-generated method stub
						final ArrayList<String> busline = searchedBuslines
								.get(arg2);

						final String favString = userdabase
								.checkFavBuslineWithBuslineID(busline.get(0)) ? "取消收藏该线路"
								: "收藏该线路";
						final String[] arrayChoice = new String[] { "查看该线路",
								favString };
						new AlertDialog.Builder(NewRootView.this)
								.setTitle("您可进行的操作")
								.setItems(arrayChoice,
										new DialogInterface.OnClickListener() { // content
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												if (which == 0) {
													// Intent intent = null;
													Intent intent = new Intent(
															NewRootView.this,
															BuslineListView.class);
													ArrayList<String> busline = new ArrayList<String>();
													busline = searchedBuslines
															.get(arg2);
													intent.putExtra("title",
															busline.get(1));
													intent.putExtra(
															"buslineID",
															busline.get(0));
													intent.putExtra(
															"buslineName",
															busline.get(2));
													intent.putExtra(
															"stationName", "");
													startActivity(intent);
												} else if (which == 1) {
													if (favString
															.equalsIgnoreCase("取消收藏该线路")) {
														userdabase
																.deleteFavBuslineWithBusline(busline);
														refreshData();
														Toast toast = Toast
																.makeText(
																		getApplicationContext(),
																		"您已取消收藏\""
																				+ busline
																						.get(2)
																				+ "路",
																		Toast.LENGTH_LONG);
														toast.show();

													} else {
														userdabase
																.addFavBuslineWithBusline(busline);
														refreshData();
														Toast toast = Toast
																.makeText(
																		getApplicationContext(),
																		"您已成功收藏\""
																				+ busline
																						.get(2)
																				+ "路",
																		Toast.LENGTH_LONG);
														toast.show();
													}
												}
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss(); // �ر�alertDialog
											}
										}).show();
						return true;
					}
				});

		mApplication = LocationUtil.getInstance(NewRootView.this);
		curlocation = (TextView) findViewById(R.id.curlocation);

		buslineContainer = (LinearLayout) findViewById(R.id.realtime_bus);
		TimerTask task = new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						curlocation.setText(mApplication.addressString);
					}
				});
				// curlocation.setText("当前位置："+mApplication.addressString);
			}
		};
		Timer timer = new Timer(true);
		timer.schedule(task, 2000, 10000);
		rtbusListData = new ArrayList<HashMap<String, Object>>();
		rtbusListData2 = new ArrayList<HashMap<String, Object>>();
		rtBusArrayList = new ArrayList<View>();
		settingBtn = (Button) findViewById(R.id.setting_home);
		DataBaseHelper.getInstance(NewRootView.this);

		settingBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NewRootView.this, SettingView.class);
				intent.putExtra("TEXT", NewRootView.this.getResources()
						.getString(R.string.title_transparent_desc));
				intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
						AbConstant.TITLE_TRANSPARENT);
				startActivity(intent);
				// overridePendingTransition(R.anim.out_alpha,
				// R.anim.enter_alpha);
			}
		});

		dianyingBtn = (Button) findViewById(R.id.hotel);
		cantingBtn = (Button) findViewById(R.id.canting);
		yinhangBtn = (Button) findViewById(R.id.yinhang);
		chaoshiBtn = (Button) findViewById(R.id.chaoshi);
		tixingBtn = (Button) findViewById(R.id.tixingzhandian);
		favStationBtn = (Button) findViewById(R.id.shoucangzhandian);
		favBuslineBtn = (Button) findViewById(R.id.shoucangxianlu);

		tixingBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NewRootView.this,
						MyAlertStationView.class);
				intent.putExtra("TEXT", NewRootView.this.getResources()
						.getString(R.string.title_transparent_desc));
				// ���ñ�����͸��
				intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
						AbConstant.TITLE_TRANSPARENT);
				startActivity(intent);
			}
		});

		favStationBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NewRootView.this,
						MyFavoriteStationView.class);
				intent.putExtra("TEXT", NewRootView.this.getResources()
						.getString(R.string.title_transparent_desc));
				// ���ñ�����͸��
				intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
						AbConstant.TITLE_TRANSPARENT);
				startActivity(intent);

			}
		});

		favBuslineBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NewRootView.this,
						MyFavoriteBuslineView.class);
				intent.putExtra("TEXT", NewRootView.this.getResources()
						.getString(R.string.title_transparent_desc));
				// ���ñ�����͸��
				intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
						AbConstant.TITLE_TRANSPARENT);
				startActivity(intent);

			}
		});
		dianyingBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// �Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ
				if (isNetworkConnected(NewRootView.this)) {
					Intent intent = new Intent(NewRootView.this,
							SearchSomethingView.class);
					intent.putExtra("TEXT", NewRootView.this.getResources()
							.getString(R.string.title_transparent_desc));
					// ���ñ�����͸��
					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
							AbConstant.TITLE_TRANSPARENT);
					intent.putExtra("keyword", "酒店");
					startActivity(intent);
					// overridePendingTransition(R.anim.out_alpha,
					// R.anim.enter_alpha);
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"您的网络有问题，请检查~", Toast.LENGTH_LONG);
					toast.show();
				}

			}
		});

		cantingBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isNetworkConnected(NewRootView.this)) {
					Intent intent = new Intent(NewRootView.this,
							SearchSomethingView.class);
					intent.putExtra("TEXT", NewRootView.this.getResources()
							.getString(R.string.title_transparent_desc));
					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
							AbConstant.TITLE_TRANSPARENT);
					intent.putExtra("keyword", "小吃");
					startActivity(intent);
					// overridePendingTransition(R.anim.out_alpha,
					// R.anim.enter_alpha);
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"您的网络有问题，请检查~", Toast.LENGTH_LONG);
					toast.show();
				}

			}
		});

		yinhangBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// �Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ
				if (isNetworkConnected(NewRootView.this)) {
					Intent intent = new Intent(NewRootView.this,
							SearchSomethingView.class);
					intent.putExtra("TEXT", NewRootView.this.getResources()
							.getString(R.string.title_transparent_desc));
					// ���ñ�����͸��
					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
							AbConstant.TITLE_TRANSPARENT);
					intent.putExtra("keyword", "银行");
					startActivity(intent);
					// overridePendingTransition(R.anim.out_alpha,
					// R.anim.enter_alpha);
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"您的网络有问题，请检查~", Toast.LENGTH_LONG);
					toast.show();
				}

			}
		});

		chaoshiBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// �Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ
				if (isNetworkConnected(NewRootView.this)) {
					Intent intent = new Intent(NewRootView.this,
							SearchSomethingView.class);
					intent.putExtra("TEXT", NewRootView.this.getResources()
							.getString(R.string.title_transparent_desc));
					// ���ñ�����͸��
					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
							AbConstant.TITLE_TRANSPARENT);
					intent.putExtra("keyword", "超市");
					startActivity(intent);
					// overridePendingTransition(R.anim.out_alpha,
					// R.anim.enter_alpha);
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"您的网络有问题，请检查~", Toast.LENGTH_LONG);
					toast.show();
				}

			}
		});

		// allRtBuses = UserDataDBHelper.getInstance(NewRootView.this)
		// .getMyAllFavRTBus();
		allRtBuses = new ArrayList<ArrayList<String>>();
		segmented = (SegmentedGroup) findViewById(R.id.segmented4);
		segmented.check(R.id.in_month);
		getFavStationsAndBuslines();

		mainScrollView
				.setOnScrollListener(new AbHorizontalScrollView.AbOnScrollListener() {

					@Override
					public void onScrollToRight() {
						// TODO Auto-generated method stub
						segmented.check(R.id.in_year);
						mainScrollView.scrollTo(screenWidth, 0);
					}

					@Override
					public void onScrollToLeft() {
						// TODO Auto-generated method stub
						segmented.check(R.id.in_month);
						mainScrollView.scrollTo(0, 0);
					}

					@Override
					public void onScrollStoped() {
						// TODO Auto-generated method stub
						// if (mainScrollView.getScrollX() <= screenWidth / 2) {
						// segmented.check(R.id.in_month);
						// mainScrollView.scrollTo(0, 0);
						// return;
						// } else {
						// segmented.check(R.id.in_year);
						// mainScrollView.scrollTo(screenWidth, 0);
						// return;
						// }
					}

					@Override
					public void onScroll(int arg1) {
						// TODO Auto-generated method stub

					}
				});

		segmented.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.in_month:
					mainScrollView.scrollTo(0, 0);
					return;
				case R.id.in_year:
					mainScrollView.scrollTo(screenWidth, 0);
					return;
				}
			}
		});

		task2 = new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {

						if (isNetworkConnected(NewRootView.this)) {
							GetAllBuses();
						} else {
							Toast toast = Toast.makeText(
									getApplicationContext(), "您的网络有问题哦~",
									Toast.LENGTH_LONG);
							toast.show();
						}
					}
				});
			}
		};

		timer2 = new Timer(true);
		SharedPreferences mySharedPreferences = getSharedPreferences("setting",
				SettingView.MODE_PRIVATE);
		String timeString = mySharedPreferences.getString("refreshMode", "30秒");
		timeString = timeString.substring(0, timeString.length() - 1);
		timer2.schedule(task2, 200, Integer.parseInt(timeString) * 1000);

		TimerTask task4 = new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						firstTimeRequest++;
						refreshRealtimeBuses();
					}
				});
			}
		};

		task3 = new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						getNearbyStationsAndBuslines();

					}
				});
			}
		};
		timer3 = new Timer(true);
		timer3.schedule(task3, 300, 5000 * 60);

		Timer timer4 = new Timer(true);
		timer4.schedule(task4, 0, Integer.parseInt(timeString) * 1000); // 延时1000ms后执行，1000ms执行一次

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("itemsIcon", R.drawable.bus_img);
		map.put("itemsTitle", "暂未发现实时公交");
		map.put("itemsText", "敬请关注");
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
				} else {
				}
			}
		};

	}

	public void getNearbyStationsAndBuslines() {
		favStations = UserDataDBHelper.getInstance(NewRootView.this).favStations;
		favBuslines = UserDataDBHelper.getInstance(NewRootView.this).favBuslines;
		alertStations = UserDataDBHelper.getInstance(NewRootView.this).alertStations;
		allStations.clear();
		ArrayList<String> curItem;

		// 附近
		LocationUtil mApplication = LocationUtil.getInstance(NewRootView.this);
		;
		BDLocation location = mApplication.mLocation;
		nearbyStations = DataBaseHelper.getInstance(NewRootView.this)
				.getAroundStationsWithLocation(
						new LatLng(location.getLatitude(), location
								.getLongitude()));
		for (ArrayList<String> station : nearbyStations) {
			int k = allStations.size();
			int i;
			for (i = 0; i < k; i++) {
				if (allStations.get(i).get(0).trim()
						.equalsIgnoreCase(station.get(1).trim())) {
					break;
				}
			}
			if (k == 0 || k == i) {
				curItem = new ArrayList<String>();
				curItem.add(station.get(1));
				curItem.add(station.get(2));
				curItem.add(station.get(3));
				curItem.add("nearby");
				allStations.add(curItem);
			}
			if (allStations.size() > 7) {
				break;
				// 想要从缓存中获取文件
			}
		}

		for (ArrayList<String> station : alertStations) {
			int k = allStations.size();
			int i;
			for (i = 0; i < k; i++) {
				if (allStations.get(i).get(0).equalsIgnoreCase(station.get(0))) {
					curItem = allStations.get(i);
					if (curItem.get(3).equalsIgnoreCase("nearby"))
						curItem.set(3, "alert2");
					break;
				}
			}
		}

		mtAllStationsAdapter.notifyDataSetChanged();

		userdabase = UserDataDBHelper.getInstance(NewRootView.this);
		allBuslines.clear();

		nearbyBuslines = DataBaseHelper.getInstance(NewRootView.this)
				.getNearbyBuslineWithLocation(
						new LatLng(mApplication.mLocation.getLatitude(),
								mApplication.mLocation.getLongitude()));
		for (ArrayList<String> busline : nearbyBuslines) {
			int k = allBuslines.size();
			int i;
			for (i = 0; i < k; i++) {
				if (allBuslines.get(i).get(1).equalsIgnoreCase(busline.get(1))) {
					break;
				}
			}
			if (k == 0 || k == i) {
				curItem = new ArrayList<String>();
				curItem.add(busline.get(0));
				curItem.add(busline.get(1));
				curItem.add(busline.get(2));
				curItem.add("nearby");
				curItem.add(busline.get(3));
				curItem.add(busline.get(4));
				curItem.add(busline.get(5));
				allBuslines.add(curItem);
			}
			if (allBuslines.size() > 10) {
				break;
			}
		}

		mtAllBuslinesAdapter.notifyDataSetChanged();
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

	@Override
	public void onResume() {
		super.onResume();

		PollingService.hasKnown = true;

		MobclickAgent.onPageStart("SplashScreen"); // ͳ��ҳ��
		MobclickAgent.onResume(this); // ͳ��ʱ��
		registerReceiver(mWifiReceiver, wifiFilter);
		registerReceiver(mActivityReceiver, activityFilter);

	}

	public void refreshRealtimeBuses() {
		if (rtBusArrayList == null) {
			rtBusArrayList = new ArrayList<View>();
		} else {
			buslineContainer.removeAllViews();
			rtBusArrayList.clear();
		}

		int j = rtbusListData.size();
		HashMap<String, Object> rtBus;
		for (int i = 0; (i < j) && (i < 5); i++) {
			rtBus = rtbusListData.get(i);
			View stationItem = View.inflate(NewRootView.this,
					R.layout.bus_item_view, null);
			stationItem.setTag(i + "");
			TextView title = (TextView) stationItem.findViewById(R.id.tv_title);
			TextView info = (TextView) stationItem.findViewById(R.id.tv_info);
			TextView bustitle = (TextView) stationItem
					.findViewById(R.id.bus_title);
			TextView stationinfo = (TextView) stationItem
					.findViewById(R.id.station_title);
			LinearLayout itemsContainer = (LinearLayout) stationItem
					.findViewById(R.id.itemsContainer);
			LinearLayout itemsContainer2 = (LinearLayout) stationItem
					.findViewById(R.id.itemsContainer2);
			title.setText(rtBus.get("itemsTitle").toString());
			if (rtBus.get("itemsTitle").toString().equalsIgnoreCase("暂未发现实时公交")) {
				info.setVisibility(View.GONE);
				itemsContainer.setVisibility(View.GONE);
				itemsContainer2.setVisibility(View.VISIBLE);
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
						Intent intent = new Intent(NewRootView.this,
								BuslineListView.class);
						@SuppressWarnings("unchecked")
						ArrayList<String> busline = (ArrayList<String>) rtbusListData
								.get(Integer.parseInt(arg0.getTag().toString()))
								.get("busline");
						intent.putExtra("title", busline.get(1));
						intent.putExtra("buslineID", busline.get(0));
						intent.putExtra("buslineName", busline.get(2));
						intent.putExtra("stationName", "");
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

	public void getFavStationsAndBuslines() {
		favStations = UserDataDBHelper.getInstance(NewRootView.this).favStations;
		favBuslines = UserDataDBHelper.getInstance(NewRootView.this).favBuslines;
		alertStations = UserDataDBHelper.getInstance(NewRootView.this).alertStations;

		ArrayList<ArrayList<String>> stations = UserDataDBHelper.getInstance(
				NewRootView.this).getLatestSearchStations();

		searchedStations.clear();
		ArrayList<String> curItem;
		int m = 0;

		for (ArrayList<String> station : stations) {
			int k = searchedStations.size();
			int i;
			for (i = 0; i < k; i++) {
				if (searchedStations.get(i).get(0)
						.equalsIgnoreCase(station.get(0))) {
					curItem = searchedStations.get(i);
					if (curItem.get(3).equalsIgnoreCase("fav") == false)
						curItem.set(3, "search");
					break;
				}
			}
			if (k == 0 || k == i) {
				curItem = new ArrayList<String>();
				curItem.add(station.get(0));
				curItem.add(station.get(1));
				curItem.add(station.get(2));
				curItem.add("search");
				searchedStations.add(curItem);
			}
			if (searchedStations.size() > 4) {
				break;
			}
		}

		for (ArrayList<String> station : alertStations) {
			int k = searchedStations.size();
			int i;
			for (i = 0; i < k; i++) {
				if (searchedStations.get(i).get(0)
						.equalsIgnoreCase(station.get(0))) {
					curItem = searchedStations.get(i);
					if (curItem.get(3).equalsIgnoreCase("search"))
						curItem.set(3, "alert1");
					if (curItem.get(3).equalsIgnoreCase("fav"))
						curItem.set(3, "alert1");
					if (curItem.get(3).equalsIgnoreCase("nearby"))
						curItem.set(3, "alert2");

					break;
				}
			}
		}

		mtSearchedStationsAdapter.notifyDataSetChanged();

		userdabase = UserDataDBHelper.getInstance(NewRootView.this);
		searchedBuslines.clear();

		m = 0;
		for (ArrayList<String> busline : userdabase.getLatestSearchBuslines()) {
			int k = searchedBuslines.size();
			int i;
			for (i = 0; i < k; i++) {
				if (searchedBuslines.get(i).get(1)
						.equalsIgnoreCase(busline.get(1))) {
					curItem = searchedBuslines.get(i);
					if (curItem.get(3).equalsIgnoreCase("fav") == false)
						curItem.set(3, "search");
					break;
				}
			}
			if (k == 0 || k == i) {
				curItem = new ArrayList<String>();
				curItem.add(busline.get(0));
				curItem.add(busline.get(1));
				curItem.add(busline.get(2));
				curItem.add("search");
				searchedBuslines.add(curItem);
			}
			if (searchedBuslines.size() > 4) {
				break;
			}
		}

		mtSearchedBuslinesAdapter.notifyDataSetChanged();
	}

	public void refreshData() {
		favStations = UserDataDBHelper.getInstance(NewRootView.this).favStations;
		favBuslines = UserDataDBHelper.getInstance(NewRootView.this).favBuslines;
		alertStations = UserDataDBHelper.getInstance(NewRootView.this).alertStations;
		// 收藏部分
		if (segmented.getCheckedRadioButtonId() == R.id.in_month) {
			getFavStationsAndBuslines();
		} else {
			allStations.clear();
			ArrayList<String> curItem;

			for (ArrayList<String> station : nearbyStations) {
				int k = allStations.size();
				int i;
				for (i = 0; i < k; i++) {
					if (allStations.get(i).get(0).trim()
							.equalsIgnoreCase(station.get(1).trim())) {
						break;
					}
				}
				if (k == 0 || k == i) {
					curItem = new ArrayList<String>();
					curItem.add(station.get(1));
					curItem.add(station.get(2));
					curItem.add(station.get(3));
					curItem.add("nearby");
					allStations.add(curItem);
				}
				if (allStations.size() > 7) {
					break;
					// 想要从缓存中获取文件
				}
			}

			for (ArrayList<String> station : alertStations) {
				int k = allStations.size();
				int i;
				for (i = 0; i < k; i++) {
					if (allStations.get(i).get(0)
							.equalsIgnoreCase(station.get(0))) {
						curItem = allStations.get(i);
						if (curItem.get(3).equalsIgnoreCase("nearby"))
							curItem.set(3, "alert2");
						break;
					}
				}
			}

			mtAllStationsAdapter.notifyDataSetChanged();

			allBuslines.clear();
			for (ArrayList<String> busline : nearbyBuslines) {
				int k = allBuslines.size();
				int i;
				for (i = 0; i < k; i++) {
					if (allBuslines.get(i).get(1)
							.equalsIgnoreCase(busline.get(1))) {
						break;
					}
				}
				if (k == 0 || k == i) {
					curItem = new ArrayList<String>();
					curItem.add(busline.get(0));
					curItem.add(busline.get(1));
					curItem.add(busline.get(2));
					curItem.add("nearby");
					allBuslines.add(curItem);
				}
				if (allBuslines.size() > 10) {
					break;
				}
			}

			mtAllBuslinesAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Stop polling service
		System.out.println("Stop polling service...");
		PollingUtils.stopPollingService(NewRootView.this, PollingService.class,
				PollingService.ACTION);
		ServiceUtils.stopPollingService(getApplicationContext(),
				ScanService.class, Constants.SERVICE_ACTION);
	}

	public void GetAllBuses() {
		if (isNetworkConnected(NewRootView.this)) {
			rtbusListData.clear();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemsIcon", R.drawable.bus_img);
			map.put("itemsTitle", "暂未发现实时公交");
			map.put("itemsText", "敬请关注");
			rtbusListData2.add(map);

			for (int i = 0; i < rtbusListData2.size(); i++) {
				rtbusListData.add(rtbusListData2.get(i));
			}
			rtbusListData2.clear();
			rtbusListData2.add(map);

			int j = allRtBuses.size();
			for (int i = 0; i < j; i++) {
				ArrayList<String> busline = new ArrayList<String>();
				busline = allRtBuses.get(i);
				Log.i("busline", busline.toString());
				ArrayList<String> buslineArrayList = BuslineDBHelper
						.getInstance(NewRootView.this)
						.getBuslineInfoWithBuslineName(busline.get(2));
				if (buslineArrayList.size() > 0) {
					try {
						Log.i("busline", "有公交");
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
	}

	public void get_realtime_data(final ArrayList<String> busline,
			int station_num) throws JSONException, UnsupportedEncodingException {
		ArrayList<String> buslineArrayList = BuslineDBHelper.getInstance(
				NewRootView.this).getBuslineInfoWithBuslineName(busline.get(2));
		if (buslineArrayList.size() > 0) {
			String JSONDataUrl = "http://bjgj.aibang.com:8899/bus.php?city="
					+ URLEncoder.encode("北京", "utf-8") + "&id="
					+ buslineArrayList.get(3) + "&no=" + station_num
					+ "&type=1&encrypt=1&versionid=2";
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
						}
					});
			MyVolley.sharedVolley(NewRootView.this).getRequestQueue()
					.add(jsonObjectRequest);

		}
	}

	public class doRequestResult implements Runnable {
		private JSONArray busesArray;
		private ArrayList<String> curbusline;

		public doRequestResult(JSONArray busesArray, ArrayList<String> busline) {
			this.busesArray = busesArray;
			this.curbusline = busline;
		}

		public void run() {
			try {
				try {
					int j = busesArray.length();
					int min = 0;
					double distance = 0;
					double mindistance = 1000000000.0;
					int min2 = 0;
					double mindistance2 = 1000000000.0;
					LocationUtil locater = LocationUtil
							.getInstance(NewRootView.this);
					String stationName = "";
					for (int i = 0; i < j; i++) {

						JSONObject jsonObject = (JSONObject) busesArray.get(i);
						stationName = new MyCipher("aibang"
								+ jsonObject.getString("gt"))
								.decrypt(jsonObject.getString("ns"));
						ArrayList<ArrayList<String>> stations = DataBaseHelper
								.getInstance(NewRootView.this)
								.getStationSWithBuslineName(curbusline.get(2));
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

								if (curbusline.get(3).equalsIgnoreCase(
										arrayList.get(0))) {
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
							.getInstance(NewRootView.this)
							.getStationSWithBuslineName(curbusline.get(2));
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

								if (curbusline.get(3).equalsIgnoreCase(
										arrayList.get(0))) {
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
									curbusline.get(2).substring(0,
											curbusline.get(2).indexOf("(")));
							map.put("station_title", curbusline.get(3));
							String nameString = curbusline.get(2);
							nameString = "开往</font> <font color=\"yellow\">"
									+ nameString.substring(nameString
											.indexOf("-") + 1);
							nameString = nameString.substring(0,
									nameString.length() - 1);

							map.put("itemsTitle", nameString + "</font>");
							if ((myNum - curNum) == 0) {
								map.put("itemsText",
										"</font> <font color=\"red\">即将到站</font> ");

							} else {
								map.put("itemsText", "<font color=\"black\">"
										+ (myNum - curNum) + "</font> 站, "
										+ (int) (distanceTotal / 100) / 10.0
										+ " km");
							}

							map.put("distance", mindistance + "");
							map.put("busline", curbusline);
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

								if (curbusline.get(3).equalsIgnoreCase(
										arrayList.get(0))) {
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
										curbusline.get(2).substring(0,
												curbusline.get(2).indexOf("(")));
								map.put("station_title", curbusline.get(3));
								String nameString = curbusline.get(2);
								nameString = "<font color=\"white\" background-color=\"red\">开往</font><font color=\"yellow\">"
										+ nameString.substring(nameString
												.indexOf("-") + 1);
								nameString = nameString.substring(0,
										nameString.length() - 1);
								map.put("itemsTitle", nameString + "</font>");
								if ((myNum - curNum) == 0) {
									map.put("itemsText",
											"<font color=\"red\">即将到站</font>， 下辆 <font color=\"black\">"
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
								map.put("busline", curbusline);
								rtbusListData2.add(map);
							} else {
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("itemsIcon", R.drawable.bus_img);
								map.put("bus_title",
										curbusline.get(2).substring(0,
												curbusline.get(2).indexOf("(")));
								map.put("station_title", curbusline.get(3));
								String nameString = curbusline.get(2);
								nameString = "<font color=\"white\" background-color=\"red\">开往</font><font color=\"yellow\">"
										+ nameString.substring(nameString
												.indexOf("-") + 1);
								nameString = nameString.substring(0,
										nameString.length() - 1);
								map.put("itemsTitle", nameString);
								if ((myNum - curNum) == 0) {
									map.put("itemsText",
											"<font color=\"red\">即将到站</font> ");

								} else {
									map.put("itemsText",
											"<font color=\"black\">"
													+ (myNum - curNum)
													+ "</font> 站, "
													+ (int) (distanceTotal / 100)
													/ 10.0 + " km");
								}

								map.put("distance", mindistance + "");
								map.put("busline", curbusline);
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
						map.put("itemsText", "敬请关注");
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

	protected class MyGridAdapter extends BaseAdapter {

		public ArrayList<ArrayList<String>> stations;
		public int type;

		public MyGridAdapter(ArrayList<ArrayList<String>> stations, int type) {
			// TODO Auto-generated constructor stub
			this.stations = stations;
			this.type = type;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (this.type > 1) {
				return stations.size();
			}
			return stations.size() + 1;
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
			View view = View.inflate(NewRootView.this, R.layout.bus_grid_item,
					null);
			TextView title = (TextView) view.findViewById(R.id.tv_title);
			ImageView img = (ImageView) view.findViewById(R.id.img_name);
			img.setVisibility(View.INVISIBLE);
			if (position == stations.size()) {
				title.setText("");
				title.setBackgroundResource(R.drawable.searchmore);
			} else if (type % 2 == 0) {
				String type = stations.get(position).get(3);
				if (type.equalsIgnoreCase("search")) {
					title.setBackgroundColor(0xffff9234);
				} else if (type.equalsIgnoreCase("nearby")) {
					title.setBackgroundColor(0xff00b2ae);
				} else if (type.equalsIgnoreCase("fav")) {
					img.setVisibility(View.VISIBLE);
					title.setBackgroundColor(0xffff9234);
					img.setImageResource(R.drawable.favimgbg);
					;
				} else if (type.equalsIgnoreCase("alert1")) {
					img.setVisibility(View.VISIBLE);
					title.setBackgroundColor(0xffff9234);
					img.setImageResource(R.drawable.alertimgbg);
				} else if (type.equalsIgnoreCase("alert2")) {
					title.setBackgroundColor(0xff00b2ae);
					img.setVisibility(View.VISIBLE);
					img.setImageResource(R.drawable.alertimgbg);
				}
				title.setText(stations.get(position).get(0));
			} else if (type % 2 == 1) {
				String type = stations.get(position).get(3);
				if (type.equalsIgnoreCase("search")) {
					title.setBackgroundColor(0xffff9234);
				}
				if (type.equalsIgnoreCase("fav")) {
					img.setVisibility(View.VISIBLE);
					title.setBackgroundColor(0xffff9234);
					img.setImageResource(R.drawable.favimgbg);
					;
				} else if (type.equalsIgnoreCase("nearby")) {
					title.setBackgroundColor(0xff00b2ae);
				}

				title.setText(stations.get(position).get(1));
			}
			return view;
		}

	}

	@Override
	public void onPause() {
		super.onPause();

		MobclickAgent.onPageEnd("SplashScreen"); // ��֤ onPageEnd ��onPause
		MobclickAgent.onPause(this);
		task2.cancel();
		timer2.cancel();
		task3.cancel();
		timer3.cancel();
		unregisterReceiver(mWifiReceiver);
		unregisterReceiver(mActivityReceiver);

	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onRestart() {
		super.onRestart();

		getFavStationsAndBuslines();

		task2 = new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						if (isNetworkConnected(NewRootView.this)) {
							GetAllBuses();
						}

					}
				});
			}
		};
		timer2 = new Timer();

		task3 = new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						getNearbyStationsAndBuslines();
					}
				});
			}
		};

		timer3 = new Timer();
		timer3.schedule(task3, 5000 * 60, 5000 * 60);
		SharedPreferences mySharedPreferences = getSharedPreferences("setting",
				SettingView.MODE_PRIVATE);
		String timeString = mySharedPreferences.getString("refreshMode", "30秒");
		timeString = timeString.substring(0, timeString.length() - 1);
		timer2.schedule(task2, 1000, Integer.parseInt(timeString) * 1000);

	}

	*//**
	 * * 实现 ActionBar.OnNavigationListener接口
	 *//*
	class DropDownListenser implements OnNavigationListener {
		// 得到和SpinnerAdapter里一致的字符数组
		String[] listNames = getResources().getStringArray(R.array.function);

		 当选择下拉菜单项的时候，将Activity中的内容置换为对应的Fragment 
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			// 生成自定的Fragment

			return true;
		}
	}
}
*/