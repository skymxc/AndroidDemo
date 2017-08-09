package com.skymxc.demo.event;

/**
 * Created by mxc on 2017/8/7.
 * description: 消息事件
 */

public class MessageEvent {
    protected String msg;

    public MessageEvent(String msg) {
        this.msg = msg;
    }

    public MessageEvent() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
