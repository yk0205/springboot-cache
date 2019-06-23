package com.yk.controller;



import com.yk.pojo.User;
import com.yk.redis.RedisService;
import com.yk.redis.prefix.UserKey;
import com.yk.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private RedisService redisService;


    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet (){
        User user = redisService.get(UserKey.getById,"key", User.class);
        return  Result.success(user);
    }
    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<User> redisSet (){
        User user = new User();
        user.setId(1);
        user.setName("111");
        boolean flag = redisService.set(UserKey.getById ,""+1, user);
        user = redisService.get(UserKey.getById,"key", User.class);
        return  Result.success(user);
    }




}
