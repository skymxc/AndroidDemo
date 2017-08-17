package com.samson.scroll;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * ScrollView并没有实现滚动监听，所以我们必须自行实现对ScrollView的监听，
 * 我们很自然的想到在onTouchEvent()方法中实现对滚动Y轴进行监听
 * ScrollView的滚动Y值进行监听
 */
public class MyScrollView extends ScrollView {

    /**
     * 滚动的回调接口
     */
    public interface MyOnScrollListener{
        //回调方法， 返回MyScrollView滑动的Y方向距离
        void onScroll(int scorllY);
    }

    private MyOnScrollListener onScrollListener;

    //主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
    private int lastScrollY;

    public MyScrollView(Context context) {
        this(context,null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置滚动接口
     * @param onScrollListener
     */
    public void setOnScrollListener(MyOnScrollListener onScrollListener){
        this.onScrollListener= onScrollListener;
    }

    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private Handler handler=new Handler(){

        public void handleMessage(Message message){
            int scorllY=MyScrollView.this.getScrollY();

            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            //如果不等与手指离开后的Y值，说明滑动还没停止，继续获取新值
            if (lastScrollY!=scorllY) {
                lastScrollY=scorllY;
                //滑动没停止，再次获取
                handler.sendMessageDelayed(handler.obtainMessage(),5);
            }
            if (onScrollListener!=null) {
                onScrollListener.onScroll(scorllY);
            }
        }
    };

    /**
     *  重写onTouchEvent， 当用户的手在MyScrollView上面的时候，
     *  直接将MyScrollView滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候，
     *  MyScrollView可能还在滑动，所以当用户抬起手我们隔5毫秒给handler发送消息，在handler处理
     *  MyScrollView滑动的距离
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (onScrollListener!=null) {
            onScrollListener.onScroll(lastScrollY=this.getScrollY());
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                //如果手指离开，就20毫秒后再次请求，其他操作不影响，都传递滑动距离即可
                //20毫秒后再次执行，以防手指离开后还在继续滑动
                handler.sendMessageDelayed(handler.obtainMessage(), 20);
                break;
        }
        return super.onTouchEvent(ev);
    }
}
