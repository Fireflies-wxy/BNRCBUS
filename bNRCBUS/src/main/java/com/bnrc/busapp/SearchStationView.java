package com.bnrc.busapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import u.aly.bu;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.domob.android.ads.AdEventListener;
import cn.domob.android.ads.AdView;
import cn.domob.android.ads.AdManager.ErrorCode;

import com.bnrc.ui.rtBus.Group;
import com.bnrc.util.AnimationUtil;
import com.bnrc.util.DataBaseHelper;
import com.bnrc.util.LocationUtil;
import com.bnrc.util.MostSimilarString;
import com.bnrc.util.PCDataBaseHelper;
import com.bnrc.util.PCUserDataDBHelper;
import com.bnrc.util.UserDataDBHelper;
import com.bnrc.util.collectwifi.BaseActivity;
import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bnrc.activity.AboutActivity;
import com.bnrc.busapp.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;

public class SearchStationView extends BaseActivity {
	private ImageButton mReLocate;
	public MapView mMapView;
	public PCDataBaseHelper mPcDataBaseHelper = null;
	public PCUserDataDBHelper mPcUserDataDBHelper = null;
	public BaiduMap mBaiduMap = null;
	public LatLng myPoint = null;
	public LocationUtil mLocationUtil = null;
	private BDLocation mBDLocation = null;
	private LatLng startLng;
	private LatLng endLng;
	List<Overlay> overList = new ArrayList<Overlay>();
	private AbTitleBar mAbTitleBar;
	private List<Group> stations;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setAbContentView(R.layout.search_station_view);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("附近站点");
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleBarGravity(Gravity.CENTER, Gravity.CENTER);
		initbase();
		mLocationUtil = LocationUtil.getInstance(SearchStationView.this);
		// 加载地图和定位
		mReLocate = (ImageButton) findViewById(R.id.iv_reLocate);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启交通图
		mBaiduMap.setTrafficEnabled(true);
		mPcDataBaseHelper = PCDataBaseHelper.getInstance(this
				.getApplicationContext());

		getAroundStation();
		mReLocate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getAroundStation();
			}
		});
		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

			@Override
			public void onMapStatusChangeStart(MapStatus arg0) {
				// TODO Auto-generated method stub
				startLng = arg0.target;
			}

			@Override
			public void onMapStatusChangeFinish(MapStatus arg0) {
				// TODO Auto-generated method stub
				try {
					endLng = arg0.target;
					if (startLng.latitude != endLng.latitude
							|| startLng.longitude != endLng.longitude) {
						searchStations(arg0.target);
						MapStatusUpdate u = MapStatusUpdateFactory
								.zoomTo(17.0f);
						mBaiduMap.animateMapStatus(u);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			@Override
			public void onMapStatusChange(MapStatus arg0) {
				// TODO Auto-generated method stub

			}
		});
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public boolean onMarkerClick(final Marker arg0) {
				// TODO Auto-generated method stub
				// 创建InfoWindow展示的view

				// MapStatusUpdate u = MapStatusUpdateFactory
				// .zoomTo(20.0f);
				// mBaiduMap.animateMapStatus(u);
				// u = MapStatusUpdateFactory.newLatLng(pt);
				// mBaiduMap.animateMapStatus(u);

				// double distance = DistanceUtil.getDistance(myPoint,
				// arg0.getPosition());
				// button.setBackgroundResource(R.drawable.popup);
				if (arg0.getTitle().equalsIgnoreCase("我的位置")) {
					// Button button = new Button(getApplicationContext());
					// button.setTextSize(17);
					// button.setEnabled(true);
					// button.setText(arg0.getTitle() + "\n"
					// + mApplication.addressString);
					// // 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
					// InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
					// // 显示InfoWindow
					// mBaiduMap.showInfoWindow(mInfoWindow);
				} else {
					//  创建InfoWindow展示的view     
					// View popup = View.inflate(SearchStationView.this,
					// R.layout.station_pop, null);

					// InfoWindow mInfoWindow = new InfoWindow(popup, pt, -47);
					// // 显示InfoWindow
					// mBaiduMap.showInfoWindow(mInfoWindow);
					// BitmapDescriptor bitmap2 = BitmapDescriptorFactory
					// .fromResource(R.drawable.bus_img2);
					// arg0.setIcon(bitmap2);
					if (stations == null || stations.size() <= 0)
						return false;
					Intent intent = new Intent(SearchStationView.this,
							StationListView.class);
					intent.putExtra("StationName", arg0.getTitle());
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					AnimationUtil.activityZoomAnimation(SearchStationView.this);
				}

				return false;
			}
		});
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				mBaiduMap.hideInfoWindow();
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				mBaiduMap.hideInfoWindow();
			}
		});
		mMapView.removeViewAt(2);
	}

	private void searchStations(LatLng newPos) {
		mBaiduMap.clear();
		overList.clear();

		try {
			stations = mPcDataBaseHelper
					.acquireAroundStationsWithLocation(newPos);
			Toast.makeText(getApplicationContext(),
					"在附近查找到" + stations.size() + "个站点", Toast.LENGTH_SHORT)
					.show();
			for (Group station : stations) {
				// 定义Maker坐标点
				double Latitude = station.getLatitide();
				double Longitude = station.getLongitude();
				LatLng stationPoint = new LatLng(Latitude, Longitude);
				View view = LayoutInflater.from(SearchStationView.this)
						.inflate(R.layout.map_station_popview, null);
				TextView tv_stationName = (TextView) view
						.findViewById(R.id.tv_stationName);
				tv_stationName.setText(station.getStationName());
				BitmapDescriptor bitmap = BitmapDescriptorFactory
						.fromView(view);
				// // 构建Marker图标
				// BitmapDescriptor bitmap = BitmapDescriptorFactory
				// .fromResource(R.drawable.bus_img2);
				// 构建MarkerOption，用于在地图上添加Marker
				MarkerOptions option2 = new MarkerOptions()
						.position(stationPoint).icon(bitmap).zIndex(9) // 设置marker所在层级
						.draggable(true).title(station.getStationName()); // 设置手势拖拽;
				option2.animateType(MarkerAnimateType.grow);

				overList.add(mBaiduMap.addOverlay(option2));

			}
		} catch (SQLException sqle) {
			throw sqle;
		}

	}

	private void getAroundStation() {
		mBaiduMap.clear();
		overList.clear();
		mBDLocation = mLocationUtil.getmLocation();
		if (mBDLocation != null) {
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(mBDLocation.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(mBDLocation.getLatitude())
					.longitude(mBDLocation.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			myPoint = new LatLng(mBDLocation.getLatitude(),
					mBDLocation.getLongitude());

			// 开启定位图层
			mBaiduMap.setMyLocationEnabled(true);

			// // 构建Marker图标
			// BitmapDescriptor bitmap =
			// BitmapDescriptorFactory.fromResource(R.drawable.nearby_station);
			// // 构建MarkerOption，用于在地图上添加Marker
			// OverlayOptions option = new
			// MarkerOptions().position(mpoint).icon(bitmap).zIndex(9) //
			// 设置marker所在层级
			// .draggable(true).title("我的位置"); // 设置手势拖拽;
			// // 在地图上添加Marker，并显示
			// // mBaiduMap.addOverlay(option);
			// // 调用BaiduMap对象的setOnMarkerDragListener方法设置marker拖拽的监听
			// MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(17.0f);
			// mBaiduMap.animateMapStatus(u);
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(myPoint,
					17.0f);
			mBaiduMap.animateMapStatus(u);
		}
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.NORMAL, true, null));
		// // 定义文字所显示的坐标点
		// LatLng llText = new LatLng(mpoint.latitude, mpoint.longitude);
		// // 构建文字Option对象，用于在地图上添加文字
		// OverlayOptions textOption = new
		// TextOptions().bgColor(0xAAFFFF00).fontSize(36).fontColor(0xFFFF00FF)
		// .text(mApplication.addressString).rotate(0).position(llText);
		// // 在地图上添加该文字对象并显示
		// // mBaiduMap.addOverlay(textOption);

		try {
			stations = mPcDataBaseHelper
					.acquireAroundStationsWithLocation(myPoint);

			Toast.makeText(getApplicationContext(),
					"在附近查找到" + stations.size() + "个站点", Toast.LENGTH_SHORT)
					.show();
			for (Group station : stations) {
				// 定义Maker坐标点
				double Latitude = station.getLatitide();
				double Longitude = station.getLongitude();
				LatLng stationPoint = new LatLng(Latitude, Longitude);

				// 构建Marker图标
				// BitmapDescriptor bitmap = BitmapDescriptorFactory
				// .fromResource(R.drawable.bus_img2);
				View view = LayoutInflater.from(SearchStationView.this)
						.inflate(R.layout.map_station_popview, null);
				TextView tv_stationName = (TextView) view
						.findViewById(R.id.tv_stationName);
				tv_stationName.setText(station.getStationName());
				BitmapDescriptor bitmap = BitmapDescriptorFactory
						.fromView(view);
				// BitmapDescriptor bitmap = BitmapDescriptorFactory
				// .fromResource(R.drawable.icon_lt_hotel);
				// 构建MarkerOption，用于在地图上添加Marker
				MarkerOptions option2 = new MarkerOptions()
						.position(stationPoint).icon(bitmap).zIndex(9) // 设置marker所在层级
						.draggable(true).title(station.getStationName()); // 设置手势拖拽;
				// option2.animateType(MarkerAnimateType.drop);
				// // 在地图上添加Marker，并显示
				// View popup = View.inflate(SearchStationView.this,
				// R.layout.station_pop, null);
				// InfoWindow mInfoWindow = new InfoWindow(popup, point, -47);
				// // 显示InfoWindow
				// mBaiduMap.showInfoWindow(mInfoWindow);
				overList.add(mBaiduMap.addOverlay(option2));

			}
			// BitmapDescriptor bitmap2 =
			// BitmapDescriptorFactory.fromResource(R.drawable.icon_count);
			// ((Marker) overList.get(3)).setIcon(bitmap2);
		} catch (SQLException sqle) {
			throw sqle;
		}

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
		mMapView.onResume();
//		registerReceiver(mWifiReceiver, wifiFilter);
//		registerReceiver(mActivityReceiver, activityFilter);

	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // 保证 onPageEnd 在onPause
													// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
		mMapView.onResume();
//		unregisterReceiver(mWifiReceiver);
//		unregisterReceiver(mActivityReceiver);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}
}
