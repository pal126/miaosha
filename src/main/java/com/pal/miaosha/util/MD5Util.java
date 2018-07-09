package com.pal.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    private static final String salt = "a912jdssfj3lpf";

    public static String MD5(String str) {
        return DigestUtils.md5Hex(str);
    }

    public static String inputPassToFormPass(String input) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + input + salt.charAt(5) + salt.charAt(6);
        return MD5(str);
    }

    public static String formPassToDBPass(String formPass, String saltDB) {
        String str = "" + saltDB.charAt(0) + saltDB.charAt(2) + formPass + saltDB.charAt(5) + saltDB.charAt(6);
        return MD5(str);
    }

    public static String inputPassToDBPass(String input, String saltDB) {
        String formPass = inputPassToFormPass(input);
        return formPassToDBPass(formPass, saltDB);
    }
}
