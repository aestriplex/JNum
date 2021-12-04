package test.abstractnumber.rational;

import main.abstractnumber.rational.RationalNumber;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.*;

public class RationalNumberTest {

    @Test
    public void bigDecimalValue() {
        RationalNumber n = RationalNumber.valueOf("2.5");
        BigDecimal expected = new BigDecimal("2.5").setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected,n.bigDecimalValue());
    }

    @Test
    public void longValue() {
        RationalNumber n = RationalNumber.valueOf("2.5");
        assertEquals(2,n.longValue());
    }

    @Test
    public void bigDecimalValueWithCustomRounding() {
        RationalNumber n = RationalNumber.valueOf(1,6);
        BigDecimal expected = new BigDecimal("0.166666666").setScale(6, RoundingMode.FLOOR);

        assertEquals(expected,n.bigDecimalValueWithCustomRounding(6,RoundingMode.FLOOR));
    }

    @Test
    public void longValueWithCustomRounding() {
    }

    @Test
    public void reciprocal() {
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
    public void multiply() {
        RationalNumber n1 = RationalNumber.valueOf(2,8);
        RationalNumber n2 = RationalNumber.valueOf(7,24);

        RationalNumber expected = RationalNumber.valueOf(7,96);

        assertEquals(expected,n1.multiply(n2));
    }

    @Test
    public void divide() {
        RationalNumber n1 = RationalNumber.valueOf(2,8);
        RationalNumber n2 = RationalNumber.valueOf(7,24);

        RationalNumber expected = RationalNumber.valueOf(6,7);

        assertEquals(expected,n1.divide(n2));
    }

    @Test
    public void add() {
        RationalNumber n1 = RationalNumber.valueOf(2,8);
        RationalNumber n2 = RationalNumber.valueOf(7,24);

        RationalNumber expected = RationalNumber.valueOf(13,24);

        assertEquals(expected,n1.add(n2));
    }

    @Test
    public void subtract() {
        RationalNumber n1 = RationalNumber.valueOf(2,8);
        RationalNumber n2 = RationalNumber.valueOf(7,24);

        RationalNumber expected = RationalNumber.valueOf(-1,24);

        assertEquals(expected,n1.subtract(n2));
    }

    @Test
    public void duplicateThis() {
        RationalNumber n = RationalNumber.valueOf(2,8);

        assertEquals(n, n.duplicateThis());
    }

    @Test
    public void sum() {
        RationalNumber n1 = RationalNumber.valueOf(1,2);
        RationalNumber n2 = RationalNumber.valueOf(1,4);
        RationalNumber n3 = RationalNumber.valueOf(2,8);
        RationalNumber n4 = RationalNumber.valueOf(7,24);
        RationalNumber n5 = RationalNumber.valueOf(5,2);

        RationalNumber expected = RationalNumber.valueOf(91,24);

        assertEquals(expected,n1.sum(n2,n3,n4,n5));
    }

    @Test
    public void product() {
        RationalNumber n1 = RationalNumber.valueOf(1,2);
        RationalNumber n2 = RationalNumber.valueOf(1,4);
        RationalNumber n3 = RationalNumber.valueOf(2,8);
        RationalNumber n4 = RationalNumber.valueOf(7,24);
        RationalNumber n5 = RationalNumber.valueOf(5,2);

        RationalNumber expected = RationalNumber.valueOf(35,1536);

        assertEquals(expected,n1.product(n2,n3,n4,n5));
    }

    @Test
    public void applyPercentage() {
        RationalNumber n1 = RationalNumber.valueOf(1,2);

        RationalNumber expected = RationalNumber.valueOf(50);

        assertEquals(expected,n1.applyPercentage());
    }

    @Test
    public void getPercentageOf() {
        RationalNumber n1 = RationalNumber.valueOf(91,24);
        RationalNumber percentage = RationalNumber.valueOf(1,2);

        RationalNumber expected = RationalNumber.valueOf(2275,12);

        assertEquals(expected,n1.getPercentageOf(percentage));
    }

    @Test
    public void testPower() {
    }

    @Test
    public void testToString() {
        RationalNumber n1 = RationalNumber.valueOf(1,2);
        RationalNumber n2 = RationalNumber.valueOf(4);

        assertEquals("{1/2}",n1.toString());
        assertEquals("4",n2.toString());
    }

    @Test
    public void compareTo() {
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