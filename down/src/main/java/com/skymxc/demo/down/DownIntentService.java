package com.skymxc.demo.down;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Observable;
import java.util.Observer;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.skymxc.demo.down.action.FOO";
    private static final String ACTION_BAZ = "com.skymxc.demo.down.action.BAZ";
    private static final String ACTION_ADD = "com.skymxc.demo.down.action.ADD";
    private static final String ACTION_CANCEL = "com.skymxc.demo.down.action.CANCEL";
    private static final String ACTION_ADD_OBSERVER = "com.skymxc.demo.down.action.ADD.OBSERVER";
    private static final String ACTION_REMOVE_OBSERVER = "com.skymxc.demo.down.action.REMOVE.OBSERVER";


    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.skymxc.demo.down.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.skymxc.demo.down.extra.PARAM2";
    private static final String EXTRA_PARAM_URL = "com.skymxc.demo.down.extra.url";
    private static final String EXTRA_PARAM_PATH = "com.skymxc.demo.down.extra.path";
    private static final String EXTRA_PARAM_OBSERVER = "com.skymxc.demo.down.extra.observer";

    private static final int NOTIFY_ID = 931917;

    private DownServer server;

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
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DownIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DownIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
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

    public static void startActionAddObserver(Context context,String url,CustomObserver observer){
        Intent intent = new Intent(context,DownIntentService.class);
        intent.setAction(ACTION_ADD_OBSERVER);
        intent.putExtra(EXTRA_PARAM_URL,url);
        intent.putExtra(EXTRA_PARAM_OBSERVER,observer);
        context.startService(intent);
    }


    public static void startActionRemoveObserver(Context context,String url,CustomObserver observer){
        Intent intent = new Intent(context,DownIntentService.class);
        intent.setAction(ACTION_REMOVE_OBSERVER);
        intent.putExtra(EXTRA_PARAM_URL,url);
        intent.putExtra(EXTRA_PARAM_OBSERVER,observer);
        context.startService(intent);
    }

    public static void startActionCancel(Context context,String url,boolean del){
        Intent intent = new Intent(context,DownIntentService.class);
        intent.setAction(ACTION_REMOVE_OBSERVER);
        intent.putExtra(EXTRA_PARAM_URL,url);
        intent.putExtra(EXTRA_PARAM1,del);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_ADD:
                    String url = intent.getStringExtra(EXTRA_PARAM_URL);
                    String path = intent.getStringExtra(EXTRA_PARAM_PATH);
                    handleActionAdd(url,path);
                    break;
                case ACTION_REMOVE_OBSERVER:
                    url = intent.getStringExtra(EXTRA_PARAM_URL);
                    CustomObserver observer = (CustomObserver) intent.getSerializableExtra(EXTRA_PARAM_OBSERVER);
                    handleActionRemoveObserver(url,observer);
                    break;
                case ACTION_ADD_OBSERVER:
                    url = intent.getStringExtra(EXTRA_PARAM_URL);
                     observer = (CustomObserver) intent.getSerializableExtra(EXTRA_PARAM_OBSERVER);
                    handleActionAddObserver(url,observer);
                    break;
                case ACTION_CANCEL:
                    url = intent.getStringExtra(EXTRA_PARAM_URL);
                    boolean del = intent.getBooleanExtra(EXTRA_PARAM1,false);
                    handleActionCancel(url,del);
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
        server.cancel(url,del);

    }
    private void handleActionAdd(String url, String path) {
        server.add(url,path);
        sendNotify();
    }

    private void handleActionAddObserver(String url, Observer observer){
        server.addObserver(url,observer);
    }
    private void handleActionRemoveObserver(String url,Observer observer){
        server.removeObserver(url,observer);
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private void sendNotify(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent activity = PendingIntent.getActivity(getApplicationContext(), 200, intent, PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("下载服务")
                .setAutoCancel(false)
                .setContentIntent(activity)
                .build();
        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFY_ID,notification);
        startForeground(NOTIFY_ID,notification);
    }

    private void removeNotify(){
        stopForeground(true);
    }

    private Observer observer = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            int argInt = (int) arg;
            switch (argInt){
                case -1:
                    removeNotify();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent activity = PendingIntent.getActivity(getApplicationContext(), 200, intent, PendingIntent.FLAG_ONE_SHOT);
                    Notification notification = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setContentTitle("下载完毕")
                            .setContentIntent(activity)
                            .build();
                    NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.notify(NOTIFY_ID,notification);
                    break;
            }
        }
    };

}
