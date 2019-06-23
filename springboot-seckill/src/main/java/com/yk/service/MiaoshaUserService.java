package com.yk.service;


import com.yk.dao.MiaoshaUserDao;
import com.yk.pojo.LoginVo;
import com.yk.pojo.MiaoshaUser;
import com.yk.redis.RedisService;
import com.yk.redis.prefix.MiaoshaUserKey;
import com.yk.result.CodeMsg;
import com.yk.utils.MD5Util;
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
    private MiaoshaUserDao miaoshaUserDao;

    @Autowired
    private RedisService redisService;

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

    public CodeMsg login(HttpServletResponse response, LoginVo loginVo) {
        if(loginVo==null) {
            return CodeMsg.SERVER_ERROR;
        }
        //经过了依次MD5的密码
        String mobile=loginVo.getMobile();
        String formPass=loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user=getById(Long.parseLong(mobile));
        if(user == null){
            return CodeMsg.NO_USER;
        }

        String dbPass=user.getPwd();
        String dbSalt=user.getSalt();
        System.out.println("dbPass:"+dbPass+"   dbSalt:"+dbSalt);
        //验证密码，计算二次MD5出来的pass是否与数据库一致
        String tmppass=MD5Util.formPassToDBPass(formPass, dbSalt);
        if(!tmppass.equals(dbPass)) {
            return CodeMsg.PASSWORD_ERROR;
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return CodeMsg.SUCCESS;
    }


    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {

        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
