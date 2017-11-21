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

// TODO: Auto-generated Javadoc
/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bStringHttpResponseListener.java 
 * 鎻忚堪锛欻ttp瀛楃涓插搷搴旂洃鍚櫒
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-11-13 涓婂崍9:00:52
 */
public abstract class AbStringHttpResponseListener extends AbHttpResponseListener{
	
    /**
     * 鏋勯��.
     */
	public AbStringHttpResponseListener() {
		super();
	}

	/**
	 * 鎻忚堪锛氳幏鍙栨暟鎹垚鍔熶細璋冪敤杩欓噷.
	 *
	 * @param statusCode the status code
	 * @param content the content
	 */
    public abstract void onSuccess(int statusCode,String content);
    
    
    /**
     * 鎴愬姛娑堟伅.
     *
     * @param statusCode the status code
     * @param content the content
     */
    public void sendSuccessMessage(int statusCode,String content){
    	sendMessage(obtainMessage(AbHttpClient.SUCCESS_MESSAGE, new Object[]{statusCode, content}));
    }
		

}
