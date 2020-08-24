# 个人笔记项目 现阶段集成 
## 代码生成
    使用自定义代码生成工具，可视化页面生成代码
    管理页面为 /table/list 可自行集成
    可在 application.yml 中配置spring.generator.packageName ,
    spring.generator.tablePrefix ,spring.generator.database-name（必须）属性

    spring.generator.packageName 项目基础包名 默认为com.github
        代码生成的路径为    
            com.github.controller,  api接口
            com.github.service,     service接口
            com.github.service.impl,    serviceImpl
            com.github.bean,    pojo
            com.github.quartz.mapper   mybatisMapper
            MapperXml生成的路径为resources/mapper 文件名为{beanName}.xml
            管理页面生成的页面文件路径为resources/templates/{beanName}/list.html