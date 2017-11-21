package com.bnrc.util.collectwifi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.params.ClientPNames;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bnrc.busapp.R;
import com.bnrc.network.MyVolley;
import com.bnrc.network.StringRequest;
import com.bnrc.network.toolbox.Response;
import com.bnrc.network.toolbox.VolleyError;
import com.bnrc.util.PCUserDataDBHelper;
import com.bnrc.util.VolleyNetwork.upLoadListener;
import com.bnrc.util.VolleyNetwork.versionListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class CollectWifiDBHelper extends SQLiteOpenHelper {
	private static final String TAG = CollectWifiDBHelper.class.getSimpleName();
	private static CollectWifiDBHelper instance = null;
	private static SQLiteDatabase mDatabase;
	private static final int dbVersion = 1;
	private static final String DB_NAME = "collectdata.db"; // 保存的数据库文件名
	private static final String PACKAGE_NAME = "com.bnrc.busapp";
	private static final String DATABASE_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME; // 在手机里存放数据库的位置
	private Context mContext;
	private static final String CollectTable = "CollectWifi";
	private int _id = 0;

	public static CollectWifiDBHelper getInstance(Context context) {
		if (instance == null) {
			try {

				instance = new CollectWifiDBHelper(
						context.getApplicationContext());
				instance.mContext = context;
				instance.openDatabase();

			} catch (Exception ioe) {
				throw new Error("Unable to create database");
			}
		}
		return instance;
	}

	private CollectWifiDBHelper(Context context) {
		super(context, DB_NAME, null, dbVersion);
		this.mContext = context;
	}

	public void openDatabase() throws SQLException {
		// Open the database
		try {
			// ����ļ��ľ���·��
			String databaseFilename = DATABASE_PATH + "/databases/" + DB_NAME;
			File dir = new File(DATABASE_PATH + "/databases/");

			if (!dir.exists()) {
				dir.mkdir();
			}
			;

			if (!(new File(databaseFilename)).exists()) {
				InputStream is = mContext.getResources().openRawResource(
						R.raw.collectdata);
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

			mDatabase = SQLiteDatabase.openOrCreateDatabase(databaseFilename,
					null);

		} catch (Exception e) {
			Log.i("open error", e.getMessage());
		}
	}

	@Override
	public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
		// TODO Auto-generated method stub

	}

	public SQLiteDatabase getMyDatabase() {
		return mDatabase;
	}

	// public void closeDB() {
	// instance.close();
	// // if (mDatabase != null)
	// // mDatabase.close();
	//
	// }

	public void onOpen(SQLiteDatabase db) {

	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "upgrade");
		db.execSQL("drop table if exists " + CollectTable);
		onCreate(db);
	}

	// 插入扫描到的wifi信息
	public void InsertScanData(Map<String, Object> collectInfo) {
		Log.i(TAG, "InsertScanData: " + collectInfo.toString());
		 ContentValues values = new ContentValues();
		 values.put("DeviceID", String.valueOf(collectInfo.get("DeviceID")));
		 values.put("Time", String.valueOf(collectInfo.get("Time")));
		 values.put("Latitude", String.valueOf(collectInfo.get("Latitude")));
		 values.put("Longitude",
		 String.valueOf(collectInfo.get("Longitude")));
		 values.put("SSID", String.valueOf(collectInfo.get("SSID")));
		 values.put("MAC", String.valueOf(collectInfo.get("MAC")));
		 values.put("Level", String.valueOf(collectInfo.get("Level")));
		 values.put("LocationType",
		 String.valueOf(collectInfo.get("LocationType")));
		 values.put("LocationPrecision",
		 String.valueOf(collectInfo.get("LocationPrecision")));
		 values.put("Speed", String.valueOf(collectInfo.get("Speed")));
		 long id = getMyDatabase().insert(CollectTable, null, values);

		/*String DeviceID = String.valueOf(collectInfo.get("DeviceID"));
		String Time = String.valueOf(collectInfo.get("Time"));
		String Latitude = String.valueOf(collectInfo.get("Latitude"));
		String Longitude = String.valueOf(collectInfo.get("Longitude"));
		String SSID = String.valueOf(collectInfo.get("SSID"));
		String MAC = String.valueOf(collectInfo.get("MAC"));
		String Level = String.valueOf(collectInfo.get("Level"));
		String LocationType = String.valueOf(collectInfo.get("LocationType"));
		String LocationPrecision = String.valueOf(collectInfo
				.get("LocationPrecision"));
		String Speed = String.valueOf(collectInfo.get("Speed"));

		String sql = "insert into CollectWifi (DeviceID,Time,Latitude,Longitude,SSID,MAC,Level,LocationType,LocationPrecision,Speed) values (\'"
				+ DeviceID
				+ "\',\'"
				+ Time
				+ "\',\'"
				+ Latitude
				+ "\',\'"
				+ Longitude
				+ "\',\'"
				+ SSID
				+ "\',\'"
				+ MAC
				+ "\',\'"
				+ Level
				+ "\',\'"
				+ LocationType
				+ "\',\'"
				+ LocationPrecision
				+ "\',\'" + Speed + "\')";
		getMyDatabase().execSQL(sql);*/
		Log.i(TAG, id + "InsertScanData: "+values);

	}

	List<Map<String, String>> list;

	// 查询扫描到的wifi信息
	public List<Map<String, Object>> FindScanData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sql = "select * from CollectWifi Where _id >= " + _id;
		Cursor cursor = getMyDatabase().rawQuery(sql, null);
		while (cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("Time", cursor.getString(cursor.getColumnIndex("Time")));
			map.put("Latitude",
					cursor.getString(cursor.getColumnIndex("Latitude")));
			map.put("Longitude",
					cursor.getString(cursor.getColumnIndex("Longitude")));
			map.put("Speed", cursor.getString(cursor.getColumnIndex("Speed")));
			_id = cursor.getInt(cursor.getColumnIndex("_id"));
			list.add(map);
		}
		cursor.close();
		Log.i(TAG, "FindScanData: " + list.toString());
		return list;
	}

	// 插入用户已经确定的公交--Wifi信息
	public void InsertSureData(Map<String, String> sureData) {
		// Log.i(TAG, "InsertSureData: " + sureData.toString());
		// getMyDatabase().execSQL(
		// "insert into " + SURE_TABLE + " values(null,?,?,?)",
		// new String[] { sureData.get("SSID"), sureData.get("MAC"),
		// sureData.get("线路") });
		// Log.i(TAG,
		// "InsertSureData :  " + sureData.get("线路") + " "
		// + sureData.get("SSID") + " " + sureData.get("MAC"));
		// getMyDatabase().execSQL("insert into subinfo values(null,?,?,?)",
		// new String[] { inputBus.getText().toString().trim(),
		// "BNRC-AIR", bssid });
	}

	public boolean HasSureData(String mac) {// 查看确定的信息表中是否有要查询的mac
		// Cursor cursor = getMyDatabase().rawQuery(
		// "select * from " + SURE_TABLE + " where MAC=\'" + mac + "\'",
		// null);
		// if (cursor.getCount() != 0) {
		// Log.i(TAG, cursor.getCount() + "");
		// return true;
		// } else
		// return false;
		return false;
	}

	// 查询用户已经确定的公交--Wifi信息
	public List<Map<String, String>> FindSureData() {
		// List<Map<String, String>> list = new ArrayList<Map<String,
		// String>>();
		// Cursor cursor = getMyDatabase().rawQuery("select * from " +
		// SURE_TABLE,
		// null);
		// while (cursor.moveToNext()) {
		// Map<String, String> map = new HashMap<String, String>();
		// map.put("id", cursor.getInt(0) + "");
		// map.put("SSID", cursor.getString(1));
		// map.put("MAC", cursor.getString(2));
		// map.put("ROUTE", cursor.getString(3));
		//
		// list.add(map);
		// }
		// cursor.close();
		// Log.i(TAG, "FindSureData: " + list.toString());

		return list;
	}

	public void deleteCollectWifiTables() {
		Log.i(TAG, "create ");
		getMyDatabase().execSQL("drop table if exists " + CollectTable);
		onCreate(getMyDatabase());
	}

	public void deleteSureWifiTables() {
		// Log.i(TAG, "create ");
		// getMyDatabase().execSQL("drop table if exists " + SURE_TABLE);
		// onCreate(getMyDatabase());
	}

	public void deleteCollectWifiTablesByTimestamp(String timeStamp) {
		Log.i(TAG, "deleteCollectWifiTablesByTimestamp " + "timeStamp: "
				+ timeStamp);
		if (timeStamp.equalsIgnoreCase("0"))
			return;
		String sql = "delete from " + CollectTable + " where Time<	"
				+ timeStamp;
		getMyDatabase().execSQL(sql);
	}

	// private String postUrl = "http://10.108.106.33:8080/NewOne/JsonServlet";

	private String postUrl = "http://123.206.46.98:80/admin/collect/android/";

	// private String postUrl = "http://www.baidu.com";

	public void postFile(String newPath, final UploadDatabaseListener listener)
			throws Exception {
		String path = DATABASE_PATH + "/databases/" + newPath;
		final File file = new File(path);
		if (file.exists()) {
			String[] projection = {"MAC"};
			Cursor cursor = getMyDatabase().query(
					CollectTable,
					projection,
					null,
					null,
					null,
					null,
					null
			);
			if(cursor.getCount()>0){

				AsyncHttpClient client = new AsyncHttpClient();
				// client.getHttpClient().getParams()
				// .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
				RequestParams params = new RequestParams();
				params.put("file", file);
				Log.i(TAG,
						"file's name: " + file.getAbsolutePath() + " "
								+ file.getName()+' '+file.length());
				client.post(postUrl, params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						try {
							Log.i(TAG, "成功: " + arg0 + " "
									+ new String(arg2, "UTF-8"));
							for (Header head : arg1)
								Log.i(TAG, "成功: " + head.toString());

						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							Log.i(TAG, "Exception: " + e.getMessage());
							e.printStackTrace();
						}
						file.delete();
						instance.openDatabase();
						// mDatabase = instance.getWritableDatabase();
						listener.onComplete();
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
										  Throwable arg3) {
						// TODO Auto-generated method stub
						try {
							Log.i(TAG, "失败: " + arg0 + " " + arg1 + " " + arg2
									+ " " + arg3);
							listener.onFail();

						} catch (Exception e) {
							// TODO Auto-generated catch block
							Log.i(TAG, "Exception: " + e.getMessage());
							e.printStackTrace();
						}

					}

				});
			}
			else{
				/*file.delete();
				instance.openDatabase();*/
				file.delete();
				instance.openDatabase();
				// mDatabase = instance.getWritableDatabase();
				listener.onComplete();
				Log.i(TAG, "postFile: 没内容"+file.getName()+' '+file.length());
			}
		} else {
			Log.i(TAG, "文件不存在");
			instance.openDatabase();
			// mDatabase = instance.getWritableDatabase();
		}
	}

	public interface UploadDatabaseListener {
		void onComplete();

		void onFail();
	}

}
