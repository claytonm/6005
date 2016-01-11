package factors.server;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clay on 1/5/16.
 */
public class PrimeFactorFind {

    public static List<BigInteger> FindFactor(BigInteger n, BigInteger low, BigInteger high, int primeCertainty) {
        BigInteger N = n;
        List<BigInteger> primeFactors = new ArrayList<BigInteger>();
        for (BigInteger x = low; x.compareTo(high) == -1 | x.compareTo(high) == 0; x = x.add(BigInteger.ONE)) {
            if (x.isProbablePrime(primeCertainty)) {
                while (n.remainder(x).equals(BigInteger.ZERO) & !(high.compareTo(x) == -1)) {
                    primeFactors.add(x);
                    n = n.divide(x);
                }
            }
        }
        if (!n.equals(BigInteger.ONE) & !(N.compareTo(n) == 0)) {
            primeFactors.add(n);
        }
        return primeFactors;
    }

    public static void main(String[] args) {
        String N = "1332425524";
        String LOW = "24336";
        String HIGH = "36503";
        BigInteger n = new BigInteger(N);
        BigInteger low = new BigInteger(LOW);
        BigInteger high = new BigInteger(HIGH);
        int PRIME_CERTAINTY = 10;

        List<BigInteger> factors = FindFactor(n, low, high, PRIME_CERTAINTY);
        System.out.print(factors);
    }
}
