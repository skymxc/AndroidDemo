package com.skymxc.demo.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class BActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        EventBus.getDefault().register(this);
    }

    @Subscribe(sticky = true)
    public void onStickyEvent(OtherEvent event) {
        Log.e("onStickyEvent", "event->" + event.getMsg());

    }

    @Subscribe(sticky = true,priority = 3)
    public void onMessageEvent(MessageEvent event){
        Log.e("onMessageEvent","msg->"+event.getMsg());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.posting:
                MessageEvent event = new MessageEvent();
                event.setMsg("this is MessageEvent");
                EventBus.getDefault().post(event);
                break;
            case R.id.to_c:
                Intent intent =new Intent(this,CActivity.class);
                startActivity(intent);
                break;
            case R.id.other:
                OtherEvent event1 = new OtherEvent("other event");
                EventBus.getDefault().postSticky(event1);
                break;
            case R.id.remove_sticky:
                OtherEvent stickyEvent = EventBus.getDefault().getStickyEvent(OtherEvent.class);
                if (null!=stickyEvent){
                    Log.e("onClick","remove sticky");
                    EventBus.getDefault().removeStickyEvent(stickyEvent);
                }
                break;

        }
    }
}
