package com.pal.miaosha.access;

import com.pal.miaosha.domain.User;

/**
 * 多线程下保证线程安全的一种访问方式
 *
 * @author pal
 */
public class UserContext {

    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<User>();

    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

    public static User getUser() {
        return userThreadLocal.get();
    }

    public static void remove() {
        userThreadLocal.remove();
    }

}
