package com.mxc.dbdemo;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

public class DBContext extends ContextWrapper {
    public static final String TAG = "DBContext";

    public DBContext(Context base) {
        super(base);
    }

    /**
     * 返回 数据库文件
     * 重写此方法 返回我们位于sd卡的数据库文件
     * @param name
     * @return
     */
    @Override
    public File getDatabasePath(String name) {
        //位于 sd卡的数据库 初始化时已经创建好了
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/dbDemo",name);
        return file;
    }

    /**
     * Android 4.0后会调用此方法 还有一个2.3以前的方法就重写了，也没有那么底的版本了
     * @param name
     * @param mode
     * @param factory
     * @param errorHandler
     * @return
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
       SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name),factory);
        return db;
    }
}
