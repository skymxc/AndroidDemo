package com.mxc.dbdemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static  final  String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        query();
    }

    private void query(){

        DBContext dbContext = new DBContext(this);
        DBHelper helpter = DBHelper.getDbHelpter(dbContext);
        SQLiteDatabase db = helpter.getWritableDatabase();
        Cursor cu = db.query("table1", new String[]{"id", "name", "age"}, null, null, null, null, "id desc", null);
        if (null != cu){
            while (cu.moveToNext()){
                int id = cu.getInt(cu.getColumnIndex("id"));
                String name = cu.getString(cu.getColumnIndex("name"));
                int age = cu.getInt(cu.getColumnIndex("age"));
                Log.e(TAG, "query: id="+id+";name="+name+";age="+age);
            }
            cu.close();
        }
    }


}
