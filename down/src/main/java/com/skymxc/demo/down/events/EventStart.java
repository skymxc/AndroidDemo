package com.skymxc.demo.down.events;

/**
 * Created by mxc on 2017/8/17.
 * description:
 */

public class EventStart {
    protected String url;
    protected int total;

    public EventStart(String url, int total) {
        this.url = url;
        this.total = total;
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
