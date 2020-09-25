package com.yonyougov.http.compiler;

import java.util.HashMap;
import java.util.Map;

public class MemoryClassLoader extends ClassLoader {
    Map<String, byte[]> classBytes = new HashMap();

    public MemoryClassLoader(ClassLoader parent, Map<String, byte[]> classBytes) {
        super(parent);
        this.classBytes.putAll(classBytes);
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] buf = (byte[]) this.classBytes.get(name);
        if (buf == null) {
            return super.findClass(name);
        } else {
            this.classBytes.remove(name);
            return this.defineClass(name, buf, 0, buf.length);
        }
    }
}
