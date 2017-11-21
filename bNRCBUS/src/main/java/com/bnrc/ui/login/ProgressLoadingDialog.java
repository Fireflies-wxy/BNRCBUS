package com.bnrc.ui.login;

import com.bnrc.busapp.R;
import com.zhy.view.HorizontalProgressBarWithNumber;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * Description: Created by ZJ on 16/3/3.
 */
public class ProgressLoadingDialog extends Dialog {
	private Context context;
	private HorizontalProgressBarWithNumber loading;
	private AnimationDrawable animationDrawable;
	private OnCancelListener mOnCancelListener = null;

	public ProgressLoadingDialog(Context context) {
		super(context);
		this.context = context;
	}

	protected ProgressLoadingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.context = context;

	}

	public ProgressLoadingDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}

	public void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.download_progress_bar_horizontal,
				null);
		setContentView(view);
		loading = (HorizontalProgressBarWithNumber) view
				.findViewById(R.id.id_progressbar);
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(false);
	}

	public void setProgress(int num) {
		loading.setProgress(num);
	}

}
