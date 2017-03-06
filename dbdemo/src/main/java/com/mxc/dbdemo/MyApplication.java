package com.mxc.dbdemo;

import android.app.Application;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyApplication extends Application {
    public static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        copyDB();
    }

    /**
     * 导入现有数据库
     */
    private void copyDB() {
        File mkdir = new File(Environment.getExternalStorageDirectory(),"dbDemo");
        if(!mkdir.exists()){
            mkdir.mkdirs();
        }
        File file = new File(mkdir,"test.db");
        if (file.exists()){
            return;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AssetManager assets = getAssets();
        InputStream is = null;
        FileOutputStream fos = null;
        try {
           is = assets.open("test.db");
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len =-1;
            while ((len = is.read(buffer))!=-1){
                fos.write(buffer,0,len);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != is){
                    is.close();
                }
                if (null != fos){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "copyDB: exists="+file.exists());
    }
}
