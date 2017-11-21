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
package com.bnrc.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bToastUtil.java 
 * 鎻忚堪锛歍oast宸ュ叿绫�.
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2014-07-02 涓嬪崍11:52:13
 */

public class AbToastUtil {
    
    /** 涓婁笅鏂�. */
    private static Context mContext = null;
    
    /** 鏄剧ずToast. */
    public static final int SHOW_TOAST = 0;
    
    /**
	 * 涓昏Handler绫伙紝鍦ㄧ嚎绋嬩腑鍙敤
	 * what锛�0.鎻愮ず鏂囨湰淇℃伅
	 */
	private static Handler baseHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SHOW_TOAST:
					showToast(mContext,msg.getData().getString("TEXT"));
					break;
				default:
					break;
			}
		}
	};
    
    /**
     * 鎻忚堪锛歍oast鎻愮ず鏂囨湰.
     * @param text  鏂囨湰
     */
	public static void showToast(Context context,String text) {
		mContext = context;
		if(!AbStrUtil.isEmpty(text)){
			Toast.makeText(context,text, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
     * 鎻忚堪锛歍oast鎻愮ず鏂囨湰.
     * @param resId  鏂囨湰鐨勮祫婧怚D
     */
	public static void showToast(Context context,int resId) {
		mContext = context;
		Toast.makeText(context,""+context.getResources().getText(resId), Toast.LENGTH_SHORT).show();
	}
    
    /**
	 * 鎻忚堪锛氬湪绾跨▼涓彁绀烘枃鏈俊鎭�.
	 * @param resId 瑕佹彁绀虹殑瀛楃涓茶祫婧怚D锛屾秷鎭痺hat鍊间负0,
	 */
	public static void showToastInThread(Context context,int resId) {
		mContext = context;
		Message msg = baseHandler.obtainMessage(SHOW_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString("TEXT", context.getResources().getString(resId));
		msg.setData(bundle);
		baseHandler.sendMessage(msg);
	}
	
	/**
	 * 鎻忚堪锛氬湪绾跨▼涓彁绀烘枃鏈俊鎭�.
	 * @param toast 娑堟伅what鍊间负0
	 */
	public static void showToastInThread(Context context,String text) {
		mContext = context;
		Message msg = baseHandler.obtainMessage(SHOW_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString("TEXT", text);
		msg.setData(bundle);
		baseHandler.sendMessage(msg);
	}
    

}
