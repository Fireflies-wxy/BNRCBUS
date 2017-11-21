package com.bnrc.ui.rjz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.ab.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import com.ab.util.AbDialogUtil;
import com.ab.view.progress.AbHorizontalProgressBar;
import com.bnrc.activity.AboutActivity;
import com.bnrc.busapp.BusCircleActivity;
import com.bnrc.busapp.PollingService;
import com.bnrc.busapp.PollingUtils;
import com.bnrc.busapp.R;
import com.bnrc.busapp.SettingView;
import com.bnrc.busapp.SubWayActivity;
import com.bnrc.http.AbFileHttpResponseListener;
import com.bnrc.http.AbHttpUtil;
import com.bnrc.http.AbFileHttpResponseListener;
import com.bnrc.http.AbHttpUtil;
import com.bnrc.network.UpdateFromBaidu;
import com.bnrc.util.AbFileUtil;
import com.bnrc.util.AbToastUtil;
import com.bnrc.ui.login.CustomProgressDialog;
import com.bnrc.ui.login.DragLayout;
import com.bnrc.ui.login.LoadingDialog;
import com.bnrc.ui.login.DragLayout.DragListener;
import com.bnrc.ui.login.ProgressLoadingDialog;
import com.bnrc.ui.rjz.AlertService.onAlertListener;
import com.bnrc.ui.rjz.RTabHost.RTabHostListener;
import com.bnrc.ui.rtBus.Child;
import com.bnrc.ui.rtBus.Group;
import com.bnrc.util.GetToMarket;
import com.bnrc.util.LocationUtil;
import com.bnrc.util.PCDataBaseHelper;
import com.bnrc.util.PCUserDataDBHelper;
import com.bnrc.util.SharePrefrenceUtil;
import com.bnrc.util.UserDataDBHelper;
import com.bnrc.util.VolleyNetwork;
import com.bnrc.util.VolleyNetwork.appVersionListener;
import com.bnrc.util.VolleyNetwork.versionListener;
import com.bnrc.util.collectwifi.ActivityReceiver;
import com.bnrc.util.collectwifi.BaseActivity;
import com.bnrc.util.collectwifi.CollectWifiDBHelper;
import com.bnrc.util.collectwifi.ConfigCheck;
import com.bnrc.util.collectwifi.Constants;
import com.bnrc.util.collectwifi.MyDialogReadSureWifi;
import com.bnrc.util.collectwifi.MyDialogReadWifi;
import com.bnrc.util.collectwifi.ScanService;
import com.bnrc.util.collectwifi.ServiceUtils;
import com.bnrc.util.collectwifi.WifiAdmin;
import com.bnrc.util.collectwifi.WifiReceiver;

import com.umeng.analytics.MobclickAgent;
import com.zf.iosdialog.widget.AlertDialog;
import com.zhy.view.HorizontalProgressBarWithNumber;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.os.Environment;

public class MainActivity extends BaseActivity implements IPopWindowListener,
		OnClickListener {
	protected static final String TAG = MainActivity.class.getSimpleName();
	private RTabHost mTabHost;
	private List<BaseFragment> mFragments = new ArrayList<BaseFragment>();
	private BaseFragment mCurFragment;
	private int mLastIndex = 0;
	protected SharePrefrenceUtil mSharePrefrenceUtil;
	private Timer mRefreshTimer;
	private TimerTask mRefreshTask;
	int screenWidth;// 屏幕宽度
	public LocationUtil mLocationUtil = null;
	/** 左边侧滑菜单 */
	private DragLayout mDragLayout;
	private PCUserDataDBHelper mPcUserDataDBHelper = null;
	private PCDataBaseHelper mPcDataBaseHelper = null;
	private VolleyNetwork mVolleyNetwork;
	private RelativeLayout mCanversLayout;// 阴影遮挡图层
	// private PopupWindow mPopupWindow;// 弹窗对象
	private Map<String, Child> mChildList;
	private Child child;
	private SelectPicPopupWindow menuWindow;
	private RelativeLayout mMenuBuscircle, mMenuTransfer, mMenuAr, mMenuSetting,
			mMenuShare, mMenuAdvice, mMenuAbout;
	private BaseFragment mFragment;
	private double dbVersion, appVersion;
	private boolean isNeedUpdateDB = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_main);
		mPcUserDataDBHelper = PCUserDataDBHelper
				.getInstance(getApplicationContext());
		mPcDataBaseHelper = PCDataBaseHelper
				.getInstance(getApplicationContext());
		mLocationUtil = LocationUtil.getInstance(this);
		mLocationUtil.startLocation();
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		screenWidth = display.getWidth();
		//疑似侧边栏
		initView();
		initFragments();
		initTabHost();
		initbase();
		Log.i(TAG, "onCreate()");
		// initView();
		mSharePrefrenceUtil = SharePrefrenceUtil
				.getInstance(getApplicationContext());
		mVolleyNetwork = VolleyNetwork
				.getInstance(this.getApplicationContext());

		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		getContentResolver()
				.registerContentObserver(
						Settings.Secure
								.getUriFor(Settings.System.LOCATION_PROVIDERS_ALLOWED),
						false, mGpsMonitor);
		checkDatabaseVersion();
	}

	private void initView() {
		/**
		 * 如果需要在别的Activity界面中也实现侧滑菜单效果，需要在布局中引入DragLayout（同本Activity方式），
		 * 然后在onCreate中声明使用; Activity界面部分，需要包裹在MyRelativeLayout中.
		 */

		mDragLayout = (DragLayout) findViewById(R.id.DLayout);
		mDragLayout.setDragListener(new DragListener() {// 动作监听
					@Override
					public void onOpen() {
					}

					@Override
					public void onClose() {
					}

					@Override
					public void onDrag(float percent) {

					}
				});

		mMenuBuscircle = (RelativeLayout) findViewById(R.id.menu_buscircle);
		mMenuTransfer = (RelativeLayout) findViewById(R.id.menu_transfer);
		//mMenuAr = (RelativeLayout) findViewById(R.id.menu_ar);
		mMenuSetting = (RelativeLayout) findViewById(R.id.menu_setting);
		mMenuShare = (RelativeLayout) findViewById(R.id.menu_share);
		mMenuAdvice = (RelativeLayout) findViewById(R.id.menu_advice);
		mMenuAbout = (RelativeLayout) findViewById(R.id.menu_about);
		mMenuBuscircle.setOnClickListener(this);
		mMenuTransfer.setOnClickListener(this);
		mMenuSetting.setOnClickListener(this);
		mMenuShare.setOnClickListener(this);
		mMenuAdvice.setOnClickListener(this);
		mMenuAbout.setOnClickListener(this);
	}

	private LocationManager mLocationManager;
	private final ContentObserver mGpsMonitor = new ContentObserver(null) {
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);

			boolean enabled = mLocationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			mLocationUtil.initLocation();
			Log.i(TAG, "gps enabled? " + enabled);
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.menu_buscircle:
			Intent circleIntent = new Intent(MainActivity.this,
					BusCircleActivity.class);
			startActivity(circleIntent);
			break;
		case R.id.menu_transfer:
			Intent subwayIntent = new Intent(MainActivity.this,
					SubWayActivity.class);
			startActivity(subwayIntent);
			break;
//        case R.id.menu_ar:
//            Intent arIntent = new Intent(MainActivity.this,
//                    ArActivity.class);
//            startActivity(arIntent);
//            break;
		case R.id.menu_setting:
			Intent settingInt = new Intent(MainActivity.this, SettingView.class);
			startActivity(settingInt);
			break;
		case R.id.menu_share:

			break;
		case R.id.menu_advice:

			break;
		case R.id.menu_about:
			Intent aboutInt = new Intent(MainActivity.this, AboutActivity.class);
			startActivity(aboutInt);
			break;

		default:
			break;
		}
	}

	@Override
	public void onRestart() {
		super.onRestart();
		Log.e(TAG, "onRestart");

	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e(TAG, "onPause");

		MobclickAgent.onPageEnd("SplashScreen"); // ��֤ onPageEnd ��onPause
		MobclickAgent.onPause(this);
		cancelRefreshTimertask();
		// unregisterReceiver(mWifiReceiver);
		// unregisterReceiver(mActivityReceiver);
		Log.i(TAG, "onPause()");

	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
		PollingService.hasKnown = true;
		MobclickAgent.onPageStart("SplashScreen"); // ͳ��ҳ��
		MobclickAgent.onResume(this); // ͳ��ʱ��
		// registerReceiver(mWifiReceiver, wifiFilter);
		// registerReceiver(mActivityReceiver, activityFilter);
		openRefreshTimertask();

	}

	private void openRefreshTimertask() {
		mRefreshTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mFragment.refreshConcern();
					}
				});
			}
		};
		mRefreshTimer = new Timer(true);
		String value = mSharePrefrenceUtil.getValue("refreshFrequency", "30秒");
		int period = Integer.parseInt(value.substring(0, value.indexOf("秒"))
				.toString());
		mRefreshTimer.schedule(mRefreshTask, period * 1000, period * 1000);
	}

	private void cancelRefreshTimertask() {
		mRefreshTask.cancel();
		mRefreshTimer.cancel();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e(TAG, "onDestroy");
		// unbindService(connection);
		getContentResolver().unregisterContentObserver(mGpsMonitor);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.e(TAG, "onStart");

		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(INPUT_METHOD_SERVICE);
		// imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.e(TAG, "onStop");
		dismissLoading();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		System.out.println("onCreateOptionsMenu");

		getMenuInflater().inflate(R.menu.main, menu);
		Log.i(TAG, "onCreateOptionsMenu()");

		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// if (keyCode == KeyEvent.KEYCODE_BACK) {
		// showExitDialog();
		// return true;
		// }
		// return super.onKeyDown(keyCode, event);

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initTabHost() {
		mTabHost = (RTabHost) findViewById(R.id.tab_host);

		mTabHost.setQQTabHostListener(new RTabHostListener() {

			@Override
			public void onTabSelected(int index) {
				selectTab(index);
			}
		});

		mTabHost.selectTab(mLastIndex);
	}

	List<Class<? extends BaseFragment>> classList = null;

	private void initFragments() {
		mFragments.clear();

		classList = new ArrayList<Class<? extends BaseFragment>>();
		classList.add(ConcernFragment3.class);
		classList.add(NearFragmentSwipe.class);
		classList.add(AlertFragmentZJ.class);
		// classList.add(SettingFragment.class);

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transcation = getSupportFragmentManager()
				.beginTransaction();
		// for (Class<? extends BaseFragment> fragClass : classList) {
		// BaseFragment frag = (BaseFragment) fm.findFragmentByTag(fragClass
		// .getName());
		// if (null == frag) {
		// frag = createFragmentByClass(fragClass);
		// transcation
		// .add(R.id.page_conatainer, frag, fragClass.getName());
		// }
		//
		// mFragments.add(frag);
		// transcation.hide(frag);
		// }
		Class<? extends BaseFragment> fragClass = classList.get(mLastIndex);

		// transcation.commitAllowingStateLoss();
		mFragment = createFragmentByClass(fragClass);
		transcation.replace(R.id.page_conatainer, mFragment).commit();
	}

	private BaseFragment createFragmentByClass(
			Class<? extends BaseFragment> fragClass) {
		BaseFragment frag = null;
		try {
			try {
				Constructor<? extends BaseFragment> cons = null;
				cons = fragClass.getConstructor();
				frag = cons.newInstance();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		} catch (Throwable e) {
			throw new RuntimeException("Can not create instance for class "
					+ fragClass.getName(), e);
		}
		return frag;
	}

	private void selectTab(int index) {
		if (mLastIndex == index) {
			// Toast.makeText(MainActivity.this, "同一个TAB", Toast.LENGTH_SHORT)
			// .show();
			return;
		}
		mLastIndex = index;
		// showLoading();
		selectFragment(index);
	}

	private void selectFragment(int index) {
		// if (mFragments.get(index).isVisible()) {
		// return;
		// }
		//
		FragmentTransaction transcation = getSupportFragmentManager()
				.beginTransaction();
		//
		// if (null != mCurFragment) {
		//
		// transcation.hide(mCurFragment);
		// }
		//
		// mCurFragment = mFragments.get(index);
		// transcation.show(mCurFragment);

		// transcation.commitAllowingStateLoss();
		Class<? extends BaseFragment> fragClass = classList.get(index);
		mFragment = createFragmentByClass(fragClass);
		transcation.replace(R.id.page_conatainer, mFragment).commit();
	}

	private long mLastBackTime = 0;

	private void showExitDialog() {

		long now = System.currentTimeMillis();
		if (now - mLastBackTime < 3000) {
			exitProcess();
		} else {
			mLastBackTime = now;
			// Toast.makeText(MainActivity.this, "再次点击退出程序",
			// Toast.LENGTH_SHORT).show();
			//
			Toast toast = new Toast(MainActivity.this);
			toast.setView(LayoutInflater.from(MainActivity.this).inflate(
					R.layout.toast_view, null));

			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void exitProcess() {
		finish();
		new Handler().postDelayed(new Runnable() {
			public void run() {
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}, 400);

	}

	public int getScreenwidth() {
		return screenWidth;
	}

	// 为弹出窗口实现监听类
	private OnClickListener mPopItemListener = new OnClickListener() {

		public void onClick(View v) {
			// Map<String, Object> record = new HashMap<String, Object>();
			int LineID = child.getLineID();
			int StationID = child.getStationID();
			switch (v.getId()) {
			case R.id.iv_work:
				child.setType(Constants.TYPE_WORK);
				mPcUserDataDBHelper.addFavRecord(child);

				break;
			case R.id.iv_home:
				child.setType(Constants.TYPE_HOME);
				mPcUserDataDBHelper.addFavRecord(child);
				break;
			case R.id.iv_other:
				child.setType(Constants.TYPE_OTHER);
				mPcUserDataDBHelper.addFavRecord(child);
				break;
			case R.id.iv_del:
				child.setType(Constants.TYPE_NONE);
				mPcUserDataDBHelper.cancelFav(LineID, StationID);
				break;
			case R.id.btn_cancel:
				break;
			default:
				break;
			}
			menuWindow.dismiss();
			mFragment.refresh();
		}
	};

	@Override
	public void onPopClick(Child child) {
		// TODO Auto-generated method stub
		this.child = child;
		mCanversLayout = (RelativeLayout) findViewById(R.id.rlayout_shadow);
		menuWindow = new SelectPicPopupWindow(MainActivity.this, child,
				mPopItemListener);
		menuWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {// 点击消失
				mCanversLayout.setVisibility(View.GONE);
			}
		});
		menuWindow.showAtLocation(MainActivity.this.findViewById(R.id.DLayout),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		menuWindow.setFocusable(true);
		menuWindow.setOutsideTouchable(false);
		menuWindow.update();
		mCanversLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoginClick() {
		// TODO Auto-generated method stub
		mDragLayout.open();
	}

	private CustomProgressDialog loadingDialog;
	private LoadingDialog mLoading;

	public synchronized LoadingDialog showLoading() {
		if (mLoading == null) {
			mLoading = new LoadingDialog(this, R.layout.view_tips_loading);
			mLoading.setCancelable(false);
			mLoading.setCanceledOnTouchOutside(true);
		}
		mLoading.show();
		return mLoading;
	}

	public synchronized void dismissLoading() {
		if (!this.isFinishing() && this.mLoading != null
				&& this.mLoading.isShowing()) {
			mLoading.dismiss();
		}
	}

	private AlertDialog mAlertDialog;

	public AlertDialog showAlertDialog() {
		if (mAlertDialog != null)
			mAlertDialog.dismiss();
		String stationName = myAlertBinder.getAlertStationName();
		mAlertDialog = new AlertDialog(MainActivity.this).builder()
				.setTitle(stationName + "站").setMsg("即将到达 ，请注意下车！")
				.setNegativeButton("确认", new OnClickListener() {
					@Override
					public void onClick(View v) {
						myAlertBinder.stopAlert();
					}
				});
		// mAlertDialog.setCancelable(false);
		mAlertDialog.show();
		return mAlertDialog;
	}

	private AlertDialog mVersionDialog;

	public void checkDatabaseVersion() {
		final double localVersion = Double.parseDouble(mSharePrefrenceUtil
				.getValue("DBVersion", "2.2"));
		PackageManager manager;
		PackageInfo info = null;
		manager = this.getPackageManager();
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final double localAppVersion = Double.parseDouble(info.versionName);
		Log.i("VERSION", localVersion + "before getuploadinfo" + info.versionName);
		mVolleyNetwork.getUploadInfo(new appVersionListener() {

			@Override
			public void onSuccess(JSONObject obj) {
				// TODO Auto-generated method stub
				try {
					double dVersion = Double.parseDouble(obj.getString("V"));
					double aVersion = Double.parseDouble(obj.getString("appV"));
					String Info = obj.getString("Info");
					String url = obj.getString("url");
					if (localVersion < dVersion)
						showDBVersionDialog();
					Log.i(TAG, "localAppVersion: " + localAppVersion
							+ " aVersion: " + aVersion);
					if (localAppVersion < aVersion)
						showAppVersionDialog(Info,url);
					dbVersion = dVersion;
					appVersion = aVersion;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFail() {
				// TODO Auto-generated method stub
				startAlertService();
				Log.d(TAG, "onFail: getversioninfo");
			}
		});
	}

	private void showDBVersionDialog() {
		mVersionDialog = new AlertDialog(this).builder().setTitle("数据库更新")
				.setMsg("数据库不是最新版本，请更新数据库")
				.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(View v) {
						updateDatabase(VolleyNetwork.beijingdbURL, "pc.db");
					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// startAlertService();
						exitProcess();
					}
				});
		mVersionDialog.setCanceledOnTouchOutside(false);
		mVersionDialog.setCancelable(false);
		mVersionDialog.show();
	}

	private void showAppVersionDialog(String msg,final String url) {
		mVersionDialog = new AlertDialog(this).builder().setTitle("应用版本更新")
				.setMsg(msg).setPositiveButton("前往市场", new OnClickListener() {
					@Override
					public void onClick(View v) {
						GetToMarket gu = new GetToMarket();
						Intent i = gu.getIntent(MainActivity.this);
						boolean b = gu.judge(MainActivity.this, i);
						if (b == false) {
							startActivity(i);
						} else
							Toast.makeText(MainActivity.this, "请前往应用商店更新应用！",
									Toast.LENGTH_LONG).show();
					}
				})/*.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				})*/
				.setNegativeButton("下载APK", new OnClickListener() {
					@Override
					public void onClick(View view) {
//						Log.d(TAG, "onClick: ");
						UpdateFromBaidu update = new UpdateFromBaidu(MainActivity.this);
						update.showNoticeDialog(true, "检测到新版本，立即更新吗？", url,getFilesDir().getPath());
						Log.d(TAG, "onClick: filepath"+getFilesDir().getPath());
					/**/}
				});
		mVersionDialog.setCanceledOnTouchOutside(false);
		//mVersionDialog.setCancelable(false);
		mVersionDialog.show();
	}

	private AbHttpUtil mNetHttpUtil = null;
	// 最大100
	private int max = 100;
	private SweetAlertDialog mSweetAlertDialog;
	private ProgressLoadingDialog mProgressLoadingDialog;

	private void updateDatabase(String url, final String dbname) { // 获取Http工具类
		mNetHttpUtil = AbHttpUtil.getInstance(MainActivity.this
				.getApplicationContext());
		mNetHttpUtil.setTimeout(10000);
		mNetHttpUtil.get(url, new AbFileHttpResponseListener(url) {

			// 获取数据成功会调用这里
			@Override
			public void onSuccess(int statusCode, File file) {
				Log.d(TAG, "onSuccess");
				// Bitmap bitmap = AbFileUtil.getBitmapFromSD(file);
				mPcDataBaseHelper.copyFile(file.getAbsolutePath(), dbname);
				// mPcUserDataDBHelper.updateAlertData();
				// mPcUserDataDBHelper.updateFavorite();
				mSharePrefrenceUtil.setKey("UPDATEFAV", "no");
				mSharePrefrenceUtil.setKey("UPDATEALERT", "no");
				mSweetAlertDialog = new SweetAlertDialog(MainActivity.this);
				mSweetAlertDialog.setTitleText("更新成功!").setConfirmText("OK")
						.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
				mSweetAlertDialog
						.setConfirmClickListener(new OnSweetClickListener() {

							@Override
							public void onClick(
									SweetAlertDialog sweetAlertDialog) {
								// TODO Auto-generated method stub
								mSweetAlertDialog.dismiss();
								startAlertService();
							}
						});
				mSweetAlertDialog.setCancelable(false);
				mSweetAlertDialog.setCanceledOnTouchOutside(false);
				mSweetAlertDialog.show();
				mSharePrefrenceUtil.setKey("DBVersion", dbVersion + "");
			}

			// 开始执行前
			@Override
			public void onStart() {
				Log.d(TAG, "onStart");
				// 打开进度框
				mProgressLoadingDialog = new ProgressLoadingDialog(
						MainActivity.this, R.style.Translucent_NoTitle);
				mProgressLoadingDialog.setCancelable(false);
				mProgressLoadingDialog.show();
			}

			// 失败，调用
			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				Log.d(TAG, "onFailure");
				// AbToastUtil.showToast(MainActivity.this, error.getMessage());

				if (mProgressLoadingDialog != null) {
					mProgressLoadingDialog.dismiss();
					mProgressLoadingDialog = null;
				}
				Toast.makeText(MainActivity.this, "更新失败", Toast.LENGTH_SHORT)
						.show();
				startAlertService();
			}

			// 下载进度
			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				if (totalSize != 0)
					mProgressLoadingDialog
							.setProgress((int) (bytesWritten / (totalSize / max)));
				else
					mProgressLoadingDialog.setProgress(0);
			}

			// 完成后调用，失败，成功
			public void onFinish() {
				// 下载完成取消进度框
				if (mProgressLoadingDialog != null) {
					mProgressLoadingDialog.dismiss();
					mProgressLoadingDialog = null;
				}

				Log.d(TAG, "onFinish");
			};

		});
	}
}
