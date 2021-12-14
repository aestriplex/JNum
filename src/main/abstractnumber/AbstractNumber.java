package main.abstractnumber;

import main.abstractnumber.rational.RationalNumber;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public interface AbstractNumber<T> extends Comparable<T> {

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

    T multiply(long other);

    T divide(long other);

    T add(long other);

    T subtract(long other);

    T increment();

    T decrement();

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

    List<T> range(T stop);

    List<T> range(T stop, long step);

    List<T> range(T stop, T step);

    int compareTo(T o);
}
