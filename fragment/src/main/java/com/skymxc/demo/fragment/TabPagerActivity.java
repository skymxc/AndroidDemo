package com.skymxc.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.skymxc.demo.fragment.adapter.FragmentAdapter;
import com.skymxc.demo.fragment.fragment.AttachFragment;
import com.skymxc.demo.fragment.fragment.ContactFragment;
import com.skymxc.demo.fragment.fragment.DiscoverFragment;
import com.skymxc.demo.fragment.fragment.MsgFragment;

import java.util.ArrayList;
import java.util.List;

public class TabPagerActivity extends AppCompatActivity {

    private static final String TAG ="TabPagerActivity";

    private FragmentTabHost  tabHost;
    private ViewPager pager;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_pager);
        tabHost = (FragmentTabHost) findViewById(R.id.tab_host);
        pager = (ViewPager) findViewById(R.id.pager);
        //初始化TabHost
        initTabHost();

        //初始化pager
        initPager();

        //添加监听关联TabHost和viewPager
        bindTabAndPager();
    }

    /**
     * 为TabHost和viewPager 添加监听 让其联动
     */
    private void bindTabAndPager() {
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            /**
             *  tab改变后
             * @param tabId 当前tab的tag
             */
            @Override
            public void onTabChanged(String tabId) {
                log("vonTabChanged:"+tabId);
               int position = tabHost.getCurrentTab();
                pager.setCurrentItem(position,true);
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * 页面滑动 触发
             * @param position 当前显得第一个页面的索引，当滑动出现时屏幕就会显示两个pager， 向右滑 position为当前-1（左边的pager就显示出来了），向左滑position为当前（右面就显出来了），
             * @param positionOffset 0-1之间 position的偏移量 从原始位置的偏移量
             * @param positionOffsetPixels 从position偏移的像素值 从原始位置的便宜像素
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            log("onPageScrolled=============position:"+position+"====positionOffset:"+positionOffset+"====positionOffsetPixels:"+positionOffsetPixels);
            }

            /**
             * 页面选中后
             * @param position 当前页面的index
             */
            @Override
            public void onPageSelected(int position) {
                tabHost.setCurrentTab(position);
                log("onPageSelected==========:position:"+position);
            }

            /**
             * 页面滑动状态改变时触发
             * @param state 当前滑动状态 共三个状态值
             */
            @Override
            public void onPageScrollStateChanged(int state) {

                String stateStr="";
                switch (state){
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        stateStr="正在拖动";
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        stateStr="正在去往最终位置 即将到达最终位置";
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        stateStr="滑动停止，当前页面充满屏幕";
                        break;
                }
                log("onPageScrollStateChanged========stateCode:"+state+"====state:"+stateStr);
            }

        });
    }

    /**
     * 初始化 pager 绑定适配器
     */
    private void initPager() {
        fragments = new ArrayList<>();
        fragments.add(new DiscoverFragment());
        fragments.add(new AttachFragment());
        fragments.add(new MsgFragment());
        fragments.add(new ContactFragment());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
        pager.setAdapter(adapter);
    }

    /**
     * 初始化 TabHost
     */
    private void initTabHost() {
        tabHost.setup(this,getSupportFragmentManager(), R.id.pager);
        tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.addTab(tabHost.newTabSpec("discover").setIndicator(createView(R.drawable.selector_bg,"发现")), DiscoverFragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("attach").setIndicator(createView(R.drawable.selector_bg_attach,"关注")), AttachFragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("message").setIndicator(createView(R.drawable.selector_bg_message,"消息")), MsgFragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("info").setIndicator(createView(R.drawable.selector_bg_info,"我的")), ContactFragment.class,null);
    }

    /**
     * 返回view
     * @param icon
     * @param tab
     * @return
     */
    private View createView(int icon,String tab){
        View view = getLayoutInflater().inflate(R.layout.fragment_tab_discover,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        TextView  title = (TextView) view.findViewById(R.id.title);
        imageView.setImageResource(icon);
        title.setText(tab);
        return  view;
    }

    private void log(String log){
        Log.e(TAG,"="+log+"=");
    }

}
