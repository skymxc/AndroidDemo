package com.skymxc.demo.testcontentresolver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    private TextView tv;
    private EditText etName;
    private EditText etAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.text);
        etName = (EditText) findViewById(R.id.name);
        etAge = (EditText) findViewById(R.id.age);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.read:
                read();
                break;
            case R.id.insert:
                insert();
                break;
            case R.id.read_sms:
                querySms();
                break;
        }
    }


    private void insert() {
        String name = etName.getText().toString();
        String  age = etAge .getText().toString();

        ContentResolver resolver = getContentResolver();
        String uriStr="content://com.skymxc.demo/student";
        ContentValues cv = new ContentValues();
        cv.put("name",name);
        cv.put("age",age);
        resolver.insert(Uri.parse(uriStr),cv);
        read();
    }

    /**
     * 读取数据
     */
    private void read() {
        ContentResolver resolver= getContentResolver() ;
        String uriStr ="content://com.skymxc.demo/student";
       Cursor cursor= resolver.query(Uri.parse(uriStr),new String[]{"_id","name","age"},null,null,"age");
        StringBuffer sb = new StringBuffer("============student==================\n");

            while (cursor !=null &&cursor.moveToNext()){

                long id = cursor.getLong(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int age = cursor.getInt(cursor.getColumnIndex("age"));
                sb.append("==="+id+"===\n");
                sb.append("name:"+name+"\n");
                sb.append("age:"+age+"\n");
            }
        sb.append("============================");
            tv.setText(sb.toString());
        if (cursor != null){
            cursor.close();
        }
    }

    /**
     * 短信查询
     */
    private void querySms() {
        String[] projection = new String[]{"_id","address","person","body","type"};
        StringBuffer sb = new StringBuffer("短信数据=============\n");
        ContentResolver resolver= getContentResolver();
        Cursor cursor = resolver.query(Uri.parse("content://sms/"),projection,null,null,null);
        while (cursor != null && cursor.moveToNext()){
            sb.append("id:"+cursor.getInt(cursor.getColumnIndex("_id")));
            sb.append("\naddress:"+cursor.getString(cursor.getColumnIndex("address")));
            sb.append("\nperson:"+cursor.getString(cursor.getColumnIndex("person")));
            sb.append("\nbody:"+cursor.getString(cursor.getColumnIndex("body")));
            sb.append("\ntype:"+cursor.getString(cursor.getColumnIndex("type")));
            sb.append("\n=================================================");
        }

        tv.setText(sb.toString());
    }
}
