package com.skymxc.demo.thread;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ExecutorActivity extends AppCompatActivity {

    ProgressBar pb0;
    ProgressBar pb1;
    ProgressBar pb2;
    File mkdir;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executor);
        getSupportActionBar().setTitle("线程池");
        pb0 = (ProgressBar) findViewById(R.id.progress_0);
        pb1 = (ProgressBar) findViewById(R.id.progress_1);
        pb2 = (ProgressBar) findViewById(R.id.progress_2);
        tv=(TextView) findViewById(R.id.text);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int i = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (i != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                mkdirs();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mkdirs();
        }
    }

    private void mkdirs() {
        mkdir = new File(Environment.getExternalStorageDirectory(), "skymxc");
        if (!mkdir.exists()) {
            mkdir.mkdir();
        }
    }

    private Handler handle = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    updateProgress(msg.arg1, pb0, msg.arg2, msg.arg2, (String) msg.obj);
                    break;
                case 1:
                    updateProgress(msg.arg1, pb1, msg.arg2, msg.arg2, (String) msg.obj);
                    break;
                case 2:
                    updateProgress(msg.arg1, pb2, msg.arg2, msg.arg2, (String) msg.obj);
                    break;
                case 3:
                    tv.setText("schedule->"+msg.arg1);
                    break;
                case 4:
                    tv.setText("schedule->complete");
                    count=0;
                    break;
            }
            return true;
        }
    });

    private void updateProgress(int ptr, ProgressBar pb, int total, int progress, String string) {
        switch (ptr) {
            case 0:
                pb.setMax(total);
                break;
            case 1:
                pb.setProgress(progress);
                break;
            case 2:
                Log.e("updateProgress", "complete->" +string);
                break;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.single:
                singleThread();
                break;
            case R.id.fixed:
                fixedThread();
                break;
            case R.id.cache:
                cacheThread();
                break;
            case R.id.schedule:
                scheduleThread();
                break;
        }
    }

    private void scheduleThread() {
        cleanMax();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);


        // 周期1秒
        final ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);

        //10s 后取消
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
               handle.sendEmptyMessage(4);
                scheduledFuture.cancel(true);
            }
        },10, TimeUnit.SECONDS);
    }

    int count=0;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            count++;
            Message message = handle.obtainMessage(3, count,0);
            handle.sendMessage(message);
        }
    };

    private void cacheThread() {
        cleanMax();
        ExecutorService executorService = Executors.newCachedThreadPool();
        Down down0 = new Down("https://www.oschina.net/uploads/osc-android-v2.8.7-release.apk", 0);
        Down down1 = new Down("https://www.oschina.net/uploads/osc.xap", 1);
        Down down2 = new Down("https://www.oschina.net/uploads/git-osc-androidv158.apk", 2);
        FutureTask<File> ft0 = new FutureTask<File>(down0);
        FutureTask<File> ft1 = new FutureTask<File>(down1);
        FutureTask<File> ft2 = new FutureTask<File>(down2);
        executorService.execute(ft0);
        executorService.execute(ft1);
        executorService.execute(ft2);
    }

    Future<File> submit0;
    Future<File> submit1;
    Future<File> submit2;

    private void fixedThread() {
        cleanMax();
        Down down0 = new Down("https://www.oschina.net/uploads/osc-android-v2.8.7-release.apk", 0);
        Down down1 = new Down("https://www.oschina.net/uploads/osc.xap", 1);
        Down down2 = new Down("https://www.oschina.net/uploads/git-osc-androidv158.apk", 2);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        submit0 = executorService.submit(down0);
        submit1 = executorService.submit(down1);
        submit2 = executorService.submit(down2);
    }

    private void cleanMax() {
        pb0.setMax(0);
        pb1.setMax(0);
        pb2.setMax(0);
    }


    private void singleThread() {
        cleanMax();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Down down0 = new Down("https://www.oschina.net/uploads/osc-android-v2.8.7-release.apk", 0);
        Down down1 = new Down("https://www.oschina.net/uploads/osc.xap", 1);
        Down down2 = new Down("https://www.oschina.net/uploads/git-osc-androidv158.apk", 2);
        FutureTask<File> ft0 = new FutureTask<File>(down0);
        FutureTask<File> ft1 = new FutureTask<File>(down1);
        FutureTask<File> ft2 = new FutureTask<File>(down2);
        executorService.submit(ft0);
        executorService.submit(ft1);
        executorService.submit(ft2);
    }


    class Down implements Callable<File> {

        private String downUrl;
        private int num;

        public Down(String url, int num) {
            this.downUrl = url;
            this.num = num;
        }

        @Override
        public File call() throws Exception {
            String substring = downUrl.substring(downUrl.lastIndexOf("/"), downUrl.length());
            File file = new File(mkdir, substring);
            if (file.exists()){
                file.delete();
            }
            URL url = new URL(downUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Log.e("call", "start->" + downUrl);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                FileOutputStream fos = new FileOutputStream(file);
                InputStream is = connection.getInputStream();
                int contentLength = connection.getContentLength();
                Message message = handle.obtainMessage(num, 0, contentLength);
                handle.sendMessage(message);
                byte[] buffer = new byte[2048];
                int len = -1;
                int current = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    current += len;
                    Message message1 = handle.obtainMessage(num, 1, current);
                    handle.sendMessage(message1);
                }
                fos.flush();
                fos.close();
                is.close();
                Message message1 = handle.obtainMessage(num, 2, 0, file.getPath());
                handle.sendMessage(message1);
            } else {
                Log.e("call", "code(" + connection.getResponseCode() + ");msg:" + connection.getResponseMessage());
            }
            connection.disconnect();
            Log.e("call", "close->" + downUrl);
            return file;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handle.removeCallbacksAndMessages(null);
    }


}
