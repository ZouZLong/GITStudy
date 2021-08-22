package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * 击打游戏
 */
public class MyView extends View {

    private Paint paint;
    private int initHeight = 400; //默认的画布高度
    private int initWeight = 20; //默认的矩形宽度
    private int initSpacing = 5; //默认的每一个矩形的间距

    private List<Integer> list;//要绘画的高度集合(全部的数据)
    private List<Integer> showList; //显示给用户看的数据
    private GestureDetector mGesture; //手势识别器
    private int initShow = 20; //默认显示在画板View中的数据
    private int position = 0; //表示暂时还没有滑动过数据

    //第一个构造方法时在代码中创建view的时候可以使用的
    public MyView(Context context) {
        super(context);
    }

    //而第二个构造方法则是在xml中创建view的时候使用的。
    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();//定义画笔
        paint.setColor(0xFFFF6600); //设置颜色
        paint.setStyle(Paint.Style.FILL); //设置画笔类型

        list = new ArrayList<>();
        showList = new ArrayList<>();
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 测量。系统会先根据xml布局文件和代码中对控件属性的设置，来获取或者计算出每个View和ViewGrop的尺寸，并将这些尺寸保存下来。
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 布局。根据测量出的结果以及对应的参数，来确定每一个控件应该显示的位置。
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 绘制。确定好位置后，就将这些控件绘制到屏幕上。
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Log.e("123", "绘画 showList.size():" + showList.size());

        for (int i = 0; i < showList.size(); i++) {
            canvas.drawRect(
                    initSpacing * (i + 1) + initWeight * i,
                    initHeight - showList.get(i),
                    initSpacing * (i + 1) + initWeight * i + initWeight,
                    initHeight, paint); //使用画笔在画布上画矩形
        }
    }


    {
        mGesture = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                showList.clear();

                if (distanceX > 0) { //←
                    int IntDistanceX = Math.round(distanceX);
                    if (IntDistanceX > 10) { //滑动的速度
                        if (position < 1) return true;
                        position--;
                        if (list.size() - initShow - position > list.size() - 1)
                            return true; //到最一开始的数据了
                        for (int i = list.size() - initShow - position; i < list.size() - position; i++) {
                            showList.add(list.get(i));
                        }
                    }
                }

                if (distanceX < 0) {//→
                    int IntDistanceX = Math.round(-distanceX);
                    if (IntDistanceX > 10) { //滑动的速度
                        position++;
                        if (list.size() - initShow - position < 0) {
                            position = list.size() - initShow;
                            return true; //到最一开始的数据了
                        }
                        for (int i = list.size() - initShow - position; i < list.size() - position; i++) {
                            showList.add(list.get(i));
                        }
                    }
                }

                if (showList.size() > 0) invalidate();
                return true;
            }


            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGesture.onTouchEvent(event);
        return true;
    }

    /**
     * height 增加的数据的绘画高度
     */
    public void addData(int height) {
        position = 0;
        list.add(height);
        if (list.size() >= initShow) {
            showList.clear();
            int i = 0;
            while (i < initShow) {
                i++;
                showList.add(list.get(list.size() - i));
            }
            Collections.reverse(showList);
        } else {
            showList.add(height);
        }
        postInvalidate();
    }
}
