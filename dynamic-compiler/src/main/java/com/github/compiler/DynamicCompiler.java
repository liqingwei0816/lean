/**
 * 
 */
package com.github.compiler;


import com.github.compiler.exceptions.DynamicCompilerException;
import com.github.compiler.impl.DynamicClassLoader;
import com.github.compiler.impl.DynamicForwardingJavaFileManager;
import com.github.compiler.impl.DynamicJavaFileManager;
import com.github.compiler.impl.DynamicSimpleFileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author deniz.toktay
 *
 */
public class DynamicCompiler<T> {
	private final Logger logger = LoggerFactory.getLogger(DynamicCompiler.class);
	public static final String JAVA_EXTENSION = ".java";
	private final DynamicClassLoader classLoader;
	private final JavaCompiler javaCompiler;
	private final List<String> options;
	private DiagnosticCollector<JavaFileObject> diags;
	private final DynamicForwardingJavaFileManager javaFileManager;

	public DynamicCompiler(ClassLoader parentClassLoader, List<String> options) {
		super();
		logger.info("Dynamic Compiler Initializing");
		javaCompiler = ToolProvider.getSystemJavaCompiler();
		if (javaCompiler == null) {
			throw new IllegalStateException("Cannot find the system Java compiler. No JDK found");
		}
		this.classLoader = new DynamicClassLoader(parentClassLoader);
		diags = new DiagnosticCollector<>();
		StandardJavaFileManager standardJavaFileManager = javaCompiler.getStandardFileManager(diags, null, null);
		final JavaFileManager fileManager = new DynamicJavaFileManager(classLoader, standardJavaFileManager);
		javaFileManager = new DynamicForwardingJavaFileManager(fileManager, classLoader);
		this.options = new ArrayList<>();
		if (options != null) {
			this.options.addAll(options);
		}
		logger.info("Dynamic Compiler Initialized");
	}

	public synchronized Class<T> compile(final String qualifiedClassName, final CharSequence javaSource,
			final DiagnosticCollector<JavaFileObject> diagnosticsList, final Class<?>... types)
			throws DynamicCompilerException {
		if (diagnosticsList != null)
			diags = diagnosticsList;
		else
			diags = new DiagnosticCollector<>();
		Map<String, CharSequence> classes = new HashMap<>(1);
		classes.put(qualifiedClassName, javaSource);
		Map<String, Class<T>> compiled = compile(classes, diagnosticsList);
		return compiled.get(qualifiedClassName);
	}

	public synchronized Map<String, Class<T>> compile(Map<String, CharSequence> classes,
			DiagnosticCollector<JavaFileObject> diagnosticsList) throws DynamicCompilerException {
		List<JavaFileObject> sources = new ArrayList<>();
		for (Entry<String, CharSequence> entry : classes.entrySet()) {
			String qualifiedClassName = entry.getKey();
			CharSequence javaSource = entry.getValue();
			if (javaSource != null) {
				final int dotPos = qualifiedClassName.lastIndexOf('.');
				final String className = dotPos == -1 ? qualifiedClassName : qualifiedClassName.substring(dotPos + 1);
				final String packageName = dotPos == -1 ? "" : qualifiedClassName.substring(0, dotPos);
				final DynamicSimpleFileObject source = new DynamicSimpleFileObject(className, javaSource);
				sources.add(source);
				javaFileManager.putFileForInput(StandardLocation.SOURCE_PATH, packageName, className + JAVA_EXTENSION,
						source);
			}
		}
		final CompilationTask task = javaCompiler.getTask(null, javaFileManager, diags, options, null, sources);
		final Boolean result = task.call();
		for (Diagnostic<? extends JavaFileObject> diagnostic : diags.getDiagnostics()) {
			System.out.println(diagnostic.getMessage(null));
		}
		if (result == null || !result) {
			throw new DynamicCompilerException("Compilation failed.");
		}
		try {
			Map<String, Class<T>> compiled = new HashMap<>();
			for (String qualifiedClassName : classes.keySet()) {
				final Class<T> newClass = loadClass(qualifiedClassName);
				compiled.put(qualifiedClassName, newClass);
			}
			return compiled;
		} catch (ClassNotFoundException e) {
			throw new DynamicCompilerException("Class Not Found in Compile ", e);
		} catch (IllegalArgumentException e) {
			throw new DynamicCompilerException("Illegal Argument in Compile ", e);
		} catch (SecurityException e) {
			throw new DynamicCompilerException("Security in Reflection in Compile ", e);
		}
	}

	@SuppressWarnings("unchecked")
	public Class<T> loadClass(String qualifiedClassName) throws ClassNotFoundException {
		return (Class<T>) classLoader.loadClass(qualifiedClassName);
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

	public ClassLoader getClassLoader() {
		return javaFileManager.getClassLoader();
	}
}
