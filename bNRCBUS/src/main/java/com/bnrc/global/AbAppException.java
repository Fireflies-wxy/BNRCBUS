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
package com.bnrc.global;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import com.bnrc.util.AbStrUtil;

// TODO: Auto-generated Javadoc

/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bAppException.java 
 * 鎻忚堪锛氬叕鍏卞紓甯哥被.
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-10-16 涓嬪崍1:33:39
 */
public class AbAppException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1;

	
	/** 寮傚父娑堟伅. */
	private String msg = null;

	/**
	 * 鏋勯�犲紓甯哥被.
	 *
	 * @param e 寮傚父
	 */
	public AbAppException(Exception e) {
		super();

		try {
			if( e instanceof HttpHostConnectException) {  
				msg = AbAppConfig.UNKNOWN_HOST_EXCEPTION;
			}else if (e instanceof ConnectException) {
				msg = AbAppConfig.CONNECT_EXCEPTION;
			}else if (e instanceof ConnectTimeoutException) {
				msg = AbAppConfig.CONNECT_EXCEPTION;
			}else if (e instanceof UnknownHostException) {
				msg = AbAppConfig.UNKNOWN_HOST_EXCEPTION;
			}else if (e instanceof SocketException) {
				msg = AbAppConfig.SOCKET_EXCEPTION;
			}else if (e instanceof SocketTimeoutException) {
				msg = AbAppConfig.SOCKET_TIMEOUT_EXCEPTION;
			}else if( e instanceof NullPointerException) {  
				msg = AbAppConfig.NULL_POINTER_EXCEPTION;
			}else if( e instanceof ClientProtocolException) {  
				msg = AbAppConfig.CLIENT_PROTOCOL_EXCEPTION;
			}else {
				if (e == null || AbStrUtil.isEmpty(e.getMessage())) {
					msg = AbAppConfig.NULL_MESSAGE_EXCEPTION;
				}else{
				    msg = e.getMessage();
				}
			}
		} catch (Exception e1) {
		}
		
	}

	/**
	 * 鐢ㄤ竴涓秷鎭瀯閫犲紓甯哥被.
	 *
	 * @param message 寮傚父鐨勬秷鎭�
	 */
	public AbAppException(String message) {
		super(message);
		msg = message;
	}

	/**
	 * 鎻忚堪锛氳幏鍙栧紓甯镐俊鎭�.
	 *
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return msg;
	}

}
