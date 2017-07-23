package com.skymxc.drag.practicenetwork_volley;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    private ListView lv ;
    private List<Lession> lessions;
    private LessonAdapter adapter;
    private Picasso picasso;
    private EditText etUrl;
    private ImageView image0;
    private ImageView image1;
    private NetworkImageView image2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv);
        etUrl = (EditText) findViewById(R.id.url);
        image0 = (ImageView) findViewById(R.id.image0);
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (NetworkImageView) findViewById(R.id.image2);
        lessions = new ArrayList<>();
        adapter= new LessonAdapter();
        lv.setAdapter(adapter);
        picasso = Picasso.with(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.go:
                //检查版本更新
                checkVersion();

                break;
            case R.id.load_data:
                loadData();
                break;
            case R.id.load_image_request:
                loadImageRequest();
                break;
            case R.id.load_image_loader:
                loadImageLoader();
                break;
            case R.id.load_image_network:
                loadNetWorkImageView();
                break;
        }
    }

    /**
     * 使用 NetWorkImageView
     */
    private void loadNetWorkImageView() {
        String url = etUrl.getText().toString();
        image2.setDefaultImageResId(R.mipmap.ic_launcher);//  网络加载前的占位图
        image2.setErrorImageResId(R.mipmap.jiantou);        //加载错误时提示图
        image2.setImageUrl(url,Http.getLoader(this, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return null;
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {

            }
        }));
    }

    /**
     * 使用Loader加载图片
     */
    private void loadImageLoader() {
        String url = etUrl.getText().toString();
        ImageLoader.ImageCache cache = new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                Log.e("Tag","====getBitmap()从缓存加载====="+s);
                //((BitmapDrawable) (image0.getDrawable())).getBitmap()
                return null;
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                Log.e("Tag","====putBitmap()放入缓存====="+s);
            }
        };
        ImageLoader loader = Http.getLoader(this,cache);
        loader.get(url, new ImageLoader.ImageListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this,"网络访问出错，请稍后重试",Toast.LENGTH_SHORT).show();
                Log.e("Tag",volleyError+"");
                image1.setImageResource(R.mipmap.jiantou);
            }

            /**
             * 这个方法会被调用两次 缓存处理一次 网络加载一次
             * @param imageContainer 网络请求信息
             * @param b 区分是缓存（true） 还是网络加载
             */
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                Log.e("Tag","===onResponse()======缓存加载:"+b+"====bitmap:"+imageContainer.getBitmap()+"=====url:"+imageContainer.getRequestUrl());

                Bitmap bmp = imageContainer.getBitmap();
                if (bmp!=null){//这里需要注意 如果没有使用缓存 从缓存中读取的Bitmap就是空的，
                    image1.setImageBitmap(bmp);
                }

            }
        },(int)getResources().getDimension(R.dimen.image_w),(int)getResources().getDimension(R.dimen.image_h));


    }

    /**
     * ImageRequest加载图片
     */
    private void loadImageRequest() {
        String url =etUrl.getText().toString();
    Http.getInstance(this).execImageRequest(url,
                    new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    if (bitmap!=null){
                        image0.setImageBitmap(bitmap);
                    }
                }
            },
            (int) getResources().getDimension(R.dimen.image_w),
            (int)getResources().getDimension(R.dimen.image_h),
            Bitmap.Config.ARGB_8888,
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MainActivity.this,"网络访问出错，请稍后重试",Toast.LENGTH_SHORT).show();
                }
            });
    }

    /**
     * Get方式加载数据
     */
    private void loadData(){
        String url ="http://toolsmi.com/starclass/lessons";
        Http.getInstance(this).execStringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
            if (!TextUtils.isEmpty(s)){
                Result<List<Lession>> result = JSON.parseObject(s,new TypeReference<Result<List<Lession>>>(){});
                Toast.makeText(MainActivity.this,result.describe,Toast.LENGTH_SHORT).show();
                lessions.addAll(result.data);
                adapter.notifyDataSetChanged();
            }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(MainActivity.this,"网络访问出错，请稍后重试",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 检查版本更新使用Post方式
     */
    private void checkVersion(){
        String url ="http://toolsmi.com/starclass/ver";
        Map<String,String> params = new HashMap<>();
        params.put("ver",BuildConfig.VERSION_CODE+"");
        Http.getInstance(this).execStringRequestPost(url,params, new Response.Listener<String>() {
            @Override
            public void onResponse(String str) {
                Log.e("Tag","======onResponse=============="+str);
                //将字符串解析为对象
                if (!TextUtils.isEmpty(str)) {
                    Result<VersionInfo> result = JSON.parseObject(str, new TypeReference<Result<VersionInfo>>() {
                    });
                    if (result.state ==1){
                        new AlertDialog.Builder(MainActivity.this).setMessage("目前版本"+BuildConfig.VERSION_NAME+",检查到新版本"+result.data.getVersionName()+"，是否更新?")
                                .setPositiveButton("立即更新",null)
                                .setNegativeButton("下次再议",null)
                                .show();
                    }else{
                        Toast.makeText(MainActivity.this,"目前已经是最新版本",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this,"网络出错，请稍后重试",Toast.LENGTH_SHORT).show();
            }
        });
    }

    class  LessonAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return lessions.size();
        }

        @Override
        public Lession getItem(int i) {
            return lessions.get(i);
        }

        @Override
        public long getItemId(int i) {
            return lessions.get(i).getTid();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder ;
            if (view==null){
                view =  LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_listview_item,null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            Lession lession = getItem(i);
            holder.tvDesc.setText(lession.getLdescribe());
            holder.tvTitle.setText(lession.getLname());
            picasso.load(lession.getThumbUrl())
                    .resizeDimen(R.dimen.image_w,R.dimen.image_h)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.jiantou)
                    .into(holder.image);
            return view;
        }

        class ViewHolder{
            ImageView image;
            TextView tvTitle;
            TextView tvDesc;
            public ViewHolder(View view){
                image = (ImageView) view.findViewById(R.id.image);
                tvTitle = (TextView) view.findViewById(R.id.title);
                tvDesc = (TextView)view. findViewById(R.id.desc1);
            }
        }
    }
}
