package fr.uge.microservice.tool;

public class ByteClassLoader extends ClassLoader {

    public ByteClassLoader() {}

    public ByteClassLoader(ClassLoader parentClassLoader) {
        super(parentClassLoader);
    }

    public Class<?> defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}