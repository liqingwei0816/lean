package com.github.compiler.impl;

import javax.tools.JavaFileObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author deniz.toktay
 *
 */
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
			byte[] bytes = ((org.edtoktay.dynamic.compiler.impl.DynamicSimpleFileObject) file).getByteCode();
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
			org.edtoktay.dynamic.compiler.impl.DynamicSimpleFileObject file = (org.edtoktay.dynamic.compiler.impl.DynamicSimpleFileObject) classes.get(qualifiedClassName);
			if (file != null) {
				return new ByteArrayInputStream(file.getByteCode());
			}
		}
		return super.getResourceAsStream(name);
	}

	public void add(final String qualifiedName, final JavaFileObject file) {
		classes.put(qualifiedName, file);
	}

	public Collection<? extends JavaFileObject> files() {
		return Collections.unmodifiableCollection(classes.values());
	}
}
