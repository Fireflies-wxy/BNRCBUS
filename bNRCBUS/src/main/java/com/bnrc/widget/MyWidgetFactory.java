package com.bnrc.widget;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.bnrc.busapp.MyCipher;
import com.bnrc.busapp.R;
import com.bnrc.ui.rtBus.Child;
import com.bnrc.ui.rtBus.Group;
import com.bnrc.util.PCUserDataDBHelper;
import com.bnrc.util.UserDataDBHelper;
import com.bnrc.util.VolleyNetwork;
import com.bnrc.util.VolleyNetwork.requestListener;
import com.bnrc.util.collectwifi.Constants;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;

public class MyWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
	private static final String TAG = MyWidgetFactory.class.getSimpleName();
	private Context mContext;
	private VolleyNetwork mVolleyNetwork;
	private PCUserDataDBHelper mUserDataDBHelper;
	private List<Child> mChildren;

	// 构造
	public MyWidgetFactory(Context context, Intent intent) {
		log("MyWidgetFactory");
		mContext = context;
		mVolleyNetwork = VolleyNetwork.getInstance(mContext
				.getApplicationContext());
		mUserDataDBHelper = PCUserDataDBHelper.getInstance(mContext
				.getApplicationContext());
	}

	@Override
	public int getCount() {
		log("getCount");
		if (mChildren == null)
			return 0;
		return mChildren.size();
	}

	@Override
	public long getItemId(int position) {
		log("getItemId");
		return position;
	}

	// 在调用getViewAt的过程中，显示一个LoadingView。
	// 如果return null，那么将会有一个默认的loadingView
	@Override
	public RemoteViews getLoadingView() {
		log("getLoadingView");
		return null;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		log("getViewAt, position=" + position);
		if (position < 0 || position >= getCount()) {
			return null;
		}
		if (mChildren == null || mChildren.size() == 0)
			return null;
		RemoteViews views = new RemoteViews(mContext.getPackageName(),
				R.layout.layout_widget_item);
		Child child = mChildren.get(position);
		if (child != null) {
			views.setTextViewText(R.id.tv_buslineName, child.getLineName());
			views.setTextViewText(R.id.tv_destination, child.getEndStation());
			views.setTextViewText(R.id.tv_destination2, child.getStationName());
			Log.i("lalalala", "真的！ ");
			if (child.getRtInfo() != null) {
				views.setTextViewText(R.id.tv_info, Html.fromHtml((child
						.getRtInfo().get("itemsText").toString())));
				Log.i("lalalala", "真的！ "
						+ child.getRtInfo().get("itemsText").toString());

			}
		}
		return views;
	}

	@Override
	public int getViewTypeCount() {
		log("getViewTypeCount");
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		log("hasStableIds");
		return true;
	}

	@Override
	public void onCreate() {
		log("onCreate");
	}

	@Override
	public void onDataSetChanged() {
		if (mChildren != null)
			log("onDataSetChanged: " + mChildren.size());
		else {
			log("onDataSetChanged: " + "mChildre==null");

		}
		if (mChildren == null)
			loadDataBase();
		else
			getServerInfo(mChildren);
	}

	@Override
	public void onDestroy() {
		log("onDestroy");
	}

	private void log(String log) {
		Log.d(TAG, log);
	}

	private DownloadTask mTask;

	private void loadDataBase() {
		if (mTask != null)
			mTask.cancel(true);
		mTask = new DownloadTask();
		mTask.execute();

	}

	private void getServerInfo(List<Child> children) {
		if (children == null || children.size() == 0)
			return;
		for (final Child child : children) {
			final int LineID = child.getLineID();
			int StationID = child.getStationID();
			Log.i(TAG, "LineID: " + LineID + " ; " + "StationID: " + StationID);
			mVolleyNetwork.getNearestBusWithLineAndOneStation(LineID,
					StationID, new requestListener() {

						@Override
						public void onSuccess(JSONObject data) {
							// TODO Auto-generated method stub
							try {
								JSONArray arr = null;
								if (data.toString().indexOf("[") > 0) {
									arr = data.getJSONArray("dt");
								} else {
									JSONObject busJsonObject = data
											.getJSONObject("dt");
									arr = new JSONArray("["
											+ busJsonObject.toString() + "]");
								}
								if (arr != null && arr.length() > 0) {
									Log.i(TAG, "ARR!=NULL");
									Map<String, String> map = new HashMap<String, String>();
									JSONObject json = arr.getJSONObject(arr
											.length() - 1);
									int distance = json.getInt("Sd");
									int time = json.getInt("St");
									int station = json.getInt("bn");
									if (time <= 10) {
										map.put("itemsText", "已经到站");
									} else {
										map.put("itemsText", station + " 站");
									}
									child.setRtInfo(map);
									Log.i(TAG, child.getLineName()
											+ " distance: " + distance + " ; "
											+ "time: " + time);
									if (child != null) {
										Log.i(TAG, map.get("itemsText"));
										child.setRtRank(Child.ARRIVING);
										child.setDataChanged(true);
									}
								} else {
									Map<String, String> showText = new HashMap<String, String>();
									showText.put("itemsText",
											"<font color=\"black\">" + "暂无信息"
													+ "</font>");
									if (child != null) {
										Log.i(TAG, showText.get("itemsText"));
										child.setRtInfo(showText);
										child.setRtRank(Child.NOTYET);
										child.setDataChanged(true);
									}
								}
								// sortGroup();
								// mWorkConcernAdapter
								// .notifyDataSetChanged();

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void onNotAccess() {
							// TODO Auto-generated method stub
							Map<String, String> showText = new HashMap<String, String>();
							showText.put("itemsText", "<font color=\"grey\">"
									+ "未开通" + "</font>");
							if (child != null) {
								Log.i(TAG, showText.get("itemsText"));
								child.setRtInfo(showText);
								child.setRtRank(Child.NOTEXIST);
							}
							sortChildren();
							// mWorkConcernAdapter.notifyDataSetChanged();
							Log.i(TAG, "未开通");
						}

						@Override
						public void onFormatError() {
							// TODO Auto-generated method stub
							Log.i(TAG, "数据格式不对");
							if (child.getOfflineID() <= 0) {
								Map<String, String> showText = new HashMap<String, String>();
								showText.put("itemsText",
										"<font color=\"grey\">" + "未开通"
												+ "</font>");
								if (child != null) {
									Log.i(TAG, showText.get("itemsText"));
									child.setRtInfo(showText);
									child.setRtRank(Child.NOTEXIST);
									child.setDataChanged(true);

								}
								sortChildren();
								// mWorkConcernAdapter
								// .notifyDataSetChanged();

							} else {
								try {
									getRtInfo(child);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onDataNA(String url) {
							// TODO Auto-generated method stub
							getRtInfo(child, url);
							Log.i(TAG, "数据过旧");
						}

						@Override
						public void onNetError() {
							// TODO Auto-generated method stub
							Map<String, String> showText = new HashMap<String, String>();
							showText.put("itemsText", "<font color=\"grey\">"
									+ "网络不佳" + "</font>");
							if (child != null) {
								child.setRtInfo(showText);
								child.setRtRank(Child.NOTYET);
								child.setDataChanged(true);
							}
							sortChildren();
						}
					});
		}

	}

	private void getRtInfo(final Child child) throws JSONException,
			UnsupportedEncodingException {
		final int sequence = child.getSequence();
		int offlineID = child.getOfflineID();
		String Url = "http://bjgj.aibang.com:8899/bus.php?city="
				+ URLEncoder.encode("北京", "utf-8") + "&id=" + offlineID
				+ "&no=" + sequence + "&type=2&encrypt=1&versionid=2";
		Log.i("OKHTTP", "url " + Url);// 创建okHttpClient对象
		OkHttpClient mOkHttpClient = new OkHttpClient();
		// 创建一个Request
		final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
				.url(Url).build();
		// new call
		Call call = mOkHttpClient.newCall(request);
		// 请求加入调度
		call.enqueue(new Callback() {

			@Override
			public void onFailure(com.squareup.okhttp.Request arg0,
					IOException arg1) {
				// TODO Auto-generated method stub
				// Log.i(TAG, "onFailure: " + arg0.body().toString());
				if (child != null) {
					Map<String, String> showText = new HashMap<String, String>();
					showText.put("itemsText", "暂无信息");
					// 到站
					child.setRtInfo(showText);
					child.setDataChanged(true);
					child.setRtRank(Child.NOTYET);
				}
				// mHandler.post(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// mNearAdapter.notifyDataSetChanged();
				// }
				// });
			}

			@Override
			public void onResponse(com.squareup.okhttp.Response arg0)
					throws IOException {
				// TODO Auto-generated method stub
				try {
					String response = arg0.body().string();
					Log.i("OKHTTP", "response " + response);
					JSONObject responseJson = XML.toJSONObject(response);
					JSONObject rootJson = responseJson.getJSONObject("root");
					int status = rootJson.getInt("status");
					if (status != 200) {
						if (child != null) {
							Map<String, String> showText = new HashMap<String, String>();
							showText.put("itemsText", "暂无信息");
							// 到站
							child.setRtInfo(showText);
							child.setDataChanged(true);
							child.setRtRank(Child.NOTYET);
						}
						sortChildren();
						// mHandler.post(new Runnable() {
						//
						// @Override
						// public void run() {
						// // TODO Auto-generated method stub
						// mNearAdapter.notifyDataSetChanged();
						// }
						// });
						return;
					}
					JSONObject dataJson = rootJson.getJSONObject("data");
					JSONArray busJsonArray = null;
					if (dataJson.toString().indexOf("[") > 0) {
						busJsonArray = (JSONArray) dataJson.get("bus");
						busJsonArray = dataJson.getJSONArray("bus");
					} else {
						JSONObject busJsonObject = dataJson
								.getJSONObject("bus");
						busJsonArray = new JSONArray("["
								+ busJsonObject.toString() + "]");
					}
					dealRtInfo(busJsonArray, child);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					if (child != null) {
						Map<String, String> showText = new HashMap<String, String>();
						showText.put("itemsText", "暂无信息");
						// 到站
						child.setRtInfo(showText);
						child.setDataChanged(true);
						child.setRtRank(Child.NOTYET);
					}
					// mHandler.post(new Runnable() {
					//
					// @Override
					// public void run() {
					// // TODO Auto-generated method stub
					// mNearAdapter.notifyDataSetChanged();
					// }
					// });
					e.printStackTrace();
				}
			}
		});
	}

	private void getRtInfo(final Child child, String url) {
		final int sequence = child.getSequence();
		Log.i("Volley", "url:" + url);
		// 创建okHttpClient对象
		OkHttpClient mOkHttpClient = new OkHttpClient();
		// 创建一个Request
		final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
				.url(url).build();
		// new call
		Call call = mOkHttpClient.newCall(request);
		// 请求加入调度
		call.enqueue(new Callback() {

			@Override
			public void onFailure(com.squareup.okhttp.Request arg0,
					IOException arg1) {
				// TODO Auto-generated method stub
				// Log.i(TAG, "onFailure: " + arg0.body().toString());
				if (child != null) {
					Map<String, String> showText = new HashMap<String, String>();
					showText.put("itemsText", "网络不佳");
					// 到站
					child.setRtInfo(showText);
					child.setRtRank(Child.NOTYET);
					child.setDataChanged(true);
				}
				// mHandler.post(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// mNearAdapter.notifyDataSetChanged();
				// }
				// });
			}

			@Override
			public void onResponse(com.squareup.okhttp.Response arg0)
					throws IOException {
				// TODO Auto-generated method stub
				try {
					String response = arg0.body().string();
					Log.i(TAG, "onResponse: " + response);
					Log.i("OKHTTP", "response " + response);
					JSONObject responseJson = XML.toJSONObject(response);
					JSONObject rootJson = responseJson.getJSONObject("root");
					int status = rootJson.getInt("status");
					if (status != 200) {
						// Log.i(TAG, child.getBuslineFullName()
						// + " 暂无实时公交信息");
						if (child != null) {
							Map<String, String> showText = new HashMap<String, String>();
							showText.put("itemsText", "网络不佳");
							// 到站
							child.setRtInfo(showText);
							child.setDataChanged(true);

							child.setRtRank(Child.NOTYET);
						}
						sortChildren();
						// mHandler.post(new Runnable() {
						//
						// @Override
						// public void run() {
						// // TODO Auto-generated method stub
						// mNearAdapter.notifyDataSetChanged();
						// }
						// });
						return;
					}
					JSONObject dataJson = rootJson.getJSONObject("data");
					JSONArray busJsonArray = null;
					if (dataJson.toString().indexOf("[") > 0) {
						busJsonArray = (JSONArray) dataJson.get("bus");
						busJsonArray = dataJson.getJSONArray("bus");
					} else {
						JSONObject busJsonObject = dataJson
								.getJSONObject("bus");
						busJsonArray = new JSONArray("["
								+ busJsonObject.toString() + "]");
					}
					dealRtInfo(busJsonArray, child);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					if (child != null) {
						Map<String, String> showText = new HashMap<String, String>();
						showText.put("itemsText", "网络不佳");
						// 到站
						child.setRtInfo(showText);
						child.setDataChanged(true);

						child.setRtRank(Child.NOTYET);
					}
					sortChildren();
					// mHandler.post(new Runnable() {
					//
					// @Override
					// public void run() {
					// // TODO Auto-generated method stub
					// mNearAdapter.notifyDataSetChanged();
					// }
					// });
					e.printStackTrace();
				}
			}
		});

	}

	private void dealRtInfo(JSONArray json, final Child child) {
		Map<String, String> showText = new HashMap<String, String>();
		showText.put("itemsText", "暂无信息");
		int rank = Child.NOTYET;
		int sequence = child.getSequence();
		try {
			try {
				int count = json.length();
				Log.i(TAG, "busJsonArray_count: " + count);
				int max = 0;
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				// Map<String, Object> map = new HashMap<String, Object>();
				for (int j = 0; j < count; j++) {
					JSONObject busJson = (JSONObject) json.get(j);
					MyCipher mCiper = new MyCipher("aibang"
							+ busJson.getString("gt"));

					String nextStationName = mCiper.decrypt(busJson
							.getString("ns"));// nextStationName
					int nextStationNum = Integer.parseInt(mCiper
							.decrypt(busJson.getString("nsn")));// nextStationNum
					int id = busJson.getInt("id");
					String nextStationDistance = busJson.getString("nsd");// nextStationDistance
					String nextStationTime = busJson.getString("nst");// nextStationTime

					String stationDistance = mCiper.decrypt(busJson
							.getString("sd"));// stationDistance
					String stationArrivingTime = mCiper.decrypt(busJson
							.getString("st"));
					String st_c = null;
					if (isNumeric(stationArrivingTime))
						// st_c = TimeStampToDate(Long.parseLong(st),
						// "HH:mm");// station_arriving_time
						st_c = String.valueOf(TimeStampToDelTime(Long
								.parseLong(stationArrivingTime)));// station_arriving_time
					else
						st_c = "-1";
					String x = mCiper.decrypt(busJson.getString("x"));
					String y = mCiper.decrypt(busJson.getString("y"));
					Log.i(TAG,
							"next_station_name: " + nextStationName + "\n"
									+ "next_station_num: " + nextStationNum
									+ "\n" + "next_station_distance: "
									+ nextStationDistance + "\n"
									+ "next_station_arriving_time: "
									+ nextStationTime + "\n"
									+ "station_distance: " + stationDistance
									+ "\n" + "station_arriving_time: "
									+ stationArrivingTime + "   " + st_c + "\n"
									+ " currentTime "
									+ System.currentTimeMillis());
					if (nextStationNum <= sequence) {
						// map.clear();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("nextStationNum", nextStationNum);
						map.put("stationArrivingTime", stationArrivingTime);
						map.put("stationDistance", stationDistance);
						max = nextStationNum;
						list.add(map);
					}
				}
				if (list.size() <= 0) {

					// 起点站
					if (sequence == 1) {
						showText.put("itemsText", "起点站");
						rank = Child.FIRSTSTATION;
					} // 未开通
					else {
						showText.put("itemsText", "暂无信息");
						rank = Child.NOTYET;
					}
					throw new JSONException("暂无信息");

				} else {
					Collections.sort(list, comparatorRt);
					int size = list.size() > 3 ? 3 : list.size();
					for (int i = 0; i < size; i++) {
						Map<String, ?> map = list.get(i);
						int nextStationNum = (Integer) map
								.get("nextStationNum");
						String stationArrivingTime = map.get(
								"stationArrivingTime").toString();
						String stationDistance = map.get("stationDistance")
								.toString();
						if (nextStationNum <= sequence) {
							if (isNumeric(stationArrivingTime)) {
								if (nextStationNum == sequence) {
									// 已到站
									if (Integer.parseInt(stationArrivingTime) < 10) {
										showText.put("itemsText",
												"<font color=\"red\">" + "已到站"
														+ "</font>");
										// tmpMap.put("station", "已经");
										// tmpMap.put("time", "到站");
										rank = Child.ARRIVING;
									}
									// 即将到站
									else if (Integer.parseInt(stationDistance) < 10) {
										showText.put("itemsText",
												"<font color=\"red\">" + "即将到站"
														+ "</font>");
										// tmpMap.put("station", "即将");
										// tmpMap.put("time", "到站");
										rank = Child.SOON;
									} else {
										int nstime = TimeStampToDelTime(Long
												.parseLong(stationArrivingTime));// 计算还有几分钟
										if (nstime <= 0) {
											showText.put("itemsText",
													"<font color=\"red\">"
															+ "即将到站"
															+ "</font>");
											// tmpMap.put("station", "即将");
											// tmpMap.put("time", "到站");
											rank = Child.SOON;
										} else {
											showText.put("itemsText",
													"<font color=\"red\">"
															+ nstime + " 分钟"
															+ "</font>");
											// tmpMap.put("station", 1 + " 站");
											// tmpMap.put("time", nstime +
											// " 分");
											rank = Child.ONTHEWAY;
										}
									}
								} else {
									int nstime = TimeStampToDelTime(Long
											.parseLong(stationArrivingTime));// 计算还有几分钟
									if (nstime <= 0) {
										showText.put("itemsText",
												"<font color=\"red\">" + "即将到站"
														+ "</font>");
										// tmpMap.put("station", "即将");
										// tmpMap.put("time", "到站");
										rank = Child.SOON;
									} else {
										showText.put("itemsText",
												"<font color=\"red\">" + nstime
														+ " 分钟" + "</font>");
										// tmpMap.put("station", (sequence
										// - nextStationNum + 1)
										// + " 站");
										// tmpMap.put("time", nstime + " 分");
										rank = Child.ONTHEWAY;
									}
								}
							}
						}
						// else {
						// showText.put("itemsText", "暂无信息");
						// rank = Child.NOTYET;
						// }
					}
				}
			} catch (SQLException sqle) {
				throw sqle;
			}

		} catch (JSONException e) {
			Log.e("JSON exception", e.getMessage());
			e.printStackTrace();
		} finally {
			if (child != null) {
				child.setRtInfo(showText);
				child.setDataChanged(true);
				child.setRtRank(rank);
			}
			if (child.getRtInfo() != null)
				Log.i("lalalala", child.getRtInfo().get("itemsText").toString());

			sortChildren();
			// mHandler.post(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// mWorkConcernAdapter.notifyDataSetChanged();
			// }
			// });
		}
	}

	class DownloadTask extends AsyncTask<Integer, Integer, List<Child>> {
		// 后面尖括号内分别是参数（线程休息时间），进度(publishProgress用到)，返回值 类型
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			Log.d(TAG, "onPreExecute");
			super.onPreExecute();
		}

		@Override
		protected List<Child> doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			Log.d(TAG, "doInBackground");
			return getFavStationsAndBuslines();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onProgressUpdate");
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(List<Child> result) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onPostExecute");
			if (result != null && result.size() > 0) {
				mChildren = result;
				Log.i(TAG, mChildren.get(0).getLineName());
				getServerInfo(mChildren);
			}
		}

	}

	public List<Child> getFavStationsAndBuslines() {
		Log.i(TAG, "getFavStationsAndBuslines");
		mChildren = mUserDataDBHelper.acquireFavInfoWithLocation(mChildren);
		return mChildren;
	}

	private void sortChildren() {
		if (mChildren != null)
			Collections.sort(mChildren, comparator);
	}

	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("-?[0-9]+.*[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public int TimeStampToDelTime(Long timestampString) {
		if (timestampString < 0)
			return (int) 0;
		double delTime = (timestampString * 1000 - System.currentTimeMillis()) / 1000 / 60;
		return (int) Math.ceil(delTime);
	}

	Comparator<Map<String, ?>> comparatorRt = new Comparator<Map<String, ?>>() {
		public int compare(Map<String, ?> c1, Map<String, ?> c2) {

			int n1 = Integer.parseInt(c1.get("nextStationNum").toString());
			int n2 = Integer.parseInt(c2.get("nextStationNum").toString());
			if (n1 > n2)
				return -1;
			else if (n1 < n2)
				return 1;
			return 0;

		}
	};
	Comparator<Child> comparator = new Comparator<Child>() {
		public int compare(Child c1, Child c2) {

			if (c1 == null && c2 == null)
				return 0;
			else if (c1 == null)
				return -1;
			else if (c2 == null)
				return 1;
			int rank1 = c1.getRtRank();
			int rank2 = c2.getRtRank();
			if (rank1 > rank2)
				return -1;
			else if (rank1 < rank2)
				return 1;
			else
				return 0;

		}
	};

}