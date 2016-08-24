package factors.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  PrimeFactorsClient class for PrimeFactorsServer.
 *
 *  Your PrimeFactorsClient class should take in Program arguments space-delimited
 *  indicating which PrimeFactorsServers it will connect to.
 *      ex. args of "localhost:4444 localhost:4445 localhost:4446"
 *          will connect the client to PrimeFactorsServers running on
 *          localhost:4444, localhost:4445, localhost:4446
 *
 *  Your client should take user input from standard input.  The appropriate input
 *  that can be processed is a number.  If your input is not of the correct format,
 *  you should ignore it and continue to the next one.
 *
 *  Your client should distribute to each server the appropriate range of values
 *  to look for prime factors through.
 */


public class PrimeFactorsClient {


    /**
     * @param args String array containing Program arguments.  Each String indicates a
     *      PrimeFactorsServer location in the form "host:port"
     *      If no program arguments are inputted, this Client will terminate.
     */

    // BigInteger numServers = new BigInteger("3");
    String hostName;
    int portNumber;
    String N;
    String low;
    String high;
    List<String> factors = new ArrayList<String>();



    public static void main(String[] args) {

        String hostName = "localhost";
        // get number of servers to user
        BigInteger numServers = new BigDecimal(args.length).toBigInteger();

        // convert string args into integers
        List<Integer> portNumbers = new ArrayList<Integer>();
        for (String portNumber : args) {
            portNumbers.add(Integer.parseInt(portNumber));
        }

        // get integer to factor from user
        try (
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ) {
            while (true) {
                List<String> factors = new ArrayList<String>();
                String N = stdIn.readLine();
                List<BigInteger> partition = PartitionSearchSpace.getPartition(N, numServers);
                System.out.println(partition);

                for (int i = 0; i <= partition.size() - 2; i++) {
                    int portNumber = portNumbers.get(i);
                    System.out.println(portNumber);
                    String low = partition.get(i).add(BigInteger.ONE).toString();
                    String high = partition.get(i + 1).toString();
                    try (
                            Socket echoSocket = new Socket(hostName, portNumber);
                            PrintWriter out =
                                    new PrintWriter(echoSocket.getOutputStream(), true);
                            BufferedReader in =
                                    new BufferedReader(
                                            new InputStreamReader(echoSocket.getInputStream()));
                    ) {
                        String outString = "factor " + N + " " + low + " " + high;
                        out.println(outString);
                        String factorsOutput;
                        // list to hold factors
                        while ((factorsOutput = in.readLine()) != null) {
                            factors.add(Arrays.asList(factorsOutput.split("\\s+")).get(2));
                        }
                        echoSocket.close();
                    } catch (IOException e) {
                        System.err.println("Couldn't read standard input.");
                        System.exit(1);
                    }
                }

                StringBuilder outString = new StringBuilder();
                for (String factor : factors) {
                    outString.append(factor + "*");
                }
                System.out.println(">>>> " + outString.substring(0, outString.length() - 1).toString() + "\n");

            }
        } catch(IOException e){
            System.err.println("Couldn't read standard input.");
            System.exit(1);
        }
    }
}

