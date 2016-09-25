package com.skymxc.demo.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.skymxc.demo.fragment.adapter.FragmentAdapter;
import com.skymxc.demo.fragment.fragment.AttachFragment;
import com.skymxc.demo.fragment.fragment.ContactFragment;
import com.skymxc.demo.fragment.fragment.DiscoverFragment;
import com.skymxc.demo.fragment.fragment.MsgFragment;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager pager;
    private TabLayout tabLayoutMenu;

    private FragmentAdapter adapter ;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.container);
        tabLayoutMenu = (TabLayout) findViewById(R.id.tab_layout_menu);
        initTabLayoutAndPager();


        //想使用自己的布局就得 通过 监听进行关联
        bindPagerAndTab();
    }

    private void bindPagerAndTab() {
        tabLayoutMenu.addTab(tabLayoutMenu.newTab().setCustomView(createView(R.drawable.selector_bg,"发现")));
        tabLayoutMenu.addTab(tabLayoutMenu.newTab().setCustomView(createView(R.drawable.selector_bg_attach,"关注")));
        tabLayoutMenu.addTab(tabLayoutMenu.newTab().setCustomView(createView(R.drawable.selector_bg_message,"消息")));
        tabLayoutMenu.addTab(tabLayoutMenu.newTab().setCustomView(createView(R.drawable.selector_bg_info,"我的")));
        tabLayoutMenu.setSelectedTabIndicatorHeight(0);//去除指示器

        tabLayoutMenu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            /**
             * 选中tab后触发
             * @param tab 选中的tab
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //与pager 关联
                pager.setCurrentItem(tab.getPosition(),true);
            }

            /**
             * 退出选中状态时触发
             * @param tab 退出选中的tab
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            /**
             * 重复选择时触发
             * @param tab 被 选择的tab
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //关联 TabLayout
                tabLayoutMenu.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTabLayoutAndPager() {
        //关联 viewPager 使用关联后 tab就会自动去获取pager的title，使用addTab就会无效
        tabLayout.setupWithViewPager(pager);

        fragments = new ArrayList<>();
        fragments.add(new DiscoverFragment());
        fragments.add(new AttachFragment());
        fragments.add(new MsgFragment());
        fragments.add(new ContactFragment());
        adapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
        pager.setAdapter(adapter);
    }


    private View createView(int icon, String tab){
        View view = getLayoutInflater().inflate(R.layout.fragment_tab_discover,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        TextView title = (TextView) view.findViewById(R.id.title);
        imageView.setImageResource(icon);
        title.setText(tab);
        return  view;
    }
}
