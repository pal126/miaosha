package com.pal.miaosha.service;

import com.pal.miaosha.dao.UserDao;
import com.pal.miaosha.domain.User;
import com.pal.miaosha.exception.GlobalException;
import com.pal.miaosha.redis.RedisService;
import com.pal.miaosha.redis.UserKey;
import com.pal.miaosha.result.CodeMsg;
import com.pal.miaosha.util.MD5Util;
import com.pal.miaosha.util.UUIDUtil;
import com.pal.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    UserDao userDao;

    @Autowired
    RedisService redisService;

    public User getById(Long id) {
        return userDao.getById(id);
    }

    public User getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(UserKey.token, token, User.class);
        //延长有效期
        if(user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

    public Boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        User user = getById(Long.valueOf(mobile));
        //手机号是否存在
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String pass = MD5Util.formPassToDBPass(loginVo.getPassword(),user.getSalt());
        //密码是否正确
        if (!pass.equals(user.getPassword())) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, User user) {
        redisService.set(UserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
