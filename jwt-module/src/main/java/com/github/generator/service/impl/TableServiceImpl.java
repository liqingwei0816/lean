package com.github.generator.service.impl;

import com.github.generator.GeneratorProperty;
import com.github.generator.entity.Table;
import com.github.generator.mapper.TableMapper;
import com.github.generator.service.TableService;
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
