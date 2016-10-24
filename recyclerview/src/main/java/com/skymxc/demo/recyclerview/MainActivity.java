package com.skymxc.demo.recyclerview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //创建适配器
    }

    @Override
    public void onClick(View v) {
        Intent intent =null;
        switch (v.getId()){
            case R.id.linear:
                intent = new Intent(this,LinearActivity.class);

                break;
        }
        startActivity(intent);
    }
}
