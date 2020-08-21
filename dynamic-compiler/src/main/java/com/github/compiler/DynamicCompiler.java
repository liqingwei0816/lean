package com.github.compiler;


import com.github.compiler.impl.DynamicClassLoader;
import com.github.compiler.impl.DynamicForwardingJavaFileManager;
import com.github.compiler.impl.DynamicJavaFileManager;
import com.github.compiler.impl.JavaSourceObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class DynamicCompiler {
    private static final Logger logger = LoggerFactory.getLogger(DynamicCompiler.class);
    //Java文件后缀名
    private final JavaCompiler javaCompiler;
    private final List<String> options;
    private final DiagnosticCollector<JavaFileObject> diagnostic;
    private final DynamicForwardingJavaFileManager javaFileManager;

    public DynamicCompiler() {
        super();
        logger.info("Dynamic Compiler Initializing");
        javaCompiler = ToolProvider.getSystemJavaCompiler();
        if (javaCompiler == null) {
            throw new IllegalStateException("Cannot find the system Java compiler. No JDK found");
        }
        DynamicClassLoader classLoader = new DynamicClassLoader(getClass().getClassLoader());
        diagnostic = new DiagnosticCollector<>();
        StandardJavaFileManager standardJavaFileManager = javaCompiler.getStandardFileManager(diagnostic, null, null);
        final JavaFileManager fileManager = new DynamicJavaFileManager(classLoader, standardJavaFileManager);
        javaFileManager = new DynamicForwardingJavaFileManager(fileManager, classLoader);
        this.options = Arrays.asList("-target", System.getProperty("java.specification.version"));
    }

    /**
     * @param classes map
     */
    public synchronized Boolean compile(List<JavaSourceObject> classes) {
        StringWriter stringWriter = new StringWriter();
        final CompilationTask task = javaCompiler.getTask(stringWriter, javaFileManager, diagnostic, options, null, classes);
        return task.call();
    }


    public static URI toURI(String name) {
        try {
            return new URI(name);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public DynamicForwardingJavaFileManager getJavaFileManager() {
        return javaFileManager;
    }

}
