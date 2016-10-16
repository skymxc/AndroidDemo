package com.skymxc.demo.broadcast.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by sky-mxc
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"==action:"+intent.getAction()+"===time:"+ SystemClock.currentThreadTimeMillis());
        Log.i(TAG,"==type:"+intent.getStringExtra("type"));
        if (intent.getAction().equals("com.mxc.example.broadcast.order")){
            //终止广播
           // abortBroadcast();
            //增加额外的结果
            Bundle b = getResultExtras(true);//获取数据 如果没有就创建
            b.putString("result","MyReceiver");
            setResultExtras(b);
        }
    }
}
