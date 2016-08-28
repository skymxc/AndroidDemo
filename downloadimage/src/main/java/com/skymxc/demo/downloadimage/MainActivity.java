package com.skymxc.demo.downloadimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText edit ;
    private ImageView img1 ;
    private ImageView img2;
    private ProgressBar pb;
    private String urlStr ;
    private Button btstart;
    private Button btCanel;
    private MyAsync myAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit = (EditText) findViewById(R.id.url);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView)findViewById(R.id.img2);
        pb = (ProgressBar) findViewById(R.id.pb);
        btCanel = (Button) findViewById(R.id.cancel_async);
        btstart = (Button) findViewById(R.id.down_async);
    }


    public  void click(View v){

        switch (v.getId()){
            case R.id.down_thread:
                Toast.makeText(MainActivity.this,"开始下载",Toast.LENGTH_SHORT).show();
                loadImage();

                break;
            case R.id.down_async:
                String url = edit.getText().toString();
                 myAsync = new MyAsync();
                pb.setProgress(0);
                myAsync.execute(url);
                pb.setProgress(100);
                break;
            case R.id.cancel_async:
                if (myAsync!=null && myAsync.getStatus() == AsyncTask.Status.RUNNING){
                    myAsync.cancel(true);
                    btCanel.setEnabled(false);
                    btstart.setEnabled(true);
                }else{
                    btCanel.setEnabled(true);
                    btstart.setEnabled(false);
                }
                break;
        }
    }

    /**
     * 启动新线程下载图片
     */
    private void loadImage(){
        new Thread(){
            @Override
            public void run() {
                 urlStr = edit.getText().toString();       //获取下载的地址
                Log.e("Tag","下载地址："+urlStr);
                HttpURLConnection connection = null;    //url连接
                try {
                    URL url = new URL(urlStr);
                    connection= (HttpURLConnection) url.openConnection();   //打开连接
                    connection.setRequestMethod("GET");                 //设置访问方式 默认是GET 必须为大写
                    connection.setDoInput(true);                        //从网络读取数据 默认是true
                  //  connection.setDoOutput(false);                      //上传数据 但是请求方法必须是post 默认是true
                    connection.setReadTimeout(10000);                   //设置读取超时时间 毫秒单位
                    connection.setConnectTimeout(10000);                //设置连接超时时间 毫秒单位
                   // connection.setRequestProperty("Content-Type","text/plain;charset=utf-8");//上传数据时使用，可以对增加多个请求参数
                    int code = connection.getResponseCode();            //获取网络请求响应吗 常用：200,404 500
                    Log.e("Tag","========网络请求响应吗："+code);
                    if (code==200){
                        InputStream is = connection.getInputStream();       //获取到输入流
                       final Bitmap bmp= BitmapFactory.decodeStream(is);          //通过位图工厂将输入流转换为位图
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img1.setImageBitmap(bmp);
                            }
                        });
                    }else{
                        //关于UI的操作只能在 主线程进行  runOnUiThread ：将操作寄送到主线程运行
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"网络请求错误",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    if (connection!=null){
                        connection.disconnect();    //断开连接
                    }
                }


            }
        }.start();
    }

    /**
     * 使用 异步任务下载图片 并显示进度
     * 参数1 String  就是 doInbackground() 的参数类型 我们的代码就在这里写 系统默认调用
     * 参数2 Integer      onProgressUpdate() 的参数类型 系统不会自动调用此方法 手动调用：publishProgress()
     * 参数3 Bitmap       doInbackground() 的返回值类型 也是 onPostExecute() 的参数类型
     */
    class MyAsync extends AsyncTask<String ,Integer,Bitmap>{


        /**
         * 在 doInbackground() 执行前，系统自动调用 在主线程运行
         */
        @Override
        protected void onPreExecute() {
            Toast.makeText(MainActivity.this,"异步任务开始执行下载",Toast.LENGTH_SHORT).show();
          //  super.onPreExecute();
            btstart.setEnabled(false);
            btCanel.setEnabled(true);
        }

        /**
         * 不在主线程 执行
         * @param strings url
         * @return 位图
         */
        @Override
        protected Bitmap doInBackground(String... strings) {

           HttpURLConnection connection =null;
            try {
                URL url = new URL(strings[0]);
                connection= (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setConnectTimeout(20000);
                int code = connection.getResponseCode();
                if (code==200){
                    //为了显示进度条这里使用 字节数组输出流
                    InputStream is = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int length =-1 ;
                    int progress =0;    //进度
                    int count = connection.getContentLength();  //获取内容产固定
                    byte[] bs = new byte[5];
                   while ((length=is.read(bs))!=-1){
                       progress+=length;    //进度累加
                       if (count ==0){
                           publishProgress(-1);
                       }else{
                           //进度值改变通知
                           publishProgress((int)((float)progress/count*100));
                       }

                       Log.e("Tag","=任务是否取消："+isCancelled()+"=======任务进度："+(int)((float)progress/count*100)+"%");
                       if (isCancelled()){//如果取消了任务 就不执行
                           return null;
                       }
                       //由于我这网速太快，为了看到进度条就睡眠一会吧
                      //     Thread.sleep(10);


                       bos.write(bs,0,length);
                   }
                    Log.e("Tag","=========任务完成");
                    return BitmapFactory.decodeByteArray(bos.toByteArray(),0,bos.size());


                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (connection!=null){
                    connection.disconnect();
                }
            }
            return null;
        }

        /**
         * 在 doInbackground() 执行后 系统自动调用  在主线程运行
         * @param bitmap 位图
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.e("Tag","===============任务是否取消："+isCancelled());
                Toast.makeText(MainActivity.this, "AsyncTask 下载完成，进行UI绘制", Toast.LENGTH_SHORT).show();
                img2.setImageBitmap(bitmap);    //设置位图
                // super.onPostExecute(bitmap);
                btstart.setEnabled(true);
                btCanel.setEnabled(false);
        }

        /**
         * 系统不会自动调用 使用 publishProgress() 调用
         * 在主线程执行
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];       //进度值
           if (progress!=-1) {
               pb.setProgress(progress);
           }
        }
    }
}
