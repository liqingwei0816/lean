package com.guttv.service.impl;

import com.guttv.bean.Rights;
import com.guttv.mapper.RightsMapper;
import com.guttv.service.RightsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class RightsServiceImpl implements RightsService {

    @Resource
    private RightsMapper rightsMapper;

    @Override
    public List<Rights> getList(Rights rights) {
        return rightsMapper.selectList(rights);
    }

    @Override
    public Rights getOne(Rights rights) {
        return rightsMapper.selectOne(rights);
    }

    @Override
    public Integer delete(Integer id) {
        return rightsMapper.deleteById(id);
    }

    @Override
    public Integer updateById(@NotNull Rights rights) {
        return rightsMapper.updateById(rights);
    }

}