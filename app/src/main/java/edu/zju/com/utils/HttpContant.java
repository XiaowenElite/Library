package edu.zju.com.utils;

/**
 * Created by bin on 2016/11/19.
 */

public class HttpContant {
    private static final String HTTP = "http://";

    private static final String HTTPS = "https://";

    private static final String SERVER_HOST = "60.12.220.16:9999/thinkphp/AndroidAPI/index.php/Home/Index/";

    private static final String ENCRYPTION_PATH = "";

    private static final String UNENCRYPTION_PATH = "";

    private static final Integer HTTP_PORT = 8000;

    private static final Integer HTTPS_PORT = 443;


    public static String getEncryptionPath() {
        return HTTPS + SERVER_HOST + ENCRYPTION_PATH;
    }

    public static String getUnencryptionPath() {
        return HTTP + SERVER_HOST + UNENCRYPTION_PATH;
    }

    public static Integer getHttpPort(){
        return HTTP_PORT;
    }

    public static Integer getHttpsPort(){
        return HTTPS_PORT;
    }
}
