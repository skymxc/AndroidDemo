package com.skymxc.demo.down;

import android.util.Log;

import com.skymxc.demo.down.events.EventComplete;
import com.skymxc.demo.down.events.EventError;
import com.skymxc.demo.down.events.EventProgress;
import com.skymxc.demo.down.events.EventStart;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by mxc on 2017/8/18.
 * description:
 */

public class DownServer implements Observer {

    ExecutorService executorService;
    HashMap<String, Future> taskMap = new HashMap<>();

    public DownServer(int number) {
        executorService = Executors.newScheduledThreadPool(number);
    }

    public void add(String url, String path) {
        DownTask task = new DownTask(url, path);
        executorService.execute(task);
    }

    public void cancel(String url, boolean delete) {

    }


    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof EventError) {
            EventError event = (EventError) arg;
            error(event);
        } else if (arg instanceof EventStart) {
            EventStart event = (EventStart) arg;

        } else if (arg instanceof EventComplete) {
            EventComplete event = (EventComplete) arg;
            complete(event);

        } else if (arg instanceof EventProgress) {
            EventProgress event = (EventProgress) arg;

        } else {
            Log.e("update", "arg->" + arg.toString());
        }
    }

    private void complete(EventComplete event) {
        Future future = taskMap.get(event.getUrl());
        if (!future.isDone()) {
            future.cancel(true);
        }
        next(event.getUrl());
    }

    private void next(String url) {

    }

    private void error(EventError event) {
        // TODO: 2017/8/18 下载出错处理
        next(event.getUrl());
    }
}
