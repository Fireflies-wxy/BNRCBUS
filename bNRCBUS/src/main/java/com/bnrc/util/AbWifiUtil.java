package com.bnrc.util;

import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class AbWifiUtil {
	
	
	/**
	 * 
	 * 鎻忚堪锛氭墦寮�wifi.
	 * @param context
	 * @param enabled
	 * @return
	 */
	public static void setWifiEnabled(Context context,boolean enabled){
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if(enabled){
			wifiManager.setWifiEnabled(true);
		}else{
			wifiManager.setWifiEnabled(false);
		}
	}
	
	/**
	 * 
	 * 鎻忚堪锛氭槸鍚︽湁缃戠粶杩炴帴.
	 * @param context
	 * @return
	 */
	public static boolean isConnectivity(Context context) {
		
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((connectivityManager.getActiveNetworkInfo() != null && connectivityManager
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || telephonyManager
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}
	
	/**
	 * 鍒ゆ柇褰撳墠缃戠粶鏄惁鏄痺ifi缃戠粶.
	 *
	 * @param context the context
	 * @return boolean
	 */
	public static boolean isWifiConnectivity(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 鎻忚堪锛氬緱鍒版墍鏈夌殑WiFi鍒楄〃.
	 * @param context
	 * @return
	 */
	public static List<ScanResult> getScanResults(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		List<ScanResult> list = null;
		//寮�濮嬫壂鎻廤iFi
		boolean f = wifiManager.startScan();
		if(!f){
			getScanResults(context);
		}else{
			// 寰楀埌鎵弿缁撴灉
			list = wifiManager.getScanResults();
		}
		
		return list;
	}

	/**
	 * 
	 * 鎻忚堪锛氭牴鎹甋SID杩囨护鎵弿缁撴灉.
	 * @param context
	 * @param bssid
	 * @return
	 */
	public static ScanResult getScanResultsByBSSID(Context context,String bssid) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		ScanResult scanResult = null;
		//寮�濮嬫壂鎻廤iFi
		boolean f = wifiManager.startScan();
		if(!f){
			getScanResultsByBSSID(context,bssid);
		}
		// 寰楀埌鎵弿缁撴灉
		List<ScanResult> list = wifiManager.getScanResults();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				// 寰楀埌鎵弿缁撴灉
				scanResult = list.get(i);
				if (scanResult.BSSID.equals(bssid)) {
					break;
				}
			}
		}
		return scanResult;
	}

	/**
	 * 
	 * 鎻忚堪锛氳幏鍙栬繛鎺ョ殑WIFI淇℃伅.
	 * @param context
	 * @return
	 */
	public static WifiInfo getConnectionInfo(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		return wifiInfo;
	}
	
	
}
