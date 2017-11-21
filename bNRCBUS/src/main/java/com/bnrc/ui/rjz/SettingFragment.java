package com.bnrc.ui.rjz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.Inflater;

import org.json.JSONObject;

import cn.domob.android.ads.AdEventListener;
import cn.domob.android.ads.AdView;
import cn.domob.android.ads.AdManager.ErrorCode;

import com.ab.global.AbConstant;
import com.ab.view.wheel.AbStringWheelAdapter;
import com.ab.view.wheel.AbWheelAdapter;
import com.ab.view.wheel.AbWheelView;
import com.bnrc.activity.AboutActivity;
import com.bnrc.busapp.R;
import com.bnrc.busapp.SettingView;
import com.bnrc.util.LocationUtil;
import com.bnrc.util.collectwifi.Constants;
import com.bnrc.util.collectwifi.ScanService;
import com.bnrc.util.collectwifi.ServiceUtils;
import com.bnrc.util.collectwifi.WifiReceiver;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingFragment extends BaseFragment {
	private Context mContext;
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
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	private String searchRArr[] = { "600米", "700米", "800米", "900米", "1000米",
			"1100米", "1200米", "1300米", "1400米", "1500米" };
	private String alertRArr[] = { "100米", "200米", "400米", "600米", "800米",
			"1000米" };
	private String refreshTArr[] = { "5秒", "10秒", "15秒", "20秒", "30秒", "45秒",
			"60秒" };
	private String batteryModeArr[] = { "1级(比较损耗)", "2级(推荐)", "3级(损耗很少)" };

	RelativeLayout mAdContainer;
	AdView mAdview;
	LayoutInflater mInflater;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		mContext = (Context) getActivity();
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.setting_view, null);

		MobclickAgent.updateOnlineConfig(mContext);
		MobclickAgent
				.setOnlineConfigureListener(new UmengOnlineConfigureListener() {
					@Override
					public void onDataReceived(JSONObject data) {
					}
				});
		String value = MobclickAgent.getConfigParams(mContext, "open_ad");

		/*if (value.equals("1")) {
			mAdContainer = (RelativeLayout) view.findViewById(R.id.adcontainer);
			// Create ad view
			mAdview = new AdView(getActivity(), "56OJzfwIuN7tr9LoSs",
					"16TLmHWoAp8diNUdpuAEMYfi");
			SharedPreferences mySharedPreferences = mContext
					.getSharedPreferences("setting",
							UserSettingView.MODE_PRIVATE);
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
					return mContext;
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
		}*/

		mInflater = LayoutInflater.from(mContext);

		// �����뾶ѡ����ѡ��Ľ���
		selectSearchRView = mInflater.inflate(R.layout.choose_one, null);
		// ���Ѿ���ѡ����ѡ��Ľ���
		selectAlertRView = mInflater.inflate(R.layout.choose_one, null);
		// ����ģʽѡ����ѡ��Ľ���
		selectBatteryModeView = mInflater.inflate(R.layout.choose_one, null);
		refreshModeView = mInflater.inflate(R.layout.choose_one, null);

		// ʵ����SharedPreferences���󣨵�һ����
		SharedPreferences mySharedPreferences = mContext.getSharedPreferences(
				"setting", SettingView.MODE_PRIVATE);
		searchRTextView = (TextView) view.findViewById(R.id.searchRTv);
		searchRTextView.setText(mySharedPreferences.getString("searchRMode",
				"800米"));

		alertRTextView = (TextView) view.findViewById(R.id.alertRTv);
		alertRTextView.setText(mySharedPreferences.getString("alertRMode",
				"200米"));

		batteryModeTextView = (TextView) view.findViewById(R.id.batteryModeTv);
		batteryModeTextView.setText(mySharedPreferences.getString(
				"batteryMode", "2级(推荐)"));

		refreshTextView = (TextView) view.findViewById(R.id.refreshModeTv);
		refreshTextView.setText(mySharedPreferences.getString("refreshMode",
				"30秒"));

		// acceptAlertTextView = (TextView) findViewById(R.id.acceptAlertTv);
		// acceptAlertTextView.setText(mySharedPreferences.getString("acceptAlertMode",
		// "����"));

		userSetView = (TextView) view.findViewById(R.id.userSetTv);
		shareAppTextView = (TextView) view.findViewById(R.id.shareAppTv);
		feedbackTextView = (TextView) view.findViewById(R.id.feedbackTv);
		aboutTextView = (TextView) view.findViewById(R.id.aboutTv);

//		userSetView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// Intent intent = null;
//				Intent intent = new Intent(mContext, UserSettingView.class);
//				// ������ͼ
//				startActivity(intent);
//			}
//		});

		// acceptAlertTextView.setOnClickListener(new OnClickListener() {

		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// showDialog(1, mAvatarView, 40);
		// }
		// });

		feedbackTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				FeedbackAgent agent = new FeedbackAgent(mContext);
				agent.startFeedbackActivity();
				agent.sync();
			}
		});

		aboutTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, AboutActivity.class);
				startActivity(intent);
			}
		});

		initWheelDataOfSearchRadius(selectSearchRView, searchRTextView);
		initWheelDataOfAlertDistance(selectAlertRView, alertRTextView);
		initWheelDataOfBatteryMode(selectBatteryModeView, batteryModeTextView);
		initWheelDataOfRefreshFrequency(refreshModeView, refreshTextView);

		// ����������Activity��������³�Ա����
		final UMSocialService mShareController = UMServiceFactory
				.getUMSocialService("com.umeng.share");
		// ���÷�������
		mShareController
				.setShareContent("我正在使用\"北京实时公交助手\"查公交，太方便啦，还可以看到站信息，你也试试吧？");
		// ���÷���ͼƬ������2Ϊ����ͼƬ����Դ����
		// mController.setShareMedia(new UMImage(getActivity(),
		// R.drawable.icon));

		// ���΢��ƽ̨
		UMWXHandler wxHandler = new UMWXHandler(mContext, "wx967daebe835fbeac",
				"5fa9e68ca3970e87a1f83e563c8dcbce");
		wxHandler.addToSocialSDK();
		// ���΢������Ȧ
		UMWXHandler wxCircleHandler = new UMWXHandler(mContext,
				"wx967daebe835fbeac", "5fa9e68ca3970e87a1f83e563c8dcbce");
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// ����1Ϊ��ǰActivity������2Ϊ��������QQ���������APP
		// ID������3Ϊ��������QQ���������APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(),
				"100424468", "c7394704798a158208a74ab60104f0ba");
		qqSsoHandler.addToSocialSDK();

		// ����1Ϊ��ǰActivity������2Ϊ��������QQ���������APP
		// ID������3Ϊ��������QQ���������APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
				"100424468", "c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();

		mShareController.getConfig().removePlatform(SHARE_MEDIA.TENCENT,
				SHARE_MEDIA.WEIXIN_CIRCLE);
		shareAppTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// �Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ
				mShareController.openShare(getActivity(), false);

			}
		});

		// ��������SSO handler
		mShareController.getConfig().setSsoHandler(new SinaSsoHandler());
		preferences = mContext.getSharedPreferences(Constants.SETTING,
				mContext.MODE_PRIVATE);
		editor = preferences.edit();
		return view;
	}

	// ��λ��ʾ����
	public void initWheelDataOfSearchRadius(View mDataView,
			final TextView textView) {
		final AbWheelView mWheelView1 = (AbWheelView) mDataView
				.findViewById(R.id.wheelView1);
		// mWheelView1.setAdapter(new AbNumericWheelAdapter(40, 190));
		mWheelView1.setAdapter(new AbWheelAdapter() {
			@Override
			public int getMaximumLength() {
				// TODO Auto-generated method stub
				return searchRArr.length;
			}

			@Override
			public int getItemsCount() {
				// TODO Auto-generated method stub
				return searchRArr.length;
			}

			@Override
			public String getItem(int arg0) {
				// TODO Auto-generated method stub
				return searchRArr[arg0];
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

				int index = mWheelView1.getCurrentItem();
				String val = mWheelView1.getAdapter().getItem(index);
				textView.setText(val);
				// ʵ����SharedPreferences���󣨵�һ����
				SharedPreferences mySharedPreferences = mContext
						.getSharedPreferences("setting",
								SettingView.MODE_PRIVATE);
				// ʵ����SharedPreferences.Editor���󣨵ڶ�����
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				// ��putString�ķ�����������
				editor.putString("searchRMode", val);
				// �ύ��ǰ����
				editor.commit();
			}

		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}

		});
	}

	// ��λ��ʾ����
	public void initWheelDataOfAlertDistance(View mDataView,
			final TextView textView) {
		final AbWheelView mWheelView1 = (AbWheelView) mDataView
				.findViewById(R.id.wheelView1);
		// mWheelView1.setAdapter(new AbNumericWheelAdapter(40, 190));
		mWheelView1.setAdapter(new AbWheelAdapter() {
			@Override
			public int getMaximumLength() {
				// TODO Auto-generated method stub
				return alertRArr.length;
			}

			@Override
			public int getItemsCount() {
				// TODO Auto-generated method stub
				return alertRArr.length;
			}

			@Override
			public String getItem(int arg0) {
				// TODO Auto-generated method stub
				return alertRArr[arg0];
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
				int index = mWheelView1.getCurrentItem();
				String val = mWheelView1.getAdapter().getItem(index);
				textView.setText(val);
				// ʵ����SharedPreferences���󣨵�һ����
				SharedPreferences mySharedPreferences = mContext
						.getSharedPreferences("setting",
								SettingView.MODE_PRIVATE);
				// ʵ����SharedPreferences.Editor���󣨵ڶ�����
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				// ��putString�ķ�����������
				editor.putString("alertRMode", val);
				// �ύ��ǰ����
				editor.commit();
			}

		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}

		});
	}

	// ��λ��ʾ����
	public void initWheelDataOfBatteryMode(View mDataView,
			final TextView textView) {
		final AbWheelView mWheelView1 = (AbWheelView) mDataView
				.findViewById(R.id.wheelView1);
		// mWheelView1.setAdapter(new AbNumericWheelAdapter(40, 190));
		mWheelView1.setAdapter(new AbWheelAdapter() {
			@Override
			public int getMaximumLength() {
				// TODO Auto-generated method stub
				return batteryModeArr.length;
			}

			@Override
			public int getItemsCount() {
				// TODO Auto-generated method stub
				return batteryModeArr.length;
			}

			@Override
			public String getItem(int arg0) {
				// TODO Auto-generated method stub
				return batteryModeArr[arg0];
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
				int index = mWheelView1.getCurrentItem();
				String val = mWheelView1.getAdapter().getItem(index);
				textView.setText(val);
				// ʵ����SharedPreferences���󣨵�һ����
				SharedPreferences mySharedPreferences = mContext
						.getSharedPreferences("setting",
								SettingView.MODE_PRIVATE);
				// ʵ����SharedPreferences.Editor���󣨵ڶ�����
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				// ��putString�ķ�����������
				editor.putString("batteryMode", val);
				// �ύ��ǰ����
				editor.commit();
			}

		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}

		});
	}

	// ��λ��ʾ����
	public void initWheelDataOfRefreshFrequency(View mDataView,
			final TextView textView) {
		final AbWheelView mWheelView1 = (AbWheelView) mDataView
				.findViewById(R.id.wheelView1);
		// mWheelView1.setAdapter(new AbNumericWheelAdapter(40, 190));
		mWheelView1.setAdapter(new AbWheelAdapter() {
			@Override
			public int getMaximumLength() {
				// TODO Auto-generated method stub
				return refreshTArr.length;
			}

			@Override
			public int getItemsCount() {
				// TODO Auto-generated method stub
				return refreshTArr.length;
			}

			@Override
			public String getItem(int arg0) {
				// TODO Auto-generated method stub
				return refreshTArr[arg0];
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
				int index = mWheelView1.getCurrentItem();
				String val = mWheelView1.getAdapter().getItem(index);
				textView.setText(val);
				// ʵ����SharedPreferences���󣨵�һ����
				SharedPreferences mySharedPreferences = mContext
						.getSharedPreferences("setting",
								SettingView.MODE_PRIVATE);
				// ʵ����SharedPreferences.Editor���󣨵ڶ�����
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				// ��putString�ķ�����������
				editor.putString("refreshMode", val);
				// �ύ��ǰ����
				editor.commit();
			}

		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}

		});
	}

}
