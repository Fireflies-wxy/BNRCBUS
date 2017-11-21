package com.bnrc.busapp;

import com.ab.view.titlebar.AbTitleBar;
import com.bnrc.util.AnimationUtil;
import com.bnrc.util.VolleyNetwork;
import com.bnrc.util.VolleyNetwork.reportListener;
import com.bnrc.util.collectwifi.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UploadMistakeActivity extends BaseActivity {
	private AbTitleBar mAbTitleBar;
	private EditText mEdit;
	private TextView mCountText;
	private TextView mMistakeType;
	private String mistakeName = "";
	private VolleyNetwork mVolleyNetwork;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.upload_mistake_activity);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextSize(20);
		mAbTitleBar.setTitleTextMargin(0, 0, 0, 0);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarGravity(Gravity.CENTER, Gravity.CENTER);
		mAbTitleBar.setTitleText("纠错描述");
		initbase();
		Intent intent = getIntent();
		mistakeName = intent.getStringExtra("MistakeName");
		mEdit = (EditText) findViewById(R.id.edt_mistake);
		mCountText = (TextView) findViewById(R.id.tv_count);
		mMistakeType = (TextView) findViewById(R.id.tv_mistakeType);
		mMistakeType.setText(mistakeName);
		mEdit.addTextChangedListener(new MaxLengthWatcher(150, mEdit,
				mCountText));
		initTitleRightLayout();
		mVolleyNetwork = VolleyNetwork
				.getInstance(this.getApplicationContext());
	}

	private void initTitleRightLayout() {
		mAbTitleBar.clearRightView();

		View rightViewApp = LayoutInflater.from(UploadMistakeActivity.this)
				.inflate(R.layout.app_btn2, null);

		final TextView appBtn = (TextView) rightViewApp
				.findViewById(R.id.appBtn);

		appBtn.setTextColor(Color.WHITE);

		appBtn.setTextSize(18);
		appBtn.setText("提交");
		mAbTitleBar.addRightView(rightViewApp);

		appBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = mEdit.getText().toString();
				if (content.length() <= 0) {
					Toast.makeText(UploadMistakeActivity.this, "请具体描述一下错误情况~",
							1000).show();
					return;
				}
				mVolleyNetwork.reportMistake(mistakeName, content,
						new reportListener() {

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								Toast.makeText(UploadMistakeActivity.this,
										"提交成功", 1000).show();
							}

							@Override
							public void onFail() {
								// TODO Auto-generated method stub
								Toast.makeText(UploadMistakeActivity.this,
										"提交失败，请检查网络~", 1000).show();
							}
						});
			}

		});

		MobclickAgent.updateOnlineConfig(this);
	}

	@Override
	public void onPause() {
		super.onPause(); // 关闭软键盘
		mEdit.clearFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEdit.getWindowToken(), 0);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	class MaxLengthWatcher implements TextWatcher {

		private int maxLen = 0;
		private EditText editText = null;
		private TextView textview = null;

		public MaxLengthWatcher(int maxLen, EditText editText, TextView textview) {
			this.maxLen = maxLen;
			this.editText = editText;
			this.textview = textview;
		}

		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			Editable editable = editText.getText();
			int len = editable.length();
			textview.setText(len + "/150");
			if (len >= maxLen) {
				// int selEndIndex = Selection.getSelectionEnd(editable);
				// String str = editable.toString();
				// //截取新字符串
				// String newStr = str.substring(0,maxLen);
				// editText.setText(newStr);
				// editable = editText.getText();
				//
				// //新字符串的长度
				// int newLen = editable.length();
				// //旧光标位置超过字符串长度
				// if(selEndIndex > newLen)
				// {
				// selEndIndex = editable.length();
				// }
				// //设置新光标所在的位置
				// Selection.setSelection(editable, selEndIndex);
				Toast.makeText(UploadMistakeActivity.this, "超出字数限制了", 1000)
						.show();
			}
			if (len == 0)
				textview.setHint("请简要描述您发现的错误");
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

	}

}
