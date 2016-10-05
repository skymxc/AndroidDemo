package com.skymxc.demo.animation;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.image);


    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()){
            case R.id.image:
                //FrameAnimation  帧动画
                ((AnimationDrawable)image.getBackground()).start();
                break;
            case R.id.anim_alpha_xml:
                //AlphaAnimation xml定义
                AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(this,R.anim.anim_alpha);
                view.startAnimation(alphaAnimation);
                break;
            case R.id.anim_alpha_code:
                //AlphaAnimation 代码创建
                AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.1f,0.8f);
                alphaAnimation1.setDuration(2000);
                alphaAnimation1.setStartOffset(100);
                alphaAnimation1.setFillAfter(true);
                alphaAnimation1.setRepeatCount(2);
                alphaAnimation1.setRepeatMode(Animation.REVERSE);
                view.startAnimation(alphaAnimation1);

                break;

            case R.id.anim_translation_xml:
                TranslateAnimation translateAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(this,R.anim.anim_translation);
                view.startAnimation(translateAnimation);
                break;
            case R.id.anim_translation_code:
                TranslateAnimation translateAnimation1 = new TranslateAnimation(-10,100,0,0);
                translateAnimation1.setInterpolator(new BounceInterpolator());
                translateAnimation1.setDuration(2000);
                view.startAnimation(translateAnimation1);
                break;
            case R.id.anim_rotate_xml:
                RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(this,R.anim.anim_rotate);
                view.startAnimation(rotate);
                break;
            case R.id.anim_rotate_code:
                RotateAnimation rotateAnimation = new RotateAnimation(10,45,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setInterpolator(new DecelerateInterpolator());
                rotateAnimation.setDuration(2000);
                rotateAnimation.setFillAfter(true);
                view.startAnimation(rotateAnimation);
                break;
            case R.id.anim_scale_xml:
                ScaleAnimation scale = (ScaleAnimation) AnimationUtils.loadAnimation(this,R.anim.anim_scale);
                view.startAnimation(scale);
                break;
            case R.id.anim_scale_code:
                ScaleAnimation scaleAnimation = new ScaleAnimation(0, 2, 0, 2, 100, 100);
                scaleAnimation.setDuration(2000);
                scaleAnimation.setInterpolator(new LinearInterpolator());
                view.startAnimation(scaleAnimation);
                break;
            case R.id.anim_set_xml:
                Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim_set);
                view.startAnimation(animation);
                break;
            case  R.id.anim_set_code:
                //子动画是否共用差值器
                AnimationSet set = new AnimationSet(true);
                set.addAnimation(new RotateAnimation(-180,0));
                set.addAnimation(new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f));
                set.setDuration(2000);
                set.setFillAfter(true);
                view.startAnimation(set);
                break;
            case R.id.animator_value_xml:
                Log.e("MainActivity","-===");
                ValueAnimator animator = (ValueAnimator) AnimatorInflater.loadAnimator(this,R.animator.animator_value);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        //intType
                      int value= (int) animation.getAnimatedValue();
                        image.setTranslationY(value);
                      //  image.setTranslationX(value);

                        //floatType
//                        float alpha = (float) animation.getAnimatedValue();
//                        image.setAlpha(alpha);

                    }
                });
                animator.start();
                break;
            case R.id.animator_value_code:
                //   ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f,0.5f,0f);
                PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofFloat("alpha",1f,0.5f);
                PropertyValuesHolder widthHolder = PropertyValuesHolder.ofInt("width",1,200);
                PropertyValuesHolder rotateHolder = PropertyValuesHolder.ofFloat("rotate",0,180);
                ValueAnimator valueAnimator = ValueAnimator.ofPropertyValuesHolder(alphaHolder,widthHolder,rotateHolder);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
//                       float value= (float) animation.getAnimatedValue();
//                        image.setAlpha(value);

                      float alpha= (float) animation.getAnimatedValue("alpha");
                        int width = (int) animation.getAnimatedValue("width");
                        float rotate = (float) animation.getAnimatedValue("rotate");
                        Log.e("MainActivity","alpha:"+alpha);
                        Log.e("MainActivity","width:"+width);

                        image.setAlpha(alpha);
                       image.setMaxWidth(width);
                        image.setMinimumWidth(width);
                        image.setRotation(rotate);
                    }
                });
                valueAnimator.setDuration(2000);
                valueAnimator.start();
                break;
            case R.id.animator_object_xml:
                Log.e("MainActivity","===animator_object_xml==");
                ObjectAnimator object = (ObjectAnimator) AnimatorInflater.loadAnimator(this,R.animator.animator_object);
                object.setTarget(image);
                object.start();
                //image.setTranslationX();
                break;
            case R.id.animator_object_code:
                Log.e("MainActivity","===animator_object_code==");
              //  ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(image,"alpha",1f,0.2f);
                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view,"backgroundColor", Color.RED,Color.YELLOW,Color.BLUE,Color.GREEN);
                objectAnimator.setDuration(2000);
                objectAnimator.setInterpolator(new BounceInterpolator());
                objectAnimator.start();
                break;
        }
    }
}
