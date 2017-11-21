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

import java.util.List;

// TODO: Auto-generated Javadoc

/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bTaskListListener.java 
 * 鎻忚堪锛氭暟鎹墽琛岀殑鎺ュ彛.
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-9-2 涓嬪崍12:52:13
 */
public abstract class AbTaskListListener extends AbTaskListener{

	/**
	 * Gets the list.
	 *
	 * @return 杩斿洖鐨勭粨鏋滃垪琛�
	 */
	public abstract List<?> getList();

	/**
	 * 鎻忚堪锛氭墽琛屽畬鎴愬悗鍥炶皟.
	 * 涓嶇鎴愬姛涓庡惁閮戒細鎵ц
	 * @param paramList 杩斿洖鐨凩ist
	 */
    public abstract void update(List<?> paramList);
	
}
