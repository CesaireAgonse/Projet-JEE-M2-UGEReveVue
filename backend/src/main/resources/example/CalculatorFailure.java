import java.io.ObjectStreamConstants;

public class Calculator implements ObjectStreamConstants {
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return 0;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public int divide(int a, int b) {
        return 0;
    }
}