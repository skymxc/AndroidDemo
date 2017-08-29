package com.skymxc.demo.down;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skymxc.demo.down.events.EventComplete;
import com.skymxc.demo.down.events.EventError;
import com.skymxc.demo.down.events.EventProgress;
import com.skymxc.demo.down.events.EventStart;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.crypto.interfaces.PBEKey;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    private static File mkdir;
    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PrefUtil.init(getApplicationContext());
        recyclerView =(RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        initData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (i!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},200);
            }else{
                initMkdir();
            }
        }else{
            initMkdir();
        }
    }

    private void initMkdir(){
        mkdir = new File(Environment.getExternalStorageDirectory(),"skymxc");
        if (!mkdir.exists()){
            mkdir.mkdirs();
        }
    }

    private void initData() {
        List<String> data = new ArrayList<>();
        data.add("http://jxb.sintoon.cn:80/audio/%E5%8C%97%E4%BA%AC%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%AE%9E%E9%AA%8C%E5%B0%8F%E5%AD%A6/2016%E7%BA%A7/10%E7%8F%AD/mxc%E5%AD%9F%E7%A5%A5%E8%B6%85/130b12b1-d0a4-4380-9cd1-58047205cd50.amr");
        data.add("http://jxb.sintoon.cn:80/audio/%E5%8C%97%E4%BA%AC%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%AE%9E%E9%AA%8C%E5%B0%8F%E5%AD%A6/2016%E7%BA%A7/10%E7%8F%AD/mxc%E5%AD%9F%E7%A5%A5%E8%B6%85/38939e35-6b75-4b20-90d5-20385adcaea3.amr");
        data.add("http://jxb.sintoon.cn:80/audio/%E5%8C%97%E4%BA%AC%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%AE%9E%E9%AA%8C%E5%B0%8F%E5%AD%A6/2016%E7%BA%A7/10%E7%8F%AD/mxc%E5%AD%9F%E7%A5%A5%E8%B6%85/da4eff3f-17ef-4c87-87cb-98409ca846b1.amr");
        data.add("http://jxb.sintoon.cn:80/audio/%E5%8C%97%E4%BA%AC%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%AE%9E%E9%AA%8C%E5%B0%8F%E5%AD%A6/2016%E7%BA%A7/10%E7%8F%AD/mxc%E5%AD%9F%E7%A5%A5%E8%B6%85/6fe6a923-3716-42b5-b369-1a50aca3928d.amr");
        data.add("http://jxb.sintoon.cn:80/audio/%E5%8C%97%E4%BA%AC%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%AE%9E%E9%AA%8C%E5%B0%8F%E5%AD%A6/2016%E7%BA%A7/10%E7%8F%AD/mxc%E5%AD%9F%E7%A5%A5%E8%B6%85/7fb0996f-a94c-4022-87f6-0d5ed1742c28.amr");
        data.add("http://jxb.sintoon.cn:80/audio/%E5%8C%97%E4%BA%AC%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%AE%9E%E9%AA%8C%E5%B0%8F%E5%AD%A6/2016%E7%BA%A7/10%E7%8F%AD/mxc%E5%AD%9F%E7%A5%A5%E8%B6%85/09a2edaa-4ea3-457e-b8d7-50a97003dd3f.amr");
        data.add("http://jxb.sintoon.cn:80/audio/%E5%8C%97%E4%BA%AC%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%AE%9E%E9%AA%8C%E5%B0%8F%E5%AD%A6/Teacher/teacher%E8%80%81%E5%B8%88%E6%B5%8B%E8%AF%95/f553a2cc-f252-4478-85cb-967a2cbff25f.amr");
        data.add("http://jxb.sintoon.cn:80/audio/%E5%8C%97%E4%BA%AC%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%AE%9E%E9%AA%8C%E5%B0%8F%E5%AD%A6/Teacher/teacher%E8%80%81%E5%B8%88%E6%B5%8B%E8%AF%95/2842872e-7ab7-4122-84f7-b094e9d81b9e.amr");
        data.add("http://jxb.sintoon.cn:80/audio/%E5%8C%97%E4%BA%AC%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%AE%9E%E9%AA%8C%E5%B0%8F%E5%AD%A6/2016%E7%BA%A7/10%E7%8F%AD/mxc%E5%AD%9F%E7%A5%A5%E8%B6%85/67ad3188-6df7-4cc8-b9f6-c1f141da591f.amr");
        data.add("http://jxb.sintoon.cn:80/audio/%E5%8C%97%E4%BA%AC%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%AE%9E%E9%AA%8C%E5%B0%8F%E5%AD%A6/2016%E7%BA%A7/10%E7%8F%AD/mxc%E5%AD%9F%E7%A5%A5%E8%B6%85/38bc6fb9-c26b-42ea-a7a7-47d5f4304a77.amr");
        data.add("http://jxb.sintoon.cn:80/audio/%E5%8C%97%E4%BA%AC%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%AE%9E%E9%AA%8C%E5%B0%8F%E5%AD%A6/2016%E7%BA%A7/10%E7%8F%AD/mxc%E5%AD%9F%E7%A5%A5%E8%B6%85/da488ac5-e867-43b7-8a50-99375e6a94a3.amr");
        data.add("http://jxb.sintoon.cn:80/audio/%E5%8C%97%E4%BA%AC%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%AE%9E%E9%AA%8C%E5%B0%8F%E5%AD%A6/2016%E7%BA%A7/10%E7%8F%AD/mxc%E5%AD%9F%E7%A5%A5%E8%B6%85/4868b659-8420-4d84-80ce-e0e53b576b8e.amr");
        data.add("http://jxb.sintoon.cn:80/audio/%E5%8C%97%E4%BA%AC%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%AE%9E%E9%AA%8C%E5%B0%8F%E5%AD%A6/2016%E7%BA%A7/10%E7%8F%AD/mxc%E5%AD%9F%E7%A5%A5%E8%B6%85/f7e2ee86-7b34-4fb8-a72e-94dd250ad77f.amr");
        data.add("http://jxb.sintoon.cn:80/audio/%E5%8C%97%E4%BA%AC%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%AE%9E%E9%AA%8C%E5%B0%8F%E5%AD%A6/2016%E7%BA%A7/10%E7%8F%AD/mxc%E5%AD%9F%E7%A5%A5%E8%B6%85/b9f255f9-d3c7-4789-96a8-168a31cb105e.amr");
        data.add("http://jxb.sintoon.cn:80/audio/%E5%8C%97%E4%BA%AC%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%AE%9E%E9%AA%8C%E5%B0%8F%E5%AD%A6/2016%E7%BA%A7/10%E7%8F%AD/mxc%E5%AD%9F%E7%A5%A5%E8%B6%85/d56efbc9-7cbe-460c-84ee-12417d72d23a.amr");
        data.add("http://jxb.sintoon.cn:80/audio/%E5%8C%97%E4%BA%AC%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%AE%9E%E9%AA%8C%E5%B0%8F%E5%AD%A6/2016%E7%BA%A7/10%E7%8F%AD/mxc%E5%AD%9F%E7%A5%A5%E8%B6%85/ee6538d5-20ba-436b-bcc6-50e1c72531e6.amr");
         myAdapter = new MyAdapter(data,this);
        recyclerView.setAdapter(myAdapter);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
            initMkdir();
        }
    }

    @Override
    protected void onDestroy() {
        myAdapter.destroy();
        super.onDestroy();
    }

    static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<String> arrays = new ArrayList<>();
        private LayoutInflater inflater;
        private Context context;
        public MyAdapter(List<String> data, Context context){
            this.arrays = data;
            this.context = context.getApplicationContext();
            inflater = LayoutInflater.from(context.getApplicationContext());
            EventBus.getDefault().register(this);
        }

        public void destroy(){
            EventBus.getDefault().unregister(this);
        }
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(inflater.inflate(R.layout.layout_item_pre_down,null));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {
            if (payloads.size()==0){
                holder.tvUrl.setText(arrays.get(position));
                holder.itemView.setTag(position);
                holder.itemView.setOnClickListener(onClickListener);
                holder.pb.setVisibility(View.GONE);
                return;
            }
            Message msg  = (Message) payloads.get(0);
            switch (msg.what){
                case 0:
                    holder.pb.setVisibility(View.VISIBLE);
                    holder.pb.setMax(msg.arg1);
                    break;
                case 1:
                    holder.pb.setVisibility(View.GONE);
                    holder.pb.setMax(0);
                    break;
                case 2:
                    holder.pb.setProgress(msg.arg1);
                    break;
                case 3:
                    holder.pb.setVisibility(View.GONE);
                    holder.pb.setMax(0);
                    holder.tvUrl.setText(holder.tvUrl.getText()+";出错");
                    break;
            }
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return arrays==null?0:arrays.size();
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                String url = arrays.get(position);
                String name = url.substring(url.lastIndexOf("/")+1, url.length());
                Log.e("onClick","name:"+name+"\nurl->"+url);
                File file = new File(mkdir,name);
                DownIntentService.startActionAdd(context,url,file.getPath());
            }
        };
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onStartEvent(EventStart event){
            int i = searchIndex(event.getUrl());
            Message msg = new Message();
            msg.what =0;
//               msg.obj = event;
            msg.arg1 = event.getTotal();
            notifyItemChanged(i,msg);
        }
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onCompleteEvent(EventComplete event){
            int i = searchIndex(event.getUrl());
            Message msg = new Message();
            msg.what =1;
//            msg.obj= event;
            notifyItemChanged(i,msg);
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onProgressEvent(EventProgress event){
            int i = searchIndex(event.getUrl());
            Message msg = new Message();
            msg.what = 2;
            msg.arg1 = event.getProgress();
            msg.arg2= event.getTotal();
//            msg.obj= event;
            notifyItemChanged(i,msg);
        }
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onErrorEvent(EventError event){
            int i = searchIndex(event.getUrl());
            Message msg = new Message();
            msg.what = 3;
//            msg.obj = event;
            notifyItemChanged(i,msg);
        }
       private int searchIndex(String url){
           int index =-1;
           int size = arrays.size();
           for (int i=0;i<size;i++){
               if (arrays.get(i).equals(url)){
                   index = i;
                   break;
               }
           }
           return index;
       }
        static class MyViewHolder extends RecyclerView.ViewHolder{

            TextView tvUrl;
            ProgressBar pb;
            public MyViewHolder(View itemView) {
                super(itemView);
                tvUrl = (TextView) itemView.findViewById(R.id.url);
                pb= (ProgressBar) itemView.findViewById(R.id.progress);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                itemView.setLayoutParams(lp);
            }
        }
    }
}
