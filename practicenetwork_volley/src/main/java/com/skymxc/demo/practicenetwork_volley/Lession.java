package com.skymxc.drag.practicenetwork_volley;

/**
 * Created by sky-mxc
 * 课程实体类
 */
public class Lession {
   private  String ldescribe;
   private  int lessionType;
   private  String  lname;
   private  int needStars ;
   private  int nums;
   private  String thumbUrl;
   private  int tid;

    public void setLdescribe(String ldescribe) {
        this.ldescribe = ldescribe;
    }

    public void setLessionType(int lessionType) {
        this.lessionType = lessionType;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setNeedStars(int needStars) {
        this.needStars = needStars;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getLdescribe() {
        return ldescribe;
    }

    public int getLessionType() {
        return lessionType;
    }

    public String getLname() {
        return lname;
    }

    public int getNeedStars() {
        return needStars;
    }

    public int getNums() {
        return nums;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public int getTid() {
        return tid;
    }
}
