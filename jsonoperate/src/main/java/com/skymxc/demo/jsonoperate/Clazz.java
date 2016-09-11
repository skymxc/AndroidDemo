package com.skymxc.demo.jsonoperate;

import java.util.List;

/**
 * Created by sky-mxc
 */
public class Clazz {
    private String name;
    private List<Student> students;

    public void setName(String name) {
        this.name = name;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public List<Student> getStudents() {
        return students;
    }
}
