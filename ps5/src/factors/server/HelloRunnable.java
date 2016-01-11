//package factors.server;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.net.UnknownHostException;
//
//public class HelloRunnable implements Runnable {
//
//    int counter = 0;
//    int portNumber;
//
//    HelloRunnable(int portNumber) {
//        this.portNumber = portNumber;
//    }
//
//    public void run() {
//
//        String hostName = "localhost";
//
//        try (
//                Socket echoSocket = new Socket(hostName, portNumber);
//                PrintWriter out =
//                        new PrintWriter(echoSocket.getOutputStream(), true);
//                BufferedReader in =
//                        new BufferedReader(
//                                new InputStreamReader(echoSocket.getInputStream()));
//                BufferedReader stdIn =
//                        new BufferedReader(
//                                new InputStreamReader(System.in))
//        ) {
//            String userInput stdIn.readLine();
//            while ((userInput = stdIn.readLine()) != null) {
//                out.println(userInput);
//                System.out.println(in.readLine());
//            }
//        } catch (UnknownHostException e) {
//            System.err.println("Don't know about host " + hostName);
//            System.exit(1);
//        } catch (IOException e) {
//            System.err.println("Couldn't get I/O for the connection to " +
//                    hostName);
//            System.exit(1);
//        }
//
//    }
//
//    public static void main(String args[]) {
//        (new Thread(new HelloRunnable(4444))).start();
//        (new Thread(new HelloRunnable(5555))).start();
//    }
//
//}