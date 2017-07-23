package com.skymxc.drag.menus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv;
    private Button btMenu;
    private RelativeLayout root;
    private Button btWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv);
        btMenu = (Button) findViewById(R.id.popup_menu);
        root = (RelativeLayout) findViewById(R.id.root);
        btWindow = (Button) findViewById(R.id.popup_window);
        ininData();
        registerForContextMenu(lv);
    }
    private void ininData(){
        List<Map<String,String>> data = new LinkedList<>();
        Map<String,String> map1 = new HashMap<>();
        map1.put("text1","张三");
        map1.put("text2","1班");
        Map<String,String> map2 = new HashMap<>();
        map2.put("text1","李四");
        map2.put("text2","2班");
        data.add(map1);
        data.add(map2);
        SimpleAdapter adapter = new SimpleAdapter(this,data,android.R.layout.simple_list_item_2,new String[]{"text1","text2"},new int[]{android.R.id.text1,android.R.id.text2});
        lv.setAdapter(adapter);
    }

    /**
     * 创建 选项菜单
     * @param menu activity的菜单
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * 将我们定义的菜单文件加载进来 ，设置给activity的menu
         */
        getMenuInflater().inflate(R.menu.option_menu,menu);
        //通过代码添加
        /**
         * 参数1 所属的组 不分组使用 Menu.NONE
         * 参数2 id 可以在 目录文件下定义id文件
         * 参数3 顺序
         * 参数4 title
         */
        menu.add(Menu.NONE,R.id.item_change,menu.size()+1,"切换");
        return true;
    }

    /**
     * 选项菜单被点击事件处理
     * @param item 被点击的菜单项
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String action ="";
        switch (item.getItemId()){
            case R.id.feedback:
                action="反馈";
                break;
            case R.id.logout:
                action="注销";
                break;
            case R.id.set_account:
                action="账号设置";
                break;
            case R.id.set_general:
                action="通用设置";
                break;
        }
        Toast.makeText(this,"点击了 "+action+" 项",Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("选择操作");
        menu.add(Menu.NONE,R.id.context_remove,0,"删除");
        menu.add(Menu.NONE,R.id.context_update,1,"修改");
        // 也可以使用getMenuInflater 加载定义好的menu
       // getMenuInflater().inflate(R.menu.context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //用的是ListView
        AdapterView.AdapterContextMenuInfo  info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        View  view = info.targetView;//得到长按的View
       String name = ((TextView)view.findViewById(android.R.id.text1)).getText().toString();
        switch (item.getItemId()){
            case R.id.context_remove:
                Toast.makeText(this,item.getTitle()+"/"+name,Toast.LENGTH_SHORT).show();
                break;
            case R.id.context_update:
                Toast.makeText(this,item.getTitle()+""+name,Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.popup_menu:
                //参数1 上下文，参数2 要在谁的附近显示
                PopupMenu menu = new PopupMenu(this,btMenu);
              //  menu.getMenu().add()代码添加
                menu.getMenuInflater().inflate(R.menu.popup_menu,menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Log.e("Tag","=选择=:"+menuItem.getTitle());
                        return true;
                    }
                });

                menu.show();
                break;
            case R.id.popup_window:
                PopupWindow window  = new PopupWindow(this);
                //加载布局
                View cv = getLayoutInflater().inflate(R.layout.layout_popup_window,null);
                Button bt = (Button) cv.findViewById(R.id.ok);
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                //测量布局的宽高，PopupWindow不设置宽高是不会显示的
                cv.measure(View.MeasureSpec.makeMeasureSpec(1<<30-1,View.MeasureSpec.AT_MOST),View.MeasureSpec.makeMeasureSpec(1<<30-1,View.MeasureSpec.AT_MOST));
                int width = cv.getMeasuredWidth();
                int height = cv.getMeasuredHeight();
                window.setHeight(height);
                window.setWidth(width);
                window.setContentView(cv);
                window.setFocusable(true);//不设置获得焦点 点击别的地方 窗体不会消失
             //   window.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
             //   window.showAsDropDown(btWindow,20,20);    //在控件的附件显示  后边是偏移量
              //  window.showAtLocation(root, Gravity.CENTER|Gravity.BOTTOM,20,20); //在父容器的中间显示 后边是偏移量
                window.showAtLocation(root, Gravity.CENTER,20,20); //在父容器的中间显示 后边是偏移量


                break;
        }
    }


}
