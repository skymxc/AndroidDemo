package com.skymxc.demo.thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ExecutorActivity extends AppCompatActivity {

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executor);
        getSupportActionBar().setTitle("线程池");
        iv = (ImageView) findViewById(R.id.image);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.single:
                singleThread();
                break;
        }
    }

    private void singleThread() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new SingleT("https://avatars0.githubusercontent.com/u/19643691?v=4&u=2a6388a87c38af17af6aeb451f6b6e972537d152&s=400"));
        executorService.execute(new SingleT("https://www.ctolib.com/avatarsicons/17905935.png"));
    }


    class SingleT  implements Runnable{

        private String url;
        public SingleT(String url){
            this.url = url;
        }

        @Override
        public void run() {
            try {
                Log.e("call","start-->"+this.url);
                URL url = new URL(this.url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode()==200){
                    InputStream is = connection.getInputStream();
                    final Bitmap finalBmp =BitmapFactory.decodeStream(is);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv.setImageBitmap(finalBmp);
                        }
                    });
                }else{
                    Log.e("call","code("+connection.getResponseCode()+")"+";msg:"+connection.getResponseMessage());
                }
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
            Log.e("call","close");

            }
        }
    }

}
