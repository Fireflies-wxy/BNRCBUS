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
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

// TODO: Auto-generated Javadoc
/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bRequestParams.java 
 * 鎻忚堪锛欻ttp璇锋眰鍙傛暟
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-11-13 涓婂崍10:28:55
 */
public class AbRequestParams {
	
    /** url鍙傛暟. */
    protected ConcurrentHashMap<String, String> urlParams;
    
    /** 鏂囦欢鍙傛暟. */
    protected ConcurrentHashMap<String, FileWrapper> fileParams;
    
    /** 娴佸父閲�. */
    private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

    /**
     * 鏋勯�犱竴涓┖鐨勮姹傚弬鏁�.
     */
    public AbRequestParams() {
        init();
    }

    /**
     * 鐢ㄤ竴涓猰ap鏋勯�犺姹傚弬鏁�.
     *
     * @param source the source
     */
    public AbRequestParams(Map<String, String> source) {
        init();

        for (Map.Entry<String, String> entry : source.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 鐢ㄤ竴涓猭ey鍜寁alue鏋勯�犺姹傚弬鏁�.
     *
     * @param key the key
     * @param value the value
     */
    public AbRequestParams(String key, String value) {
        init();
        put(key, value);
    }
    
    /**
     * 鍒濆鍖�.
     */
    private void init() {
        urlParams = new ConcurrentHashMap<String, String>();
        fileParams = new ConcurrentHashMap<String, FileWrapper>();
    }


    /**
     * 澧炲姞涓�瀵硅姹傚弬鏁�.
     *
     * @param key the key
     * @param value the value
     */
    public void put(String key, String value) {
        if (key != null && value != null) {
        	urlParams.put(encode(key), encode(value));
        }
    }
    
    /**
     * 澧炲姞涓�涓枃浠跺煙.
     *
     * @param key the key
     * @param file the file
     * @param contentType the content type of the file, eg. application/json
     */
    public void put(String key, File file,String contentType) {
        if (key != null && file != null) {
        	fileParams.put(encode(key), new FileWrapper(file,contentType));
        }
    }
    
    /**
     * 澧炲姞涓�涓枃浠跺煙.
     *
     * @param key the key
     * @param file the file
     */
    public void put(String key, File file) {
    	put(encode(key),file,APPLICATION_OCTET_STREAM);
    }
    
    
    /**
     * 鍒犻櫎涓�涓姹傚弬鏁�.
     *
     * @param key the key
     */
    public void remove(String key) {
        urlParams.remove(encode(key));
        fileParams.remove(encode(key));
    }

    /**
     * 鎻忚堪锛氳浆鎹负鍙傛暟瀛楃涓�.
     *
     * @return the string
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            if (result.length() > 0)
                result.append("&");

            result.append(decode(entry.getKey()));
            result.append("=");
            result.append(decode(entry.getValue()));
        }

        return result.toString();
    }

    /**
     * 鑾峰彇鍙傛暟鍒楄〃.
     * @return the params list
     */
    public List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> paramsList = new LinkedList<BasicNameValuePair>();
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
        	paramsList.add(new BasicNameValuePair(decode(entry.getKey()), decode(entry.getValue())));
        }
        return paramsList;
    }

    
    /**
     * 鑾峰彇鍙傛暟瀛楃涓�.
     * @return the param string
     */
    public String getParamString() {
        return URLEncodedUtils.format(getParamsList(), HTTP.UTF_8);
    }
    
   /**
    * 鑾峰彇HttpEntity.
    *
    * @param responseListener the response listener
    * @return the entity
    * @throws IOException Signals that an I/O exception has occurred.
    */
    public HttpEntity getEntity(AbHttpResponseListener responseListener) throws IOException {
        
    	//涓嶅寘鍚枃浠剁殑
    	if (fileParams.isEmpty()) {
            return createFormEntity();
        } else {
        	//鍖呭惈鏂囦欢鍜屽弬鏁扮殑
            return createMultipartEntity(responseListener);
        }
    }
    
    /**
     * 鍒涘缓HttpEntity.
     * @return the http entity
     */
    public HttpEntity createFormEntity() {
        try {
            return new UrlEncodedFormEntity(getParamsList(), HTTP.UTF_8);
        } catch (Exception e) {
        	e.printStackTrace();
            return null; 
        }
    }
    
    /**
     * 鎻忚堪锛氬垱寤烘枃浠跺煙HttpEntity.
     *
     * @param responseListener the response listener
     * @return the http entity
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private HttpEntity createMultipartEntity(AbHttpResponseListener responseListener) throws IOException {
    	AbMultipartEntity entity = new AbMultipartEntity(responseListener);

        // Add string params
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            entity.addPart(entry.getKey(), entry.getValue());
        }

        // Add file params
        for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
            FileWrapper fileWrapper = entry.getValue();
            entity.addPart(entry.getKey(), fileWrapper.file, fileWrapper.contentType);
        }

        return entity;
    }

    /**
     * 鑾峰彇url鍙傛暟.
     *
     * @return the url params
     */
	public ConcurrentHashMap<String, String> getUrlParams() {
		return urlParams;
	}
	
	

	/**
	 * Gets the file params.
	 *
	 * @return the file params
	 */
	public ConcurrentHashMap<String, FileWrapper> getFileParams() {
		return fileParams;
	}

	/**
	 * Sets the file params.
	 *
	 * @param fileParams the file params
	 */
	public void setFileParams(ConcurrentHashMap<String, FileWrapper> fileParams) {
		this.fileParams = fileParams;
	}

	/**
	 * 璁剧疆url鍙傛暟.
	 *
	 * @param urlParams the url params
	 */
	public void setUrlParams(ConcurrentHashMap<String, String> urlParams) {
		this.urlParams = urlParams;
	}
	
	/**
	 * 
	 * 鎻忚堪锛氬弬鏁拌浆鎹�.
	 * @param s
	 * @return
	 */
	public String encode(String s){
		try {
			return URLEncoder.encode(s,HTTP.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * 
	 * 鎻忚堪锛氬弬鏁拌浆鎹�.
	 * @param s
	 * @return
	 */
	public String decode(String s){
		try {
			return URLDecoder.decode(s,HTTP.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	
	/**
     * 鏂囦欢绫�.
     */
    private static class FileWrapper {
        
        /** 鏂囦欢. */
        public File file;
        
        /** 绫诲瀷. */
        public String contentType;

        /**
         * 鏋勯��.
         * @param file the file
         * @param contentType the content type
         */
        public FileWrapper(File file, String contentType) {
            this.file = file;
            this.contentType = contentType;
        }
    }
    
    
}
