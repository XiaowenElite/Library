package edu.zju.com;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by bin on 16/11/17.
 * 初始化okgo
 */

public class LibraryApp extends Application {

    private List<Activity> mList = new LinkedList<Activity>();
    private static LibraryApp instance;
    private static Context context;

    public static Context getContext() {

        return context;
    }


    public synchronized static LibraryApp getInstance() {
        if (null == instance) {
            instance = new LibraryApp();
        }
        return instance;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //  System.exit(0);//去掉这个
        }
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
