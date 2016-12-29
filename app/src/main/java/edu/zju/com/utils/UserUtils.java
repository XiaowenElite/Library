package edu.zju.com.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lixiaowen on 16/12/13.
 */

public class UserUtils {


    public static String getUsername() {
        return Operator.getValueFormPreferences("username", null);
    }

    public static void setUsername(String username) {
        Operator.setValueToPreferences("username", username);
    }

    public static String getPassword() {
        return Operator.getValueFormPreferences("password", null);
    }

    public static void setPassword(String password) {
        Operator.setValueToPreferences("password", password);
    }

    public static void setCurrentPage(String currentPage){
        Operator.setValueToPreferences("currentpage",currentPage);
    }

    public static String getCurrentPage(){
        return Operator.getValueFormPreferences("currentpage","0");
    }


    public static void setCurrentEmPage(String currentPage){
        Operator.setValueToPreferences("currentempage",currentPage);
    }

    public static String getEmCurrentPage(){
        return Operator.getValueFormPreferences("currentempage","0");
    }

    public static void setAirname(String airname){
        Operator.setValueToPreferences("airname",airname);
    }

    public static String getAirname(){
        return Operator.getValueFormPreferences("airname",null);
    }

    public static void setAircmd(String aircam){
        Operator.setValueToPreferences("aircam",aircam);
    }

    public static String getAircmd(){
        return Operator.getValueFormPreferences("aircam",null);
    }


    public static void setAiraddr(String airaddr){
        Operator.setValueToPreferences("airaddr",airaddr);
    }

    public static String getAiraddr(){
        return Operator.getValueFormPreferences("airaddr",null);
    }


    public static void setAirroute(String airroute){
        Operator.setValueToPreferences("airroute",airroute);
    }

    public static String getAirroute(){
        return Operator.getValueFormPreferences("airroute",null);
    }


    public static boolean isIpAddress(String ipAddress){
        Pattern p = Pattern.compile("(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)");
        Matcher m = p.matcher(ipAddress);
        return m.matches();
    }

    public static boolean isPort(String port){
        Pattern p = Pattern.compile("[1-9]\\d*");
        Matcher m = p.matcher(port);
        if (m.matches()){
            int pp = Integer.parseInt(port);
            if (pp>1&&pp<65535){
                return true;
            }
        }
        return false;
    }
}
