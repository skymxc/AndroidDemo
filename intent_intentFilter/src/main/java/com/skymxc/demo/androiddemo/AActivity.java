package com.skymxc.demo.androiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        Intent intent = getIntent();//获取启动此activity的intent
        String origin = intent.getStringExtra("origin");
        TextView tv = (TextView) findViewById(R.id.text);
        tv.setText("origin："+origin);
    }
}
