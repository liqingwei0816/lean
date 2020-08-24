package com.github.compiler.impl;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DynamicForwardingJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private final DynamicClassLoader classLoader;
    private final Map<String, JavaSourceObject> fileOutObjects = new HashMap<>();

    public DynamicForwardingJavaFileManager(JavaFileManager fileManager, DynamicClassLoader classLoader) {
        super(fileManager);
        this.classLoader = classLoader;
    }

    public Map<String, JavaSourceObject> getFileOutObjects() {
        return fileOutObjects;
    }

    @Override
    public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
        return super.getFileForInput(location, packageName, relativeName);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String qualifiedName, Kind kind,
                                               FileObject outputFile) {
        JavaSourceObject file = new JavaSourceObject(qualifiedName, kind);
        classLoader.add(qualifiedName, file);
        fileOutObjects.put(qualifiedName, file);
        return file;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return classLoader;
    }

    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        String result;
        if (file instanceof JavaSourceObject)
            result = file.getName();
        else
            result = super.inferBinaryName(location, file);
        return result;
    }

}
