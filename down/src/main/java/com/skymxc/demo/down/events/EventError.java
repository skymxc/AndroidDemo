package com.skymxc.demo.down.events;

/**
 * Created by mxc on 2017/8/17.
 * description:
 */

public class EventError extends EventBase{
    protected int code;
    protected String msg;

    public EventError(int code, String msg, String url) {
        this.code = code;
        this.msg = msg;
        this.url = url;
    }

    public EventError() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
