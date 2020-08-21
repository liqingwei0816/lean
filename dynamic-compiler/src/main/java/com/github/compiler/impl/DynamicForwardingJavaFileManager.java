package com.github.compiler.impl;

import com.github.compiler.DynamicCompiler;

import javax.tools.*;
import javax.tools.JavaFileObject.Kind;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author deniz.toktay
 *
 */
public class DynamicForwardingJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
	private final DynamicClassLoader classLoader;
	private final Map<URI, JavaSourceObject> fileObjects = new HashMap<>();
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
		FileObject fileObject = fileObjects.get(uri(location, packageName, relativeName));
		if (fileObject != null)
			return fileObject;
		return super.getFileForInput(location, packageName, relativeName);
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String qualifiedName, Kind kind,
			FileObject outputFile) {
		JavaSourceObject file = new JavaSourceObject(qualifiedName, kind);
		classLoader.add(qualifiedName, file);
		fileOutObjects.put(qualifiedName,file);
		return file;
	}

	@Override
	public ClassLoader getClassLoader(Location location) {
		return classLoader;
	}

	@Override
	public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse)
			throws IOException {
		Iterable<JavaFileObject> result = super.list(location, packageName, kinds, recurse);
		ArrayList<JavaFileObject> files = new ArrayList<>();
		if (location == StandardLocation.CLASS_PATH && kinds.contains(Kind.CLASS)) {
			for (JavaFileObject file : fileObjects.values()) {
				if (file.getKind() == Kind.CLASS && file.getName().startsWith(packageName))
					files.add(file);
			}
			files.addAll(classLoader.files());
		} else if (location == StandardLocation.SOURCE_PATH && kinds.contains(Kind.SOURCE)) {
			for (JavaFileObject file : fileObjects.values()) {
				if (file.getKind() == Kind.SOURCE && file.getName().startsWith(packageName))
					files.add(file);
			}
		}
		for (JavaFileObject file : result) {
			files.add(file);
		}
		return files;
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

	public void putFileForInput(StandardLocation sourcePath, String packageName, String relativeName,
			JavaSourceObject source) {
		fileObjects.put(uri(sourcePath, packageName, relativeName), source);
	}

	private URI uri(Location location, String packageName, String relativeName) {
		return DynamicCompiler.toURI(location.getName() + '/' + packageName + '/' + relativeName);
	}
}
