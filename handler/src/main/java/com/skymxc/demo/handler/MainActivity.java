package com.skymxc.drag.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    private static final int PAGE =20;
    private static final String TAG ="MainActivity";
    private int current;
    private boolean touch=false;

    private ViewPager pager;

    private int[] images = new int[]{R.mipmap.h, R.mipmap.a,R.mipmap.d,R.mipmap.f,R.mipmap.h, R.mipmap.a};

    private MyAdapter myAdapter;
    private ProgressBar pb;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        //    Log.e(TAG,"handlerMessage.What:"+msg.what);
            switch (msg.what){
                case PAGE:
                    if (!touch) {
                        //viewpager的自动轮播设置
                        if (current < images.length - 1) {
                            current++;
                        } else {
                            current = 0;
                        }
                        movePager();
                    }
                    this.sendEmptyMessageDelayed(PAGE,3000);
                    break;
                case 30:
                    int progress = msg.getData().getInt("progress");
                    Log.e(TAG,"progress==:"+progress);
                    pb.setProgress(progress);
                    break;
            }
        }
    };

    private void movePager() {
        pager.setCurrentItem(current,true);
        if (current ==images.length-1){
            pager.setCurrentItem(1,false);
        }else if(current==0){
            pager.setCurrentItem(images.length-2,false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //"http://dl.360safe.com/inst.exe"
    }

    private void initView() {
        pb = (ProgressBar) findViewById(R.id.pb);
        pager = (ViewPager) findViewById(R.id.pager);
        mHandler.sendEmptyMessageDelayed(PAGE,3000);
        myAdapter = new MyAdapter();
        pager.setAdapter(myAdapter);
        pager.setCurrentItem(1);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current=position;
                movePager();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            switch (state){
                case ViewPager.SCROLL_STATE_DRAGGING:
                    touch=true;
                    break;
                case ViewPager.SCROLL_STATE_IDLE://滑动停止
                    current=pager.getCurrentItem();
                    movePager();
                    touch=false;
                    break;
            }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.down:
                DownLoadUtil down = new DownLoadUtil(mHandler);
                down.start();
                Log.e(TAG,"是否相等："+down.mHandler.getLooper());
                Log.e(TAG,"是否相等："+mHandler.getLooper());

                break;
        }
    }


    class  MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(images[position]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
