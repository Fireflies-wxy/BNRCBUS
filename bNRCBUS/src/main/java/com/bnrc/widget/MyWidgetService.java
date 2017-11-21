package com.bnrc.widget;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.bnrc.busapp.MyCipher;
import com.bnrc.busapp.R;
import com.bnrc.ui.rtBus.Child;
import com.bnrc.ui.rtBus.Group;
import com.bnrc.util.PCUserDataDBHelper;
import com.bnrc.util.VolleyNetwork;
import com.bnrc.util.VolleyNetwork.requestListener;
import com.bnrc.util.VolleyNetwork.upLoadListener;
import com.bnrc.util.collectwifi.Constants;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

public class MyWidgetService extends RemoteViewsService {

	private static final String TAG = "MyWidgetService";

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new MyWidgetFactory(getApplicationContext(), intent);
	}

}