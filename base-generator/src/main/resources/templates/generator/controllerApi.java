package [(${packageName})].controller;

import [(${packageName})].bean.[(${table.entityName})];
import [(${packageName})].service.[(${table.entityName})]Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("[(${table.beanName})]")
public class [(${table.entityName})]Controller {

    @Resource
    private [(${table.entityName})]Service [(${table.beanName})]Service;

    @RequestMapping("dataList")
    public ResultUtils dataList([(${table.entityName})] [(${table.beanName})]) {

        PageInfo<Object> pageInfo = PageHelper.startPage([(${table.beanName})].getPageNum(), [(${table.beanName})].getPageSize()).doSelectPageInfo(() -> [(${table.beanName})]Service.getList([(${table.beanName})]));
        return ResultUtils.success(pageInfo);
    }

    @RequestMapping("delete/{id}")
    public ResultUtils delete(@PathVariable("id") Integer id) {
        Integer delete = this.sysUserService.delete(id);
        return ResultUtils.success(delete);
    }

}
