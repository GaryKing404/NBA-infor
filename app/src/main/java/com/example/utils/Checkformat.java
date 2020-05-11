package com.example.utils;

import com.blankj.utilcode.util.RegexUtils;

public class Checkformat {

    public static boolean checkActandpsw(String account, String psw) {
        if (!RegexUtils.isMobileExact(account)) {
            return false;
        } else if (psw.length() < 6) {
            return false;
        }
        return true;
    }

    public static boolean checkActandpsw12(String psw1, String psw2) {
//        if (!RegexUtils.isMobileExact(account)) {
//            return false;
//        }
        if (psw1.length() < 6) {
            return false;
        }
        if (psw2.length() < 6) {
            return false;
        }
        return true;
    }
}
