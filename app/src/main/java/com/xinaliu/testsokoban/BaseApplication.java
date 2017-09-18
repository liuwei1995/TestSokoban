package com.xinaliu.testsokoban;

import android.app.Application;
import android.os.Build;
import android.widget.Toast;

import com.xinaliu.testsokoban.util.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Field;

/**
 * Created by liuwei on 2017/3/20
 */

public class BaseApplication extends Application{

    public static final int SDK = Build.VERSION.SDK_INT;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashUtils.getInstance().init(this);
        try {
            byte[] bytes =  FileUtils.readFileFromAssetsByFileNameToBytes(this,"game.json");
            String s = new String(bytes);
            JSONArray jsonArray = new JSONArray(s);

            int[][][] gameDataArray = new int[jsonArray.length()][][];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray jsonArray1 = jsonArray.getJSONArray(i);
                int[][] ints = new int[jsonArray1.length()][];
                gameDataArray[i] = ints;
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONArray jsonArray2 = jsonArray1.getJSONArray(j);
                    int[] ints1 = new int[jsonArray2.length()];
                    ints[j] = ints1;
                    for (int k = 0; k < jsonArray2.length(); k++) {
                        ints1[k] = jsonArray2.getInt(k);
                    }
                }
            }
            try {
                Field gameDataArrayField = GameStateDataProvider.class.getDeclaredField("gameDataArray");
                gameDataArrayField.setAccessible(true);
                gameDataArrayField.set(null,gameDataArray);
            } catch (NoSuchFieldException e) {
                Toast.makeText(this,"NoSuchFieldException="+e.toString(),Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                Toast.makeText(this,"IllegalAccessException="+e.toString(),Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } catch (JSONException e) {
//            Toast.makeText(this,"JSONException="+e.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
