//package com.bnrc.multipallist;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.UserManager;
//import android.view.Gravity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.baidu.platform.comapi.map.G;
//import com.bnrc.busapp.R;
//import com.bnrc.multipallist.DemoAdapter.ViewHolder;
//import com.bnrc.ui.rtBus.Child;
//import com.bnrc.ui.rtBus.Group;
//import com.bnrc.util.UserDataDBHelper;
//import com.bnrc.util.collectwifi.BaseActivity;
//
//public class MultiActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
//
//	private Button mCancleBtn = null;
//
//	private ViewGroup mSelectAll = null;
//
//	private Button mSureBtn = null;
//
//	private ListView mListView = null;
//
//	private TextView mSelectAllText;
//	private DemoAdapter adpAdapter = null;
//	private List<Group> mGroups;
//	private List<Child> mChildren;
//	private UserDataDBHelper mUserDB = null;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setAbContentView(R.layout.activity_multi_list);
//		this.setTitleTextMargin(20, 0, 0, 0);
//		this.setLogo(R.drawable.button_selector_back);
//		this.setTitleLayoutBackground(R.drawable.top_bg);
//		this.setTitleLayoutGravity(Gravity.CENTER, Gravity.CENTER);
//		this.setTitleText("选择提醒线路");
//		Intent intent = getIntent();
//		Bundle bundle = intent.getExtras();
//		mGroups = (List<Group>) bundle.getSerializable("DATA");
//		mChildren = new ArrayList<Child>();
//		initView();
//		initData();
//		mUserDB = UserDataDBHelper.getInstance(MultiActivity.this);
//	}
//
//	private void initView() {
//
//		mCancleBtn = (Button) findViewById(R.id.btnCancle);
//		mCancleBtn.setOnClickListener(this);
//
//		mSureBtn = (Button) findViewById(R.id.btnSure);
//		mSureBtn.setOnClickListener(this);
//
//		mSelectAll = (ViewGroup) findViewById(R.id.rLayout_SelectAll);
//		mSelectAll.setOnClickListener(this);
//
//		mListView = (ListView) findViewById(R.id.lvListView);
//		mListView.setOnItemClickListener(this);
//		mSelectAllText = (TextView) findViewById(R.id.tv_SelectAll);
//	}
//
//	/**
//	 * ��ʼ����ͼ
//	 */
//	private void initData() {
//		generateChildren();
//		adpAdapter = new DemoAdapter(this, mChildren);
//		mListView.setAdapter(adpAdapter);
//
//	}
//
//	private void generateChildren() {
//		if (mGroups != null) {
//			for (Group group : mGroups) {
//				mChildren.addAll(group.getChildren());
//			}
//		}
//	}
//
//	@Override
//	public void onClick(View v) {
//
//		switch (v.getId()) {
//		case R.id.btnCancle:
//			finish();
//			break;
//		case R.id.btnSure:
//			//
//			// if (isAlert) {
//			// mUserDB.deleteAlertStation(stationName);
//			// appBtn.setBackgroundResource(R.drawable.icon_notalert);
//			// isAlert = false;
//			// } else {
//			// ArrayList<String> station = new ArrayList<String>();
//			// station.add(stationName);
//			// station.add(1 + "");
//			// station.add(mLatitude);
//			// station.add(mLongitude);
//			// station.add(AZ);
//			// mUserDB.addAlertRecord(station);
//			// appBtn.setBackgroundResource(R.drawable.icon_isalert);
//			// isAlert = true;
//			//
//			// }
//
//			Map<Integer, Boolean> map = adpAdapter.getCheckMap();
//			int count = adpAdapter.getCount();
//			ArrayList<String> station = new ArrayList<String>();
//			for (int i = 0; i < count; i++) {
//				int position = i - (count - adpAdapter.getCount());
//				Child child = (Child) adpAdapter.getItem(position);
//				if (map.get(i) != null && map.get(i)) {
//					String stationName = child.getStationName();
//					double latitude = child.getLatitude();
//					double longitude = child.getLongitude();
//					// String AZ = child.getAZ();
//					// String SN = child.getBuslineSN();
//					station.clear();
//					station.add(stationName);
//					// station.add(SN);
//					station.add(1 + "");
//					station.add(latitude + "");
//					station.add(longitude + "");
//					// station.add(AZ);
//					mUserDB.addAlertRecord(station);
//				}
//				// else
//				// mUserDB.deleteAlertStation(child.getBuslineSN());
//
//				// if (bean.isCanRemove()) {
//				// adpAdapter.getCheckMap().remove(i);
//				// adpAdapter.remove(position);
//				// } else {
//				// map.put(position, false);
//				// }
//
//			}
//			finish();
//			break;
//		case R.id.rLayout_SelectAll:
//			if (mSelectAllText.getText().toString().trim().equals("全选")) {
//				adpAdapter.configCheckMap(true);
//
//				adpAdapter.notifyDataSetChanged();
//
//				mSelectAllText.setText("全不选");
//			} else {
//				adpAdapter.configCheckMap(false);
//
//				adpAdapter.notifyDataSetChanged();
//
//				mSelectAllText.setText("全选");
//			}
//			break;
//		default:
//			break;
//		}
//
//		// if (v == mSureBtn) {
//		// Map<Integer, Boolean> map = adpAdapter.getCheckMap();
//		// int count = adpAdapter.getCount();
//		// for (int i = 0; i < count; i++) {
//		// int position = i - (count - adpAdapter.getCount());
//		// if (map.get(i) != null && map.get(i)) {
//		//
//		// Child bean = (Child) adpAdapter.getItem(position);
//		//
//		// if (bean.isCanRemove()) {
//		// adpAdapter.getCheckMap().remove(i);
//		// adpAdapter.remove(position);
//		// } else {
//		// map.put(position, false);
//		// }
//		//
//		// }
//		// }
//		// adpAdapter.notifyDataSetChanged();
//		// }
//
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> listView, View itemLayout, int position, long id) {
//
//		if (itemLayout.getTag() instanceof ViewHolder) {
//			ViewHolder holder = (ViewHolder) itemLayout.getTag();
//			holder.cbCheck.toggle();
//
//		}
//
//	}
//}
