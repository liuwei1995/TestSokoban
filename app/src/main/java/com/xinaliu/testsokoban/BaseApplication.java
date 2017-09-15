package com.xinaliu.testsokoban;

import android.app.Application;
import android.os.Build;

import com.xinaliu.testsokoban.util.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by liuwei on 2017/3/20
 */

public class BaseApplication extends Application{

    public static final int SDK = Build.VERSION.SDK_INT;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            byte[] bytes =  FileUtils.readFileFromAssetsByFileNameToBytes(this,"game.json");
            String s = new String(bytes);
            JSONArray jsonArray = new JSONArray(s);

            int[][][] gameDataArray = new int[jsonArray.length()][][];
            
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray jsonArray1 = jsonArray.getJSONArray(i);
                int[][] ints = new int[jsonArray1.length()][];

                for (int j = 0; j < jsonArray1.length(); j++) {
                    
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
