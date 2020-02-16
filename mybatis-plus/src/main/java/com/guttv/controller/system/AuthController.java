package com.guttv.controller.system;

import com.guttv.bean.system.Auth;
import com.guttv.service.system.AuthService;
import com.guttv.util.ResultUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @RequestMapping("list")
    public ModelAndView list(ModelAndView model) {
        model.setViewName("system/auth/list");
        return model;
    }

    @RequestMapping("dataList")
    public ResultUtils dataList() {
        List<Auth> list = authService.getList(new Auth());
        list.forEach(e->e.setOpen(true));
        return ResultUtils.success(list);
    }

    @PostMapping("addOrUpdate")
    public ResultUtils addOrUpdate(@RequestBody Auth auth) {
        if (auth.getId() == null) {
            //add
            authService.insert(auth);
        } else {
            //update todo mapper属性对应 角色 权限绑定页面重写
            authService.updateById(auth);
        }
        return ResultUtils.success("完成");
    }

    @PostMapping(value = "delete")
    public ResultUtils delete(@RequestBody Auth auth) {
        Auth authQuery = new Auth();
        authQuery.setParentNode(auth.getId());
        Long count = authService.count(authQuery);
        if (count==0){
            authService.delete(auth.getId());
            return ResultUtils.success();
        }else {
            return ResultUtils.error("只能删除子节点");
        }
    }


}
