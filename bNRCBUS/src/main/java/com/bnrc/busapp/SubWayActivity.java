package com.bnrc.busapp;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.bnrc.util.AnimationUtil;
import com.bnrc.util.NetAndGpsUtil;
import com.bnrc.util.collectwifi.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SubWayActivity extends BaseActivity {
	private static final String TAG = SubWayActivity.class.getSimpleName();
	private AbTitleBar mAbTitleBar;
	private WebView mWebview;
	private WebSettings settings;
	private NetAndGpsUtil mNetAndGpsUtil;
	private Handler mHandler = new Handler();
	private static final String subwayUrl = "http://www.bjsubway.com/subway/images/subway_map.jpg";
	private static final String subwayAssets = "file:///android_asset/subway_map.png";
	private static final String baidu = "http://www.baidu.com";
	private static final String map = "http://ii.911cha.com/ditie/beijing.png";
	private static final String error = "file:///android_asset/error.html";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setAbContentView(R.layout.subway_activity);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextSize(20);
		mAbTitleBar.setTitleTextMargin(0, 0, 0, 0);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarGravity(Gravity.CENTER, Gravity.CENTER);
		mAbTitleBar.setTitleText("北京地铁线路图");
		initbase();
		mNetAndGpsUtil = NetAndGpsUtil.getInstance(getApplicationContext());
		openWeb();
	}

	private void openWeb() {
		mWebview = (WebView) findViewById(R.id.webView);
		mWebview.setWebViewClient(new MyWebViewClient());
		if (mNetAndGpsUtil.isNetworkAvailable())
			openUrl(map);
		else
			mWebview.loadUrl(error);

	}

	public void openUrl(String url) {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		if (width > 650) {
			this.mWebview.setInitialScale(190);
		} else if (width > 520) {
			this.mWebview.setInitialScale(160);
		} else if (width > 450) {
			this.mWebview.setInitialScale(140);
		} else if (width > 300) {
			this.mWebview.setInitialScale(120);
		} else {
			this.mWebview.setInitialScale(100);
		}
		settings = mWebview.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		settings.setBuiltInZoomControls(true);
		settings.setDisplayZoomControls(false);
		settings.setSupportZoom(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		settings.setLoadWithOverviewMode(true);
		// settings.setDomStorageEnabled(true);// 扩大比例的缩放
		settings.setUseWideViewPort(true);
		mWebview.loadUrl(url);
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// view.loadUrl(url);
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
			dismissLoading();
			mWebview.loadUrl(error);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			Log.i(TAG, "onPageStarted");
			showLoading();

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			dismissLoading();
			Log.i(TAG, "onPageFinished");

		}

	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
		if (mWebview != null) {
			// settings.setBuiltInZoomControls(true);
			// mWebview.setVisibility(View.GONE);
			mWebview.stopLoading();
			mWebview.removeAllViews();
			mWebview.destroy();
			mWebview = null;
		}
	}

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
