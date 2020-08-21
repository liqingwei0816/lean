package com.github.compiler.impl;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

public   class JavaSourceObject extends SimpleJavaFileObject {
    //此处为接受Java源代码相关
    //接受源代码
    private CharSequence content;
    //类名称
    private String className;
    public JavaSourceObject(String className, CharSequence content) {
        //不明用处
        super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.content = content;
        this.className = className;
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

    public String getClassName() {
        return className;
    }

    @Override
    public OutputStream openOutputStream() {
        return bos;
    }
}