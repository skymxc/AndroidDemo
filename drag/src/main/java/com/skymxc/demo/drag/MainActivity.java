package com.skymxc.demo.drag;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnActionListener {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        MyAdapter adapter = new MyAdapter(this);
        adapter.setOnActionListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onDelete(String item, int position) {
        Toast.makeText(this, "delete->" + item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdate(String item, int position) {

        Toast.makeText(this, "update->" + item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(String item, int position) {
        Toast.makeText(this, "onClick->" + item, Toast.LENGTH_SHORT).show();
    }
}
