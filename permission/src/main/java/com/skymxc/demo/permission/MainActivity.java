package com.skymxc.drag.permission;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    private static final String  TAG ="MainActivity";

    private static final int SMS = 10;
    private static final int CONTACTS = 20;
    private static final int PHONE = 30;

    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.text);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.read_sms:
                requestSms();
                break;
            case R.id.read_contacts:
                requestContacts();
                break;
            case R.id.phone:
                requestPhone();
                break;
        }
    }

    //打电话
    private void requestPhone() {
        final String permission ="android.permission.CALL_PHONE";
        int targetSdkVersion =0;
        int answer =-1;
        //检测系统版本
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //检测TargetSDKVersion
            try {
                PackageInfo info=  getPackageManager().getPackageInfo(getPackageName(),0);
                targetSdkVersion = info.applicationInfo.targetSdkVersion;

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            //检查是否有权限
            if (targetSdkVersion>=Build.VERSION_CODES.M){
                //>= 23
                answer= checkSelfPermission(permission);
            }else{
                //<23
                answer = PermissionChecker.checkSelfPermission(this,permission);
            }
            LogCheckResult(answer);
            if (answer!= PermissionChecker.PERMISSION_GRANTED){
                //无权限
                //请求权限
                if (!shouldShowRequestPermissionRationale(permission)){
                    new AlertDialog.Builder(this).setTitle("权限说明")
                            .setMessage("使用此功能需要打电话的权限 ，请在授权后使用")
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{permission},PHONE);

                                }

                            })
                            .setNeutralButton("取消",null)
                            .show();

                }else{
                    requestPermissions(new String[]{permission},CONTACTS);

                }
            }else{
                //有权限
            }
        }
    }

    /**
     * 请求 读取联系人
     */
    private void requestContacts() {
        final String permission ="android.permission.READ_CONTACTS";
        int targetSdkVersion =0;
        int answer =-1;
        //检测系统版本
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //检测TargetSDKVersion
            try {
                PackageInfo info=  getPackageManager().getPackageInfo(getPackageName(),0);
                 targetSdkVersion = info.applicationInfo.targetSdkVersion;

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            //检查是否有权限
            if (targetSdkVersion>=Build.VERSION_CODES.M){
                //>= 23
               answer= checkSelfPermission(permission);
            }else{
                //<23
                answer = PermissionChecker.checkSelfPermission(this,permission);
            }
            LogCheckResult(answer);
            if (answer!= PermissionChecker.PERMISSION_GRANTED){
                //无权限
                //请求权限
                boolean en =shouldShowRequestPermissionRationale(permission);
                log("shouldShowRequestPermissionRationale 是否含有不在提示:"+en);
                if (!en){
                    new AlertDialog.Builder(this).setTitle("权限说明")
                            .setMessage("使用此功能需要读取联系人的权限 请授权后使用")
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   ActivityCompat.requestPermissions(MainActivity.this,new String[]{permission},CONTACTS);

                                }
                            })
                            .show();

                }else{
                    requestPermissions(new String[]{permission},CONTACTS);

                }
            }else{
                //有权限
            }
        }
    }

    /**
     * 请求短信权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestSms() {
        //
            final String permission = "android.permission.READ_SMS";

        //检查当前系统版本是否在6.0以上
        if (checkVersion()){
            int result =-1;
            //检测targetSDKVersion 是否在23以上
                if (checkTargetSdkVersion()){
                    //targetSDKVersion >=23
                    //检查是否具有读取短信的权限
                    result = checkSelfPermission(permission);
                }else{
                    //targetSDKVersion <23
                    //检查是否具有读取短信的权限
                    result= PermissionChecker.checkSelfPermission(this,permission);
                }
                LogCheckResult(result);
                if(result==PermissionChecker.PERMISSION_GRANTED){
                    //已经有了权限
                    //TODO 读取短信
                    Toast.makeText(this,"读取短信授权成功",Toast.LENGTH_SHORT).show();
                    tv.setText(getSmsInPhone());
                }else{
                    //没有权限
                    //TODO 请求权限
                    // 第一次请求就返回false 拒绝过返回true 或者 用户选择不再提示返回false
                  boolean answer=  shouldShowRequestPermissionRationale(permission);
                    log("shouldShowRequestPermissionRationale :"+answer);
                    if (!answer){
                        new AlertDialog.Builder(this).setTitle("权限说明")
                                .setMessage("此功能需要读取短信的权限，没有权限无法使用此功能。请在稍后授权后使用")
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        requestPermissions(new String[]{permission},SMS);

                                    }
                                })
                                .setNeutralButton("取消",null)
                                .show();
                    }else{
                        requestPermissions(new String[]{permission},SMS);

                    }
                }

        }else{
            //无需请求
            Toast.makeText(this,"读取短信授权成功",Toast.LENGTH_SHORT).show();
            tv.setText(getSmsInPhone());
        }


    }

    /**
     *  申请权限的响应
     * @param requestCode 请求码
     * @param permissions 权限数组
     * @param grantResults 结果数组
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case SMS:
              LogCheckResult(grantResults[0]);
                if (grantResults.length>0 && grantResults[0]==PermissionChecker.PERMISSION_GRANTED){
                    //TODO 读取短信
                    Toast.makeText(this,"读取短信授权成功",Toast.LENGTH_SHORT).show();
                    tv.setText(getSmsInPhone());
                }else{
                    Toast.makeText(this,"读取短信授权失败",Toast.LENGTH_SHORT).show();
                }
                break;
            case PHONE:
                LogCheckResult(grantResults[0]);
                if (grantResults.length>0 && grantResults[0] == PermissionChecker.PERMISSION_GRANTED){
                    //TODO 打电话的权限获取成功
                    Toast.makeText(this,"成功获取打电话的权限",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"获取打电话的权限 失败",Toast.LENGTH_SHORT).show();
                  //  requestPhone();
                }
                break;
            case CONTACTS:
                LogCheckResult(grantResults[0]);
                if (grantResults.length>0 && grantResults[0] == PermissionChecker.PERMISSION_GRANTED){
                    Toast.makeText(this,"成功获取操作联系人的权限",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"获取操作联系人的权限 失败",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    /**
     * 打印请求结果
     * @param result
     */
    private void LogCheckResult(int result) {
        switch (result){
            case PermissionChecker.PERMISSION_GRANTED:
                //已有权限
                log("已经授权");
            break;
            case PermissionChecker.PERMISSION_DENIED:
                log("没有授权：PERMISSION_DENIED");
                break;
            case PermissionChecker.PERMISSION_DENIED_APP_OP:
                log("没有授权：PERMISSION_DENIED_APP_OP");
                break;
        }
    }


    /**
     * 检查系统版本是否在6.0或者6.0以上
     * @return
     */
    private boolean checkVersion(){
        // Build.VERSION.SDK_INT 当前系统版本
        //Build.VERSION_CODES.M 6.0版本
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            return true;
        }
        return false;
    }


    /**
     * 检查targetSDKVersion 是否在 23以上
     * @return
     */
    private boolean checkTargetSdkVersion(){
        PackageInfo info= null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int targetSdk=  info.applicationInfo.targetSdkVersion;
        log("TargetSdkVersion:"+targetSdk);
        if (targetSdk>=Build.VERSION_CODES.M){
            return true;
        }
        return false;
    }

    public String getSmsInPhone() {
        log("开始读取短信");
        /**
         content://sms/               所有短信
         content://sms/inbox        收件箱
         content://sms/sent          已发送
         content://sms/draft         草稿
         content://sms/outbox     发件箱
         content://sms/failed       发送失败
         content://sms/queued    待发送列表
         */

        /**
         * 主要结构
         _id => 短消息序号 如100
         thread_id => 对话的序号 如100
         address => 发件人地址，手机号.如+8613811810000
         person => 发件人，返回一个数字就是联系人列表里的序号，陌生人为null
         date => 日期  long型。如1256539465022
         protocol => 协议 0 SMS_RPOTO, 1 MMS_PROTO
         read => 是否阅读 0未读， 1已读
         status => 状态 -1接收，0 complete, 64 pending, 128 failed
         type => 类型 1是接收到的，2是已发出
         body => 短消息内容
         service_center => 短信服务中心号码编号。如+8613800755500
         */



        final String SMS_URI_ALL = "content://sms/";
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_SEND = "content://sms/sent";
        final String SMS_URI_DRAFT = "content://sms/draft";
        final String SMS_URI_OUTBOX = "content://sms/outbox";
        final String SMS_URI_FAILED = "content://sms/failed";
        final String SMS_URI_QUEUED = "content://sms/queued";

        StringBuilder smsBuilder = new StringBuilder();

        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
            Cursor cur = getContentResolver().query(uri, projection, null, null, "date desc");      // 获取手机内部短信
            log("cursor:"+cur.getCount());
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");

                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int intType = cur.getInt(index_Type);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(longDate);
                    String strDate = dateFormat.format(d);

                    String strType = "";
                    if (intType == 1) {
                        strType = "接收";
                    } else if (intType == 2) {
                        strType = "发送";
                    } else {
                        strType = "null";
                    }

                    smsBuilder.append("[ ");
                    smsBuilder.append(strAddress + ", ");
                    smsBuilder.append(intPerson + ", ");
                    smsBuilder.append(strbody + ", ");
                    smsBuilder.append(strDate + ", ");
                    smsBuilder.append(strType);
                    smsBuilder.append(" ]\n\n");
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if

            smsBuilder.append("getSmsInPhone has executed!");

        } catch (SQLiteException ex) {
            log("SQLiteException in getSmsInPhone");
        }

        return smsBuilder.toString();
    }

    private void log(String log){
        Log.e(TAG,"========"+log);
    }
}
