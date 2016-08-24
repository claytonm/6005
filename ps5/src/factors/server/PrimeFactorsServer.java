package factors.server;


import factors.factor.PrimeFactor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

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

    /** Certainty variable for BigInteger isProbablePrime() function. */
    private final static int PRIME_CERTAINTY = 10;
    private int port;

    /**
     * @param args String array containing Program arguments.  It should only 
     *      contain one String indicating the port it should connect to.
     *      Defaults to port 4444 if no Program argument is present.
     */

    ServerSocket serverSocket;

    public PrimeFactorsServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.port = port;
    }

    public int getPort() {return port;}

    public static PrimeFactorsServer[] PrimeFactorsServers(int[] ports) throws IOException {
        int numServers = ports.length;
        PrimeFactorsServer[] servers = new PrimeFactorsServer[numServers];
        for (int i = 0; i < numServers; i++) {
            servers[i] = new PrimeFactorsServer(ports[i]);
        }
        return servers;
    }

    public static int[] getPorts(String[] args) {
        int[] ports = new int[args.length];
        // convert each portString to an integer
        for (int i = 0; i < ports.length; i++) {
            ports[i] = Integer.parseInt(args[i]);
        }
        return ports;
    }

    /**
     * @param out
     * @param request
     * Parses request into N, hi, and low using parseRequest
     * Passes N, hi, and low to PrimeFactor and saves results to factors
     * Prints each factor in factors array to out in the form "found N factor"
     */

    public void sendFactors(PrintWriter out, String request) {
        BigInteger[] args = parseRequest(request);
        BigInteger N = args[0];

        BigInteger low = args[1];
        BigInteger high = args[2];

        BigInteger[] factors = PrimeFactor.findPrimeFactors(N, low, high, PRIME_CERTAINTY);

        for (BigInteger factor : factors) {
            out.print(formatReply(N, factor) + "\n");
            out.flush();
        }
        out.print("Done" + "\n");
        out.flush();
    }

    /**
     * @param request
     * @return BigInteger[]
     * request is of form factor N hi low
     * return is array of BigInteger[N, hi, low]
     */

    public BigInteger[] parseRequest(String request) {
        BigInteger[] bigInts = new BigInteger[3];
        String[] args = request.split("\\s+");
        for (int i = 1; i < args.length; i++) {
            bigInts[i-1] = new BigInteger(args[i]);
        }
        return bigInts;
    }

    public String formatReply(BigInteger N, BigInteger factor) {
        return "found " + N + " " + factor;
    }


    public void handle(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream());

        try {

            String request = in.readLine();
            while (request != null) {
                sendFactors(out, request);
                request = in.readLine();
            }
        } finally {
            in.close();
            out.close();
        }
    }

    public void serve() throws IOException {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            try {
                handle(clientSocket);
            } finally {
                clientSocket.close();
            }
        }
    }

    public static void runServers(PrimeFactorsServer[] servers) throws IOException {
        for (PrimeFactorsServer server : servers) {
            server.serve();
        }
    }


    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        PrimeFactorsServer server = new PrimeFactorsServer(port);
        server.serve();
    }
}
