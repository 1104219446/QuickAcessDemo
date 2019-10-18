package com.test.star.quickacessdemo.test2.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.test.star.quickacessdemo.R;
import com.test.star.quickacessdemo.Unit;
import com.test.star.quickacessdemo.bean.DownItem;
import com.test.star.quickacessdemo.bean.UpItem;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Describe：
 * Author:zhuokai.zeng
 * CreateTime:2019/10/15
 */
public class ShowView extends LinearLayout {

    private int col=4;//行
    private int row=4;//列
    private int mWidth;//宽
    private int mHeight;//高
    private int mItemWidth;
    private int mItemHeight;
    private int mPadingLeftAndRight;//左右间距
    private int mPadingTopAndBotton;//上下间距
    private int mPadingItem=10;
    private int mStartPosX;//开始绘画位置X坐标
    private int mStartPosY;//开始绘画位置y坐标
    //手指移动位置
    private int mMoveX;
    private int mMoveY;
    //绘画圆形位置
    private RectF mLightRect=new RectF();
    private RectF mItemRect=new RectF();
    private int mPosRow;//矩形位置行从0开始
    private int mPosCol;//列从0开始
    private MoveListener mMoveListener;
    private boolean mIsIn=false;
    private boolean mIsPress=false;

    private ArrayList<UpItem> mItems;

    private Paint mLightRectPaint;//亮的矩形画笔
    private Paint mItemTextPaint;//item的文字画笔
    private Paint mItemImagePaint;//item图像画笔


    private int mRectStorkeWidth=5;
    private int mItemTextBase=30;
    private int mItemTextLeftdis=110;
    private Toast mLog;

    //长按响应时间
    private long dragResponseMS=1000;
    private Handler mHandler=new Handler();
    private Vibrator mVibrator;
    private ImageView mDragImageView;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private int mStatusHeight;
    private WindowManager mWindowManager;
    private UpItem mStartDragItem;
    private int mStartDragitemPosInList;



    public ShowView(Context context) {
        super(context);
        init();
    }

    public ShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mVibrator=(Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager=(WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        initPaint();
        initData();

    }

    private void initData() {
        mItems= new ArrayList<>();
        for(int i=0;i<2;i++){
            for(int j=0;j<4;j++){
                mItems.add(new UpItem("Upper"+(i*4+j), R.mipmap.ic_launcher,j,i));
            }
        }

    }

    private void initPaint() {
        mLightRectPaint =new Paint();
        mLightRectPaint.setStyle(Paint.Style.STROKE);//只描边
        mLightRectPaint.setStrokeWidth(mRectStorkeWidth);
        mLightRectPaint.setColor(Color.BLUE);
        mItemImagePaint=new Paint();
        mItemTextPaint=new Paint();
        mItemTextPaint.setColor(Color.WHITE);
        mItemTextPaint.setTextSize(40f);
        mItemTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth=MeasureSpec.getSize(widthMeasureSpec);
        mHeight =MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth,mHeight);

        initGrid();
    }

    private void initGrid() {
        if(mWidth>mHeight){
            mPadingLeftAndRight=Math.abs(mWidth-mHeight)/2;
            mPadingTopAndBotton=0;
        }else{
            mPadingTopAndBotton=Math.abs(mWidth-mHeight)/2;
            mPadingLeftAndRight=0;
        }

        mItemHeight= (mHeight-mPadingTopAndBotton*2)/row;
        mItemWidth=(mWidth-mPadingLeftAndRight*2)/col;

        //开始绘画位置
        mStartPosX=mPadingLeftAndRight;
        mStartPosY=mPadingTopAndBotton;

        mStatusHeight= Unit.getStatusBarHeight(getContext());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mMoveX= (int) event.getRawX();
                mMoveY= (int) event.getRawY();
                if(getStartDragView()){
                    mHandler.postDelayed(mLongClickRunnalbe,dragResponseMS);
                    invalidate();

                }
                break;
            case MotionEvent.ACTION_MOVE:
                onDragItem((int)event.getX(),(int)event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                removeMirrorImageView();
                mHandler.removeCallbacks(mLongClickRunnalbe);

                break;
        }


        return super.onTouchEvent(event);
    }

    private void removeMirrorImageView() {
        if(mDragImageView!=null){
            mWindowManager.removeView(mDragImageView);
            mDragImageView=null;
        }
    }

    private boolean getStartDragView() {
        int cnt=0;
        for(UpItem item:mItems){
            cnt++;
            if(mPosCol==item.getCol()&&mPosRow==item.getRow()){
                mStartDragItem=item;
                mStartDragitemPosInList=cnt;
                return true;
            }
        }
        return false;
    }

    private Runnable mLongClickRunnalbe=new Runnable() {
        @Override
        public void run() {
            mVibrator.vibrate(50);
            mStartDragItem.setShow(false);
//            mStartDragItemView.setVisibility(View.INVISIBLE);
            //根据我们按下的点显示item镜像
            Bitmap dragBitmap=BitmapFactory.decodeResource(getResources(),mStartDragItem.getImageResId());
            createDragImage(dragBitmap, mMoveX, mMoveY);

        }
    };

    private void onDragItem(int moveX,int moveY){
        mWindowLayoutParams.x=moveX-mDragImageView.getWidth()/2;
        mWindowLayoutParams.y=moveY+mDragImageView.getHeight()/2;
        mWindowManager.updateViewLayout(mDragImageView,mWindowLayoutParams);//更新镜像位置
        swapItem(moveX,moveY);
    }

    private void swapItem(int x, int y) {
        if(x<mStartPosX){
            mPosCol=0;
        }else if(x>mWidth-mStartPosX){
            mPosCol=col-1;
        }
        else{
            mPosCol=(x-mStartPosX)%mItemWidth==0?
                    (x-mStartPosX)/mItemWidth :(x-mStartPosX)/mItemWidth+1;
        }
        if(y<mStartPosY){
            mPosRow=0;
        }else if(y>mHeight-mStartPosY){
            mPosRow=row-1;
        }
        else{
            mPosRow=(y-mStartPosY)/mItemHeight;
        }

        UpItem tmp = null;
        int cnt=0;
        for(UpItem item:mItems){
            cnt++;
            if(mPosCol==item.getCol()&&mPosRow==item.getRow()){
                tmp=item;
            }
        }
        if(tmp!=null){
            Collections.swap(mItems,cnt,mStartDragitemPosInList);
        }
    }


    private void createDragImage(Bitmap bitmap,int downX,int downY){
        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(bitmap);

        mWindowLayoutParams=new WindowManager.LayoutParams();
        mWindowLayoutParams.format=PixelFormat.TRANSLUCENT;
        mWindowLayoutParams.gravity=Gravity.TOP|Gravity.LEFT;
        mWindowLayoutParams.x=downX-bitmap.getWidth()/2;
        mWindowLayoutParams.y=downY-bitmap.getHeight()/2-mStatusHeight;
        mWindowLayoutParams.alpha = 0.5f; //透明度
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;

        mWindowManager.addView(mDragImageView, mWindowLayoutParams);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mIsPress&&mIsIn){
            drawLightRect(canvas);
        }
        drawAllItems(canvas);
        //画外边框item定位用
        canvas.drawRect(mStartPosX,mStartPosY,mStartPosX+mItemWidth*col
        ,mStartPosY+mItemHeight*row,mLightRectPaint);

        super.onDraw(canvas);
    }

    private void drawAllItems(Canvas canvas) {
        for(UpItem item:mItems){

            if(item.isShow()){
                mItemTextPaint.setAlpha(255);
                mItemImagePaint.setAlpha(255);
            }else{
                mItemImagePaint.setAlpha(0);
                mItemTextPaint.setAlpha(0);
            }
            int left=item.getCol()*mItemWidth+mStartPosX;
            int top=item.getRow()*mItemHeight+mStartPosY;
            canvas.drawText(item.getText(),left+mItemTextLeftdis
            ,top-mItemTextBase+mItemHeight,mItemTextPaint);
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher)
            ,left+mItemWidth/4,top+mItemHeight/5,mItemImagePaint);

        }
    }

    private void drawLightRect(Canvas canvas) {
        canvas.drawRoundRect(mLightRect,20f,20f, mLightRectPaint);
    }

    void autoJugeInView(int x, int y){
        if(y>mStartPosY+mItemHeight*row){
            mIsIn=false;
        }else{
            mIsIn=true;
        }
    }

    public void posToRect(){
        autoJugeInView(mMoveX,mMoveY);
        if(mMoveX<mStartPosX){
            mPosCol=0;
        }else if(mMoveX>mWidth-mStartPosX){
            mPosCol=col-1;
        }
        else{
            mPosCol=(mMoveX-mStartPosX)%mItemWidth==0?
                    (mMoveX-mStartPosX)/mItemWidth:(mMoveX-mStartPosX)/mItemWidth+1;
        }
        if(mMoveY<mStartPosY){
            mPosRow=0;
        }else if(mMoveY>mHeight-mStartPosY){
            mPosRow=row-1;
        }
        else{
            mPosRow=(mMoveY-mStartPosY)/mItemHeight;
        }
        //行列转换成绘制矩形Rect
        colAndRowChange(col,row,mLightRect);

    }


    private RectF colAndRowChange(int col, int row,RectF rectF) {
        if(mPosCol>=4){
            mPosCol=3;
        }
        if(mPosRow>=4){
            mPosRow=3;
        }
        //画的矩形向内缩进mPadingItem距离
        float left=mStartPosX+mPosCol*mItemWidth+mPadingItem;
        float top=mStartPosY+mPosRow*mItemHeight+mPadingItem;
        float right=left+mItemWidth-mPadingItem;
        float bottom=top+mItemHeight-mPadingItem;
        //Log.d("66", "posToRect: "+mPosCol);
        //Log.d("666", "posToRect: "+(mMoveY-mPadingTopAndBotton)/mItemHeight);
        if(rectF==null){
            rectF=new RectF(left,top,right,bottom);
        }else{
            rectF.left=left;
            rectF.right=right;
            rectF.top=top;
            rectF.bottom=bottom;
        }
        return rectF;
    }




    public void onMoveEnd(int x,int y,String string,int resId,int pos){
        if(x<mStartPosX){
            mPosCol=0;
        }else if(x>mWidth-mStartPosX){
            mPosCol=col-1;
        }
        else{
            mPosCol=(x-mStartPosX)%mItemWidth==0?
                    (x-mStartPosX)/mItemWidth :(x-mStartPosX)/mItemWidth+1;
        }
        if(y<mStartPosY){
            mPosRow=0;
        }else if(y>mHeight-mStartPosY){
            mPosRow=row-1;
        }
        else{
            mPosRow=(y-mStartPosY)/mItemHeight;
        }
        boolean isExist=false;
        for(UpItem item:mItems){
            if(mPosCol==item.getCol()&&mPosRow==item.getRow()){
                isExist=true;
                break;
            }
        }
        //添加item
        if(!isExist&&mIsIn&&!mIsPress){
            mItems.add(new UpItem(string,resId,mPosCol,mPosRow));
            mMoveListener.addSuccess(pos);
            invalidate();
        }else{
            if(isExist){
                if(mLog==null){
                    mLog=Toast.makeText(getContext(),"添加区域已经存在item无法添加",Toast.LENGTH_SHORT);
                    mLog.show();
                }else{
                    mLog.show();
                }
            }
            mMoveListener.addFail();
        }

    }

    public void setmMoveListener(MoveListener mMoveListener) {
        this.mMoveListener = mMoveListener;
    }

    public interface MoveListener{
        void addSuccess(int pos);
        void addFail();
    }

    public void setmIsPress(boolean mIsPress) {
        this.mIsPress = mIsPress;
    }

    public void setmMoveX(int mMoveX) {
        this.mMoveX = mMoveX;
    }

    public void setmMoveY(int mMoveY) {
        this.mMoveY = mMoveY;
    }

}
