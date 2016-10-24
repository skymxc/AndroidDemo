package com.skymxc.demo.recyclerview.entity;

/**
 * Created by sky-mxc
 */
public class Item {
    private static final String TAG = "Item";

    private String name ;
    private String icon;
    private int type;
    private String msg;

    public Item() {
        super();
    }

    public Item(String name, String icon, int type, String msg) {
        this.name = name;
        this.icon = icon;
        this.type = type;
        this.msg = msg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static String getTAG() {

        return TAG;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
