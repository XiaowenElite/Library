package edu.zju.com;

import android.app.Application;
import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

/**
 * Created by bin on 16/11/17.
 * 初始化okgo
 */

public class LibraryApp extends Application {


    private static Context context;

    public static Context getContext() {

        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.init(this);
        context = getApplicationContext();

        try {
            OkGo.getInstance()
                    .debug("OkGo")
                    .setConnectTimeout(10000)  //鍏ㄥ眬鐨勮繛鎺ヨ秴鏃舵椂闂�
                    .setReadTimeOut(5000)     //鍏ㄥ眬鐨勮鍙栬秴鏃舵椂闂�
                    .setWriteTimeOut(5000)    //鍏ㄥ眬鐨勫啓鍏ヨ秴鏃舵椂闂�
                    .setCacheMode(CacheMode.NO_CACHE)
                    .setRetryCount(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
