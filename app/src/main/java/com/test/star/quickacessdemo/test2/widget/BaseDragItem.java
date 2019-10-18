package com.test.star.quickacessdemo.test2.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.star.quickacessdemo.R;

/**
 * Describe：
 * Author:zhuokai.zeng
 * CreateTime:2019/10/15
 */
public class BaseDragItem extends RelativeLayout {

    private Context context;
    private ImageView imageView;
    private TextView textView;
    private int mWidth;
    private int mHeight;


    public BaseDragItem(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public BaseDragItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    public BaseDragItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }

    private void init() {
        inflate(context, R.layout.drag_up_item,this);
        imageView=findViewById(R.id.up_item_image);
        textView=findViewById(R.id.up_item_text);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int widthmode=MeasureSpec.getMode(widthMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int heightmode=MeasureSpec.getMode(heightMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        if(height<width){
            mWidth=height;
            mHeight=height;
        }else{
            mHeight=width;
            mWidth=width;
        }
        setMeasuredDimension(mWidth,mHeight);

        changeSize();

    }

    private void changeSize() {

    }
}
