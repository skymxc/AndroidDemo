package com.skymxc.demo.androiddemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class BActivity extends AppCompatActivity {


    private RadioGroup rg ;

    private CheckBox cb1;

    private CheckBox cb2;

    private CheckBox cb3;

    private EditText editUri ;

    private EditText editType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        rg = (RadioGroup) findViewById(R.id.action_group);
        cb1 = (CheckBox) findViewById(R.id.cb_c1);
        cb2 = (CheckBox) findViewById(R.id.cb_c2);
        cb3 = (CheckBox) findViewById(R.id.cb_c3);
        editUri = (EditText) findViewById(R.id.edit_uri);
        editType = (EditText) findViewById(R.id.edit_type);
    }

    public void click(View v){
        switch (v.getId()){
            case R.id.bt_start:

                //获取到选择的action
               int id= rg.getCheckedRadioButtonId();
               // Log.e("Tag","============getCheckedRadioButtonId============="+id);

                //获取到选中的 action
                RadioButton rb = (RadioButton) findViewById(id);
                String action = rb.getText().toString();
                //创建intent
                Intent in = new Intent(action);

                //获取到选中的catagory，并添加到intent中
                List<String> catagoryStr = getCataGorys();
                if (catagoryStr!=null && catagoryStr.size()>0){
                    for(String str :catagoryStr){
                        Log.e("Tag","===================catagory============="+str);
                        in.addCategory(str);
                    }
                }
                //设置data和 type
                String uri = editUri.getText().toString();
                String type = editType.getText().toString();
                if (type !=null && type.length()>0){
                    in.setDataAndType(Uri.parse(uri),type);
                }else{
                    in.setData(Uri.parse(uri));
                }


                Log.e("Tag","=======action:"+in.getAction());
                Log.e("Tag","=======Uri:"+in.getData());
                Log.e("Tag","=======Type:"+in.getType());
                if (in.getCategories()!=null && in.getCategories().size()>0){
                    for (String str :in.getCategories()){
                        Log.e("Tag","=======categorie:"+str);
                    }
                }
                //启动activity
                startActivity(in);
                break;
        }
    }


    /**
     * 获取选中的附加信息
     * @return
     */
    private List<String> getCataGorys() {
        List<String> catagorys = new ArrayList<String>();
        if (cb1.isChecked()){
            catagorys.add(cb1.getText().toString());
        }
        if (cb2.isChecked()){
            catagorys.add(cb2.getText().toString());
        }

        if (cb3.isChecked()){
            catagorys.add(cb3.getText().toString());
        }

        return catagorys;
    }
}
