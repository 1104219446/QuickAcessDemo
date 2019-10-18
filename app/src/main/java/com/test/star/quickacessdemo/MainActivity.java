package com.test.star.quickacessdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.test.star.quickacessdemo.bean.DownItem;
import com.test.star.quickacessdemo.bean.UpItem;
import com.test.star.quickacessdemo.test2.DownDragAdapter;
import com.test.star.quickacessdemo.test2.DragGridView;
import com.test.star.quickacessdemo.test2.UpDragAdapter;
import com.test.star.quickacessdemo.test2.widget.ShowView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<DownItem> mDownSource = new ArrayList<>();
//    private List<UpItem> mUpSource=new ArrayList();

    private DragGridView mDownDragView;
//    private DragGridView mUpDragView;

    private DownDragAdapter mDownAdapter;
//    private UpDragAdapter mUpAdapter;

    private ShowView mShowView;

    private Button mCancelBtn;      //取消
    private Button mCompleteBtn;    //自定义完成

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        initView();     //初始化视图

        initData();     //初始化数据

        initAdapter();  //初始化适配器

        initListener(); //初始化监听

    }

    private void initView() {
        mDownDragView = findViewById(R.id.dragGridView);
//        mUpDragView =findViewById(R.id.upGridView);
        mShowView=findViewById(R.id.up_drag_view);
        mCompleteBtn=findViewById(R.id.btn_complete);
        mCancelBtn=findViewById(R.id.btn_cancel);
    }

    private void initListener() {
        //设置拖动视图item改变监听器
        mDownDragView.setOnChangeListener(new DragGridView.OnChangeListener() {

            @Override
            public void onChange(int from, int to) {

                Collections.swap(mDownSource,from,to);

                mDownAdapter.notifyDataSetChanged();

            }

            @Override
            public void onMove(int x, int y) {
                mShowView.setmMoveX(x);
                mShowView.setmMoveY(y);
                mShowView.posToRect();
                mShowView.invalidate();
            }

            @Override
            public void onMoveEnd(int x, int y, DownItem item,int pos) {
                mShowView.setmIsPress(false);
                mShowView.invalidate();
                mShowView.onMoveEnd(x,y,item.getText(),R.mipmap.ic_launcher,pos);
            }


            @Override
            public void onDown(boolean isDown) {
                mShowView.setmIsPress(isDown);
            }
        });

        //设置ShowView添加item成功回调监听
        mShowView.setmMoveListener(new ShowView.MoveListener() {
            @Override
            public void addSuccess(int pos) {
                mDownDragView.removeAt(pos);
            }

            @Override
            public void addFail() {
                mDownDragView.addFail();
            }
        });

        mCompleteBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);


    }

    private void initAdapter() {

        mDownAdapter=new DownDragAdapter(this,R.layout.drag_down_item,mDownSource);
        mDownDragView.setAdapter(mDownAdapter);

//        mUpAdapter=new UpDragAdapter(this,R.layout.drag_up_item,mUpSource);
//        mUpDragView.setAdapter(mUpAdapter);

    }

    private void initData() {

//        for (int i = 1; i <= 16; i++) {
//            UpItem bean=new UpItem();
//            bean.setImageResId(R.mipmap.ic_launcher);
//            bean.setText("upper"+i);
//            mUpSource.add(bean);
//        }

        for(int i=1;i<=16;i++){
            DownItem item=new DownItem();
            item.setText("down"+i);
            mDownSource.add(item);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:

                break;

            case R.id.btn_complete:
                break;
        }
    }
}
