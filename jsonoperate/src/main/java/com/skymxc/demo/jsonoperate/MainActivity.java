package com.skymxc.drag.jsonoperate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.parser_object:
                parserJsonStr();
                break;
            case R.id.list_to_json:
                listToJson();
                break;

        }
    }

    /**
     * 将list集合转为json字符串
     */
    private void listToJson() {
        Clazz clazz  = new Clazz();
        clazz.setName("sky");
        List<Student> stus = new ArrayList<>();
        Student stu1 = new Student("张三",1,19,true,new String[]{"song","sport","swimming"});
        Student stu2 = new Student("李四",1,20,true,new String[]{"song","sleep","swimming"});
        Student stu3 = new Student("王五",1,39,false,new String[]{"drink","sport","swimming"});
        stus.add(stu1);
        stus.add(stu2);
        stus.add(stu3);
        clazz.setStudents(stus);

        //转为json字符串
        JSONObject jo = new JSONObject();
        try {
            jo.put("name",clazz.getName());
            JSONArray ja = new JSONArray();
            for (Student stu:stus){
                JSONObject j = new JSONObject();
                j.put("name",stu.getName());
                j.put("age",stu.getAge());
                j.put("live",stu.isLive());
                JSONArray jsonArray = new JSONArray();
                for(int i=0;i<stu.getInterests().length;i++){
                    jsonArray.put(stu.getInterests()[i]);
                }
                j.put("interest",jsonArray);
                ja.put(j);
            }
            jo.put("students",ja);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Tag","=list ot json ="+jo.toString());
    }

    /**
     * 解析 json字符串
     */
    private void parserJsonStr() {
        try {
            //得到json字符串
            String json = getJsonStr(getAssets().open("object.json"));
            //封装为json对象
            JSONObject jo = new JSONObject(json);
            //逐一获取属性
            String name = jo.getString("name");
            int age = jo.getInt("age");
            int id = jo.getInt("id");
            boolean live = jo.getBoolean("live");
            JSONArray ja = jo.getJSONArray("interest");
            String [] interest = new String[ja.length()];
           for (int i=0;i<ja.length();i++){
               interest[i]= ja.getString(i)+",";
           }
            Student stu = new Student(name,id,age,live,interest);
            Log.e("Tag","===="+stu.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * 读取json字符串
     * @param is  输入流
     * @return json字符串
     * @throws IOException
     */
    public String getJsonStr(InputStream is) throws IOException {
        StringBuffer json = new StringBuffer();
        InputStreamReader isr = new InputStreamReader(is,"utf-8");
        BufferedReader reader = new BufferedReader(isr);

        String line =null;
        while ((line = reader.readLine())!=null){
            json.append(line);
        }
        return json.toString();
    }
}
