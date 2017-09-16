package com.mylibrary.utils;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


/**
 * 解析joson
 *
 * @author chirs
 */
public class GsonUtils {

    /**
     * 将json解析成java对象
     *
     * @param json
     * @param cls
     * @return
     */
    public static <T> T parseFromJson(String json, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(json, cls);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Gson", "json or class error , from  " + cls.getSimpleName() + " error json :" + json);
        }
        return t;
    }

    /**
     * 将json解析为java对象列表
     *
     * @param json
     * @param cls
     * @return
     */
    public static <T> List<T> parseFromJsons(String json, Class<T> cls) {
        List<T> listT = new ArrayList<T>();
        try {
            JSONArray ja = new JSONArray(json);
            for (int i = 0; i < ja.length(); i++) {
                listT.add(parseFromJson(ja.getString(i), cls));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Gson", "json or class error , from  " + cls.getSimpleName() + " error json :" + json);
        }
        return listT;
    }

    /**
     * 将java对象解析为json
     *
     * @param cls
     * @return
     */
    public static <T> String parseToJson(Class<T> cls) {
        String json = "";
        try {
            Gson gson = new Gson();
            json = gson.toJson(cls);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Gson", "class error , from " + cls.getSimpleName());
        }
        return json;
    }

    /**
     * 将java对象列表解析为json
     *
     * @param clsList
     * @return
     */
    public static <T> String parseToJsons(List<T> clsList) {
        String json = "";
        try {
            Gson gson = new Gson();
            json = gson.toJson(clsList);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Gson", "class list error , please check");
        }
        return json;
    }


}
