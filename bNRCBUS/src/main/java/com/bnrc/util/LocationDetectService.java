package com.bnrc.util;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bnrc.busapp.BuslineListView;
import com.bnrc.busapp.MyAlertStationView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;

public class LocationDetectService extends Service {
	public static LocationDetectService mInstance = null;
	public UserDataDBHelper dabase = null;
	public boolean detectAlertOn = true;
	public Timer timer = null;
	public LocationUtil mApplication = null;
	public AlertDialog mDialog = null;
	public LatLng preAlertPoint = null;
	public static int alertTime = 0;
	public static int maxAlertTime = 6;

	public static LocationDetectService getInstance() {
		if (mInstance == null) {
			mInstance = new LocationDetectService();
		}
		return mInstance;
	}

	@Override
	// Serviceʱ������
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());
		timer = new Timer(true);
		mApplication = LocationUtil.getInstance(LocationDetectService.this);
		dabase = UserDataDBHelper.getInstance(this.getApplicationContext());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	// ��������ʹ��startService()��������Serviceʱ���÷���������
	public void onStart(Intent intent, int startId) {
		TimerTask task = new TimerTask() {
			public void run() {
				dabase.getAlertStations();
				detectAlertOn = true;
				if (dabase.alertStations.size() != 0) {
					LatLng mypoint = new LatLng(LocationUtil
							.getInstance(getApplicationContext())
							.getmLocation().getLatitude(), LocationUtil
							.getInstance(getApplicationContext())
							.getmLocation().getLongitude());
					ArrayList<String> tempStation = dabase.alertStations.get(0);
					LatLng stationPoint = new LatLng(
							Float.parseFloat(tempStation.get(1)),
							Float.parseFloat(tempStation.get(2)));
					double distance = DistanceUtil.getDistance(mypoint,
							stationPoint);

					if (distance < 1000) {
						// �����޸ĸ�����վ�Ŀ���״̬��
						detectAlertOn = false;
						Vibrator mVibrator;
						if (alertTime == 0) {
							alertTime++;
							preAlertPoint = stationPoint;
							mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
							mVibrator.vibrate(1000);

						} else if (preAlertPoint.latitude == stationPoint.latitude
								&& preAlertPoint.longitude == stationPoint.longitude) {
							if (alertTime < maxAlertTime) {
								mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
								mVibrator.vibrate(1000);
								preAlertPoint = stationPoint;
								alertTime++;
							} else {
								//
							}
						}
					}
				}
			}
		};
		timer.scheduleAtFixedRate(task, 5000, 10000);// ��ʱ1000ms��ִ�У�1000msִ��һ��
		// timer.cancel(); //
	}

	@Override
	// ��Service����ʹ��ʱ����
	public void onDestroy() {
		super.onDestroy();
		timer.cancel(); // �˳���ʱ��
	}

	@Override
	// ��ʹ��startService()��������Serviceʱ����������ֻ��дreturn null
	public IBinder onBind(Intent intent) {
		return null;
	}

}
