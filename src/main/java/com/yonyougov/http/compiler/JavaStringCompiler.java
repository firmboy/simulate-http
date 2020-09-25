package com.yonyougov.http.compiler;

import javax.tools.*;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

/**
 * @ClassName JavaStringCompiler
 * @Description TODO
 * @Author playboy
 * @Date 2020/9/25 3:10 下午
 * @Version 1.0
 **/
public class JavaStringCompiler {

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    StandardJavaFileManager stdManager;

    public JavaStringCompiler() {
        this.stdManager = this.compiler.getStandardFileManager((DiagnosticListener) null, (Locale) null, (Charset) null);
    }

    public Map<String, byte[]> compile(String fileName, String source) throws IOException {
        MemoryJavaFileManager manager = new MemoryJavaFileManager(this.stdManager);
        Throwable var4 = null;

        Map var8;
        try {
            JavaFileObject javaFileObject = manager.makeStringSource(fileName, source);
            JavaCompiler.CompilationTask task = this.compiler.getTask((Writer) null, manager, (DiagnosticListener) null, (Iterable) null, (Iterable) null, Arrays.asList(javaFileObject));
            Boolean result = task.call();
            if (result == null || !result) {
                throw new RuntimeException("Compilation failed.");
            }

            var8 = manager.getClassBytes();
        } catch (Throwable var17) {
            var4 = var17;
            throw var17;
        } finally {
            if (manager != null) {
                if (var4 != null) {
                    try {
                        manager.close();
                    } catch (Throwable var16) {
                        var4.addSuppressed(var16);
                    }
                } else {
                    manager.close();
                }
            }

        }

        return var8;
    }

    public Class<?> loadClass(String name, Map<String, byte[]> classBytes) throws ClassNotFoundException, IOException {
        ClassLoader classLoader1 = JavaStringCompiler.class.getClassLoader();
        MemoryClassLoader classLoader = new MemoryClassLoader(classLoader1, classBytes);
        Throwable var4 = null;

        Class var5;
        try {
            var5 = classLoader.loadClass(name);
        } catch (Throwable var14) {
            var4 = var14;
            throw var14;
        } finally {

        }

        return var5;
    }
}
