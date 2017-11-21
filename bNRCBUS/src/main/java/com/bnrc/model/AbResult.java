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
package com.bnrc.model;

import com.bnrc.util.AbJsonUtil;

// TODO: Auto-generated Javadoc
/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bResult.java 
 * 鎻忚堪锛氱粨鏋�
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-11-13 涓婂崍9:00:52
 */
public class AbResult {
	
	/** 杩斿洖鐮侊細鎴愬姛. */
    public static final int RESULT_OK = 0;
    
    /** 杩斿洖鐮侊細澶辫触. */
    public static final int RESULT_ERROR = -1;

	/** 缁撴灉code. */
	private int resultCode;
	
	/** 缁撴灉 message. */
	private String resultMessage;
	
    /**
     * 鏋勯��
     */
	public AbResult() {
		super();
	}

	/**
	 * 鏋勯��
	 * @param resultCode
	 * @param resultMessage
	 */
	public AbResult(int resultCode, String resultMessage) {
		super();
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
	}
	
	/**
	 * 鐢╦son鏋勯�犺嚜宸�
	 * @param json
	 */
	public AbResult(String json) {
		super();
		AbResult result = (AbResult)AbJsonUtil.fromJson(json, this.getClass());
		this.resultCode = result.getResultCode();
		this.resultMessage = result.getResultMessage();
	}

	/**
	 * 鑾峰彇杩斿洖鐮�.
	 *
	 * @return the result code
	 */
	public int getResultCode() {
		return resultCode;
	}

	/**
	 * 璁剧疆杩斿洖鐮�.
	 *
	 * @param resultCode the new result code
	 */
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * 鑾峰彇杩斿洖淇℃伅.
	 *
	 * @return the result message
	 */
	public String getResultMessage() {
		return resultMessage;
	}

	/**
	 * 璁剧疆杩斿洖淇℃伅.
	 *
	 * @param resultMessage the new result message
	 */
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
	/**
	 * 
	 * 鎻忚堪锛氳浆鎹㈡垚json.
	 * @return
	 */
	public String toJson(){
		return AbJsonUtil.toJson(this);
	}
	

}
