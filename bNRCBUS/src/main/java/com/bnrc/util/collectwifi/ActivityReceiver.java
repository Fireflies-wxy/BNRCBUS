package com.bnrc.util.collectwifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ActivityReceiver extends BroadcastReceiver {
	private static final String TAG = "ActivityReceiver";
	private NotifyAdmin mNotifyAdmin = null;

	@Override
	public void onReceive(Context pContext, Intent intent) {
		String action = intent.getAction();
		Log.i(TAG, "ActivityReceiver_pContext: " + pContext + " action: "
				+ action);

		if (action.trim().equals(Constants.UPDATE_ACTION)) {// 后台扫描到确定的wifi通知Activity弹出对话框进行选择

			String update = intent.getStringExtra("update");
			Log.i(TAG,
					"MainActivity_ActivityReceiver_action_update: "
							+ update.toString());
			if (!NotifyAdmin.IsShow) {
				Log.i(TAG, "到弹出dialog这里了!~");
				mNotifyAdmin = new NotifyAdmin(pContext, update);
				mNotifyAdmin.showNotification();
				NotifyAdmin.IsShow = true;
				Toast.makeText(pContext, "系统确定到您正在乘坐公交车，请选择所乘坐的线路！！！！", 1000)
						.show();

			}
		}

	}
}
