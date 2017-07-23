package com.skymxc.drag.broadcast;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.skymxc.drag.broadcast.receiver.LocalReceiver;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    private LocalBroadcastManager manager;
    private LocalReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = LocalBroadcastManager.getInstance(this);
        receiver = new LocalReceiver();

        IntentFilter filter = new IntentFilter("com.example.broadcast.local.async");
        manager.registerReceiver(receiver,filter);


    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
       // unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.to_second:
                Intent in = new Intent(this,SecondActivity.class);
                startActivity(in);
                break;
            case R.id.send_broadcast:
                //发送普通广播
                Intent intent = new Intent("com.mxc.example.broadcast.normal");
                intent.putExtra("type","normal");
                sendBroadcast(intent);
                break;
            case R.id.send_order_broadcast:
                Intent orderIntent = new Intent("com.mxc.example.broadcast.order");
                orderIntent.putExtra("type","order");
                //不添加权限
                sendOrderedBroadcast(orderIntent,null);
                break;
            case R.id.send_local_broadcast:

                Intent localIntent = new Intent("com.example.broadcast.local.async");
                manager.sendBroadcast(localIntent);
                //发送 同步的（有序）的广播
                manager.sendBroadcastSync(localIntent);
                break;
        }

    }
}
