package main.abstractnumber;

import main.abstractnumber.rational.RationalNumber;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface AbstractNumber<T> {

    RationalNumber toggleSign();

    RationalNumber abs();

    double doubleValue();

    BigDecimal bigDecimalValue();

    long longValue();

    BigDecimal bigDecimalValueWithCustomRounding(int scale, RoundingMode roundingMode);

    T reciprocal();

    T multiply(T other);

    T divide(T other);

    T add(T other);

    T subtract(T other);

    RationalNumber multiply(long other);

    RationalNumber divide(long other);

    RationalNumber add(long other);

    RationalNumber subtract(long other);

    RationalNumber increment();

    RationalNumber decrement();

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
