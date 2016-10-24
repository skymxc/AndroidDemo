package com.skymxc.demo.browser;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String  TAG = "MainActivity tag";

    private WebView webView;
    private long exit =0;
    private FloatingActionButton floatButton;
    private WebChromeClient chromeClient;
    private WebSettings settings;
    private WebViewClient viewClient;
    private Toolbar toolbar;
    private TextView tvTitle;
    private ImageView ImgIcon;

    private float dy =0;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        tvTitle = (TextView) findViewById(R.id.title);
        ImgIcon = (ImageView) findViewById(R.id.icon);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        webView = (WebView) findViewById(R.id.web_view);
        floatButton = (FloatingActionButton) findViewById(R.id.float_button);

        /**
         * ======基本设置
         */
        webView.setBackgroundColor(Color.argb(150,240,224,225));
        webView.setHorizontalScrollBarEnabled(false);  //水平滚动条不显示
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY); //WebView内部显示
        //初始缩放比例 50%
//        webView.setInitialScale(50);
        webView.loadUrl("http://blog.csdn.net/mxiaochao/article/details/52832719");
//        webView.loadUrl("http://192.168.31.2:8080/soubug/");
        settings = webView.getSettings();

        settings.setUseWideViewPort(true);    //支持 viewport
        settings.setLoadWithOverviewMode(true);   //自适应屏幕

        //保留缩放功能 隐藏缩放控件
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);        //支持缩放

        /**
         * ===== ChromeClient  ViewClient  对象设置
         */

        viewClient = new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "shouldOverrideUrlLoading: url="+url);
                view.loadUrl(url);
                return true;

            }
        };
        chromeClient = new WebChromeClient(){
            //获取 title
            @Override
            public void onReceivedTitle(WebView view, String title){
                tvTitle.setText(title);
            }
            //获取 icon
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                ImgIcon.setImageBitmap(icon);
            }
        };

        webView.setWebViewClient(viewClient);
        webView.setWebChromeClient(chromeClient);
        //滚动条监听
        webView.setOnScrollChangeListener(scrollChangeLis);




    }
    private   View.OnScrollChangeListener scrollChangeLis = new View.OnScrollChangeListener() {
        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                Log.e(TAG, "onScrollChange: scrollX="+scrollX);
//                Log.e(TAG, "onScrollChange: oldScrollX="+oldScrollX);
            Log.e(TAG, "onScrollChange: scrollY="+scrollY);
//                Log.e(TAG, "onScrollChange: oldScrollY="+oldScrollY);
            if (scrollY>0){
                floatButton.setVisibility(View.VISIBLE);
            }else{
                floatButton.setVisibility(View.GONE);
            }
            dy = scrollY;
        }
    };
    @Override
    public void onBackPressed() {
        //判断 WebView 是否可以回退
        if (webView.canGoBack()){
            webView.goBack();
        }else{
                Log.e(TAG, "onBackPressed: currentTimeMillis="+System.currentTimeMillis());
                Log.e(TAG, "onBackPressed: exit="+exit);
                Log.e(TAG, "onBackPressed: currentTimeMillis- exit="+(System.currentTimeMillis()-exit));
            if (System.currentTimeMillis()-exit >2000){
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                exit= System.currentTimeMillis();
            }else{
                super.onBackPressed();

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.float_button:
                Log.i(TAG, "onClick: dy="+dy);
                webView.scrollTo(0,0);
                floatButton.setVisibility(View.GONE);
                break;
            case R.id.add:
                break;
        }
    }
}
