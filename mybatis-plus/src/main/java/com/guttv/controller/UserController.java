package com.guttv.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.guttv.bean.User;
import com.guttv.service.UserService;
import com.guttv.util.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("list")
    public ModelAndView list(ModelAndView model) {
        model.setViewName("user/list");
        return model;
    }

    @RequestMapping("dataList")
    public ResultUtils dataList(User user) {

        PageInfo<Object> pageInfo = PageHelper.startPage(user.getPageNum(), user.getPageSize()).doSelectPageInfo(() -> userService.getList(user));
        return ResultUtils.success(pageInfo);
    }
}
