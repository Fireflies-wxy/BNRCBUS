/*package com.bnrc.busapp;

import java.util.Calendar;

import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
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
import com.ab.view.wheel.AbWheelAdapter;
import com.ab.view.wheel.AbWheelView;
import com.bnrc.busapp.R;
import com.bnrc.util.collectwifi.BaseActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class UserSettingView extends BaseActivity {
	private View selectSexView = null;
	private View selectAgeView = null;

	private TextView ageTextView = null;
	private TextView sexTextView = null;

	private View mAvatarView = null;
	private String sexArr[] = { "男", "女" };
	private String ageArr[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
			"21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31",
			"32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42",
			"43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53",
			"54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64",
			"65", "66", "67", "68", "68", "70", "71", "72", "73", "74", "75",
			"76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86",
			"87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97",
			"98", "99", "100" };

	RelativeLayout mAdContainer;
	AdView mAdview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.user_setting_view);
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("用户设置");
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
		String value = MobclickAgent.getConfigParams(UserSettingView.this,
				"open_ad");

		if (value.equals("1")) {
			mAdContainer = (RelativeLayout) findViewById(R.id.adcontainer);
			// Create ad view
			mAdview = new AdView(this, "56OJzfwIuN7tr9LoSs",
					"16TLmHWoAp8diNUdpuAEMYfi");
			SharedPreferences mySharedPreferences = getSharedPreferences(
					"setting", UserSettingView.MODE_PRIVATE);
			String agString = mySharedPreferences.getString("userAge", "20");
			String sexString = mySharedPreferences.getString("userSex", "女");

			if (sexString.equals("女")) {
				mAdview.setUserGender("female");
			} else {
				mAdview.setUserGender("male");
			}
			// mAdview.setKeyword("game");

			Calendar mycalendar = Calendar.getInstance();// ��ȡ����ʱ��
			String curYearString = String
					.valueOf(mycalendar.get(Calendar.YEAR));// ��ȡ���
			int age = Integer.parseInt(agString);
			int birth = Integer.parseInt(curYearString) - age;
			mAdview.setUserBirthdayStr(birth + "-08-08");
			mAdview.setUserPostcode("123456");
			mAdview.setAdEventListener(new AdEventListener() {
				@Override
				public void onAdOverlayPresented(AdView adView) {
					Log.i("DomobSDKDemo", "overlayPresented");
				}

				@Override
				public void onAdOverlayDismissed(AdView adView) {
					Log.i("DomobSDKDemo", "Overrided be dismissed");
				}

				@Override
				public void onAdClicked(AdView arg0) {
					Log.i("DomobSDKDemo", "onDomobAdClicked");
				}

				@Override
				public void onLeaveApplication(AdView arg0) {
					Log.i("DomobSDKDemo", "onDomobLeaveApplication");
				}

				@Override
				public Context onAdRequiresCurrentContext() {
					return UserSettingView.this;
				}

				@Override
				public void onAdFailed(AdView arg0, ErrorCode arg1) {
					Log.i("DomobSDKDemo", "onDomobAdFailed");
				}

				@Override
				public void onEventAdReturned(AdView arg0) {
					Log.i("DomobSDKDemo", "onDomobAdReturned");
				}
			});
			RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
			mAdview.setLayoutParams(layout);
			mAdContainer.addView(mAdview);
		}

		logoView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// �����뾶ѡ����ѡ��Ľ���
		selectSexView = mInflater.inflate(R.layout.choose_one, null);
		// ���Ѿ���ѡ����ѡ��Ľ���
		selectAgeView = mInflater.inflate(R.layout.choose_one, null);

		sexTextView = (TextView) findViewById(R.id.sexTv);
		ageTextView = (TextView) findViewById(R.id.ageTv);
		// ʵ����SharedPreferences���󣨵�һ����
		SharedPreferences mySharedPreferences = getSharedPreferences("setting",
				UserSettingView.MODE_PRIVATE);
		ageTextView = (TextView) findViewById(R.id.ageTv);
		ageTextView.setText(mySharedPreferences.getString("userAge", "20"));

		sexTextView = (TextView) findViewById(R.id.sexTv);
		sexTextView.setText(mySharedPreferences.getString("userSex", "女"));

		sexTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(AbConstant.DIALOGBOTTOM, selectSexView, 40);
			}
		});

		ageTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(AbConstant.DIALOGBOTTOM, selectAgeView, 40);
			}
		});

		initWheelData1(selectSexView, sexTextView);
		initWheelData2(selectAgeView, ageTextView);

	}

	// ��λ��ʾ����
	public void initWheelData1(View mDataView, final TextView textView) {
		final AbWheelView mWheelView1 = (AbWheelView) mDataView
				.findViewById(R.id.wheelView1);
		// mWheelView1.setAdapter(new AbNumericWheelAdapter(40, 190));
		mWheelView1.setAdapter(new AbWheelAdapter() {
			@Override
			public int getMaximumLength() {
				// TODO Auto-generated method stub
				return sexArr.length;
			}

			@Override
			public int getItemsCount() {
				// TODO Auto-generated method stub
				return sexArr.length;
			}

			@Override
			public String getItem(int arg0) {
				// TODO Auto-generated method stub
				return sexArr[arg0];
			}
		});
		// ��ѭ������
		mWheelView1.setCyclic(false);
		// �������
		mWheelView1.setLabel(null);
		// ��ʼ��ʱ��ʾ������
		mWheelView1.setCurrentItem(40);
		mWheelView1.setValueTextSize(35);
		mWheelView1.setLabelTextSize(35);
		mWheelView1.setLabelTextColor(0x80000000);
		mWheelView1.setCenterSelectDrawable(this.getResources().getDrawable(
				R.drawable.wheel_select));

		Button okBtn = (Button) mDataView.findViewById(R.id.okBtn);
		Button cancelBtn = (Button) mDataView.findViewById(R.id.cancelBtn);
		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeDialog(1);
				int index = mWheelView1.getCurrentItem();
				String val = mWheelView1.getAdapter().getItem(index);
				textView.setText(val);
				// ʵ����SharedPreferences���󣨵�һ����
				SharedPreferences mySharedPreferences = getSharedPreferences(
						"setting", UserSettingView.MODE_PRIVATE);
				// ʵ����SharedPreferences.Editor���󣨵ڶ�����
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				// ��putString�ķ�����������
				editor.putString("userSex", val);
				// �ύ��ǰ����
				editor.commit();
			}

		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeDialog(1);
			}

		});
	}

	// ��λ��ʾ����
	public void initWheelData2(View mDataView, final TextView textView) {
		final AbWheelView mWheelView1 = (AbWheelView) mDataView
				.findViewById(R.id.wheelView1);
		// mWheelView1.setAdapter(new AbNumericWheelAdapter(40, 190));
		mWheelView1.setAdapter(new AbWheelAdapter() {
			@Override
			public int getMaximumLength() {
				// TODO Auto-generated method stub
				return ageArr.length;
			}

			@Override
			public int getItemsCount() {
				// TODO Auto-generated method stub
				return ageArr.length;
			}

			@Override
			public String getItem(int arg0) {
				// TODO Auto-generated method stub
				return ageArr[arg0];
			}
		});
		// ��ѭ������
		mWheelView1.setCyclic(false);
		// �������
		mWheelView1.setLabel(null);
		// ��ʼ��ʱ��ʾ������
		mWheelView1.setCurrentItem(40);
		mWheelView1.setValueTextSize(35);
		mWheelView1.setLabelTextSize(35);
		mWheelView1.setLabelTextColor(0x80000000);
		mWheelView1.setCenterSelectDrawable(this.getResources().getDrawable(
				R.drawable.wheel_select));

		Button okBtn = (Button) mDataView.findViewById(R.id.okBtn);
		Button cancelBtn = (Button) mDataView.findViewById(R.id.cancelBtn);
		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeDialog(1);
				int index = mWheelView1.getCurrentItem();
				String val = mWheelView1.getAdapter().getItem(index);
				textView.setText(val);
				// ʵ����SharedPreferences���󣨵�һ����
				SharedPreferences mySharedPreferences = getSharedPreferences(
						"setting", UserSettingView.MODE_PRIVATE);
				// ʵ����SharedPreferences.Editor���󣨵ڶ�����
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				// ��putString�ķ�����������
				editor.putString("userAge", val);
				// �ύ��ǰ����
				editor.commit();
			}

		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeDialog(1);
			}

		});
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // ͳ��ҳ��
		MobclickAgent.onResume(this); // ͳ��ʱ��
		registerReceiver(mWifiReceiver, wifiFilter);
		registerReceiver(mActivityReceiver, activityFilter);

	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // ��֤ onPageEnd ��onPause
													// ֮ǰ����,��Ϊ onPause
													// �лᱣ����Ϣ
		MobclickAgent.onPause(this);
		unregisterReceiver(mWifiReceiver);
		unregisterReceiver(mActivityReceiver);

	}
}
*/