package [(${packageName})].mapper;

import com.github.pagehelper.PageHelper;
import [(${packageName})].bean.[(${table.entityName})];
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotNull;
import java.util.List;

@Mapper
public interface [(${table.entityName})]Mapper {

    List<[(${table.entityName})]> selectList(@NotNull [(${table.entityName})] [(${table.beanName})]);

    default List<[(${table.entityName})]> selectAll() {
        return selectList(new [(${table.entityName})]());
    }


    [(${table.entityName})] selectOne(@NotNull [(${table.entityName})] [(${table.beanName})]);

    default Long selectCount(@NotNull [(${table.entityName})] [(${table.beanName})]) {
        return PageHelper.count(() -> selectList([(${table.beanName})]));
    }

    @Delete("delete  from [(${table.name})] where id = #{id}")
    Integer deleteById(@NotNull Integer id);

    Integer insert(@NotNull [(${table.entityName})] [(${table.beanName})]);

    Integer updateById(@NotNull [(${table.entityName})] [(${table.beanName})]);
}
