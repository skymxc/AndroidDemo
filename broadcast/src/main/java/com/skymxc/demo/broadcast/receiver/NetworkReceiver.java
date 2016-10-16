package com.skymxc.demo.broadcast.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sky-mxc
 * 网络状态改变接收器
 */

public class NetworkReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
            Log.i(TAG,"=====网络状态改变了===");
            Toast.makeText(context,"网络状态改变了",Toast.LENGTH_LONG).show();
        }else if (intent.getAction().equals("com.mxc.example.broadcast.normal")){
            Log.i(TAG,"=====接收到普通广播=====type="+intent.getStringExtra("type"));
        }
    }
}
