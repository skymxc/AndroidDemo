package com.skymxc.demo.down.events;

/**
 * Created by mxc on 2017/8/28.
 * description:
 */

public class EventBase {

    protected String url;

    public EventBase(String url) {
        this.url = url;
    }

    public EventBase() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
