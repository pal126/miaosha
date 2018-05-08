package com.pal.miaosha.service;

import com.pal.miaosha.dao.UserDaoa;
import com.pal.miaosha.domain.Usera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServicea {

    @Autowired
    UserDaoa userDao;

    public Usera getById(int id) {
        return userDao.getById(id);
    }

}
