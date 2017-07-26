package com.skymxc.demo.drag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.to_drag:
                Intent intent = new Intent(this,TDragActivity.class);
                startActivity(intent);
                break;
            case R.id.to_swipe:
                intent = new Intent(this,TSwipeActivity.class);
                startActivity(intent);
                break;
        }
    }


}
