package com.skymxc.drag.getweather;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private  Resp resp;

    private Button bt ;

    private ListView lvHead;

    private ListView  lvMiddle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = (Button) findViewById(R.id.get_weather);
        lvHead= (ListView) findViewById(R.id.lv_header);
        lvHead.setDividerHeight(0);
        lvMiddle = (ListView) findViewById(R.id.lv_middle);
        lvMiddle.setDividerHeight(0);
    }

    public void click(View v){
        switch (v.getId()){
            case R.id.get_weather:
                String url ="http://wthrcdn.etouch.cn/WeatherApi?citykey=101010100";

                MyTask task = new MyTask();
                task.execute(url);
               Log.e("Tag","======"+resp+"=======Size:");

                break;
        }
    }

    private  Resp getWeather(InputStream is){
        Resp resp =null;
        Weather weather = null;
        Map<String,String> otherMap = new HashMap<>();
        Map<String,String> envrimentMap = new HashMap<>();
        List<Weather> weathers = null;

        //创建pullParser解析器
        //设置解析内容
        //开始解析  封装到 Resp对象中
        try {
            Log.e("Tag","============"+is.available());
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser pullParser = factory.newPullParser();
            pullParser.setInput(is,"utf-8");
        //    pullParser.set

            int type = pullParser.getEventType();


            String levle ="";
            while (type!=XmlPullParser.END_DOCUMENT){

                String tag = pullParser.getName();

                switch (type){
                    case XmlPullParser.START_DOCUMENT:
                        Log.e("Tag","========START_DOCUMENT===========开始读取，创建Resp对象");
                        resp = new Resp();
                        break;
                    case XmlPullParser.START_TAG:
                        Log.e("Tag","=========START_TAG=====Tag:"+tag);
                        //等级标识
                        if (tag.equals("environment")){
                            levle="environment";
                        }else if( tag.equals("forecast")){
                            Log.e("Tag","=====创建 WeatherList");
                            weathers= new ArrayList<>();
                            levle="forecast";
                        }else if (tag.equals("resp")){
                            levle="resp";
                        }

                        if (levle.equals("resp")){
                            getOtherMap(otherMap, pullParser, tag);
                        }else if (levle.equals("environment")){
                            getEnvironment(envrimentMap, pullParser, tag);
                        }else if (levle.equals("forecast")){
                            if (tag.equals("weather")){
                                weather = new Weather();
                            }else{
                                switch (tag){
                                    case "date":
                                        weather.setDate(pullParser.nextText());
                                        break;
                                    case "high":
                                        weather.setHigh(pullParser.nextText());
                                        break;
                                    case "low":
                                        weather.setLow(pullParser.nextText());
                                        break;
                                }
                            }
                        }



                        break;
                    case XmlPullParser.END_TAG:

                        Log.e("Tag","======END_TAG===="+tag);
                        if (tag.equals("weather")){
                            weathers.add(weather);
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                }
                type= pullParser.next();
            }



        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        resp.setOtherMap(otherMap);
        resp.setEnvirmentMap(envrimentMap);
        resp.setWeathers(weathers);

        return resp;
    }

    private void getOtherMap(Map<String, String> otherMap, XmlPullParser pullParser, String tag) throws XmlPullParserException, IOException {
        switch (tag){
            case "city":
                otherMap.put("城市",pullParser.nextText());
                break;
            case "wendu":
                otherMap.put("温度",pullParser.nextText());
                break;
            case "fengli":
                otherMap.put("风力",pullParser.nextText());
                break;
            case "fengxiang":
                otherMap.put("风向",pullParser.nextText());
                break;
            case "shidu":
                otherMap.put("湿度",pullParser.nextText());
                break;
            case "sunrise_1":
                otherMap.put("日出",pullParser.nextText());
                break;
            case "sunset_1":
                otherMap.put("日落",pullParser.nextText());
                break;
        }
    }

    private void getEnvironment(Map<String, String> envrimentMap, XmlPullParser pullParser, String tag) throws XmlPullParserException, IOException {
        switch (tag){
            case "aqi":
                envrimentMap.put("aqi",pullParser.nextText());
                break;
            case "pm25":
                envrimentMap.put("pm2.5",pullParser.nextText());
                break;
            case "suggest":
                envrimentMap.put("建议",pullParser.nextText());
                break;
            case "quality":
                envrimentMap.put("空气状况",pullParser.nextText());
                break;
            case "MajorPollutants":
                envrimentMap.put("MajorPollutants",pullParser.nextText());
                break;
            case "no2":
                envrimentMap.put("no2",pullParser.nextText());
                break;
            case "time":
                envrimentMap.put("time",pullParser.nextText());
                break;
            case "o3":
                envrimentMap.put("o3",pullParser.nextText());
                break;
            case "co":
                envrimentMap.put("co",pullParser.nextText());
                break;
            case "pm10":
                envrimentMap.put("pm10",pullParser.nextText());
                break;
            case "so2":
                envrimentMap.put("so2",pullParser.nextText());
                break;

        }
    }


    class  MyTask extends AsyncTask<String,Void,Resp>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bt.setText("获取中....");
            bt.setEnabled(false);

        }

        @Override
        protected Resp doInBackground(String... strings) {
          //  Resp resp =null;
            String urlStr = strings[0];
            HttpURLConnection connection =null;

            try {
                URL url = new URL(urlStr);
                connection= (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                int code = connection.getResponseCode();
                Log.e("Tag","=========响应码==="+code);
                if (code==200){
                  InputStream is=connection.getInputStream();
                    Log.e("Tag","====is长度"+is.available()+"======"+connection.getContentLength());
                    resp= getWeather(is);
                    return resp;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (connection!=null){
                    connection.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Resp resp) {




            List<Map<String,String>> headerdata = new ArrayList<>();
            Map<String,String> header1 = new HashMap<>();
            header1.put("text1","城市："+resp.getOtherMap().get("城市"));
            headerdata.add(header1);
            Map<String,String> header2 = new HashMap<>();
            header2.put("text1","温度:"+resp.getOtherMap().get("温度")+"度");
            headerdata.add(header2);
            Map<String,String> header3 = new HashMap<>();
            header3.put("text1","风力:"+resp.getOtherMap().get("风力"));
            header3.put("text2","风向："+resp.getOtherMap().get("风向"));
            headerdata.add(header3);
            Map<String,String> header4 = new HashMap<>();
            header4.put("text1","湿度:"+resp.getOtherMap().get("湿度"));
            headerdata.add(header4);
            Map<String,String> header5 = new HashMap<>();
            header5.put("text1","日出："+resp.getOtherMap().get("日出"));
            header5.put("text2","日落："+resp.getOtherMap().get("日落"));
            headerdata.add(header5);
            Map<String,String> header6 = new HashMap<>();
            header6.put("text1","pm2.5:"+resp.getEnvirmentMap().get("pm2.5"));
            header6.put("text2","空气状况:"+resp.getEnvirmentMap().get("空气状况"));
            headerdata.add(header6);
            Map<String,String> header7 = new HashMap<>();
            header7.put("text1","建议:"+resp.getEnvirmentMap().get("建议"));
            headerdata.add(header7);

            SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this,headerdata,R.layout.weahter_header_layout,new String[]{"text1","text2"},new int[]{R.id.text1,R.id.text2});

            lvHead.setAdapter(simpleAdapter);


            List<Map<String,String>> middleData =new ArrayList<>();

            Map<String,String> weather1 = new HashMap<>();
            weather1.put("date",resp.getWeathers().get(1).getDate());
            weather1.put("high",resp.getWeathers().get(1).getHigh());
            weather1.put("low",resp.getWeathers().get(1).getLow());
            middleData.add(weather1);
            Map<String,String> weather2 = new HashMap<>();
            weather2.put("date",resp.getWeathers().get(2).getDate());
            weather2.put("high",resp.getWeathers().get(2).getHigh());
            weather2.put("low",resp.getWeathers().get(2).getLow());
            middleData.add(weather2);
            Map<String,String> weather3 = new HashMap<>();
            weather3.put("date",resp.getWeathers().get(3).getDate());
            weather3.put("high",resp.getWeathers().get(3).getHigh());
            weather3.put("low",resp.getWeathers().get(3).getLow());
            middleData.add(weather3);
            Map<String,String> weather4 = new HashMap<>();
            weather4.put("date",resp.getWeathers().get(4).getDate());
            weather4.put("high",resp.getWeathers().get(4).getHigh());
            weather4.put("low",resp.getWeathers().get(4).getLow());
            middleData.add(weather4);



            SimpleAdapter simpleAdapter1 = new SimpleAdapter(MainActivity.this,middleData,R.layout.weahter_middle_layout,new String[]{"date","high","low"},new int[]{R.id.date,R.id.high,R.id.low});


            lvMiddle.setAdapter(simpleAdapter1);

            bt.setText("获取天气状况");
            bt.setEnabled(true);
            Log.e("Tag","========长度"+resp.getOtherMap().size());
            Log.e("Tag","========长度"+resp.getEnvirmentMap().size());
            Log.e("Tag","========长度"+resp.getWeathers().size());

        }
    }

}
