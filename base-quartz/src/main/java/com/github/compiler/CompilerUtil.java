package com.github.compiler;

import com.github.compiler.impl.JavaSourceObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 动态编译工具类
 * 由内存到内存的动态编译,
 */
@Slf4j
public class CompilerUtil {

    public static Map<String, JavaSourceObject> compile(List<JavaSourceObject> javaSourceObjects) throws Exception {
        DynamicCompiler compiler = new DynamicCompiler();
        Boolean compile = compiler.compile(javaSourceObjects);
        if (compile){
            return compiler.getJavaFileManager().getFileOutObjects();

        }else {
            String collect = compiler.getDiagnostic().getDiagnostics().stream().map(Object::toString).collect(Collectors.joining("\n"));
            throw new Exception("编译失败:\n"+collect);
        }
    }

    /**
     * 不支持内部类
     * @param className 类名
     * @param source 源码
     */
    public static byte[] compileOne(String className, String source) throws Exception {
        Map<String, JavaSourceObject> compile = compile(Collections.singletonList(new JavaSourceObject(className, source)));
        JavaSourceObject javaSourceObject = compile.get(className);
        return javaSourceObject.getBytes();
    }




}