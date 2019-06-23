package com.yk.controller;

import com.yk.pojo.LoginVo;
import com.yk.redis.RedisService;
import com.yk.result.CodeMsg;
import com.yk.result.Result;
import com.yk.service.MiaoshaUserService;
import com.yk.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final Log log = LogFactory.getLog(this.getClass());


    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }


    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, LoginVo loginVo) {
        log.info(loginVo.toString());
        //参数校验  使用了注解参数校验
        String passInput = loginVo.getPassword();
        String mobile = loginVo.getMobile();
        if (StringUtils.isEmpty(passInput)) {
            return Result.error(CodeMsg.PASSWORD_EMPTY);
        }
        if (StringUtils.isEmpty(mobile)) {
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }
        System.out.println("mobile：" + mobile);
        if (!ValidatorUtil.isMobile(mobile)) {//手机号验证不通过 false
            return Result.error(CodeMsg.MOBILE_ERROR);
        }
        miaoshaUserService.login(response,loginVo);

        return Result.success(true);
    }




}
