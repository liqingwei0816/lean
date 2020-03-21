# 个人笔记项目 现阶段集成 
## 1.security
     使用标准RBAC模式权限管理
### 1.1 用户名密码登录方式
    拦截路径为/login 参数为username password 登录成功跳转首页
### 1.2 验证码登录方式
    拦截路径为/codeLogin 参数为mobile code 登录成功跳转首页
    需自定义实现CodeUtil接口用于验证码操作,默认实现为SimpleCodeUtill类,实现类必须加入springbean体系

## 2.异常处理 
    MyControllerAdvice
    使用同一异常处理方式,现仅对IllegalArgumentException做了处理，其他异常统一反馈系统异常
##  3.代码生成
    使用自定义代码生成工具，可视化页面生成代码
    可在 application.yml 中配置spring.generator.packageName ,
     spring.generator.tablePrefix ,spring.generator.database-name（必须）属性

    spring.generator.packageName 项目基础包名 默认为com.github
        代码生成的路径为    
            com.github.quartz.controller,  api接口
            com.github.service,     service
            com.github.service.impl,    serviceImpl
            com.github.bean,    pojo
            com.github.quartz.mapper   mybatisMapper
            MapperXml生成的路径为resources/mapper 文件名为{beanName}.xml
            管理页面生成路径为resources/templates/{beanName}/list.html
## 4.缓存
    默认使用 ehcache3.x为缓存管理 配置文件默认为resources/ehcache.xml
    可使用spring.cache.jcache.config 指定配置文件位置
    默认存在auth缓存用于权限管理缓存控制,失效时间五分钟
## 任务调度管理
任务调度管理集成spring-boot-starter-quartz,使用数据库做任务持久化,实现集群模式
### 自定义配置
#### 1.覆写ResourceLoaderClassLoadHelper获取jobClass
项目中覆盖org.springframework.scheduling.quartz.ResourceLoaderClassLoadHelper 重写loadClass(String name, Class<T> clazz)方法,使用自定义ClassLoad加载jobClass用于对动态编译加载job支持

#### 2.spring boot2.x实现了bean注入
   实现运行时添加job并对job中注入bean做处理,job中可直接使用注入的bean对象
#### 3.任务调度管理页面管理
页面路径为 /quartz/list 

任务管理页面中添加job时若工作类内容为空则使用已存在的job类,若工作类内容存在且项目中不存在工作类对应的job,则编译并加载工作类内容对应的job实现类同时将job存入数据库用于job集群处理

任务调度组件配置独立数据源方式,释放DataSourceConfig中的@Configuration注解，并在application.yml中配置数据源信息

jobDataMap 任务管理页面中的jobData已data为可以原样存入数据中，使用时需自定义解析规则

## 待添加
    日志管理模块 logback