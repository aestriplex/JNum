package main.abstractnumber.rational;

import main.abstractnumber.AbstractNumber;
import main.abstractnumber.rational.exceptions.InvalidFormatStringException;
import main.abstractnumber.rational.exceptions.NotImplementedFeatureException;
import main.abstractnumber.rational.exceptions.ZeroDenominatorException;
import main.abstractnumber.rational.exceptions.ZeroExponentialException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.regex.Pattern;

public class RationalNumber extends Number implements AbstractNumber<RationalNumber>, Comparable<RationalNumber> {

    private final static int DEFAULT_SCALE = 2;
    private final static RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;
    private final static Pattern IS_DECIMAL = Pattern.compile("-?\\d+((.)?\\d+)?", Pattern.CASE_INSENSITIVE);
    private final static Pattern IS_INTEGER = Pattern.compile("-?\\d+", Pattern.CASE_INSENSITIVE);

    private final static long[] LONG_10_POW = {
            1,      10,      100,      1000,      10000,
            100000, 1000000, 10000000, 100000000, 1000000000
    };

    private final static long[] LONG_2_POW = {
            1,    2,    4,    8,    16,
            32,   64,   128,  256,  512,
            1024, 2048, 4096, 8192, 16384
    };

    public final static RationalNumber PI =
            new RationalNumber(
                    3141592653589793L,
                    1000000000000000L,
                    false);
    public final static RationalNumber E =
            new RationalNumber(
                    543656365691809L,
                    200000000000000L,
                    false);
    public final static RationalNumber ZERO = new RationalNumber(0);
    public final static RationalNumber ONE = new RationalNumber(1);
    public final static RationalNumber MINUS_ONE = new RationalNumber(-1);
    public final static RationalNumber TEN = new RationalNumber(10);
    public final static RationalNumber HUNDRED = new RationalNumber(100);
    public final static RationalNumber ONE_HALF = new RationalNumber(1,2,false);
    public final static RationalNumber ONE_QUARTER = new RationalNumber(1,4,false);

    private long numerator;
    private long denominator;

    private RationalNumber(long numerator, long denominator, boolean simplify) {

        if (denominator == 0) throw new ZeroDenominatorException();

        this.numerator = numerator;
        this.denominator = denominator;

        setSign();

        if (simplify)
            simplifyRationalNumber();
    }

    private RationalNumber(BigDecimal number, boolean simplify) {

        int scale = number.scale();
        this.numerator = number.movePointRight(scale).longValue();
        this.denominator = expBase10(scale);

        if (denominator == 0) throw new ZeroDenominatorException();

        setSign();

        if (simplify)
            simplifyRationalNumber();
    }

    private RationalNumber(String number, boolean simplify) {

        String inputNumber = number.trim();
        validateString(inputNumber);
        if (isAnInteger(inputNumber)) {
            this.numerator = Long.parseLong(inputNumber);
            this.denominator = 1;
        } else {
            int scale = countDecimalDigits(inputNumber);
            this.numerator = Long.parseLong(inputNumber.replace(".",""));
            this.denominator = expBase10(scale);

            if (denominator == 0) throw new ZeroDenominatorException();

            setSign();

            if (simplify)
                simplifyRationalNumber();
        }
    }

    public RationalNumber(double number) {
        this(Double.toString(number),true);
    }

    public RationalNumber(long numerator) {
        this.numerator = numerator;
        this.denominator = 1;
    }

    public RationalNumber(long numerator, long denominator) {
        this(numerator,denominator,true);
    }

    public RationalNumber(BigDecimal number) { this(number,true); }

    public RationalNumber(String number) { this(number,true); }

    private int countDecimalDigits(String number) {
        int decimalDigits = 0, dotPosition = number.indexOf('.');

        for (int l = number.length(); dotPosition < l-1; decimalDigits++, dotPosition++) ;

        return decimalDigits;
    }
    private boolean isADecimalNumber(String number) {
        return IS_DECIMAL.matcher(number).matches();
    }

    private boolean isAnInteger(String number) {
        return IS_INTEGER.matcher(number).matches();
    }

    private void validateString(String number) {
        if (!isADecimalNumber(number)) throw new InvalidFormatStringException();
    }

    private void simplifyRationalNumber() {
        long gcd = greatestCommonDenominator(this.numerator, this.denominator);
        this.numerator /=  gcd;
        this.denominator /= gcd;
    }

    private void setSign() {
        if (this.denominator < 0) {
            this.numerator *= -1;
            this.numerator *= -1;
        }
    }

    private long expBase2(long exponent) {
        if (exponent <= LONG_2_POW.length) return LONG_2_POW[(int)exponent];
        return naturalPow(2,exponent);
    }

    private long expBase10(long exponent) {
        if (exponent <= LONG_10_POW.length) return LONG_10_POW[(int)exponent];
        return naturalPow(10,exponent);
    }

    private long naturalPow(long base, long exponent) {
        if (base == 0 && exponent == 0) throw new ZeroExponentialException();

        long result = 1;
        for (long i = 0; i < exponent; ++i)
            result *= base;

        return result;
    }

    @Override
    public RationalNumber toggleSign() {
        return new RationalNumber(-1 * this.numerator, this.denominator, false);
    }

    @Override
    public RationalNumber abs() {
        return this.numerator < 0 ? toggleSign() : this;
    }

    @Override
    public float floatValue() { return (float) this.numerator / this.denominator; }

    @Override
    public double doubleValue() { return (double) this.numerator / this.denominator; }

    @Override
    public int intValue() { return (int) (this.numerator / this.denominator); }

    @Override
    public long longValue() { return this.numerator / this.denominator; }

    @Override
    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(numerator)
                .divide(BigDecimal.valueOf(denominator),
                        DEFAULT_SCALE,
                        DEFAULT_ROUNDING_MODE);
    }

    @Override
    public BigDecimal bigDecimalValueWithCustomRounding(int scale, RoundingMode roundingMode) {
        return BigDecimal.valueOf(numerator)
                .divide(BigDecimal.valueOf(denominator), scale, roundingMode);
    }

    private long greatestCommonDenominator(long first, long second) {
        return second == 0 ?
                first :
                greatestCommonDenominator(second, first % second);
    }

    private long leastCommonMultiple(long first, long second) {
        return (first * second) / greatestCommonDenominator(first, second);
    }

//    public long greatestCommonDenominator(long first, long second) {
//        if (first == 0) return second;
//        if (second == 0) return first;
//        long min = Math.min(first,second);
//        for (long i = min; i >= 1; i--) {
//            if (first % i == 0 && second % i == 0)
//                return i;
//        }
//        return 1;
//    }

//    private long leastCommonMultiple(long first, long second) {
//        long max = Math.max(first,second);
//        for (long i = max; i < first * second; i += max)
//            if (i % first == 0 && i % second == 0)
//                return i;
//        return first * second;
//    }

    @Override
    public RationalNumber reciprocal() {
        return new RationalNumber(denominator, numerator,false);
    }

    @Override
    public RationalNumber multiply(RationalNumber other) {
        return new RationalNumber(
                numerator * other.numerator,
                denominator * other.denominator,
                true
        );
    }

    @Override
    public RationalNumber divide(RationalNumber other) {
        return new RationalNumber(
                numerator * other.denominator,
                denominator * other.numerator,
                true
        );
    }

    @Override
    public RationalNumber add(RationalNumber other) {
        long lcm = leastCommonMultiple(denominator, other.denominator);
        long newNumerator = (numerator * (lcm / denominator)) + (other.numerator * (lcm / other.denominator));
        return new RationalNumber(newNumerator, lcm, true);
    }

    @Override
    public RationalNumber subtract(RationalNumber other) {
        long lcm = leastCommonMultiple(denominator, other.denominator);
        long newNumerator = (numerator * (lcm / denominator)) - (other.numerator * (lcm / other.denominator));
        return new RationalNumber(newNumerator, lcm, true);
    }

    @Override
    public RationalNumber multiply(long other) {
        return new RationalNumber(numerator * other, denominator, true);
    }

    @Override
    public RationalNumber divide(long other) {
        return new RationalNumber(numerator, denominator * other, true);
    }

    @Override
    public RationalNumber add(long other) {
        long lcm = leastCommonMultiple(denominator, other);
        long newNumerator = (numerator * (lcm / denominator)) + (other * lcm);
        return new RationalNumber(newNumerator, lcm, true);
    }

    @Override
    public RationalNumber subtract(long other) {
        long lcm = leastCommonMultiple(denominator, other);
        long newNumerator = (numerator * (lcm / denominator)) - (other * lcm);
        return new RationalNumber(newNumerator, lcm, true);
    }

    @Override
    public RationalNumber increment() {
        return new RationalNumber(this.numerator + 1, this.denominator, true);
    }

    @Override
    public RationalNumber decrement() {
        return new RationalNumber(this.numerator - 1, this.denominator,true);
    }

    @Override
    public RationalNumber duplicateThis() {
        return new RationalNumber(this.numerator, this.denominator, false);
    }

    @Override
    public RationalNumber sum(RationalNumber... others) {
        RationalNumber result = duplicateThis();
        for (final RationalNumber other : others) result = result.add(other);
        return new RationalNumber(result.numerator, result.denominator,true);
    }

    @Override
    public RationalNumber product(RationalNumber... others) {
        RationalNumber result = duplicateThis();
        for (RationalNumber other : others) result = result.multiply(other);
        return new RationalNumber(result.numerator, result.denominator, true);
    }

    @Override
    public RationalNumber difference(RationalNumber... others) {
        RationalNumber result = duplicateThis();
        for (RationalNumber other : others) result = result.subtract(other);
        return new RationalNumber(result.numerator, result.denominator, true);
    }

    @Override
    public RationalNumber quotient(RationalNumber... others) {
        RationalNumber result = duplicateThis();
        for (RationalNumber other : others) result = result.divide(other);
        return new RationalNumber(result.numerator, result.denominator, true);
    }

    @Override
    public RationalNumber applyPercentage() {

        return new RationalNumber(this.numerator * 100, this.denominator, true);
    }

    @Override
    public RationalNumber getPercentageOf(RationalNumber number) {
        return multiply(number).applyPercentage();
    }

    @Override
    public RationalNumber power(long exponent) {
        if (exponent >= 0)
            return new RationalNumber(
                    naturalPow(this.numerator, exponent),
                    naturalPow(this.denominator, exponent),
                    false
            );

        long exp = Math.abs(exponent);
        return new RationalNumber(
                naturalPow(this.denominator, exp),
                naturalPow(this.numerator, exp),
                false
        );
    }

    @Override
    public RationalNumber power(RationalNumber exponent) {
        if (exponent.denominator == 1) return power(exponent.numerator);
        throw new NotImplementedFeatureException("powers with rational exponent");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RationalNumber that = (RationalNumber) o;
        return numerator == that.numerator && denominator == that.denominator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }

    @Override
    public String toString() {
        return this.denominator == 1 ?
                String.format("%d", this.numerator) :
                String.format("{%d/%d}", this.numerator, this.denominator);
    }

    public static RationalNumber valueOf(BigDecimal decimalNumber) {
        return new RationalNumber(decimalNumber, true);
    }

    public static RationalNumber valueOf(long numerator, long denominator) {
        return new RationalNumber(numerator, denominator, true);
    }

    public static RationalNumber valueOf(long numerator) {
        return new RationalNumber(numerator);
    }

    public static RationalNumber valueOf(String number) {
        return new RationalNumber(number, true);
    }

    public static RationalNumber valueOf(double number) {
        return new RationalNumber(number);
    }

    @Override
    public List<RationalNumber> range(RationalNumber stop) {

        List<RationalNumber> newList = new ArrayList<>();

        if (compareTo(stop) >= 0) {
            for (RationalNumber i = this; i.compareTo(stop) > 0; i = i.subtract(RationalNumber.valueOf(1))) {
                newList.add(i);
            }
        } else {
            for (RationalNumber i = this; i.compareTo(stop) < 0; i = i.add(RationalNumber.valueOf(1))) {
                newList.add(i);
            }
        }

        return newList;
    }

    @Override
    public List<RationalNumber> range(RationalNumber stop, long step) {
        return range(stop,RationalNumber.valueOf(step));
    }

    @Override
    public List<RationalNumber> range(RationalNumber stop, RationalNumber step) {

        List<RationalNumber> newList = new ArrayList<>();

        if (compareTo(stop) >= 0) {
            for (RationalNumber i = this; i.compareTo(stop) > 0; i = i.subtract(step)) {
                newList.add(i);
            }
        } else {
            for (RationalNumber i = this; i.compareTo(stop) < 0; i = i.add(step)) {
                newList.add(i);
            }
        }

        return newList;
    }

    public static List<RationalNumber> range(RationalNumber start, RationalNumber stop, RationalNumber step) {

        List<RationalNumber> newList = new ArrayList<>();

        if (start.compareTo(stop) >= 0) {
            for (RationalNumber i = start; i.compareTo(stop) > 0; i = i.subtract(step)) {
                newList.add(i);
            }
        } else {
            for (RationalNumber i = stop; i.compareTo(stop) < 0; i = i.add(step)) {
                newList.add(i);
            }
        }

        return newList;
    }

    @Override
    public int compareTo(RationalNumber o) {
        if (this.denominator == 1 && o.denominator == 1) return Long.compare(this.numerator,o.numerator);
        RationalNumber quotient = divide(o);
        return Long.compare(quotient.numerator, quotient.denominator);
    }
}
