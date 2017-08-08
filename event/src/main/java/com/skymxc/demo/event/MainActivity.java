package com.skymxc.demo.event;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.SubscriberExceptionEvent;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //使用索引 并在defaultEventBus 使用
        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();

        //订阅 事件注册
        EventBus.getDefault().register(this);
    }


    /**
     * 默认 posting 与发送者在同一线程
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING,priority = 2)
    public void onMessageEvent(MessageEvent event){
        Log.e("onMessageEvent","msg->"+event.getMsg()+";thread->"+Thread.currentThread().getName());
    }

    /**
     * UI 线程
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN,priority = 1)
    public void onMsgEventMain(MessageEvent event){
        Log.e("onMsgEventMain","msg->"+event.getMsg()+";thread-》"+Thread.currentThread().getName());
    }

    /**
     * 后台线程 如果是在UI线程发出，就另开线程，否则直接运行
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND,priority = 9)
    public void onMsgEventBackground(MessageEvent event){
        Log.e("onMsgEventBackground","msg->"+event.getMsg()+";thread->"+Thread.currentThread().getName());
    }

    /**
     * 异步线程 另开一个线程
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.ASYNC,priority = 3)
    public void onMsgEventAsync(MessageEvent event){
        Log.e("onMsgEventAsync","msg->"+event.getMsg()+";thread->"+Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExceptionEvent(SubscriberExceptionEvent event){
        Log.e("onExceptionEvent","msg->"+event.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.to_b:
                Intent intent = new Intent(this,BActivity.class);
                startActivity(intent);
                break;
            case R.id.publish:
                //发送 sticky 事件
                OtherEvent event = new OtherEvent("this is postSticky!");
                EventBus.getDefault().postSticky(event);
                break;
        }
    }


}
