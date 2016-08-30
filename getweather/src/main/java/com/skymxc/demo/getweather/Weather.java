package com.skymxc.demo.getweather;

/**
 * Created by sky-mxc
 */
public class Weather {
    //日期
    private String date;
    //最高温度
    private String high;
    //最低温度
    private String low;

    public void setDate(String date) {
        this.date = date;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getDate() {
        return date;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }
}
