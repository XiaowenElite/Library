package edu.zju.com.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

import java.util.HashMap;

/**
 * Created by bin on 2016/11/19.
 */

public class JsonUtil {
    public static String toJson(HashMap<String, String> params){
        Gson gson = new Gson();
        return  gson.toJson(params);
    }

    //json->String
    public static <T> T fromJson(String jsonString,Class<T> tClass){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
        Gson gson = gsonBuilder.create();
        return  gson.fromJson(jsonString,tClass);
    }
}
