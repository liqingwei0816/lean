package com.github.compiler;

import com.github.compiler.impl.JavaSourceObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 动态编译工具类
 * 由内存到内存的动态编译,
 */
@Slf4j
public class CompilerUtil {

    public static Map<String, JavaSourceObject> compile(List<JavaSourceObject> javaSourceObjects) {
        DynamicCompiler compiler = new DynamicCompiler();
        Boolean compile = compiler.compile(javaSourceObjects);
        if (compile) {
            return compiler.getJavaFileManager().getFileOutObjects();
        }
        return null;
    }

    public static byte[] compileOne(String className, String source) {
        Map<String, JavaSourceObject> compile = compile(Collections.singletonList(new JavaSourceObject(className, source)));
        if (compile != null) {
            return compile.get(className).getBytes();
        }
        return null;
    }

}