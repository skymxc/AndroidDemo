package com.samson.scroll;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import cn.bingoogolapple.bgabanner.BGABanner;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;
import com.jaredrummler.materialspinner.MaterialSpinnerBaseAdapter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyScrollView.MyOnScrollListener {

    private static  final  String TAG = "MainActivity";
    //6大模块
    public GridLayout riversLakesContent;

    //列表
    public LinearLayout riversLakesRlTitle1;
    //列表
    public LinearLayout riversLakesRlTitle;
    //滑动图片
    BGABanner bannerGuideContent;
    //
    MyScrollView riversLakesScroll;

    protected ListView list;

    //标题距离头部的高度
    private int bottom;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottom = getResources().getDimensionPixelOffset(R.dimen.dp150);
        riversLakesContent = (GridLayout) findViewById(R.id.rivers_lakes_content);
        riversLakesRlTitle1 = (LinearLayout) findViewById(R.id.rivers_lakes_rl_title1);
        riversLakesRlTitle = (LinearLayout) findViewById(R.id.rivers_lakes_rl_title);
        bannerGuideContent = (BGABanner) findViewById(R.id.banner_guide_content);
        riversLakesScroll = (MyScrollView) findViewById(R.id.rivers_lakes_scroll);
        list = (ListView) findViewById(R.id.list_view);
        riversLakesScroll.setOnScrollListener(this);

        List<String> arrays = new LinkedList<>();
        for (int i =0;i<=20;i++){
            arrays.add("测试数据++++++++"+i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,android.R.id.text1,arrays);
        list.setAdapter(adapter);

        //设置头部滚动的图片
        bannerGuideContent.setData(Arrays.asList(R.mipmap.page1, R.mipmap.page2, R.mipmap.page3), null);
        //设置适配器
        bannerGuideContent.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                ((ImageView) view).setImageResource((Integer) model);
            }
        });
        //ScollView显示不是顶部的解决办法
        bannerGuideContent.setFocusable(true);
        bannerGuideContent.setFocusableInTouchMode(true);
        bannerGuideContent.requestFocus();
    }


    @Override
    public void onScroll(int scorllY) {
        Log.e(TAG, "onScroll: scorlly="+scorllY+"bottom="+bottom);
        if (scorllY >= bottom) {
            if (riversLakesRlTitle != null) {
                if (riversLakesRlTitle.getChildCount() > 0) {
                    riversLakesRlTitle.removeView(riversLakesContent);
                }
                if (riversLakesRlTitle1.getChildCount() <= 0) {
                    riversLakesRlTitle1.addView(riversLakesContent);
                }
                flag = true;
            }
        } else {
            if (flag) {
                riversLakesRlTitle1.removeView(riversLakesContent);
                riversLakesRlTitle.addView(riversLakesContent);
                flag = false;
            }
        }
    }

}
