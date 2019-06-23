package com.yk.service;


import com.yk.dao.MiaoshaUserDao;
import com.yk.pojo.MiaoshaUser;
import com.yk.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiaoshaUserService {
	public static final String COOKIE1_NAME_TOKEN="token";
	
	@Autowired
	MiaoshaUserDao miaoshaUserDao;
	@Autowired
	RedisService redisService;
	/**
	 * 根据id取得对象，先去缓存中取
	 * @param id
	 * @return
	 */
	public MiaoshaUser getById(long id) {
		return miaoshaUserDao.getById(id);
	}

}
