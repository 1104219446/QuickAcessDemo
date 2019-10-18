package com.test.star.quickacessdemo.test2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.star.quickacessdemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Describe：万能Adapter
 * Author:zhuokai.zeng
 * CreateTime:2019/10/14
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T>mData;
    private int mLayoutId;   //item布局文件
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewholder viewholder=CommonViewholder.get(context,convertView,parent,mLayoutId,position);

        setViewContent(viewholder,getItem(position));

        return viewholder.getConvertView();
    }

    public CommonAdapter(Context context,int layoutId,List<T> data){
        this.context=context;
        this.mLayoutId=layoutId;
        mData=data;
    }


    public abstract void setViewContent(CommonViewholder viewholder,T data);

}
