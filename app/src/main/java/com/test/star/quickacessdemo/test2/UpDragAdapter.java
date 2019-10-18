package com.test.star.quickacessdemo.test2;

import android.content.Context;

import com.test.star.quickacessdemo.R;
import com.test.star.quickacessdemo.bean.UpItem;

import java.util.List;

/**
 * Describe：
 * Author:zhuokai.zeng
 * CreateTime:2019/10/15
 */
public class UpDragAdapter extends CommonAdapter<UpItem> {

    public UpDragAdapter(Context context, int layoutId, List<UpItem> data) {
        super(context, layoutId, data);
    }

    @Override
    public void setViewContent(CommonViewholder viewholder, UpItem data) {
        viewholder.setImageRes(R.id.up_item_image,data.getImageResId())
                .setText(R.id.up_item_text,data.getText());
    }
}
