package com.skymxc.demo.down;

import android.util.Log;

import com.skymxc.demo.down.events.EventBase;
import com.skymxc.demo.down.events.EventComplete;
import com.skymxc.demo.down.events.EventError;
import com.skymxc.demo.down.events.EventProgress;
import com.skymxc.demo.down.events.EventStart;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mxc on 2017/8/18.
 * description: 文件下载
 */

public class DownServer implements Observer {

    ExecutorService executorService;
    protected Map<String, DownTask> taskMap = new HashMap<>();
    protected Map<String, String> pathMap = new HashMap<>();
    protected Map<String, Observable> taskObservableMap = new HashMap<>();
    protected Observable observable;

    public DownServer(int number) {
        executorService = Executors.newFixedThreadPool(number);
        observable = new Observable();
    }

    public void add(String url, String path) {
        DownTask task = new DownTask(url, path);
        task.addObserver(this);
        if (!taskMap.containsKey(url)) {
            taskMap.put(url, task);
        }
        executorService.execute(task);
    }

    public boolean cancel(String url, boolean delete) {
        if (taskMap.containsKey(url)) {
            DownTask downTask = taskMap.get(url);
            downTask.stop = true;
            if (delete && pathMap.containsKey(url)) {
                String s = pathMap.get(url);
                File file = new File(s);
                if (file.exists()) {
                    file.delete();
                }
            }
            return true;
        }

        return false;
    }


    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof EventError) {
            EventError event = (EventError) arg;
            error(event);
        } else if (arg instanceof EventStart) {
            EventStart event = (EventStart) arg;
            start(event);
        } else if (arg instanceof EventComplete) {
            EventComplete event = (EventComplete) arg;
            complete(event);
        } else if (arg instanceof EventProgress) {
            EventProgress event = (EventProgress) arg;
            progress(event);
        } else {
            Log.e("update", "arg->" + arg.toString());
        }
    }

    private void progress(EventProgress event) {
        postSubEvent(event);
        Log.e("progress","progress->\n"+event.getProgress());
    }

    private void start(EventStart event) {
        Log.e("start","total->\n"+event.getTotal());
        pathMap.put(event.getUrl(), event.getPath());
        postSubEvent(event);


    }

    private void complete(EventComplete event) {
        Log.e("complete","url->\n"+event.getUrl());
        postSubEvent(event);
        if (taskMap.size() == 0) {
            observable.notifyObservers(-1);
        }
    }

    private void postSubEvent(EventBase event) {
        if (taskObservableMap.containsKey(event.getUrl())) {
            taskObservableMap.get(event.getUrl()).notifyObservers(event);
        }
    }

    private void error(EventError event) {
        postSubEvent(event);
        taskMap.remove(event.getUrl());
        pathMap.remove(event.getUrl());
        taskObservableMap.remove(event.getUrl());

        if (taskMap.size() == 0) {
            observable.notifyObservers(-1);
        }
    }

    public void addObserver(String url, Observer observer) {
        if (taskMap.containsKey(url)) {
            if (!taskObservableMap.containsKey(url)) {
                taskObservableMap.put(url, new Observable());
            }
            taskObservableMap.get(url).addObserver(observer);
        }
    }

    public void removeObserver(String url, Observer observer) {
        if (taskMap.containsKey(url)) {
            if (taskObservableMap.containsKey(url)) {
                taskObservableMap.get(url).deleteObserver(observer);
                int i = taskObservableMap.get(url).countObservers();
                if (i == 0) {
                    taskObservableMap.remove(url);
                }
            }
        }
    }

    public void addSObserver(Observer observer){
        observable.addObserver(observer);
    }
}
