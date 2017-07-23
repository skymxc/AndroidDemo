package com.skymxc.drag.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by sky-mxc
 */
public class MyDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "MyDecoration";
    private Context mContext;
    private Paint paint;
    private Drawable drawable;
    public MyDecoration(Context context){
        paint = new Paint();
        paint.setColor(Color.GRAY|Color.GREEN);
        paint.setAntiAlias(true);//抗锯齿
        mContext = context;
      drawable = mContext.getResources().getDrawable(R.drawable.ewm);
    }

    /**
     * 在RecyclerView的上层绘制
     * 当 RecyclerView 的视图 改变时 才会调用此方法 去绘制
     * @param c 画布
     * @param parent RecyclerView
     * @param state 状态
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        Log.i(TAG, "onDrawOver: ");

        //获取到所有的子项
      int count=  parent.getChildCount();

        for (int i =0;i<count;i++) {
            //拿到 子项视图
            View v = parent.getChildAt(i);
            //拿到 当前view在 RecyclerView中的position
            int position = parent.getChildLayoutPosition(v);
            //根据position 拿到当前此ItemView的类型
//            int type = parent.getAdapter().getItemViewType(position);
//            if (type == 0) {
                //在子项视图左上角绘制
                drawable.setBounds(v.getLeft()+5, v.getTop()+10, v.getLeft() + 54, v.getTop() + 34);
                //绘制
                drawable.draw(c);
//            }
        }


    }

    /**
     * 在 RecyclerView 的下层绘制
     *
     * @param c 画布
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        Log.i(TAG, "onDraw: ");
        //拿到子项的总数
        int count = parent.getChildCount();
        for (int i=0;i<count;i++){
            //拿到childView
            View childView = parent.getChildAt(i);
            //拿到shape 图形
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.shape);
            //设置 shape 图像的边界
            drawable.setBounds(childView.getLeft()-9,childView.getTop()-9,childView.getRight()+9,childView.getBottom()+9);
            //绘制
            drawable.draw(c);
        }

    }

    /**
     * 设置四边的偏移量
     * 每显示一个 itemView 就会调用一次
     * @param outRect 对子项的四边边距的封装 的一个矩形
     * @param view 子视图
     * @param parent RecyclerView自身
     * @param state  RecyclerView的状态 不包含滑动状态
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //为 item 边增加10像素 间距
       outRect.set(3,2,2,2);
        Log.i(TAG, "getItemOffsets: ");

    }




}
