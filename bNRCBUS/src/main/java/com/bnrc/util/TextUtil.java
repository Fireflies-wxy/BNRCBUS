package com.bnrc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class TextUtil {

	/**
	 * 鍘婚櫎鐗规畩绗﹀彿鎴栧皢涓枃绗﹀彿杞寲涓鸿嫳鏂囩鍙�
	 * @param str
	 * @return String
	 */
	public static String filterString(String str) {
		if (TextUtils.isEmpty(str)) {
			return "";
		}
		//鏇挎崲涓枃绗﹀彿
		str = str.replaceAll("锛�", ":").replaceAll("锛�", ",").replaceAll("锛�", "?").
				replaceAll("锛�", "!").replaceAll("銆�", "[").replaceAll("銆�", "]");
		//娓呴櫎鐗规畩绗﹀彿
		String regEx = "[銆庛�廬";
		Pattern p = Pattern.compile(regEx);
		Matcher matcher = p.matcher(str);
		return matcher.replaceAll("").trim();
	}
	
	/**
	 * 浣挎枃鏈唴瀹瑰崟琛屾樉绀�
	 * @param text
	 * @return String
	 */
	public static String getSingleLines(String text) {
		text = filterString(text);
		if (!TextUtils.isEmpty(text)) {
			text = text.length() > 12 ? text.substring(0, 12) + "..." : text;
		}
		return text;
	}
	
	/**
	 * 浣挎枃鏈唴瀹硅嚜鍔ㄦ崲琛岋紝姣忚瀛楃鏁伴檺鍒跺湪lineNum涓�
	 * @param text 鏂囨湰鍐呭
	 * @param lineNum 涓�琛屽寘鍚殑瀛楃鏁�
	 * @param maxLine 闄愬埗鐨勮鏁�
	 * @return String
	 */
	public static String getSeveralLines(String text, int lineNum, int maxLine) {
		text = filterString(text);
		String tempStr = "";
		StringBuilder sb = new StringBuilder();
		if (!TextUtils.isEmpty(text)) {
			int length = text.length();
			int lines = length % lineNum;
			if (lines == 0) {
				lines = length / lineNum;
			} else {
				lines = length / lineNum + 1;
			}
			for (int i = 0; i < lines; i++) {
				sb.append(text.substring(i * lineNum, ((i+1) * lineNum) > length ? length : ((i+1) * lineNum))).append("\n");
				tempStr = sb.toString();
				if (i == (maxLine-1)) {
					tempStr = tempStr.substring(0, tempStr.length() - 3) + "...";
					//涓嬪垪浠ｇ爜浜﹀彲
//					sb.delete(0, sb.length());	//闃叉鏁版嵁閲嶅
//					tempStr = sb.append(tempStr.substring(0, tempStr.length() - 3)).append("...").toString();
					break;
				}
			}
			sb.delete(0, sb.length());
			sb = null;
		}
		return tempStr;
	}
	
}
