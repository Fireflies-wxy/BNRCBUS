package com.bnrc.util.collectwifi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class ConfigCheck {
	private Context context;

	public ConfigCheck(Context context) {
		this.context = context;
	}

	// 是否有可用网络
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = cm.getActiveNetworkInfo();
		if (network != null) {
			return network.isAvailable();
		}
		return false;
	}

	// Gps是否可用
	public boolean isGpsEnable() {
		LocationManager locationManager = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	// 检测Wifi有没有打开
	public boolean isWifiEnable() {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		return wifiManager.isWifiEnabled();
	}

	public boolean ping(Handler handler) {

		String result = null;
		try {
			Thread.sleep(5000);
			String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
			Process p = Runtime.getRuntime()
					.exec("/system/bin/ping -c 1 " + ip);// ping网址3次
			// 读取ping的内容，可以不加
			InputStream input = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer stringBuffer = new StringBuffer();
			String content = "";
			while ((content = in.readLine()) != null) {
				stringBuffer.append(content);
			}
			Log.d("------ping-----",
					"result content : " + stringBuffer.toString());
			// ping的状态
			int status = p.waitFor();
			if (status == 0) {
				result = "success";
				Toast.makeText(context, "连接服务器线程开始", 1000).show();
//				new Thread_send_json(context, handler).start();
				return true;
			} else {
				result = "failed";
			}
		} catch (IOException e) {
			result = "IOException";
		} catch (InterruptedException e) {
			result = "InterruptedException";
		} finally {
			Log.d("----result---", "result = " + result);
		}
		return false;

	}

	// 强制打开GPS
	public static final void openGPS(Context context) {
		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}

	public void turnGPSOn() {
		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", true);
		context.sendBroadcast(intent);

		String provider = Settings.Secure.getString(
				context.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (!provider.contains("gps")) { // if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			context.sendBroadcast(poke);
		}
	}
}
