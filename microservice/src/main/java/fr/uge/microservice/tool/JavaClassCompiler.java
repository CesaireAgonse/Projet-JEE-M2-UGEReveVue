package fr.uge.microservice.tool;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaClassCompiler {

    private static final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    public static Map<String, byte[]> compileJavaCodeAndGetBytes(List<DynamicJavaClass> dynamicJavaClasses) throws IOException {
        Map<String, byte[]> classBytesMap = new HashMap<>();
        JavaFileManager fileManager = new ForwardingJavaFileManager<StandardJavaFileManager>(
                compiler.getStandardFileManager(null, null, null)) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
                return new SimpleJavaFileObject(URI.create("string:///" + className.replace('.', '/') + kind.extension), kind) {
                    @Override
                    public OutputStream openOutputStream() {
                        return new ByteArrayOutputStream() {
                            @Override
                            public void close() throws IOException {
                                super.close();
                                classBytesMap.put(className, this.toByteArray());
                            }
                        };
                    }
                };
            }
        };
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, dynamicJavaClasses);
        if (!task.call()) {
            throw new RuntimeException("Compilation failed");
        }
        return classBytesMap;
    }
}