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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Process;

import com.bnrc.util.AbAppUtil;
// TODO: Auto-generated Javadoc

/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bThreadFactory.java 
 * 鎻忚堪锛氱嚎绋嬪伐鍘�.
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2011-11-10 涓嬪崍11:52:13
 */
public class AbThreadFactory {
	
	/** 浠诲姟鎵ц鍣�. */
	public static Executor mExecutorService = null;
	
	/** 淇濆瓨绾跨▼鏁伴噺 . */
	private static final int CORE_POOL_SIZE = 5;
	
	/** 鏈�澶х嚎绋嬫暟閲� . */
    private static final int MAXIMUM_POOL_SIZE = 64;
    
    /** 娲诲姩绾跨▼鏁伴噺 . */
    private static final int KEEP_ALIVE = 5;

    /** 绾跨▼宸ュ巶 . */
    private static final ThreadFactory mThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AbThread #" + mCount.getAndIncrement());
        }
    };

    /** 闃熷垪. */
    private static final BlockingQueue<Runnable> mPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(10);
    
    /**
     * 鑾峰彇鎵ц鍣�.
     *
     * @return the executor service
     */
    public static Executor getExecutorService() { 
        if (mExecutorService == null) { 
        	int numCores = AbAppUtil.getNumCores();
        	mExecutorService
	         = new ThreadPoolExecutor(numCores * CORE_POOL_SIZE,numCores * MAXIMUM_POOL_SIZE,numCores * KEEP_ALIVE,
                    TimeUnit.SECONDS, mPoolWorkQueue, mThreadFactory);
        }
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        return mExecutorService;
    } 
	
	
}
