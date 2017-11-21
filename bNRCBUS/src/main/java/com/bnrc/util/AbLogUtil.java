/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bnrc.util;

import java.util.Calendar;

import android.content.Context;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bLogUtil.java 
 * 鎻忚堪锛氭棩蹇楀伐鍏风被.
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2014-06-26 涓嬪崍11:52:13
 */
public class AbLogUtil {
	
    /** debug寮�鍏�. */
	public static boolean D = true;
	
	/** info寮�鍏�. */
	public static boolean I = true;
	
	/** error寮�鍏�. */
	public static boolean E = true;
	
	/** 璧峰鎵ц鏃堕棿. */
	public static long startLogTimeInMillis = 0;

	/**
	 * debug鏃ュ織
	 * @param tag
	 * @param message
	 */
	public static void d(String tag,String message) {
		if(D) Log.d(tag, message);
	}
	
	/**
	 * debug鏃ュ織
	 * @param context
	 * @param message
	 */
	public static void d(Context context,String message) {
		String tag = context.getClass().getSimpleName();
		d(tag, message);
	}
	
	/**
	 * debug鏃ュ織
	 * @param clazz
	 * @param message
	 */
	public static void d(Class<?> clazz,String message) {
		String tag = clazz.getSimpleName();
		d(tag, message);
	}
	
	/**
	 * info鏃ュ織
	 * @param tag
	 * @param message
	 */
	public static void i(String tag,String message) {
		Log.i(tag, message);
	}
	
	/**
	 * info鏃ュ織
	 * @param context
	 * @param message
	 */
	public static void i(Context context,String message) {
		String tag = context.getClass().getSimpleName();
		i(tag, message);
	}
	
	/**
	 * info鏃ュ織
	 * @param clazz
	 * @param message
	 */
	public static void i(Class<?> clazz,String message) {
		String tag = clazz.getSimpleName();
		i(tag, message);
	}
	
	
	
	/**
	 * error鏃ュ織
	 * @param tag
	 * @param message
	 */
	public static void e(String tag,String message) {
		Log.e(tag, message);
	}
	
	/**
	 * error鏃ュ織
	 * @param context
	 * @param message
	 */
	public static void e(Context context,String message) {
		String tag = context.getClass().getSimpleName();
		e(tag, message);
	}
	
	/**
	 * error鏃ュ織
	 * @param clazz
	 * @param message
	 */
	public static void e(Class<?> clazz,String message) {
		String tag = clazz.getSimpleName();
		e(tag, message);
	}
	
	/**
	 * 鎻忚堪锛氳褰曞綋鍓嶆椂闂存绉�.
	 * 
	 */
	public static void prepareLog(String tag) {
		Calendar current = Calendar.getInstance();
		startLogTimeInMillis = current.getTimeInMillis();
		Log.d(tag,"鏃ュ織璁℃椂寮�濮嬶細"+startLogTimeInMillis);
	}
	
	/**
	 * 鎻忚堪锛氳褰曞綋鍓嶆椂闂存绉�.
	 * 
	 */
	public static void prepareLog(Context context) {
		String tag = context.getClass().getSimpleName();
		prepareLog(tag);
	}
	
	/**
	 * 鎻忚堪锛氳褰曞綋鍓嶆椂闂存绉�.
	 * 
	 */
	public static void prepareLog(Class<?> clazz) {
		String tag = clazz.getSimpleName();
		prepareLog(tag);
	}
	
	/**
	 * 鎻忚堪锛氭墦鍗拌繖娆＄殑鎵ц鏃堕棿姣锛岄渶瑕侀鍏堣皟鐢╬repareLog().
	 *
	 * @param tag 鏍囪
	 * @param message 鎻忚堪
	 * @param printTime 鏄惁鎵撳嵃鏃堕棿
	 */
	public static void d(String tag, String message,boolean printTime) {
		Calendar current = Calendar.getInstance();
		long endLogTimeInMillis = current.getTimeInMillis();
		Log.d(tag,message+":"+(endLogTimeInMillis-startLogTimeInMillis)+"ms");
	}
	
	
	/**
	 * 鎻忚堪锛氭墦鍗拌繖娆＄殑鎵ц鏃堕棿姣锛岄渶瑕侀鍏堣皟鐢╬repareLog().
	 *
	 * @param tag 鏍囪
	 * @param message 鎻忚堪
	 * @param printTime 鏄惁鎵撳嵃鏃堕棿
	 */
	public static void d(Context context,String message,boolean printTime) {
		String tag = context.getClass().getSimpleName();
	    d(tag,message,printTime);
	}
	
	/**
	 * 鎻忚堪锛氭墦鍗拌繖娆＄殑鎵ц鏃堕棿姣锛岄渶瑕侀鍏堣皟鐢╬repareLog().
	 *
	 * @param clazz 鏍囪
	 * @param message 鎻忚堪
	 * @param printTime 鏄惁鎵撳嵃鏃堕棿
	 */
	public static void d(Class<?> clazz,String message,boolean printTime) {
		String tag = clazz.getSimpleName();
		d(tag,message,printTime);
	}

	/**
	 * debug鏃ュ織鐨勫紑鍏�
	 * @param d
	 */
	public static void debug(boolean d) {
		D  = d;
	}
	
	/**
	 * info鏃ュ織鐨勫紑鍏�
	 * @param i
	 */
	public static void info(boolean i) {
		I  = i;
	}
	
	/**
	 * error鏃ュ織鐨勫紑鍏�
	 * @param e
	 */
	public static void error(boolean e) {
		E  = e;
	}
	
	/**
	 * 璁剧疆鏃ュ織鐨勫紑鍏�
	 * @param e
	 */
	public static void setVerbose(boolean d,boolean i,boolean e) {
		D  = d;
		I  = i;
		E  = e;
	}
	
	/**
	 * 鎵撳紑鎵�鏈夋棩蹇楋紝榛樿鍏ㄦ墦寮�
	 * @param d
	 */
	public static void openAll() {
		D  = true;
		I  = true;
		E  = true;
	}
	
	/**
	 * 鍏抽棴鎵�鏈夋棩蹇�
	 * @param d
	 */
	public static void closeAll() {
		D  = false;
		I  = false;
		E  = false;
	}


}
