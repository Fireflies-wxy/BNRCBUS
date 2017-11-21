package com.bnrc.util;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.overlayutil.PoiOverlay;

public  class MyPoiOverlay extends PoiOverlay {  
    public MyPoiOverlay(BaiduMap baiduMap) {  
        super(baiduMap);  
    }  
    @Override  
    public boolean onPoiClick(int index) {  
        super.onPoiClick(index);  
        return true;  
    }  
}
