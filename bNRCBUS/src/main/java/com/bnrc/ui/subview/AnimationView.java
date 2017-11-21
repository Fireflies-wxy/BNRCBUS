package com.bnrc.ui.subview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by frank on 16/1/1.
 */
public class AnimationView extends View {

    int mStrokWidthOutter = 50;
    int mStrokWidthInner = 40;

    RectF mRectF;
    Paint mPaint;

    int mMeasureWidth;

    public AnimationView(Context context) {
        super(context);
        init();
    }

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mRectF = new RectF();
        mPaint = new Paint();

        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMeasureWidth = getMeasuredWidth();

        Log.d("", "RJZ mMeasureWidth " + mMeasureWidth);

        mStrokWidthOutter = mMeasureWidth/8;
        mStrokWidthInner = mMeasureWidth/5;

    }

    int mStartAngle1 = 30;
    int mSwipeAngle1 = 200;
    int mStartAngle2 = 40;
    int mSwipeAngle2 = 70;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawOutter(canvas);
        drawInner(canvas);

    }

    private void drawOutter(Canvas canvas) {

        float offset = mStrokWidthOutter/2;
        mRectF.set(offset, offset, mMeasureWidth - offset, mMeasureWidth - offset);
        Shader mShader = new SweepGradient(getMeasuredWidth()/2, getMeasuredHeight()/2, Color.rgb(231, 56, 94), Color.argb(0, 231, 56, 94));
        mPaint.setShader(mShader);
        mPaint.setStrokeWidth(mStrokWidthOutter);
        canvas.drawArc(mRectF, mStartAngle1, mSwipeAngle1, false, mPaint);

    }

    private void drawInner(Canvas canvas) {
        float offset = mStrokWidthOutter + mStrokWidthInner/2;
        mRectF.set(offset, offset, mMeasureWidth-offset, mMeasureWidth-offset);
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStrokeWidth(mStrokWidthInner);
        canvas.drawArc(mRectF, mStartAngle2, mSwipeAngle2, false, mPaint);
    }

    public void start() {
        mRedrawThread.start();
    }

    private Thread mRedrawThread = new Thread() {
        @Override
        public void run() {
            while (true) {

                mStartAngle1 += 5;
                if(mStartAngle1>=360) {
                    mStartAngle1 = 0;
                }

                mStartAngle2 -= 2;
                if(mStartAngle2<=0) {
                    mStartAngle2 = 360;
                }

                try {
                    Thread.sleep(10); // 慢点更新
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                postInvalidate();
            }
        }
    };

}
