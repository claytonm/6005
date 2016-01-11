package util;

import java.math.BigInteger;

public class BigMath {

    /**
     * Given a BigInteger input n, where n >= 0, returns the largest BigInteger r such that r*r <= n.
     * 
     * For n < 0, returns 0.
     * 
     * 
     * details: http://faruk.akgul.org/blog/javas-missing-algorithm-biginteger-sqrt
     * 
     * @param n BigInteger input.
     * @return for n >= 0: largest BigInteger r such that r*r <= n.
     *             n <  0: BigInteger 0
     */
    public static BigInteger sqrt(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
        while(b.compareTo(a) >= 0) {
          BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
          if (mid.multiply(mid).compareTo(n) > 0) 
              b = mid.subtract(BigInteger.ONE);
          else 
              a = mid.add(BigInteger.ONE);
        }
        return a.subtract(BigInteger.ONE);
    }
}
