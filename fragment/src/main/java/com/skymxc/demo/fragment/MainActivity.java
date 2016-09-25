package com.skymxc.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_tabhost:
                 intent = new Intent(this,FragmentTabHostActivity.class);

                break;
            case R.id.fragmnet_tab_pager:
                intent = new Intent(this,TabPagerActivity.class);
                break;
            case R.id.tab_layout_pager:
                intent = new Intent(this,TabLayoutActivity.class);
                break;
        }
        startActivity(intent);
    }
}
