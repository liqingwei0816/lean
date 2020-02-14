package com.guttv.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.guttv.bean.system.Auth;
import com.guttv.bean.system.Role;
import com.guttv.bean.system.RoleAuth;
import com.guttv.service.system.RoleAuthService;
import com.guttv.service.system.RoleService;
import com.guttv.util.ResultUtils;
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
        List<Auth> authList = this.roleService.auth(roleId);
        return ResultUtils.success(authList);
    }

    @Resource
    private RoleAuthService roleAuthService;

    @PostMapping("addAuth")
    public ResultUtils addAuth(RoleAuth roleAuth, Boolean checked) {
        Assert.notNull(checked, "参数 checked 不能为空");
        Assert.notNull(roleAuth, "参数 roleId 和 authId 不能为空");
        Assert.notNull(roleAuth.getRoleId(), "参数 roleId 不能为空");
        Assert.notNull(roleAuth.getAuthId(), "参数 authId 不能为空");
        RoleAuth roleAuthDb = roleAuthService.getOne(roleAuth);
        if (checked) {
            if (roleAuthDb == null) {
                roleAuthService.insert(roleAuth);
            }
            return ResultUtils.success("添加完成");
        } else {
            if (roleAuthDb != null) {
                roleAuthService.delete(roleAuthDb.getId());
            }
            return ResultUtils.success("删除完成");
        }
    }

}
