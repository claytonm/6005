package echo.client;

import java.io.IOException;
import java.net.*;
import java.io.*;

/**
 * A simple client that will interact with an EchoServer.
 */
public class EchoClient {

	/**
	 * @param args String array containing Program arguments.  It should only
	 *             contain exactly one String indicating which server to connect to.
	 *             We require that this string be in the form hostname:portnumber.
	 */
	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.err.println(
					"Usage: java EchoClient <server address:port number>");
			System.exit(1);
		} else {
			String[] serverAddress = args[0].split(":");
			if (serverAddress.length != 2) {
				System.err.println(
						"Argument must be of the form <server address:port number>");
				System.exit(1);
			} else {

				String hostName = serverAddress[0];
				int portNumber = Integer.parseInt(serverAddress[1]);

				try (
						Socket echoSocket = new Socket(hostName, portNumber);
						PrintWriter out =
								new PrintWriter(echoSocket.getOutputStream(), true);
						BufferedReader in =
								new BufferedReader(
										new InputStreamReader(echoSocket.getInputStream()));
						BufferedReader stdIn =
								new BufferedReader(
										new InputStreamReader(System.in))
				) {
					String userInput;
					while ((userInput = stdIn.readLine()) != null) {
						out.println(userInput);
						System.out.println(in.readLine());
					}
				} catch (UnknownHostException e) {
					System.err.println("Don't know about host " + hostName);
					System.exit(1);
				} catch (IOException e) {
					System.err.println("Couldn't get I/O for the connection to " +
							hostName);
					System.exit(1);
				}
			}
		}
	}
}

