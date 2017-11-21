package com.bnrc.ui.rjz;

import java.util.ArrayList;
import java.util.List;

import com.bnrc.busapp.PollingService;
import com.bnrc.busapp.PollingUtils;
import com.bnrc.busapp.R;
import com.bnrc.ui.listviewswipedelete.BlogItem;
import com.bnrc.ui.listviewswipedelete.DelBlogItem;
import com.bnrc.ui.listviewswipedelete.ListViewCompat;
import com.bnrc.ui.listviewswipedelete.SlideAdapter;
import com.bnrc.ui.listviewswipedelete.SlideView;
import com.bnrc.ui.listviewswipedelete.SlideView.OnSlideListener;
import com.bnrc.util.collectwifi.Constants;
import com.bnrc.util.collectwifi.ScanService;
import com.bnrc.util.collectwifi.ServiceUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AlertFragment extends BaseFragment implements OnItemClickListener {
	private static final String TAG = AlertFragment.class.getSimpleName();
	private Context mContext;
	private ListViewCompat mListView;
	private List<DelBlogItem> blogItems;
	private SlideAdapter adapter;
	private IPopWindowListener mChooseListener;
	private ImageButton menuSettingBtn;// 菜单呼出按钮

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = (Context) getActivity();
		mChooseListener = (IPopWindowListener) activity;
		Log.i("BaseFragment", TAG + " onAttach");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.activity_listview_swipe, null);

		mListView = (ListViewCompat) view.findViewById(R.id.list);
		blogItems = new ArrayList<DelBlogItem>();
		menuSettingBtn = (ImageButton) view.findViewById(R.id.menu_imgbtn);
		menuSettingBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mChooseListener.onLoginClick();
			}
		});
		DelBlogItem item = new DelBlogItem();
		item.setStation("明光桥北");
		item.setAlertState("提醒");
		blogItems.add(item);
		DelBlogItem item1 = new DelBlogItem();
		item1.setStation("西单");
		item1.setAlertState("提醒");
		blogItems.add(item1);
		DelBlogItem item2 = new DelBlogItem();
		item2.setStation("天通苑");
		item2.setAlertState("不提醒");
		blogItems.add(item2);
		DelBlogItem item3 = new DelBlogItem();
		item3.setStation("通州北苑");
		item3.setAlertState("不提醒");
		blogItems.add(item3);
		DelBlogItem item4 = new DelBlogItem();
		item4.setStation("大红门 ");
		item4.setAlertState("提醒");
		blogItems.add(item4);
		adapter = new SlideAdapter(mContext, blogItems);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Toast.makeText(mContext, "onItemClick position=" + position,
		// 0).show();

	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i("BaseFragment", TAG + " onStart");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop polling service

		Log.i("BaseFragment", TAG + " onDestroy");

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.i("BaseFragment", TAG + " onActivityCreated");

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("BaseFragment", TAG + " onCreate");

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.i("BaseFragment", TAG + " onDestroyView");

	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.i("BaseFragment", TAG + " onDetach");

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("BaseFragment", TAG + " onResume");

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("BaseFragment", TAG + " onStop");

	}

}
