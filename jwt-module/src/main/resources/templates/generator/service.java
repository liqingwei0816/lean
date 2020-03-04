package [(${packageName})].service;

import [(${packageName})].bean.[(${table.entityName})];

import javax.validation.constraints.NotNull;
import java.util.List;

public interface [(${table.entityName})]Service {

    List<[(${table.entityName})]> getList([(${table.entityName})] [(${table.beanName})]);

    [(${table.entityName})] getOne([(${table.entityName})] [(${table.beanName})]);

    Integer delete(Integer id);

    Integer insert([(${table.entityName})] [(${table.beanName})]);

    Integer updateById(@NotNull [(${table.entityName})] [(${table.beanName})]);
}
