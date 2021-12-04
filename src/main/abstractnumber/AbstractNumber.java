package main.abstractnumber;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface AbstractNumber<T> {

    double doubleValue();

    BigDecimal bigDecimalValue();

    long longValue();

    BigDecimal bigDecimalValueWithCustomRounding(int scale, RoundingMode roundingMode);

    T reciprocal();

    T multiply(T other);

    T divide(T other);

    T add(T other);

    T subtract(T other);

    T duplicateThis();

    T sum(T... others);

    T product(T... others);

    T difference(T... others);

    T quotient(T... others);

    T applyPercentage();

    T getPercentageOf(T number);

    T power(long exponent);

    T power(T exponent);

    @Override
    boolean equals(Object other);

    @Override
    int hashCode();

    @Override
    String toString();
    
    int compareTo(T o);
}
