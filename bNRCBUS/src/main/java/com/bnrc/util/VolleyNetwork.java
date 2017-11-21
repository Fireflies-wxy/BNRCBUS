package com.bnrc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bnrc.network.JsonObjectRequest;
import com.bnrc.network.MyVolley;
import com.bnrc.network.toolbox.Request.Method;
import com.bnrc.network.toolbox.Response;
import com.bnrc.network.toolbox.VolleyError;
import com.bnrc.util.collectwifi.CollectWifiDBHelper;
import com.bnrc.util.collectwifi.WifiAdmin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.ScanResult;
import android.util.Log;

public class VolleyNetwork {
	private String TAG = VolleyNetwork.class.getSimpleName();
	private static VolleyNetwork mInstance = null;
	private Context mContext;
	private String upLoadURL = "http://123.206.46.98:80/admin/update/update";// 上传实时数据
	private String lineWithOneStationURL = "http://123.206.46.98:80/admin/station";// 请求本条线路上固定站点的最近一辆车
	private String lineWithAllStationURL = "http://123.206.46.98:80/admin/line";// 请求本条线路上所有车次的信息
	private String dbVersionURL = "http://123.206.46.98:80/admin/version";// 请求数据库版本号
	private String appVersionURL = "http://123.206.46.98:80/admin/version/version";
	public static final String beijingdbURL = "http://123.206.46.98:80/admin/version/download";// 更新数据库
	private static final String reportURL = "http://123.206.46.98:80/admin/report";
	private static final String postCollectMessageURL = "http://123.206.46.98:80/admin/collect/android_real";
	public static int lineList;
	private boolean isPostMessage = false;
	private LocationUtil mLocationUtil;
	private CollectWifiDBHelper mCollectWifiDBHelper;
	private SharePrefrenceUtil mSharePrefrenceUtil;
	//在线上传部分

	private WifiAdmin mWifiAdminInstance;
	private List<ScanResult> wifiScanResults;
	private String wifiSSID = "16wifi";//在线扫描

	private VolleyNetwork(Context context) {
		mContext = context.getApplicationContext();
		mLocationUtil = LocationUtil.getInstance(mContext);
		mCollectWifiDBHelper = CollectWifiDBHelper.getInstance(mContext);
		mSharePrefrenceUtil = SharePrefrenceUtil.getInstance(mContext);
		mWifiAdminInstance = WifiAdmin.getInstance(mContext);
	}

	public static VolleyNetwork getInstance(Context context) {
		if (mInstance == null)
			mInstance = new VolleyNetwork(context);
		return mInstance;
	}

	public void upLoadRtInfo(JSONObject data, final upLoadListener listener) {
//		Log.i(TAG, "upLoadRtInfo data: " + data.toString());
		JsonObjectRequest request = new JsonObjectRequest(Method.POST,
				upLoadURL, data, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject obj) {
						// Log.i(TAG, "upLoadRtInfo response: " +
						// obj.toString());
						listener.onSuccess();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						// error.getMessage();
						// Log.i(TAG,
						// "upLoadRtInfo VolleyError: " + error.toString());
						listener.onFail();
					}

				});
		// FakeX509TrustManager.allowAllSSL();
		MyVolley.sharedVolley(mContext).getRequestQueue().add(request);
	}

	public void getNearestBusWithLineAndOneStation(final int LineID,
			final int StationID, final requestListener listener) {
		JSONObject params = new JSONObject();
		try {
			params.put("LID", LineID);
			params.put("SID", StationID);
			params.put("T", System.currentTimeMillis() / 1000);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//		Log.i(TAG, "getNearestBusWithLineAndOneStation: " + params.toString());

		JsonObjectRequest request = new JsonObjectRequest(Method.POST,
				lineWithOneStationURL, params,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject obj) {
//						Log.i(TAG,
//								"getNearestBusWithLineAndOneStation onResponse: "
//										+ LineID + " " + StationID + " "
//										+ obj.toString());
						try {
							int status = obj.getInt("s");
							switch (status) {
							case 0:
								listener.onSuccess(obj);
								break;
							case 1:
								listener.onFormatError();
								break;
							case 2:
								String url = obj.getString("u");
								// Log.i(TAG,
								// "getNearestBusWithLineAndOneStation url: "
								// + url);
								listener.onDataNA(url);
								break;
							case 3:
								listener.onNotAccess();
								break;
							default:
								listener.onNetError();
								break;
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
//						Log.e(TAG,
//								"getNearestBusWithLineAndOneStation onErrorResponse: "
//										+ LineID + " " + StationID + " "
//										+ error.getMessage()
//										+ error.getLocalizedMessage());
						// listener.onFormatError();
						listener.onNetError();
					}

				});
		// FakeX509TrustManager.allowAllSSL();
		MyVolley.sharedVolley(mContext).getRequestQueue().add(request);
	}

	public void getAllBusesWithLineAndOneStation(int LineID, int StationID,
			final requestListener listener) {
		JSONObject params = new JSONObject();
		try {
			params.put("LID", LineID);
			params.put("SID", StationID);
			params.put("T", System.currentTimeMillis() / 1000);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//		Log.i(TAG,
//				"getAllBusesWithLineAndOneStation request: "
//						+ params.toString());
		JsonObjectRequest request = new JsonObjectRequest(Method.POST,
				lineWithAllStationURL, params,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject obj) {
//						Log.i(TAG,
//								"getAllBusesWithLineAndOneStation onResponse: "
//										+ obj.toString());
						try {
							int status = obj.getInt("s");
							switch (status) {
							case 0:
								listener.onSuccess(obj);
								break;
							case 1:
								listener.onFormatError();
								break;
							case 2:
								String url = obj.getString("u");
								listener.onDataNA(url);
								break;
							case 3:
								listener.onNotAccess();
								break;
							default:
								listener.onNotAccess();
								break;
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
//						Log.e(TAG,
//								"getAllBusesWithLineAndOneStation onErrorResponse: "
//										+ error.getMessage());
						listener.onFormatError();
					}

				});
		// FakeX509TrustManager.allowAllSSL();
		MyVolley.sharedVolley(mContext).getRequestQueue().add(request);
	}

	public void getDatabaseVersion(final versionListener listener) {

		JsonObjectRequest request = new JsonObjectRequest(dbVersionURL, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							String version = response.getString("V");
//							Log.i(TAG, version);
							listener.onSuccess(version);
						} catch (Exception e) {
							// TODO: handle exception
							listener.onFail();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
//						Log.e(TAG, "VolleyError: " + error.getMessage());
						listener.onFail();
					}
				});
		// FakeX509TrustManager.allowAllSSL();
		MyVolley.sharedVolley(mContext).getRequestQueue().add(request);
		// RequestQueue requestQueue = Volley.newRequestQueue(mContext, new );
		// requestQueue.start();
	}

	public void getUploadInfo(final appVersionListener listener) {
		final String localVersion = mSharePrefrenceUtil.getValue("DBVersion",
				"2.2");
		PackageManager manager;
		PackageInfo info = null;
		manager = mContext.getPackageManager();
		try {
			info = manager.getPackageInfo(mContext.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final String localAppVersion = info.versionName;
		JSONObject json = new JSONObject();
		try {
			json.put("V", localVersion);
			json.put("appV", localAppVersion);
			json.put("Tm", 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			Log.i(TAG, e.getMessage());
			e.printStackTrace();
		}
		Log.i(TAG, "getUploadInfo " + json.toString());
		JsonObjectRequest request = new JsonObjectRequest(Method.POST,
				appVersionURL, json, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject obj) {
//						Log.i(TAG,
//								"getUploadInfo onResponse: " + obj.toString());
						int status = -1;
						try {
							status = obj.getInt("s");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Log.i(TAG, "onResponse: "+obj.toString());
						switch (status) {
						case 0:
							listener.onFail();
							break;
						case 5:
							listener.onSuccess(obj);
							break;
						default:
							listener.onFail();
							break;
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Log.e(TAG,
						// "getUploadInfo onErrorResponse: "
						// + error.getMessage());
						listener.onFail();
					}

				});
		MyVolley.sharedVolley(mContext).getRequestQueue().add(request);
	}

	public void reportMistake(String type, String content,
			final reportListener listener) {

		JSONObject params = new JSONObject();
		try {
			params.put("type", type);
			params.put("content", content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//		Log.i(TAG, "reportMistake request: " + params.toString());
		JsonObjectRequest request = new JsonObjectRequest(Method.POST,
				reportURL, params, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject obj) {
//						Log.i(TAG,
//								"reportMistake onResponse: " + obj.toString());
						listener.onSuccess();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
//						Log.e(TAG,
//								"reportMistake onErrorResponse: "
//										+ error.getMessage());
						listener.onFail();
					}

				});
		MyVolley.sharedVolley(mContext).getRequestQueue().add(request);
	}

	private void postCollectMessage() {
		final List<Map<String, Object>> message = mLocationUtil
				.getPostMessage();
		if (message.size() <= 0) {
//			Log.i(TAG, "message.size()  " + message.size());
			return;
		}
		wifiScanResults = mWifiAdminInstance.getScanWifiMac(wifiSSID.trim());//扫描wifi

		JSONObject json = new JSONObject();
		try {
			if (wifiScanResults.size() != 0) {
				for (ScanResult scanResult : wifiScanResults) {
					JSONArray array = new JSONArray();
					json.put("DeviceID", mLocationUtil.getDeviceID());
					json.put("LineID",lineList);
					json.put("MAC",scanResult.BSSID);
					json.put("data", array);
					Log.i(TAG, "postCollectMessage: mac"+scanResult.BSSID+"LineID"+lineList);
					Log.i(TAG, "postCollectMessage: data"+json);
					for (Map<String, Object> map : message) {
//						Log.i(TAG, "map " + map.toString());
						JSONArray arr = new JSONArray();
						arr.put(map.get("Latitude"));
						arr.put(map.get("Longitude"));
						arr.put(map.get("Speed"));
						arr.put(map.get("Amizuth"));
						arr.put(map.get("TimeInterval"));
//						arr.put(lineList);
						array.put(arr);
					}
					JsonObjectRequest request = new JsonObjectRequest(Method.POST,
							postCollectMessageURL, json,
							new Response.Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject obj) {
//						Log.i(TAG,
//								"postCollectMessage onResponse: "
//										+ obj.toString());
									// listener.onSuccess();
								}
							}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
//						Log.e(TAG, "postCollectMessage onErrorResponse: "
//								+ error.getMessage());
							// listener.onFail();
						}

					});
					MyVolley.sharedVolley(mContext).getRequestQueue().add(request);
				}
			}

			message.clear();

		} catch (Exception e) {
			// TODO Auto-generated catch block
//			Log.i(TAG, e.getMessage());
			e.printStackTrace();
		}
//		Log.i(TAG, "postCollectMessage " + json.toString());

	}

	class postCollectThread extends Thread {
		@Override
		public void run() {
			while (isPostMessage) {
				postCollectMessage();

				try {
					Thread.sleep(30 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void startPostMessage() {
		isPostMessage = true;
		new postCollectThread().start();
	}

	public void stopPostMessage() {
		isPostMessage = false;
	}

	public interface upLoadListener {
		public void onSuccess();

		public void onFail();
	}

	public interface reportListener {
		public void onSuccess();

		public void onFail();
	}

	public interface requestListener {
		public void onSuccess(JSONObject data);// 成功

		public void onNotAccess();// 未开通

		public void onDataNA(String url);// 数据不够新

		public void onFormatError();// 格式错误

		public void onNetError();// 网络错误
	}

	public interface versionListener {
		public void onSuccess(String version);// 成功

		public void onFail();
	}

	public interface appVersionListener {
		public void onSuccess(JSONObject version);// 成功

		public void onFail();
	}
}
