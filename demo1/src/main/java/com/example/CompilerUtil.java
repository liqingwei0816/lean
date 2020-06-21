package com.example;

import lombok.extern.slf4j.Slf4j;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.*;

/**
 * 动态编译工具类
 * 由内存到内存的动态编译,
 */
@Slf4j
public class CompilerUtil {

    public  Map<String, JavaSourceObject> compile(List<JavaSourceObject> javaSourceObjects) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        ClassFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(null, null, null));

        List<String> options = new ArrayList<>();
        options.add("-classpath");
        options.add("./BOOT-INF/classes/");
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, javaSourceObjects);

        if (task.call()) {
            //反馈字节码
            return fileManager.getClassMap();
        } else {
            throw new RuntimeException("编译失败,请查看");
        }
    }

    public  byte[] compileOne(String className, String source) {

        Map<String, JavaSourceObject> compile = compile(Collections.singletonList(new JavaSourceObject(className, source)));
        JavaSourceObject javaClassObject = compile.get(className);
        return javaClassObject.getBytes();

    }

    /**
     * 文件内容为内存中动态拼出的字符串
     */
    private static class JavaSourceObject extends SimpleJavaFileObject {
        //此处为接受Java源代码相关
        //接受源代码
        private CharSequence content;
        public JavaSourceObject(String className, CharSequence content) {
            //不明用处
            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.content = content;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return content;
        }

        //下面为输出class字节码相关
        //该流不需要关闭,位于内存中,GC会自动回收 用于接受class字节码
        protected final ByteArrayOutputStream bos = new ByteArrayOutputStream();

        public JavaSourceObject(String name, Kind kind) {
            super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
        }
        //获取编译后的字节码
        public byte[] getBytes() {
            return bos.toByteArray();
        }

        @Override
        public OutputStream openOutputStream() {
            return bos;
        }


    }

    /**
     * 编译时class输出至JavaClassObject的ByteArrayOutputStream
     */
    public static class ClassFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

        public ClassFileManager(StandardJavaFileManager standardManager) {
            super(standardManager);
        }

        private Map<String, JavaSourceObject> classMap = new HashMap<>();

        public Map<String, JavaSourceObject> getClassMap() {
            return classMap;
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className,
                                                   JavaFileObject.Kind kind, FileObject sibling) {
            JavaSourceObject classObject = new JavaSourceObject(className, kind);
            classMap.put(className, classObject);
            return classObject;
        }
    }


}