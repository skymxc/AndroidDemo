package com.skymxc.demo.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * 消息机制
 */
public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSupportActionBar().setTitle("消息机制");
        new ThreadA().start();
        new ThreadB().start();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_to_work:  //从主线发送给工作线程一个消息


                //创建一个消息
                Message msg = new Message();
                msg.what = 100; //标识你这消息想要干啥
                msg.obj = Thread.currentThread().getName();

                /**
                 * 因为handler 里持有所在线程的Looper，所有handler发送的消息会在所在线程中执行
                 */
                //发送给A线程
                handlerA.sendMessage(msg);
                //发送给B线程 1s 后
                Message message1 = handlerB.obtainMessage(100, Thread.currentThread().getName());
                handlerB.sendMessageDelayed(message1,1000);
                break;
            case R.id.work_to_work: //A 发送给B
                /**
                 * 给A线程发送一个信号，
                 * A接受到信号就会给B发送一个消息了
                 */
                Message message = handlerA.obtainMessage(101);
                message.sendToTarget();
                break;
            case R.id.work_to_main: //B 给main线程发送一个消息
                /**
                 * 给B发送一个信息
                 * B接到信号后就会给面发送消息了
                 */
                handlerB.obtainMessage(101).sendToTarget();
                break;
        }
    }

    /**
     * Main 的handler
     * 程序启动的时候就为Main线程创建了Looper
     */
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.e("handleMessage", Thread.currentThread().getName() + ";src->" + msg.obj);
            return false;
        }
    });

    Handler handlerA = null;
    Handler handlerB = null;

    class ThreadA extends Thread implements Handler.Callback {

        public ThreadA() {
            super("ThreadA");
        }

        @Override
        public void run() {
            //创建此线程的Looper对象 ，一个线程只能有一个，所以此方法只能调用一次
            Looper.prepare();
            handlerA = new Handler(this);
            //开始循环遍历
            Looper.loop();
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    Log.e("handleMessage", Thread.currentThread().getName() + ";src->" + msg.obj);
                    break;
                case 101:   //给B发送一个消息
                    Message message =new Message();
                    message.what = 100;
                    message.obj = Thread.currentThread().getName();
                    handlerB.sendMessage(message);
                    break;
            }
            return false;
        }
    }

    class ThreadB extends Thread implements Handler.Callback {

        public ThreadB() {
            super("ThreadB");
        }

        @Override
        public void run() {
            Looper.prepare();
            handlerB = new Handler(this);
            Looper.loop();

        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    Log.e("handleMessage", Thread.currentThread().getName() + ";src->" + msg.obj);
                    break;
                case 101:   //给main发送一个消息
                    Message message = new Message();
                    message.what = 100;
                    message.obj = Thread.currentThread().getName();
                    handler.sendMessage(message);
                    break;
            }
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        handlerB.removeCallbacksAndMessages(null);
        handlerA.removeCallbacksAndMessages(null);
    }
}
