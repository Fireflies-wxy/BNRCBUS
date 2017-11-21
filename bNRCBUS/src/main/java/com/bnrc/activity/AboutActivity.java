package com.bnrc.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.bnrc.busapp.R;

public class AboutActivity extends AbActivity {
	String version = null;
	private AbTitleBar mAbTitleBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.about);
		mAbTitleBar = this.getTitleBar();

		mAbTitleBar.setTitleText(R.string.about);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);

		// logoView.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// finish();
		// }
		// });
		TextView version_val = ((TextView) findViewById(R.id.version_val));

		try {
			PackageInfo pinfo = getPackageManager().getPackageInfo(
					"com.andbase", PackageManager.GET_CONFIGURATIONS);
			version = pinfo.versionName;
			version_val.setText("V" + version);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
