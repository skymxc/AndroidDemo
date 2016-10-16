package com.skymxc.demo.broadcast.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by sky-mxc
 */

public class LocalReceiver extends BroadcastReceiver {
    private  static final String TAG ="LocalReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"===接受到 广播："+intent.getAction());
    }
}
