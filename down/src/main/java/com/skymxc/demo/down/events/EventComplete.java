package com.skymxc.demo.down.events;

/**
 * Created by mxc on 2017/8/17.
 * description:
 */

public class EventComplete {

    protected String url;
    protected String path;

    public EventComplete(String url, String path) {
        this.url = url;
        this.path = path;
    }

    public EventComplete() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
