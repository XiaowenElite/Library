package edu.zju.com.utils;

import android.content.Context;
import android.content.SharedPreferences;

import edu.zju.com.LibraryApp;

/**
 * Created by lixiaowen on 16/12/17.
 */

public class Operator {
    static SharedPreferences getSharedPreferences() {

        return LibraryApp.getContext().getSharedPreferences("SzCloud", Context.MODE_PRIVATE);
    }

    static SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }

    static void setValueToPreferences(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    static String getValueFormPreferences(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    static void setValueToPreferences(String key, boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    static Boolean getValueFromPreference(String key, Boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }
}
