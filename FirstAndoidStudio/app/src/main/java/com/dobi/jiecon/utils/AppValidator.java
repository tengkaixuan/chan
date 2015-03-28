package com.dobi.jiecon.utils;

/**
 * Created by rock on 15/1/18.
 */
public class AppValidator {

    public static boolean isValideApp(String appName){
        if (appName.equals("launcher")
            ||appName.equals("settings")
                )
            return false;
        return true;
    }
}
