package com.skymxc.demo.parsexml;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private InputStream is;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            is = getAssets().open("students.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sax_parse:
                SAXParserFactory factory = SAXParserFactory.newInstance();
                StudentHandler handler = new StudentHandler();
                try {
                    SAXParser parser = factory.newSAXParser();
                    parser.parse(getAssets().open("students.xml"),handler);
                    Log.e("Tag","===Size:"+handler.getStudents().size());
                    for (Student stu :handler.getStudents()){
                        Log.e("Tag","==Name:"+stu.getName()+"===Age:"+stu.getAge()+"====Clazz:"+stu.getClazz()+"==id:"+stu.getId());
                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.pull_parse:
                executePull();
                break;
        }
    }

    /**
     * pullParse
     */
    private void executePull() {
        try {
            List<Student> students =null;
            Student student =null;
            //得到解析工厂
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //创建pullParser
            XmlPullParser parser = factory.newPullParser();
            //设置解析内容 指定字符编码 utf-8
            parser.setInput(getAssets().open("students.xml"),"utf-8");
            //开始解析并得到状态码
            int type = parser.getEventType();
            //循环读取解析
            while (type!=XmlPullParser.END_DOCUMENT){
                switch (type){
                    case XmlPullParser.START_DOCUMENT:
                        Log.e("Tag","======START_DOCUMENT=========");
                        students = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        Log.e("Tag","======START_TAG========="+parser.getName());
                        switch (parser.getName()){
                            case "Student":
                                student = new Student();
                                int id = Integer.parseInt(parser.getAttributeValue("","id"));
                                Log.e("Tag","=======读取属性id：======"+id);
                                student.setId(id);
                                break;
                            case "name":
                                String text = parser.nextText();
                                Log.e("Tag","======读取文本======"+text);
                                student.setName(text);
                                break;
                            case "age":
                                int age = Integer.parseInt(parser.nextText());
                                Log.e("Tag","======读取文本======"+age);
                                student.setAge(age);
                                break;
                            case "clazz":
                                 text = parser.nextText();
                                Log.e("Tag","======读取文本======"+text);
                                student.setClazz(text);
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        Log.e("Tag","======END_TAG========="+parser.getName());
                        switch (parser.getName()){
                            case "Student":
                                students.add(student);
                                break;
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        Log.e("Tag","======END_DOCUMENT=========");
                        break;
                }
                //继续读取 并返回状态码
                type = parser.next();
            }

            for (Student stu :students){
                Log.e("Tag","==Name:"+stu.getName()+"===Age:"+stu.getAge()+"====Clazz:"+stu.getClazz()+"==id:"+stu.getId());
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
