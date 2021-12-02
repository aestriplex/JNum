import java.math.BigDecimal;
import java.math.RoundingMode;

public class Main {

    private static final String N = "0.5";

    public static void main(String[] args) {
        double n = new RationalNumber(103)
                .divide(RationalNumber.valueOf(60))
                .multiply(RationalNumber.valueOf(350))
                .bigDecimalValue()
                .doubleValue();
        double m = BigDecimal.valueOf(103)
                .divide(BigDecimal.valueOf(60),2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(350))
                .doubleValue();

        System.out.println(String.format("%f    %f",n,m));

        RationalNumber num = RationalNumber.valueOf(1,4);
        System.out.println(num.power(RationalNumber.valueOf(-2)));
    }
}
