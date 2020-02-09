package com.guttv.controller;

import com.guttv.bean.Auth;
import com.guttv.service.AuthService;
import com.guttv.util.ResultUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @RequestMapping("list")
    public ModelAndView list(ModelAndView model) {
        model.setViewName("auth/list");
        return model;
    }

    @RequestMapping("dataList")
    public ResultUtils dataList() {
        List<Auth> list = authService.getList(new Auth());
        list.forEach(e -> e.setAllAuth(list));
        List<Auth> collect = list.stream().filter(e -> e.getParentNode() == null).collect(Collectors.toList());
        return ResultUtils.success(collect);
    }

    @PostMapping("addOrUpdate")
    public ResultUtils addOrUpdate(@RequestBody Auth auth) {
        if (auth.getId() == null) {
            //add
            authService.insert(auth);
        } else {
            //update
            authService.updateById(auth);
        }
        return ResultUtils.success(authService.getTreeAndSpreadParent(auth));
    }

    @PostMapping(value = "delete")
    public ResultUtils delete(@RequestBody Auth auth) {
        if (auth.getChildren()!=null){
            return ResultUtils.success("存在下级节点，无法删除",authService.getTreeAndSpreadParent(auth));
        }
        authService.delete(auth.getId());
        return ResultUtils.success(authService.getTreeAndSpreadParent(auth));
    }



}
