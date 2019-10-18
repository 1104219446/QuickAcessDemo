package com.test.star.quickacessdemo.test2;

import android.content.Context;
import android.util.Log;

import com.test.star.quickacessdemo.R;
import com.test.star.quickacessdemo.bean.DownItem;

import java.util.List;

/**
 * Describe：
 * Author:zhuokai.zeng
 * CreateTime:2019/10/15
 */
public class DownDragAdapter extends CommonAdapter<DownItem> {


    public DownDragAdapter(Context context, int layoutId, List<DownItem> data) {
        super(context, layoutId, data);
    }

    @Override
    public void setViewContent(CommonViewholder viewholder, DownItem data) {
        viewholder.setText(R.id.down_item_text,data.getText());
    }

    public void addItem(int pos,DownItem item){
        mData.add(pos,item);
    }

    public void removeItem(int pos){
        mData.remove(pos);
//        Log.d("123123", "removeItem: "+mData.size());
    }

}
