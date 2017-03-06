package com.mxc.dbdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = "DBHelper";

    private  static final String NAME = "test.db";
    private  static final int VERSION = 1;

    private  static DBHelper dbHelpter;

    public static DBHelper getDbHelpter(Context context){
        if (dbHelpter == null){
            dbHelpter = new DBHelper(context);
        }
        return dbHelpter;
    }


    private DBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
