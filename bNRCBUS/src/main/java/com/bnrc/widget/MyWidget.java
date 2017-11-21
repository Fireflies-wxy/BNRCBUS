package com.bnrc.widget;

import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

import com.bnrc.busapp.R;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class MyWidget extends AppWidgetProvider {

	private static final String TAG = "zhangjia05";
	private final String EXAMPLE_SERVICE_INTENT = "android.appwidget.action.EXAMPLE_APP_WIDGET_SERVICE";
	private static Set<Integer> idsSet = new HashSet();

	/** package */
	static ComponentName getComponentName(Context context) {
		return new ComponentName(context, MyWidget.class);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.d(TAG, "onUpdate");
		for (int appWidgetId : appWidgetIds) {
			idsSet.add(Integer.valueOf(appWidgetId));
		}
		performUpdate(context, appWidgetManager, appWidgetIds, null);
	}

	private void performUpdate(Context context,
			AppWidgetManager appWidgetManager, int[] appWidgetIds,
			long[] changedEvents) {
		for (int appWidgetId : appWidgetIds) {
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
	}

	@Override
	public void onEnabled(Context context) {
		Log.i(TAG, "onEnabled");
		Intent intent = new Intent();
		intent.setAction(EXAMPLE_SERVICE_INTENT);
		// intent.setPackage("com.bnrc.widget");
		context.startService(intent);
		Log.i(TAG, "startService");

		// Enter relevant functionality for when the first widget is created
	}

	@Override
	public void onDisabled(Context context) {
		Log.i(TAG, "onDisabled");
		Intent intent = new Intent();
		intent.setAction(EXAMPLE_SERVICE_INTENT);
		// intent.setPackage("com.bnrc.widget");
		context.stopService(intent);
		// Enter relevant functionality for when the last widget is disabled
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		Log.i(TAG, "onReceive");
		for (int id : idsSet) {
			updateAppWidget(context, AppWidgetManager.getInstance(context), id);
		}

	}

	private void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {
		Log.d(TAG, "appWidgetId = " + appWidgetId);
		Intent intent = new Intent(context, MyWidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.layout_widget);
		views.setRemoteAdapter(R.id.listView, intent);

		appWidgetManager.updateAppWidget(appWidgetId, views);

		appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
				R.id.listView);
	}

	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);
		Log.i(TAG, "onAppWidgetOptionsChanged");
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Log.i(TAG, "onDeleted");
		for (int appWidgetId : appWidgetIds) {
			idsSet.remove(Integer.valueOf(appWidgetId));
		}
	}

}
