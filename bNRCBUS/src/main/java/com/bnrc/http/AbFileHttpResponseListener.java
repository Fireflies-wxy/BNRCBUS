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

import java.io.File;

import android.content.Context;

import com.bnrc.util.AbFileUtil;

// TODO: Auto-generated Javadoc
/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bBinaryHttpResponseListener.java 
 * 鎻忚堪锛欻ttp鏂囦欢鍝嶅簲鐩戝惉鍣�
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-11-13 涓婂崍9:00:52
 */
public abstract class AbFileHttpResponseListener extends AbHttpResponseListener{
	
    /** 褰撳墠缂撳瓨鏂囦欢. */
    private File mFile;
    
    /**
     * 涓嬭浇鏂囦欢鐨勬瀯閫�,鐢ㄩ粯璁ょ殑缂撳瓨鏂瑰紡.
     *
     * @param url the url
     */
	public AbFileHttpResponseListener(String url) {
		super();
	}
	
	/**
	 * 榛樿鐨勬瀯閫�.
	 */
	public AbFileHttpResponseListener() {
		super();
	}
	
	/**
     * 涓嬭浇鏂囦欢鐨勬瀯閫�,鎸囧畾缂撳瓨鏂囦欢鍚嶇О.
     * @param file 缂撳瓨鏂囦欢鍚嶇О
     */
    public AbFileHttpResponseListener(File file) {
        super();
	    this.mFile = file;
    }
	
	/**
	 * 鎻忚堪锛氫笅杞芥枃浠舵垚鍔熶細璋冪敤杩欓噷.
	 *
	 * @param statusCode the status code
	 * @param file the file
	 */
    public void onSuccess(int statusCode,File file){};
    
    /**
     * 鎻忚堪锛氬鏂囦欢涓婁紶鎴愬姛璋冪敤.
     *
     * @param statusCode the status code
     */
    public void onSuccess(int statusCode){};
    
   
   /**
    * 鎴愬姛娑堟伅.
    *
    * @param statusCode the status code
    */
    public void sendSuccessMessage(int statusCode){
    	sendMessage(obtainMessage(AbHttpClient.SUCCESS_MESSAGE, new Object[]{statusCode}));
    }
    
    /**
     * 澶辫触娑堟伅.
     *
     * @param statusCode the status code
     * @param error the error
     */
    public void sendFailureMessage(int statusCode,Throwable error){
    	sendMessage(obtainMessage(AbHttpClient.FAILURE_MESSAGE, new Object[]{statusCode, error}));
    }
    

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return mFile;
	}

	/**
	 * Sets the file.
	 *
	 * @param file the new file
	 */
	public void setFile(File file) {
		this.mFile = file;
		try {
			if(!file.getParentFile().exists()){
			      file.getParentFile().mkdirs();
			}
			if(!file.exists()){
			      file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Sets the file.
	 *
	 * @param context the context
	 * @param name the name
	 */
	public void setFile(Context context,String name) {
		//鐢熸垚缂撳瓨鏂囦欢
        if(AbFileUtil.isCanUseSD()){
	    	File file = new File(AbFileUtil.getFileDownloadDir(context) + name);
	    	setFile(file);
        }
	}
    
}
