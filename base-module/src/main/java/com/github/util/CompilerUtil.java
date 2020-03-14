package com.github.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CompilerUtil{
    private static Logger log = LoggerFactory.getLogger(CompilerUtil.class);

    public  void compiler(Path path) {
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            int flag = compiler.run(null, null, null, path.toFile().getAbsolutePath());
            System.out.println(flag == 0 ? "编译成功" : "编译失败");
        } catch (Exception e) {
            log.error("test", e);
        }
    }


    /**
     * 将class字符串放入磁盘
     * @param fileName 文件名 默认上级目录为 System.getProperty("user.dir")
     * @param classString class内容
     */
    public  Path createJavaFile(String fileName, String classString) throws IOException {
        Path path = Paths.get(getJavaFilePath()+File.separator+fileName);
        return Files.write(path, classString.getBytes(), StandardOpenOption.CREATE);
    }

    /**
     * 加载class类文件 todo 需修改为从内存中加载  或循环查询数据库 定时加载更新 不能使用new  URLClassLoader  ,每次加载使用相同的ClassLoader对象
     * @param classFullName 类全名
     */
    public  Class<?> loadClass1(String classFullName) throws MalformedURLException, ClassNotFoundException {
        URL url = Paths.get(getJavaFilePath()).toUri().toURL();
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
        return urlClassLoader.loadClass(classFullName);
    }

    /**
     * 根据class字符串获取class对象 包含 创建java文件  编译Java文件 获取class文件 获取class类对象 不能使用lombok注解
     * @param fileName 文件名,名称必须为类对象
     * @param classContent class内容
     */
    public  String getClassObjectByJavaFile(String fileName,String classContent) throws IOException, ClassNotFoundException {
        //1 创建java文件
        Path javaFile = createJavaFile(fileName, classContent);
        //2 编译Java文件 获取class文件
        compiler(javaFile);
        //3. 获取class类对象
        //获取包名
        String[] split = classContent.split(";", 2);
        return split[0].replaceFirst("package", "").trim() + "." + fileName.replace(".java", "");
    }

    /**
     * 获取当前用户目录
     */
    private static String getJavaFilePath(){
        return System.getProperty("user.dir");
    }
}
