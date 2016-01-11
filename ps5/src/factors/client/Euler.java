package factors.client;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by clay on 1/11/16.
 */
public class Euler {
    private static BigInteger fibIter(BigInteger fib0, BigInteger fib1) {
        return (fib0.add(fib1));
    }

    private static int getMinDigits(BigInteger minDigits) {
        minDigits = minDigits.add(BigInteger.ONE.negate());
        BigInteger fib0 = BigInteger.ONE;
        BigInteger fib1 = BigInteger.ONE;

        BigInteger fib2 = fibIter(fib0, fib1);
        int index = 3;
        BigInteger digits = new BigDecimal(Math.log10(fib2.doubleValue())).toBigInteger();

        while (digits.compareTo(minDigits) == -1) {
            fib0 = fib1;
            fib1 = fib2;
            fib2 = fibIter(fib0, fib1);
            digits = new BigDecimal(Math.log10(fib2.doubleValue())).toBigInteger();
            index++;
        }
        return index;
    }

    public static void main(String[] args) {
        String N = "1000";
        BigInteger n = new BigInteger(N);
        System.out.print(Euler.getMinDigits(n));
    }
}

