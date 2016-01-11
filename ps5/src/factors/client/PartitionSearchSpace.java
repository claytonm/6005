package factors.client;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clay on 1/7/16.
 */
public class PartitionSearchSpace {

    public static List<BigInteger> getPartition(String n, BigInteger p) {
        BigInteger N = new BigInteger(n);
        BigInteger logN = new BigDecimal(Math.sqrt(N.doubleValue())).toBigInteger();
        BigInteger partLength = logN.divide(p);
        // partition holds search space cut-offs
        List<BigInteger> partition = new ArrayList<BigInteger>();
        List<BigInteger> partitionReturn = new ArrayList<BigInteger>();
        partition.add(BigInteger.ZERO);
        BigInteger currentPart = BigInteger.ONE.add(BigInteger.ONE);

        // BigInteger k = new BigDecimal(doubleValue).toBigInteger();

        while (currentPart.compareTo(logN) == -1) {
            currentPart = currentPart.add(partLength);
            partition.add(currentPart);
        }

        int j = 0;
        while (j < 4) {
            partitionReturn.add(partition.get(j));
            j++;
        }
        return (partitionReturn);
    }

    public static void main(String args[]) {
        String N = "123456789";
        BigInteger p = new BigInteger("3");
        List<BigInteger> partition = getPartition(N, p);
        System.out.println(partition);
    }
}
