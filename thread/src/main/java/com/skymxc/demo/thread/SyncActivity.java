package com.skymxc.demo.thread;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * 共享同步
 */
public class SyncActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        getSupportActionBar().setTitle("资源共享");

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sale_0:
                //票资源
                Tickets tickets = new Tickets();
                SaleRunnable saleRunnable = new SaleRunnable(tickets);
                //开三个窗口使用同一程序去卖票
                new Thread(saleRunnable, "窗口零").start();
                new Thread(saleRunnable, "窗口一").start();
                new Thread(saleRunnable, "窗口二").start();
                break;
            case R.id.sale_1:
                tickets = new Tickets();
                SaleThread sale0 = new SaleThread(tickets, "窗口零");
                SaleThread sale1 = new SaleThread(tickets, "窗口一");
                SaleThread sale2 = new SaleThread(tickets, "窗口二");
                sale0.start();
                sale1.start();
                sale2.start();
                break;

        }
    }

    /**
     * 票
     */
    class Tickets {
        int num = 5;

        public int get() {
            return num;
        }

        public void set(int num) {
            this.num = num;
        }
    }

    /**
     * 卖票 程序
     */
    class SaleRunnable implements Runnable {

        private Tickets tickets;

        public SaleRunnable(Tickets tickets) {
            this.tickets = tickets;
        }

        @Override
        public void run() {
            while (true) {
                if (sale()) {
                    break;
                }
                try {
                    //延迟 1000
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.e(Thread.currentThread().getName(), "没票了");

        }

        private synchronized boolean sale() {
            if (tickets.get() > 0) {
                Log.e(Thread.currentThread().getName(), "销售第" + tickets.get() + "张，剩余" + (tickets.get() - 1) + "张");
                tickets.set(tickets.get() - 1);
                return false;
            }
            return true;

        }
    }

    /**
     * 卖票窗口
     */
    class SaleThread extends Thread {

        private Tickets tickets;

        public SaleThread(Tickets ti, String name) {
            super(name);
            tickets = ti;
        }

        @Override
        public void run() {
            while (true) {
                //同步这个资源
                synchronized (tickets) {
                    if (tickets.get() > 0) {
                        Log.e(Thread.currentThread().getName(), "销售第" + tickets.get() + "张，剩余" + (tickets.get() - 1) + "张");
                        tickets.set(tickets.get() - 1);
                    } else {
                        break;
                    }
                }
                try {
                    //延迟 1000
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.e(Thread.currentThread().getName(), "没票了");
        }
    }



}
