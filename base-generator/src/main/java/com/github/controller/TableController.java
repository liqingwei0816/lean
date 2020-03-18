package com.github.controller;

import com.github.entity.Table;
import com.github.service.TableService;
import com.github.util.ThymeleafUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.util.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
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
    public ResultUtils createCode(Table table, boolean html, boolean service, boolean api) {
        try {
            String name = table.getName();
            List<Table> tables = tableService.getTables();
            Optional<Table> optional = tables.stream().filter(e -> e.getName().equals(name)).findAny();
            table=optional.orElseThrow(()->new NullPointerException("对应表单不存在"+name));
            if (service){
                thymeleafUtil.createServiceAndLowerLevel(table);
            }
            if (html){
                thymeleafUtil.createController(table);
                thymeleafUtil.createHtml(table);
            }
            if (api){
                thymeleafUtil.createApi(table,html);
            }
            return ResultUtils.success();
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtils.error(e.getMessage());
        }
    }

    @RequestMapping("list")
    public ModelAndView list(ModelAndView model) {
        model.setViewName("generator/table/list");
        return model;
    }


}
