/*package com.bnrc.util.collectwifi;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class Thread_send_json1 extends Thread {
	private static final String TAG = "Thread_send_json";
	private List<Map<String, String>> mInfo;
	private Handler mHandler;
	private String mUrl = "http://10.108.104.217:8080/NewOne/JsonServlet";
	private CollectWifiDBHelper mCollectWifiDBHelper;

	public Thread_send_json(Context pContext, Handler pHandler) {
		this.mHandler = pHandler;
		mCollectWifiDBHelper = CollectWifiDBHelper.getInstance(pContext);
		Log.i(TAG, "url is " + mUrl);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		mInfo = mCollectWifiDBHelper.FindScanData();
		if (mInfo.size() > 0) {
			try {
				// 请求数据
				HttpClient hClient = new DefaultHttpClient();
				HttpPost request = new HttpPost(mUrl);
				JSONArray jArray = new JSONArray();
				for (Map<String, String> map : mInfo) {
					// 请求json报文
					// 先封装一个 JSON 对象
					JSONObject param = new JSONObject();
					try {
						param.put("Time", map.get("时间"));
						param.put("Lat", map.get("纬度"));
						param.put("Lon", map.get("经度"));
						param.put("SSID", map.get("SSID"));
						param.put("MAC", map.get("MAC"));
						param.put("Level", map.get("Level"));
						param.put("LocType", map.get("LocType"));
						param.put("LocRadius", map.get("LocRadius"));
						param.put("LocSpeed", map.get("LocSpeed"));
						jArray.put(param);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// 绑定到请求 Entry
				StringEntity se = new StringEntity(jArray.toString(), "UTF-8");
				request.setEntity(se);
				// 发送请求
				HttpResponse response = hClient.execute(request);
				Log.i(TAG, "response.getStatusCode: "
						+ response.getStatusLine().getStatusCode());

				if (response.getStatusLine().getStatusCode() == 200) {

					String responseMsg = EntityUtils.toString(response
							.getEntity());
					Log.i(TAG, "responseMsg is " + responseMsg);
					Message msg = new Message();
					Bundle bundle = new Bundle();// 存放数据
					bundle.putString("responseMsg", responseMsg);
					msg.setData(bundle);
					msg.what = 0x11;
					mHandler.sendMessage(msg); // 向Handler发送消息,更新UI
				}
				if (hClient != null) {
					hClient.getConnectionManager().shutdown();
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
*/