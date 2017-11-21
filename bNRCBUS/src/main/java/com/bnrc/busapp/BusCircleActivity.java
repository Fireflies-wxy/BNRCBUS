package com.bnrc.busapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.global.AbConstant;
import com.ab.view.titlebar.AbTitleBar;
import com.bnrc.util.AnimationUtil;
import com.bnrc.util.collectwifi.BaseActivity;
import com.umeng.analytics.MobclickAgent;

public class BusCircleActivity extends BaseActivity {
	private TextView mSubway;
	private TextView mHotel;
	private TextView mRestaurant;
	private TextView mBank;
	private TextView mSupermarket;
	private TextView mOil;
	private TextView mNetbar;
	private TextView mKTV;
	private AbTitleBar mAbTitleBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_buscircle);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleTextMargin(20, 0, 0, 0);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleBarGravity(Gravity.CENTER, Gravity.CENTER);
		mAbTitleBar.setTitleTextSize(18);
		mAbTitleBar.setTitleText("公交圈");
		initbase();
		mSubway = (TextView) findViewById(R.id.tv_subway);
		mHotel = (TextView) findViewById(R.id.tv_hotel);
		mRestaurant = (TextView) findViewById(R.id.tv_restaurant);
		mBank = (TextView) findViewById(R.id.tv_ATM);
		mSupermarket = (TextView) findViewById(R.id.tv_supermarket);
		mOil = (TextView) findViewById(R.id.tv_oil);
		mNetbar = (TextView) findViewById(R.id.tv_netbar);
		mKTV = (TextView) findViewById(R.id.tv_KTV);
		setListener();

	}

	private void setListener() {
		mSubway.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(BusCircleActivity.this,
						SearchSomethingView.class);

				// ���ñ�����͸��
				intent.putExtra("Keyword", "地铁");
				startActivity(intent);
				AnimationUtil.activityZoomAnimation(BusCircleActivity.this);

			}
		});
		mOil.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(BusCircleActivity.this,
						SearchSomethingView.class);
				intent.putExtra("TEXT", BusCircleActivity.this.getResources()
						.getString(R.string.title_transparent_desc));
				// ���ñ�����͸��
				intent.putExtra("Keyword", "加油站");
				startActivity(intent);
				AnimationUtil.activityZoomAnimation(BusCircleActivity.this);

			}
		});

		mNetbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(BusCircleActivity.this,
						SearchSomethingView.class);
				intent.putExtra("TEXT", BusCircleActivity.this.getResources()
						.getString(R.string.title_transparent_desc));
				// ���ñ�����͸��
				intent.putExtra("Keyword", "网吧");
				startActivity(intent);
				AnimationUtil.activityZoomAnimation(BusCircleActivity.this);

			}
		});

		mKTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(BusCircleActivity.this,
						SearchSomethingView.class);
				intent.putExtra("TEXT", BusCircleActivity.this.getResources()
						.getString(R.string.title_transparent_desc));
				intent.putExtra("Keyword", "KTV");
				startActivity(intent);
				AnimationUtil.activityZoomAnimation(BusCircleActivity.this);

			}
		});
		mHotel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// �Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ

				Intent intent = new Intent(BusCircleActivity.this,
						SearchSomethingView.class);
				intent.putExtra("TEXT", BusCircleActivity.this.getResources()
						.getString(R.string.title_transparent_desc));
				// ���ñ�����͸��
				intent.putExtra("Keyword", "酒店");
				startActivity(intent);
				AnimationUtil.activityZoomAnimation(BusCircleActivity.this);

			}
		});

		mRestaurant.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(BusCircleActivity.this,
						SearchSomethingView.class);
				intent.putExtra("TEXT", BusCircleActivity.this.getResources()
						.getString(R.string.title_transparent_desc));
				intent.putExtra("Keyword", "小吃");
				startActivity(intent);
				AnimationUtil.activityZoomAnimation(BusCircleActivity.this);

			}
		});

		mBank.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// �Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ

				Intent intent = new Intent(BusCircleActivity.this,
						SearchSomethingView.class);
				intent.putExtra("TEXT", BusCircleActivity.this.getResources()
						.getString(R.string.title_transparent_desc));
				// ���ñ�����͸��
				intent.putExtra("Keyword", "银行");
				startActivity(intent);
				AnimationUtil.activityZoomAnimation(BusCircleActivity.this);

			}
		});

		mSupermarket.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// �Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ

				Intent intent = new Intent(BusCircleActivity.this,
						SearchSomethingView.class);
				intent.putExtra("TEXT", BusCircleActivity.this.getResources()
						.getString(R.string.title_transparent_desc));
				intent.putExtra("Keyword", "超市");
				startActivity(intent);
				AnimationUtil.activityZoomAnimation(BusCircleActivity.this);

			}
		});

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
