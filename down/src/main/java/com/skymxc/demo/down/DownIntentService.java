package com.skymxc.demo.down;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import java.util.Observable;
import java.util.Observer;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class DownIntentService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_ADD = "com.skymxc.demo.down.action.ADD";
    private static final String ACTION_CANCEL = "com.skymxc.demo.down.action.CANCEL";
    private static final String ACTION_ADD_OBSERVER = "com.skymxc.demo.down.action.ADD.OBSERVER";
    private static final String ACTION_REMOVE_OBSERVER = "com.skymxc.demo.down.action.REMOVE.OBSERVER";


    private static final String EXTRA_PARAM1 = "com.skymxc.demo.down.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.skymxc.demo.down.extra.PARAM2";
    private static final String EXTRA_PARAM_URL = "com.skymxc.demo.down.extra.url";
    private static final String EXTRA_PARAM_PATH = "com.skymxc.demo.down.extra.path";
    private static final String EXTRA_PARAM_OBSERVER = "com.skymxc.demo.down.extra.observer";

    private static final int NOTIFY_ID = 931917;

    private DownServer server;
    private Handler handler;

    public DownIntentService() {
        super("DownIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        server = new DownServer(10);
        server.addSObserver(observer);
    }

    /**
     * 添加 下载
     *
     * @param context
     * @param url
     * @param path
     */
    public static void startActionAdd(Context context, String url, String path) {
        Intent intent = new Intent(context, DownIntentService.class);
        intent.setAction(ACTION_ADD);
        intent.putExtra(EXTRA_PARAM_URL, url);
        intent.putExtra(EXTRA_PARAM_PATH, path);
        context.startService(intent);
    }



    public static void startActionCancel(Context context, String url, boolean del) {
        Intent intent = new Intent(context, DownIntentService.class);
        intent.setAction(ACTION_REMOVE_OBSERVER);
        intent.putExtra(EXTRA_PARAM_URL, url);
        intent.putExtra(EXTRA_PARAM1, del);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (handler == null) {
                handler = new Handler();
            }
            switch (action) {
                case ACTION_ADD:
                    String url = intent.getStringExtra(EXTRA_PARAM_URL);
                    String path = intent.getStringExtra(EXTRA_PARAM_PATH);
                    handleActionAdd(url, path);
                    break;
                case ACTION_CANCEL:
                    url = intent.getStringExtra(EXTRA_PARAM_URL);
                    boolean del = intent.getBooleanExtra(EXTRA_PARAM1, false);
                    handleActionCancel(url, del);
                    break;
            }
            /*
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
            */
        }
    }

    private void handleActionCancel(String url, boolean del) {
        server.cancel(url, del);

    }

    private void handleActionAdd(String url, String path) {
        server.add(url, path);
        sendNotify();
    }

    private Notification sendNotify() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent activity = PendingIntent.getActivity(this, 200, intent, PendingIntent.FLAG_ONE_SHOT);
        final Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("下载服务")
                .setAutoCancel(false)
//                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(activity)
                .build();
        notification.flags = Notification.FLAG_NO_CLEAR;
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(NOTIFY_ID, notification);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startForeground(NOTIFY_ID, notification);

            }
        }, 50);
        return notification;
    }

    private void removeNotify() {
        stopForeground(true);
    }

    private Observer observer = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            int argInt = (int) arg;
            switch (argInt) {
                case -1:
                    removeNotify();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent activity = PendingIntent.getActivity(getApplicationContext(), 200, intent, PendingIntent.FLAG_ONE_SHOT);
                    Notification notification = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setContentTitle("下载完毕")
                            .setContentIntent(activity)
                            .setAutoCancel(true)
//                            .setDefaults(Notification.DEFAULT_ALL)
                            .build();
                    NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.notify(NOTIFY_ID, notification);
                    break;
            }
        }
    };


    @Override
    public void onDestroy() {
        if (null!=handler){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (null!=server){
            server.removeSObserver(observer);
        }

        super.onDestroy();

    }
}
