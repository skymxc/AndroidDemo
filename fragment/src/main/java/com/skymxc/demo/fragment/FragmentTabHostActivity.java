package com.skymxc.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.skymxc.demo.fragment.fragment.AttachFragment;
import com.skymxc.demo.fragment.fragment.ContactFragment;
import com.skymxc.demo.fragment.fragment.DiscoverFragment;
import com.skymxc.demo.fragment.fragment.MsgFragment;

public class FragmentTabHostActivity extends AppCompatActivity {

    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_tab_host);
        tabHost = (FragmentTabHost) findViewById(R.id.tab_host);
        //初始化设置
        tabHost.setup(this,getSupportFragmentManager(),R.id.container);
        tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.addTab(tabHost.newTabSpec("discover").setIndicator(createView(R.drawable.selector_bg,"发现")), DiscoverFragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("attach").setIndicator(createView(R.drawable.selector_bg_attach,"关注")), AttachFragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("message").setIndicator(createView(R.drawable.selector_bg_message,"消息")), MsgFragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("info").setIndicator(createView(R.drawable.selector_bg_info,"我的")), ContactFragment.class,null);
    }

    private View createView(int icon,String tab){
        View view = getLayoutInflater().inflate(R.layout.fragment_tab_discover,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        TextView title = (TextView) view.findViewById(R.id.title);
        imageView.setImageResource(icon);
        title.setText(tab);
        return  view;
    }
}
