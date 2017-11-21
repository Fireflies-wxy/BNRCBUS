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

import android.os.Handler;
import android.os.Message;



// TODO: Auto-generated Javadoc
/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bHttpResponseListener.java 
 * 鎻忚堪锛欻ttp鍝嶅簲鐩戝惉鍣�
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-11-13 涓婂崍9:00:52
 */
public abstract class AbHttpResponseListener {
	
    /** The handler. */
    private Handler mHandler;
    
    /**
     * 鏋勯��.
     */
    public AbHttpResponseListener() {
		super();
	}

	/**
	 * 鎻忚堪锛氳幏鍙栨暟鎹紑濮�.
	 */
    public abstract void onStart();
    
    
    /**
	 * 瀹屾垚鍚庤皟鐢紝澶辫触锛屾垚鍔燂紝璋冪敤.
	 */
    public abstract void onFinish();
    
    /**
	 * 閲嶈瘯.
	 */
    public void onRetry() {}
    
    /**
     * 鎻忚堪锛氬け璐ワ紝璋冪敤.
     *
     * @param statusCode the status code
     * @param content the content
     * @param error the error
     */
    public abstract void onFailure(int statusCode, String content,Throwable error);
    
    /**
     * 杩涘害.
     *
     * @param bytesWritten the bytes written
     * @param totalSize the total size
     */
    public void onProgress(int bytesWritten, int totalSize) {}
    
    /**
     * 寮�濮嬫秷鎭�.
     */
    public void sendStartMessage(){
    	sendMessage(obtainMessage(AbHttpClient.START_MESSAGE, null));
    }
    
    /**
     * 瀹屾垚娑堟伅.
     */
    public void sendFinishMessage(){
    	sendMessage(obtainMessage(AbHttpClient.FINISH_MESSAGE,null));
    }
    
    /**
     * 杩涘害娑堟伅.
     *
     * @param bytesWritten the bytes written
     * @param totalSize the total size
     */
    public void sendProgressMessage(int bytesWritten, int totalSize) {
        sendMessage(obtainMessage(AbHttpClient.PROGRESS_MESSAGE, new Object[]{bytesWritten, totalSize}));
    }
    
    /**
     * 澶辫触娑堟伅.
     *
     * @param statusCode the status code
     * @param content the content
     * @param error the error
     */
    public void sendFailureMessage(int statusCode,String content,Throwable error){
    	sendMessage(obtainMessage(AbHttpClient.FAILURE_MESSAGE, new Object[]{statusCode,content, error}));
    }
    
    /**
     * 閲嶈瘯娑堟伅.
     */
    public void sendRetryMessage() {
        sendMessage(obtainMessage(AbHttpClient.RETRY_MESSAGE, null));
    }
    
    /**
     * 鍙戦�佹秷鎭�.
     * @param msg the msg
     */
    public void sendMessage(Message msg) {
        if (msg != null) {
        	msg.sendToTarget();
        }
    }
    
    /**
     * 鏋勯�燤essage.
     * @param responseMessage the response message
     * @param response the response
     * @return the message
     */
    protected Message obtainMessage(int responseMessage, Object response) {
        Message msg;
        if (mHandler != null) {
            msg = mHandler.obtainMessage(responseMessage, response);
        } else {
            msg = Message.obtain();
            if (msg != null) {
                msg.what = responseMessage;
                msg.obj = response;
            }
        }
        return msg;
    }

	/**
	 * Gets the handler.
	 *
	 * @return the handler
	 */
	public Handler getHandler() {
		return mHandler;
	}

	/**
	 * 鎻忚堪锛氳缃瓾andler.
	 *
	 * @param handler the new handler
	 */
	public void setHandler(Handler handler) {
		this.mHandler = handler;
	}

}
