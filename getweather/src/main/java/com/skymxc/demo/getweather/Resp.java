package com.skymxc.demo.getweather;

import java.util.List;
import java.util.Map;

/**
 * Created by sky-mxc
 * 获取到的天气信息
 */
public class Resp {
    //头部的那些信息
    private Map<String ,String> otherMap;

    //环境信息
    private Map<String,String> envirmentMap;

    //天气 好几天的
    private List<Weather> weathers;

    public Map<String, String> getOtherMap() {
        return otherMap;
    }

    public Map<String, String> getEnvirmentMap() {
        return envirmentMap;
    }



    public void setOtherMap(Map<String, String> otherMap) {
        this.otherMap = otherMap;
    }

    public void setEnvirmentMap(Map<String, String> envirmentMap) {
        this.envirmentMap = envirmentMap;
    }

    public List<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }
}
