package com.test.star.quickacessdemo.test2;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Describe：万能Viewholder
 * Author:zhuokai.zeng
 * CreateTime:2019/10/15
 */
public class CommonViewholder {
    private SparseArray<View>mViews;
    private int mPosition;
    private View mConvertView;


    public CommonViewholder(Context context, ViewGroup parent,int layoutId,int position){
        this.mPosition=position;
        this.mViews=new SparseArray<>();
        mConvertView=LayoutInflater.from(context).inflate(layoutId,parent,false);
        mConvertView.setTag(this);
    }

    public static CommonViewholder get(Context context,View convertView,ViewGroup parent,
                                       int layoutId,int position){
        if(convertView==null){
            return new CommonViewholder(context,parent,layoutId,position);
        }else{
            CommonViewholder viewholder= (CommonViewholder) convertView.getTag();
            viewholder.mPosition=position;
            return viewholder;
        }
    }

    public View getConvertView(){
        return mConvertView;
    }

    public <T extends View> T getView(int viewId){
        View view=mViews.get(viewId);
        if(view==null){
            view=mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }
    //链式调用
    public CommonViewholder setText(int viewId,String text){
        TextView textView=getView(viewId);
        textView.setText(text);
        return this;
    }

    public CommonViewholder setImageRes(int viewId,int resId){
        ImageView iv=getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

}
