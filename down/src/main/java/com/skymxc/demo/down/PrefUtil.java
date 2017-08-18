package com.skymxc.demo.down;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mxc on 2017/8/17.
 * description:
 */

public class PrefUtil {
    private static Context context;
    public static void init(Context context){
        PrefUtil.context  =  context;
    }
    private static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

    public static void putProgress(String url,int progress){
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(url,progress);
        edit.commit();
    }
    public static int getProgress(String url){
        return preferences.getInt(url,0);
    }
    public static void removeProgress(String url){
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove(url);
        edit.commit();
    }
}
