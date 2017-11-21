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



// TODO: Auto-generated Javadoc
/**
 * 漏 2012 amsoft.cn 
 * 鍚嶇О锛欰bProcessInfo.java 
 * 鎻忚堪锛氳繘绋嬩俊鎭�
 * 
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-11-13 涓婂崍9:00:52
 */
public class AbProcessInfo{

	/**
	 * The user id of this process.
	 */
	public String uid;
	
	/** The name of the process that this object is associated with. */
	public String processName;

	/** The pid of this process; 0 if none. */
	public int pid;

	/**  鍗犵敤鐨勫唴瀛� B. */
	public long memory;
	
	/**  鍗犵敤鐨凜PU. */
	public String cpu;
	
	/**  杩涚▼鐨勭姸鎬侊紝鍏朵腑S琛ㄧず浼戠湢锛孯琛ㄧず姝ｅ湪杩愯锛孼琛ㄧず鍍垫鐘舵�侊紝N琛ㄧず璇ヨ繘绋嬩紭鍏堝�兼槸璐熸暟. */
	public String status;
	
	/**  褰撳墠浣跨敤鐨勭嚎绋嬫暟. */
	public String threadsCount;
	
	/**
	 * Instantiates a new ab process info.
	 */
	public AbProcessInfo() {
		super();
	}

	/**
	 * Instantiates a new ab process info.
	 *
	 * @param processName the process name
	 * @param pid the pid
	 */
	public AbProcessInfo(String processName, int pid) {
		super();
		this.processName = processName;
		this.pid = pid;
	}


}
