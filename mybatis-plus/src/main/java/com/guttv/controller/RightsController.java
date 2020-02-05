package com.guttv.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.guttv.bean.Rights;
import com.guttv.service.RightsService;
import com.guttv.util.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@RestController
@RequestMapping("rights")
public class RightsController {

    @Resource
    private RightsService rightsService;

    @RequestMapping("list")
    public ModelAndView list(ModelAndView model) {
        model.setViewName("rights/list");
        return model;
    }

    @RequestMapping("dataList")
    public ResultUtils dataList(Rights rights) {

        PageInfo<Object> pageInfo = PageHelper.startPage(rights.getPageNum(), rights.getPageSize()).doSelectPageInfo(() -> rightsService.getList(rights));
        return ResultUtils.success(pageInfo);
    }


}
