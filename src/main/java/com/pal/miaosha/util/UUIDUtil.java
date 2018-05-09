package com.pal.miaosha.util;

import java.util.UUID;

public class UUIDUtil {

    /**
     * JDK自带UUID,不带"-"
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
