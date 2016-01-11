package factors.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
    public static void main(String[] args) {
        // get port number
        int portNumber = 4444;
        if (args.length == 0) {
            portNumber = portNumber;
        } else if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]);
        } else {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }
        while (true) {
            try (
                    ServerSocket serverSocket =
                            new ServerSocket(portNumber);
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
                        + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
            }
        }
    }
}

