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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

import com.bnrc.util.dct.FDCT;

// TODO: Auto-generated Javadoc

/**
 * 漏 2012 amsoft.cn 
 * 鍚嶇О锛欰bImageUtil.java 
 * 鎻忚堪锛氬浘鐗囧鐞嗙被.
 * 
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-01-17 涓嬪崍11:52:13
 */
public class AbImageUtil {

	/** 鍥剧墖澶勭悊锛氳鍓�. */
	public static final int CUTIMG = 0;

	/** 鍥剧墖澶勭悊锛氱缉鏀�. */
	public static final int SCALEIMG = 1;

	/** 鍥剧墖澶勭悊锛氫笉澶勭悊. */
	public static final int ORIGINALIMG = 2;

	/** 鍥剧墖鏈�澶у搴�. */
	public static final int MAX_WIDTH = 4096/2;

	/** 鍥剧墖鏈�澶ч珮搴�. */
	public static final int MAX_HEIGHT = 4096/2;

	/**
	 * 鐩存帴鑾峰彇浜掕仈缃戜笂鐨勫浘鐗�.
	 * 
	 * @param url
	 *            瑕佷笅杞芥枃浠剁殑缃戠粶鍦板潃
	 * @param type
	 *            鍥剧墖鐨勫鐞嗙被鍨嬶紙鍓垏鎴栬�呯缉鏀惧埌鎸囧畾澶у皬锛屽弬鑰傾bConstant绫伙級
	 * @param desiredWidth
	 *            鏂板浘鐗囩殑瀹�
	 * @param desiredHeight
	 *            鏂板浘鐗囩殑楂�
	 * @return Bitmap 鏂板浘鐗�
	 */
	public static Bitmap getBitmap(String url, int type,
			int desiredWidth, int desiredHeight) {
		Bitmap bm = null;
		URLConnection con = null;
		InputStream is = null;
		try {
			URL imageURL = new URL(url);
			con = imageURL.openConnection();
			con.setDoInput(true);
			con.connect();
			is = con.getInputStream();
			// 鑾峰彇璧勬簮鍥剧墖
			Bitmap wholeBm = BitmapFactory.decodeStream(is, null, null);
			if (type == CUTIMG) {
				bm = getCutBitmap(wholeBm, desiredWidth, desiredHeight);
			} else if (type == SCALEIMG) {
				bm = getScaleBitmap(wholeBm, desiredWidth, desiredHeight);
			} else {
				bm = wholeBm;
			}
		} catch (Exception e) {
			AbLogUtil.d(AbImageUtil.class, "" + e.getMessage());
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bm;
	}

	/**
	 * 鎻忚堪锛氳幏鍙栧師鍥�.
	 * 
	 * @param file
	 *            File瀵硅薄
	 * @return Bitmap 鍥剧墖
	 */
	public static Bitmap getBitmap(File file) {
		Bitmap resizeBmp = null;
		try {
			resizeBmp = BitmapFactory.decodeFile(file.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resizeBmp;
	}

	/**
	 * 鎻忚堪锛氱缉鏀惧浘鐗�.鍘嬬缉
	 * 
	 * @param file
	 *            File瀵硅薄
	 * @param desiredWidth
	 *            鏂板浘鐗囩殑瀹�
	 * @param desiredHeight
	 *            鏂板浘鐗囩殑楂�
	 * @return Bitmap 鏂板浘鐗�
	 */
	public static Bitmap getScaleBitmap(File file, int desiredWidth, int desiredHeight) {

		Bitmap resizeBmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 璁剧疆涓簍rue,decodeFile鍏堜笉鍒涘缓鍐呭瓨 鍙幏鍙栦竴浜涜В鐮佽竟鐣屼俊鎭嵆鍥剧墖澶у皬淇℃伅
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getPath(), opts);

		// 鑾峰彇鍥剧墖鐨勫師濮嬪搴﹂珮搴�
		int srcWidth = opts.outWidth;
		int srcHeight = opts.outHeight;
		int[] size = resizeToMaxSize(srcWidth, srcHeight, desiredWidth, desiredHeight);
		desiredWidth = size[0];
		desiredHeight = size[1];

		// 缂╂斁鐨勬瘮渚�
		float scale = getMinScale(srcWidth, srcHeight, desiredWidth, desiredHeight);
		int destWidth = srcWidth;
		int destHeight = srcHeight;
		if (scale != 0) {
			destWidth = (int) (srcWidth * scale);
			destHeight = (int) (srcHeight * scale);
		}

		// 榛樿涓篈RGB_8888.
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		// 浠ヤ笅涓や釜瀛楁闇�涓�璧蜂娇鐢細
		// 浜х敓鐨勪綅鍥惧皢寰楀埌鍍忕礌绌洪棿锛屽鏋滅郴缁焔c锛岄偅涔堝皢琚竻绌恒�傚綋鍍忕礌鍐嶆琚闂紝濡傛灉Bitmap宸茬粡decode锛岄偅涔堝皢琚嚜鍔ㄩ噸鏂拌В鐮�
		opts.inPurgeable = true;
		// 浣嶅浘鍙互鍏变韩涓�涓弬鑰冭緭鍏ユ暟鎹�(inputstream銆侀樀鍒楃瓑)
		opts.inInputShareable = true;

		// inSampleSize=2 琛ㄧず鍥剧墖瀹介珮閮戒负鍘熸潵鐨勪簩鍒嗕箣涓�锛屽嵆鍥剧墖涓哄師鏉ョ殑鍥涘垎涔嬩竴
		// 缂╂斁鐨勬瘮渚嬶紝SDK涓缓璁叾鍊兼槸2鐨勬寚鏁板�硷紝閫氳繃inSampleSize鏉ヨ繘琛岀缉鏀撅紝鍏跺�艰〃鏄庣缉鏀剧殑鍊嶆暟
		if (scale < 0.25) {
			// 缂╁皬鍒�4鍒嗕箣涓�
			opts.inSampleSize = 2;
		} else if (scale < 0.125) {
			// 缂╁皬鍒�8鍒嗕箣涓�
			opts.inSampleSize = 4;
		} else {
			opts.inSampleSize = 1;
		}

		// 璁剧疆澶у皬
		opts.outWidth = destWidth;
		opts.outHeight = destHeight;

		// 鍒涘缓鍐呭瓨
		opts.inJustDecodeBounds = false;
		// 浣垮浘鐗囦笉鎶栧姩
		opts.inDither = false;

		resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
		// 缂╁皬鎴栬�呮斁澶�
		resizeBmp = getScaleBitmap(resizeBmp, scale);
		return resizeBmp;
	}

	/**
	 * 鎻忚堪锛氱缉鏀惧浘鐗�,涓嶅帇缂╃殑缂╂斁.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param desiredWidth
	 *            鏂板浘鐗囩殑瀹�
	 * @param desiredHeight
	 *            鏂板浘鐗囩殑楂�
	 * @return Bitmap 鏂板浘鐗�
	 */
	public static Bitmap getScaleBitmap(Bitmap bitmap, int desiredWidth, int desiredHeight) {

		if (!checkBitmap(bitmap)) {
			return null;
		}
		Bitmap resizeBmp = null;

		// 鑾峰緱鍥剧墖鐨勫楂�
		int srcWidth = bitmap.getWidth();
		int srcHeight = bitmap.getHeight();

		int[] size = resizeToMaxSize(srcWidth, srcHeight, desiredWidth, desiredHeight);
		desiredWidth = size[0];
		desiredHeight = size[1];

		float scale = getMinScale(srcWidth, srcHeight, desiredWidth, desiredHeight);
		resizeBmp = getScaleBitmap(bitmap, scale);
		//瓒呭嚭鐨勮鎺�
		if (resizeBmp.getWidth() > desiredWidth || resizeBmp.getHeight() > desiredHeight) {
			resizeBmp  = getCutBitmap(resizeBmp,desiredWidth,desiredHeight);
		}
		return resizeBmp;
	}

	/**
	 * 鎻忚堪锛氭牴鎹瓑姣斾緥缂╂斁鍥剧墖.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param scale
	 *            姣斾緥
	 * @return Bitmap 鏂板浘鐗�
	 */
	public static Bitmap getScaleBitmap(Bitmap bitmap, float scale) {

		if (!checkBitmap(bitmap)) {
			return null;
		}

		if (scale == 1) {
			return bitmap;
		}

		Bitmap resizeBmp = null;
		try {
			// 鑾峰彇Bitmap璧勬簮鐨勫鍜岄珮
			int bmpW = bitmap.getWidth();
			int bmpH = bitmap.getHeight();
			
			// 娉ㄦ剰杩欎釜Matirx鏄痑ndroid.graphics搴曚笅鐨勯偅涓�
			Matrix matrix = new Matrix();
			// 璁剧疆缂╂斁绯绘暟锛屽垎鍒负鍘熸潵鐨�0.8鍜�0.8
			matrix.postScale(scale, scale);
			resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpW, bmpH, matrix, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resizeBmp != bitmap) {
				bitmap.recycle();
			}
		}
		return resizeBmp;
	}

	/**
	 * 鎻忚堪锛氳鍓浘鐗�.
	 * 
	 * @param file
	 *            File瀵硅薄
	 * @param desiredWidth
	 *            鏂板浘鐗囩殑瀹�
	 * @param desiredHeight
	 *            鏂板浘鐗囩殑楂�
	 * @return Bitmap 鏂板浘鐗�
	 */
	public static Bitmap getCutBitmap(File file, int desiredWidth, int desiredHeight) {

		Bitmap resizeBmp = null;

		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 璁剧疆涓簍rue,decodeFile鍏堜笉鍒涘缓鍐呭瓨 鍙幏鍙栦竴浜涜В鐮佽竟鐣屼俊鎭嵆鍥剧墖澶у皬淇℃伅
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getPath(), opts);

		// 鑾峰彇鍥剧墖鐨勫師濮嬪搴�
		int srcWidth = opts.outWidth;
		// 鑾峰彇鍥剧墖鍘熷楂樺害
		int srcHeight = opts.outHeight;

		int[] size = resizeToMaxSize(srcWidth, srcHeight, desiredWidth, desiredHeight);
		desiredWidth = size[0];
		desiredHeight = size[1];

		// 缂╂斁鐨勬瘮渚�
		float scale = getMinScale(srcWidth, srcHeight, desiredWidth, desiredHeight);
		int destWidth = srcWidth;
		int destHeight = srcHeight;
		if (scale != 1) {
			destWidth = (int) (srcWidth * scale);
			destHeight = (int) (srcHeight * scale);
		}

		// 榛樿涓篈RGB_8888.
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		// 浠ヤ笅涓や釜瀛楁闇�涓�璧蜂娇鐢細
		// 浜х敓鐨勪綅鍥惧皢寰楀埌鍍忕礌绌洪棿锛屽鏋滅郴缁焔c锛岄偅涔堝皢琚竻绌恒�傚綋鍍忕礌鍐嶆琚闂紝濡傛灉Bitmap宸茬粡decode锛岄偅涔堝皢琚嚜鍔ㄩ噸鏂拌В鐮�
		opts.inPurgeable = true;
		// 浣嶅浘鍙互鍏变韩涓�涓弬鑰冭緭鍏ユ暟鎹�(inputstream銆侀樀鍒楃瓑)
		opts.inInputShareable = true;
		// 缂╂斁鐨勬瘮渚嬶紝缂╂斁鏄緢闅炬寜鍑嗗鐨勬瘮渚嬭繘琛岀缉鏀剧殑锛岄�氳繃inSampleSize鏉ヨ繘琛岀缉鏀撅紝鍏跺�艰〃鏄庣缉鏀剧殑鍊嶆暟锛孲DK涓缓璁叾鍊兼槸2鐨勬寚鏁板��
		if (scale < 0.25) {
			// 缂╁皬鍒�4鍒嗕箣涓�
			opts.inSampleSize = 2;
		} else if (scale < 0.125) {
			// 缂╁皬
			opts.inSampleSize = 4;
		}else {
			opts.inSampleSize = 1;
		}
		// 璁剧疆澶у皬
		opts.outHeight = destHeight;
		opts.outWidth = destWidth;
		// 鍒涘缓鍐呭瓨
		opts.inJustDecodeBounds = false;
		// 浣垮浘鐗囦笉鎶栧姩
		opts.inDither = false;
		Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), opts);
		if (bitmap != null) {
			resizeBmp = getCutBitmap(bitmap, desiredWidth, desiredHeight);
		}
		return resizeBmp;
	}

	/**
	 * 鎻忚堪锛氳鍓浘鐗�.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param desiredWidth
	 *            鏂板浘鐗囩殑瀹�
	 * @param desiredHeight
	 *            鏂板浘鐗囩殑楂�
	 * @return Bitmap 鏂板浘鐗�
	 */
	public static Bitmap getCutBitmap(Bitmap bitmap, int desiredWidth, int desiredHeight) {

		if (!checkBitmap(bitmap)) {
			return null;
		}
		
		if (!checkSize(desiredWidth, desiredHeight)) {
			return null;
		}

		Bitmap resizeBmp = null;

		try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();

			int offsetX = 0;
			int offsetY = 0;

			if (width > desiredWidth) {
				offsetX = (width - desiredWidth) / 2;
			} else {
				desiredWidth = width;
			}

			if (height > desiredHeight) {
				offsetY = (height - desiredHeight) / 2;
			} else {
				desiredHeight = height;
			}

			resizeBmp = Bitmap.createBitmap(bitmap, offsetX, offsetY, desiredWidth,desiredHeight);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resizeBmp != bitmap) {
				bitmap.recycle();
			}
		}
		return resizeBmp;
	}
	
	/**
	 * 鎻忚堪锛氳幏鍙栧浘鐗囧昂瀵�
	 * 
	 * @param file File瀵硅薄
	 * @return Bitmap 鏂板浘鐗�
	 */
	public static float[] getBitmapSize(File file) {
		float[] size = new float[2];
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 璁剧疆涓簍rue,decodeFile鍏堜笉鍒涘缓鍐呭瓨 鍙幏鍙栦竴浜涜В鐮佽竟鐣屼俊鎭嵆鍥剧墖澶у皬淇℃伅
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getPath(), opts);
		// 鑾峰彇鍥剧墖鐨勫師濮嬪搴﹂珮搴�
		size[0] = opts.outWidth;
		size[1] = opts.outHeight;
		return size;
	}

	private static float getMinScale(int srcWidth, int srcHeight, int desiredWidth,
			int desiredHeight) {
		// 缂╂斁鐨勬瘮渚�
		float scale = 0;
		// 璁＄畻缂╂斁姣斾緥锛屽楂樼殑鏈�灏忔瘮渚�
		float scaleWidth = (float) desiredWidth / srcWidth;
		float scaleHeight = (float) desiredHeight / srcHeight;
		if (scaleWidth > scaleHeight) {
			scale = scaleWidth;
		} else {
			scale = scaleHeight;
		}
		
		return scale;
	}

	private static int[] resizeToMaxSize(int srcWidth, int srcHeight,
			int desiredWidth, int desiredHeight) {
		int[] size = new int[2];
		if(desiredWidth <= 0){
			desiredWidth = srcWidth;
		}
		if(desiredHeight <= 0){
			desiredHeight = srcHeight;
		}
		if (desiredWidth > MAX_WIDTH) {
			// 閲嶆柊璁＄畻澶у皬
			desiredWidth = MAX_WIDTH;
			float scaleWidth = (float) desiredWidth / srcWidth;
			desiredHeight = (int) (desiredHeight * scaleWidth);
		}

		if (desiredHeight > MAX_HEIGHT) {
			// 閲嶆柊璁＄畻澶у皬
			desiredHeight = MAX_HEIGHT;
			float scaleHeight = (float) desiredHeight / srcHeight;
			desiredWidth = (int) (desiredWidth * scaleHeight);
		}
		size[0] = desiredWidth;
		size[1] = desiredHeight;
		return size;
	}

	private static boolean checkBitmap(Bitmap bitmap) {
		if (bitmap == null) {
			AbLogUtil.e(AbImageUtil.class, "鍘熷浘Bitmap涓虹┖浜�");
			return false;
		}

		if (bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
			AbLogUtil.e(AbImageUtil.class, "鍘熷浘Bitmap澶у皬涓�0");
			return false;
		}
		return true;
	}

	private static boolean checkSize(int desiredWidth, int desiredHeight) {
		if (desiredWidth <= 0 || desiredHeight <= 0) {
			AbLogUtil.e(AbImageUtil.class, "璇锋眰Bitmap鐨勫楂樺弬鏁板繀椤诲ぇ浜�0");
			return false;
		}
		return true;
	}

	/**
	 * Drawable杞珺itmap.
	 * 
	 * @param drawable
	 *            瑕佽浆鍖栫殑Drawable
	 * @return Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitmap瀵硅薄杞崲Drawable瀵硅薄.
	 * 
	 * @param bitmap
	 *            瑕佽浆鍖栫殑Bitmap瀵硅薄
	 * @return Drawable 杞寲瀹屾垚鐨凞rawable瀵硅薄
	 */
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		BitmapDrawable mBitmapDrawable = null;
		try {
			if (bitmap == null) {
				return null;
			}
			mBitmapDrawable = new BitmapDrawable(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mBitmapDrawable;
	}

	/**
	 * Bitmap瀵硅薄杞崲TransitionDrawable瀵硅薄.
	 * 
	 * @param bitmap
	 *            瑕佽浆鍖栫殑Bitmap瀵硅薄 imageView.setImageDrawable(td);
	 *            td.startTransition(200);
	 * @return Drawable 杞寲瀹屾垚鐨凞rawable瀵硅薄
	 */
	public static TransitionDrawable bitmapToTransitionDrawable(Bitmap bitmap) {
		TransitionDrawable mBitmapDrawable = null;
		try {
			if (bitmap == null) {
				return null;
			}
			mBitmapDrawable = new TransitionDrawable(new Drawable[] {
					new ColorDrawable(android.R.color.transparent),
					new BitmapDrawable(bitmap) });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mBitmapDrawable;
	}

	/**
	 * Drawable瀵硅薄杞崲TransitionDrawable瀵硅薄.
	 * 
	 * @param drawable
	 *            瑕佽浆鍖栫殑Drawable瀵硅薄 imageView.setImageDrawable(td);
	 *            td.startTransition(200);
	 * @return Drawable 杞寲瀹屾垚鐨凞rawable瀵硅薄
	 */
	public static TransitionDrawable drawableToTransitionDrawable(
			Drawable drawable) {
		TransitionDrawable mBitmapDrawable = null;
		try {
			if (drawable == null) {
				return null;
			}
			mBitmapDrawable = new TransitionDrawable(new Drawable[] {
					new ColorDrawable(android.R.color.transparent), drawable });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mBitmapDrawable;
	}

	/**
	 * 灏咮itmap杞崲涓篵yte[].
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param mCompressFormat
	 *            鍥剧墖鏍煎紡 Bitmap.CompressFormat.JPEG,CompressFormat.PNG
	 * @param needRecycle
	 *            鏄惁闇�瑕佸洖鏀�
	 * @return byte[] 鍥剧墖鐨刡yte[]
	 */
	public static byte[] bitmap2Bytes(Bitmap bitmap,
			Bitmap.CompressFormat mCompressFormat, final boolean needRecycle) {
		byte[] result = null;
		ByteArrayOutputStream output = null;
		try {
			output = new ByteArrayOutputStream();
			bitmap.compress(mCompressFormat, 100, output);
			result = output.toByteArray();
			if (needRecycle) {
				bitmap.recycle();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 鑾峰彇Bitmap澶у皬.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param mCompressFormat
	 *            鍥剧墖鏍煎紡 Bitmap.CompressFormat.JPEG,CompressFormat.PNG
	 * @return 鍥剧墖鐨勫ぇ灏�
	 */
	public static int getByteCount(Bitmap bitmap,
			Bitmap.CompressFormat mCompressFormat) {
		int size = 0;
		ByteArrayOutputStream output = null;
		try {
			output = new ByteArrayOutputStream();
			bitmap.compress(mCompressFormat, 100, output);
			byte[] result = output.toByteArray();
			size = result.length;
			result = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return size;
	}

	/**
	 * 鎻忚堪锛氬皢byte[]杞崲涓築itmap.
	 * 
	 * @param b
	 *            鍥剧墖鏍煎紡鐨刡yte[]鏁扮粍
	 * @return bitmap 寰楀埌鐨凚itmap
	 */
	public static Bitmap bytes2Bimap(byte[] b) {
		Bitmap bitmap = null;
		try {
			if (b.length != 0) {
				bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 灏咺mageView杞崲涓築itmap.
	 * 
	 * @param view
	 *            瑕佽浆鎹负bitmap鐨刅iew
	 * @return byte[] 鍥剧墖鐨刡yte[]
	 */
	public static Bitmap imageView2Bitmap(ImageView view) {
		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createBitmap(view.getDrawingCache());
			view.setDrawingCacheEnabled(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 灏哣iew杞崲涓篋rawable.闇�瑕佹渶涓婂眰甯冨眬涓篖inearlayout
	 * 
	 * @param view
	 *            瑕佽浆鎹负Drawable鐨刅iew
	 * @return BitmapDrawable Drawable
	 */
	public static Drawable view2Drawable(View view) {
		BitmapDrawable mBitmapDrawable = null;
		try {
			Bitmap newbmp = view2Bitmap(view);
			if (newbmp != null) {
				mBitmapDrawable = new BitmapDrawable(newbmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mBitmapDrawable;
	}

	/**
	 * 灏哣iew杞崲涓築itmap.闇�瑕佹渶涓婂眰甯冨眬涓篖inearlayout
	 * 
	 * @param view
	 *            瑕佽浆鎹负bitmap鐨刅iew
	 * @return byte[] 鍥剧墖鐨刡yte[]
	 */
	public static Bitmap view2Bitmap(View view) {
		Bitmap bitmap = null;
		try {
			if (view != null) {
				view.setDrawingCacheEnabled(true);
				view.measure(
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				view.layout(0, 0, view.getMeasuredWidth(),
						view.getMeasuredHeight());
				view.buildDrawingCache();
				bitmap = view.getDrawingCache();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 灏哣iew杞崲涓篵yte[].
	 * 
	 * @param view
	 *            瑕佽浆鎹负byte[]鐨刅iew
	 * @param compressFormat
	 *            the compress format
	 * @return byte[] View鍥剧墖鐨刡yte[]
	 */
	public static byte[] view2Bytes(View view,
			Bitmap.CompressFormat compressFormat) {
		byte[] b = null;
		try {
			Bitmap bitmap = AbImageUtil.view2Bitmap(view);
			b = AbImageUtil.bitmap2Bytes(bitmap, compressFormat, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 鎻忚堪锛氭棆杞珺itmap涓轰竴瀹氱殑瑙掑害.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param degrees
	 *            the degrees
	 * @return the bitmap
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
		Bitmap mBitmap = null;
		try {
			Matrix m = new Matrix();
			m.setRotate(degrees % 360);
			mBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mBitmap;
	}

	/**
	 * 鎻忚堪锛氭棆杞珺itmap涓轰竴瀹氱殑瑙掑害骞跺洓鍛ㄦ殫鍖栧鐞�.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param degrees
	 *            the degrees
	 * @return the bitmap
	 */
	public static Bitmap rotateBitmapTranslate(Bitmap bitmap, float degrees) {
		Bitmap mBitmap = null;
		int width;
		int height;
		try {
			Matrix matrix = new Matrix();
			if ((degrees / 90) % 2 != 0) {
				width = bitmap.getWidth();
				height = bitmap.getHeight();
			} else {
				width = bitmap.getHeight();
				height = bitmap.getWidth();
			}
			int cx = width / 2;
			int cy = height / 2;
			matrix.preTranslate(-cx, -cy);
			matrix.postRotate(degrees);
			matrix.postTranslate(cx, cy);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mBitmap;
	}

	/**
	 * 杞崲鍥剧墖杞崲鎴愬渾褰�.
	 * 
	 * @param bitmap
	 *            浼犲叆Bitmap瀵硅薄
	 * @return the bitmap
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * 杞崲鍥剧墖杞崲鎴愰暅闈㈡晥鏋滅殑鍥剧墖.
	 * 
	 * @param bitmap
	 *            浼犲叆Bitmap瀵硅薄
	 * @return the bitmap
	 */
	public static Bitmap toReflectionBitmap(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}

		try {
			int reflectionGap = 1;
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();

			// This will not scale but will flip on the Y axis
			Matrix matrix = new Matrix();
			matrix.preScale(1, -1);

			// Create a Bitmap with the flip matrix applied to it.
			// We only want the bottom half of the image
			Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
					width, height / 2, matrix, false);

			// Create a new bitmap with same width but taller to fit
			// reflection
			Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
					(height + height / 2), Config.ARGB_8888);

			// Create a new Canvas with the bitmap that's big enough for
			// the image plus gap plus reflection
			Canvas canvas = new Canvas(bitmapWithReflection);
			// Draw in the original image
			canvas.drawBitmap(bitmap, 0, 0, null);
			// Draw in the gap
			Paint deafaultPaint = new Paint();
			canvas.drawRect(0, height, width, height + reflectionGap,
					deafaultPaint);
			// Draw in the reflection
			canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
			// Create a shader that is a linear gradient that covers the
			// reflection
			Paint paint = new Paint();
			LinearGradient shader = new LinearGradient(0, bitmap.getHeight(),
					0, bitmapWithReflection.getHeight() + reflectionGap,
					0x70ffffff, 0x00ffffff, TileMode.CLAMP);
			// Set the paint to use this shader (linear gradient)
			paint.setShader(shader);
			// Set the Transfer mode to be porter duff and destination in
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
			// Draw a rectangle using the paint with our linear gradient
			canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
					+ reflectionGap, paint);

			bitmap = bitmapWithReflection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 閲婃斁Bitmap瀵硅薄.
	 * 
	 * @param bitmap
	 *            瑕侀噴鏀剧殑Bitmap
	 */
	public static void releaseBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			try {
				if (!bitmap.isRecycled()) {
					AbLogUtil.d(AbImageUtil.class,
							"Bitmap閲婃斁" + bitmap.toString());
					bitmap.recycle();
				}
			} catch (Exception e) {
			}
			bitmap = null;
		}
	}

	/**
	 * 閲婃斁Bitmap鏁扮粍.
	 * 
	 * @param bitmaps
	 *            瑕侀噴鏀剧殑Bitmap鏁扮粍
	 */
	public static void releaseBitmapArray(Bitmap[] bitmaps) {
		if (bitmaps != null) {
			try {
				for (Bitmap bitmap : bitmaps) {
					if (bitmap != null && !bitmap.isRecycled()) {
						AbLogUtil.d(AbImageUtil.class,
								"Bitmap閲婃斁" + bitmap.toString());
						bitmap.recycle();
					}
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 鎻忚堪锛氱畝鍗曠殑鍥惧儚鐨勭壒寰佸�硷紝鐢ㄤ簬缂╃暐鍥炬壘鍘熷浘姣旇緝濂�.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @return the hash code
	 */
	public static String getHashCode(Bitmap bitmap) {
		// 绗竴姝ワ紝缂╁皬灏哄銆�
		// 灏嗗浘鐗囩缉灏忓埌8x8鐨勫昂瀵革紝鎬诲叡64涓儚绱犮�傝繖涓�姝ョ殑浣滅敤鏄幓闄ゅ浘鐗囩殑缁嗚妭锛�
		// 鍙繚鐣欑粨鏋勩�佹槑鏆楃瓑鍩烘湰淇℃伅锛屾憭寮冧笉鍚屽昂瀵搞�佹瘮渚嬪甫鏉ョ殑鍥剧墖宸紓銆�

		Bitmap temp = Bitmap.createScaledBitmap(bitmap, 8, 8, false);

		int width = temp.getWidth();
		int height = temp.getHeight();
		Log.i("th", "灏嗗浘鐗囩缉灏忓埌8x8鐨勫昂瀵�:" + width + "*" + height);

		// 绗簩姝ワ紝绗簩姝ワ紝绠�鍖栬壊褰┿��
		// 灏嗙缉灏忓悗鐨勫浘鐗囷紝杞负64绾х伆搴︺�備篃灏辨槸璇达紝鎵�鏈夊儚绱犵偣鎬诲叡鍙湁64绉嶉鑹层��
		int[] pixels = new int[width * height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				pixels[i * height + j] = rgbToGray(temp.getPixel(i, j));
			}
		}

		releaseBitmap(temp);

		// 绗笁姝ワ紝璁＄畻骞冲潎鍊笺��
		// 璁＄畻鎵�鏈�64涓儚绱犵殑鐏板害骞冲潎鍊笺��
		int avgPixel = AbMathUtil.average(pixels);

		// 绗洓姝ワ紝姣旇緝鍍忕礌鐨勭伆搴︺��
		// 灏嗘瘡涓儚绱犵殑鐏板害锛屼笌骞冲潎鍊艰繘琛屾瘮杈冦�傚ぇ浜庢垨绛変簬骞冲潎鍊硷紝璁颁负1锛涘皬浜庡钩鍧囧�硷紝璁颁负0銆�
		int[] comps = new int[width * height];
		for (int i = 0; i < comps.length; i++) {
			if (pixels[i] >= avgPixel) {
				comps[i] = 1;
			} else {
				comps[i] = 0;
			}
		}

		// 绗簲姝ワ紝璁＄畻鍝堝笇鍊笺��
		// 灏嗕笂涓�姝ョ殑姣旇緝缁撴灉锛岀粍鍚堝湪涓�璧凤紝灏辨瀯鎴愪簡涓�涓�64浣嶇殑鏁存暟锛�
		// 杩欏氨鏄繖寮犲浘鐗囩殑鎸囩汗銆�
		StringBuffer hashCode = new StringBuffer();
		for (int i = 0; i < comps.length; i += 4) {
			int result = comps[i] * (int) Math.pow(2, 3) + comps[i + 1]
					* (int) Math.pow(2, 2) + comps[i + 2]
					* (int) Math.pow(2, 1) + comps[i + 2];
			hashCode.append(AbMathUtil.binaryToHex(result));
		}
		String sourceHashCode = hashCode.toString();
		// 寰楀埌鎸囩汗浠ュ悗锛屽氨鍙互瀵规瘮涓嶅悓鐨勫浘鐗囷紝鐪嬬湅64浣嶄腑鏈夊灏戜綅鏄笉涓�鏍风殑銆�
		// 鍦ㄧ悊璁轰笂锛岃繖绛夊悓浜庤绠�"姹夋槑璺濈"锛圚amming distance锛夈��
		// 濡傛灉涓嶇浉鍚岀殑鏁版嵁浣嶄笉瓒呰繃5锛屽氨璇存槑涓ゅ紶鍥剧墖寰堢浉浼硷紱濡傛灉澶т簬10锛屽氨璇存槑杩欐槸涓ゅ紶涓嶅悓鐨勫浘鐗囥��
		return sourceHashCode;
	}

	/**
	 * 鎻忚堪锛氬浘鍍忕殑鐗瑰緛鍊间綑寮︾浉浼煎害.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @return the DCT hash code
	 */
	public static String getDCTHashCode(Bitmap bitmap) {

		// 灏嗗浘鐗囩缉灏忓埌32x32鐨勫昂瀵�
		Bitmap temp = Bitmap.createScaledBitmap(bitmap, 32, 32, false);

		int width = temp.getWidth();
		int height = temp.getHeight();
		Log.i("th", "灏嗗浘鐗囩缉灏忓埌32x32鐨勫昂瀵�:" + width + "*" + height);

		// 绠�鍖栬壊褰┿��
		int[] pixels = new int[width * height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				pixels[i * height + j] = rgbToGray(temp.getPixel(i, j));
			}
		}

		releaseBitmap(temp);

		int[][] pxMatrix = AbMathUtil.arrayToMatrix(pixels, width, height);
		double[][] doublePxMatrix = AbMathUtil.intToDoubleMatrix(pxMatrix);

		// 璁＄畻DCT,宸茬粡鍙樻垚8*8浜�
		double[][] dtc = FDCT.fDctTransform(doublePxMatrix);

		// 璁＄畻骞冲潎鍊笺��
		double[] dctResult = AbMathUtil.matrixToArray(dtc);
		int avgPixel = AbMathUtil.average(dctResult);

		// 姣旇緝鍍忕礌鐨勭伆搴︺��
		// 灏嗘瘡涓儚绱犵殑鐏板害锛屼笌骞冲潎鍊艰繘琛屾瘮杈冦�傚ぇ浜庢垨绛変簬骞冲潎鍊硷紝璁颁负1锛涘皬浜庡钩鍧囧�硷紝璁颁负0銆�
		int[] comps = new int[8 * 8];
		for (int i = 0; i < comps.length; i++) {
			if (dctResult[i] >= avgPixel) {
				comps[i] = 1;
			} else {
				comps[i] = 0;
			}
		}

		// 璁＄畻鍝堝笇鍊笺��
		// 灏嗕笂涓�姝ョ殑姣旇緝缁撴灉锛岀粍鍚堝湪涓�璧凤紝灏辨瀯鎴愪簡涓�涓�64浣嶇殑鏁存暟锛�
		// 杩欏氨鏄繖寮犲浘鐗囩殑鎸囩汗銆�
		StringBuffer hashCode = new StringBuffer();
		for (int i = 0; i < comps.length; i += 4) {
			int result = comps[i] * (int) Math.pow(2, 3) + comps[i + 1]
					* (int) Math.pow(2, 2) + comps[i + 2]
					* (int) Math.pow(2, 1) + comps[i + 2];
			hashCode.append(AbMathUtil.binaryToHex(result));
		}
		String sourceHashCode = hashCode.toString();
		// 寰楀埌鎸囩汗浠ュ悗锛屽氨鍙互瀵规瘮涓嶅悓鐨勫浘鐗囷紝鐪嬬湅64浣嶄腑鏈夊灏戜綅鏄笉涓�鏍风殑銆�
		// 鍦ㄧ悊璁轰笂锛岃繖绛夊悓浜庤绠�"姹夋槑璺濈"锛圚amming distance锛夈��
		// 濡傛灉涓嶇浉鍚岀殑鏁版嵁浣嶄笉瓒呰繃5锛屽氨璇存槑涓ゅ紶鍥剧墖寰堢浉浼硷紱濡傛灉澶т簬10锛屽氨璇存槑杩欐槸涓ゅ紶涓嶅悓鐨勫浘鐗囥��
		return sourceHashCode;
	}

	/**
	 * 鎻忚堪锛氬浘鍍忕殑鐗瑰緛鍊奸鑹插垎甯� 灏嗛鑹插垎4涓尯锛�0,1,2,3 鍖虹粍鍚堝叡64缁勶紝璁＄畻姣忎釜鍍忕礌鐐瑰睘浜庡摢涓尯.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @return the color histogram
	 */
	public static int[] getColorHistogram(Bitmap bitmap) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 鍖洪鑹插垎甯�
		int[] areaColor = new int[64];

		// 鑾峰彇鑹插僵鏁扮粍銆�
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pixels = bitmap.getPixel(i, j);
				int alpha = (pixels >> 24) & 0xFF;
				int red = (pixels >> 16) & 0xFF;
				int green = (pixels >> 8) & 0xFF;
				int blue = (pixels) & 0xFF;
				int redArea = 0;
				int greenArea = 0;
				int blueArea = 0;
				// 0-63 64-127 128-191 192-255
				if (red >= 192) {
					redArea = 3;
				} else if (red >= 128) {
					redArea = 2;
				} else if (red >= 64) {
					redArea = 1;
				} else if (red >= 0) {
					redArea = 0;
				}

				if (green >= 192) {
					greenArea = 3;
				} else if (green >= 128) {
					greenArea = 2;
				} else if (green >= 64) {
					greenArea = 1;
				} else if (green >= 0) {
					greenArea = 0;
				}

				if (blue >= 192) {
					blueArea = 3;
				} else if (blue >= 128) {
					blueArea = 2;
				} else if (blue >= 64) {
					blueArea = 1;
				} else if (blue >= 0) {
					blueArea = 0;
				}
				int index = redArea * 16 + greenArea * 4 + blueArea;
				// 鍔犲叆
				areaColor[index] += 1;
			}
		}
		return areaColor;
	}

	/**
	 * 璁＄畻"姹夋槑璺濈"锛圚amming distance锛夈��
	 * 濡傛灉涓嶇浉鍚岀殑鏁版嵁浣嶄笉瓒呰繃5锛屽氨璇存槑涓ゅ紶鍥剧墖寰堢浉浼硷紱濡傛灉澶т簬10锛屽氨璇存槑杩欐槸涓ゅ紶涓嶅悓鐨勫浘鐗囥��.
	 * 
	 * @param sourceHashCode
	 *            婧恏ashCode
	 * @param hashCode
	 *            涓庝箣姣旇緝鐨刪ashCode
	 * @return the int
	 */
	public static int hammingDistance(String sourceHashCode, String hashCode) {
		int difference = 0;
		int len = sourceHashCode.length();
		for (int i = 0; i < len; i++) {
			if (sourceHashCode.charAt(i) != hashCode.charAt(i)) {
				difference++;
			}
		}
		return difference;
	}

	/**
	 * 鐏板害鍊艰绠�.
	 * 
	 * @param pixels
	 *            鍍忕礌
	 * @return int 鐏板害鍊�
	 */
	private static int rgbToGray(int pixels) {
		// int _alpha = (pixels >> 24) & 0xFF;
		int _red = (pixels >> 16) & 0xFF;
		int _green = (pixels >> 8) & 0xFF;
		int _blue = (pixels) & 0xFF;
		return (int) (0.3 * _red + 0.59 * _green + 0.11 * _blue);
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		// System.out.println(getHashCode(""));
	}

}
