//package com.bnrc.ui.rjz;
//
//import com.ab.global.AbConstant;
//import com.bnrc.busapp.MyAlertStationView;
//import com.bnrc.busapp.MyFavoriteBuslineView;
//import com.bnrc.busapp.MyFavoriteStationView;
//import com.bnrc.busapp.R;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class SearchFragment extends BaseFragment {
//
//	private int mLastIndex = -1;
//	private Context mContext;
//	private TextView gongjiaochuxing;
//	private TextView dianyingBtn;
//	private TextView cantingBtn;
//	private TextView yinhangBtn;
//	private TextView chaoshiBtn;
//	private TextView tixingBtn;
//	private TextView favStationBtn;
//	private TextView favBuslineBtn;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		mContext = (Context) getActivity();
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//
//		View view = LayoutInflater.from(mContext).inflate(
//				R.layout.activity_buscircle, null);
//		gongjiaochuxing = (TextView) view.findViewById(R.id.gongjiaochuxing);
//		dianyingBtn = (TextView) view.findViewById(R.id.hotel);
//		cantingBtn = (TextView) view.findViewById(R.id.canting);
//		yinhangBtn = (TextView) view.findViewById(R.id.yinhang);
//		chaoshiBtn = (TextView) view.findViewById(R.id.chaoshi);
//		tixingBtn = (TextView) view.findViewById(R.id.tixingzhandian);
//		favStationBtn = (TextView) view.findViewById(R.id.shoucangzhandian);
//		favBuslineBtn = (TextView) view.findViewById(R.id.shoucangxianlu);
//		setListener();
//		return view;
//	}
//
//	private void setListener() {
//		gongjiaochuxing.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//		tixingBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(mContext, MyAlertStationView.class);
//				intent.putExtra(
//						"TEXT",
//						mContext.getResources().getString(
//								R.string.title_transparent_desc));
//				// ���ñ�����͸��
//				intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//						AbConstant.TITLE_TRANSPARENT);
//				startActivity(intent);
//			}
//		});
//
//		favStationBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(mContext,
//						MyFavoriteStationView.class);
//				intent.putExtra(
//						"TEXT",
//						mContext.getResources().getString(
//								R.string.title_transparent_desc));
//				// ���ñ�����͸��
//				intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//						AbConstant.TITLE_TRANSPARENT);
//				startActivity(intent);
//
//			}
//		});
//
//		favBuslineBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(mContext,
//						MyFavoriteBuslineView.class);
//				intent.putExtra(
//						"TEXT",
//						mContext.getResources().getString(
//								R.string.title_transparent_desc));
//				// ���ñ�����͸��
//				intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//						AbConstant.TITLE_TRANSPARENT);
//				startActivity(intent);
//
//			}
//		});
//		dianyingBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				// �Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ
//				if (isNetworkConnected(mContext)) {
//					Intent intent = new Intent(mContext,
//							SearchSomethingView.class);
//					intent.putExtra(
//							"TEXT",
//							mContext.getResources().getString(
//									R.string.title_transparent_desc));
//					// ���ñ�����͸��
//					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//							AbConstant.TITLE_TRANSPARENT);
//					intent.putExtra("keyword", "酒店");
//					startActivity(intent);
//					// overridePendingTransition(R.anim.out_alpha,
//					// R.anim.enter_alpha);
//				} else {
//					Toast toast = Toast.makeText(
//							mContext.getApplicationContext(), "您的网络有问题，请检查~",
//							Toast.LENGTH_LONG);
//					toast.show();
//				}
//
//			}
//		});
//
//		cantingBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if (isNetworkConnected(mContext)) {
//					Intent intent = new Intent(mContext,
//							SearchSomethingView.class);
//					intent.putExtra(
//							"TEXT",
//							mContext.getResources().getString(
//									R.string.title_transparent_desc));
//					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//							AbConstant.TITLE_TRANSPARENT);
//					intent.putExtra("keyword", "小吃");
//					startActivity(intent);
//					// overridePendingTransition(R.anim.out_alpha,
//					// R.anim.enter_alpha);
//				} else {
//					Toast toast = Toast.makeText(
//							mContext.getApplicationContext(), "您的网络有问题，请检查~",
//							Toast.LENGTH_LONG);
//					toast.show();
//				}
//
//			}
//		});
//
//		yinhangBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				// �Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ
//				if (isNetworkConnected(mContext)) {
//					Intent intent = new Intent(mContext,
//							SearchSomethingView.class);
//					intent.putExtra(
//							"TEXT",
//							mContext.getResources().getString(
//									R.string.title_transparent_desc));
//					// ���ñ�����͸��
//					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//							AbConstant.TITLE_TRANSPARENT);
//					intent.putExtra("keyword", "银行");
//					startActivity(intent);
//					// overridePendingTransition(R.anim.out_alpha,
//					// R.anim.enter_alpha);
//				} else {
//					Toast toast = Toast.makeText(
//							mContext.getApplicationContext(), "您的网络有问题，请检查~",
//							Toast.LENGTH_LONG);
//					toast.show();
//				}
//
//			}
//		});
//
//		chaoshiBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				// �Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ
//				if (isNetworkConnected(mContext)) {
//					Intent intent = new Intent(mContext,
//							SearchSomethingView.class);
//					intent.putExtra(
//							"TEXT",
//							mContext.getResources().getString(
//									R.string.title_transparent_desc));
//					// ���ñ�����͸��
//					intent.putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,
//							AbConstant.TITLE_TRANSPARENT);
//					intent.putExtra("keyword", "超市");
//					startActivity(intent);
//					// overridePendingTransition(R.anim.out_alpha,
//					// R.anim.enter_alpha);
//				} else {
//					Toast toast = Toast.makeText(
//							mContext.getApplicationContext(), "您的网络有问题，请检查~",
//							Toast.LENGTH_LONG);
//					toast.show();
//				}
//
//			}
//		});
//
//	}
//
//	private boolean isNetworkConnected(Context context) {
//		if (context != null) {
//			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
//					.getSystemService(Context.CONNECTIVITY_SERVICE);
//			NetworkInfo mNetworkInfo = mConnectivityManager
//					.getActiveNetworkInfo();
//			if (mNetworkInfo != null) {
//				return mNetworkInfo.isAvailable();
//			}
//		}
//		return false;
//	}
//}
