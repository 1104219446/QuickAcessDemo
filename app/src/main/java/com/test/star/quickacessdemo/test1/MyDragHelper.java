package com.test.star.quickacessdemo.test1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.view.View;
import android.view.ViewGroup;

/**
 * Describe：
 * Author:zhuokai.zeng
 * CreateTime:2019/10/8
 */
public class MyDragHelper extends ViewDragHelper.Callback {

    private int mLeft;
    private int mTop;

    @Override
    public boolean tryCaptureView(@NonNull View view, int i) {
        //注意这里一定要返回true，否则后续的拖拽回调是不会生效的
        return true;
    }

    @Override
    public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
        return left;
    }

    @Override
    public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
        return top;
    }

    @Override
    public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
        super.onViewCaptured(capturedChild, activePointerId);
        mLeft=capturedChild.getLeft();
        mTop=capturedChild.getTop();
    }

    @Override
    public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
        super.onViewReleased(releasedChild, xvel, yvel);
        
    }
}
