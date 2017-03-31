package edu.zju.com.utils;

/**
 * Created by bin on 2016/11/19.
 */

public class HttpContant {

    private static final String HTTP = "http://";

    private static final String HTTPS = "https://";

    private static final String SERVER_HOST = " ";

    //private static String SERVER_ADDR = "60.12.220.16";
    private static String SERVER_ADDR = "60.12.220.16";

    //60.12.220.16
    //10.82.81.130

    private static final String ENCRYPTION_PATH = "";

    private static final String UNENCRYPTION_PATH = "/smartlibrary/IOSAPI/index.php/Home/Index/";

    private static String HTTP_PORT = "9998";

    private static final String HTTPS_PORT = "443";


    public static String getEncryptionPath() {
        return HTTPS + SERVER_HOST + ENCRYPTION_PATH;
    }

    public static String getUnencryptionPath() {
        //return HTTP + getServerAddr() + ":" + getHttpPort() + UNENCRYPTION_PATH;
        return HTTP + SERVER_ADDR + ":" + HTTP_PORT + UNENCRYPTION_PATH;


    }


    public static String getHttpPort() {
        return Operator.getValueFormPreferences("port", "9999");
    }

    public static void setHttpPort(String httpPort) {
        Operator.setValueToPreferences("port", httpPort);
    }

    public static String getServerAddr() {
        return Operator.getValueFormPreferences("serverAddr", "60.12.220.16");
    }

    public static void setServerAddr(String serverAddr) {
        Operator.setValueToPreferences("serverAddr", serverAddr);
    }
}
