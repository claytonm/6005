package echo.server;

import java.io.IOException;
import java.net.*;
import java.io.*;

/**
 * A simple server that will echo client inputs.
 */
public class EchoServer {

    /**
     * @param args String array containing Program arguments.  It should only
     *      contain at most one String indicating the port it should connect to.
     *      The String should be parseable into an int.
     *      If no arguments, we default to port 4444.
     */

	public static void main(String[] args) throws IOException {

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
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    out.println(">>> " + inputLine);
                }
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port "
                        + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
            }
        }
    }
}
