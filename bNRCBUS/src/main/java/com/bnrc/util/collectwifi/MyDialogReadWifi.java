package com.bnrc.util.collectwifi;

import java.util.List;
import java.util.Map;

import com.bnrc.busapp.R;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView;
import android.widget.Toast;

public class MyDialogReadWifi extends Dialog implements
		android.view.View.OnClickListener {

	private Context context;
	private ListView readList;
	// private ItemDialogListener listener;
	private List<Map<String, String>> LocalSave;
	private MyReadDialogAdapter readAdapter;

	// interface ItemDialogListener {
	// public void onClick(View view);
	// }

	public MyDialogReadWifi(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public MyDialogReadWifi(Context context, int theme,
			List<Map<String, String>> LocalSave) {
		// TODO Auto-generated constructor stub
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;
		// this.listener = listener;
		this.LocalSave = LocalSave;
	}

	private void initView() {
		readList = (ListView) findViewById(R.id.dialog_readwifi_list);
		readAdapter = new MyReadDialogAdapter(context, LocalSave);
		readList.setAdapter(readAdapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_readwifi);
		initView();

	}

	public void onClick(View view) {
		// if (view.getId() == R.id.submit) {
		// Toast.makeText(context, "success!", Toast.LENGTH_LONG).show();
		// dismiss();

		// }
	}
}
