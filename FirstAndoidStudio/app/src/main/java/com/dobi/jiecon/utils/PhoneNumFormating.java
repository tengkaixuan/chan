package com.dobi.jiecon.utils;

/**
 * Created by rock on 15/2/7.
 */
public class PhoneNumFormating {
    public static String telNumberRefine(String phone_num) {
        return phone_num.replace(" ", "").replace("-", "");
    }
}
