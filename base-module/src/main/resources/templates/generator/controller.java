package [(${packageName})].controller;

import [(${packageName})].bean.[(${table.entityName})];
import [(${packageName})].service.[(${table.entityName})]Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("[(${table.beanName})]")
public class [(${table.entityName})]Controller {

    @Resource
    private [(${table.entityName})]Service [(${table.beanName})]Service;

    @RequestMapping("list")
    public ModelAndView list(ModelAndView model) {
        model.setViewName("[(${table.beanName})]/list");
        return model;
    }

    @RequestMapping("dataList")
    public ResultUtils dataList([(${table.entityName})] [(${table.beanName})]) {

        PageInfo<Object> pageInfo = PageHelper.startPage([(${table.beanName})].getPageNum(), [(${table.beanName})].getPageSize()).doSelectPageInfo(() -> [(${table.beanName})]Service.getList([(${table.beanName})]));
        return ResultUtils.success(pageInfo);
    }

    @PostMapping("addOrUpdate")
    public ResultUtils addOrUpdate(@RequestBody [(${table.entityName})] [(${table.beanName})]) {
        Integer count;
        if ([(${table.beanName})].getId() == null) {
            //add
            count=[(${table.beanName})]Service.insert([(${table.beanName})]);
        } else {
            //update
            count=[(${table.beanName})]Service.updateById([(${table.beanName})]);
        }
        return ResultUtils.success(count);
    }

    @PostMapping("delete")
    public ResultUtils delete(@RequestBody [(${table.entityName})] [(${table.beanName})]) {
        Integer delete = this.[(${table.beanName})]Service.delete([(${table.beanName})].getId());
        return ResultUtils.success(delete);
    }

}
