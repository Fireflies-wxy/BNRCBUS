package com.bnrc.ui.subview;

import com.bnrc.busapp.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by frank on 16/1/3.
 */
public class DefaultFooterView extends com.bnrc.ui.refresh.OverScrollFooterLayout {

	RotateLoading mLoadingMoreView;
	View mPullToLoadMoreTipsView;

	public DefaultFooterView(Context context) {
		super(context);
	}

	public DefaultFooterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected View getContentView(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.default_pull_load_more_footer, null);
		mLoadingMoreView = (RotateLoading) view.findViewById(R.id.footer_loading);
		mPullToLoadMoreTipsView = view.findViewById(R.id.footer_to_load);
		return view;
	}

	@Override
	public void onLoadingMore() {

		mLoadingMoreView.setVisibility(View.VISIBLE);
		mPullToLoadMoreTipsView.setVisibility(View.GONE);
		if (!mLoadingMoreView.isStart()) {
			mLoadingMoreView.start();
		}
	}

	@Override
	public void onPullToLoadMore() {
		mLoadingMoreView.setVisibility(View.GONE);
		mPullToLoadMoreTipsView.setVisibility(View.VISIBLE);
	}
}
