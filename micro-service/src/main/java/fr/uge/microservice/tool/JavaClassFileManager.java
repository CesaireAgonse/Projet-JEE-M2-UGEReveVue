package fr.uge.microservice.tool;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class JavaClassFileManager extends ForwardingJavaFileManager<StandardJavaFileManager>{

    private byte[] clazz;

    protected JavaClassFileManager(StandardJavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
        return new SimpleJavaFileObject(URI.create("string:///" + className.replace('.', '/') + kind.extension), kind) {
            @Override
            public OutputStream openOutputStream() {
                return new ByteArrayOutputStream() {
                    @Override
                    public void close() throws IOException {
                        super.close();
                        clazz = this.toByteArray();
                    }
                };
            }
        };
    }

    public byte[] getClazz() {
        return clazz;
    }
}
