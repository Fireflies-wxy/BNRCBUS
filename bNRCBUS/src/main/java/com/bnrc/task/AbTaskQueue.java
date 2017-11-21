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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

import android.os.Handler;
import android.os.Message;

import com.bnrc.util.AbLogUtil;

// TODO: Auto-generated Javadoc

/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bTaskQueue.java 
 * 鎻忚堪锛氱嚎绋嬮槦鍒�.
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2011-11-10 涓嬪崍11:52:13
 */
public class AbTaskQueue extends Thread { 
	
	/** 绛夊緟鎵ц鐨勪换鍔�. 鐢� LinkedList澧炲垹鏁堢巼楂�*/
	private static LinkedList<AbTaskItem> mAbTaskItemList = null;
    
    /** 鍗曚緥瀵硅薄. */
  	private static AbTaskQueue abTaskQueue = null; 
  	
  	/** 鍋滄鐨勬爣璁�. */
	private boolean mQuit = false;
	
	/**  瀛樻斁杩斿洖鐨勪换鍔＄粨鏋�. */
    private static HashMap<String,Object> result;
	
	/** 鎵ц瀹屾垚鍚庣殑娑堟伅鍙ユ焺. */
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
     * 鍗曚緥鏋勯��.
     *
     * @return single instance of AbTaskQueue
     */
    public static AbTaskQueue getInstance() { 
        if (abTaskQueue == null) { 
            abTaskQueue = new AbTaskQueue();
        } 
        return abTaskQueue;
    } 
	
	/**
	 * 鏋勯�犳墽琛岀嚎绋嬮槦鍒�.
	 */
    public AbTaskQueue() {
    	mQuit = false;
    	mAbTaskItemList = new LinkedList<AbTaskItem>();
    	result = new HashMap<String,Object>();
    	//浠庣嚎绋嬫睜涓幏鍙�
    	Executor mExecutorService  = AbThreadFactory.getExecutorService();
    	mExecutorService.execute(this); 
    }
    
    /**
     * 寮�濮嬩竴涓墽琛屼换鍔�.
     *
     * @param item 鎵ц鍗曚綅
     */
    public void execute(AbTaskItem item) { 
         addTaskItem(item); 
    } 
    
    
    /**
     * 寮�濮嬩竴涓墽琛屼换鍔″苟娓呴櫎鍘熸潵闃熷垪.
     * @param item 鎵ц鍗曚綅
     * @param cancel 娓呯┖涔嬪墠鐨勪换鍔�
     */
    public void execute(AbTaskItem item,boolean cancel) { 
	    if(cancel){
	    	 cancel(true);
	    }
    	addTaskItem(item); 
    } 
     
    /**
     * 鎻忚堪锛氭坊鍔犲埌鎵ц绾跨▼闃熷垪.
     *
     * @param item 鎵ц鍗曚綅
     */
    private synchronized void addTaskItem(AbTaskItem item) { 
    	if (abTaskQueue == null) { 
    	    abTaskQueue = new AbTaskQueue();
        }
    	mAbTaskItemList.add(item);
    	//娣诲姞浜嗘墽琛岄」灏辨縺娲绘湰绾跨▼ 
        this.notify();
        
    } 
 
    /**
     * 鎻忚堪锛氱嚎绋嬭繍琛�.
     *
     * @see java.lang.Thread#run()
     */
    @Override 
    public void run() { 
        while(!mQuit) { 
        	try {
        	    while(mAbTaskItemList.size() > 0){
            
					AbTaskItem item  = mAbTaskItemList.remove(0);
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
				    
				    //鍋滄鍚庢竻绌�
				    if(mQuit){
				    	mAbTaskItemList.clear();
				    	return;
				    }
        	    }
        	    try {
					//娌℃湁鎵ц椤规椂绛夊緟 
					synchronized(this) { 
					    this.wait();
					}
				} catch (InterruptedException e) {
					AbLogUtil.e("AbTaskQueue","鏀跺埌绾跨▼涓柇璇锋眰");
					e.printStackTrace();
					//琚腑鏂殑鏄��鍑哄氨缁撴潫锛屽惁鍒欑户缁�
					if (mQuit) {
						mAbTaskItemList.clear();
	                    return;
	                }
	                continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
        } 
    } 
    
    /**
     * 鎻忚堪锛氱粓姝㈤槦鍒楅噴鏀剧嚎绋�.
     *
     * @param mayInterruptIfRunning the may interrupt if running
     */
    public void cancel(boolean mayInterruptIfRunning){
		mQuit  = true;
		if(mayInterruptIfRunning){
			interrupted();
		}
		abTaskQueue  = null;
    }

}

