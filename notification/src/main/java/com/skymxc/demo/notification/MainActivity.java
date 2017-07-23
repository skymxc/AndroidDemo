package com.skymxc.drag.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RemoteViews;

import java.io.File;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    private static final int NID =10;

    private NotificationManager manager ;

    private Bitmap bmp ;

    private CheckBox cbDefault;
    private CheckBox cbSound;
    private CheckBox cbLight;
    private CheckBox cbVibrate;
    private CheckBox cbProgress;
    private CheckBox cbIndicator;
    private CheckBox cbActivity;
    private CheckBox cbBroadcast;
    private CheckBox cbNeverClear;
    private CheckBox cbAutoClear;
    private CheckBox cbFullScreen;
    private CheckBox cbCustomView;
    private int current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        bmp = BitmapFactory.decodeResource(getResources(),R.mipmap.d);

        cbDefault = (CheckBox) findViewById(R.id.default_hint);
        cbSound = (CheckBox) findViewById(R.id.sound);
        cbLight = (CheckBox) findViewById(R.id.light);
        cbVibrate = (CheckBox) findViewById(R.id.vibrate);
        cbProgress = (CheckBox) findViewById(R.id.show_progress);
        cbIndicator = (CheckBox) findViewById(R.id.show_indicator);
        cbActivity = (CheckBox) findViewById(R.id.activity_intent);
        cbBroadcast = (CheckBox) findViewById(R.id.broadcast_intent);
        cbAutoClear = (CheckBox) findViewById(R.id.auto_clear);
        cbNeverClear = (CheckBox) findViewById(R.id.never_clear);
        cbFullScreen = (CheckBox) findViewById(R.id.show_fullscreen);
        cbCustomView = (CheckBox) findViewById(R.id.custom_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_normal_notification:
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setContentText("好吧")
                        .setContentTitle("二狗子")
                        .setSmallIcon(R.mipmap.ao5)
                        .setLargeIcon(bmp)
                        .setAutoCancel(true);



                manager.notify(NID,builder.build());
                break;
            case R.id.send__notification:
                Notification notification  = createNotification();
                //不能清除 通知
                if (cbNeverClear.isChecked()){
                    notification.flags |=Notification.FLAG_NO_CLEAR;
                }

                //响应后自动删除 方式一
                if (cbAutoClear.isChecked()){
                    notification.flags|=Notification.FLAG_AUTO_CANCEL;
              }



                if (cbProgress.isChecked()){
                    cbDefault.post(new Runnable() {
                        @Override
                        public void run() {
                            Notification notification= createNotification();
                            manager.notify(20,notification);
                            if (current<=100) {
                                cbProgress.postDelayed(this, 500);
                            }
                        }
                    });
                }else{

                manager.notify(20,notification);
                }

                break;
            case R.id.cancel_notification:
                manager.cancel(NID);
                break;
            case R.id.cancel_all:
                manager.cancelAll();
                break;
        }
    }

    /**
     * 创建 Notification
     * @return
     */
    private Notification createNotification() {
        Log.e("tag","=====createNotification()===========current:"+current);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon)
                .setContentTitle("微微一笑很倾城")
                .setContentText("好吧")
                .setLargeIcon(bmp);

        //震动
        if (cbVibrate.isChecked()){
            builder.setVibrate(new long[]{1000*1});
        }

        //声音
        if (cbSound.isChecked()){
            builder.setSound(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()+"/Download/aa.mp3")));
        }

        //声音，震动，呼吸灯.... 全部默认
        if (cbDefault.isChecked()){
            builder.setDefaults(Notification.DEFAULT_ALL);
        }

        //进度
        if( cbProgress.isChecked()){
            if (current<=100){
                builder.setProgress(100,current,false);
                current+=5;
            }else{
                //移除进度条
                builder.setProgress(0,0,false);
            }
        }

        //指示器
        if (cbIndicator.isChecked()){
            builder.setProgress(0,0,true);
        }
        //增加响应的intent
        if (cbActivity.isChecked()){
            Intent intent = new Intent(this,SecondActivity.class);
            PendingIntent pi= PendingIntent.getActivity(this,50,intent,PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pi);
        }

        //响应后自动清除 方式二
//        if (cbAutoClear.isChecked()){
//            builder.setAutoCancel(true);
//        }

        //发送广播
        if (cbBroadcast.isChecked()){
            PendingIntent pi = PendingIntent.getBroadcast(this,60,new Intent("com.mxc.example.broadcast"),PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pi);
        }
        //悬挂式
        if (cbFullScreen.isChecked()){
            Intent intent = new Intent(this,SecondActivity.class);
            PendingIntent pi= PendingIntent.getActivity(this,50,intent,PendingIntent.FLAG_UPDATE_CURRENT);
           builder.setFullScreenIntent(pi,true);
        }

        if (cbCustomView.isChecked()){
            RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.layout_notification);
            remoteViews.setImageViewResource(R.id.icon,R.mipmap.ao5);
            remoteViews.setTextViewText(R.id.title,"母猪真的上树了");
            remoteViews.setTextViewTextSize(R.id.title,TypedValue.COMPLEX_UNIT_SP,18);
            remoteViews.setTextColor(R.id.title, Color.GREEN);
            remoteViews.setTextViewText(R.id.text,"关于母猪能不能上树的问题，一直是很有争论");
            remoteViews.setTextViewTextSize(R.id.text, TypedValue.COMPLEX_UNIT_SP,15);
            builder.setContent(remoteViews);
        }


        return builder.build();
    }
}
