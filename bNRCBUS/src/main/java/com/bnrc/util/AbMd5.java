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

import java.security.MessageDigest;

// TODO: Auto-generated Javadoc

/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bMd5.java 
 * 鎻忚堪锛歁D5鍔犲瘑.
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-01-17 涓嬪崍11:52:13
 */
public class AbMd5 {
	
	/**
	 * 鎻忚堪锛歁D5鍔犲瘑.
	 * @param str 瑕佸姞瀵嗙殑瀛楃涓�
	 * @return String 鍔犲瘑鐨勫瓧绗︿覆
	 */
	public final static String MD5(String str) {
		char hexDigits[] = { // 鐢ㄦ潵灏嗗瓧鑺傝浆鎹㈡垚 16 杩涘埗琛ㄧず鐨勫瓧绗�
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			byte[] strTemp = str.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte tmp[] = mdTemp.digest(); // MD5 鐨勮绠楃粨鏋滄槸涓�涓� 128 浣嶇殑闀挎暣鏁帮紝
			// 鐢ㄥ瓧鑺傝〃绀哄氨鏄� 16 涓瓧鑺�
			char strs[] = new char[16 * 2]; // 姣忎釜瀛楄妭鐢� 16 杩涘埗琛ㄧず鐨勮瘽锛屼娇鐢ㄤ袱涓瓧绗︼紝
			// 鎵�浠ヨ〃绀烘垚 16 杩涘埗闇�瑕� 32 涓瓧绗�
			int k = 0; // 琛ㄧず杞崲缁撴灉涓搴旂殑瀛楃浣嶇疆
			for (int i = 0; i < 16; i++) { // 浠庣涓�涓瓧鑺傚紑濮嬶紝瀵� MD5 鐨勬瘡涓�涓瓧鑺�
				// 杞崲鎴� 16 杩涘埗瀛楃鐨勮浆鎹�
				byte byte0 = tmp[i]; // 鍙栫 i 涓瓧鑺�
				strs[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 鍙栧瓧鑺備腑楂� 4 浣嶇殑鏁板瓧杞崲,
				// >>> 涓洪�昏緫鍙崇Щ锛屽皢绗﹀彿浣嶄竴璧峰彸绉�
				strs[k++] = hexDigits[byte0 & 0xf]; // 鍙栧瓧鑺備腑浣� 4 浣嶇殑鏁板瓧杞崲
			}
			return new String(strs).toUpperCase(); // 鎹㈠悗鐨勭粨鏋滆浆鎹负瀛楃涓�
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		System.out.println(AbMd5.MD5("2011123456").toLowerCase());
	}

}
