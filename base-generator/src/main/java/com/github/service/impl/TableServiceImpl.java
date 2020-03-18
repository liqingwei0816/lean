package com.github.service.impl;

import com.github.GeneratorProperty;
import com.github.entity.Table;
import com.github.mapper.TableMapper;
import com.github.service.TableService;
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
