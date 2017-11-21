package com.bnrc.ui.rjz;

import java.util.ArrayList;
import java.util.Map;

import com.bnrc.ui.login.LoadingDialog;
import com.bnrc.ui.rtBus.Child;

import android.content.Intent;
import android.view.View;

public interface IPopWindowListener {
	void onPopClick(Child child);

	void onLoginClick();

	LoadingDialog showLoading();

	void dismissLoading();
}
