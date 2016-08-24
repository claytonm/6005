package echo.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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

    private static final int ECHO_PORT = 4444;
    ServerSocket serverSocket;

    public EchoServer (int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void sendReply(PrintWriter out, String request) throws IOException {
        out.println(">>> " + request);
        out.flush();
    }

    public void handle(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        try {
            String request = in.readLine();
            while (request != null) {
                sendReply(out, request);
                request = in.readLine();
            }
        } finally {
            out.close();
            in.close();
        }
    }

    private void serve() throws IOException {
        while (true) {
            // blocks until a client connects
            Socket clientSocket = serverSocket.accept();
            try {
                handle(clientSocket);
            } catch (IOException ioe) {
                ioe.printStackTrace();	// but don't terminate serve()
            } finally {
                clientSocket.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.err.println("Usage: java EchoServer [port], where port is an optional\n"
                    + " numerical argument within range 0-65535.");
            System.exit(1);
        }
        int port = 0;
        if (args.length == 0) {
            port = ECHO_PORT;
        } else if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {
                // complain about ill-formatted argument
                System.err.println("EchoServer: illegal port, " + args[0]);
                nfe.printStackTrace();
            }
        }
        try {
            EchoServer server = new EchoServer(port);
            server.serve();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
