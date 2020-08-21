package com.github.compiler;


import com.github.compiler.exceptions.DynamicCompilerException;
import com.github.compiler.impl.*;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class DynamicCompiler {
    private static final Logger logger = Logger.getLogger(DynamicCompiler.class.getName());
    //Java文件后缀名
    public static final String JAVA_EXTENSION = ".java";
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
        javaFileManager = new DynamicForwardingJavaFileManager(fileManager,classLoader);
        this.options =  Arrays.asList("-target",System.getProperty("java.specification.version"));
    }

    public synchronized Boolean compile(final String qualifiedClassName, final CharSequence javaSource)
            throws DynamicCompilerException {
        Map<String, CharSequence> classes = new HashMap<>(1);
        classes.put(qualifiedClassName, javaSource);
        return compile(Collections.singletonList(new JavaSourceObject(qualifiedClassName,javaSource)));
    }

    /**
     *
     * @param classes map
     */
    public synchronized Boolean compile(List<JavaSourceObject> classes) throws DynamicCompilerException {
        //todo 待删除
        classes.forEach(entry -> {
            String qualifiedClassName = entry.getClassName();
            CharSequence javaSource = entry.getCharContent(false);
            final int dotPos = qualifiedClassName.lastIndexOf('.');
            //类名,不包含包名
            final String className = dotPos == -1 ? qualifiedClassName : qualifiedClassName.substring(dotPos + 1);
            final JavaSourceObject source = new JavaSourceObject(className, javaSource);
            final String packageName = dotPos == -1 ? "" : qualifiedClassName.substring(0, dotPos);
            /*javaFileManager.putFileForInput(StandardLocation.SOURCE_PATH, packageName, className + JAVA_EXTENSION,
                    source);*/
        });

        final CompilationTask task = javaCompiler.getTask(null, javaFileManager, diagnostic, options, null, classes);
        final Boolean result = task.call();
        if (result == null || !result) {
            throw new DynamicCompilerException("Compilation failed.");
        }
        return true;
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

    public DiagnosticCollector<JavaFileObject> getDiagnostic() {
        return diagnostic;
    }
}
