package com.guttv.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.guttv.bean.system.SysUser;
import com.guttv.bean.system.SysUserRole;
import com.guttv.service.system.SysUserRoleService;
import com.guttv.service.system.SysUserService;
import com.guttv.util.ResultUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.security.Principal;

@RestController
@RequestMapping("sysUser")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @RequestMapping("list")
    public ModelAndView list(ModelAndView model) {
        model.setViewName("system/sysUser/list");
        return model;
    }

    @RequestMapping("dataList")
    public ResultUtils dataList(SysUser sysUser) {
        PageInfo<SysUser> pageInfo = PageHelper.startPage(sysUser.getPageNum(), sysUser.getPageSize()).doSelectPageInfo(() -> sysUserService.getList(sysUser));
        pageInfo.getList().forEach(e->e.setPassword(null));
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

    @Resource
    private SysUserRoleService sysUserRoleService;

    /**
     *
     * @param checked true 添加  否则 删除
     */
    @PostMapping("addRole")
    public ResultUtils addRole(SysUserRole userRole, Boolean checked) {
        if (checked) {
            Assert.notNull(userRole.getRoleId(),"参数 roleId 不能为空");
            Assert.notNull(userRole.getSysUserId(),"参数 sysUserId 不能为空");
            sysUserRoleService.insertNoExists(userRole);
        }else {
            Assert.notNull(userRole.getId(),"参数 sysUserId 不能为空");
            sysUserRoleService.delete(userRole.getId());
        }
        return ResultUtils.success("操作成功");
    }


    /**
     * @param sysUserId 管理员Id
     * @return 当前查询用户拥有的权限，返回所有权限列表，拥有的角色为选中状态
     */
    @GetMapping("roles/{sysUserId}")
    public ResultUtils roles(@PathVariable Integer sysUserId) {
        return ResultUtils.success(sysUserService.getRoles(sysUserId));
    }


    /**
     * @param password 密码
     * @return 当前查询用户拥有的权限，返回所有权限列表，拥有的角色为选中状态
     */
    @PostMapping("changePassword")
    public ResultUtils changePassword(String password, Principal principal) {
        Assert.hasLength(password,"参数 password 不能为空");
        String name = principal.getName();
        SysUser userName = sysUserService.getByUserName(name);
        Assert.notNull(userName,"用户不存在");
        userName.setPassword(bCryptPasswordEncoder.encode(password));
        sysUserService.updateById(userName);
        return ResultUtils.success();
    }

}
