package com.tdqc.util;

import com.google.gson.Gson;

/**
 * Gson封装
 * Created by chenyen on 2017/6/26.
 */
public class GsonUtil {

    private static Gson gson = null;

    private static Gson getGson(){
        if(gson == null){
            gson = new Gson();
        }
        return gson;
    }

    public static synchronized String toJson(Object object){
        return getGson().toJson(object);
    }

    public static synchronized <T> T fromJson(String json, Class<T> clz){
        return getGson().fromJson(json,clz);
    }

}
