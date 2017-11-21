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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// TODO: Auto-generated Javadoc

/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bStrUtil.java 
 * 鎻忚堪锛氬瓧绗︿覆澶勭悊绫�.
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-01-17 涓嬪崍11:52:13
 */
public class AbStrUtil {
    
    /**
     * 鎻忚堪锛氬皢null杞寲涓衡�溾��.
     *
     * @param str 鎸囧畾鐨勫瓧绗︿覆
     * @return 瀛楃涓茬殑String绫诲瀷
     */
    public static String parseEmpty(String str) {
        if(str==null || "null".equals(str.trim())){
        	str = "";
        }
        return str.trim();
    }
    
    /**
     * 鎻忚堪锛氬垽鏂竴涓瓧绗︿覆鏄惁涓簄ull鎴栫┖鍊�.
     *
     * @param str 鎸囧畾鐨勫瓧绗︿覆
     * @return true or false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
    
    /**
     * 鑾峰彇瀛楃涓蹭腑鏂囧瓧绗︾殑闀垮害锛堟瘡涓腑鏂囩畻2涓瓧绗︼級.
     *
     * @param str 鎸囧畾鐨勫瓧绗︿覆
     * @return 涓枃瀛楃鐨勯暱搴�
     */
    public static int chineseLength(String str) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 鑾峰彇瀛楁鍊肩殑闀垮害锛屽鏋滃惈涓枃瀛楃锛屽垯姣忎釜涓枃瀛楃闀垮害涓�2锛屽惁鍒欎负1 */
        if(!isEmpty(str)){
	        for (int i = 0; i < str.length(); i++) {
	            /* 鑾峰彇涓�涓瓧绗� */
	            String temp = str.substring(i, i + 1);
	            /* 鍒ゆ柇鏄惁涓轰腑鏂囧瓧绗� */
	            if (temp.matches(chinese)) {
	                valueLength += 2;
	            }
	        }
        }
        return valueLength;
    }
    
    /**
     * 鎻忚堪锛氳幏鍙栧瓧绗︿覆鐨勯暱搴�.
     *
     * @param str 鎸囧畾鐨勫瓧绗︿覆
     * @return  瀛楃涓茬殑闀垮害锛堜腑鏂囧瓧绗﹁2涓級
     */
     public static int strLength(String str) {
         int valueLength = 0;
         String chinese = "[\u0391-\uFFE5]";
         if(!isEmpty(str)){
	         //鑾峰彇瀛楁鍊肩殑闀垮害锛屽鏋滃惈涓枃瀛楃锛屽垯姣忎釜涓枃瀛楃闀垮害涓�2锛屽惁鍒欎负1
	         for (int i = 0; i < str.length(); i++) {
	             //鑾峰彇涓�涓瓧绗�
	             String temp = str.substring(i, i + 1);
	             //鍒ゆ柇鏄惁涓轰腑鏂囧瓧绗�
	             if (temp.matches(chinese)) {
	                 //涓枃瀛楃闀垮害涓�2
	                 valueLength += 2;
	             } else {
	                 //鍏朵粬瀛楃闀垮害涓�1
	                 valueLength += 1;
	             }
	         }
         }
         return valueLength;
     }
     
     /**
      * 鎻忚堪锛氳幏鍙栨寚瀹氶暱搴︾殑瀛楃鎵�鍦ㄤ綅缃�.
      *
      * @param str 鎸囧畾鐨勫瓧绗︿覆
      * @param maxL 瑕佸彇鍒扮殑闀垮害锛堝瓧绗﹂暱搴︼紝涓枃瀛楃璁�2涓級
      * @return 瀛楃鐨勬墍鍦ㄤ綅缃�
      */
     public static int subStringLength(String str,int maxL) {
    	 int currentIndex = 0;
         int valueLength = 0;
         String chinese = "[\u0391-\uFFE5]";
         //鑾峰彇瀛楁鍊肩殑闀垮害锛屽鏋滃惈涓枃瀛楃锛屽垯姣忎釜涓枃瀛楃闀垮害涓�2锛屽惁鍒欎负1
         for (int i = 0; i < str.length(); i++) {
             //鑾峰彇涓�涓瓧绗�
             String temp = str.substring(i, i + 1);
             //鍒ゆ柇鏄惁涓轰腑鏂囧瓧绗�
             if (temp.matches(chinese)) {
                 //涓枃瀛楃闀垮害涓�2
                 valueLength += 2;
             } else {
                 //鍏朵粬瀛楃闀垮害涓�1
                 valueLength += 1;
             }
             if(valueLength >= maxL){
            	 currentIndex = i;
            	 break;
             }
         }
         return currentIndex;
     }
     
    /**
     * 鎻忚堪锛氭墜鏈哄彿鏍煎紡楠岃瘉.
     *
     * @param str 鎸囧畾鐨勬墜鏈哄彿鐮佸瓧绗︿覆
     * @return 鏄惁涓烘墜鏈哄彿鐮佹牸寮�:鏄负true锛屽惁鍒檉alse
     */
 	public static Boolean isMobileNo(String str) {
 		Boolean isMobileNo = false;
 		try {
			Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
			Matcher m = p.matcher(str);
			isMobileNo = m.matches();
		} catch (Exception e) {
			e.printStackTrace();
		}
 		return isMobileNo;
 	}
 	
 	/**
	  * 鎻忚堪锛氭槸鍚﹀彧鏄瓧姣嶅拰鏁板瓧.
	  *
	  * @param str 鎸囧畾鐨勫瓧绗︿覆
	  * @return 鏄惁鍙槸瀛楁瘝鍜屾暟瀛�:鏄负true锛屽惁鍒檉alse
	  */
 	public static Boolean isNumberLetter(String str) {
 		Boolean isNoLetter = false;
 		String expr = "^[A-Za-z0-9]+$";
 		if (str.matches(expr)) {
 			isNoLetter = true;
 		}
 		return isNoLetter;
 	}
 	
 	/**
	  * 鎻忚堪锛氭槸鍚﹀彧鏄暟瀛�.
	  *
	  * @param str 鎸囧畾鐨勫瓧绗︿覆
	  * @return 鏄惁鍙槸鏁板瓧:鏄负true锛屽惁鍒檉alse
	  */
 	public static Boolean isNumber(String str) {
 		Boolean isNumber = false;
 		String expr = "^[0-9]+$";
 		if (str.matches(expr)) {
 			isNumber = true;
 		}
 		return isNumber;
 	}
 	
 	/**
	  * 鎻忚堪锛氭槸鍚︽槸閭.
	  *
	  * @param str 鎸囧畾鐨勫瓧绗︿覆
	  * @return 鏄惁鏄偖绠�:鏄负true锛屽惁鍒檉alse
	  */
 	public static Boolean isEmail(String str) {
 		Boolean isEmail = false;
 		String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
 		if (str.matches(expr)) {
 			isEmail = true;
 		}
 		return isEmail;
 	}
 	
 	/**
	  * 鎻忚堪锛氭槸鍚︽槸涓枃.
	  *
	  * @param str 鎸囧畾鐨勫瓧绗︿覆
	  * @return  鏄惁鏄腑鏂�:鏄负true锛屽惁鍒檉alse
	  */
    public static Boolean isChinese(String str) {
    	Boolean isChinese = true;
        String chinese = "[\u0391-\uFFE5]";
        if(!isEmpty(str)){
	         //鑾峰彇瀛楁鍊肩殑闀垮害锛屽鏋滃惈涓枃瀛楃锛屽垯姣忎釜涓枃瀛楃闀垮害涓�2锛屽惁鍒欎负1
	         for (int i = 0; i < str.length(); i++) {
	             //鑾峰彇涓�涓瓧绗�
	             String temp = str.substring(i, i + 1);
	             //鍒ゆ柇鏄惁涓轰腑鏂囧瓧绗�
	             if (temp.matches(chinese)) {
	             }else{
	            	 isChinese = false;
	             }
	         }
        }
        return isChinese;
    }
    
    /**
     * 鎻忚堪锛氭槸鍚﹀寘鍚腑鏂�.
     *
     * @param str 鎸囧畾鐨勫瓧绗︿覆
     * @return  鏄惁鍖呭惈涓枃:鏄负true锛屽惁鍒檉alse
     */
    public static Boolean isContainChinese(String str) {
    	Boolean isChinese = false;
        String chinese = "[\u0391-\uFFE5]";
        if(!isEmpty(str)){
	         //鑾峰彇瀛楁鍊肩殑闀垮害锛屽鏋滃惈涓枃瀛楃锛屽垯姣忎釜涓枃瀛楃闀垮害涓�2锛屽惁鍒欎负1
	         for (int i = 0; i < str.length(); i++) {
	             //鑾峰彇涓�涓瓧绗�
	             String temp = str.substring(i, i + 1);
	             //鍒ゆ柇鏄惁涓轰腑鏂囧瓧绗�
	             if (temp.matches(chinese)) {
	            	 isChinese = true;
	             }else{
	            	 
	             }
	         }
        }
        return isChinese;
    }
 	
 	/**
	  * 鎻忚堪锛氫粠杈撳叆娴佷腑鑾峰緱String.
	  *
	  * @param is 杈撳叆娴�
	  * @return 鑾峰緱鐨凷tring
	  */
 	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			//鏈�鍚庝竴涓猏n鍒犻櫎
			if(sb.indexOf("\n")!=-1 && sb.lastIndexOf("\n") == sb.length()-1){
				sb.delete(sb.lastIndexOf("\n"), sb.lastIndexOf("\n")+1);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
 	
 	/**
	  * 鎻忚堪锛氭爣鍑嗗寲鏃ユ湡鏃堕棿绫诲瀷鐨勬暟鎹紝涓嶈冻涓や綅鐨勮ˉ0.
	  *
	  * @param dateTime 棰勬牸寮忕殑鏃堕棿瀛楃涓诧紝濡�:2012-3-2 12:2:20
	  * @return String 鏍煎紡鍖栧ソ鐨勬椂闂村瓧绗︿覆锛屽:2012-03-20 12:02:20
	  */
 	public static String dateTimeFormat(String dateTime) {
		StringBuilder sb = new StringBuilder();
		try {
			if(isEmpty(dateTime)){
				return null;
			}
			String[] dateAndTime = dateTime.split(" ");
			if(dateAndTime.length>0){
				  for(String str : dateAndTime){
					if(str.indexOf("-")!=-1){
						String[] date =  str.split("-");
						for(int i=0;i<date.length;i++){
						  String str1 = date[i];
						  sb.append(strFormat2(str1));
						  if(i< date.length-1){
							  sb.append("-");
						  }
						}
					}else if(str.indexOf(":")!=-1){
						sb.append(" ");
						String[] date =  str.split(":");
						for(int i=0;i<date.length;i++){
						  String str1 = date[i];
						  sb.append(strFormat2(str1));
						  if(i< date.length-1){
							  sb.append(":");
						  }
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		return sb.toString();
	}
 	
 	/**
	  * 鎻忚堪锛氫笉瓒�2涓瓧绗︾殑鍦ㄥ墠闈㈣ˉ鈥�0鈥�.
	  *
	  * @param str 鎸囧畾鐨勫瓧绗︿覆
	  * @return 鑷冲皯2涓瓧绗︾殑瀛楃涓�
	  */
    public static String strFormat2(String str) {
		try {
			if(str.length()<=1){
				str = "0"+str;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return str;
	}
    
    /**
     * 鎻忚堪锛氭埅鍙栧瓧绗︿覆鍒版寚瀹氬瓧鑺傞暱搴�.
     *
     * @param str the str
     * @param length 鎸囧畾瀛楄妭闀垮害
     * @return 鎴彇鍚庣殑瀛楃涓�
     */
    public static String cutString(String str,int length){
		return cutString(str, length,"");
	}
    
    /**
     * 鎻忚堪锛氭埅鍙栧瓧绗︿覆鍒版寚瀹氬瓧鑺傞暱搴�.
     *
     * @param str 鏂囨湰
     * @param length 瀛楄妭闀垮害
     * @param dot 鐪佺暐绗﹀彿
     * @return 鎴彇鍚庣殑瀛楃涓�
     */
	public static String cutString(String str,int length,String dot){
		int strBLen = strlen(str,"GBK");
		if( strBLen <= length ){
     		return str;
     	}
		int temp = 0;
		StringBuffer sb = new StringBuffer(length);
		char[] ch = str.toCharArray();
		for ( char c : ch ) {
			sb.append( c );
			if ( c > 256 ) {
	    		temp += 2;
	    	} else {
	    		temp += 1;
	    	}
			if (temp >= length) {
				if( dot != null) {
					sb.append( dot );
				}
	            break;
	        }
		}
		return sb.toString();
    }
	
	/**
	 * 鎻忚堪锛氭埅鍙栧瓧绗︿覆浠庣涓�涓寚瀹氬瓧绗�.
	 *
	 * @param str1 鍘熸枃鏈�
	 * @param str2 鎸囧畾瀛楃
	 * @param offset 鍋忕Щ鐨勭储寮�
	 * @return 鎴彇鍚庣殑瀛楃涓�
	 */
	public static String cutStringFromChar(String str1,String str2,int offset){
		if(isEmpty(str1)){
			return "";
		}
		int start = str1.indexOf(str2);
		if(start!=-1){
			if(str1.length()>start+offset){
				return str1.substring(start+offset);
			}
		}
		return "";
    }
	
	/**
	 * 鎻忚堪锛氳幏鍙栧瓧鑺傞暱搴�.
	 *
	 * @param str 鏂囨湰
	 * @param charset 瀛楃闆嗭紙GBK锛�
	 * @return the int
	 */
	public static int strlen(String str,String charset){
		if(str==null||str.length()==0){
			return 0;
		}
		int length=0;
		try {
			length = str.getBytes(charset).length;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return length;
	}
    
	/**
	 * 鑾峰彇澶у皬鐨勬弿杩�.
	 *
	 * @param size 瀛楄妭涓暟
	 * @return  澶у皬鐨勬弿杩�
	 */
    public static String getSizeDesc(long size) {
	   	 String suffix = "B";
	   	 if (size >= 1024){
			suffix = "K";
			size = size>>10;
			if (size >= 1024){
				suffix = "M";
				//size /= 1024;
				size = size>>10;
				if (size >= 1024){
	    		        suffix = "G";
	    		        size = size>>10;
	    		        //size /= 1024;
		        }
			}
	   	}
        return size+suffix;
    }
    
    /**
     * 鎻忚堪锛歩p鍦板潃杞崲涓�10杩涘埗鏁�.
     *
     * @param ip the ip
     * @return the long
     */
    public static long ip2int(String ip){ 
    	ip = ip.replace(".", ",");
    	String[]items = ip.split(","); 
    	return Long.valueOf(items[0])<<24 |Long.valueOf(items[1])<<16 |Long.valueOf(items[2])<<8 |Long.valueOf(items[3]); 
    } 
	
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
		System.out.println(dateTimeFormat("2012-3-2 12:2:20"));
	}

}
