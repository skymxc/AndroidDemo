package com.skymxc.demo.drag;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by mxc on 2017/7/23.
 * description:
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String[] items = new String[]{"item1", "item2", "item3", "item4", "item5",
            "item6", "item7", "item8", "item9", "item10", "item11",
            "item12", "item13", "item14", "item15", "item16"};

    private LayoutInflater inflater;
    private OnActionListener onActionListener;

    public MyAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.layout_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            String item = items[position];
            viewHolder.tvContent.setText(item);
            viewHolder.actionUpdate.setTag(position);
            viewHolder.actionDel.setTag(position);
            viewHolder.actionDel.setOnClickListener(onClickListener);
           viewHolder.tvContent.setTag(position);
            viewHolder.tvContent.setOnClickListener(onClickListener);
            viewHolder.actionUpdate.setOnClickListener(onClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            String item = items[position];
            switch (v.getId()) {
                case R.id.action_delete:
                    onActionListener.onDelete(item, position);
                    break;
                case R.id.action_update:
                    onActionListener.onUpdate(item, position);
                    break;
                case R.id.content:
                    onActionListener.onClick(item, position);
                    break;
            }
        }
    };

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvContent;
        TextView actionDel;
        TextView actionUpdate;

        public MyViewHolder(View itemView) {
            super(itemView);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(layoutParams);
            tvContent = (TextView) itemView.findViewById(R.id.content);
            actionDel = (TextView) itemView.findViewById(R.id.action_delete);
            actionUpdate = (TextView) itemView.findViewById(R.id.action_update);
        }
    }

    public interface OnActionListener {
        void onDelete(String item, int position);

        void onUpdate(String item, int position);

        void onClick(String item, int position);
    }
}
