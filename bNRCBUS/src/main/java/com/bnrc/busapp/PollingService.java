package com.bnrc.busapp;

import java.util.ArrayList;

import com.baidu.mapapi.model.LatLng;
import com.bnrc.ui.rjz.MainActivity;
import com.bnrc.util.DataBaseHelper;
import com.bnrc.util.LocationDetectService;
import com.bnrc.util.LocationUtil;
import com.bnrc.util.UserDataDBHelper;

import android.R.bool;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PollingService extends Service {
	private final String TAG = PollingService.class.getSimpleName();
	public static final String ACTION = "com.bnrc.busapp.PollingService";
	public static boolean hasAlert;
	public static boolean hasKnown;
	public static String stationNameString = "";
	public static int alertR = 0;
	public static double preDistance;
	public UserDataDBHelper userdabase = null;
	public static LocationUtil locationer;

	private Notification mNotification;
	private NotificationManager mManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		initNotifiManager();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		new PollingThread().start();
		userdabase = UserDataDBHelper.getInstance(getApplicationContext());
		locationer = LocationUtil.getInstance(PollingService.this);

		SharedPreferences mySharedPreferences = getApplicationContext()
				.getSharedPreferences("setting", Context.MODE_PRIVATE);
		String alertRMode = mySharedPreferences.getString("alertRMode", "200米");
		alertR = Integer.parseInt(alertRMode.subSequence(0,
				alertRMode.length() - 1).toString());

	}

	private void initNotifiManager() {
		mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_launcher;
		mNotification = new Notification();
		mNotification.icon = icon;
		mNotification.tickerText = "下车提醒";
		mNotification.defaults |= Notification.DEFAULT_SOUND;
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
	}

	private void showNotificationWithStopNameAndDistance(String stopName,
			int distance) {
		mNotification.when = System.currentTimeMillis();
		// Navigator to the new activity when click the notification title
		Intent i = new Intent(this, MainActivity.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		mNotification.setLatestEventInfo(this,
				getResources().getString(R.string.app_name), "即将到站：" + stopName
						+ "距离您" + distance + "米", pendingIntent);
		mManager.notify(0, mNotification);
	}

	/**
	 * Polling thread ģ����Server��ѯ���첽�߳�
	 * 
	 * @Author Ryan
	 * @Create 2013-7-13 ����10:18:34
	 */
	static int count = 0;

	class PollingThread extends Thread {
		@Override
		public void run() {

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("Service:onDestroy");
	}

}
