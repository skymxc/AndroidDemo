package com.skymxc.demo.thread;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class CreateActivity extends AppCompatActivity {

    TextView tvResult;

    int count = 0;
    StringBuffer buffer = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("创建线程");
        setContentView(R.layout.activity_create);
        tvResult = (TextView) findViewById(R.id.result);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                //启动线程
                MyThread thread0 = new MyThread();
                MyRunnable myRunnable = new MyRunnable();
                Thread thread1 = new Thread(myRunnable);
                thread0.start();
                thread1.start();

                break;
            case R.id.run:
                 thread0 = new MyThread();
                 myRunnable = new MyRunnable();
                 thread1 = new Thread(myRunnable);
                //这里就有区别了
                thread0.run();//新开了一个线程
                thread1.run();  //main线程
                break;
            case R.id.clean:
                buffer.delete(0,buffer.length());
                tvResult.setText(buffer);
                break;
        }
    }


    public void cal() {
        count++;
    }


    /**
     * 通过继承Thread 创建一个Thread
     */
    class MyThread extends Thread {

        public MyThread() {
        }

        /**
         * 重写run方法 JVM会自动调用此方法
         */
        @Override
        public void run() {
            super.run();
            cal();
            String str = "thread:" + getName() + ";count:" + count + ";MyThread\n";
            buffer.append(str);
            Log.e("MyThread",str);
            //在其他线程 访问UI
            tvResult.post(new Runnable() {
                @Override
                public void run() {
                    tvResult.setText(buffer.toString());
                }
            });
        }

        /**
         * 重载（Overload）run()方法 和普通的方法一样，并不会在该线程的start()方法被调用后被JVM自动运行
         *
         * @param str
         */
        public void run(String str) {
            Log.e("run", str + "");
        }
    }

    /**
     * 通过实现 Runnable 创建一个线程
     */
    class MyRunnable implements Runnable {

        /**
         * JVM会自动调用此方法
         */
        @Override
        public void run() {
            cal();
            String str = Thread.currentThread().getName() + ";count:" + count + ";MyRunnable\n";
            buffer.append(str);
            Log.e("MyRunnable",str);
            //其他线程访问UI
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvResult.setText(buffer.toString());
                }
            });
        }
    }
}
