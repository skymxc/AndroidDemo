package com.mxc.dbdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.UUID;

import static java.lang.Math.random;

public class MainActivity extends AppCompatActivity {

    private static  final  String TAG = "MainActivity";

    private Button btInsert;
    private Button btUpdate;
    private Button btDelete;
    private Button btQuery;
    private TextView tvShow;

    private  DBContext context = new DBContext(this);
    private  DBHelper helper = DBHelper.getDbHelpter(context);

    private byte[] bytes;   //方便测试

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btDelete = (Button) findViewById(R.id.db_random);
        btUpdate = (Button) findViewById(R.id.db_update);
        btInsert = (Button) findViewById(R.id.db_insert);
        btQuery = (Button) findViewById(R.id.db_query);
        tvShow  = (TextView) findViewById(R.id.db_text);
    }


   public void onClick(View v){
       switch (v.getId()){
           case R.id.db_random:
               random();
               break;
           case R.id.db_insert:
               insert();
               break;
           case R.id.db_query:
               query();
               break;
           case R.id.db_update:
               update();
               break;
       }
   }

    private void insert() {
        UUID uuid = UUID.randomUUID();
        String name = "mxc";
        byte[] bytes = UUIDUtils.toByte(uuid);
        ContentValues cv = new ContentValues();
        cv.put("id",bytes);
        cv.put("name",name);

        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        db.insert("tb0",null,cv);
        db.setTransactionSuccessful();
        db.endTransaction();
        query();
    }

    private void query(){
        StringBuffer sb = new StringBuffer("数据如下：\n");
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("tb0", new String[]{"id", "name", "age"}, null, null, null, null, null);
        while (cursor.moveToNext()){
            byte[] ids = cursor.getBlob(cursor.getColumnIndex("id"));
            bytes = ids;
            UUID uuid = UUIDUtils.toUUID(ids);
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int age = cursor.getInt(cursor.getColumnIndex("age"));
            sb.append("uuid="+uuid.toString());
            sb.append(";\n");
            String hexStr = UUIDUtils.bytesToHexStr(ids);
           sb.append("hexStr="+hexStr);
            sb.append(";\n");
            Log.e(TAG, "query: hexStr="+hexStr);
            sb.append("name="+name);
            sb.append(";\n");
            sb.append("age="+age);
            sb.append(";\n");
            sb.append("---------------------------\n");
        }
        cursor.close();
        tvShow.setText(sb.toString());
    }

    private void update(){
        /**
         * 对于uuid的比较 ，转换为16进制字符串
         */
        String hexStr = UUIDUtils.bytesToHexStr(bytes);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name","sky-mxc");
        cv.put("age",23);
        Log.e(TAG, "update: hexStr="+hexStr);
        db.beginTransaction();
        db.update("tb0",cv,"id = x'"+hexStr+"'",null);
        db.setTransactionSuccessful();
        db.endTransaction();
        query();
    }

    private void random(){
        UUID uuid = UUID.randomUUID();
        byte[] bytes = UUIDUtils.toByte(uuid);
        String hexStr= UUIDUtils.bytesToHexStr(bytes);
        StringBuffer sb = new StringBuffer();
        sb.append("uuid="+uuid);
        sb.append("\n");
        sb.append("hexStr="+hexStr);
        sb.append("\n------------------------");
        tvShow.setText(sb.toString());
    }




}
