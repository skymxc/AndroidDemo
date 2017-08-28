package com.skymxc.demo.down.events;

/**
 * Created by mxc on 2017/8/17.
 * description:
 */

public class EventProgress extends EventBase{
    protected int progress;
    protected int total;

    public EventProgress(int progress, int total, String url) {
        this.progress = progress;
        this.total = total;
        this.url = url;
    }

    public EventProgress() {
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
