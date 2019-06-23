package com.yk.service;


import com.yk.dao.MiaoshaUserDao;
import com.yk.pojo.LoginVo;
import com.yk.pojo.MiaoshaUser;
import com.yk.redis.RedisService;
import com.yk.redis.prefix.MiaoshaUserKey;
import com.yk.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Resource
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    /**
     * 根据id取得对象，先去缓存中取
     *
     * @param id
     * @return
     */
    public MiaoshaUser getById(long id) {
        return miaoshaUserDao.getById(id);
    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        // 延长有效期
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

    public void login(HttpServletResponse response, LoginVo loginVo) {
        MiaoshaUser user = new MiaoshaUser();
        user.setNickname(loginVo.getMobile());
        user.setPwd(loginVo.getPassword());
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
    }


    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {

        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
