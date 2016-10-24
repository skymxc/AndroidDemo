package com.skymxc.demo.recyclerview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

/**
 * Created by sky-mxc
 */
public class MyItemAnimator extends SimpleItemAnimator {
    private static final String TAG = "MyItemAnimator";

    private ArrayList<RecyclerView.ViewHolder> mRemoveList = new ArrayList<>();

    /**
     * 移除时调用 返回值决定是否需要执行动画
     * @param holder
     * @return
     */
    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {

        mRemoveList.add(holder);
        Log.e(TAG, "animateRemove: ");

        return true;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        Log.e(TAG, "animateAdd: ");
        return false;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        Log.e(TAG, "animateMove: ");
        return false;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        Log.e(TAG, "animateChange: ");
        return false;
    }

    @Override
    public void runPendingAnimations() {
        Log.e(TAG, "runPendingAnimations: ");

        if (!mRemoveList.isEmpty()){
            for (final RecyclerView.ViewHolder holder :mRemoveList){
                     View view = holder.itemView;


                    AnimatorSet set = new AnimatorSet();
                    set.setInterpolator(new LinearInterpolator());
                    set.setDuration(2000);
                    ArrayList<Animator> items = new ArrayList<>();
                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view,"scaleX",1,0f);
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view,"scaleY",1,0f);
                    ObjectAnimator alpha = ObjectAnimator.ofFloat(view,"alpha",1,0.5f);
                    ObjectAnimator transalteX = ObjectAnimator.ofFloat(view,"translateX",1f,20f);
                     //  items.add(scaleX);
                   // items.add(scaleY);
                  //  items.add(alpha);
                    items.add(transalteX);
                    set.playTogether(items);
                    set.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            dispatchRemoveFinished(holder);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    set.start();
            }
        }
        mRemoveList.clear();
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        Log.e(TAG, "endAnimation: ");
    }

    @Override
    public void endAnimations() {
        Log.e(TAG, "endAnimations: ");
    }

    @Override
    public boolean isRunning() {
        Log.e(TAG, "isRunning: return="+!mRemoveList.isEmpty());
        return !mRemoveList.isEmpty();
    }
}
