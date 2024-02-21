package fr.uge.microservice.tool;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynamicJavaClass extends SimpleJavaFileObject {
    private final String code;

    public DynamicJavaClass(String name, String code) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}