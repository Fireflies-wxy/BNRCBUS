package com.bnrc.util.collectwifi;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;

public class WifiReceiver extends BroadcastReceiver {
	private static final String TAG = "WifiReceiver";
	private WifiAdmin mWifiAdminInstance = null;
	public static int IsScan = 0;// 0为初始值，1为开始扫描，2为停止扫描
	private SharedPreferences preferences;
	private ConfigCheck mConfigCheck;
	private Handler mHandler;

	public WifiReceiver(Handler handler) {
		this.mHandler = handler;
	}

	public void onReceive(final Context context, Intent intent) {
		if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 这个监听wifi的打开与关闭，与wifi的连接无关
			int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
			switch (wifiState) {
			case WifiManager.WIFI_STATE_ENABLED:
				Log.i(TAG, "WifiManager.WIFI_STATE_ENABLED");
				try {
					if (!ServiceUtils.isPollingStart()) {
						preferences = context.getSharedPreferences(
								Constants.SETTING, context.MODE_PRIVATE);
						if (preferences
								.getString(Constants.SETTING_MET, "手动收集公交信息")
								.trim().equalsIgnoreCase("自动收集公交信息")
								&& IsScan != 2) {

							ServiceUtils
									.startPollingService(
											context.getApplicationContext(),
											ScanService.class,
											Constants.SERVICE_ACTION);
						} else if (IsScan == 1)
							ServiceUtils
									.startPollingService(
											context.getApplicationContext(),
											ScanService.class,
											Constants.SERVICE_ACTION);
					}
					// mConfigCheck = new ConfigCheck(context);
					// mConfigCheck.ping(mHandler);
				} catch (NullPointerException e) {
					Log.i("NULLPOINTER", "WIFI_STATE_ENABLED " + e.toString());
				}
				break;
			case WifiManager.WIFI_STATE_ENABLING:
				Log.i(TAG, "WifiManager.WIFI_STATE_ENABLING");

				break;
			case WifiManager.WIFI_STATE_DISABLED:
				Log.i(TAG, "WifiManager.WIFI_STATE_DISABLED");

				break;
			case WifiManager.WIFI_STATE_DISABLING:
				Log.i(TAG, "WifiManager.WIFI_STATE_DISABLING");
				try {
					if (ServiceUtils.isPollingStart()) {
						ServiceUtils.stopPollingService(
								context.getApplicationContext(),
								ScanService.class, Constants.SERVICE_ACTION);
						IsScan = 2;
					}
				} catch (NullPointerException e) {
					Log.i("NULLPOINTER", "WIFI_STATE_DISABLING " + e.toString());
				}
				break;
			default:
				break;
			}

		}
	}
}
