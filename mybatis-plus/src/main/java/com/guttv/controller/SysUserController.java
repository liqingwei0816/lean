package com.guttv.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.guttv.bean.SysUser;
import com.guttv.service.SysUserService;
import com.guttv.util.ResultUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
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


    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("addOrUpdate")
    public ResultUtils addOrUpdate(@RequestBody SysUser sysUser) {

        Assert.notNull(sysUser, "参数 userName 和 password 不能为空");
        Assert.notNull(sysUser.getUserName(), "参数 userName 不能为空");
        Assert.notNull(sysUser.getPassword(), "参数  password 不能为空");
        SysUser sysUserDb = sysUserService.getByUserName(sysUser.getUserName());
        Integer count;
        if (sysUser.getId() == null) {
            if (sysUserDb != null) {
                return ResultUtils.error("用户已存在");
            }
            //add
            sysUser.setPassword(bCryptPasswordEncoder.encode(sysUser.getPassword()));
            count = sysUserService.insert(sysUser);
        } else {
            if (sysUserDb == null) {
                return ResultUtils.error("用户不存在");
            }
            //update
            count = sysUserService.updateById(sysUser);
        }
        return ResultUtils.success(count);
    }

    @PostMapping("delete")
    public ResultUtils delete(@RequestBody SysUser sysUser) {
        Integer delete = this.sysUserService.delete(sysUser.getId());
        return ResultUtils.success(delete);
    }

    //todo 管理员角色绑定问题  查看按钮问题

}
