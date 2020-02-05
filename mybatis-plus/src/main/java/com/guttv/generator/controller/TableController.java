package com.guttv.generator.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.guttv.generator.entity.Table;
import com.guttv.generator.service.TableService;
import com.guttv.generator.util.ThymeleafUtil;
import com.guttv.util.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("table")
public class TableController {

    @Resource
    private TableService tableService;
    @Resource
    private ThymeleafUtil thymeleafUtil;

    @RequestMapping("dataList")
    public ResultUtils dataList(Table table) {
        PageInfo<Object> pageInfo = PageHelper.startPage(table).doSelectPageInfo(() -> tableService.getTables());
        return ResultUtils.success(pageInfo);
    }

    @RequestMapping("createCode")
    public ResultUtils createCode(Table table,Boolean html) {
        try {
            String name = table.getName();
            List<Table> tables = tableService.getTables();
            Optional<Table> optional = tables.stream().filter(e -> e.getName().equals(name)).findAny();
            table=optional.orElseThrow(()->new NullPointerException("对应表单不存在"+name));
            thymeleafUtil.createBean(table);
            thymeleafUtil.createMapper(table);
            thymeleafUtil.createService(table);
            thymeleafUtil.createController(table);
            return ResultUtils.success();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return ResultUtils.error(e.getMessage());
        }
    }

    @RequestMapping("list")
    public ModelAndView list(ModelAndView model) {
        model.setViewName("table/list");
        return model;
    }


}
