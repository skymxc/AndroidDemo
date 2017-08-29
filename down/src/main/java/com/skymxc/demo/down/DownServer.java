package com.skymxc.demo.down;

import android.util.Log;

import com.skymxc.demo.down.events.EventBase;
import com.skymxc.demo.down.events.EventComplete;
import com.skymxc.demo.down.events.EventError;
import com.skymxc.demo.down.events.EventProgress;
import com.skymxc.demo.down.events.EventStart;

import org.greenrobot.eventbus.EventBus;

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

public class DownServer  extends CustomObserver {

    ExecutorService executorService;
    protected Map<String, DownTask> taskMap = new HashMap<>();
    protected Map<String, String> pathMap = new HashMap<>();
    protected Map<String, CustomObservable> taskObservableMap = new HashMap<>();
    protected CustomObservable observable;

    public DownServer(int number) {
        executorService = Executors.newFixedThreadPool(number);
        observable = new CustomObservable();
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
    public void updateStart(EventStart event) {
        Log.e("start","total->\n"+event.getTotal());
        pathMap.put(event.getUrl(), event.getPath());
        postSubEvent(event);
        EventBus.getDefault().post(event);

    }

    @Override
    public void updateComplete(EventComplete event) {
        Log.e("complete","url->\n"+event.getPath());
        postSubEvent(event);
        EventBus.getDefault().post(event);
        removeAllValue(event.getUrl());
        if (taskMap.size() == 0) {
            observable.notifyObservers(-1);
        }
    }

    @Override
    public void updateError(EventError event) {
        postSubEvent(event);
        EventBus.getDefault().post(event);
        removeAllValue(event.getUrl());
        if (taskMap.size() == 0) {
            observable.notifyObservers(-1);
        }
    }

    @Override
    public void updateProgress(EventProgress event) {
        postSubEvent(event);
        EventBus.getDefault().post(event);
    }

    private void progress(EventProgress event) {
        postSubEvent(event);
        EventBus.getDefault().post(event);
    }

    private void start(EventStart event) {
        Log.e("start","total->\n"+event.getTotal());
        pathMap.put(event.getUrl(), event.getPath());
        postSubEvent(event);
        EventBus.getDefault().post(event);

    }

    private void complete(EventComplete event) {
        Log.e("complete","url->\n"+event.getPath());
        postSubEvent(event);
        EventBus.getDefault().post(event);
        removeAllValue(event.getUrl());
        if (taskMap.size() == 0) {
            observable.notifyObservers(-1);
        }
    }

    private void removeAllValue(String url) {
        removeValue(taskObservableMap, url);
        removeValue(pathMap, url);
        removeValue(taskMap, url);
    }

    private void removeValue(Map  map,String key){
        if (map.containsKey(key)){
            map.remove(key);
        }
    }

    private void postSubEvent(EventBase event) {
        if (taskObservableMap.containsKey(event.getUrl())) {
            taskObservableMap.get(event.getUrl()).notifyObservers(event);
        }
    }

    private void error(EventError event) {
        postSubEvent(event);
        EventBus.getDefault().post(event);
        removeAllValue(event.getUrl());
        if (taskMap.size() == 0) {
            observable.notifyObservers(-1);
        }
    }

    public void addObserver(String url, Observer observer) {
        if (null ==observer) return;
        if (taskMap.containsKey(url)) {
            if (!taskObservableMap.containsKey(url)) {
                taskObservableMap.put(url, new CustomObservable());
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
        if (null ==observer) return;
        observable.addObserver(observer);
    }
    public void removeSObserver(Observer observer){
        observable.deleteObserver(observer);
    }
}
