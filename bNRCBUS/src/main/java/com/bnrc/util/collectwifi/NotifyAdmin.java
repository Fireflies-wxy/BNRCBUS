package com.bnrc.util.collectwifi;



import com.bnrc.busapp.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotifyAdmin {
	private static final String TAG = "Notify";
	static final int NOTIFICATION_ID = 0x1123;
	private Context mContext;
	private Notification mNotification;
	private NotificationManager mManager;
	public static boolean IsShow = false;
	private String sureMac = "";

	public NotifyAdmin(Context pContext, String pSureMac) {
		this.mContext = pContext;
		this.sureMac = pSureMac;
		mManager = (NotificationManager) pContext
				.getSystemService(pContext.NOTIFICATION_SERVICE);
		initNotifiManager();

	}

	public void cancelNotify() {
		mManager.cancel(NOTIFICATION_ID);
	}

	// 初始化通知栏配置
	private void initNotifiManager() {
		mManager = (NotificationManager) mContext
				.getSystemService(mContext.NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_launcher;
		mNotification = new Notification();
		mNotification.icon = icon;
		mNotification.tickerText = "已扫描到公交热点";
		mNotification.defaults |= Notification.DEFAULT_SOUND;
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
	}

	// 弹出Notification
	public void showNotification() {
		Log.d(TAG, "showNotification()...");
		mNotification.when = System.currentTimeMillis();
		// Navigator to the new activity when click the notification title
		Intent notificationIntent = new Intent(mContext,
				MyDialogSelectBuslineActivity.class);
		notificationIntent.putExtra("sureMac", sureMac);
		Log.i(TAG, this.toString());
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification.setLatestEventInfo(mContext, "请选择您乘坐的公交线路~", null,
				pendingIntent);
		mManager.notify(0, mNotification);
		IsShow = true;
	}
}
