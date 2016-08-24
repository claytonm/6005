package factors.client;


import util.BigMath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
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
    private static String HOST_NAME = "localhost";
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private int port;
    private static BigInteger[] factors;

    public PrimeFactorsClient(String host, int port) throws IOException {
        this.port = port;
        clientSocket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream());
    }

    public int getPort() {return this.port;}

    public String formatRequest(BigInteger N, BigInteger low, BigInteger high) {
        return "factor " + " " + N + " " + low + " " + high;
    }

    public void send(String message) throws IOException {
        out.println(message);
        out.flush();
    }

    public void showReply() throws IOException {
        String reply = in.readLine();
        while (reply != null) {
            reply = in.readLine();
        }
    }

    public String getFactor(String response) {
        String factor = response.split("\\s+")[2];
        return factor;
    }

    public List<String> getFactors() throws IOException {
        List<String> factors = new ArrayList<String>();
        String factor;
        String reply = in.readLine();
        if (reply == null) {
            // no factors have been calculated, try again
            return getFactors();
        }

        while (reply != null) {
            if (reply.startsWith("Done")) {
                break;
            } else {
                factor = getFactor(reply);
                factors.add(factor);
            }
            reply = in.readLine();
        }
        return factors;

    }

    public void request(BigInteger N, BigInteger low, BigInteger high) throws IOException {
        String requestString = formatRequest(N, low, high);
        send(requestString);
    }

    /**
     *
     * @param host
     * @param ports
     * @return Array of PrimeFactorsClient, one for each port listed in the program input
     * @throws IOException
     */
    public static PrimeFactorsClient[] PrimeFactorsClients(String host, int[] ports) throws IOException {
        int numServers = ports.length;
        PrimeFactorsClient[] clients = new PrimeFactorsClient[numServers];
        for (int i = 0; i < numServers; i++) {
            clients[i] = new PrimeFactorsClient(host, ports[i]);
        }
        return clients;
    }

    /**
     *
     * @param N
     * @param servers
     * @return Array of BigIntegers [2, sqrt(N)/numServers, 2sqrt(N)/numservers, ..., sqrt(N)]
     */
    public static BigInteger[] divideSpace(BigInteger N, int servers) {
        // Array to hold the partition
        BigInteger[] partition = new BigInteger[servers + 1];
        // convert int to BigInteger
        BigInteger numServers = BigInteger.valueOf(servers);
        // calculate hi - low for each server
        BigInteger chunkSize = BigMath.sqrt(N).divide(numServers);
        partition[0] = BigInteger.ONE;
        for (int i = 1; i < partition.length; i++) {
            partition[i] = partition[i-1].add(chunkSize);
        }
        return partition;
    }

    /**
     *
     * @param args: program input
     * @return
     */
    public static int[] getPorts(String[] args) {
        int[] ports = new int[args.length];
        // convert each portString to an integer
        for (int i = 0; i < ports.length; i++) {
            ports[i] = Integer.parseInt(args[i]);
        }
        return ports;
    }

    public static List<String> aggregateFactors(PrimeFactorsClient[] clients) throws IOException {
        List<String> factors = new ArrayList<String>();
        List<String> clientFactors;
        for (PrimeFactorsClient client : clients) {
            clientFactors = client.getFactors();
            for (String factor : clientFactors) {
                factors.add(factor);
            }
        }
        return factors;
    }

    public static void formatOutput(BigInteger N, List<String> factors) {
        StringBuilder output = new StringBuilder();
        for (String factor : factors) {
            output.append(factor + "*");
        }
        output.deleteCharAt(output.length() - 1);
        System.out.println(N + " = " + output.toString());
    }

    public static void runClients(PrimeFactorsClient[] clients) throws IOException {
        int numServers = clients.length;
        // verify that clients were created on requested ports
        for (PrimeFactorsClient client : clients) {System.out.println(client.getPort());}
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String input = stdIn.readLine();
        while (input != null) {
            BigInteger N = new BigInteger(input);
            BigInteger[] partition = divideSpace(N, numServers);

            // print partition boundaries for debugging
            for (BigInteger part : partition) {
            }
            System.out.print("\n");

            BigInteger low;
            BigInteger high;
            for (int i = 0; i < numServers; i++) {
                low = partition[i].add(BigInteger.ONE);
                high = partition[i+1];
                clients[i].request(N, low, high);
            }
            // aggregate and print factors
            List<String> factors = aggregateFactors(clients);
            formatOutput(N, factors);

            input = stdIn.readLine();
        }
    }


    public static void main(String[] args) throws IOException {
        int[] ports = getPorts(args);
        PrimeFactorsClient[] clients = PrimeFactorsClients(HOST_NAME, ports);
        runClients(clients);
    }
}
