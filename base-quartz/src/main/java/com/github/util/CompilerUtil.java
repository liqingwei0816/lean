package com.github.util;

import com.github.mapper.quartz.DynamicJobMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

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

@Slf4j
public class CompilerUtil {

    private static String baseDir = "/";

    public static URLClassLoader urlClassLoader;
    static {
        try {
            URL url = Paths.get(baseDir).toAbsolutePath().toUri().toURL();
            urlClassLoader=new URLClassLoader(new URL[]{url});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


    /**
     * 编译java文件
     * @param path java文件的路径
     */
    public static void compiler(Path path) {
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            int flag = compiler.run(null, null, null, path.toFile().getAbsolutePath());
            System.out.println(flag == 0 ? "编译成功" : "编译失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 将class字符串放入磁盘
     *
     * @param className    文件名 默认上级目录为 System.getProperty("user.dir")
     * @param classString class内容
     */
    public static Path createJavaFile(String className, String classString) throws IOException {
        Path path = getFilePathByClassName(className);
        return Files.write(path, classString.getBytes(), StandardOpenOption.CREATE);
    }

    /**
     *
     * 获取java文件路径
     * @param className 类名
     */
    public static  Path getFilePathByClassName(String className){
        String filePath = className.replace(".", File.separator);
        return Paths.get(baseDir + filePath + ".java");
    }



    public static Class<?> getClass(String className) throws ClassNotFoundException {
        Class<?> aClass;
        try {
            //查看是否存在已编译的class
            aClass= Class.forName(className, true, urlClassLoader);
        } catch (ClassNotFoundException e) {
            //不存在 创建文件并编译后加载
            Path filePath = getFilePathByClassName(className);
            File file = filePath.toFile();
            if (!file.exists()){
                //需要创建java文件
                try {
                    //创建java文件
                    DynamicJobMapper dynamicJobMapper = SpringUtil.getBean(DynamicJobMapper.class);
                    //java文件字符串
                    String classString = dynamicJobMapper.selectByClassName(className);
                    if (StringUtils.isEmpty(classString)){
                        log.error("工作类未找到");
                        throw e;
                    }
                    createJavaFile(className, classString);
                } catch (IOException ex) {
                    System.out.println("创建文件失败");
                    ex.printStackTrace();
                    throw e;
                }
            }
            //编译java文件
            compiler(filePath);
            aClass= Class.forName(className, true, urlClassLoader);
        }
        return aClass;
    }



}
