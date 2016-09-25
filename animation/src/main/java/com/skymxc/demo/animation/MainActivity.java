package com.skymxc.demo.animation;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.image);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image:
                //FrameAnimation  帧动画
                ((AnimationDrawable)image.getBackground()).start();
                break;
        }
    }
}
