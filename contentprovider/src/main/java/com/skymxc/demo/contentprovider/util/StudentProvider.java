package com.skymxc.demo.contentprovider.util;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by sky-mxc
 */

public class StudentProvider extends ContentProvider {

    private DBHelper dbHelper;
    private UriMatcher uriMatcher;

    //匹配结果是一张表
    private static final int STUDENTS = 1;
    //匹配结果是一个条数据
    private static final int STUDENT = 2;
    //一般是包名 避免重复
    public static final String AUTHORITY = "com.skymxc.demo";

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        //初始化 uri匹配者   UriMatcher.NO_MATCH：匹配不上时返回
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //添加能够匹配的uri格式 参数1authorities 匹配住机。  参数2 匹配路径   参数3 code ： match success  return this code；
        // 代表这个uri 操作的是一个表，匹配码是 STUDENTS
        uriMatcher.addURI(AUTHORITY,"student", STUDENTS);
        //代表这个uri 操作的是一条数据 匹配成功后返回 STUDENT
        uriMatcher.addURI(AUTHORITY,"student/#",STUDENT);
        return true;
    }

    /**
     *  查询操作
     * @param uri
     * @param projection 要查询的列
     * @param condition 查询条件
     * @param values 查询参数
     * @param sortOrder 排序
     * @return
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String condition, String[] values, String sortOrder) {
        Cursor cursor =null;
        SQLiteDatabase db= dbHelper.getDB();
        //匹配这个uri 要查询一张表还是 某条数据
        switch (uriMatcher.match(uri)){
            case STUDENT:
                //查询某条数据  ContentUris  :工具类 可以解析出id
                long id= ContentUris.parseId(uri);
                String where ="_id ="+id+" ";
                if (!TextUtils.isEmpty(condition)){
                    where+= " and "+condition;
                }
               cursor= db.query(DBHelper.TABLE_NAME,projection,where,values,null,null,sortOrder);
                break;
            case STUDENTS:
                //查询一张表
                cursor = db.query(DBHelper.TABLE_NAME,projection,condition,values,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("match fail 。uri:"+uri+"");

        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        String type="unKnow";
        switch (uriMatcher.match(uri)){
            case STUDENT:
                type="vnd.android.cursor.item/student";
                break;
            case STUDENTS:
                type= "vnd.android.cursor.dir/student";
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getDB();
        int line=0;
        switch (uriMatcher.match(uri)){
            case STUDENT:
                break;
            case STUDENTS:
              line= (int) db.insert(DBHelper.TABLE_NAME,"_id",values);


                break;
        }
        if (line>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
