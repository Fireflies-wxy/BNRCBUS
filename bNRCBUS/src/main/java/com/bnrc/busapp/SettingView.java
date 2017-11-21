package com.bnrc.busapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.domob.android.ads.AdEventListener;
import cn.domob.android.ads.AdView;
import cn.domob.android.ads.AdManager.ErrorCode;

import com.ab.activity.AbActivity;
import com.ab.global.AbConstant;

import com.ab.view.titlebar.AbTitleBar;
import com.ab.view.wheel.AbStringWheelAdapter;
import com.ab.view.wheel.AbWheelAdapter;
import com.ab.view.wheel.AbWheelView;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.bnrc.activity.AboutActivity;
import com.bnrc.busapp.R;
import com.bnrc.util.LocationUtil;
import com.bnrc.util.NetAndGpsUtil;
import com.bnrc.util.SharePrefrenceUtil;
import com.bnrc.util.collectwifi.BaseActivity;
import com.bnrc.util.collectwifi.Constants;
import com.bnrc.util.collectwifi.ScanService;
import com.bnrc.util.collectwifi.ServiceUtils;
import com.bnrc.util.collectwifi.WifiReceiver;

import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
import com.zf.iosdialog.widget.ActionSheetDialog.onListener;

public class SettingView extends BaseActivity {
	private View selectSearchRView = null;
	private View selectAlertRView = null;
	private View selectBatteryModeView = null;
	private View refreshModeView = null;

	private TextView searchRTextView = null;
	private TextView alertRTextView = null;
	private TextView batteryModeTextView = null;
	// private TextView acceptAlertTextView = null;
	private TextView userSetView = null;
	private TextView shareAppTextView = null;
	private TextView feedbackTextView = null;
	private TextView aboutTextView = null;
	private TextView refreshTextView = null;
	// 收集wifi的相关设置
	private TextView tv_scanmethod, tv_scanap, tv_scantime;
	private View mDataViewRadius = null, mDataViewAlertDistance = null,
			mDataViewBattery = null;
	private View mDataViewScanMethod = null, mDataViewScanAp = null,
			mDataViewScanTime = null, mDataViewScanPrecision = null;
	private SharePrefrenceUtil mSharePrefrenceUtil;
	private LocationUtil mLocationUtil;
	private NetAndGpsUtil mNetAndGpsUtil;
	private String[] searchRadius;
	private String[] alertRadius;
	private String[] refreshFrequency;
	private String[] batteryArray;
	RelativeLayout mAdContainer;
	AdView mAdview;
	private AbTitleBar mAbTitleBar;
	private UMSocialService mShareController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.setting_view);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("系统设置");
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleBarGravity(Gravity.CENTER, Gravity.CENTER);
		initbase();
		MobclickAgent.updateOnlineConfig(this);
		MobclickAgent
				.setOnlineConfigureListener(new UmengOnlineConfigureListener() {
					@Override
					public void onDataReceived(JSONObject data) {
					}
				});
		String value = MobclickAgent.getConfigParams(SettingView.this,
				"open_ad");

		// if (value.equals("1")) {
		// mAdContainer = (RelativeLayout) findViewById(R.id.adcontainer);
		// // Create ad view
		// mAdview = new AdView(this, "56OJzfwIuN7tr9LoSs",
		// "16TLmHWoAp8diNUdpuAEMYfi");
		// SharedPreferences mySharedPreferences = getSharedPreferences(
		// "setting", UserSettingView.MODE_PRIVATE);
		// String agString = mySharedPreferences.getString("userAge", "20");
		// String sexString = mySharedPreferences.getString("userSex", "女");
		//
		// if (sexString.equals("女")) {
		// mAdview.setUserGender("female");
		// } else {
		// mAdview.setUserGender("male");
		// }
		// // mAdview.setKeyword("game");
		//
		// Calendar mycalendar = Calendar.getInstance();// ��ȡ����ʱ��
		// String curYearString = String
		// .valueOf(mycalendar.get(Calendar.YEAR));// ��ȡ���
		// int age = Integer.parseInt(agString);
		// int birth = Integer.parseInt(curYearString) - age;
		// mAdview.setUserBirthdayStr(birth + "-08-08");
		// mAdview.setUserPostcode("123456");
		// mAdview.setAdEventListener(new AdEventListener() {
		// @Override
		// public void onAdOverlayPresented(AdView adView) {
		// Log.i("DomobSDKDemo", "overlayPresented");
		// }
		//
		// @Override
		// public void onAdOverlayDismissed(AdView adView) {
		// Log.i("DomobSDKDemo", "Overrided be dismissed");
		// }
		//
		// @Override
		// public void onAdClicked(AdView arg0) {
		// Log.i("DomobSDKDemo", "onDomobAdClicked");
		// }
		//
		// @Override
		// public void onLeaveApplication(AdView arg0) {
		// Log.i("DomobSDKDemo", "onDomobLeaveApplication");
		// }
		//
		// @Override
		// public Context onAdRequiresCurrentContext() {
		// return SettingView.this;
		// }
		//
		// @Override
		// public void onAdFailed(AdView arg0, ErrorCode arg1) {
		// Log.i("DomobSDKDemo", "onDomobAdFailed");
		// }
		//
		// @Override
		// public void onEventAdReturned(AdView arg0) {
		// Log.i("DomobSDKDemo", "onDomobAdReturned");
		// }
		// });
		// RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
		// mAdview.setLayoutParams(layout);
		// mAdContainer.addView(mAdview);
		// }

		// logoView.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// finish();
		// }
		// });
		mSharePrefrenceUtil = SharePrefrenceUtil.getInstance(this
				.getApplicationContext());
		mLocationUtil = LocationUtil.getInstance(this.getApplicationContext());
		mNetAndGpsUtil = NetAndGpsUtil
				.getInstance(this.getApplicationContext());
		initDataArray();
		selectSearchRView = mInflater.inflate(R.layout.choose_one, null);
		selectAlertRView = mInflater.inflate(R.layout.choose_one, null);
		selectBatteryModeView = mInflater.inflate(R.layout.choose_one, null);
		refreshModeView = mInflater.inflate(R.layout.choose_one, null);
		searchRTextView = (TextView) findViewById(R.id.searchRTv);
		alertRTextView = (TextView) findViewById(R.id.alertRTv);
		batteryModeTextView = (TextView) findViewById(R.id.batteryModeTv);
		refreshTextView = (TextView) findViewById(R.id.refreshModeTv);
		userSetView = (TextView) findViewById(R.id.userSetTv);
		shareAppTextView = (TextView) findViewById(R.id.shareAppTv);
		feedbackTextView = (TextView) findViewById(R.id.feedbackTv);
		aboutTextView = (TextView) findViewById(R.id.aboutTv);

		String searchradius = mSharePrefrenceUtil.getValue("searchRadius",
				"500米");
		String alertradius = mSharePrefrenceUtil
				.getValue("alertRadius", "500米");
		String battery = mSharePrefrenceUtil.getValue("battery", "智能切换");
		String refreshfrequency = mSharePrefrenceUtil.getValue(
				"refreshFrequency", "30秒");

		searchRTextView.setText(searchradius);
		alertRTextView.setText(alertradius);
		batteryModeTextView.setText(battery);
		refreshTextView.setText(refreshfrequency);

		// userSetView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // Intent intent = null;
		// Intent intent = new Intent(SettingView.this,
		// UserSettingView.class);
		// // ������ͼ
		// startActivity(intent);
		// }
		// });

		searchRTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// showDialog(AbConstant.DIALOGBOTTOM, selectSearchRView, 40);
				showSelectDialog("设置搜索半径", searchRadius, "searchRadius",
						new onChangeText() {

							@Override
							public void onChange(String text) {
								// TODO Auto-generated method stub
								searchRTextView.setText(text);
							}

							@Override
							public void onCancel() {
								// TODO Auto-generated method stub

							}
						});
			}
		});

		alertRTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// showDialog(AbConstant.DIALOGBOTTOM, selectAlertRView, 40);
				showSelectDialog("设置提醒半径", alertRadius, "alertRadius",
						new onChangeText() {

							@Override
							public void onChange(String text) {
								// TODO Auto-generated method stub
								alertRTextView.setText(text);
							}

							@Override
							public void onCancel() {
								// TODO Auto-generated method stub

							}
						});

			}
		});

		batteryModeTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// showDialog(AbConstant.DIALOGBOTTOM, selectBatteryModeView,
				// 40);
				showSelectDialog("设置电量模式", batteryArray, "battery",
						new onChangeText() {

							@Override
							public void onChange(String text) {
								// TODO Auto-generated method stub
								batteryModeTextView.setText(text);
							}

							@Override
							public void onCancel() {
								// TODO Auto-generated method stub

							}
						});

			}

		});

		refreshTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// showDialog(AbConstant.DIALOGBOTTOM, refreshModeView, 40);
				showSelectDialog("设置刷新频率", refreshFrequency,
						"refreshFrequency", new onChangeText() {

							@Override
							public void onChange(String text) {
								// TODO Auto-generated method stub
								refreshTextView.setText(text);
							}

							@Override
							public void onCancel() {
								// TODO Auto-generated method stub

							}
						});

			}

		});

		feedbackTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				FeedbackAgent agent = new FeedbackAgent(SettingView.this);
				agent.startFeedbackActivity();
				agent.sync();
			}
		});

		aboutTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingView.this,
						AboutActivity.class);
				startActivity(intent);
			}
		});

		// ����������Activity��������³�Ա����
		mShareController = UMServiceFactory
				.getUMSocialService("com.umeng.share");
		// ���÷�������
		mShareController
				.setShareContent("我正在使用\"等车来\"查公交，太方便啦，还可以看到站信息，你也试试吧？");
		// ���÷���ͼƬ������2Ϊ����ͼƬ����Դ����
		// mController.setShareMedia(new UMImage(getActivity(),
		// R.drawable.icon));

		// ���΢��ƽ̨
		UMWXHandler wxHandler = new UMWXHandler(this, "wxea7896d63d6b0b42",
				"4991e5b714c986c94798cb15c271b8fc");
		wxHandler.addToSocialSDK();
		// ���΢������Ȧ
		UMWXHandler wxCircleHandler = new UMWXHandler(this,
				"wxea7896d63d6b0b42", "4991e5b714c986c94798cb15c271b8fc");
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// ����1Ϊ��ǰActivity������2Ϊ��������QQ���������APP
		// ID������3Ϊ��������QQ���������APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1105239657",
				"luT0kCy07n0rHB0E");
		qqSsoHandler.addToSocialSDK();

		// ����1Ϊ��ǰActivity������2Ϊ��������QQ���������APP
		// ID������3Ϊ��������QQ���������APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
				"1105239657", "luT0kCy07n0rHB0E");
		qZoneSsoHandler.addToSocialSDK();

		mShareController.getConfig().removePlatform(SHARE_MEDIA.TENCENT,
				SHARE_MEDIA.WEIXIN_CIRCLE);
		shareAppTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// �Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ
				mShareController.openShare(SettingView.this, false);

			}
		});

		// ��������SSO handler
		mShareController.getConfig().setSsoHandler(new SinaSsoHandler());
		initbase();
	}

	private void initDataArray() {
		searchRadius = getResources().getStringArray(
				R.array.Setting_searchRadius);
		alertRadius = getResources()
				.getStringArray(R.array.Setting_alertRadius);
		refreshFrequency = getResources().getStringArray(
				R.array.Setting_refreshFrequency);
		batteryArray = getResources().getStringArray(R.array.Setting_battery);
	}

	private ActionSheetDialog mDialog;

	private void showSelectDialog(String title, String[] array,
			final String key, final onChangeText listener) {
		if (mDialog == null) {
			mDialog = new ActionSheetDialog(this).builder();
			mDialog.setTitle(title);
			for (final String str : array) {
				mDialog = mDialog.addSheetItem(str, SheetItemColor.Blue,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								mSharePrefrenceUtil.setKey(key, str);
								if (key.equalsIgnoreCase("battery")) {
									mLocationUtil.initLocation();
								}
								listener.onChange(str);
								mDialog = null;
							}
						});
			}
			mDialog.setOnDismissListener(new onListener() {

				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					mDialog = null;
				}

			});
		}
		mDialog.show();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // ͳ��ҳ��
		MobclickAgent.onResume(this); // ͳ��ʱ��
		// registerReceiver(mWifiReceiver, wifiFilter);
		// registerReceiver(mActivityReceiver, activityFilter);

	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // ��֤ onPageEnd ��onPause //
													// ֮ǰ����,��Ϊ onPause
													// �лᱣ����Ϣ
		MobclickAgent.onPause(this);
		// unregisterReceiver(mWifiReceiver);
		// unregisterReceiver(mActivityReceiver);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mShareController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
}

interface onChangeText {
	void onChange(String text);

	void onCancel();
}