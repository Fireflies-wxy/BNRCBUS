package com.bnrc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bnrc.busapp.R;
import com.bnrc.util.collectwifi.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class BeijingDBHelper extends SQLiteOpenHelper {
	public static final String TAG = "DATABASEUTIL";
	public static BeijingDBHelper mDatabaseInstance;
	private static final String DBNAME = "beijing.db";
	private static final int DBVERSION = 1;
	private static final String PACKAGE_NAME = "com.example.copybnrcbus";
	private static final String DATABASE_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME; // 在手机里存放数据库的位置
	private Context mContext;
	private static SQLiteDatabase mDatabase;
	private ArrayList<ArrayList<String>> alertStations = null;
	private ArrayList<ArrayList<String>> favStations = null;

	public BeijingDBHelper(Context pContext, String pDbName, int pDbVersion) {
		super(pContext, pDbName, null, pDbVersion);
		this.mContext = pContext;
	}

	public static BeijingDBHelper getDatabaseInstance(Context pContext) {
		if (mDatabaseInstance == null) {
			mDatabaseInstance = new BeijingDBHelper(pContext, DBNAME, DBVERSION);
			mDatabaseInstance.openSqliteDatabase();
			mDatabase = mDatabaseInstance.getWritableDatabase();
		}

		return mDatabaseInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase pDatabase) {
		// TODO Auto-generated method stub
		Log.i(TAG, "databaseUtil_onCreate");
		/*
		 * String createTableStationFav =
		 * "create table station_fav (name text not null ,latitude text not null,longitude text not null,_id Integer PRIMARY KEY AUTOINCREMENT)"
		 * ;
		 * 
		 * pDatabase.execSQL(createTableStationFav); String
		 * createTableStationAlert =
		 * "create table station_alert (name text not null ,latitude text not null,longitude text not null,_id Integer PRIMARY KEY AUTOINCREMENT)"
		 * ;
		 * 
		 * pDatabase.execSQL(createTableStationAlert); String
		 * createTableLatestSearch =
		 * "create table latest_search (id text not null ,keyName text not null,name text not null,_id Integer PRIMARY KEY AUTOINCREMENT)"
		 * ;
		 * 
		 * pDatabase.execSQL(createTableLatestSearch);
		 */
	}

	@Override
	public void onUpgrade(SQLiteDatabase pDatabase, int arg1, int arg2) {
		// TODO Auto-generated method stub
		Log.i(TAG, "databaseUtil_onUpgrade");
		/*
		 * pDatabase.execSQL("drop table if exists station_fav");
		 * pDatabase.execSQL("drop table if exists station_alert");
		 * pDatabase.execSQL("drop table if exists latest_search");
		 * 
		 * onCreate(pDatabase);
		 */
	}

	// 打开数据库
	private void openSqliteDatabase() {
		Log.i(TAG, "databaseUtil_opendatabase ");
		try {
			String databaseFilename = DATABASE_PATH + "/databases/" + DBNAME;
			File dir = new File(DATABASE_PATH + "/databases/");
			Log.i(TAG, "databaseUtil_dir: " + dir.toString());
			Log.i(TAG, "databaseUtil_databaseFilename: " + databaseFilename);

			if (!dir.exists())
				dir.mkdir();
			if (!(new File(databaseFilename)).exists()) {
				Log.i(TAG, "databaseUtil_copy bj");
				InputStream is = mContext.getResources().openRawResource(
						R.raw.pc);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}

				fos.close();
				is.close();
				// }
				// SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
			}
		} catch (Exception e) {
		}

	}

	private SQLiteDatabase getMyDatabase() {
		return mDatabase;
	}

	/*************************************************************************************/
	public ArrayList<ArrayList<String>> getAroundStationsWithLocation(
			final LatLng location) {
		// openDataBase();
		ArrayList<ArrayList<String>> stations = new ArrayList<ArrayList<String>>();
		float latRadius = 0.0f;
		float lngRadius = 0.0f;

		SharedPreferences preferences = mContext.getSharedPreferences(
				Constants.SETTING, Context.MODE_PRIVATE);
		String searchradius = preferences.getString(Constants.SETTING_RAD,
				"1000米");
		int radius = Integer.parseInt(searchradius.subSequence(0,
				searchradius.length() - 1).toString());
		switch (radius) {
		case 600:
			latRadius = 0.004f;
			lngRadius = 0.005f;
			break;
		case 700:
			latRadius = 0.005f;
			lngRadius = 0.0065f;
			break;
		case 800:
			latRadius = 0.006f;
			lngRadius = 0.008f;
			break;
		case 900:
			latRadius = 0.007f;
			lngRadius = 0.009f;
			break;
		case 1000:
			latRadius = 0.008f;
			lngRadius = 0.010f;
			break;
		case 1100:
			latRadius = 0.009f;
			lngRadius = 0.011f;
			break;
		case 1200:
			latRadius = 0.009f;
			lngRadius = 0.012f;
			break;
		case 1300:
			latRadius = 0.010f;
			lngRadius = 0.013f;
			break;
		case 1400:
			latRadius = 0.010f;
			lngRadius = 0.014f;
			break;
		case 1500:
			latRadius = 0.011f;
			lngRadius = 0.015f;
			break;
		default:
			break;
		}

		double lat = location.latitude;
		double lng = location.longitude;
		double smallLat = lat - latRadius;
		double smallLng = lng - lngRadius;
		double bigLat = lat + latRadius;
		double bigLng = lng + lngRadius;

		// ���Ҹ�����վ
		String sql = "select * from stopline where  lat > " + smallLat
				+ " and lat < " + bigLat + " and lng > " + smallLng
				+ " and lng < " + bigLng;
		Cursor cursor = getMyDatabase().rawQuery(sql, null);
		while (cursor.moveToNext()) {
			ArrayList<String> arrayArrayList = new ArrayList<String>();
			arrayArrayList.add(cursor.getInt(0) + "");
			arrayArrayList.add(cursor.getString(1));
			arrayArrayList.add(cursor.getFloat(2) + "");
			arrayArrayList.add(cursor.getFloat(3) + "");
			arrayArrayList.add(cursor.getString(4));

			// int j = stations.size();
			// ArrayList<String> station;
			// int i = 0;
			// for (; i < j; i++) {
			// station = stations.get(i);
			// if (cursor.getString(1).equalsIgnoreCase(station.get(1))) {
			// break;
			// }
			// }
			// if (i == j ){
			// stations.add(arrayArrayList);
			// }
			stations.add(arrayArrayList);
		}

		Comparator<ArrayList<String>> comparator = new Comparator<ArrayList<String>>() {
			public int compare(ArrayList<String> s1, ArrayList<String> s2) {
				LatLng stationPoint1 = new LatLng(Float.parseFloat(s1.get(2)),
						Float.parseFloat(s1.get(3)));
				double distance1 = DistanceUtil.getDistance(location,
						stationPoint1);
				LatLng stationPoint2 = new LatLng(Float.parseFloat(s2.get(2)),
						Float.parseFloat(s2.get(3)));
				double distance2 = DistanceUtil.getDistance(location,
						stationPoint2);
				if (distance1 > distance2) {
					return 1;
				} else if (distance1 < distance2) {
					return -1;
				}
				return 0;
			}
		};

		Collections.sort(stations, comparator);

		cursor.close();

		// close();
		return stations;
	}

	public ArrayList<ArrayList<String>> getAroundStationsUpOrDownWithLocation(
			final LatLng location) {
		// openDataBase();
		ArrayList<ArrayList<String>> stations = new ArrayList<ArrayList<String>>();
		float latRadius = 0.0f;
		float lngRadius = 0.0f;

		SharedPreferences preferences = mContext.getSharedPreferences(
				Constants.SETTING, Context.MODE_PRIVATE);
		String searchradius = preferences.getString(Constants.SETTING_RAD,
				"1000米");
		int radius = Integer.parseInt(searchradius.subSequence(0,
				searchradius.length() - 1).toString());
		switch (radius) {
		case 600:
			latRadius = 0.004f;
			lngRadius = 0.005f;
			break;
		case 700:
			latRadius = 0.005f;
			lngRadius = 0.0065f;
			break;
		case 800:
			latRadius = 0.006f;
			lngRadius = 0.008f;
			break;
		case 900:
			latRadius = 0.007f;
			lngRadius = 0.009f;
			break;
		case 1000:
			latRadius = 0.008f;
			lngRadius = 0.010f;
			break;
		case 1100:
			latRadius = 0.009f;
			lngRadius = 0.011f;
			break;
		case 1200:
			latRadius = 0.009f;
			lngRadius = 0.012f;
			break;
		case 1300:
			latRadius = 0.010f;
			lngRadius = 0.013f;
			break;
		case 1400:
			latRadius = 0.010f;
			lngRadius = 0.014f;
			break;
		case 1500:
			latRadius = 0.011f;
			lngRadius = 0.015f;
			break;
		default:
			break;
		}

		double lat = location.latitude;
		double lng = location.longitude;
		double smallLat = lat - latRadius;
		double smallLng = lng - lngRadius;
		double bigLat = lat + latRadius;
		double bigLng = lng + lngRadius;

		// ���Ҹ�����վ
		String sql = "select min(id),stopname,lat,lng,lid from stopline where  lat > "
				+ smallLat
				+ " and lat < "
				+ bigLat
				+ " and lng > "
				+ smallLng
				+ " and lng < " + bigLng + " group by stopname";
		Cursor cursor = getMyDatabase().rawQuery(sql, null);
		while (cursor.moveToNext()) {
			ArrayList<String> arrayArrayList = new ArrayList<String>();
			arrayArrayList.add(cursor.getInt(0) + "");
			arrayArrayList.add(cursor.getString(1));
			arrayArrayList.add(cursor.getFloat(2) + "");
			arrayArrayList.add(cursor.getFloat(3) + "");
			arrayArrayList.add(cursor.getString(4));

			// int j = stations.size();
			// ArrayList<String> station;
			// int i = 0;
			// for (; i < j; i++) {
			// station = stations.get(i);
			// if (cursor.getString(1).equalsIgnoreCase(station.get(1))) {
			// break;
			// }
			// }
			// if (i == j ){
			// stations.add(arrayArrayList);
			// }
			stations.add(arrayArrayList);
		}

		Comparator<ArrayList<String>> comparator = new Comparator<ArrayList<String>>() {
			public int compare(ArrayList<String> s1, ArrayList<String> s2) {
				LatLng stationPoint1 = new LatLng(Float.parseFloat(s1.get(2)),
						Float.parseFloat(s1.get(3)));
				double distance1 = DistanceUtil.getDistance(location,
						stationPoint1);
				LatLng stationPoint2 = new LatLng(Float.parseFloat(s2.get(2)),
						Float.parseFloat(s2.get(3)));
				double distance2 = DistanceUtil.getDistance(location,
						stationPoint2);
				if (distance1 > distance2) {
					return 1;
				} else if (distance1 < distance2) {
					return -1;
				}
				return 0;
			}
		};

		Collections.sort(stations, comparator);

		cursor.close();

		// close();
		return stations;
	}

	public String getAnotherStationIdWithStationIdAndStationName(
			String stationId, String stationName) {
		String anotherStationId = "0";
		String sql = "select id from stopline where stopname= \'" + stationName
				+ "\'";
		Cursor cursor = getMyDatabase().rawQuery(sql, null);
		while (cursor.moveToNext()) {
			if (!cursor.getString(0).equalsIgnoreCase(stationId)) {
				anotherStationId = cursor.getString(0);
				break;
			}
		}
		return anotherStationId;
	}

	public ArrayList<String> getAnotherBuslineInfoWithBuslineIdAndBuslineKeyname(
			String buslineId, String buslineKeyname) {
		ArrayList<String> anotherBuslineInfo = new ArrayList<String>();
		String sql = "select * from line where keyName= \'" + buslineKeyname
				+ "\'";
		Cursor cursor = getMyDatabase().rawQuery(sql, null);
		while (cursor.moveToNext()) {
			if (!cursor.getString(0).equalsIgnoreCase(buslineId)) {
				anotherBuslineInfo.add(cursor.getString(0));
				anotherBuslineInfo.add(cursor.getString(1));
				anotherBuslineInfo.add(cursor.getString(2));
				break;
			}
		}
		return anotherBuslineInfo;
	}

	public ArrayList<ArrayList<String>> getNearbyBuslineWithLocation(
			LatLng location) {
		ArrayList<ArrayList<String>> buslines = new ArrayList<ArrayList<String>>();
		double lat = location.latitude;
		double lng = location.longitude;

		// 设定经纬度分别与当前位置差0.003度的矩形区域为附近区域
		double smallLat = lat - 0.004;
		double smallLng = lng - 0.004;
		double bigLat = lat + 0.004;
		double bigLng = lng + 0.004;

		String sql0 = "select lid from stop where  latitude > " + smallLat
				+ " and latitude < " + bigLat + " and longtitude > " + smallLng
				+ " and longtitude < " + bigLng;

		Cursor cursor0 = getMyDatabase().rawQuery(sql0, null);
		ArrayList<String> lineIds = new ArrayList<String>();
		while (cursor0.moveToNext()) {
			lineIds.add(cursor0.getInt(0) + "");
		}
		cursor0.close();

		// 根据线路id查找线路名，需要显示线路名，线路id需要纪录，传到下一页面，查看线路详情。
		for (int i = 0; i < lineIds.size(); i++) {
			String sql = "select keyName, name from line where id = "
					+ lineIds.get(i);
			Cursor cursor = getMyDatabase().rawQuery(sql, null);
			while (cursor.moveToNext()) {
				int j = buslines.size();
				int j2 = 0;
				ArrayList<String> arrayArrayList;
				for (j2 = 0; j2 < j; j2++) {
					arrayArrayList = buslines.get(j2);
					if (arrayArrayList.get(1).equalsIgnoreCase(
							cursor.getString(0))) {
						break;
					}
				}
				if (j2 == j) {
					arrayArrayList = new ArrayList<String>();
					arrayArrayList.add(lineIds.get(i));
					arrayArrayList.add(cursor.getString(0));
					arrayArrayList.add(cursor.getString(1));
					buslines.add(arrayArrayList);
				}
			}
			cursor0.close();
		}

		return buslines;
	}

	public ArrayList<ArrayList<String>> getBusLinesWithStationID(
			String stationId) {
		ArrayList<ArrayList<String>> buslines = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> stations = new ArrayList<ArrayList<String>>();
		// openDataBase();
		Log.i(TAG, "getBusLinesWithStationID " + Integer.parseInt(stationId));
		String sql = "select lid,lat,lng from stopline where id = \'"
				+ Integer.parseInt(stationId) + "\'";
		Cursor cursor = getMyDatabase().rawQuery(sql, null);
		while (cursor.moveToNext()) {
			ArrayList<String> arrayArrayList = new ArrayList<String>();
			arrayArrayList.add(cursor.getString(0));
			arrayArrayList.add(cursor.getString(1));
			arrayArrayList.add(cursor.getString(2));
			stations.add(arrayArrayList);
		}
		cursor.close();
		Log.i(TAG, "stations's size is " + stations.size());
		if (stations.size() == 0) {
			return buslines;
		} else {
			String[] buslineidArr;
			if (stations.get(0).get(0).indexOf(";") > 0)
				buslineidArr = stations.get(0).get(0).split(";");
			else
				buslineidArr = stations.get(0).get(0).split(",");
			for (String stemp : buslineidArr) {
				String sql2 = "select id, keyName, name ,startName,endName from line where id ="
						+ Integer.parseInt(stemp);
				Cursor cursor2 = getMyDatabase().rawQuery(sql2, null);
				while (cursor2.moveToNext()) {
					ArrayList<String> arrayArrayList = new ArrayList<String>();
					arrayArrayList.add(cursor2.getString(0));
					arrayArrayList.add(cursor2.getString(1));
					arrayArrayList.add(cursor2.getString(2));
					arrayArrayList.add(cursor2.getString(3));
					arrayArrayList.add(cursor2.getString(4));
					buslines.add(arrayArrayList);
					Log.i(TAG, "busline is " + arrayArrayList.get(1));

				}
				cursor2.close();
			}
			return buslines;
		}

	}

	public ArrayList<ArrayList<String>> getStationsWithStationKeyword(
			String keyword) {
		// openDataBase();
		ArrayList<ArrayList<String>> stations = new ArrayList<ArrayList<String>>();
		// 查找附近车站
		String sql = "select * from stopline where  stopname  like '%"
				+ keyword + "%'";
		Cursor cursor = getMyDatabase().rawQuery(sql, null);

		while (cursor.moveToNext()) {
			ArrayList<String> arrayArrayList = new ArrayList<String>();
			arrayArrayList.add(cursor.getInt(0) + "");
			arrayArrayList.add(cursor.getString(1));
			arrayArrayList.add(cursor.getFloat(2) + "");
			arrayArrayList.add(cursor.getFloat(3) + "");
			arrayArrayList.add(cursor.getString(4));
			stations.add(arrayArrayList);
		}
		cursor.close();
		// close();
		return stations;
	}

	public ArrayList<ArrayList<String>> getBusLinesWithKeyword(String keyword) {
		Log.i(TAG, "DatabaseUtil_getBusLinesWithKeyword_keyword " + keyword);
		ArrayList<ArrayList<String>> buslines = new ArrayList<ArrayList<String>>();
		// openDataBase();

		String sql = "select id, keyName, name from line where keyName like '%"
				+ keyword + "%' order by keyName desc";
		Cursor cursor = getMyDatabase().rawQuery(sql, null);

		while (cursor.moveToNext()) {
			ArrayList<String> arrayArrayList = new ArrayList<String>();
			arrayArrayList.add(cursor.getString(0));
			arrayArrayList.add(cursor.getString(1));
			arrayArrayList.add(cursor.getString(2));
			buslines.add(arrayArrayList);
		}
		cursor.close();
		// close();
		return buslines;
	}

	public ArrayList<ArrayList<String>> getStationsWithBuslineId(
			String buslineId) {
		ArrayList<ArrayList<String>> stations = new ArrayList<ArrayList<String>>();
		// openDataBase();

		/*
		 * String sql =
		 * "select name, latitude, longtitude ,id from stop where lid = " +
		 * buslineId;
		 */
		String sql = "select name, latitude, longtitude from stop where lid = "
				+ buslineId;
		Cursor cursor = getMyDatabase().rawQuery(sql, null);

		while (cursor.moveToNext()) {
			ArrayList<String> arrayArrayList = new ArrayList<String>();
			arrayArrayList.add(cursor.getString(0));
			arrayArrayList.add(cursor.getFloat(1) + "");
			arrayArrayList.add(cursor.getFloat(2) + "");
			int stationId = getStationIdWithStationNameAndlid(
					cursor.getString(0), buslineId);
			arrayArrayList.add(stationId + "");
			stations.add(arrayArrayList);
		}
		cursor.close();
		// close();
		return stations;// name lat lon id
	}

	public ArrayList<ArrayList<String>> getBusLinesWithStation(
			ArrayList<String> station) {
		ArrayList<ArrayList<String>> buslines = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> stations = new ArrayList<ArrayList<String>>();
		// openDataBase();
		String sql = "select lid,lat,lng from stopline where stopname = \'"
				+ station.get(0) + "\'";
		Cursor cursor = getMyDatabase().rawQuery(sql, null);

		MostSimilarString mostSimilarString = new MostSimilarString();
		String buslineIds = null;
		String[] buslineidArr = null;
		float maxSimilar = 0;
		int maxsimilarIndex = 0;
		float curSimilar;
		while (cursor.moveToNext()) {
			ArrayList<String> arrayArrayList = new ArrayList<String>();
			arrayArrayList.add(cursor.getString(0));
			arrayArrayList.add(cursor.getString(1));
			arrayArrayList.add(cursor.getString(2));
			stations.add(arrayArrayList);
			curSimilar = mostSimilarString.getSimilarityRatio(
					cursor.getString(1), station.get(1))
					+ mostSimilarString.getSimilarityRatio(cursor.getString(2),
							station.get(2));
			if (curSimilar > maxSimilar) {
				maxSimilar = curSimilar;
				maxsimilarIndex = stations.size() - 1;
			}
		}
		if (stations.size() == 0) {
			buslineIds = null;
		} else {
			buslineIds = stations.get(maxsimilarIndex).get(0);
		}
		cursor.close();
		if (buslineIds == null) {
			return buslines;
		} else {
			buslineidArr = buslineIds.split(",");
			for (String stemp : buslineidArr) {
				String sql2 = "select id, keyName, name from line where id ="
						+ stemp;
				Cursor cursor2 = getMyDatabase().rawQuery(sql2, null);
				while (cursor2.moveToNext()) {
					ArrayList<String> arrayArrayList = new ArrayList<String>();
					arrayArrayList.add(cursor2.getString(0));
					arrayArrayList.add(cursor2.getString(1));
					arrayArrayList.add(cursor2.getString(2));
					buslines.add(arrayArrayList);
					break;
				}
				cursor2.close();
			}
		}
		// close();
		return buslines;
	}

	public int getStationIdWithStationNameAndlid(String stationkeyName,
			String lid) {
		String sql = "select id,lid from stopline where stopname =\'"
				+ stationkeyName + "\'";
		Cursor cursor = getMyDatabase().rawQuery(sql, null);
		while (cursor.moveToNext()) {
			String[] busArray;
			if (cursor.getString(1).indexOf(";") > 0)
				busArray = cursor.getString(1).split(";");
			else
				busArray = cursor.getString(1).split(",");
			for (String busline : busArray) {
				if (busline.trim().equalsIgnoreCase(lid))
					return cursor.getInt(0);
			}

		}
		return -1;
	}

	// 获得附近公交信息
	private ArrayList<Map<String, String>> FindNearBusLine(LatLng myPoint) {
		ArrayList<Map<String, String>> BusInfo = new ArrayList<Map<String, String>>();// 附近公交线路信息
		ArrayList<String> SameBus = new ArrayList<String>();// 筛选附近重复的线路

		double myLat = myPoint.latitude;
		double myLng = myPoint.longitude;
		float latRadius = 0.004f;
		float lngRadius = 0.005f;
		SharedPreferences preferences = mContext.getSharedPreferences(
				Constants.SETTING, mContext.MODE_PRIVATE);
		String radius = preferences.getString(Constants.SETTING_RAD, "500米");
		radius = radius.substring(0, radius.indexOf("米"));
		int rad = Integer.parseInt(radius);
		switch (rad) {
		case 600:
			latRadius = 0.004f;
			lngRadius = 0.005f;
			break;
		case 700:
			latRadius = 0.005f;
			lngRadius = 0.0065f;
			break;
		case 800:
			latRadius = 0.006f;
			lngRadius = 0.008f;
			break;
		case 900:
			latRadius = 0.007f;
			lngRadius = 0.009f;
			break;
		case 1000:
			latRadius = 0.008f;
			lngRadius = 0.010f;
			break;
		case 1100:
			latRadius = 0.009f;
			lngRadius = 0.011f;
			break;
		case 1200:
			latRadius = 0.009f;
			lngRadius = 0.012f;
			break;
		case 1300:
			latRadius = 0.010f;
			lngRadius = 0.013f;
			break;
		case 1400:
			latRadius = 0.010f;
			lngRadius = 0.014f;
			break;
		case 1500:
			latRadius = 0.011f;
			lngRadius = 0.015f;
			break;
		default:
			break;
		}
		double smallLat = myLat - latRadius;
		double smallLng = myLng - lngRadius;
		double bigLat = myLat + latRadius;
		double bigLng = myLng + lngRadius;
		// 查找附近车站
		String sqlStation = "select * from stopline where  lat > " + smallLat
				+ " and lat < " + bigLat + " and lng > " + smallLng
				+ " and lng < " + bigLng;
		Cursor cursorStation = getMyDatabase().rawQuery(sqlStation, null);
		while (cursorStation.moveToNext()) {
			double StaLat = Double.parseDouble(cursorStation.getString(2));
			double StaLng = Double.parseDouble(cursorStation.getString(3));
			LatLng stationPoint = new LatLng(StaLat, StaLng);
			double distance = DistanceUtil.getDistance(myPoint, stationPoint);
			if (distance < Integer.parseInt(radius)) {
				String busAll = cursorStation.getString(4);
				String[] splitBus = busAll.split(",");
				Map<String, String> summary = null;
				Map<String, String> newSummary = null;
				for (int i = 0; i < splitBus.length; i++) {
					summary = new HashMap<String, String>();
					String sqlBus = "select * from line where id=\'"
							+ splitBus[i] + "\'";
					Cursor cursorBus = getMyDatabase().rawQuery(sqlBus, null);
					while (cursorBus.moveToNext()) {
						if (!SameBus.contains(cursorBus.getString(1))) {
							SameBus.add(cursorBus.getString(cursorBus
									.getColumnIndex("keyName")));
							Log.i("SUMMARY", cursorStation.getString(1) + " "
									+ cursorBus.getString(1));
							summary.put("距离", String.valueOf((int) distance));
							summary.put("车站", cursorStation.getString(1));
							summary.put("方向", cursorBus.getString(4));
							summary.put("线路", cursorBus.getString(1));
							BusInfo.add(summary);
						} else {
							for (Map<String, String> map : BusInfo) {
								if (map.get("线路")
										.equals(cursorBus.getString(1))
										&& (int) distance < Integer
												.parseInt(map.get("距离"))) {
									BusInfo.remove(map);
									newSummary = new HashMap<String, String>();
									newSummary.put("距离",
											String.valueOf((int) distance));
									newSummary.put("车站",
											cursorStation.getString(1));
									newSummary
											.put("方向", cursorBus.getString(4));
									newSummary
											.put("线路", cursorBus.getString(1));
									BusInfo.add(newSummary);
									Log.i("STATION",
											"原来的是 "
													+ map.get("车站")
													+ map.get("线路")
													+ map.get("距离")
													+ "\n后来的是 "
													+ cursorStation
															.getString(1)
													+ cursorBus.getString(1)
													+ distance);
									break;
								}
							}
						}
					}
					cursorBus.close();
				}

			}
		}
		cursorStation.close();
		return BusInfo;
	}

	public ArrayList<Map<String, String>> getNearBusInfo(LatLng myPoint) {
		return sortNearBusInfo(FindNearBusLine(myPoint));
	}

	private ArrayList<Map<String, String>> sortNearBusInfo(
			ArrayList<Map<String, String>> list) {

		Collections.sort(list, new MapComparator());
		return list;
	}

	class MapComparator implements Comparator<Map<String, String>> {
		@Override
		public int compare(Map<String, String> a, Map<String, String> b) {
			// TODO Auto-generated method stub
			if (a.get("距离").length() > b.get("距离").length())
				return 1;
			else if (a.get("距离").length() < b.get("距离").length())
				return -1;
			else {
				int sameDis = a.get("距离").compareTo(b.get("距离"));
				if (sameDis != 0 && (a.get("车站").compareTo(b.get("车站")) == 0)) {
					sameDis = 0;
				}
				return sameDis == 0 ? a.get("线路").compareTo(b.get("线路"))
						: sameDis;
			}

		}
	}

	// AutoCompleteView中根据keyName筛选公交
	public Cursor FindBusByKeyname(String keyWord) {
		Cursor cursor = getMyDatabase()
				.rawQuery(
						"select *,name as _id from line where name like ? group by Keyname order by keyName",
						new String[] { keyWord + "%" });
		return cursor;
	}

	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			String newFilename = DATABASE_PATH + "/databases/" + newPath;
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newFilename);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				oldfile.delete();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

}
