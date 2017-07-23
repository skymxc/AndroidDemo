package com.skymxc.drag.contentprovider;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.skymxc.drag.contentprovider.util.StudentProvider;

public class MainActivity extends AppCompatActivity {

   private ContentObserver observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化观察者
        observer = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                Log.e("MainActivity","======数据改变了===");
            }
        };

        Uri uri = Uri.parse("content://"+StudentProvider.AUTHORITY+"/student");
        //为student 注册观察者
        /**
         * parameter1 观察的uri
         * parameter2 uri的后代是否连带 观察
         * parameter3 observer
         */
        getContentResolver().registerContentObserver(uri,true,observer);

    }
}
