package com.test.star.quickacessdemo.test2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.test.star.quickacessdemo.bean.DownItem;

import static com.test.star.quickacessdemo.Unit.getStatusBarHeight;

/**
 * Describe：
 * Author:zhuokai.zeng
 * CreateTime:2019/10/8
 */
public class DragGridView extends GridView {
    //长按响应时间
    private long dragResponseMS=1000;

    private boolean isDrag=false;

    private int mDownX;
    private int mDownY;
    private int moveX;
    private int moveY;
    //正在拖拽item位置
    private int mDragPosition;
    //开始拖拽Item对应View
    private View mStartDragItemView=null;
    //镜像View
    private ImageView mDragImageView;
    //震动器
    private Vibrator mVibrator;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private Bitmap mDragBitmap;

    private int mPoint2itemTop;
    private int mPoint2itemLeft;

    private int mOffset2Top;
    private int mOffset2Left;

    private int mStatusHeight;

    private int mDownScrollBorder;
    private int mUpScrollBorder;

    private static final int speed=20;
    private OnChangeListener onChangeListener;




    public DragGridView(Context context) {
        super(context);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mStatusHeight =  getStatusBarHeight(context);//获取状态栏的高度
    }

    public DragGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mStatusHeight =  getStatusBarHeight(context);//获取状态栏的高度
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mStatusHeight =  getStatusBarHeight(context);//获取状态栏的高度
    }


    private Handler mHandler=new Handler();

    private Runnable mLongClickRunnalbe=new Runnable() {
        @Override
        public void run() {
            isDrag=true;
            mVibrator.vibrate(50);
            mStartDragItemView.setVisibility(View.INVISIBLE);
            //根据我们按下的点显示item镜像
            createDragImage(mDragBitmap, mDownX, mDownY);
        }
    };

    /**
     * 设置回调接口
     * @param onChanageListener
     */
    public void setOnChangeListener(OnChangeListener onChanageListener){
        this.onChangeListener = onChanageListener;
    }

    public void setDragResponseMS(long dragResponseMS){
        this.dragResponseMS=dragResponseMS;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX=(int)ev.getX();
                mDownY=(int)ev.getY();
                //获取当前item位置
                mDragPosition=pointToPosition(mDownX,mDownY);
                if(mDragPosition==AdapterView.INVALID_POSITION){
                    return super.dispatchTouchEvent(ev);
                }

                onChangeListener.onDown(true);

                mHandler.postDelayed(mLongClickRunnalbe,dragResponseMS);
                mStartDragItemView=getChildAt(mDragPosition-getFirstVisiblePosition());
                mPoint2itemTop=mDownY-mStartDragItemView.getTop();
                mPoint2itemLeft=mDownX-mStartDragItemView.getLeft();
                mOffset2Top= (int) (ev.getRawY()-mDownY);
                mOffset2Left= (int) (ev.getRawX()-mDownX);
                mDownScrollBorder=getHeight()/4;
                mUpScrollBorder=getHeight()*3/4;
                //获取Image图像
                mStartDragItemView.setDrawingCacheEnabled(true);
                mDragBitmap=Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
                mStartDragItemView.destroyDrawingCache();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX= (int) ev.getX();
                int moveY= (int) ev.getY();
                if(!isTouchInItem(mStartDragItemView,moveX,moveY)){
                    mHandler.removeCallbacks(mLongClickRunnalbe);
                }
                break;
            case MotionEvent.ACTION_UP:
                mHandler.removeCallbacks(mLongClickRunnalbe);
                mHandler.removeCallbacks(mScrollRunnable);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
//判断手指在item上面
    private boolean isTouchInItem(View dragView, int x, int y) {
        if(dragView==null){
            return false;
        }
        int leftOffset=dragView.getLeft();
        int topOffset=dragView.getTop();
        if(x<leftOffset||x>leftOffset+dragView.getWidth()){
            return false;
        }
        if(y<topOffset||y>topOffset+dragView.getHeight()){
            return false;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isDrag&&mDragImageView!=null){
            switch (ev.getAction()){
                case MotionEvent.ACTION_MOVE:
                    moveX= (int) ev.getX();
                    moveY= (int) ev.getY();
                    onDragItem(moveX,moveY);
                    break;
                case MotionEvent.ACTION_UP:
                    onStopDrag();
                    isDrag=false;
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    private void createDragImage(Bitmap bitmap,int downX,int downY){
        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(bitmap);

        mWindowLayoutParams=new WindowManager.LayoutParams();
        mWindowLayoutParams.format=PixelFormat.TRANSLUCENT;
        mWindowLayoutParams.gravity=Gravity.TOP|Gravity.LEFT;
        mWindowLayoutParams.x=downX-bitmap.getWidth()/2+mOffset2Left;
        mWindowLayoutParams.y=downY-bitmap.getHeight()/2+mOffset2Top-mStatusHeight;
        mWindowLayoutParams.alpha = 0.5f; //透明度
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;

        mWindowManager.addView(mDragImageView, mWindowLayoutParams);
    }


    private void onDragItem(int moveX,int moveY){
        mWindowLayoutParams.x=moveX+mOffset2Left-mDragImageView.getWidth()/2;
        mWindowLayoutParams.y=moveY+mOffset2Top-mStatusHeight-mDragImageView.getHeight()/2;
        //传递手指移动距离参数
        onChangeListener.onMove(mWindowLayoutParams.x,mWindowLayoutParams.y+mStatusHeight/2);
        mWindowManager.updateViewLayout(mDragImageView,mWindowLayoutParams);//更新镜像位置
        onSwapItem(moveX,moveY);
        mHandler.post(mScrollRunnable);
    }

    private Runnable mScrollRunnable=new Runnable() {
        @Override
        public void run() {
            int scrollY=0;
            if(moveY>mUpScrollBorder){
                scrollY=speed;
                mHandler.postDelayed(mScrollRunnable,25);
            }else if(moveY<mDownScrollBorder){
                scrollY-=speed;
                mHandler.postDelayed(mScrollRunnable,25);
            }else{
                scrollY=0;
                mHandler.removeCallbacks(mScrollRunnable);
            }
            onSwapItem(moveX,moveY);
            smoothScrollBy(scrollY,10);
        }
    };

    private void onSwapItem(int moveX,int moveY) {
        int tempPosition = pointToPosition(moveX, moveY);
        if (tempPosition != mDragPosition && tempPosition != AdapterView.INVALID_POSITION) {
            if (onChangeListener != null) {
                onChangeListener.onChange(mDragPosition, tempPosition);
            }

            getChildAt(tempPosition - getFirstVisiblePosition()).setVisibility(View.INVISIBLE);//拖动到了新的item,新的item隐藏掉
            getChildAt(mDragPosition - getFirstVisiblePosition()).setVisibility(View.VISIBLE);//之前的item显示出来

            mDragPosition = tempPosition;
        }
    }

    public void addFail(){
        View view=getChildAt(mDragPosition-getFirstVisiblePosition());
        if(view!=null){
            view.setVisibility(View.VISIBLE);
        }
    }

    private void onStopDrag(){
        //拖动结束
        onChangeListener.onMoveEnd(mWindowLayoutParams.x,mWindowLayoutParams.y
                , (DownItem) getAdapter().getItem(mDragPosition),mDragPosition);
        View view=getChildAt(mDragPosition-getFirstVisiblePosition());
        if(view!=null){
            view.setVisibility(View.VISIBLE);
        }
        //((DragAdapter)this.getAdapter()).setItemHide(-1);
        removeDragImage();
        onChangeListener.onDown(false);
    }

    private void removeDragImage(){
        if(mDragImageView!=null){
            mWindowManager.removeView(mDragImageView);
            mDragImageView=null;
        }
    }





    public void removeAt(int pos) {
        DownDragAdapter adapter=((DownDragAdapter)getAdapter());
        adapter.removeItem(pos);
        adapter.notifyDataSetInvalidated();
    }

    public interface OnChangeListener{
        //交换数据用
        void onChange(int from,int to);

        void onMove(int x,int y);

        void onMoveEnd(int x, int y, DownItem item,int pos);

        void onDown(boolean isDown);

    }




}
