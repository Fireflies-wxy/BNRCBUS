package com.bnrc.util;

import java.util.List;

import org.json.JSONException;

import android.content.Context;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.overlayutil.BusLineOverlay;
import com.bnrc.busapp.BuslineMapView;

public class StationOverlay extends BusLineOverlay {

	public MapView mMapView;
	public BaiduMap mBaiduMap = null;
	public Context mCtx;

	public StationOverlay(BaiduMap arg0, Context context) {
		super(arg0);

		// TODO Auto-generated constructor stub
		mBaiduMap = arg0;
		mCtx = context;
	}

	// @Override
	public boolean onBusStationClick(int index) {
		try {
			((BuslineMapView) mCtx).showStationView(index);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
