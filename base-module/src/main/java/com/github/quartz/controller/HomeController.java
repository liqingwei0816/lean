package com.github.quartz.controller;

import com.github.config.security.phoneSecurity.CodeUtil;
import com.github.util.SpringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @RequestMapping({"/","index"})
    public String index() {
        return "index";
    }
    @RequestMapping({"loginPage"})
    public String login() {
        return "login";
    }

    /**
     * 获取手机验证码
     */
    @RequestMapping({"getPhoneCode"})
    @ResponseBody
    public String getPhoneCode(String mobile) {
        CodeUtil bean = SpringUtil.getBean(CodeUtil.class);
        return bean.createCode(mobile);
    }

}
