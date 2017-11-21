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
package com.bnrc.task;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

import android.os.Handler;
import android.os.Message;
// TODO: Auto-generated Javadoc
/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bTaskPool.java 
 * 鎻忚堪锛氱敤andbase绾跨▼姹�
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-5-23 涓婂崍10:10:53
 */

public class AbTaskPool{
	
	/** 鍗曚緥瀵硅薄 The http pool. */
	private static AbTaskPool abTaskPool = null; 
	
	/** 绾跨▼鎵ц鍣�. */
	public static Executor mExecutorService = null;
	
	/**  瀛樻斁杩斿洖鐨勪换鍔＄粨鏋�. */
    private static HashMap<String,Object> result;
	
	/** 涓嬭浇瀹屾垚鍚庣殑娑堟伅鍙ユ焺. */
    private static Handler handler = new Handler() { 
        @Override 
        public void handleMessage(Message msg) { 
        	AbTaskItem item = (AbTaskItem)msg.obj; 
        	if(item.getListener() instanceof AbTaskListListener){
        		((AbTaskListListener)item.getListener()).update((List<?>)result.get(item.toString())); 
        	}else if(item.getListener() instanceof AbTaskObjectListener){
        		((AbTaskObjectListener)item.getListener()).update(result.get(item.toString())); 
        	}else{
        		item.getListener().update(); 
        	}
        	result.remove(item.toString());
        } 
    }; 
    
	
	/**
	 * 鏋勯�犵嚎绋嬫睜.
	 */
    protected AbTaskPool() {
        result = new HashMap<String,Object>();
        mExecutorService = AbThreadFactory.getExecutorService();
    } 
	
	/**
	 * 鍗曚緥鏋勯�犲浘鐗囦笅杞藉櫒.
	 *
	 * @return single instance of AbHttpPool
	 */
    public static AbTaskPool getInstance() { 
    	if (abTaskPool == null) { 
    		abTaskPool = new AbTaskPool(); 
        } 
        return abTaskPool;
    } 
    
    /**
     * 鎵ц浠诲姟.
     * @param item the item
     */
    public void execute(final AbTaskItem item) {   
    	mExecutorService.execute(new Runnable() { 
    		public void run() {
    			try {
    				//瀹氫箟浜嗗洖璋�
                    if (item.getListener() != null) { 
                        if(item.getListener() instanceof AbTaskListListener){
                            result.put(item.toString(), ((AbTaskListListener)item.getListener()).getList());
                        }else if(item.getListener() instanceof AbTaskObjectListener){
                            result.put(item.toString(), ((AbTaskObjectListener)item.getListener()).getObject());
                        }else{
                        	item.getListener().get();
                            result.put(item.toString(), null);
                        }
                        
                    	//浜ょ敱UI绾跨▼澶勭悊 
                        Message msg = handler.obtainMessage(); 
                        msg.obj = item; 
                        handler.sendMessage(msg); 
                    }                              
    			} catch (Exception e) { 
    				e.printStackTrace();
    			}                         
    		}                 
    	});                 
    	
    }
	
}
