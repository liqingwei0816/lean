package com.github.compiler.impl;


import com.github.compiler.InternalClassFinder;

import javax.tools.*;
import javax.tools.JavaFileObject.Kind;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

public class DynamicJavaFileManager implements JavaFileManager {
    private final ClassLoader classLoader;
    private final StandardJavaFileManager standardJavaFileManager;
    private final InternalClassFinder internalClassFinder;

    public DynamicJavaFileManager(ClassLoader classLoader, StandardJavaFileManager standardJavaFileManager) {
        super();
        this.classLoader = classLoader;
        this.standardJavaFileManager = standardJavaFileManager;
        this.internalClassFinder = new InternalClassFinder(classLoader);
    }

    @Override
    public int isSupportedOption(String option) {
        return -1;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return classLoader;
    }

    @Override
    public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse)
            throws IOException {
        if (location == StandardLocation.PLATFORM_CLASS_PATH)
            return standardJavaFileManager.list(location, packageName, kinds, recurse);
        else if (location == StandardLocation.CLASS_PATH && kinds.contains(Kind.CLASS)) {
            if (packageName.startsWith("java."))
                return standardJavaFileManager.list(location, packageName, kinds, recurse);
            else
                return internalClassFinder.find(packageName);
        }
        return Collections.emptyList();
    }

    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        if (file instanceof DynamicJavaFileObject)
            return ((DynamicJavaFileObject) file).binaryName();
        else
            return standardJavaFileManager.inferBinaryName(location, file);
    }

    @Override
    public boolean isSameFile(FileObject a, FileObject b) {
        return standardJavaFileManager.isSameFile(a, b);
    }

    @Override
    public boolean handleOption(String current, Iterator<String> remaining) {
        return standardJavaFileManager.handleOption(current, remaining);
    }

    @Override
    public boolean hasLocation(Location location) {
        return location == StandardLocation.CLASS_PATH || location == StandardLocation.PLATFORM_CLASS_PATH;
    }

    @Override
    public JavaFileObject getJavaFileForInput(Location location, String className, Kind kind) throws IOException {
        return standardJavaFileManager.getJavaFileForInput(location, className, kind);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling)
            throws IOException {
        return standardJavaFileManager.getJavaFileForOutput(location, className, kind, sibling);
    }

    @Override
    public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
        return standardJavaFileManager.getFileForInput(location, packageName, relativeName);
    }

    @Override
    public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling)
            throws IOException {
        return standardJavaFileManager.getFileForOutput(location, packageName, relativeName, sibling);
    }

    @Override
    public void flush() throws IOException {
        standardJavaFileManager.flush();
    }

    @Override
    public void close() throws IOException {
        standardJavaFileManager.close();
    }

}
