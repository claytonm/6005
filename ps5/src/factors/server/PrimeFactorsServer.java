package factors.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 *  PrimeFactorsServer performs the "server-side" algorithm 
 *  for counting prime factors.
 *
 *  Your PrimeFactorsServer should take in a single Program Argument 
 *  indicating which port your Server will be listening on.
 *      ex. arg of "4444" will make your Server listen on 4444.
 *      
 *  Your server will only need to handle one client at a time.  If the 
 *  connected client disconnects, your server should go back to listening for
 *  future clients to connect to.
 *  
 *  The client messages that come in will indicate the value that is being
 *  factored and the range of values this server will be processing over.  
 *  Your server will take this in and message back all factors for our value.
 */
public class PrimeFactorsServer {

    /**
     * Certainty variable for BigInteger isProbablePrime() function.
     */
    private final static int PRIME_CERTAINTY = 10;

    /**
     * @param args String array containing Program arguments.  It should only
     *             contain one String indicating the port it should connect to.
     *             Defaults to port 4444 if no Program argument is present.
     */

    private int port;

    public PrimeFactorsServer(int port) {
        this.port = port;
        while (true) {
            try (
                    ServerSocket serverSocket =
                            new ServerSocket(port);
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out =
                            new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));
            )
        }

    public List<String> parseInput(String input) {
        Arrays.asList(input.split("\\s+"));
    }

    public BigInteger getN(List<String> input) {
        return new BigInteger(input.get(1));
    }

    public BigInteger getLow(List<String> input) {
        return new BigInteger(input.get(2));
    }

    public BigInteger getHigh(List<String> input) {
        return new BigInteger(input.get(3));
    }


    public void factor (int port) {
        while (true) {
            try (
                    ServerSocket serverSocket =
                            new ServerSocket(port);
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out =
                            new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));
            ) {
                String inputLine = in.readLine();
                List<String> inputList = Arrays.asList(inputLine.split("\\s+"));
                BigInteger N = new BigInteger(inputList.get(1));
                if (!N.equals(BigInteger.ONE)) {
                    BigInteger low = new BigInteger(inputList.get(2));
                    BigInteger high = new BigInteger(inputList.get(3));
                    List<BigInteger> primes = PrimeFactorFind.FindFactor(N, low, high, PRIME_CERTAINTY);
                    for (BigInteger prime : primes) {
                        out.println("found " + N.toString() + " " + prime.toString());
                    }
                } else {
                    out.println("invalid");
                }
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port "
                        + port + " or listening for a connection");
                System.out.println(e.getMessage());
            }
        }
    }

    public int getPort() {
        return port;
    }
}

