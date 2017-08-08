package com.skymxc.demo.event;

/**
 * Created by mxc on 2017/8/8.
 * description:
 */

public class OtherEvent {
    private String msg;

    public OtherEvent(String msg) {
        this.msg = msg;
    }

    public OtherEvent() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
