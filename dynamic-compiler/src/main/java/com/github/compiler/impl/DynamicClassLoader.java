package com.github.compiler.impl;

import javax.tools.JavaFileObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DynamicClassLoader extends ClassLoader {
    private final Map<String, JavaFileObject> classes = new HashMap<>();

    public DynamicClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        JavaFileObject file = classes.get(name);
        if (file != null) {
            byte[] bytes = ((JavaSourceObject) file).getBytes();
            return defineClass(name, bytes, 0, bytes.length);
        }
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ignored) {
        }
        return super.findClass(name);
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        if (name.endsWith(".class")) {
            String qualifiedClassName = name.substring(0, name.length() - ".class".length()).replace('/', '.');
            JavaSourceObject file = (JavaSourceObject) classes.get(qualifiedClassName);
            if (file != null) {
                return new ByteArrayInputStream(file.getBytes());
            }
        }
        return super.getResourceAsStream(name);
    }

    public void add(final String qualifiedName, final JavaFileObject file) {
        classes.put(qualifiedName, file);
    }

}
