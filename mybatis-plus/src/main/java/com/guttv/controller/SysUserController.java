package com.guttv.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.guttv.bean.SysUser;
import com.guttv.service.SysUserService;
import com.guttv.util.ResultUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;

@RestController
@RequestMapping("sysUser")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @RequestMapping("list")
    public ModelAndView list(ModelAndView model) {
        model.setViewName("sysUser/list");
        return model;
    }

    @RequestMapping("dataList")
    public ResultUtils dataList(SysUser sysUser) {

        PageInfo<Object> pageInfo = PageHelper.startPage(sysUser.getPageNum(), sysUser.getPageSize()).doSelectPageInfo(() -> sysUserService.getList(sysUser));
        return ResultUtils.success(pageInfo);
    }


    @RequestMapping("delete")
    public ResultUtils delete(SysUser sysUser) {
        Integer delete = this.sysUserService.delete(sysUser.getId());
        return ResultUtils.success(delete);
    }
}
