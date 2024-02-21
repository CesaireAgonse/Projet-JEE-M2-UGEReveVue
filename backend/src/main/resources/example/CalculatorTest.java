import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class CalculatorTest {
    
    @Test
    public void testAdd() {
        Calculator calculator = new Calculator();
        Assertions.assertEquals(5, calculator.add(2, 3));
    }

    @Test
    public void testSubtract() {
        Calculator calculator = new Calculator();
        Assertions.assertEquals(2, calculator.subtract(5, 3));
    }

    @Test
    public void testMultiply() {
        Calculator calculator = new Calculator();
        Assertions.assertEquals(15, calculator.multiply(3, 5));
    }

    @Test
    public void testDivide() {
        Calculator calculator = new Calculator();
        Assertions.assertEquals(2, calculator.divide(10, 5));
    }

    @Test
    public void testDivideByZero() {
        Calculator calculator = new Calculator();
        Assertions.assertThrows(IllegalArgumentException.class, () -> calculator.divide(10, 0));
    }
}