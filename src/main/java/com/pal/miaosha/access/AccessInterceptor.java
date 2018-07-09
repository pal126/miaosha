package com.pal.miaosha.access;

import com.alibaba.fastjson.JSON;
import com.pal.miaosha.domain.User;
import com.pal.miaosha.redis.AccessKey;
import com.pal.miaosha.redis.RedisService;
import com.pal.miaosha.result.CodeMsg;
import com.pal.miaosha.result.Result;
import com.pal.miaosha.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * AccessLimit拦截器
 *
 * @author pal
 */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    /**
     * 拦截业务处理
     * preHandle在进入controller之前调用
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @return boolean
     * @throws Exception Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            //user信息存入threadLocal
            User user = getUser(request, response);
            UserContext.setUser(user);

            //获取@AccessLimit注解
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            //验证登陆信息
            String key = request.getRequestURI();
            if (needLogin) {
                if (user == null) {
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
                key += "_" + user.getId();
            }

            //接口限流
            AccessKey accessKey = AccessKey.withExpire(seconds);
            Integer count = redisService.get(accessKey, key, Integer.class);
            if (count == null) {
                redisService.set(accessKey, key, 1);
            } else if (count < maxCount) {
                redisService.incr(accessKey, key);
            } else {
                render(response, CodeMsg.REQUEST_OVER_LIMIT);
                return false;
            }
        }
        return true;
    }

    /**
     * 关闭threadLocal
     * 在所有动作完成后调用afterCompletion
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @param ex       ex
     * @throws Exception Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }

    /**
     * 返回消息到前端
     *
     * @param response response
     * @param codeMsg  codeMsg
     * @throws Exception Exception
     */
    private void render(HttpServletResponse response, CodeMsg codeMsg) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(codeMsg));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(UserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, UserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        return userService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String string) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (string.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
