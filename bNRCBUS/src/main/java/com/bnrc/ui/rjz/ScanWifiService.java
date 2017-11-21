package com.bnrc.ui.rjz;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bnrc.busapp.R;
import com.bnrc.ui.rjz.AlertService.AlertBinder;
import com.bnrc.ui.rjz.AlertService.onAlertListener;
import com.bnrc.ui.rtBus.Child;
import com.bnrc.util.LocationUtil;
import com.bnrc.util.PCUserDataDBHelper;
import com.bnrc.util.SharePrefrenceUtil;
import com.bnrc.util.collectwifi.CollectWifiDBHelper;
import com.bnrc.util.collectwifi.CollectWifiDBHelper.UploadDatabaseListener;
import com.bnrc.util.collectwifi.Constants;
import com.bnrc.util.collectwifi.ScanService;
import com.bnrc.util.collectwifi.WifiAdmin;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
//in use
public class ScanWifiService extends Service {
	private static final String TAG = ScanWifiService.class.getSimpleName();
	private CollectWifiDBHelper mCollectWifiDBHelper = null;
	private List<ScanResult> wifiScanResults;
	private LocationUtil mLocationUtil;
	private WifiAdmin mWifiAdminInstance;
	private Map<String, Object> mScanWifiInfo;
	private List<String> wifiInfo;
	private String wifiSSID = "";
	private BDLocation mBDLocation;
	private ScanServiceBinder mBinder = new ScanServiceBinder();
	private TimerTask mWifiTask, mPostTask;
	private Timer mWifiTimer, mPostTimer;
	private Handler mHandler = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onBind");

		return mBinder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mCollectWifiDBHelper = CollectWifiDBHelper
				.getInstance(getApplicationContext());
		mLocationUtil = LocationUtil.getInstance(getApplicationContext());
		mBDLocation = mLocationUtil.getmLocation();
		mWifiAdminInstance = WifiAdmin.getInstance(getApplicationContext());
		mScanWifiInfo = new HashMap<String, Object>();
		wifiInfo = new ArrayList<String>();
		Log.i(TAG, "onCreate");

	}

	private void openWifiTimertask() {
		mWifiTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					scanProcess();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		};
		mWifiTimer = new Timer(true);
		mWifiTimer.schedule(mWifiTask, 0, 5 * 1000);
	}

	private void openPostTimertask() {

		mPostTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				postDatabase();
			}
		};
		mPostTimer = new Timer(true);
		mPostTimer.schedule(mPostTask, 6 * 10 * 1000, 6 * 10 * 1000);
	}

	private void postDatabase() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					closeWifiTimertask();
					mCollectWifiDBHelper.postFile("collectdata.db",
							new UploadDatabaseListener() {

								@Override
								public void onFail() {
									// TODO Auto-generated method stub
									openWifiTimertask();
								}

								@Override
								public void onComplete() {
									// TODO Auto-generated method stub
									openWifiTimertask();

								}
							});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void closeWifiTimertask() {
		if (mWifiTask != null)
			mWifiTask.cancel();
		if (mWifiTimer != null)
			mWifiTimer.cancel();
	}

	private void closePostTimertask() {
		if (mPostTask != null)
			mPostTask.cancel();
		if (mPostTimer != null)
			mPostTimer.cancel();
	}

	private void scanProcess() {
		wifiSSID = "16wifi";
//		wifiSSID = "BNRC-air";
		wifiScanResults = mWifiAdminInstance.getScanWifiMac(wifiSSID.trim());
		wifiInfo.clear();
		mBDLocation = mLocationUtil.getmLocation();
		if (wifiScanResults.size() != 0) {
			for (ScanResult scanResult : wifiScanResults) {
				if (mBDLocation == null)
					return;
				if (mScanWifiInfo == null)
					mScanWifiInfo = new HashMap<String, Object>();
				mScanWifiInfo.clear();
				mScanWifiInfo.put("DeviceID", mLocationUtil.getDeviceID());
				mScanWifiInfo.put("Time", System.currentTimeMillis());
				mScanWifiInfo.put("Latitude", mBDLocation.getLatitude());
				mScanWifiInfo.put("Longitude", mBDLocation.getLongitude());
				mScanWifiInfo.put("SSID", wifiSSID);
				mScanWifiInfo.put("MAC", scanResult.BSSID);
				mScanWifiInfo.put("Level", scanResult.level);
				// wifiInfo.add(scanResult.BSSID);
				// Log.i(TAG,
				// "mBDLocation.getLocType(): " + mBDLocation.getLocType());
				if (mBDLocation.getLocType() == 61
						|| mBDLocation.getLocType() == 161) {
					mScanWifiInfo.put("LocationType",
							getLocType(mBDLocation.getLocType()));
					mScanWifiInfo.put("LocationPrecision",
							mBDLocation.getRadius());
					mScanWifiInfo.put(
							"Speed",
							mBDLocation.hasSpeed() == true ? (int) mBDLocation
									.getSpeed() : -1);
				} else if (mBDLocation.getLocType() == 68) {
					mScanWifiInfo.put("LocationType",
							getLocType(mBDLocation.getLocType()));
					mScanWifiInfo.put("LocationPrecision",
							mBDLocation.getRadius());
					mScanWifiInfo.put("Speed", -1);
				} else {
					mScanWifiInfo.put("LocationType",
							getLocType(mBDLocation.getLocType()));
					mScanWifiInfo.put("LocationPrecision", -1);
					mScanWifiInfo.put("Speed", -1);
				}
				mCollectWifiDBHelper.InsertScanData(mScanWifiInfo);
			}

		}
	}

	private String getLocType(int type) {

		/*
		 * String LocType = ""; switch (type) { case 61: LocType = "GPS"; break;
		 * case 161: LocType = "Net"; break; case 68: LocType = "offline";
		 * break; default: LocType = "fail"; break; } return LocType;
		 */
		return type + "";
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "onDestroy");

	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onUnbind");
		closePostTimertask();
		closeWifiTimertask();
		return super.onUnbind(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	public class ScanServiceBinder extends Binder {
		public void startScanWifi() {
			Log.d(TAG, "startScanWifi() executed");
			openWifiTimertask();// 执行具体的任务
		}

		public void uploadWifiDatabase() {
			openPostTimertask();
		}

	}

}
