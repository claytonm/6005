package echo.client;

import java.io.*;
import java.net.Socket;

/**
 * A simple client that will interact with an EchoServer.
 */
public class EchoClient {

	/**
	 * @param args String array containing Program arguments.  It should only 
	 *      contain exactly one String indicating which server to connect to.
	 *      We require that this string be in the form hostname:portnumber.
	 */

	BufferedReader in;
	PrintWriter out;
	Socket socket;

	public EchoClient(String host, int port) throws IOException {
		socket = new Socket(host, port);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	public void send(String message) throws IOException {
		out.println(message);
		out.flush();
	}

	public void showReply() throws IOException {
		System.out.println(in.readLine());
	}

	public void request() throws IOException {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String input = stdIn.readLine();
		while (input != null) {
			send(input);
			showReply();
			input = stdIn.readLine();
		}
	}

	public static void main(String[] args) throws IOException {
			int port = Integer.parseInt(args[1]);
			EchoClient client = new EchoClient("localhost", port);
			client.request();
	}
}
