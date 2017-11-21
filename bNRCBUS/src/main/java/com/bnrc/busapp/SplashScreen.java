package com.bnrc.busapp;

import org.json.JSONObject;

import com.ab.global.AbConstant;
import com.bnrc.ui.rjz.MainActivity;
import com.bnrc.util.AppResourcesUtils;
import com.bnrc.util.LocationUtil;
import com.bnrc.util.SharePrefrenceUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	private SharePrefrenceUtil mSharePrefrenceUtil = null;
	private ImageView mImage;
	private int TIME = 1;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		setContentView(R.layout.splashscreen);
		AppResourcesUtils.init(getApplicationContext());
		com.umeng.socialize.utils.Log.LOG = true;
		MobclickAgent.updateOnlineConfig(this);
		MobclickAgent
				.setOnlineConfigureListener(new UmengOnlineConfigureListener() {
					@Override
					public void onDataReceived(JSONObject data) {
					}
				});

		LocationUtil mLocationUtil = LocationUtil
				.getInstance(SplashScreen.this);
		mSharePrefrenceUtil = SharePrefrenceUtil.getInstance(this
				.getApplicationContext());
		mImage = (ImageView) findViewById(R.id.wordpress_logo);
		String isFirst = mSharePrefrenceUtil.getValue("FIRST", "yes");
		if (isFirst.equals("yes")) {
			mImage.setImageResource(R.drawable.firstpage);
			mSharePrefrenceUtil.setKey("FIRST", "no");
			TIME = 3;
		} else
			mImage.setImageResource(R.drawable.firstpage);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				/*
				 * Create an Intent that will start the Main WordPress Activity.
				 */
				Intent intent = new Intent(SplashScreen.this,
						MainActivity.class);
				SplashScreen.this.startActivity(intent);
				SplashScreen.this.finish();
			}
		}, TIME * 1000); // 2900 for release

	}
}
