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

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bHttpListener.java 
 * 鎻忚堪锛欻ttp鍝嶅簲鐩戝惉鍣�
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2014-08-05 涓婂崍9:00:52
 */
public abstract class AbHttpListener{
	
    /**
     * 鏋勯��.
     */
	public AbHttpListener() {
		super();
	}

	/**
	 * 璇锋眰鎴愬姛.
	 *
	 * @param content the content
	 */
    public void onSuccess(String content){};
    
    /**
	 * 璇锋眰鎴愬姛.
	 *
	 * @param list the list
	 */
    public void onSuccess(List<?> list){};
    
    /**
     * 璇锋眰澶辫触.
     * @param content the content
     */
    public abstract void onFailure(String content);
    
    
    /**
	 * 鎻忚堪锛氳幏鍙栨暟鎹紑濮�.
	 */
    public void onStart(){};
    
    
    /**
	 * 瀹屾垚鍚庤皟鐢紝澶辫触锛屾垚鍔燂紝璋冪敤.
	 */
    public void onFinish(){};

}
