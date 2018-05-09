package com.pal.miaosha.service;

import com.pal.miaosha.dao.UserDao;
import com.pal.miaosha.domain.User;
import com.pal.miaosha.result.CodeMsg;
import com.pal.miaosha.util.MD5Util;
import com.pal.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getById(Long id) {
        return userDao.getById(id);
    }

    public CodeMsg login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        User user = getById(Long.valueOf(mobile));
        //手机号是否存在
        if (user == null) {
            return CodeMsg.MOBILE_NOT_EXIST;
        }
        String pass = MD5Util.formPassToDBPass(loginVo.getPassword(),user.getSalt());
        //密码是否正确
        if (!pass.equals(user.getPassword())) {
            return CodeMsg.PASSWORD_ERROR;
        }
        return CodeMsg.SUCCESS;
    }
}
