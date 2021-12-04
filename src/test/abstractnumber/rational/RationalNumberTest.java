package test.abstractnumber.rational;

import main.abstractnumber.rational.exceptions.NotImplementedFeatureException;
import main.abstractnumber.rational.RationalNumber;
import main.abstractnumber.rational.exceptions.ZeroDenominatorException;
import main.abstractnumber.rational.exceptions.ZeroExponentialException;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.*;

public class RationalNumberTest {

    @Test(expected = ZeroDenominatorException.class)
    public void zeroDenominatorTest() {
        RationalNumber n = RationalNumber.valueOf(1,0);
    }

    @Test
    public void bigDecimalValueTest() {
        RationalNumber n = RationalNumber.valueOf("2.5");
        BigDecimal expected = new BigDecimal("2.5").setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected,n.bigDecimalValue());
    }

    @Test
    public void longValueTest() {
        RationalNumber n = RationalNumber.valueOf("2.5");

        assertEquals(2,n.longValue());
    }

    @Test
    public void doubleValueTest() {
        RationalNumber n = RationalNumber.valueOf("2.5");

        assertEquals((double)5/2,n.doubleValue(),0.01);
    }

    @Test
    public void bigDecimalValueWithCustomRoundingTest() {
        RationalNumber n = RationalNumber.valueOf(1,6);
        BigDecimal expected = new BigDecimal("0.166666666").setScale(6, RoundingMode.FLOOR);

        assertEquals(expected,n.bigDecimalValueWithCustomRounding(6,RoundingMode.FLOOR));
    }

    @Test
    public void reciprocalTest() {
        RationalNumber n1 = RationalNumber.valueOf(2,8);
        RationalNumber n2 = RationalNumber.valueOf(1,2);
        RationalNumber n3 = RationalNumber.valueOf(2);
        RationalNumber n4 = RationalNumber.valueOf("0.5");

        assertEquals(RationalNumber.valueOf(8,2),n1.reciprocal());
        assertEquals(RationalNumber.valueOf(2),n2.reciprocal());
        assertEquals(RationalNumber.valueOf(1,2),n3.reciprocal());
        assertEquals(RationalNumber.valueOf(2),n4.reciprocal());
    }

    @Test
    public void multiplyTest() {
        RationalNumber n1 = RationalNumber.valueOf(2,8);
        RationalNumber n2 = RationalNumber.valueOf(7,24);

        RationalNumber expected = RationalNumber.valueOf(7,96);

        assertEquals(expected,n1.multiply(n2));
    }

    @Test
    public void divideTest() {
        RationalNumber n1 = RationalNumber.valueOf(2,8);
        RationalNumber n2 = RationalNumber.valueOf(7,24);

        RationalNumber expected = RationalNumber.valueOf(6,7);

        assertEquals(expected,n1.divide(n2));
    }

    @Test
    public void addTest() {
        RationalNumber n1 = RationalNumber.valueOf(2,8);
        RationalNumber n2 = RationalNumber.valueOf(7,24);

        RationalNumber expected = RationalNumber.valueOf(13,24);

        assertEquals(expected,n1.add(n2));
    }

    @Test
    public void subtractTest() {
        RationalNumber n1 = RationalNumber.valueOf(2,8);
        RationalNumber n2 = RationalNumber.valueOf(7,24);

        RationalNumber expected = RationalNumber.valueOf(-1,24);

        assertEquals(expected,n1.subtract(n2));
    }

    @Test
    public void duplicateThisTest() {
        RationalNumber n = RationalNumber.valueOf(2,8);

        assertEquals(n, n.duplicateThis());
    }

    @Test
    public void sumTest() {
        RationalNumber n1 = RationalNumber.valueOf(1,2);
        RationalNumber n2 = RationalNumber.valueOf(1,4);
        RationalNumber n3 = RationalNumber.valueOf(2,8);
        RationalNumber n4 = RationalNumber.valueOf(7,24);
        RationalNumber n5 = RationalNumber.valueOf(5,2);

        RationalNumber expected = RationalNumber.valueOf(91,24);

        assertEquals(expected,n1.sum(n2,n3,n4,n5));
    }

    @Test
    public void productTest() {
        RationalNumber n1 = RationalNumber.valueOf(1,2);
        RationalNumber n2 = RationalNumber.valueOf(1,4);
        RationalNumber n3 = RationalNumber.valueOf(2,8);
        RationalNumber n4 = RationalNumber.valueOf(7,24);
        RationalNumber n5 = RationalNumber.valueOf(5,2);

        RationalNumber expected = RationalNumber.valueOf(35,1536);

        assertEquals(expected,n1.product(n2,n3,n4,n5));
    }

    @Test
    public void differenceTest() {
        RationalNumber n1 = RationalNumber.valueOf(1,2);
        RationalNumber n2 = RationalNumber.valueOf(1,4);
        RationalNumber n3 = RationalNumber.valueOf(2,8);
        RationalNumber n4 = RationalNumber.valueOf(7,24);
        RationalNumber n5 = RationalNumber.valueOf(5,2);

        RationalNumber expected = RationalNumber.valueOf(-67,24);

        assertEquals(expected,n1.difference(n2,n3,n4,n5));
    }

    @Test
    public void quotientTest() {
        RationalNumber n1 = RationalNumber.valueOf(1,2);
        RationalNumber n2 = RationalNumber.valueOf(1,4);
        RationalNumber n3 = RationalNumber.valueOf(2,8);
        RationalNumber n4 = RationalNumber.valueOf(7,24);
        RationalNumber n5 = RationalNumber.valueOf(5,2);

        RationalNumber expected = RationalNumber.valueOf(384,35);

        assertEquals(expected,n1.quotient(n2,n3,n4,n5));
    }

    @Test
    public void applyPercentageTest() {
        RationalNumber n1 = RationalNumber.valueOf(1,2);

        RationalNumber expected = RationalNumber.valueOf(50);

        assertEquals(expected,n1.applyPercentage());
    }

    @Test
    public void getPercentageOfTest() {
        RationalNumber n1 = RationalNumber.valueOf(91,24);
        RationalNumber percentage = RationalNumber.valueOf(1,2);

        RationalNumber expected = RationalNumber.valueOf(2275,12);

        assertEquals(expected,n1.getPercentageOf(percentage));
    }

    @Test
    public void powerTest() {
        RationalNumber n = RationalNumber.valueOf(2);
        RationalNumber exp1 = RationalNumber.valueOf(-2);
        RationalNumber exp2 = RationalNumber.valueOf(5);

        assertEquals(RationalNumber.valueOf(1,4),n.power(exp1));
        assertEquals(RationalNumber.valueOf(32),n.power(exp2));
    }

    @Test(expected = NotImplementedFeatureException.class)
    public void powerWithRationalExponentTest() {
        RationalNumber n = RationalNumber.valueOf(2);
        RationalNumber exp = RationalNumber.valueOf(1,2);
        n.power(exp);
    }

    @Test(expected = ZeroExponentialException.class)
    public void powerWithZeroExponentAndZeroBase() {
        RationalNumber n = RationalNumber.valueOf(0);
        n.power(RationalNumber.valueOf(0));
    }

    @Test
    public void toStringTest() {
        RationalNumber n1 = RationalNumber.valueOf(1,2);
        RationalNumber n2 = RationalNumber.valueOf(4);

        assertEquals("{1/2}",n1.toString());
        assertEquals("4",n2.toString());
    }

    @Test
    public void compareToTest() {
        RationalNumber n1 = RationalNumber.valueOf(1,2);
        RationalNumber n2 = RationalNumber.valueOf(1,4);
        RationalNumber n3 = RationalNumber.valueOf(2,8);
        RationalNumber n4 = RationalNumber.valueOf(7,24);
        RationalNumber n5 = RationalNumber.valueOf(5,2);

        assertEquals(1, n1.compareTo(n2));
        assertEquals(0, n2.compareTo(n3));
        assertEquals(-1, n3.compareTo(n4));
        assertEquals(-1, n4.compareTo(n5));
    }
}
