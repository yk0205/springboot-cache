package com.yk.service;

import com.yk.dao.UserDao;
import com.yk.pojo.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class UserService {

    private final Log log = LogFactory.getLog(this.getClass());

    @Resource
    private UserDao userDao;


    public User getUser(String username,String password){
        userDao.selectByPrimaryKey(0);
        return  null;
    }


}
