package com.skymxc.demo.drag;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/**
 * Created by mxc on 2017/7/23.
 * description:
 */

public class SwipeItemLayout extends LinearLayout {
    private final double AUTO_OPEN_SPEED_LIMIT = 500.0;
    private View mActionView;
    private View mContentView;
    private int mDragDistance;
    private ViewDragHelper mDragHelper;
    private boolean isOpen;


    public SwipeItemLayout(Context context) {
        this(context, null);
    }

    public SwipeItemLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeItemLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new SwipeItemDragHelper());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        mActionView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mDragDistance = mActionView.getMeasuredWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }


    float x = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                float gap = event.getRawX() - x;
                int sl = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                if (Math.abs(gap) > sl) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                x = 0;
                break;
        }
        return true;
    }

    /**
     * 因为要在 DragHelper的中使用动画
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    class SwipeItemDragHelper extends ViewDragHelper.Callback {

        private int dragDx;

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mContentView || child == mActionView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            dragDx += dx;
            if (child == mContentView) {
                /**
                 * 这个位置 的范围应该是在 0和 -dragDistance之间；最大是0；最小是 -dragDistance
                 */
                int leftBound = getPaddingLeft();
                int minLeft = -leftBound - mDragDistance;
                int newLeft = Math.min(Math.max(minLeft, left), 0);
                return newLeft;
            } else {
                /**
                 * 这个view的位置范围应该是在 父布局的宽度-actionView的宽和父布局的宽度之间；
                 */
                int leftBound = getPaddingLeft();
                int minLeft = getWidth() - leftBound - mActionView.getWidth();
                int newLeft = Math.min(Math.max(minLeft, left), getWidth());
                return newLeft;
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            //同时移动
            if (changedView == mContentView) {
                mActionView.offsetLeftAndRight(dx);
            } else {
                mContentView.offsetLeftAndRight(dx);
            }
            invalidate();
//            Log.e("onViewPosition", "dx-->" + dx);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            /**
             * 这里的速度 是这样计算的  每秒的拖动的像素 值
             * 速度判断
             *  如果向→滑动 速度肯定是 正数；
             *  如果向←滑动 速度肯定是 负数
             * 如果 拖动距离 是 actionView的 ¼ 就允许打开或关闭
             */
            //根据速度决定是否打开
            boolean settleToOpen = false;
            float realVel = Math.abs(xvel);
            int realDragX = Math.abs(dragDx);
            if (realVel > AUTO_OPEN_SPEED_LIMIT) { //根据速度判断
                if (xvel > 0) { //右滑
                    settleToOpen = false;
                } else {  //左滑
                    settleToOpen = true;
                }
            } else if (realDragX > mDragDistance / 4) {  //根据拖动距离判断
                if (dragDx > 0) { //右滑
                    settleToOpen = false;
                } else {
                    settleToOpen = true;
                }
            }
            isOpen = settleToOpen;
            int settleDestX = isOpen ? -mDragDistance : 0;
            Log.e("onViewReleased", "settleToOpen->" + settleToOpen + ";destX->" + settleDestX + ";xvel->" + xvel + ";dragDx-->" + dragDx);
            mDragHelper.smoothSlideViewTo(mContentView, settleDestX, 0);
            ViewCompat.postInvalidateOnAnimation(SwipeItemLayout.this);
            dragDx = 0;
        }
    }


}
