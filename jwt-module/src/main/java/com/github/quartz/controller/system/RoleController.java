package com.github.quartz.controller.system;

import com.github.bean.system.Role;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.service.system.RoleService;
import com.github.util.ResultUtils;
import lombok.Data;
import org.springframework.util.Assert;
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
        model.setViewName("system/role/list");
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
            count = roleService.insert(role);
        } else {
            //update
            count = roleService.updateById(role);
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
        return ResultUtils.success(this.roleService.auth(roleId));
    }

    @PostMapping("addAuth")
    public ResultUtils addAuth(@RequestBody RoleVo roleVo ) {
        Assert.notEmpty(roleVo.getAuthIds(), "参数 authIds 不能为空");
        Assert.notNull(roleVo.getRoleId(), "参数 roleId 不能为空");
        roleService.changeAuth(roleVo);
        return ResultUtils.success("完成");
    }
    @Data
    public static class RoleVo{
        List<Integer> authIds;
        Integer roleId;
    }

}
