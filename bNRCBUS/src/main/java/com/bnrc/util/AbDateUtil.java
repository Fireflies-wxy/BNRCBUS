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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

// TODO: Auto-generated Javadoc

/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bDateUtil.java 
 * 鎻忚堪锛氭棩鏈熷鐞嗙被.
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-01-18 涓嬪崍11:52:13
 */
public class AbDateUtil {
	
	/** 鏃堕棿鏃ユ湡鏍煎紡鍖栧埌骞存湀鏃ユ椂鍒嗙. */
	public static final String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";
	
	/** 鏃堕棿鏃ユ湡鏍煎紡鍖栧埌骞存湀鏃�. */
	public static final String dateFormatYMD = "yyyy-MM-dd";
	
	/** 鏃堕棿鏃ユ湡鏍煎紡鍖栧埌骞存湀. */
	public static final String dateFormatYM = "yyyy-MM";
	
	/** 鏃堕棿鏃ユ湡鏍煎紡鍖栧埌骞存湀鏃ユ椂鍒�. */
	public static final String dateFormatYMDHM = "yyyy-MM-dd HH:mm";
	
	/** 鏃堕棿鏃ユ湡鏍煎紡鍖栧埌鏈堟棩. */
	public static final String dateFormatMD = "MM/dd";
	
	/** 鏃跺垎绉�. */
	public static final String dateFormatHMS = "HH:mm:ss";
	
	/** 鏃跺垎. */
	public static final String dateFormatHM = "HH:mm";
	
	/** 涓婂崍. */
    public static final String AM = "AM";

    /** 涓嬪崍. */
    public static final String PM = "PM";


    /**
	 * 鎻忚堪锛歋tring绫诲瀷鐨勬棩鏈熸椂闂磋浆鍖栦负Date绫诲瀷.
	 *
	 * @param strDate String褰㈠紡鐨勬棩鏈熸椂闂�
	 * @param format 鏍煎紡鍖栧瓧绗︿覆锛屽锛�"yyyy-MM-dd HH:mm:ss"
	 * @return Date Date绫诲瀷鏃ユ湡鏃堕棿
	 */
	public static Date getDateByFormat(String strDate, String format) {
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = mSimpleDateFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 鎻忚堪锛氳幏鍙栧亸绉讳箣鍚庣殑Date.
	 * @param date 鏃ユ湡鏃堕棿
	 * @param calendarField Calendar灞炴�э紝瀵瑰簲offset鐨勫�硷紝 濡�(Calendar.DATE,琛ㄧず+offset澶�,Calendar.HOUR_OF_DAY,琛ㄧず锛媜ffset灏忔椂)
	 * @param offset 鍋忕Щ(鍊煎ぇ浜�0,琛ㄧず+,鍊煎皬浜�0,琛ㄧず锛�)
	 * @return Date 鍋忕Щ涔嬪悗鐨勬棩鏈熸椂闂�
	 */
	public Date getDateByOffset(Date date,int calendarField,int offset) {
		Calendar c = new GregorianCalendar();
		try {
			c.setTime(date);
			c.add(calendarField, offset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c.getTime();
	}
	
	/**
	 * 鎻忚堪锛氳幏鍙栨寚瀹氭棩鏈熸椂闂寸殑瀛楃涓�(鍙亸绉�).
	 *
	 * @param strDate String褰㈠紡鐨勬棩鏈熸椂闂�
	 * @param format 鏍煎紡鍖栧瓧绗︿覆锛屽锛�"yyyy-MM-dd HH:mm:ss"
	 * @param calendarField Calendar灞炴�э紝瀵瑰簲offset鐨勫�硷紝 濡�(Calendar.DATE,琛ㄧず+offset澶�,Calendar.HOUR_OF_DAY,琛ㄧず锛媜ffset灏忔椂)
	 * @param offset 鍋忕Щ(鍊煎ぇ浜�0,琛ㄧず+,鍊煎皬浜�0,琛ㄧず锛�)
	 * @return String String绫诲瀷鐨勬棩鏈熸椂闂�
	 */
	public static String getStringByOffset(String strDate, String format,int calendarField,int offset) {
		String mDateTime = null;
		try {
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			c.setTime(mSimpleDateFormat.parse(strDate));
			c.add(calendarField, offset);
			mDateTime = mSimpleDateFormat.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return mDateTime;
	}
	
	/**
	 * 鎻忚堪锛欴ate绫诲瀷杞寲涓篠tring绫诲瀷(鍙亸绉�).
	 *
	 * @param date the date
	 * @param format the format
	 * @param calendarField the calendar field
	 * @param offset the offset
	 * @return String String绫诲瀷鏃ユ湡鏃堕棿
	 */
	public static String getStringByOffset(Date date, String format,int calendarField,int offset) {
		String strDate = null;
		try {
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			c.setTime(date);
			c.add(calendarField, offset);
			strDate = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}
	

	/**
	 * 鎻忚堪锛欴ate绫诲瀷杞寲涓篠tring绫诲瀷.
	 *
	 * @param date the date
	 * @param format the format
	 * @return String String绫诲瀷鏃ユ湡鏃堕棿
	 */
	public static String getStringByFormat(Date date, String format) {
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
		String strDate = null;
		try {
			strDate = mSimpleDateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}
	
	/**
	 * 鎻忚堪锛氳幏鍙栨寚瀹氭棩鏈熸椂闂寸殑瀛楃涓�,鐢ㄤ簬瀵煎嚭鎯宠鐨勬牸寮�.
	 *
	 * @param strDate String褰㈠紡鐨勬棩鏈熸椂闂达紝蹇呴』涓簓yyy-MM-dd HH:mm:ss鏍煎紡
	 * @param format 杈撳嚭鏍煎紡鍖栧瓧绗︿覆锛屽锛�"yyyy-MM-dd HH:mm:ss"
	 * @return String 杞崲鍚庣殑String绫诲瀷鐨勬棩鏈熸椂闂�
	 */
	public static String getStringByFormat(String strDate, String format) {
		String mDateTime = null;
		try {
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMDHMS);
			c.setTime(mSimpleDateFormat.parse(strDate));
			SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
			mDateTime = mSimpleDateFormat2.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mDateTime;
	}
	
	/**
	 * 鎻忚堪锛氳幏鍙杕illiseconds琛ㄧず鐨勬棩鏈熸椂闂寸殑瀛楃涓�.
	 *
	 * @param milliseconds the milliseconds
	 * @param format  鏍煎紡鍖栧瓧绗︿覆锛屽锛�"yyyy-MM-dd HH:mm:ss"
	 * @return String 鏃ユ湡鏃堕棿瀛楃涓�
	 */
	public static String getStringByFormat(long milliseconds,String format) {
		String thisDateTime = null;
		try {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			thisDateTime = mSimpleDateFormat.format(milliseconds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return thisDateTime;
	}
	
	/**
	 * 鎻忚堪锛氳幏鍙栬〃绀哄綋鍓嶆棩鏈熸椂闂寸殑瀛楃涓�.
	 *
	 * @param format  鏍煎紡鍖栧瓧绗︿覆锛屽锛�"yyyy-MM-dd HH:mm:ss"
	 * @return String String绫诲瀷鐨勫綋鍓嶆棩鏈熸椂闂�
	 */
	public static String getCurrentDate(String format) {
		AbLogUtil.d(AbDateUtil.class, "getCurrentDate:"+format);
		String curDateTime = null;
		try {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			Calendar c = new GregorianCalendar();
			curDateTime = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curDateTime;

	}

	/**
	 * 鎻忚堪锛氳幏鍙栬〃绀哄綋鍓嶆棩鏈熸椂闂寸殑瀛楃涓�(鍙亸绉�).
	 *
	 * @param format 鏍煎紡鍖栧瓧绗︿覆锛屽锛�"yyyy-MM-dd HH:mm:ss"
	 * @param calendarField Calendar灞炴�э紝瀵瑰簲offset鐨勫�硷紝 濡�(Calendar.DATE,琛ㄧず+offset澶�,Calendar.HOUR_OF_DAY,琛ㄧず锛媜ffset灏忔椂)
	 * @param offset 鍋忕Щ(鍊煎ぇ浜�0,琛ㄧず+,鍊煎皬浜�0,琛ㄧず锛�)
	 * @return String String绫诲瀷鐨勬棩鏈熸椂闂�
	 */
	public static String getCurrentDateByOffset(String format,int calendarField,int offset) {
		String mDateTime = null;
		try {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			Calendar c = new GregorianCalendar();
			c.add(calendarField, offset);
			mDateTime = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mDateTime;

	}

	/**
	 * 鎻忚堪锛氳绠椾袱涓棩鏈熸墍宸殑澶╂暟.
	 *
	 * @param milliseconds1 the milliseconds1
	 * @param milliseconds2 the milliseconds2
	 * @return int 鎵�宸殑澶╂暟
	 */
	public static int getOffectDay(long milliseconds1, long milliseconds2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(milliseconds1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(milliseconds2);
		//鍏堝垽鏂槸鍚﹀悓骞�
		int y1 = calendar1.get(Calendar.YEAR);
		int y2 = calendar2.get(Calendar.YEAR);
		int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
		int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
		int maxDays = 0;
		int day = 0;
		if (y1 - y2 > 0) {
			maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
			day = d1 - d2 + maxDays;
		} else if (y1 - y2 < 0) {
			maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
			day = d1 - d2 - maxDays;
		} else {
			day = d1 - d2;
		}
		return day;
	}
	
	/**
	 * 鎻忚堪锛氳绠椾袱涓棩鏈熸墍宸殑灏忔椂鏁�.
	 *
	 * @param date1 绗竴涓椂闂寸殑姣琛ㄧず
	 * @param date2 绗簩涓椂闂寸殑姣琛ㄧず
	 * @return int 鎵�宸殑灏忔椂鏁�
	 */
	public static int getOffectHour(long date1, long date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
		int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
		int h = 0;
		int day = getOffectDay(date1, date2);
		h = h1-h2+day*24;
		return h;
	}
	
	/**
	 * 鎻忚堪锛氳绠椾袱涓棩鏈熸墍宸殑鍒嗛挓鏁�.
	 *
	 * @param date1 绗竴涓椂闂寸殑姣琛ㄧず
	 * @param date2 绗簩涓椂闂寸殑姣琛ㄧず
	 * @return int 鎵�宸殑鍒嗛挓鏁�
	 */
	public static int getOffectMinutes(long date1, long date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		int m1 = calendar1.get(Calendar.MINUTE);
		int m2 = calendar2.get(Calendar.MINUTE);
		int h = getOffectHour(date1, date2);
		int m = 0;
		m = m1-m2+h*60;
		return m;
	}

	/**
	 * 鎻忚堪锛氳幏鍙栨湰鍛ㄤ竴.
	 *
	 * @param format the format
	 * @return String String绫诲瀷鏃ユ湡鏃堕棿
	 */
	public static String getFirstDayOfWeek(String format) {
		return getDayOfWeek(format,Calendar.MONDAY);
	}

	/**
	 * 鎻忚堪锛氳幏鍙栨湰鍛ㄦ棩.
	 *
	 * @param format the format
	 * @return String String绫诲瀷鏃ユ湡鏃堕棿
	 */
	public static String getLastDayOfWeek(String format) {
		return getDayOfWeek(format,Calendar.SUNDAY);
	}

	/**
	 * 鎻忚堪锛氳幏鍙栨湰鍛ㄧ殑鏌愪竴澶�.
	 *
	 * @param format the format
	 * @param calendarField the calendar field
	 * @return String String绫诲瀷鏃ユ湡鏃堕棿
	 */
	private static String getDayOfWeek(String format,int calendarField) {
		String strDate = null;
		try {
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			int week = c.get(Calendar.DAY_OF_WEEK);
			if (week == calendarField){
				strDate = mSimpleDateFormat.format(c.getTime());
			}else{
				int offectDay = calendarField - week;
				if (calendarField == Calendar.SUNDAY) {
					offectDay = 7-Math.abs(offectDay);
				} 
				c.add(Calendar.DATE, offectDay);
				strDate = mSimpleDateFormat.format(c.getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}
	
	/**
	 * 鎻忚堪锛氳幏鍙栨湰鏈堢涓�澶�.
	 *
	 * @param format the format
	 * @return String String绫诲瀷鏃ユ湡鏃堕棿
	 */
	public static String getFirstDayOfMonth(String format) {
		String strDate = null;
		try {
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			//褰撳墠鏈堢殑绗竴澶�
			c.set(GregorianCalendar.DAY_OF_MONTH, 1);
			strDate = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;

	}

	/**
	 * 鎻忚堪锛氳幏鍙栨湰鏈堟渶鍚庝竴澶�.
	 *
	 * @param format the format
	 * @return String String绫诲瀷鏃ユ湡鏃堕棿
	 */
	public static String getLastDayOfMonth(String format) {
		String strDate = null;
		try {
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			// 褰撳墠鏈堢殑鏈�鍚庝竴澶�
			c.set(Calendar.DATE, 1);
			c.roll(Calendar.DATE, -1);
			strDate = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	

	/**
	 * 鎻忚堪锛氳幏鍙栬〃绀哄綋鍓嶆棩鏈熺殑0鐐规椂闂存绉掓暟.
	 *
	 * @return the first time of day
	 */
	public static long getFirstTimeOfDay() {
			Date date = null;
			try {
				String currentDate = getCurrentDate(dateFormatYMD);
				date = getDateByFormat(currentDate+" 00:00:00",dateFormatYMDHMS);
				return date.getTime();
			} catch (Exception e) {
			}
			return -1;
	}
	
	/**
	 * 鎻忚堪锛氳幏鍙栬〃绀哄綋鍓嶆棩鏈�24鐐规椂闂存绉掓暟.
	 *
	 * @return the last time of day
	 */
	public static long getLastTimeOfDay() {
			Date date = null;
			try {
				String currentDate = getCurrentDate(dateFormatYMD);
				date = getDateByFormat(currentDate+" 24:00:00",dateFormatYMDHMS);
				return date.getTime();
			} catch (Exception e) {
			}
			return -1;
	}
	
	/**
	 * 鎻忚堪锛氬垽鏂槸鍚︽槸闂板勾()
	 * <p>(year鑳借4鏁撮櫎 骞朵笖 涓嶈兘琚�100鏁撮櫎) 鎴栬�� year鑳借400鏁撮櫎,鍒欒骞翠负闂板勾.
	 *
	 * @param year 骞翠唬锛堝2012锛�
	 * @return boolean 鏄惁涓洪棸骞�
	 */
	public static boolean isLeapYear(int year) {
		if ((year % 4 == 0 && year % 400 != 0) || year % 400 == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 鎻忚堪锛氭牴鎹椂闂磋繑鍥炴牸寮忓寲鍚庣殑鏃堕棿鐨勬弿杩�.
	 * 灏忎簬1灏忔椂鏄剧ず澶氬皯鍒嗛挓鍓�  澶т簬1灏忔椂鏄剧ず浠婂ぉ锛嬪疄闄呮棩鏈燂紝澶т簬浠婂ぉ鍏ㄩ儴鏄剧ず瀹為檯鏃堕棿
	 *
	 * @param strDate the str date
	 * @param outFormat the out format
	 * @return the string
	 */
	public static String formatDateStr2Desc(String strDate,String outFormat) {
		
		DateFormat df = new SimpleDateFormat(dateFormatYMDHMS);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c2.setTime(df.parse(strDate));
			c1.setTime(new Date());
			int d = getOffectDay(c1.getTimeInMillis(), c2.getTimeInMillis());
			if(d==0){
				int h = getOffectHour(c1.getTimeInMillis(), c2.getTimeInMillis());
				if(h>0){
					return "浠婂ぉ"+getStringByFormat(strDate,dateFormatHM);
					//return h + "灏忔椂鍓�";
				}else if(h<0){
					//return Math.abs(h) + "灏忔椂鍚�";
				}else if(h==0){
					int m = getOffectMinutes(c1.getTimeInMillis(), c2.getTimeInMillis());
					if(m>0){
						return m + "鍒嗛挓鍓�";
					}else if(m<0){
						//return Math.abs(m) + "鍒嗛挓鍚�";
					}else{
						return "鍒氬垰";
					}
				}
				
			}else if(d>0){
				if(d == 1){
					//return "鏄ㄥぉ"+getStringByFormat(strDate,outFormat);
				}else if(d==2){
					//return "鍓嶅ぉ"+getStringByFormat(strDate,outFormat);
				}
			}else if(d<0){
				if(d == -1){
					//return "鏄庡ぉ"+getStringByFormat(strDate,outFormat);
				}else if(d== -2){
					//return "鍚庡ぉ"+getStringByFormat(strDate,outFormat);
				}else{
				    //return Math.abs(d) + "澶╁悗"+getStringByFormat(strDate,outFormat);
				}
			}
			
			String out = getStringByFormat(strDate,outFormat);
			if(!AbStrUtil.isEmpty(out)){
				return out;
			}
		} catch (Exception e) {
		}
		
		return strDate;
	}
	
	
	/**
	 * 鍙栨寚瀹氭棩鏈熶负鏄熸湡鍑�.
	 *
	 * @param strDate 鎸囧畾鏃ユ湡
	 * @param inFormat 鎸囧畾鏃ユ湡鏍煎紡
	 * @return String   鏄熸湡鍑�
	 */
    public static String getWeekNumber(String strDate,String inFormat) {
      String week = "鏄熸湡鏃�";
      Calendar calendar = new GregorianCalendar();
      DateFormat df = new SimpleDateFormat(inFormat);
      try {
		   calendar.setTime(df.parse(strDate));
	  } catch (Exception e) {
		  return "閿欒";
	  }
      int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
      switch (intTemp){
        case 0:
          week = "鏄熸湡鏃�";
          break;
        case 1:
          week = "鏄熸湡涓�";
          break;
        case 2:
          week = "鏄熸湡浜�";
          break;
        case 3:
          week = "鏄熸湡涓�";
          break;
        case 4:
          week = "鏄熸湡鍥�";
          break;
        case 5:
          week = "鏄熸湡浜�";
          break;
        case 6:
          week = "鏄熸湡鍏�";
          break;
      }
      return week;
    }
    
    /**
     * 鏍规嵁缁欏畾鐨勬棩鏈熷垽鏂槸鍚︿负涓婁笅鍗�.
     *
     * @param strDate the str date
     * @param format the format
     * @return the time quantum
     */
    public static String getTimeQuantum(String strDate, String format) {
        Date mDate = getDateByFormat(strDate, format);
        int hour  = mDate.getHours();
        if(hour >=12)
           return "PM";
        else
           return "AM";
    }
    
    /**
     * 鏍规嵁缁欏畾鐨勬绉掓暟绠楀緱鏃堕棿鐨勬弿杩�.
     *
     * @param milliseconds the milliseconds
     * @return the time description
     */
    public static String getTimeDescription(long milliseconds) {
        if(milliseconds > 1000){
            //澶т簬涓�鍒�
            if(milliseconds/1000/60>1){
                long minute = milliseconds/1000/60;
                long second = milliseconds/1000%60;
                return minute+"鍒�"+second+"绉�";
            }else{
                //鏄剧ず绉�
                return milliseconds/1000+"绉�";
            }
        }else{
            return milliseconds+"姣";
        }
    }
	
	/**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
		System.out.println(formatDateStr2Desc("2012-3-2 12:2:20","MM鏈坉d鏃�  HH:mm"));
	}

}
