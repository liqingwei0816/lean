package com.github.quartz.classload;

import com.github.quartz.mapper.DynamicJobService;
import com.github.util.SpringUtil;

public class JobClassLoader extends ClassLoader {
    public JobClassLoader(){
        super(JobClassLoader.class.getClassLoader());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //从数据库中加载class字节码
        DynamicJobService jobService = SpringUtil.getBean(DynamicJobService.class);
        byte[] bytes = jobService.selectByteCodeByClassName(name);
        if (bytes!=null){
            return this.defineClass(name,bytes,0,bytes.length);
        }else {
           return super.findClass(name);
            //throw new ClassNotFoundException("数据库中不存在["+name+"]的记录");
        }
    }
}
