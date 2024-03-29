package com.test.star.quickacessdemo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * Describe：
 * Author:zhuokai.zeng
 * CreateTime:2019/8/23
 */
public class Unit {

    private static Matrix mMatrix;
    private static AnimatorSet mAnimatorSet;

    /**
     * 通过BitmapDrawable来获取Bitmap
     * @param mContext
     * @param fileName
     * @return
     */
    public static Bitmap getBitmapFromBitmapDrawable(Context mContext, String fileName) {
        BitmapDrawable bmpMeizi = null;
        try {
            bmpMeizi = new BitmapDrawable(mContext.getAssets().open(fileName));//"pic_meizi.jpg"
            Bitmap mBitmap = bmpMeizi.getBitmap();
            return mBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过资源ID获取Bitmap
     * @param res
     * @param resId
     * @return
     */
    public static Bitmap getBitmapFromResource(Resources res, int resId) {
        return BitmapFactory.decodeResource(res, resId);
    }

    /**
     * 通过文件路径来获取Bitmap
     * @param pathName
     * @return
     */
    public static Bitmap getBitmapFromFile(String pathName) {
        return BitmapFactory.decodeFile(pathName);
    }

    /**
     * 通过字节数组来获取Bitmap
     * @param b
     * @return
     */
    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 通过输入流InputStream来获取Bitmap
     * @param inputStream
     * @return
     */
    public static Bitmap getBitmapFromStream(InputStream inputStream) {
        return BitmapFactory.decodeStream(inputStream);
    }


    public static Bitmap scaleBitmap(float scaleX,float scaleY,Bitmap bitmap){
        if(mMatrix==null)mMatrix=new Matrix();
        mMatrix.setScale(scaleX, scaleY);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight()
                ,mMatrix,true);
    }


    public static void startTranstation(View view,float x1,float y1,float x2,float y2) {

        ObjectAnimator tansYAnimator=ObjectAnimator.ofFloat(view,"translationX",x1,x2);
        ObjectAnimator tansXAnimator=ObjectAnimator.ofFloat(view,"translationY",y1,y2);
        if(mAnimatorSet==null){
            synchronized (Unit.class){
                if(mAnimatorSet==null){
                    mAnimatorSet=new AnimatorSet();
                }
            }
        }
        mAnimatorSet.play(tansYAnimator).with(tansXAnimator);
        mAnimatorSet.setDuration(1000);
        mAnimatorSet.start();
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}