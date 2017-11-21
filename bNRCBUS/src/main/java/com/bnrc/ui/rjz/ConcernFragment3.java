package com.bnrc.ui.rjz;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.crypto.tls.AlertLevel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import u.aly.aa;

import com.ab.global.AbConstant;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.bnrc.busapp.PollingService;
import com.bnrc.busapp.PollingUtils;
import com.bnrc.busapp.R;
import com.bnrc.busapp.SearchBuslineView;
import com.bnrc.busapp.SubWayActivity;

import com.bnrc.busapp.SegmentedGroup;
import com.bnrc.busapp.SettingView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.bnrc.network.MyVolley;
import com.bnrc.ui.rtBus.Child;
import com.bnrc.ui.rtBus.Group;
import com.bnrc.ui.rtBus.PinnedHeaderExpandableConcernAdapter;
import com.bnrc.ui.rtBus.PinnedHeaderExpandableLocListView;
import com.bnrc.util.BuslineDBHelper;
import com.bnrc.util.DataBaseHelper;
import com.bnrc.util.GetToMarket;
import com.bnrc.util.LocationUtil;
import com.bnrc.util.SlidingDrawerGridView;
import com.bnrc.util.UserDataDBHelper;
import com.bnrc.util.collectwifi.Constants;
import com.bnrc.util.collectwifi.ScanService;
import com.bnrc.util.collectwifi.ServiceUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ConcernFragment3 extends BaseFragment {
	private static final String TAG = ConcernFragment3.class.getSimpleName();
	private Context mContext;
	private ImageView refesh = null;
	private ImageView openAlertView;
	private Handler mHandler;
	private String isOpenAlert;
	private EditText mSearchEdt;
	private SegmentedGroup segmented;
	private MyViewPager mPager;
	private AllConcernFragSwipe mAllFrag;
	private WorkFragSwipe mWorkFrag;
	private HomeFragSwipe mHomeFrag;
	private ArrayList<BaseFragment> mFragmentList;
	private ImageButton menuSettingBtn;// 菜单呼出按钮
	private IPopWindowListener mChooseListener;
	private MyViewPagerAdapter mPagerAdapter;
	private UserDataDBHelper mUserDB;
	private int mLastIndex = 0;
	private List<Integer> TABLE;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = (Context) getActivity();
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.zj_near_view2, null);
		// initAD();
		// LatLng myPoint = new LatLng(mBDLocation.getLatitude(),
		// mBDLocation.getLongitude());
		mUserDB = UserDataDBHelper.getInstance(mContext);
		// mUserDB.AcquireFavInfoWithLocation(myPoint);
		// initTitleRightLayout();
		menuSettingBtn = (ImageButton) view.findViewById(R.id.menu_imgbtn);
		menuSettingBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mChooseListener.onLoginClick();
			}
		});
		refesh = (ImageView) view.findViewById(R.id.refresh);
		mSearchEdt = (EditText) view.findViewById(R.id.edt_input);
		mSearchEdt.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				/** 加这个判断，防止该事件被执行两次 */
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Intent intent = new Intent(getActivity(),
							SearchBuslineView.class);
					startActivity(intent);

				}
				return false;
			}
		});
		segmented = (SegmentedGroup) view.findViewById(R.id.segmentedGroup);
		segmented.setTintColor(getResources().getColor(
				R.color.radio_button_selected_color));
		mAllFrag = new AllConcernFragSwipe();
		mWorkFrag = new WorkFragSwipe();
		mHomeFrag = new HomeFragSwipe();
		mFragmentList = new ArrayList<BaseFragment>();
		mFragmentList.add(mAllFrag);
		mFragmentList.add(mWorkFrag);
		mFragmentList.add(mHomeFrag);
		mPager = (MyViewPager) view.findViewById(R.id.content);
		segmented.setOnCheckedChangeListener(new CheckedChangeListener());
		mPagerAdapter = new MyViewPagerAdapter(getFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new PageChangeListener());
		mPager.setOffscreenPageLimit(3);
		Log.i(TAG,
				TAG + " onCreateView " + "fraglist.size: "
						+ mFragmentList.size());
		segmented.check(R.id.radBtn_all);
		TABLE = new ArrayList<Integer>();
		TABLE.add(0);
		TABLE.add(1);
		TABLE.add(2);
		mSearchEdt.clearFocus();
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mSearchEdt.getWindowToken(), 0);
		return view;
	}

	public class MyViewPagerAdapter extends FragmentPagerAdapter {
		private FragmentManager mFragmentManager;
		private FragmentTransaction mCurTransaction;

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			this.mFragmentManager = fm;
		}

		@Override
		public Fragment getItem(int position) {
			Log.i(TAG, "mFragmentList.get(position) " + position);
			return mFragmentList.get(position);

		}

		@Override
		public int getCount() {
			return mFragmentList.size();
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return FragmentStatePagerAdapter.POSITION_NONE;
		}

		public void destroyAllItem() {
			int mPosition = mPager.getCurrentItem();
			int mPositionMax = mPager.getCurrentItem() + 1;
			if (TABLE.size() > 0 && mPosition < TABLE.size()) {
				if (mPosition > 0) {
					mPosition--;
				}
				mPosition = 0;
				mPositionMax = 3;
				for (int i = mPosition; i < mPositionMax; i++) {
					try {
						Object objectobject = this.instantiateItem(mPager,
								TABLE.get(i).intValue());
						if (objectobject != null)
							destroyItem(mPager, TABLE.get(i).intValue(),
									objectobject);
					} catch (Exception e) {
						Log.i(TAG, "no more Fragment in FragmentPagerAdapter");
					}
				}
			}
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);

			if (position <= getCount()) {
				// FragmentManager manager = ((Fragment)
				// object).getFragmentManager();
				// FragmentTransaction trans = manager.beginTransaction();
				if (mCurTransaction == null)
					mCurTransaction = mFragmentManager.beginTransaction();
				mCurTransaction.remove((Fragment) object);
				mCurTransaction.commit();
			}
		}

	}

	private class CheckedChangeListener implements OnCheckedChangeListener {
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.radBtn_all:
				mPager.setCurrentItem(0);
				mLastIndex = 0;
				break;
			case R.id.radBtn_work:
				mPager.setCurrentItem(1);
				mLastIndex = 1;
				break;
			case R.id.radBtn_home:
				mPager.setCurrentItem(2);
				mLastIndex = 2;
				break;

			}
		}
	}

	// 刷新实时数据
	@Override
	public void refreshConcern() {
		if (this != null && !this.isDetached() && this.isVisible()) {
			if (mFragmentList == null)
				return;
			for (BaseFragment frag : mFragmentList)
				frag.refreshConcern();
		}
	}

	// 刷新实时数据
	@Override
	public void refresh() {
		if (this != null && !this.isDetached() && this.isVisible()) {
			if (mFragmentList == null)
				return;
			for (BaseFragment frag : mFragmentList)
				frag.refresh();
		}
	}

	private class PageChangeListener implements OnPageChangeListener {
		public void onPageSelected(int position) {
			switch (position) {
			case 0:
				segmented.check(R.id.radBtn_all);
				break;
			case 1:
				segmented.check(R.id.radBtn_work);
				break;
			case 2:
				segmented.check(R.id.radBtn_home);
				break;
			}
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	private void initAD() {
		isOpenAlert = "开启提醒功能";
		MobclickAgent.updateOnlineConfig(mContext);
		String value = MobclickAgent.getConfigParams(mContext,
				"bus_data_version");
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(value);
			String version = jsonObj.getString("version");
			String ready = jsonObj.getString("ready");
			SharedPreferences mySharedPreferences = mContext
					.getSharedPreferences("setting", SettingView.MODE_PRIVATE);
			String oldVersion = mySharedPreferences.getString(
					"bus_data_version", "1");
			if (ready.equalsIgnoreCase("YES")
					&& (version.equalsIgnoreCase(oldVersion) == false)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setMessage("公交数据已经推出了新的版本，您是否要更新？").setTitle("友情提示")
						.setNegativeButton("取消", null);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// DataBaseHelper.getInstance(mContext)
								// .DownFileWithUrl(
								// MobclickAgent.getConfigParams(
								// mContext,
								// "bus_data_url"));

							}
						});
				builder.show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		value = MobclickAgent.getConfigParams(mContext,
				"realtime_bus_data_version");
		Log.i("realtime_bus_data_version", value);
		try {
			jsonObj = new JSONObject(value);
			String version = jsonObj.getString("version");
			String ready = jsonObj.getString("ready");
			SharedPreferences mySharedPreferences = mContext
					.getSharedPreferences("setting", SettingView.MODE_PRIVATE);
			String oldVersion = mySharedPreferences.getString(
					"realtime_bus_data_version", "1");
			if (ready.equalsIgnoreCase("YES")
					&& (version.equalsIgnoreCase(oldVersion) == false)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setMessage("实时公交数据有更新，您是否要更新？").setTitle("友情提示")
						.setNegativeButton("取消", null);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// BuslineDBHelper
								// .getInstance(mContext)
								// .DownFileWithUrl(
								// MobclickAgent
								// .getConfigParams(
								// mContext,
								// "realtime_bus_data_url"));

							}
						});
				builder.show();
			}
		} catch (

		JSONException e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i(TAG, TAG + " onStart");

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = (Context) activity;
		mChooseListener = (IPopWindowListener) activity;
		Log.i(TAG, TAG + " onAttach");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop polling service
		System.out.println("Stop polling service...");
		PollingUtils.stopPollingService(mContext, PollingService.class,
				PollingService.ACTION);
		ServiceUtils.stopPollingService(mContext.getApplicationContext(),
				ScanService.class, Constants.SERVICE_ACTION);
		Log.i(TAG, TAG + " onDestroy");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, TAG + " onActivityCreated");

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(TAG, TAG + " onCreate");

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.i(TAG, TAG + " onDestroyView");

	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.i(TAG, TAG + " onDetach");
		mPagerAdapter.destroyAllItem();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG,
				TAG + " onResume" + "  fraglist.size: " + mFragmentList.size());

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, TAG + " onStop");

	}
}
