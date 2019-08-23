package com.test.star.quickacessdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Describe：
 * Author:zhuokai.zeng
 * CreateTime:2019/8/23
 */
public class DragItem extends View {

    private int mWidth;
    private int mHeight;
    private int mDefaulW=200;
    private int mDefaulH=200;
    private Bitmap mMainImage;
    private String mMainText;
    private Paint mBitmapPaint;
    private Paint mMainTextPaint;
    private Paint mCirclePaint;

    private float imageWith;
    private float imageheight;
    private float scaleX;
    private float scaleY;
    private Bitmap scaleBmp;


    public DragItem(Context context) {
        super(context);
        ButterKnife.bind(this);
        initView(context);
    }

    public DragItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        ButterKnife.bind(this);
        initView(context);
    }

    public DragItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ButterKnife.bind(this);
        initView(context);
    }

    private void initView(Context context) {
        setBackgroundColor(Color.GREEN);
        mMainText="Test";
        mMainImage=Unit.getBitmapFromResource(getResources(),R.drawable.testbmp);
        initPaint();
        //mMainImage=Unit.ScaleBitmap()
    }

    private void initPaint() {
        mBitmapPaint=new Paint();
        mBitmapPaint.setAntiAlias(true);
        //mBitmapPaint.setAlpha(50);
        mMainTextPaint=new Paint();
        mMainTextPaint.setAntiAlias(true);
        mMainTextPaint.setTextAlign(Paint.Align.LEFT);
        mMainTextPaint.setTextSize(50f);
        mCirclePaint=new Paint();
        //圆角画笔
        mCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int wSize=MeasureSpec.getSize(widthMeasureSpec);
        int hSize=MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode==MeasureSpec.EXACTLY||widthMode==MeasureSpec.AT_MOST)mWidth=wSize;
        else mWidth=Math.max(mDefaulW,wSize);
        if(heightMode==MeasureSpec.EXACTLY||widthMode==MeasureSpec.AT_MOST)mHeight=hSize;
        else mHeight=Math.max(mDefaulH,hSize);
        setMeasuredDimension(mWidth,mHeight);

        imageWith=mWidth/3;
        imageheight=mHeight/3;
        scaleX=imageheight/mMainImage.getWidth();
        scaleY=imageheight/mMainImage.getHeight();
        scaleBmp=Unit.scaleBitmap(scaleX,scaleY,mMainImage);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawImage(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        canvas.drawText(mMainText,mWidth/2-mMainTextPaint.measureText(mMainText)/2,
                mHeight*6/8,mMainTextPaint);
    }

    private void drawImage(Canvas canvas) {
        canvas.drawBitmap(scaleBmp,mWidth/2-scaleBmp.getWidth()/2,mHeight/6,mBitmapPaint);
    }
}
