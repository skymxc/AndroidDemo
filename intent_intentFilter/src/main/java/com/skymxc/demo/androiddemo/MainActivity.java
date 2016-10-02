package com.skymxc.demo.androiddemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.skymxc.demo.androiddemo.share.ShareActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View v){
        switch (v.getId()){
            case R.id.to_a:
               Intent intent = new Intent(this,AActivity.class);
                //以下都是显示使用的方法
               /* intent.setClass(MainActivity.this,AActivity.class);
                intent.setClassName(MainActivity.this,AActivity.class.getName());
                intent.setClassName(getPackageName(),AActivity.class.getName());
                intent.setComponent(new ComponentName(getPackageName(),AActivity.class.getName()));*/
                //附加数据
                intent.putExtra("origin","MainActivity");
                //跳转到AActivity
                startActivity(intent);
                break;
            case R.id.to_b:
                //构造参数是 action
                intent = new Intent("com.skymxc.demo.b1");

                startActivity(intent);
                break;
            case R.id.to_c:
                Intent in = new Intent();
                ////要执行的动作的描述 所有的action中 有一个匹配行
                in.setAction("com.skymxc.action.x2");
                //附加信息的描述 这里的描述只要在intentFilter中声明了就能匹配上，
                // 如果有一项未声明则匹配不上，如果intentFilter中没有可以不添加，会使用默认的category
                in.addCategory("com.skymxc.catagory.c2");
                //对数据进行描述 C中定义了协议为 file
//                in.setData(Uri.parse("http://"));
//                in.setType("audio/12");
                in.setDataAndType(Uri.parse("http://"),"audio/13");
                if (in.resolveActivity(getPackageManager())!=null){
                    //启动
                    startActivity(in);
                }else{
                    Toast.makeText(this,"系统中不存在可以执行此操作的程序",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.to_share:
                in = new Intent(this, ShareActivity.class);
                startActivity(in);
                break;
        }
    }
}
