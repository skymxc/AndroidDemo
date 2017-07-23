package com.skymxc.drag.recyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.skymxc.drag.recyclerview.entity.Item;

import java.util.ArrayList;
import java.util.List;

public class LinearActivity extends AppCompatActivity implements View.OnClickListener ,View.OnLongClickListener{

    private static final String TAG ="LinearActivity";

    private RecyclerView recycler;
    private MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear);
        recycler=(RecyclerView)findViewById(R.id.recycler);
        //布局管理器 线性布局
        LinearLayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        //网格布局
        GridLayoutManager gm = new GridLayoutManager(this,2);
        //瀑布式布局
        StaggeredGridLayoutManager sgm = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        sgm.setAutoMeasureEnabled(true);
        recycler.setLayoutManager(lm);

        adapter = new MyAdapter(this,getItems());
        recycler.setAdapter(adapter);

        //设置监听
        adapter.setOnClickListener(this);
        adapter.setOnLongClickListener(this);


        //设置子项装饰器
        recycler.addItemDecoration(new MyDecoration(this));

        //动画
        recycler.setItemAnimator(new MyItemAnimator());
    }

    /**
     * 获取 数据集合
     * @return
     */
    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        for (int i =0;i<=20;i++){
            Item item = new Item();
            item.setMsg("嗯好吧");
            item.setName("小——"+i);
            item.setIcon("fae");
            if (i%5==0){
                item.setType(1);
                item.setIcon("faa");
            }else{
                item.setType(0);
            }
            items.add(item);
        }
        return items;
    }

    @Override
    public void onClick(View v) {
        //得到 当前itemView的下标
        int position = recycler.getChildAdapterPosition(v);
        Toast.makeText(this, "==position:"+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        int position = recycler.getChildAdapterPosition(v);
        adapter.removeAt(position);
       // Log.e(TAG, "onLongClick: size="+recycler.getChildCount());
        return true;
    }
}
