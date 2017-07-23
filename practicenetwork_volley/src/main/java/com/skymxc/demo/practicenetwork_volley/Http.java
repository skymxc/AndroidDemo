package com.skymxc.drag.practicenetwork_volley;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * Created by sky-mxc
 */
public class Http {
    private Context context;
    private RequestQueue queue;
    private static Http http;
    private static ImageLoader loader ;

    private Http(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }

    public static Http getInstance(Context context) {
        if (http == null) {
            http = new Http(context);
        }
        return http;
    }

    /**
     * 获取ImageLoad 实例
     * @param context 上下文
     * @param cache ImageCache 缓存
     * @return ImageLoader
     */
    public static ImageLoader getLoader(Context context, ImageLoader.ImageCache cache){
        if (loader==null){
            loader = new ImageLoader(getInstance(context).queue,cache);
        }
        return loader;
    }



    /**
     * String 请求 get方式 （default）
     * @param url 地址
     * @param successListener 成功监听
     * @param errorListener 失败监听
     */
    public void execStringRequest(String url, Response.Listener<String> successListener, Response.ErrorListener errorListener){
        StringRequest getRequest = new StringRequest(url,successListener ,errorListener);
        queue.add(getRequest);

    }

    /**
     *  String 请求 post方式
     * @param url 地址
     * @param params 参数 Map结构
     * @param successListener 成功监听
     * @param errorListener 失败监听
     */
    public void execStringRequestPost(String url,final Map<String,String> params, Response.Listener<String> successListener, Response.ErrorListener errorListener){
        StringRequest getRequest = new StringRequest(Request.Method.POST,url,successListener ,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return params;
            }
        };
        queue.add(getRequest);

    }

    /**
     * 执行图片的加载
     * @param url 地址
     * @param successListener 成功监听
     * @param maxWidth 最大宽度 px
     * @param maxHeight 最大高度 px
     * @param config 清晰度
     * @param errorListener 错误监听
     */
    public void execImageRequest(String url, Response.Listener<Bitmap> successListener, int maxWidth, int maxHeight, Bitmap.Config config, Response.ErrorListener errorListener){
        ImageRequest request = new ImageRequest(url,successListener,maxWidth,maxHeight, config,errorListener);
        queue.add(request);
    }
}
