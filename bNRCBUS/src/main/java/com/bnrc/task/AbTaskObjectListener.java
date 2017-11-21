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



// TODO: Auto-generated Javadoc

/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bTaskObjectListener.java 
 * 鎻忚堪锛氭暟鎹墽琛岀殑鎺ュ彛.
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2011-12-10 涓嬪崍11:52:13
 */
public abstract class AbTaskObjectListener extends AbTaskListener{
	
	/**
	 * Gets the object.
	 *
	 * @param <T> the generic type
	 * @return 杩斿洖鐨勭粨鏋滃璞�
	 */
    public abstract <T> T getObject();
    
    /**
     * 鎻忚堪锛氭墽琛屽紑濮嬪悗璋冪敤.
     *
     * @param <T> the generic type
     * @param obj the obj
     */
    public abstract <T> void update(T obj); 
    
	
}
