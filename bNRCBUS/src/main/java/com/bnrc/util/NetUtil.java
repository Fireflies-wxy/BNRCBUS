package com.bnrc.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

	/**
	 * 妫�鏌ョ綉缁滄槸鍚﹀彲鐢�
	 * @param context
	 * @return boolean
	 */
	public static boolean checkNetAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		} else {
			return networkInfo.isAvailable();
		}
	}
}
