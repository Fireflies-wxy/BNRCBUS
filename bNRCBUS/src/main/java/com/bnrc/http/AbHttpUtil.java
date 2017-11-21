/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bnrc.http;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bHttpUtil.java 
 * 鎻忚堪锛欻ttp鎵ц宸ュ叿绫伙紝鍙鐞唃et锛宲ost锛屼互鍙婂紓姝ュ鐞嗘枃浠剁殑涓婁紶涓嬭浇
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-10-22 涓嬪崍4:15:52
 */
public class AbHttpUtil {
	
	/** 瀹炰緥鍖栧崟渚嬪璞�. */
	private AbHttpClient mClient = null;
	
	/** 宸ュ叿绫诲崟渚�. */
	private static AbHttpUtil mAbHttpUtil = null;
	
	
	/**
	 * 鎻忚堪锛氳幏鍙栧疄渚�.
	 *
	 * @param context the context
	 * @return single instance of AbHttpUtil
	 */
	public static AbHttpUtil getInstance(Context context){
	    if (null == mAbHttpUtil){
	    	mAbHttpUtil = new AbHttpUtil(context);
	    }
	    
	    return mAbHttpUtil;
	}
	
	/**
	 * 鍒濆鍖朅bHttpUtil.
	 *
	 * @param context the context
	 */
	private AbHttpUtil(Context context) {
		super();
		this.mClient = new AbHttpClient(context);
	}
	
	/**
	 * 鎻忚堪锛氭棤鍙傛暟鐨刧et璇锋眰.
	 *
	 * @param url the url
	 * @param responseListener the response listener
	 */
	public void get(String url, AbHttpResponseListener responseListener) {
		mClient.get(url,null,responseListener);
	}
	
	/**
	 * 鎻忚堪锛氬甫鍙傛暟鐨刧et璇锋眰.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void get(String url, AbRequestParams params,
			AbHttpResponseListener responseListener) {
		mClient.get(url, params, responseListener);
	}
	
	/**
	 *  
	 * 鎻忚堪锛氫笅杞芥暟鎹娇鐢紝浼氳繑鍥瀊yte鏁版嵁(涓嬭浇鏂囦欢鎴栧浘鐗�).
	 *
	 * @param url the url
	 * @param responseListener the response listener
	 */
	public void get(String url, AbBinaryHttpResponseListener responseListener) {
		mClient.get(url,null,responseListener);
	}
	
	/**
	 * 鎻忚堪锛氭枃浠朵笅杞界殑get.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void get(String url, AbRequestParams params,
			AbFileHttpResponseListener responseListener) {
		mClient.get(url, params, responseListener);
	}
	
	/**
	 * 鎻忚堪锛氭棤鍙傛暟鐨刾ost璇锋眰.
	 *
	 * @param url the url
	 * @param responseListener the response listener
	 */
	public void post(String url, AbHttpResponseListener responseListener) {
		mClient.post(url,null, responseListener);
	}
	
	/**
	 * 鎻忚堪锛氬甫鍙傛暟鐨刾ost璇锋眰.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void post(String url, AbRequestParams params,
			AbHttpResponseListener responseListener) {
		mClient.post(url, params, responseListener);
	}
	
	/**
	 * 鎻忚堪锛氭枃浠朵笅杞界殑post.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void post(String url, AbRequestParams params,
			AbFileHttpResponseListener responseListener) {
		mClient.post(url, params, responseListener);
	}
	
	/**
	 * 鎻忚堪锛氳缃繛鎺ヨ秴鏃舵椂闂�(绗竴娆¤姹傚墠璁剧疆).
	 *
	 * @param timeout 姣
	 */
	public void setTimeout(int timeout) {
		mClient.setTimeout(timeout);
	}
	
    /**
     * 鎵撳紑ssl 鑷鍚�(绗竴娆¤姹傚墠璁剧疆).
     * @param enabled
     */
    public void setEasySSLEnabled(boolean enabled){
    	mClient.setOpenEasySSL(enabled);
    }

    /**
	 * 璁剧疆缂栫爜(绗竴娆¤姹傚墠璁剧疆).
	 * @param encode
	 */
	public void setEncode(String encode) {
		mClient.setEncode(encode);
	}
	
	/**
     * 璁剧疆鐢ㄦ埛浠ｇ悊(绗竴娆¤姹傚墠璁剧疆).
     * @param userAgent
     */
	public void setUserAgent(String userAgent) {
		mClient.setUserAgent(userAgent);
	}
	
	/**
	 * 鍏抽棴HttpClient
	 * 褰揌ttpClient瀹炰緥涓嶅啀闇�瑕佹槸锛岀‘淇濆叧闂璫onnection manager锛屼互閲婃斁鍏剁郴缁熻祫婧�  
	 */
	public void shutdownHttpClient(){
	    if(mClient != null){
	    	mClient.shutdown();
	    }
	}
	
}
