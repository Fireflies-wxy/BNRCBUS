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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.bnrc.model.AbCircle;
import com.bnrc.model.AbPoint;


// TODO: Auto-generated Javadoc

/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bMathUtil.java 
 * 鎻忚堪锛氭暟瀛﹀鐞嗙被.
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-01-17 涓嬪崍11:52:13
 */
public class AbMathUtil{

  /**
   * 鍥涜垗浜斿叆.
   *
   * @param number  鍘熸暟
   * @param decimal 淇濈暀鍑犱綅灏忔暟
   * @return 鍥涜垗浜斿叆鍚庣殑鍊�
   */
  public static BigDecimal round(double number, int decimal){
    return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_UP);
  }
  
  /**
   * 鎻忚堪锛氬瓧鑺傛暟缁勮浆鎹㈡垚16杩涘埗涓�.
   *
   * @param b the b
   * @param length the length
   * @return the string
   */
  public static String byte2HexStr(byte[] b, int length){
    String hs = "";
    String stmp = "";
    for (int n = 0; n < length; ++n) {
      stmp = Integer.toHexString(b[n] & 0xFF);
      if (stmp.length() == 1)
        hs = hs + "0" + stmp;
      else {
        hs = hs + stmp;
      }
      hs = hs + ",";
    }
    return hs.toUpperCase();
  } 
  
  /**
   * 浜岃繘鍒惰浆涓哄崄鍏繘鍒�.
   *
   * @param binary the binary
   * @return char hex
   */
	public static char binaryToHex(int binary) {
		char ch = ' ';
		switch (binary){
		case 0:
			ch = '0';
			break;
		case 1:
			ch = '1';
			break;
		case 2:
			ch = '2';
			break;
		case 3:
			ch = '3';
			break;
		case 4:
			ch = '4';
			break;
		case 5:
			ch = '5';
			break;
		case 6:
			ch = '6';
			break;
		case 7:
			ch = '7';
			break;
		case 8:
			ch = '8';
			break;
		case 9:
			ch = '9';
			break;
		case 10:
			ch = 'a';
			break;
		case 11:
			ch = 'b';
			break;
		case 12:
			ch = 'c';
			break;
		case 13:
			ch = 'd';
			break;
		case 14:
			ch = 'e';
			break;
		case 15:
			ch = 'f';
			break;
		default:
			ch = ' ';
		}
		return ch;
	}
	
	
	/**
	 *  
	 * 涓�缁存暟缁勮浆涓轰簩缁存暟缁� 
	 *  
	 *
	 * @param m the m
	 * @param width the width
	 * @param height the height
	 * @return the int[][]
	 */  
    public static int[][] arrayToMatrix(int[] m, int width, int height) {  
        int[][] result = new int[height][width];  
        for (int i = 0; i < height; i++) {  
            for (int j = 0; j < width; j++) {  
                int p = j * height + i;  
                result[i][j] = m[p];  
            }  
        }  
        return result;  
    }  

	
	/**
	 *  
	 * 浜岀淮鏁扮粍杞负涓�缁存暟缁� 
	 *  
	 *
	 * @param m the m
	 * @return the double[]
	 */  
    public static double[] matrixToArray(double[][] m) {  
        int p = m.length * m[0].length;  
        double[] result = new double[p];  
        for (int i = 0; i < m.length; i++) {  
            for (int j = 0; j < m[i].length; j++) {  
                int q = j * m.length + i;  
                result[q] = m[i][j];  
            }  
        }  
        return result;  
    }  

    /**
     * 鎻忚堪锛歩nt鏁扮粍杞崲涓篸ouble鏁扮粍.
     *
     * @param input the input
     * @return the double[]
     */
    public static double[] intToDoubleArray(int[] input) {  
        int length = input.length;  
        double[] output = new double[length];  
        for (int i = 0; i < length; i++){  
            output[i] = Double.valueOf(String.valueOf(input[i]));  
        }
        return output;  
    }  
    
    /**
     * 鎻忚堪锛歩nt浜岀淮鏁扮粍杞崲涓篸ouble浜岀淮鏁扮粍.
     *
     * @param input the input
     * @return the double[][]
     */
    public static double[][] intToDoubleMatrix(int[][] input) {  
        int height = input.length;  
        int width = input[0].length;  
        double[][] output = new double[height][width];  
        for (int i = 0; i < height; i++) {  
            // 鍒�   
            for (int j = 0; j < width; j++) {  
                // 琛�   
                output[i][j] = Double.valueOf(String.valueOf(input[i][j]));  
            }  
        }  
        return output;  
    }  

    /**
     * 璁＄畻鏁扮粍鐨勫钩鍧囧��.
     *
     * @param pixels 鏁扮粍
     * @return int 骞冲潎鍊�
     */
    public static int average(int[] pixels) {
		float m = 0;
		for (int i = 0; i < pixels.length; ++i) {
			m += pixels[i];
		}
		m = m / pixels.length;
		return (int) m;
	}
    
    /**
     * 璁＄畻鏁扮粍鐨勫钩鍧囧��.
     *
     * @param pixels 鏁扮粍
     * @return int 骞冲潎鍊�
     */
    public static int average(double[] pixels) {
		float m = 0;
		for (int i = 0; i < pixels.length; ++i) {
			m += pixels[i];
		}
		m = m / pixels.length;
		return (int) m;
	}
    
    /**
	 * 鎻忚堪锛氳绠楀鏁�
	 * @param value value鐨勫鏁�
	 * @param base  浠ase涓哄簳
	 * @return
	 */
	public static double log(double value, double base) {
		return Math.log(value) / Math.log(base);
	}
    
    /**
     * 
     * 鎻忚堪锛氱偣鍦ㄧ洿绾夸笂.
     * 鐐笰锛坸锛寉锛�,B(x1,y1),C(x2,y2) 鐐笰鍦ㄧ洿绾緽C涓婂悧?
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public boolean pointOnLine(double x,double y,double x1,double y1,double x2,double y2){
        double result = ( x - x1 ) * ( y2 - y1 ) - ( y - y1 ) * ( x2 - x1 );
    	if(result==0){
			return true;
		}else{
			return false;
		}
    }
    
    
	/**
	 * 
	 * 鎻忚堪锛氱偣鍦ㄧ嚎娈典笂.
	 * 鐐笰锛坸锛寉锛�,B(x1,y1),C(x2,y2)   鐐笰鍦ㄧ嚎娈礏C涓婂悧?
	 * @param x
	 * @param y
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
    public static boolean pointAtELine(double x,double y,double x1,double y1,double x2,double y2){
    	double result = ( x - x1 ) * ( y2 - y1 ) - ( y - y1 ) * ( x2 - x1 );
    	if(result==0){
    		if(x >= Math.min(x1, x2) && x <= Math.max(x1,x2) 
    		    && y >= Math.min(y1, y2) && y <= Math.max(y1,y2)){
    		    return true;
	    	}else{
	    	    return false;
	    	}
    	}else{
    		return false;
    	}
    }
    
    /**
     * 
     * 鎻忚堪锛氫袱鏉＄洿绾跨浉浜�.
     * 鐐笰锛坸1锛寉1锛�,B(x2,y2),C(x3,y3),D(x4,y4)   鐩寸嚎AB涓庣洿绾緾D鐩镐氦鍚�?
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param x4
     * @param y4
     * @return
     */
    public  static boolean LineOnLine(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
	    double k1 = ( y2-y1 )/(x2-x1);
	    double k2 = ( y4-y3 )/(x4-x3);
		if(k1==k2){
			//System.out.println("骞宠绾�");
			return false;
		}else{
		  double x = ((x1*y2-y1*x2)*(x3-x4)-(x3*y4-y3*x4)*(x1-x2))/((y2-y1)*(x3-x4)-(y4-y3)*(x1-x2));
		  double y = ( x1*y2-y1*x2 - x*(y2-y1) ) / (x1-x2);
		  //System.out.println("鐩寸嚎鐨勪氦鐐�("+x+","+y+")");
		  return true;
		}
	}
    
    /**
     * 
     * 鎻忚堪锛氱嚎娈典笌绾挎鐩镐氦.
     * 鐐笰锛坸1锛寉1锛�,B(x2,y2),C(x3,y3),D(x4,y4)   
     * 绾挎AB涓庣嚎娈礐D鐩镐氦鍚�?
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param x4
     * @param y4
     * @return
     */
    public static boolean eLineOnELine(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
		    double k1 = ( y2-y1 )/(x2-x1);
		    double k2 = ( y4-y3 )/(x4-x3);
			if(k1==k2){
				//System.out.println("骞宠绾�");
				return false;
			}else{
			  double x = ((x1*y2-y1*x2)*(x3-x4)-(x3*y4-y3*x4)*(x1-x2))/((y2-y1)*(x3-x4)-(y4-y3)*(x1-x2));
			  double y = ( x1*y2-y1*x2 - x*(y2-y1) ) / (x1-x2);
			  //System.out.println("鐩寸嚎鐨勪氦鐐�("+x+","+y+")");
			  if(x >= Math.min(x1, x2) && x <= Math.max(x1,x2) 
					  && y >= Math.min(y1, y2) && y <= Math.max(y1,y2)
				      && x >= Math.min(x3, x4) && x <= Math.max(x3,x4) 
				      && y >= Math.min(y3, y4) && y <= Math.max(y3,y4) ){
					//System.out.println("浜ょ偣锛�"+x+","+y+"锛夊湪绾挎涓�");
				return true;
			  }else{
				//System.out.println("浜ょ偣锛�"+x+","+y+"锛変笉鍦ㄧ嚎娈典笂");
				return false;
			  } 
	       }
	}
    
    /**
     * 
     * 鎻忚堪锛氱嚎娈电洿绾跨浉浜�.
     * 鐐笰锛坸1锛寉1锛�,B(x2,y2),C(x3,y3),D(x4,y4)   
     * 绾挎AB涓庣洿绾緾D鐩镐氦鍚�?
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param x4
     * @param y4
     * @return
     */
    public static boolean eLineOnLine(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
		    double k1 = ( y2-y1 )/(x2-x1);
		    double k2 = ( y4-y3 )/(x4-x3);
			if(k1==k2){
				//System.out.println("骞宠绾�");
				return false;
			}else{
			  double x = ((x1*y2-y1*x2)*(x3-x4)-(x3*y4-y3*x4)*(x1-x2))/((y2-y1)*(x3-x4)-(y4-y3)*(x1-x2));
			  double y = ( x1*y2-y1*x2 - x*(y2-y1) ) / (x1-x2);
			  //System.out.println("浜ょ偣("+x+","+y+")");
			  if(x >= Math.min(x1, x2) && x <= Math.max(x1,x2) 
					  && y >= Math.min(y1, y2) && y <= Math.max(y1,y2)){
					//System.out.println("浜ょ偣锛�"+x+","+y+"锛夊湪绾挎涓�");
				  return true;
			  }else{
							//System.out.println("浜ょ偣锛�"+x+","+y+"锛変笉鍦ㄧ嚎娈典笂");
				return false;
			  } 
		}
	}
    
    /**
     * 
     * 鎻忚堪锛氱偣鍦ㄧ煩褰㈠唴.
     * 鐭╁舰鐨勮竟閮芥槸涓庡潗鏍囩郴骞宠鎴栧瀭鐩寸殑銆�
     * 鍙鍒ゆ柇璇ョ偣鐨勬í鍧愭爣鍜岀旱鍧愭爣鏄惁澶瑰湪鐭╁舰鐨勫乏鍙宠竟鍜屼笂涓嬭竟涔嬮棿銆�
     * 鐐笰锛坸锛寉锛�,B(x1,y1),C(x2,y2)   鐐笰鍦ㄤ互鐩寸嚎BC涓哄瑙掔嚎鐨勭煩褰腑鍚�?
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static boolean pointOnRect(double x,double y,double x1,double y1,double x2,double y2){
	      if(x >= Math.min(x1, x2) && x <= Math.max(x1,x2) && y >= Math.min(y1, y2) && y <= Math.max(y1,y2)){
    	     //System.out.println("鐐癸紙"+x+","+y+"锛夊湪鐭╁舰鍐呬笂");
    	     return true;
	      }else{
	    	 //System.out.println("鐐癸紙"+x+","+y+"锛変笉鍦ㄧ煩褰㈠唴涓�");
	    	 return false;
		  }
	}
    
    /**
     * 
     * 鎻忚堪锛氱煩褰㈠湪鐭╁舰鍐�.
     * 鍙瀵硅绾跨殑涓ょ偣閮藉湪鍙︿竴涓煩褰腑灏卞彲浠ヤ簡.
     * 鐐笰(x1,y1),B(x2,y2)锛孋(x1,y1),D(x2,y2) 浠ョ洿绾緼B涓哄瑙掔嚎鐨勭煩褰㈠湪浠ョ洿绾緽C涓哄瑙掔嚎鐨勭煩褰腑鍚�?
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param x4
     * @param y4
     * @return
     */
    public static boolean rectOnRect(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
	      if(x1 >= Math.min(x3, x4) && x1 <= Math.max(x3,x4) 
			  && y1 >= Math.min(y3, y4) && y1 <= Math.max(y3,y4)
			  && x2 >= Math.min(x3, x4) && x2 <= Math.max(x3,x4) 
			  && y2 >= Math.min(y3, y4) && y2 <= Math.max(y3,y4)){
    	     //System.out.println("鐭╁舰鍦ㄧ煩褰㈠唴");
    	     return true;
	      }else{
    	     //System.out.println("鐭╁舰涓嶅湪鐭╁舰鍐�");
    	     return false;
		  }
	}
    
    /**
     * 
     * 鎻忚堪锛氬渾蹇冨湪鐭╁舰鍐� .
     * 鍦嗗績鍦ㄧ煩褰腑涓斿渾鐨勫崐寰勫皬浜庣瓑浜庡渾蹇冨埌鐭╁舰鍥涜竟鐨勮窛绂荤殑鏈�灏忓�笺��
     * 鍦嗗績(x,y) 鍗婂緞r  鐭╁舰瀵硅鐐笰锛坸1锛寉1锛夛紝B(x2锛寉2)
     * @param x
     * @param y
     * @param r
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static boolean circleOnRect(double x,double y,double r,double x1,double y1,double x2,double y2){
		//鍦嗗績鍦ㄧ煩褰㈠唴   
		if(x >= Math.min(x1, x2) && x <= Math.max(x1,x2) 
						  && y >= Math.min(y1, y2) && y <= Math.max(y1,y2)){
		//鍦嗗績鍒�4鏉¤竟鐨勮窛绂�		  
        double l1= Math.abs(x-x1);
		double l2= Math.abs(y-y2);
		double l3= Math.abs(x-x2);
		double l4= Math.abs(y-y2);
    	if(r<=l1 && r<=l2 && r<=l3 && r<=l4){
    		  //System.out.println("鍦嗗湪鐭╁舰鍐�");
	    	  return true;
    	  }else{
    		  //System.out.println("鍦嗕笉鍦ㄧ煩褰㈠唴");
	    	  return false;
    	  }
    	 
       }else{
    	     //System.out.println("鍦嗕笉鍦ㄧ煩褰㈠唴");
    	    return false;
	   }
	}
    
    /**
	 * 
	 * 鎻忚堪锛氱偣鏄惁鏄袱涓渾鐨勪氦鐐�.
	 * @param point
	 * @param c1
	 * @param c2
	 * @return
	 */
    public static boolean pointOnCircle(AbPoint point,AbCircle c1,AbCircle c2){
		
		if(Math.pow(point.x-c2.point.x,2) + Math.pow(point.y-c2.point.y,2)==Math.pow(c2.r,2)
				&& Math.pow(point.x-c1.point.x,2) + Math.pow(point.y-c1.point.y,2)==Math.pow(c1.r,2)){
			return true;
		}
		return false;
		
	}
    
    /**
 	 * 
 	 * 鎻忚堪锛氱偣鏄惁鏄袱涓渾鐨勪氦鐐�,鍏佽0.01璇樊.
 	 * @param point
 	 * @param c1
 	 * @param c2
 	 * @param offset
 	 * @return
 	 */
     public static boolean pointOnCircle(AbPoint point,AbCircle c1,AbCircle c2,float offset){
 		
 		if((Math.pow(point.x-c2.point.x,2) + Math.pow(point.y-c2.point.y,2)<=Math.pow(c2.r,2)+offset && Math.pow(point.x-c2.point.x,2) + Math.pow(point.y-c2.point.y,2)>=Math.pow(c2.r,2)-offset)
 				&& (Math.pow(point.x-c1.point.x,2) + Math.pow(point.y-c1.point.y,2)<=Math.pow(c1.r,2)+offset && Math.pow(point.x-c1.point.x,2) + Math.pow(point.y-c1.point.y,2)>=Math.pow(c1.r,2)-offset)){
 			return true;
 		}
 		return false;
 		
 	}
    
    /**
     * 
     * 鎻忚堪锛氱偣鍦ㄥ渾涓�.
     * @param point
     * @param circle
     * @return
     */
    public static boolean pointInCircle(AbPoint point,AbCircle circle){
		//鍦嗙殑鏂圭▼ (x-x0)^2 + (y-y0)^2 <=r^2
		if(Math.pow(point.x-circle.point.x,2) + Math.pow(point.y-circle.point.y,2) <= Math.pow(circle.r,2)){
			return true;
        }else{
    	    return false;
	   }
	}
    
    
    /**
     * 
     * 鎻忚堪锛氳幏鍙栦袱鐐归棿鐨勮窛绂�.
     * @param p1
     * @param p2
     * @return
     */
    public static double getDistance(AbPoint p1,AbPoint p2) {  
        return getDistance(p1.x,p1.y,p2.x,p2.y);  
    }  

    /**
     *  
     * 鎻忚堪锛氳幏鍙栦袱鐐归棿鐨勮窛绂�.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double getDistance(double x1,double y1,double x2,double y2) {  
    	double x = x1 - x2;  
    	double y = y1 - y2;  
        return Math.sqrt(x * x + y * y);  
    }  
    
    
    /**
	 * 鐭╁舰纰版挒妫�娴� 鍙傛暟涓簒,y,width,height
	 * @param x1 绗竴涓煩褰㈢殑x
	 * @param y1 绗竴涓煩褰㈢殑y
	 * @param w1 绗竴涓煩褰㈢殑w
	 * @param h1 绗竴涓煩褰㈢殑h
	 * @param x2 绗簩涓煩褰㈢殑x
	 * @param y2 绗簩涓煩褰㈢殑y
	 * @param w2 绗簩涓煩褰㈢殑w
	 * @param h2 绗簩涓煩褰㈢殑h
	 * @return 鏄惁纰版挒
	 */
	public static boolean isRectCollision(float x1, float y1, float w1,
			float h1, float x2, float y2, float w2, float h2) {
		if (x2 > x1 && x2 > x1 + w1) {
			return false;
		} else if (x2 < x1 && x2 < x1 - w2) {
			return false;
		} else if (y2 > y1 && y2 > y1 + h1) {
			return false;
		} else if (y2 < y1 && y2 < y1 - h2) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 
	 * 鎻忚堪锛氭眰涓や釜鍦嗙殑浜ょ偣.
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static List<AbPoint> circleCrossoverPoint(AbCircle circle1,AbCircle circle2){
		List<AbPoint> pointList = new ArrayList<AbPoint>();
		//绗竴涓渾鐨勫弬鏁版柟绋�:
		// x = r1 * cosCita + x1
		// y = r1 * sinCita + y1
		
		//绗簩涓渾鐨勬柟绋�
		//(x0-x2)^2 + (y0-y2)^2 = r2^2
		
		//绗竴涓甫鍏ョ浜屼釜
		//2r1(x1-x2)cosCita + 2r1(y1-y2)sinCita = r2^2 - r1^2 - (x1-x2)^2 - (y1-y2)^2
		//浠� :
		//a = 2r1(x1-x2)
		//b = 2r1(y1-y2)
		//c = r2^2 - r1^2 - (x1-x2)^2 - (y1-y2)^2
				
		double a = 2 * circle1.r * (circle1.point.x - circle2.point.x);
		
		double b = 2 * circle1.r * (circle1.point.y - circle2.point.y);
		
		double c = (Math.pow(circle2.r, 2) - Math.pow(circle1.r, 2) - Math.pow((circle1.point.x - circle2.point.x), 2) - Math.pow((circle1.point.y - circle2.point.y), 2));
		
		//寰楀埌锛�
		//acosCita + bsinCita = c
		
		//double sinCita = Math.sqrt(1-cosCita);
		
		//a*cosCita + b* Math.sqrt(1-cosCita) = c
				
		//寰楀埌锛�
		//(a^2+b^2)cosCita^2 - 2ac*cosCita + (c^2-b^2) = 0
		
		//鏍规嵁 ax^2+bx + c = 0 寮�
		//浠わ細p = a^2+b^2
		//   q = - 2ac,
		//   r = c^2-b^2
		
		//a
		double p = (Math.pow(a, 2) + Math.pow(b, 2));
		//b
		double q = -2 * a * c;
		//c
		double r = (Math.pow(c, 2) - Math.pow(b, 2));
		
		//cosCita = (-b+-(b^2-4ac)^(1/2))/2a
		double t = (Math.pow(q, 2) - 4 * p * r);
		double cosCita = (Math.sqrt(t) - q) / (2 * p);
		double cosCita2 = (-Math.sqrt(t) - q) / (2 * p);
		
		double x_1 = cosCita * circle1.r + circle1.point.x;
		double y_1_1 = Math.sqrt(Math.pow(circle1.r, 2)-Math.pow(x_1-circle1.point.x,2))+circle1.point.y;
		double y_1_2 = -Math.sqrt(Math.pow(circle1.r, 2)-Math.pow(x_1-circle1.point.x,2))+circle1.point.y;
		
		Set<AbPoint> pointSet = new HashSet<AbPoint>();
		AbPoint p1_1 = new AbPoint(x_1,y_1_1);
		if(pointOnCircle(p1_1,circle1,circle2)){
			pointSet.add(p1_1);
		}
		
		AbPoint p1_2 = new AbPoint(x_1,y_1_2);
		if(pointOnCircle(p1_2,circle1,circle2)){
			pointSet.add(p1_2);
		}
		
		double x_2 = cosCita2 * circle1.r + circle1.point.x;
		double y_2_1 = Math.sqrt(Math.pow(circle1.r, 2)-Math.pow(x_2-circle1.point.x,2))+circle1.point.y;
		double y_2_2 = -Math.sqrt(Math.pow(circle1.r, 2)-Math.pow(x_2-circle1.point.x,2))+circle1.point.y;
		
		AbPoint p2_1 = new AbPoint(x_2,y_2_1);
		if(pointOnCircle(p2_1,circle1,circle2)){
			pointSet.add(p2_1);
		}
		
		AbPoint p2_2 = new AbPoint(x_2,y_2_2);
		if(pointOnCircle(p2_2,circle1,circle2)){
			pointSet.add(p2_2);
		}
		for(Iterator<AbPoint> iter = pointSet.iterator();iter.hasNext();){   
			AbPoint point = (AbPoint)iter.next();   
			pointList.add(point);
        }  
		return pointList;
	}
	
 
}
