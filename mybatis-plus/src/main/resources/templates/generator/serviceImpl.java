package [(${packageName})].service.impl;

import [(${packageName})].bean.[(${table.entityName})];
import [(${packageName})].mapper.[(${table.entityName})]Mapper;
import [(${packageName})].service.[(${table.entityName})]Service;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class [(${table.entityName})]ServiceImpl implements [(${table.entityName})]Service {

    @Resource
    private [(${table.entityName})]Mapper [(${table.beanName})]Mapper;

    @Override
    public List<[(${table.entityName})]> getList([(${table.entityName})] [(${table.beanName})]) {
        return [(${table.beanName})]Mapper.selectList([(${table.beanName})]);
    }

    @Override
    public [(${table.entityName})] getOne([(${table.entityName})] [(${table.beanName})]) {
        return [(${table.beanName})]Mapper.selectOne([(${table.beanName})]);
    }

    @Override
    public Integer delete(Integer id) {
        return [(${table.beanName})]Mapper.deleteById(id);
    }

    @Override
    public Integer insert([(${table.entityName})] [(${table.beanName})]){
        return [(${table.beanName})]Mapper.insert([(${table.beanName})]);
    };

    @Override
    public Integer updateById(@NotNull [(${table.entityName})] [(${table.beanName})]) {
        return [(${table.beanName})]Mapper.updateById([(${table.beanName})]);
    }

}