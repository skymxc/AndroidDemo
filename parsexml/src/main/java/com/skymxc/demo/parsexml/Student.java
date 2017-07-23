package com.skymxc.drag.parsexml;

/**
 * Created by sky-mxc
 */
public class Student {
   private String name;
   private String clazz;
   private int age;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getClazz() {
        return clazz;
    }

    public int getAge() {
        return age;
    }
}
