package com.bnrc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bnrc.busapp.R;
import com.bnrc.busapp.SettingView;
import com.umeng.analytics.MobclickAgent;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("SdCardPath")
public class BuslineDBHelper extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private static BuslineDBHelper instance;
	public static String DB_PATH = "/data/data/com.bnrc.busapp/databases/";
	public static String DB_NAME = "buslinedata.db";
	public SQLiteDatabase myDataBase;
	public Context myContext;
	private int FileLength;
	private int DownedFileLength = 0;
	private InputStream inputStream;
	private URLConnection connection;
	private OutputStream outputStream;

	// private Handler handler = new Handler() {
	// public void handleMessage(Message msg) {
	// if (!Thread.currentThread().isInterrupted()) {
	// switch (msg.what) {
	// case 0:
	// // progressBar.setMax(FileLength);
	// Log.i("文件长度----------->", FileLength + "");
	// break;
	// case 1:
	// // progressBar.setProgress(DownedFileLength);
	// int x = DownedFileLength * 100 / FileLength;
	// Log.i("文件长度----------->", DownedFileLength + "");
	// // textView.setText(x+"%");
	// break;
	// case 2:
	// openDataBase();
	// MobclickAgent.updateOnlineConfig(myContext);
	// String value = MobclickAgent.getConfigParams(myContext,
	// "realtime_bus_data_version");
	// Log.i("realtime_bus_data_version", value);
	// JSONObject jsonObj = null;
	// try {
	// jsonObj = new JSONObject(value);
	// String version = jsonObj.getString("version");
	// SharedPreferences mySharedPreferences = myContext
	// .getSharedPreferences("setting",
	// SettingView.MODE_PRIVATE);
	// SharedPreferences.Editor editor = mySharedPreferences
	// .edit();
	// editor.putString("realtime_bus_data_version", version);
	// editor.commit();
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// Toast.makeText(myContext, "公交数据更新完成", Toast.LENGTH_LONG)
	// .show();
	// break;
	//
	// default:
	// break;
	// }
	// }
	// }
	//
	// };

	public static BuslineDBHelper getInstance(Context context) {
		if (instance == null) {
			try {
				DB_PATH = context.getFilesDir().getAbsolutePath();
				DB_PATH = DB_PATH.replace("files", "databases/");

				instance = new BuslineDBHelper(context);
				// Runtime.getRuntime().exec("chmod 666" +
				// "/data/data/test.txt");
				instance.myContext = context;
				instance.openDataBase();

			} catch (IOException ioe) {
				throw new Error("Unable to create database");
			}
		}
		return instance;
	}

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 * @throws IOException
	 */
	public BuslineDBHelper(Context context) throws IOException {
		super(context, DB_NAME, null, 1);

	}

	public void openDataBase() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		try {
			// ����ļ��ľ���·��
			String databaseFilename = myPath;
			File dir = new File(DB_PATH);

			if (!dir.exists()) {
				dir.mkdir();
			}
			;

			if (!(new File(databaseFilename)).exists()) {
				InputStream is = myContext.getResources().openRawResource(
						R.raw.rtdata);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				// ��ʼ�����ļ�
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}

			myDataBase = SQLiteDatabase.openOrCreateDatabase(databaseFilename,
					null);

		} catch (Exception e) {
			Log.i("open error", e.getMessage());
		}
	}

	@Override
	public synchronized void close() {

		if (getMyDataBase() != null)
			getMyDataBase().close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public SQLiteDatabase getMyDataBase() {
		return myDataBase;
	}

	public void setMyDataBase(SQLiteDatabase myDataBase) {
		this.myDataBase = myDataBase;
	}

	public int getBaseVersionData() {
		int version = 0;
		String sql = "select version from base_version_data";
		Cursor cursor = getMyDataBase().rawQuery(sql, null);

		while (cursor.moveToNext()) {
			version = cursor.getInt(0);
		}
		cursor.close();
		// close();
		return version;
	}

	public ArrayList<String> getBuslineInfoWithBuslineId(String buslineId) {
		ArrayList<String> buslineInfo = new ArrayList<String>();
		// openDataBase();
		String sql = "select line_name,line,type,line_id, version ,state from offline_data where line_id = "
				+ buslineId;
		Cursor cursor = getMyDataBase().rawQuery(sql, null);

		while (cursor.moveToNext()) {
			buslineInfo.add(cursor.getString(0));
			buslineInfo.add(cursor.getString(1));
			buslineInfo.add(cursor.getInt(2) + "");
			buslineInfo.add(cursor.getString(3));
			buslineInfo.add(cursor.getInt(4) + "");
			buslineInfo.add(cursor.getString(5));
			break;
		}
		cursor.close();
		return buslineInfo;
	}

	public int acquireOfflineIDWithLineID(int LineID) {
		String sql = "select MatchId where LineID = \'" + LineID + "\'";
		Log.i("NearFragment2", "sql： " + sql);
		Cursor cursor = getMyDataBase().rawQuery(sql, null);
		while (cursor.moveToNext()) {
			int OfflineID = cursor.getInt(cursor.getColumnIndex("MatchId"));
			if (OfflineID > 0)
				cursor.close();
			return OfflineID;
		}
		cursor.close();
		return 0;
	}

	/*
	 * public Map<String, Object> AcquireOffLineInfoWithBuslineName(String
	 * busline) { Map<String, Object> offlineParams = new HashMap<String,
	 * Object>(); // openDataBase(); Log.i("NearFragment2", busline); busline =
	 * busline.replace("路(", "(").trim(); Log.i("NearFragment2", busline);
	 * busline = busline.replace(" ", "").trim(); Log.i("NearFragment2",
	 * busline); String string = busline.substring(busline.indexOf("(") + 1);
	 * Log.i("NearFragment2", busline); String sql =
	 * "select line_name,line,type,line_id, version ,state from offline_data where line_name like \'"
	 * + busline.substring(0, busline.indexOf("(")) + "%" + string.substring(0,
	 * string.indexOf("-") + 1) + "%\'"; Log.i("NearFragment2", "sql： " + sql);
	 * 
	 * Cursor cursor = getMyDataBase().rawQuery(sql, null);
	 * 
	 * while (cursor.moveToNext()) { offlineParams.put("line_name",
	 * cursor.getString(cursor.getColumnIndex("line_name")));
	 * offlineParams.put("line",
	 * cursor.getString(cursor.getColumnIndex("line")));
	 * offlineParams.put("type", cursor.getInt(cursor.getColumnIndex("type")));
	 * offlineParams.put("line_id",
	 * cursor.getString(cursor.getColumnIndex("line_id")));
	 * offlineParams.put("version",
	 * cursor.getInt(cursor.getColumnIndex("version")));
	 * offlineParams.put("state",
	 * cursor.getString(cursor.getColumnIndex("state"))); // buslineInfo //
	 * .add(cursor.getString(cursor.getColumnIndex("line_name")));// //
	 * buslineName //
	 * buslineInfo.add(cursor.getString(cursor.getColumnIndex("line")));// // 没用
	 * // buslineInfo.add(cursor.getInt(cursor.getColumnIndex("type")) + //
	 * "");// type //
	 * buslineInfo.add(cursor.getString(cursor.getColumnIndex("line_id")));// //
	 * OffLine_ID //
	 * buslineInfo.add(cursor.getInt(cursor.getColumnIndex("version")) // +
	 * "");// version //
	 * buslineInfo.add(cursor.getString(cursor.getColumnIndex("state")));// //
	 * state break; } cursor.close(); if (offlineParams.size() > 0)
	 * Log.i("NearFragment2", "开通了： " +
	 * offlineParams.get("line_id").toString()); return offlineParams; }
	 */

	/*
	 * public Map<String, Object> AcquireOffLineInfoWithBuslineInfo( String
	 * LineName, String StartStation, String EndStation) { Map<String, Object>
	 * offlineParams = new HashMap<String, Object>(); // openDataBase(); //
	 * Log.i("NearFragment2", busline); LineName = LineName.replace("路",
	 * "").trim(); LineName = LineName.replace("线", "").trim();
	 * Log.i("NearFragment2", LineName); LineName = LineName.replace(" ",
	 * "").trim(); int LineNameLength = LineName.length(); if
	 * (LineName.indexOf("内") == LineNameLength - 1 || LineName.indexOf("外") ==
	 * LineNameLength - 1) LineName = LineName.substring(0, LineNameLength - 1);
	 * StartStation = StartStation.replace(" ", "") .substring(0,
	 * StartStation.length() - 1).trim(); EndStation = EndStation.replace(" ",
	 * "") .substring(0, EndStation.length() - 1).trim(); Log.i("NearFragment2",
	 * LineName);
	 * 
	 * String sql =
	 * "select line_name,line,type,line_id, version ,state from offline_data where line_name like \'"
	 * + LineName + "%" + StartStation + "%" + EndStation + "%\'";
	 * Log.i("NearFragment2", "sql： " + sql); Cursor cursor =
	 * getMyDataBase().rawQuery(sql, null); while (cursor.moveToFirst()) {
	 * offlineParams.put("line_name",
	 * cursor.getString(cursor.getColumnIndex("line_name")));
	 * offlineParams.put("type", cursor.getInt(cursor.getColumnIndex("type")));
	 * offlineParams.put("line_id",
	 * cursor.getString(cursor.getColumnIndex("line_id")));
	 * offlineParams.put("version",
	 * cursor.getInt(cursor.getColumnIndex("version")));
	 * offlineParams.put("state",
	 * cursor.getString(cursor.getColumnIndex("state"))); break; }
	 * cursor.close(); if (offlineParams.size() > 0) Log.i("NearFragment2",
	 * "开通了： " + offlineParams.get("line_id").toString()); return offlineParams;
	 * }
	 * 
	 * public boolean IsRtBusline(String LineName, String StartStation, String
	 * EndStation) { // openDataBase(); LineName = LineName.replace("路",
	 * "").trim(); Log.i("NearFragment2", LineName); LineName =
	 * LineName.replace(" ", "").trim(); int LineNameLength = LineName.length();
	 * if (LineName.indexOf("内") == LineNameLength - 1 || LineName.indexOf("外")
	 * == LineNameLength - 1) LineName = LineName.substring(0, LineNameLength -
	 * 1); StartStation = StartStation.replace(" ", "") .substring(0,
	 * StartStation.length() - 1).trim(); EndStation = EndStation.replace(" ",
	 * "") .substring(0, EndStation.length() - 1).trim(); Log.i("NearFragment2",
	 * LineName); // String string = busline.substring(busline.indexOf("(") +
	 * 1); // Log.i("NearFragment2", busline); String sql =
	 * "select line_name,line,type,line_id, version ,state from offline_data where line_name like \'"
	 * + LineName + "%" + StartStation + "%" + EndStation + "%\'";
	 * Log.i("NearFragment2", "sql： " + sql);
	 * 
	 * Cursor cursor = getMyDataBase().rawQuery(sql, null); if
	 * (cursor.moveToFirst()) { cursor.close(); return true; } else {
	 * cursor.close(); return false; } }
	 */

	public String getBuslineIdWithBuslineName(String busline) {
		String buslineId = null;
		// openDataBase();
		busline.replace("路(", "(");
		String sql = "select line_id where line_name like \'"
				+ busline.substring(0, busline.indexOf("-")) + "%\'";
		Cursor cursor = getMyDataBase().rawQuery(sql, null);

		while (cursor.moveToNext()) {
			buslineId = cursor.getInt(0) + "";
			break;
		}
		cursor.close();
		return buslineId;
	}

	public String getBuslineNameWithBuslineid(String buslineId) {
		String busline = null;
		// openDataBase();
		String sql = "select line_name where  = line_id " + buslineId;
		Cursor cursor = getMyDataBase().rawQuery(sql, null);

		while (cursor.moveToNext()) {
			busline = cursor.getString(0) + "";
			break;
		}
		cursor.close();
		return busline;
	}

	// public void DownFileWithUrl(final String urlString) {
	// DownedFileLength = 0;
	// // TODO Auto-generated method stub
	// Thread thread = new Thread() {
	// public void run() {
	// try {
	// DownFile(urlString);
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// }
	// };
	// thread.start();
	// }

	// private void DownFile(String urlString) {
	//
	// /*
	// * 连接到服务器
	// */
	//
	// try {
	// URL url = new URL(urlString);
	// Log.i("urlString", urlString);
	// connection = url.openConnection();
	// if (connection.getReadTimeout() == 5) {
	// Log.i("---------->", "当前网络有问题");
	// // return;
	// }
	// inputStream = connection.getInputStream();
	//
	// } catch (MalformedURLException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// /*
	// * 文件的保存路径和和文件名其中Nobody.mp3是在手机SD卡上要保存的路径，如果不存在则新建
	// */
	//
	// File file1 = new File(DB_PATH + "update" + DB_NAME);
	// if (!file1.exists()) {
	// close();
	// file1.delete();
	// }
	// File file = new File(DB_PATH + "update" + DB_NAME);
	// if (!file.exists()) {
	// try {
	// file.createNewFile();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// /*
	// * 向SD卡中写入文件,用Handle传递线程
	// */
	// Message message = new Message();
	// try {
	// outputStream = new FileOutputStream(file);
	// byte[] buffer = new byte[1024 * 4];
	// FileLength = connection.getContentLength();
	// message.what = 0;
	// handler.sendMessage(message);
	// while (DownedFileLength < FileLength) {
	// outputStream.write(buffer);
	// DownedFileLength += inputStream.read(buffer);
	// Log.i("-------->", DownedFileLength + "");
	// Message message1 = new Message();
	// message1.what = 1;
	// handler.sendMessage(message1);
	// }
	// Message message2 = new Message();
	// message2.what = 2;
	// handler.sendMessage(message2);
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}
