package com.skymxc.demo.broadcast.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by sky-mxc
 */

public class SecondReceiver extends BroadcastReceiver {
    private static final String TAG = "SecondReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"==action:"+intent.getAction()+"===time:"+ SystemClock.currentThreadTimeMillis());
        Log.i(TAG,"==type:"+intent.getStringExtra("type"));
        Log.e(TAG,"==result:"+getResultExtras(true).getString("result"));
    }
}
