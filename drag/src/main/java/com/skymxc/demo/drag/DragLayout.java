package com.skymxc.demo.drag;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by mxc on 2017/7/23.
 * description:
 */

public class DragLayout extends RelativeLayout {

    private static final String TAG = "SwipeLayout";

    private View mContentView;
    private int mDragDistance ;
    private ViewDragHelper mDragHelper;
    public DragLayout(Context context) {
        this(context,null);
    }

    public DragLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this,1.0f,new DragHelper());
    }

    /**
     * 加载view完毕后
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //这里写死了，注意
        mContentView= getChildAt(0);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //交由 DragHelper 去判断 是否应该拦截该事件
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //如果事件被拦截，我们就将事件交由 DragHelper去处理
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
//        if (mDragHelper.continueSettling(true)){
//            ViewCompat.postInvalidateOnAnimation(this);
//        }
    }

    class DragHelper extends ViewDragHelper.Callback{

        /**
         * 决定是否捕获此view
         * 这里自由决定
         * @param child 待捕获的子元素
         * @param pointerId
         * @return 是否捕获
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child==mContentView;
        }

        /**
         * 允许的拖动范围 水平范围内
         * @param child
         * @return
         */
//        @Override
//        public int getViewHorizontalDragRange(View child) {
//            Log.e(TAG,"getViewHorizontalDragRange--DragDistance-->"+mDragDistance);
//            return mDragDistance;
//        }

        /**
         * 水平 拖动
         * @param child 拖动的元素
         * @param left 将要去往的位置
         * @param dx 拖动了的距离
         * @return 新位置
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //限制在容器内
            int leftBound = getPaddingLeft();
            int rightBound = getWidth() - mContentView.getWidth();
            int newLeft = Math.min(Math.max(left,leftBound),rightBound);
            return newLeft;
        }

        /**
         * 垂直拖动
         * @param child
         * @param top
         * @param dy
         * @return
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int topBound = getPaddingTop();
            int bottomBound = getHeight() - mContentView.getHeight();
            int newTop = Math.min(Math.max(top,topBound),bottomBound);
            return newTop;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            switch (state){
                case ViewDragHelper.STATE_IDLE:
                    Log.e("onViewDragStateChanged","state-->STATE_IDLE"+state);
                    break;
                case ViewDragHelper.STATE_DRAGGING:
                    Log.e("onViewDragStateChanged","state-->STATE_DRAGGING"+state);
                    break;
                case ViewDragHelper.STATE_SETTLING:
                    Log.e("onViewDragStateChanged","state-->STATE_SETTLING"+state);
                    break;
            }
        }

        /**
         * 当拖动的view position发生改变时触发
         * @param changedView 拖动的view
         * @param left 新位置 X轴
         * @param top 新位置 Y轴
         * @param dx 从上次位置 到这次位置移动的距离 X轴
         * @param dy 从上次位置 到这次位置移动的距离 Y轴
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        }

        /**
         * 停止拖动
         * @param releasedChild
         * @param xvel x 轴速度  每秒移动的像素值
         * @param yvel Y 轴速度 每秒移动的像素值
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            Log.e("onViewReleased","xvel-->"+xvel+";yvel-->"+yvel);
        }
    }

}
