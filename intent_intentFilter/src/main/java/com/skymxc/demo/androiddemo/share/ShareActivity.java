package com.skymxc.drag.androiddemo.share;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.skymxc.drag.androiddemo.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.skymxc.drag.androiddemo.R.id.share;

public class ShareActivity extends AppCompatActivity  implements View.OnClickListener{

    private static final String  TAG ="share.ShareActivity";

    private static final int REQUEST_CODE=1;

    private EditText etContext;
    private Intent intent;
    private EditText etSubject;
    private Uri uri;
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        etContext = (EditText) findViewById(R.id.edit_context);
        etSubject = (EditText) findViewById(R.id.edit_subject);
        image = (ImageView) findViewById(R.id.image);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case share:
                //只分享了文字
                share();
                break;
            case R.id.share_image_text:
                //图片+文本
                shareChooser();
                break;
            case R.id.image:
                //去获取图片
                intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,REQUEST_CODE);
                break;
            case R.id.share_photo_wx:
                //分享到微信朋友圈
                sharePhotoToWX();
                break;
//            case R.id.share_QQ:
//                sharePhotoToQQ();
//                break;
            case R.id.share_image_mult:
                sharePhototMult();
                break;
        }
    }

    /**
     * 分享多个图片
     * 目前来看 只有微博能同时分享文本和内容
     */
    private void sharePhototMult() {
        ArrayList<Uri> uris =new ArrayList<>();
        uris.add(uri);
        uris.add(uri);
        intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT,"分享内容测试");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,uris);
        if(hasApplication(intent)){
            startActivity(Intent.createChooser(intent,"选择分享平台"));
        }
    }

    /**
     * 分享图片到QQ   失败
     */
    private void sharePhotoToQQ() {
        String wbPackageName ="com.tencent.mobileqq";
        String wbActivityName ="com.tencent.mobileqq.activity.JumpActivity  ";
        if (checkPackage(wbPackageName)){
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.setClassName(wbPackageName,wbActivityName);
         //   intent.putExtra(Intent.EXTRA_TEXT,"QQ分享图片测试");
            intent.putExtra(Intent.EXTRA_STREAM,uri);
            if (hasApplication(intent)){
                startActivity(intent);
            }

        }

    }

    /**
     * 分享图片 和文本
     * QQ 微信 不能图片和文本一起分享
     * 微博可以实现图片和文本一起分享
     */
    private void shareChooser() {
        String context = etContext.getText().toString();

        intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT,context);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        if (hasApplication(intent)){
            startActivity(Intent.createChooser(intent,"选择要分享到的平台吧"));
        }
    }

    private void share() {
        /**
         * 简单分享
         * 让用户去选择分享平台
         * 只分享简单的文子
         */
        String context = etContext.getText().toString();
        intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,context);
        if(hasApplication(intent)){
            //没有默认选项 可以自定义dialog标题
            startActivity(Intent.createChooser(intent,"分享一下"));
            //系统默认主题
            //   startActivity(intent);
        }
    }

    /**
     * 分享图片去微信朋友圈
     */
    private void sharePhotoToWX() {
        String wxPackage = "com.tencent.mm";
        if (checkPackage(wxPackage)){
            intent = new Intent(Intent.ACTION_SEND);

            intent.setClassName(wxPackage,"com.tencent.mm.ui.tools.ShareToTimeLineUI");
            intent.setType("image/*");
            intent.putExtra("Kdescription","朋友圈分享测试");
            intent.putExtra(Intent.EXTRA_STREAM,uri);
            startActivity(intent);

        }else{
            Toast.makeText(this,"微信不存在",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null && resultCode == RESULT_OK){
             uri =  data.getData();

         ContentResolver resolver=   getContentResolver();
            try {
                Bitmap bmp= MediaStore.Images.Media.getBitmap(resolver,uri);
                image.setImageBitmap(bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Uri getUri(){
        Resources r = getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+r.getResourcePackageName(R.mipmap.top)+"/"+r.getResourceTypeName(R.mipmap.top)+"/"+r.getResourceEntryName(R.mipmap.top);
        log("path:"+path);
        Uri uri = Uri.parse(path);
        return uri;
    }

    /**
     * 判断是否存在intent要启动的Activity
     * @param intent
     * @return
     */
    private boolean hasApplication(Intent intent){

        //查询是否有该intent要启动的Activity
        List<ResolveInfo> resolveInfos= getPackageManager().queryIntentActivities(intent,0);
        log(resolveInfos.size()+"");
        return  resolveInfos.size()>0?true:false;
    }

    /**
     * 检查程序是否安装
     * @param packageName 包名
     * @return 是否安装
     */
    private boolean checkPackage(String packageName){
        try {
          PackageInfo packageInfo= getPackageManager().getPackageInfo(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            if (packageInfo != null) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return  false;
    }


    private void log(String log){
        Log.e(TAG,"==="+log+"====");
    }
}
