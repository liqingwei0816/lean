package com.guttv.generator.service.impl;

import com.guttv.generator.GeneratorProperty;
import com.guttv.generator.entity.Table;
import com.guttv.generator.mapper.TableMapper;
import com.guttv.generator.service.TableService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TableServiceImpl implements TableService {
    @Resource
    private TableMapper tableMapper;

    @Resource
    private GeneratorProperty generatorProperty;



    @Override
    public List<Table> getTables() {
        return tableMapper.getTables(generatorProperty.getDatabaseName());
    }
}
