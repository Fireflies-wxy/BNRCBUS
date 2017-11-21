package com.bnrc.ui.rjz;

import java.util.Currency;
import java.util.List;

import com.bnrc.busapp.R;
import com.bnrc.ui.login.CustomProgressDialog;
import com.bnrc.ui.login.LoadingDialog;
import com.bnrc.ui.rtBus.Group;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {
	private static final String TAG = BaseFragment.class.getSimpleName();

	// 刷新实时数据
	public void refresh() {

	}

	// 刷新实时数据
	public void refreshConcern() {

	}

	public int getFirstVisiblePosition() {
		return 0;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG, this.getClass().getSimpleName() + "onCreateView");

		return super.onCreateView(inflater, container, savedInstanceState);
	}

}
