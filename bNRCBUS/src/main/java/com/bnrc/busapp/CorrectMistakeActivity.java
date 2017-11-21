package com.bnrc.busapp;

import com.ab.view.titlebar.AbTitleBar;
import com.bnrc.util.AnimationUtil;
import com.bnrc.util.collectwifi.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class CorrectMistakeActivity extends BaseActivity {
	private AbTitleBar mAbTitleBar;
	private RelativeLayout mDelay, mAdvance, mHasCar, mNoCar, mWrongName,
			mWrongStation, mWrongLine, mCancelLine, mWrongTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.correct_mistake_activity);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextSize(20);
		mAbTitleBar.setTitleTextMargin(0, 0, 0, 0);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarGravity(Gravity.CENTER, Gravity.CENTER);
		mAbTitleBar.setTitleText("纠错");
		initbase();
		mDelay = (RelativeLayout) findViewById(R.id.rLayout_delay);
		mAdvance = (RelativeLayout) findViewById(R.id.rLayout_advance);
		mHasCar = (RelativeLayout) findViewById(R.id.rLayout_hasCar);
		mNoCar = (RelativeLayout) findViewById(R.id.rLayout_noCar);

		mWrongName = (RelativeLayout) findViewById(R.id.rLayout_wrongName);
		mWrongStation = (RelativeLayout) findViewById(R.id.rLayout_wrongStation);
		mWrongLine = (RelativeLayout) findViewById(R.id.rLayout_wrongLine);
		mCancelLine = (RelativeLayout) findViewById(R.id.rLayout_cancelLine);
		mWrongTime = (RelativeLayout) findViewById(R.id.rLayout_wrongTime);

		mDelay.setOnClickListener(mListener);
		mAdvance.setOnClickListener(mListener);
		mHasCar.setOnClickListener(mListener);
		mNoCar.setOnClickListener(mListener);

		mWrongName.setOnClickListener(mListener);
		mWrongStation.setOnClickListener(mListener);
		mWrongLine.setOnClickListener(mListener);
		mCancelLine.setOnClickListener(mListener);
		mWrongTime.setOnClickListener(mListener);

	}

	private OnClickListener mListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String mistakeName = "未知";
			switch (v.getId()) {
			case R.id.rLayout_delay:
				mistakeName = "延迟到站";
				break;
			case R.id.rLayout_advance:
				mistakeName = "提前到站";

				break;
			case R.id.rLayout_hasCar:
				mistakeName = "实际有车，显示无车";

				break;
			case R.id.rLayout_noCar:
				mistakeName = "显示有车，实际无车";

				break;

			case R.id.rLayout_wrongName:
				mistakeName = "站名有误";

				break;
			case R.id.rLayout_wrongStation:
				mistakeName = "少站或多站";

				break;
			case R.id.rLayout_wrongLine:
				mistakeName = "线路已变更";

				break;
			case R.id.rLayout_cancelLine:
				mistakeName = "线路取消或停运";

				break;
			case R.id.rLayout_wrongTime:
				mistakeName = "首末班时间有误";

				break;
			default:
				break;
			}
			Intent intent = new Intent(CorrectMistakeActivity.this,
					UploadMistakeActivity.class);
			intent.putExtra("MistakeName", mistakeName);
			startActivity(intent);
			AnimationUtil.activityZoomAnimation(CorrectMistakeActivity.this);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen");
		MobclickAgent.onPause(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen");
		MobclickAgent.onResume(this);
	}
}
