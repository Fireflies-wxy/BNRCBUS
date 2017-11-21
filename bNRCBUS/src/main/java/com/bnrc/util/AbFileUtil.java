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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;

import com.bnrc.global.AbAppConfig;

// TODO: Auto-generated Javadoc

/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bFileUtil.java 
 * 鎻忚堪锛氭枃浠舵搷浣滅被.
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-01-18 涓嬪崍11:52:13
 */
public class AbFileUtil {
	
	/** 榛樿APP鏍圭洰褰�. */
	private static  String downloadRootDir = null;
	
    /** 榛樿涓嬭浇鍥剧墖鏂囦欢鐩綍. */
	private static  String imageDownloadDir = null;
    
    /** 榛樿涓嬭浇鏂囦欢鐩綍. */
	private static  String fileDownloadDir = null;
	
	/** 榛樿缂撳瓨鐩綍. */
	private static  String cacheDownloadDir = null;
	
	/** 榛樿涓嬭浇鏁版嵁搴撴枃浠剁殑鐩綍. */
	private static  String dbDownloadDir = null;
	
	/** 鍓╀綑绌洪棿澶т簬200M鎵嶄娇鐢⊿D缂撳瓨. */
	private static int freeSdSpaceNeededToCache = 200*1024*1024;
	 
	 /**
	  * 鎻忚堪锛氶�氳繃鏂囦欢鐨勭綉缁滃湴鍧�浠嶴D鍗′腑璇诲彇鍥剧墖锛屽鏋淪D涓病鏈夊垯鑷姩涓嬭浇骞朵繚瀛�.
	  * @param url 鏂囦欢鐨勭綉缁滃湴鍧�
	  * @param type 鍥剧墖鐨勫鐞嗙被鍨嬶紙鍓垏鎴栬�呯缉鏀惧埌鎸囧畾澶у皬锛屽弬鑰傾bImageUtil绫伙級
	  * 濡傛灉璁剧疆涓哄師鍥撅紝鍒欏悗杈瑰弬鏁版棤鏁堬紝寰楀埌鍘熷浘
	  * @param desiredWidth 鏂板浘鐗囩殑瀹�
	  * @param desiredHeight 鏂板浘鐗囩殑楂�
	  * @return Bitmap 鏂板浘鐗�
	  */
	 public static Bitmap getBitmapFromSD(String url,int type,int desiredWidth, int desiredHeight){
		 Bitmap bitmap = null;
		 try {
			 if(AbStrUtil.isEmpty(url)){
		    	return null;
		     }
			 
			 //SD鍗′笉瀛樺湪 鎴栬�呭墿浣欑┖闂翠笉瓒充簡灏变笉缂撳瓨鍒癝D鍗′簡
			 if(!isCanUseSD() || freeSdSpaceNeededToCache < freeSpaceOnSD()){
				 bitmap = getBitmapFromURL(url,type,desiredWidth,desiredHeight);
				 return bitmap;
		     }
			 //涓嬭浇鏂囦欢锛屽鏋滀笉瀛樺湪灏变笅杞斤紝瀛樺湪鐩存帴杩斿洖鍦板潃
			 String downFilePath = downloadFile(url,imageDownloadDir);
			 if(downFilePath != null){
				 //鑾峰彇鍥剧墖
				 return getBitmapFromSD(new File(downFilePath),type,desiredWidth,desiredHeight);
			 }else{
				 return null;
			 }

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	 }
	 
	 
	 /**
 	 * 鎻忚堪锛氶�氳繃鏂囦欢鐨勬湰鍦板湴鍧�浠嶴D鍗¤鍙栧浘鐗�.
 	 *
 	 * @param file the file
 	 * @param type 鍥剧墖鐨勫鐞嗙被鍨嬶紙鍓垏鎴栬�呯缉鏀惧埌鎸囧畾澶у皬锛屽弬鑰傾bConstant绫伙級
 	 * 濡傛灉璁剧疆涓哄師鍥撅紝鍒欏悗杈瑰弬鏁版棤鏁堬紝寰楀埌鍘熷浘
 	 * @param desiredWidth 鏂板浘鐗囩殑瀹�
 	 * @param desiredHeight 鏂板浘鐗囩殑楂�
 	 * @return Bitmap 鏂板浘鐗�
 	 */
	 public static Bitmap getBitmapFromSD(File file,int type,int desiredWidth, int desiredHeight){
		 Bitmap bitmap = null;
		 try {
			 //SD鍗℃槸鍚﹀瓨鍦�
			 if(!isCanUseSD()){
		    	return null;
		     }
			 
			 //鏂囦欢鏄惁瀛樺湪
			 if(!file.exists()){
				 return null;
			 }
			 
			 //鏂囦欢瀛樺湪
			 if(type == AbImageUtil.CUTIMG){
				bitmap = AbImageUtil.getCutBitmap(file,desiredWidth,desiredHeight);
		 	 }else if(type == AbImageUtil.SCALEIMG){
		 		bitmap = AbImageUtil.getScaleBitmap(file,desiredWidth,desiredHeight);
		 	 }else{
		 		bitmap = AbImageUtil.getBitmap(file);
		 	 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	 }
	 
	/**
     * 鎻忚堪锛氶�氳繃鏂囦欢鐨勬湰鍦板湴鍧�浠嶴D鍗¤鍙栧浘鐗�.
     *
     * @param file the file
     * @return Bitmap 鍥剧墖
     */
     public static Bitmap getBitmapFromSD(File file){
         Bitmap bitmap = null;
         try {
             //SD鍗℃槸鍚﹀瓨鍦�
             if(!isCanUseSD()){
                return null;
             }
             //鏂囦欢鏄惁瀛樺湪
             if(!file.exists()){
                 return null;
             }
             //鏂囦欢瀛樺湪
             bitmap = AbImageUtil.getBitmap(file);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
     }
	 
	 
	 /**
	  * 鎻忚堪锛氬皢鍥剧墖鐨刡yte[]鍐欏叆鏈湴鏂囦欢.
	  * @param imgByte 鍥剧墖鐨刡yte[]褰㈠娍
	  * @param fileName 鏂囦欢鍚嶇О锛岄渶瑕佸寘鍚悗缂�锛屽.jpg
	  * @param type 鍥剧墖鐨勫鐞嗙被鍨嬶紙鍓垏鎴栬�呯缉鏀惧埌鎸囧畾澶у皬锛屽弬鑰傾bConstant绫伙級
	  * @param desiredWidth 鏂板浘鐗囩殑瀹�
	  * @param desiredHeight 鏂板浘鐗囩殑楂�
	  * @return Bitmap 鏂板浘鐗�
	  */
     public static Bitmap getBitmapFromByte(byte[] imgByte,String fileName,int type,int desiredWidth, int desiredHeight){
    	   FileOutputStream fos = null;
    	   DataInputStream dis = null;
    	   ByteArrayInputStream bis = null;
    	   Bitmap bitmap = null;
    	   File file = null;
    	   try {
    		   if(imgByte!=null){
    			   
    			   file = new File(imageDownloadDir+fileName);
    			   if(!file.exists()){
    			        file.createNewFile();
    			   }
    			   fos = new FileOutputStream(file);
    			   int readLength = 0;
    			   bis = new ByteArrayInputStream(imgByte);
    			   dis = new DataInputStream(bis);
    			   byte[] buffer = new byte[1024];
    			   
    			   while ((readLength = dis.read(buffer))!=-1) {
    				   fos.write(buffer, 0, readLength);
    			       try {
    						Thread.sleep(500);
    				   } catch (Exception e) {
    				   }
    			   }
    			   fos.flush();
    			   
    			   bitmap = getBitmapFromSD(file,type,desiredWidth,desiredHeight);
    		   }
			   
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(dis!=null){
				try {
					dis.close();
				} catch (Exception e) {
				}
			}    
			if(bis!=null){
				try {
					bis.close();
				} catch (Exception e) {
				}
			}
			if(fos!=null){
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}
        return  bitmap;
     }
	    
	/**
	 * 鎻忚堪锛氭牴鎹甎RL浠庝簰杩炵綉鑾峰彇鍥剧墖.
	 * @param url 瑕佷笅杞芥枃浠剁殑缃戠粶鍦板潃
	 * @param type 鍥剧墖鐨勫鐞嗙被鍨嬶紙鍓垏鎴栬�呯缉鏀惧埌鎸囧畾澶у皬锛屽弬鑰傾bConstant绫伙級
	 * @param desiredWidth 鏂板浘鐗囩殑瀹�
	 * @param desiredHeight 鏂板浘鐗囩殑楂�
	 * @return Bitmap 鏂板浘鐗�
	 */
	public static Bitmap getBitmapFromURL(String url,int type,int desiredWidth, int desiredHeight){
		Bitmap bit = null;
		try {
			bit = AbImageUtil.getBitmap(url, type, desiredWidth, desiredHeight);
	    } catch (Exception e) {
	    	AbLogUtil.d(AbFileUtil.class, "涓嬭浇鍥剧墖寮傚父锛�"+e.getMessage());
		}
		return bit;
	}
	
	/**
	 * 鎻忚堪锛氳幏鍙杝rc涓殑鍥剧墖璧勬簮.
	 *
	 * @param src 鍥剧墖鐨剆rc璺緞锛屽锛堚�渋mage/arrow.png鈥濓級
	 * @return Bitmap 鍥剧墖
	 */
	public static Bitmap getBitmapFromSrc(String src){
		Bitmap bit = null;
		try {
			bit = BitmapFactory.decodeStream(AbFileUtil.class.getResourceAsStream(src));
	    } catch (Exception e) {
	    	AbLogUtil.d(AbFileUtil.class, "鑾峰彇鍥剧墖寮傚父锛�"+e.getMessage());
		}
		return bit;
	}
	
	/**
	 * 鎻忚堪锛氳幏鍙朅sset涓殑鍥剧墖璧勬簮.
	 *
	 * @param context the context
	 * @param fileName the file name
	 * @return Bitmap 鍥剧墖
	 */
	public static Bitmap getBitmapFromAsset(Context context,String fileName){
		Bitmap bit = null;
		try {
			AssetManager assetManager = context.getAssets();
			InputStream is = assetManager.open(fileName);
			bit = BitmapFactory.decodeStream(is);
	    } catch (Exception e) {
	    	AbLogUtil.d(AbFileUtil.class, "鑾峰彇鍥剧墖寮傚父锛�"+e.getMessage());
		}
		return bit;
	}
	
	/**
	 * 鎻忚堪锛氳幏鍙朅sset涓殑鍥剧墖璧勬簮.
	 *
	 * @param context the context
	 * @param fileName the file name
	 * @return Drawable 鍥剧墖
	 */
	public static Drawable getDrawableFromAsset(Context context,String fileName){
		Drawable drawable = null;
		try {
			AssetManager assetManager = context.getAssets();
			InputStream is = assetManager.open(fileName);
			drawable = Drawable.createFromStream(is,null);
	    } catch (Exception e) {
	    	AbLogUtil.d(AbFileUtil.class, "鑾峰彇鍥剧墖寮傚父锛�"+e.getMessage());
		}
		return drawable;
	}
	
     /**
 	 * 涓嬭浇缃戠粶鏂囦欢鍒癝D鍗′腑.濡傛灉SD涓瓨鍦ㄥ悓鍚嶆枃浠跺皢涓嶅啀涓嬭浇
 	 *
 	 * @param url 瑕佷笅杞芥枃浠剁殑缃戠粶鍦板潃
 	 * @param dirPath the dir path
 	 * @return 涓嬭浇濂界殑鏈湴鏂囦欢鍦板潃
 	 */
 	 public static String downloadFile(String url,String dirPath){
 		 InputStream in = null;
 		 FileOutputStream fileOutputStream = null;
 		 HttpURLConnection connection = null;
 		 String downFilePath = null;
 		 File file = null;
 		 try {
 	    	if(!isCanUseSD()){
 	    		return null;
 	    	}
             //鍏堝垽鏂璖D鍗′腑鏈夋病鏈夎繖涓枃浠讹紝涓嶆瘮杈冨悗缂�閮ㄥ垎姣旇緝
             String fileNameNoMIME  = getCacheFileNameFromUrl(url);
             File parentFile = new File(imageDownloadDir);
             File[] files = parentFile.listFiles();
             for(int i = 0; i < files.length; ++i){
                  String fileName = files[i].getName();
                  String name = fileName.substring(0,fileName.lastIndexOf("."));
                  if(name.equals(fileNameNoMIME)){
                      //鏂囦欢宸插瓨鍦�
                      return files[i].getPath();
                  }
             } 
             
 			URL mUrl = new URL(url);
 			connection = (HttpURLConnection)mUrl.openConnection();
 			connection.connect();
             //鑾峰彇鏂囦欢鍚嶏紝涓嬭浇鏂囦欢
             String fileName  = getCacheFileNameFromUrl(url,connection);
             
             file = new File(imageDownloadDir,fileName);
             downFilePath = file.getPath();
             if(!file.exists()){
                 file.createNewFile();
             }else{
                 //鏂囦欢宸插瓨鍦�
                 return file.getPath();
             }
 			in = connection.getInputStream();
 			fileOutputStream = new FileOutputStream(file);
 			byte[] b = new byte[1024];
 			int temp = 0;
 			while((temp=in.read(b))!=-1){
 				fileOutputStream.write(b, 0, temp);
 			}
 		}catch(Exception e){
 			e.printStackTrace();
 			AbLogUtil.e(AbFileUtil.class, "鏈夋枃浠朵笅杞藉嚭閿欎簡,宸插垹闄�");
 			//妫�鏌ユ枃浠跺ぇ灏�,濡傛灉鏂囦欢涓�0B璇存槑缃戠粶涓嶅ソ娌℃湁涓嬭浇鎴愬姛锛岃灏嗗缓绔嬬殑绌烘枃浠跺垹闄�
 			if(file != null){
 				file.delete();
 			}
 			file = null;
 			downFilePath = null;
 		}finally{
 			try {
 				if(in!=null){
 					in.close();
 				}
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 			try {
 				if(fileOutputStream!=null){
 					fileOutputStream.close();
 				}
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 			try {
 				if(connection!=null){
 				    connection.disconnect();
 				}
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 		}
 		return downFilePath;
 	 }
	
	/**
	 * 鎻忚堪锛氳幏鍙栫綉缁滄枃浠剁殑澶у皬.
	 *
	 * @param Url 鍥剧墖鐨勭綉缁滆矾寰�
	 * @return int 缃戠粶鏂囦欢鐨勫ぇ灏�
	 */
	public static int getContentLengthFromUrl(String Url){
		int mContentLength = 0;
		try {
			 URL url = new URL(Url);
			 HttpURLConnection mHttpURLConnection = (HttpURLConnection) url.openConnection();
			 mHttpURLConnection.setConnectTimeout(5 * 1000);
			 mHttpURLConnection.setRequestMethod("GET");
			 mHttpURLConnection.setRequestProperty("Accept","image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			 mHttpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
			 mHttpURLConnection.setRequestProperty("Referer", Url);
			 mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
			 mHttpURLConnection.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			 mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			 mHttpURLConnection.connect();
			 if (mHttpURLConnection.getResponseCode() == 200){
				 // 鏍规嵁鍝嶅簲鑾峰彇鏂囦欢澶у皬
				 mContentLength = mHttpURLConnection.getContentLength();
			 }
	    } catch (Exception e) {
	    	 e.printStackTrace();
	    	 AbLogUtil.d(AbFileUtil.class, "鑾峰彇闀垮害寮傚父锛�"+e.getMessage());
		}
		return mContentLength;
	}
	
	/**
     * 鑾峰彇鏂囦欢鍚嶏紝閫氳繃缃戠粶鑾峰彇.
     * @param url 鏂囦欢鍦板潃
     * @return 鏂囦欢鍚�
     */
    public static String getRealFileNameFromUrl(String url){
        String name = null;
        try {
            if(AbStrUtil.isEmpty(url)){
                return name;
            }
            
            URL mUrl = new URL(url);
            HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
            mHttpURLConnection.setConnectTimeout(5 * 1000);
            mHttpURLConnection.setRequestMethod("GET");
            mHttpURLConnection.setRequestProperty("Accept","image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            mHttpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
            mHttpURLConnection.setRequestProperty("Referer", url);
            mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
            mHttpURLConnection.setRequestProperty("User-Agent","");
            mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            mHttpURLConnection.connect();
            if (mHttpURLConnection.getResponseCode() == 200){
                for (int i = 0;; i++) {
                        String mine = mHttpURLConnection.getHeaderField(i);
                        if (mine == null){
                            break;
                        }
                        if ("content-disposition".equals(mHttpURLConnection.getHeaderFieldKey(i).toLowerCase())) {
                            Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
                            if (m.find())
                                return m.group(1).replace("\"", "");
                        }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AbLogUtil.e(AbFileUtil.class, "缃戠粶涓婅幏鍙栨枃浠跺悕澶辫触");
        }
        return name;
    }
	
	 
	/**
	 * 鑾峰彇鐪熷疄鏂囦欢鍚嶏紙xx.鍚庣紑锛夛紝閫氳繃缃戠粶鑾峰彇.
	 * @param connection 杩炴帴
	 * @return 鏂囦欢鍚�
	 */
	public static String getRealFileName(HttpURLConnection connection){
		String name = null;
		try {
			if(connection == null){
				return name;
			}
			if (connection.getResponseCode() == 200){
				for (int i = 0;; i++) {
						String mime = connection.getHeaderField(i);
						if (mime == null){
							break;
						}
						// "Content-Disposition","attachment; filename=1.txt"
						// Content-Length
						if ("content-disposition".equals(connection.getHeaderFieldKey(i).toLowerCase())) {
							Matcher m = Pattern.compile(".*filename=(.*)").matcher(mime.toLowerCase());
							if (m.find()){
								return m.group(1).replace("\"", "");
							}
						}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			AbLogUtil.e(AbFileUtil.class, "缃戠粶涓婅幏鍙栨枃浠跺悕澶辫触");
		}
		return name;
    }
	
	/**
	 * 鑾峰彇鐪熷疄鏂囦欢鍚嶏紙xx.鍚庣紑锛夛紝閫氳繃缃戠粶鑾峰彇.
	 *
	 * @param response the response
	 * @return 鏂囦欢鍚�
	 */
    public static String getRealFileName(HttpResponse response){
        String name = null;
        try {
            if(response == null){
                return name;
            }
            //鑾峰彇鏂囦欢鍚�
            Header[] headers = response.getHeaders("content-disposition");
            for(int i=0;i<headers.length;i++){
                 Matcher m = Pattern.compile(".*filename=(.*)").matcher(headers[i].getValue());
                 if (m.find()){
                     name =  m.group(1).replace("\"", "");
                 }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AbLogUtil.e(AbFileUtil.class, "缃戠粶涓婅幏鍙栨枃浠跺悕澶辫触");
        }
        return name;
    }
    
    /**
     * 鑾峰彇鏂囦欢鍚嶏紙涓嶅惈鍚庣紑锛�.
     *
     * @param url 鏂囦欢鍦板潃
     * @return 鏂囦欢鍚�
     */
    public static String getCacheFileNameFromUrl(String url){
        if(AbStrUtil.isEmpty(url)){
            return null;
        }
        String name = null;
        try {
            name = AbMd5.MD5(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }
    
	
	/**
	 * 鑾峰彇鏂囦欢鍚嶏紙.鍚庣紑锛夛紝澶栭摼妯″紡鍜岄�氳繃缃戠粶鑾峰彇.
	 *
	 * @param url 鏂囦欢鍦板潃
	 * @param response the response
	 * @return 鏂囦欢鍚�
	 */
    public static String getCacheFileNameFromUrl(String url,HttpResponse response){
        if(AbStrUtil.isEmpty(url)){
            return null;
        }
        String name = null;
        try {
            //鑾峰彇鍚庣紑
            String suffix = getMIMEFromUrl(url,response);
            if(AbStrUtil.isEmpty(suffix)){
                suffix = ".ab";
            }
            name = AbMd5.MD5(url)+suffix;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }
	
	
	/**
	 * 鑾峰彇鏂囦欢鍚嶏紙.鍚庣紑锛夛紝澶栭摼妯″紡鍜岄�氳繃缃戠粶鑾峰彇.
	 *
	 * @param url 鏂囦欢鍦板潃
	 * @param connection the connection
	 * @return 鏂囦欢鍚�
	 */
	public static String getCacheFileNameFromUrl(String url,HttpURLConnection connection){
		if(AbStrUtil.isEmpty(url)){
			return null;
		}
		String name = null;
		try {
			//鑾峰彇鍚庣紑
			String suffix = getMIMEFromUrl(url,connection);
			if(AbStrUtil.isEmpty(suffix)){
				suffix = ".ab";
			}
			name = AbMd5.MD5(url)+suffix;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
    }
	
	
	/**
	 * 鑾峰彇鏂囦欢鍚庣紑锛屾湰鍦�.
	 *
	 * @param url 鏂囦欢鍦板潃
	 * @param connection the connection
	 * @return 鏂囦欢鍚庣紑
	 */
	public static String getMIMEFromUrl(String url,HttpURLConnection connection){
		
		if(AbStrUtil.isEmpty(url)){
			return null;
		}
		String suffix = null;
		try {
			//鑾峰彇鍚庣紑
			if(url.lastIndexOf(".")!=-1){
				 suffix = url.substring(url.lastIndexOf("."));
				 if(suffix.indexOf("/")!=-1 || suffix.indexOf("?")!=-1 || suffix.indexOf("&")!=-1){
					 suffix = null;
				 }
			}
			if(AbStrUtil.isEmpty(suffix)){
				 //鑾峰彇鏂囦欢鍚�  杩欎釜鏁堢巼涓嶉珮
				 String fileName = getRealFileName(connection);
				 if(fileName!=null && fileName.lastIndexOf(".")!=-1){
					 suffix = fileName.substring(fileName.lastIndexOf("."));
				 }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return suffix;
    }
	
	/**
	 * 鑾峰彇鏂囦欢鍚庣紑锛屾湰鍦板拰缃戠粶.
	 *
	 * @param url 鏂囦欢鍦板潃
	 * @param response the response
	 * @return 鏂囦欢鍚庣紑
	 */
    public static String getMIMEFromUrl(String url,HttpResponse response){
        
        if(AbStrUtil.isEmpty(url)){
            return null;
        }
        String mime = null;
        try {
            //鑾峰彇鍚庣紑
            if(url.lastIndexOf(".")!=-1){
                mime = url.substring(url.lastIndexOf("."));
                 if(mime.indexOf("/")!=-1 || mime.indexOf("?")!=-1 || mime.indexOf("&")!=-1){
                     mime = null;
                 }
            }
            if(AbStrUtil.isEmpty(mime)){
                 //鑾峰彇鏂囦欢鍚�  杩欎釜鏁堢巼涓嶉珮
                 String fileName = getRealFileName(response);
                 if(fileName!=null && fileName.lastIndexOf(".")!=-1){
                     mime = fileName.substring(fileName.lastIndexOf("."));
                 }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mime;
    }
	
	/**
	 * 鎻忚堪锛氫粠sd鍗′腑鐨勬枃浠惰鍙栧埌byte[].
	 *
	 * @param path sd鍗′腑鏂囦欢璺緞
	 * @return byte[]
	 */
	public static byte[] getByteArrayFromSD(String path) {  
		byte[] bytes = null; 
		ByteArrayOutputStream out = null;
	    try {
	    	File file = new File(path);  
	    	//SD鍗℃槸鍚﹀瓨鍦�
			if(!isCanUseSD()){
				 return null;
		    }
			//鏂囦欢鏄惁瀛樺湪
			if(!file.exists()){
				 return null;
			}
	    	
	    	long fileSize = file.length();  
	    	if (fileSize > Integer.MAX_VALUE) {  
	    	      return null;  
	    	}  

			FileInputStream in = new FileInputStream(path);  
		    out = new ByteArrayOutputStream(1024);  
			byte[] buffer = new byte[1024];  
			int size=0;  
			while((size=in.read(buffer))!=-1) {  
			   out.write(buffer,0,size);  
			}  
			in.close();  
            bytes = out.toByteArray();  
   
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(out!=null){
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
	    return bytes;
    }  
	
	/**
	 * 鎻忚堪锛氬皢byte鏁扮粍鍐欏叆鏂囦欢.
	 *
	 * @param path the path
	 * @param content the content
	 * @param create the create
	 */
	 public static void writeByteArrayToSD(String path, byte[] content,boolean create){  
	    
		 FileOutputStream fos = null;
		 try {
	    	File file = new File(path);  
	    	//SD鍗℃槸鍚﹀瓨鍦�
			if(!isCanUseSD()){
				 return;
		    }
			//鏂囦欢鏄惁瀛樺湪
			if(!file.exists()){
				if(create){
					File parent = file.getParentFile();
					if(!parent.exists()){
						parent.mkdirs();
						file.createNewFile();
					}
				}else{
				    return;
				}
			}
			fos = new FileOutputStream(path);  
			fos.write(content);  
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}
   }  
	 
	/**
	 * 鎻忚堪锛歋D鍗℃槸鍚﹁兘鐢�.
	 *
	 * @return true 鍙敤,false涓嶅彲鐢�
	 */
	public static boolean isCanUseSD() { 
	    try { 
	        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); 
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	    return false; 
    } 

	/**
	 * 鎻忚堪锛氬垵濮嬪寲瀛樺偍鐩綍.
	 *
	 * @param context the context
	 */
	public static void initFileDir(Context context){
		
		PackageInfo info = AbAppUtil.getPackageInfo(context);
		
		//榛樿涓嬭浇鏂囦欢鏍圭洰褰�. 
		String downloadRootPath = File.separator + AbAppConfig.DOWNLOAD_ROOT_DIR + File.separator + info.packageName + File.separator;
		
	    //榛樿涓嬭浇鍥剧墖鏂囦欢鐩綍. 
		String imageDownloadPath = downloadRootPath + AbAppConfig.DOWNLOAD_IMAGE_DIR + File.separator;
	    
	    //榛樿涓嬭浇鏂囦欢鐩綍.
		String fileDownloadPath = downloadRootPath + AbAppConfig.DOWNLOAD_FILE_DIR + File.separator;
		
		//榛樿缂撳瓨鐩綍.
		String cacheDownloadPath = downloadRootPath + AbAppConfig.CACHE_DIR + File.separator;
		
		//榛樿DB鐩綍.
		String dbDownloadPath = downloadRootPath + AbAppConfig.DB_DIR + File.separator;
	    
		try {
			if(!isCanUseSD()){
				return;
			}else{
				
				File root = Environment.getExternalStorageDirectory();
				File downloadDir = new File(root.getAbsolutePath() + downloadRootPath);			
				if(!downloadDir.exists()){
					downloadDir.mkdirs();
				}
				downloadRootDir = downloadDir.getPath();
				
				File cacheDownloadDirFile = new File(root.getAbsolutePath() + cacheDownloadPath);
				if(!cacheDownloadDirFile.exists()){
					cacheDownloadDirFile.mkdirs();
				}
				cacheDownloadDir = cacheDownloadDirFile.getPath();
				
				File imageDownloadDirFile = new File(root.getAbsolutePath() + imageDownloadPath);
				if(!imageDownloadDirFile.exists()){
					imageDownloadDirFile.mkdirs();
				}
				imageDownloadDir = imageDownloadDirFile.getPath();
				
				File fileDownloadDirFile = new File(root.getAbsolutePath() + fileDownloadPath);
				if(!fileDownloadDirFile.exists()){
					fileDownloadDirFile.mkdirs();
				}
				fileDownloadDir = fileDownloadDirFile.getPath();
				
				File dbDownloadDirFile = new File(root.getAbsolutePath() + dbDownloadPath);
				if(!dbDownloadDirFile.exists()){
					dbDownloadDirFile.mkdirs();
				}
				dbDownloadDir = dbDownloadDirFile.getPath();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
    /**
     * 璁＄畻sdcard涓婄殑鍓╀綑绌洪棿.
     *
     * @return the int
     */
    public static int freeSpaceOnSD() {
       StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
       double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / 1024*1024;
       return (int) sdFreeMB;
    }
	
    /**
     * 鏍规嵁鏂囦欢鐨勬渶鍚庝慨鏀规椂闂磋繘琛屾帓搴�.
     */
    public static class FileLastModifSort implements Comparator<File> {
        
        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

	
	/**
	 * 鍒犻櫎鎵�鏈夌紦瀛樻枃浠�.
	 *
	 * @return true, if successful
	 */
    public static boolean clearDownloadFile() {
       try {
		   File fileDirectory = new File(downloadRootDir);
		   deleteFile(fileDirectory);
	   } catch (Exception e) {
		   e.printStackTrace();
		   return false;
	   }
       return true;
    }
    
    /**
	 * 鍒犻櫎鏂囦欢.
	 *
	 * @return true, if successful
	 */
    public static boolean deleteFile(File file) {
    	
       try {
		   if(!isCanUseSD()){
				return false;
		   }
	       if (file == null) {
	            return true;
	       }
	       if(file.isDirectory()){
	    	   File[] files = file.listFiles();
	    	   for (int i = 0; i < files.length; i++) {
	    		   deleteFile(files[i]);
	           }
	       }else{
	    	   file.delete();
	       }
           
	   } catch (Exception e) {
		   e.printStackTrace();
		   return false;
	   }
       return true;
    }
    
    
    /**
     * 鎻忚堪锛氳鍙朅ssets鐩綍鐨勬枃浠跺唴瀹�.
     *
     * @param context the context
     * @param name the name
     * @param encoding the encoding
     * @return the string
     */
    public static String readAssetsByName(Context context,String name,String encoding){
    	String text = null;
    	InputStreamReader inputReader = null;
    	BufferedReader bufReader = null;
    	try {  
    		 inputReader = new InputStreamReader(context.getAssets().open(name));
    		 bufReader = new BufferedReader(inputReader);
    		 String line = null;
    		 StringBuffer buffer = new StringBuffer();
    		 while((line = bufReader.readLine()) != null){
    			 buffer.append(line);
    		 }
    		 text = new String(buffer.toString().getBytes(), encoding);
         } catch (Exception e) {  
        	 e.printStackTrace();
         } finally{
			try {
				if(bufReader!=null){
					bufReader.close();
				}
				if(inputReader!=null){
					inputReader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	return text;
    }
    
    /**
     * 鎻忚堪锛氳鍙朢aw鐩綍鐨勬枃浠跺唴瀹�.
     *
     * @param context the context
     * @param id the id
     * @param encoding the encoding
     * @return the string
     */
    public static String readRawByName(Context context,int id,String encoding){
    	String text = null;
    	InputStreamReader inputReader = null;
    	BufferedReader bufReader = null;
        try {
			inputReader = new InputStreamReader(context.getResources().openRawResource(id));
			bufReader = new BufferedReader(inputReader);
			String line = null;
			StringBuffer buffer = new StringBuffer();
			while((line = bufReader.readLine()) != null){
				 buffer.append(line);
			}
            text = new String(buffer.toString().getBytes(),encoding);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(bufReader!=null){
					bufReader.close();
				}
				if(inputReader!=null){
					inputReader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        return text;
    }


	/**
	 * Gets the download root dir.
	 *
	 * @param context the context
	 * @return the download root dir
	 */
	public static String getDownloadRootDir(Context context) {
		if(downloadRootDir == null){
			initFileDir(context);
		}
		return downloadRootDir;
	}


	/**
	 * Gets the image download dir.
	 *
	 * @param context the context
	 * @return the image download dir
	 */
	public static String getImageDownloadDir(Context context) {
		if(downloadRootDir == null){
			initFileDir(context);
		}
		return imageDownloadDir;
	}


	/**
	 * Gets the file download dir.
	 *
	 * @param context the context
	 * @return the file download dir
	 */
	public static String getFileDownloadDir(Context context) {
		if(downloadRootDir == null){
			initFileDir(context);
		}
		return fileDownloadDir;
	}


	/**
	 * Gets the cache download dir.
	 *
	 * @param context the context
	 * @return the cache download dir
	 */
	public static String getCacheDownloadDir(Context context) {
		if(downloadRootDir == null){
			initFileDir(context);
		}
		return cacheDownloadDir;
	}
	
	
	/**
	 * Gets the db download dir.
	 *
	 * @param context the context
	 * @return the db download dir
	 */
	public static String getDbDownloadDir(Context context) {
		if(downloadRootDir == null){
			initFileDir(context);
		}
		return dbDownloadDir;
	}


	/**
	 * Gets the free sd space needed to cache.
	 *
	 * @return the free sd space needed to cache
	 */
	public static int getFreeSdSpaceNeededToCache() {
		return freeSdSpaceNeededToCache;
	}


}
