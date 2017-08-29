package com.skymxc.demo.down;

import android.text.TextUtils;
import android.util.Log;

import com.skymxc.demo.down.events.EventComplete;
import com.skymxc.demo.down.events.EventError;
import com.skymxc.demo.down.events.EventProgress;
import com.skymxc.demo.down.events.EventStart;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observer;

/**
 * Created by mxc on 2017/8/17.
 * description:
 */

public class DownTask implements Runnable {

    protected String downURL;
    protected int length = 0;
    protected String downPath;
    protected int current;
    protected boolean pause;

    protected boolean stop;
    protected CustomObservable observable;

    public DownTask(String downURL, String downPath) {
        this.downPath = downPath;
        this.downURL = downURL;
        observable = new CustomObservable();
    }

    @Override
    public void run() {
        getDownLength();
        if (length == -1) {
            //url地址不对，获取不到长度
            error("URL不正确", -1);
            return;
        } else if (length == 0) {
            //长度为0 URL地址不对
            error("下载长度为0", -2);
            return;
        }

        RandomAccessFile outFile = null;
        try {
            URL url = new URL(downURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //指定开始及结束位置
            connection.addRequestProperty("Range", "bytes=" + current + "-" + length);
            connection.connect();
            if (connection.getResponseCode() == 206) {
                EventStart eventStart = new EventStart(downURL, length, downPath);
                post(eventStart);
                //本地保存路径
                outFile = new RandomAccessFile(downPath, "rws");
                outFile.seek(current);
                //网络输入流
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[2048];
                int length = -1;
                while ((length = inputStream.read(buffer)) != -1) {
                    outFile.write(buffer, 0, length);
                    //进度值保存
                    current += length;
                    PrefUtil.putProgress(downURL, current);
                    EventProgress progress = new EventProgress(current, this.length, downURL);
                    post(progress);
                    if (pause || stop) {
                        break;
                    }
                }

                outFile.close();
                inputStream.close();
                connection.disconnect();
                if (!stop && !pause) {
                    PrefUtil.removeProgress(downURL);
                    //下载完毕 最好是MD5一下，是否完整
                    EventComplete eventComplete = new EventComplete(downURL, downPath);
                    post(eventComplete);
                }
                if (stop) {//任务停止或取消
                    PrefUtil.removeProgress(downURL);

                }
            } else {
                //失败
                int code = connection.getResponseCode();
                String msg = connection.getResponseMessage();
                error(msg, code);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void getDownLength() {
        if (TextUtils.isEmpty(downURL)) return;
        try {
            URL url = new URL(downURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == 200) {
                length = connection.getContentLength();
                String contentType = connection.getContentType();
                Log.e("getDownLength", "type-->" + contentType);
                current = PrefUtil.getProgress(downURL);
            }
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            length = -1;
        } catch (IOException e) {
            e.printStackTrace();
            length = -1;
        }
    }

    public void addObserver(Observer observer) {
        observable.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        observable.deleteObserver(observer);
    }

    private void error(String msg, int code) {
        Log.e("error", "code(" + code + "),msg->" + msg);
        EventError event = new EventError(code, msg, downURL);
        post(event);
    }

    private void post(Object event) {
        observable.notifyObservers(event);
    }

}
