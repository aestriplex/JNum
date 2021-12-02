import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

public class RationalNumber {

    private final static int DEFAULT_SCALE = 2;
    private final static RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;
    private final static Pattern IS_DECIMAL = Pattern.compile("-?\\d+((.)?\\d+)?", Pattern.CASE_INSENSITIVE);
    private final static Pattern IS_INTEGER = Pattern.compile("-?\\d+", Pattern.CASE_INSENSITIVE);
    private long numerator;
    private long denominator;

    private RationalNumber(long numerator, long denominator, boolean simplify) {
        if (simplify) {
            simplifyAndStoreRationalNumber(numerator,denominator);
        } else {
            this.numerator = numerator;
            this.denominator = denominator;
        }
    }

    private RationalNumber(BigDecimal number, boolean simplify) {
        int scale = number.scale();
        if (simplify) {
            simplifyAndStoreRationalNumber(number.movePointRight(scale).longValue(),expBase10(scale));
        } else {
            this.numerator = number.movePointRight(scale).longValue();
            this.denominator = expBase10(scale);
        }
    }

    private RationalNumber(String number, boolean simplify) {
        String inputNumber = number.trim();
        validateString(inputNumber);
        if (isAnInteger(inputNumber)) {
            this.numerator = Long.parseLong(inputNumber);
            this.denominator = 1;
        } else {
            int scale = countDecimalDigits(inputNumber);
            long num = Long.parseLong(inputNumber.replace(".",""));
            long den = expBase10(scale);
            if (simplify) {
                simplifyAndStoreRationalNumber(num, den);
            } else {
                this.numerator = num;
                this.denominator = den;
            }
        }
    }

    public RationalNumber(long numerator) {
        this.numerator = numerator;
        this.denominator = 1L;
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

    private void simplifyAndStoreRationalNumber(long numerator, long denominator) {
        long gcd = greatestCommonDenominator(numerator, denominator);
        while (gcd != 1) {
            numerator /=  gcd;
            denominator /= gcd;
            gcd = greatestCommonDenominator(numerator, denominator);
        }

        this.numerator = numerator;
        this.denominator = denominator;
    }

    private long expBase2(long exponent) {
        return naturalPow(2,exponent);
    }

    private long expBase10(long exponent) {
        return naturalPow(10,exponent);
    }

    private long naturalPow(long base, long exponent) {
        if (base == 0 && exponent == 0) throw new ZeroExponentialException();

        long result = 1;
        for (long i = 0; i < exponent; ++i)
            result *= base;

        return result;
    }

    public double doubleValue() { return (double) numerator / denominator; }

    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(numerator)
                .divide(BigDecimal.valueOf(denominator),
                        DEFAULT_SCALE,
                        DEFAULT_ROUNDING_MODE);
    }

    public long longValue() {
        return bigDecimalValue()
                .longValue();
    }

    public BigDecimal bigDecimalValueWithCustomRounding(int scale, RoundingMode roundingMode) {
        return BigDecimal.valueOf(numerator)
                .divide(BigDecimal.valueOf(denominator),scale,roundingMode);
    }

    public long longValueWithCustomRounding(int scale, RoundingMode roundingMode) {
        return bigDecimalValueWithCustomRounding(scale,roundingMode)
                .longValue();
    }

    public RationalNumber reciprocal() {
        return new RationalNumber(denominator, numerator,false);
    }

    public RationalNumber multiply(RationalNumber other) {
        return new RationalNumber(
                numerator * other.numerator,
                denominator * other.denominator
        );
    }

    public RationalNumber divide(RationalNumber other) {
        return new RationalNumber(
                numerator * other.denominator,
                denominator * other.numerator
        );
    }

    private long greatestCommonDenominator(long first, long second) {
        return second == 0 ?
                first :
                greatestCommonDenominator(second, first % second);
    }

    public long leastCommonMultiple(long first, long second) {
        long max = Math.max(first,second);
        for (long i = max; i < first * second; i += max)
            if (i % first == 0 && i % second == 0)
                return i;
        return first * second;
    }

//    private long leastCommonMultiple(long first, long second) {
//        return (first * second) / greatestCommonDenominator(first, second);
//    }

    public RationalNumber add(RationalNumber other) {
        long lcm = leastCommonMultiple(denominator, other.denominator);
        long newNumerator = (numerator * (lcm / other.denominator)) + (other.numerator * (lcm / denominator));
        return new RationalNumber(newNumerator, lcm);
    }

    public RationalNumber subtract(RationalNumber other) {
        long lcm = leastCommonMultiple(denominator, other.denominator);
        long newNumerator = (numerator * (lcm / other.denominator)) - (other.numerator * (lcm / denominator));
        return new RationalNumber(newNumerator, lcm);
    }

    public RationalNumber sum(RationalNumber... others) {
        RationalNumber result = new RationalNumber(numerator,denominator,false);
        for (final RationalNumber other : others) result = result.add(other);
        return new RationalNumber(result.numerator, result.denominator);
    }

    public RationalNumber product(RationalNumber... others) {
        RationalNumber result = new RationalNumber(numerator,denominator);
        for (RationalNumber other : others) result.multiply(other);
        return new RationalNumber(result.numerator, result.denominator);
    }

    public RationalNumber applyPercentage() {
        return new RationalNumber(numerator * 100, denominator);
    }

    public RationalNumber getPercentageOf(RationalNumber number) {
        return multiply(number).applyPercentage();
    }

    public RationalNumber power(long exponent) {
        if (exponent >= 0)
            return new RationalNumber(
                    naturalPow(numerator,exponent),
                    naturalPow(denominator,exponent),
                    false
            );

        long exp = Math.abs(exponent);
        return new RationalNumber(
                naturalPow(denominator,exp),
                naturalPow(numerator,exp),
                false
        );
    }

    public RationalNumber power(RationalNumber exponent) {
        if (exponent.denominator == 1) return power(exponent.numerator);
        throw new NotImplementedFeatureException("powers with rational exponent");
    }

    public boolean equals(RationalNumber other) {
        return numerator == other.numerator && denominator == other.denominator;
    }

    @Override
    public String toString() {
        return String.format("{%d/%d}",numerator,denominator);
    }

    public static RationalNumber valueOf(BigDecimal decimalNumber) {
        return new RationalNumber(decimalNumber);
    }

    public static RationalNumber valueOf(long numerator, long denominator) {
        return new RationalNumber(numerator, denominator);
    }

    public static RationalNumber valueOf(long numerator) {
        return new RationalNumber(numerator);
    }

    public static RationalNumber valueOf(String number) {
        return new RationalNumber(number);
    }
}
