package com.test.star.quickacessdemo.bean;

import android.graphics.Bitmap;

import com.test.star.quickacessdemo.R;

/**
 * Describe：
 * Author:zhuokai.zeng
 * CreateTime:2019/10/15
 */
public class UpItem {
    private String text;
    private int imageResId;
    private int col;
    private int row;
    private boolean isShow=true;

    public UpItem(String string, int resId, int col, int row){
        text=string;
        imageResId=resId;
        this.col=col;
        this.row=row;
    }
    public UpItem(){}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
