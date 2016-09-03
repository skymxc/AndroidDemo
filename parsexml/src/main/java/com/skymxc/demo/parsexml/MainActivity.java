package com.skymxc.demo.parsexml;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

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
        }
    }
}
