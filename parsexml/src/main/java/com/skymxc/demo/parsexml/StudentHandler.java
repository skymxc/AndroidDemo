package com.skymxc.demo.parsexml;

import android.text.TextUtils;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sky-mxc
 */
public class StudentHandler extends DefaultHandler {

    private List<Student> students;
    private Student student;
    private String tag;     //记录读取到的元素的名字



    @Override
    public void startDocument() throws SAXException {
        Log.e("Tag","=======startDocument()========");
        students = new ArrayList<>();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        Log.e("Tag","=======endDocument()========");
    }

    /**
     *
     * @param uri 明明空间的uri
     * @param localName 不带前缀的元素名字
     * @param qName 带有前缀的元素名
     * @param attributes 属性
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Log.e("Tag","==========startElement()==========="+localName);
        Log.e("Tag","===startElement():uri======"+uri);
        Log.e("Tag","===startElement():localName======"+localName);
        Log.e("Tag","===startElement():qName======"+qName);

        switch (localName){
            case "Student":         //student元素
                student = new Student();
                int id = Integer.parseInt(attributes.getValue("id"));   //获取到属性id的值
                student.setId(id);
                break;
            default:
                tag=localName;//除了 student元素 其他元素都要读取文本 characters()无法获取元素名字
                break;
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        Log.e("Tag","==========endElement()==========="+localName);
        switch (localName){
            case "Student":
                students.add(student);
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        Log.e("Tag","==========characters()===========");
        Log.e("Tag","======start:"+start);
        Log.e("Tag","======length:"+length);

        //将字符转为字符串
        String text = new String(ch,start,length).trim();
        Log.e("Tag","====当前元素:"+tag+"===文本值："+text);
        //排除无效字符 会读取到一些换行符
        if (TextUtils.isEmpty(text)) return;
        if (student == null) return;
        switch (tag){
            case "name":
                student.setName(text);
                break;
            case "age":
                int age = Integer.parseInt(text);
                student.setAge(age);
                break;
            case "clazz":
                student.setClazz(text);
                break;
        }
    }

    public List<Student> getStudents() {
        return students;
    }
}
