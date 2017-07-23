package com.skymxc.drag.jsonoperate;

/**
 * Created by sky-mxc
 */
public class Student {
    private String name;
    private int id;
    private int age;
    private boolean live;
    private String[] interests;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void setInterests(String[] interests) {
        this.interests = interests;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public boolean isLive() {
        return live;
    }

    public String[] getInterests() {
        return interests;
    }

    public Student(String name, int id, int age, boolean live, String[] interests) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.live = live;
        this.interests = interests;
    }

    @Override
    public String toString() {
        StringBuffer sb =new StringBuffer();
        if (interests!=null){
            for (int i=0;i<interests.length;i++){
                sb.append(interests[i]);
            }
        }
        return "id:"+id+",name:"+name+",age:"+age+",live:"+live+",interest:"+sb.toString();
    }
}
