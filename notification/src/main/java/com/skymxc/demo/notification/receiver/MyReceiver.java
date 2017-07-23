package com.skymxc.drag.notification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sky-mxc
 */

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("tag","==========action:"+intent.getAction());
        Toast.makeText(context,"接受到广播了",Toast.LENGTH_LONG).show();
    }
}
