package edu.zju.com;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.store.PersistentCookieStore;

/**
 * Created by bin on 16/11/17.
 * 初始化okgo
 */

public class LibraryApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.init(this);

        try {
            //浠ヤ笅閮戒笉鏄繀椤荤殑锛屾牴鎹渶瑕佽嚜琛岄�夋嫨,涓�鑸潵璇村彧闇�瑕� debug,缂撳瓨鐩稿叧,cookie鐩稿叧鐨� 灏卞彲浠ヤ簡
            OkGo.getInstance()

                    //鎵撳紑璇ヨ皟璇曞紑鍏�,鎺у埗鍙颁細浣跨敤 绾㈣壊error 绾у埆鎵撳嵃log,骞朵笉鏄敊璇�,鏄负浜嗘樉鐪�,涓嶉渶瑕佸氨涓嶈鍔犲叆璇ヨ
                    .debug("OkGo")
                    //濡傛灉浣跨敤榛樿鐨� 60绉�,浠ヤ笅涓夎涔熶笉闇�瑕佷紶
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //鍏ㄥ眬鐨勮繛鎺ヨ秴鏃舵椂闂�
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //鍏ㄥ眬鐨勮鍙栬秴鏃舵椂闂�
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //鍏ㄥ眬鐨勫啓鍏ヨ秴鏃舵椂闂�

                    //鍙互鍏ㄥ眬缁熶竴璁剧疆缂撳瓨妯″紡,榛樿鏄笉浣跨敤缂撳瓨,鍙互涓嶄紶,鍏蜂綋鍏朵粬妯″紡鐪� github 浠嬬粛 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.NO_CACHE)

                    //鍙互鍏ㄥ眬缁熶竴璁剧疆缂撳瓨鏃堕棿,榛樿姘镐笉杩囨湡,鍏蜂綋浣跨敤鏂规硶鐪� github 浠嬬粛
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

                    .setCookieStore(new PersistentCookieStore());    //cookie鎸佷箙鍖栧瓨鍌紝濡傛灉cookie涓嶈繃鏈燂紝鍒欎竴鐩存湁鏁�

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
