package com.skymxc.drag.contentprovider.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sky-mxc
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String name ="sky.DB";
    private static int version =1;
    public static final String TABLE_NAME ="student";



    public DBHelper(Context context ) {
        super(context, name, null, version);
    }

    public SQLiteDatabase getDB(){
        return  this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table student (_id integer   primary key autoincrement," +
                "name varchar(10) not null," +
                "age integer not null)");
        try {
            db.beginTransaction();
            ContentValues cv1 = new ContentValues();
            cv1.put("name","张三");
            cv1.put("age",21);
            db.insert("student","_id",cv1);
            ContentValues cv2 = new ContentValues();
            cv2.put("name","李四");
            cv2.put("age",23);
            db.insert("student","_id",cv2);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
