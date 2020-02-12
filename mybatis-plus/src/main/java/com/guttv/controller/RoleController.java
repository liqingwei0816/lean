package com.guttv.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.guttv.bean.Auth;
import com.guttv.bean.Role;
import com.guttv.service.RoleService;
import com.guttv.util.ResultUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @RequestMapping("list")
    public ModelAndView list(ModelAndView model) {
        model.setViewName("role/list");
        return model;
    }

    @RequestMapping("dataList")
    public ResultUtils dataList(Role role) {

        PageInfo<Object> pageInfo = PageHelper.startPage(role.getPageNum(), role.getPageSize()).doSelectPageInfo(() -> roleService.getList(role));
        return ResultUtils.success(pageInfo);
    }

    @PostMapping("addOrUpdate")
    public ResultUtils addOrUpdate(@RequestBody Role role) {
        Integer count;
        if (role.getId() == null) {
            //add
            count=roleService.insert(role);
        } else {
            //update
            count=roleService.updateById(role);
        }
        return ResultUtils.success(count);
    }

    @PostMapping("delete")
    public ResultUtils delete(@RequestBody Role role) {
        Integer delete = this.roleService.delete(role.getId());
        return ResultUtils.success(delete);
    }

    @RequestMapping("auth/{roleId}")
    public ResultUtils auth(@PathVariable Integer roleId) {
        List<Auth> authList = this.roleService.auth(roleId);
        return ResultUtils.success(authList);
    }

}
