package com.skymxc.demo.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by sky-mxc
 */

public class DownLoadUtil extends  Thread {

    public Handler mHandler;



    public DownLoadUtil(Handler handler){
        mHandler =handler;
    }

    public  void down(String  url){
        Log.e("DownLoadUtil","==down()==");
        HttpURLConnection connection =null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setConnectTimeout(200000);
            connection.setReadTimeout(2000000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            int resultCode = connection.getResponseCode();
            Log.e("DownLoadUtil","resultCode:"+resultCode);
            if (resultCode==200){
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream bos = null;
                if (is!=null){
                    bos = new ByteArrayOutputStream();
                    byte[] b = new byte[1024];
                    int len =-1;
                    int current=0;
                    int count =connection.getContentLength();
                    Log.e("DownLoadUtil","==count:"+count);

                    while ((len=is.read(b))!=-1){
                        Bundle bundle = new Bundle();
                        Message msg = new Message();
                        msg.setData(bundle);
                        current+=len;
                        Log.e("DownLoadUtil","====progress:"+current+"===count:"+count+"====current/count:"+((float)current/count)*100);
                        msg.getData().putInt("progress",(int)(((float)current/count)*100));
                        bos.write(b,0,len);
                        msg.what=30;
                        mHandler.sendMessage(msg);
                        Thread.sleep(1000);

                    }

                    Log.e("DownLoadUtil","==over==");
                    is.close();
                    bos.close();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (connection!=null){
                connection.disconnect();
            }

        }
    }

    @Override
    public void run() {
        Log.e("DownLoadUtil","==run()==");
        down("http://dl.360safe.com/inst.exe");
    }
}
