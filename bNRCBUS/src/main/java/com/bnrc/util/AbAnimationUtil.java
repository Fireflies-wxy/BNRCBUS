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


import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

// TODO: Auto-generated Javadoc
/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bAnimationUtil.java 
 * 鎻忚堪锛氬姩鐢诲伐鍏风被.
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2011-11-10 涓嬪崍11:52:13
 */
public class AbAnimationUtil {
	
	/** 瀹氫箟鍔ㄧ敾鐨勬椂闂�. */
	public final static long aniDurationMillis = 1L;

	/**
	 * 鐢ㄦ潵鏀瑰彉褰撳墠閫変腑鍖哄煙鐨勬斁澶у姩鐢绘晥鏋�
	 * 浠�1.0f鏀惧ぇ1.2f鍊嶆暟
	 *
	 * @param view the view
	 * @param scale the scale
	 */
	public static void largerView(View view, float scale) {
		if (view == null)
			return;

		// 缃簬鎵�鏈塿iew鏈�涓婂眰
		view.bringToFront();
		int width = view.getWidth();
		float animationSize = 1 + scale / width;
		scaleView(view, animationSize);
	}

	/**
	 * 鐢ㄦ潵杩樺師褰撳墠閫変腑鍖哄煙鐨勮繕鍘熷姩鐢绘晥鏋�.
	 *
	 * @param view the view
	 * @param scale the scale
	 */
	public static void restoreLargerView(View view, float scale) {
		if (view == null)
			return;
		int width = view.getWidth();
		float toSize = 1 + scale / width;
		// 浠�1.2f缂╁皬1.0f鍊嶆暟
		scaleView(view, -1 * toSize);
	}

	/**
	 * 缂╂斁View鐨勬樉绀�.
	 *
	 * @param view 闇�瑕佹敼鍙樼殑View
	 * @param toSize 缂╂斁鐨勫ぇ灏忥紝鍏朵腑姝ｅ�间唬琛ㄦ斁澶э紝璐熷�间唬琛ㄧ缉灏忥紝鏁板�间唬琛ㄧ缉鏀剧殑鍊嶆暟
	 */
	private static void scaleView(final View view, float toSize) {
		ScaleAnimation scale = null;
		if (toSize == 0) {
			return;
		} else if (toSize > 0) {
			scale = new ScaleAnimation(1.0f, toSize, 1.0f, toSize,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		} else {
			scale = new ScaleAnimation(toSize * (-1), 1.0f, toSize * (-1),
					1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		}
		scale.setDuration(aniDurationMillis);
		scale.setInterpolator(new AccelerateDecelerateInterpolator());
		scale.setFillAfter(true);
		view.startAnimation(scale);
	}
	
	/**
	 * 璺冲姩-璺宠捣鍔ㄧ敾.
	 *
	 * @param view the view
	 * @param offsetY the offset y
	 */
    private void playJumpAnimation(final View view,final float offsetY) {
        float originalY = 0;
        float finalY = - offsetY;
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new TranslateAnimation(0, 0, originalY,finalY));
        animationSet.setDuration(300);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.setFillAfter(true);

        animationSet.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                playLandAnimation(view,offsetY);
            }
        });

        view.startAnimation(animationSet);
    }

    /**
     * 璺冲姩-钀戒笅鍔ㄧ敾.
     *
     * @param view the view
     * @param offsetY the offset y
     */
    private void playLandAnimation(final View view,final float offsetY) {
        float originalY =  - offsetY;
        float finalY = 0;
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new TranslateAnimation(0, 0, originalY,finalY));
        animationSet.setDuration(200);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setFillAfter(true);

        animationSet.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //涓ょ鍚庡啀璋�
                view.postDelayed(new Runnable(){
                    
                    @Override
                    public void run(){
                        playJumpAnimation(view,offsetY);
                    }
                }, 2000);
            }
        });

        view.startAnimation(animationSet);
    }
    
    /**
     * 鏃嬭浆鍔ㄧ敾
     * @param v
     * @param durationMillis
     * @param repeatCount  Animation.INFINITE
     * @param repeatMode  Animation.RESTART
     */
    public static void playRotateAnimation(View v,long durationMillis,int repeatCount,int repeatMode) {
    	
        //鍒涘缓AnimationSet瀵硅薄
        AnimationSet animationSet = new AnimationSet(true);
        //鍒涘缓RotateAnimation瀵硅薄
        RotateAnimation rotateAnimation = new RotateAnimation(0f,+360f, 
					Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        //璁剧疆鍔ㄧ敾鎸佺画
        rotateAnimation.setDuration(durationMillis);
        rotateAnimation.setRepeatCount(repeatCount);
        //Animation.RESTART
        rotateAnimation.setRepeatMode(repeatMode);
        //鍔ㄧ敾鎻掑叆鍣�
        rotateAnimation.setInterpolator(v.getContext(), android.R.anim.decelerate_interpolator);
        //娣诲姞鍒癆nimationSet
        animationSet.addAnimation(rotateAnimation);
        
        //寮�濮嬪姩鐢� 
        v.startAnimation(animationSet);
	}

}
