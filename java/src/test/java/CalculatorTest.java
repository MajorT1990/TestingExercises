import calculator.Calculator;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.*;
import java.math.BigInteger;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assume.assumeThat;



@RunWith(JUnitQuickcheck.class)
public class CalculatorTest {

    private Calculator calc = new Calculator();

    @Property
    public void addition(int x,int y){
        System.out.println(x + "+" + y);
        assertEquals(
                new BigInteger(String.valueOf(x)).add(new BigInteger(String.valueOf(y))).intValue(),
                calc.add(x,y));
    }

    @Property
    public void addTestsForMultiply(@InRange(minInt = -20,maxInt = 20)int x, @InRange(minInt = -20,maxInt = 20)int y) {
        assumeThat(Math.abs(x)+Math.abs(y),lessThan(20));
        System.out.println(x + "*" + y);
        assertEquals(
                new BigInteger(String.valueOf(x)).multiply(new BigInteger(String.valueOf(y))).intValue(),
                calc.multiply(x,y));

    }
}