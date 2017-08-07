package com.skymxc.demo.event;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //订阅 事件注册
        EventBus.getDefault().register(this);
    }

    @Subscribe()
    public void onMessageEvent(MessageEvent event){
        Log.e("onMessageEvent","msg->"+event.getMsg()+";thread->"+Thread.currentThread().getName());
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
                break;
            case R.id.publish:
                //发送 事件
                MessageEvent event = new MessageEvent("this is messageEvent!");
                EventBus.getDefault().post(event);
                break;
        }
    }


}
