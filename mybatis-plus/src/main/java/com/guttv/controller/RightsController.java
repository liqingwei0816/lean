package com.guttv.controller;

import com.guttv.bean.Rights;
import com.guttv.mapper.RightsMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class RightsController {

    @Resource
    private RightsMapper rightsMapper;

    @RequestMapping("list")
    public List<Rights> list(){
        return rightsMapper.selectList();
    }


}
