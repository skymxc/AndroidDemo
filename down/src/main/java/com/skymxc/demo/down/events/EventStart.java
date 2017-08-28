package com.skymxc.demo.down.events;

/**
 * Created by mxc on 2017/8/17.
 * description:
 */

public class EventStart extends EventBase{
    protected int total;
    protected String path;

    public EventStart(String url, int total, String path) {
        this.url = url;
        this.total = total;
        this.path = path;
    }

    public EventStart(String url, int total) {
        this.url = url;
        this.total = total;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public EventStart() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
