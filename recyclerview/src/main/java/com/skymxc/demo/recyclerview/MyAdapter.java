package com.skymxc.demo.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skymxc.demo.recyclerview.entity.Item;

import java.util.List;

/**
 * Created by sky-mxc
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MyAdapter";

    private Context mContext;
    private List<Item> items;
    private LayoutInflater lif ;
    private View.OnClickListener onClickListener;
    private View.OnLongClickListener onLongClickListener;

    public MyAdapter(Context context, List<Item> items) {
        this.mContext = context;
        this.items = items;
        lif = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = lif.inflate(R.layout.layout_item,null);
            //设置随机高度
            int height= (int) ((Math.random()*(200-100))+100);
            Log.i(TAG, "onCreateViewHolder: height="+height);
            v.setMinimumHeight(height);

            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            v.setLayoutParams(lp);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Item item = items.get(position);
        if (holder instanceof  ItemViewHolder){
            ItemViewHolder ih = (ItemViewHolder) holder;
            ih.msg.setText(item.getMsg());
            ih.name.setText(item.getName());
            ih.icon.setImageResource(mContext.getResources().getIdentifier(item.getIcon(),"mipmap",mContext.getPackageName()));
        }
    }

    @Override
    public int getItemCount() {
        return items == null?0:items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    public void removeAt(int position){
        Log.e(TAG, "removeAt: position="+position);
        if (position<items.size()){
            items.remove(position);
            notifyItemRemoved(position);
        }
    }


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        TextView msg;



        public ItemViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            name = (TextView) itemView.findViewById(R.id.name);
            msg = (TextView) itemView.findViewById(R.id.msg);
            this.itemView.setOnClickListener(onClickListener);
            this.itemView.setOnLongClickListener(onLongClickListener);
        }
    }
}
