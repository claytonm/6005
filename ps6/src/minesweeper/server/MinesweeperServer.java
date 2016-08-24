package minesweeper.server;

import minesweeper.Board;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MinesweeperServer {

	private final static int PORT = 4444;
	private ServerSocket serverSocket;
	private static int defaultDim = 10;
	private static Board board;

    /**
     * Make a MinesweeperServer that listens for connections on port.
     * @param port port number, requires 0 <= port <= 65535.
     */
    public MinesweeperServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
		board = new Board(defaultDim);
    }

	public MinesweeperServer(int port, int dim) throws IOException {
		serverSocket = new ServerSocket(port);
		board = new Board(dim);
	}



	public class MultiConnect implements Runnable {

		// this subclass constitutes the solution to Problem 1
		// given a socket, it connects each client that connects
		// to that socket in a new thread; the threads are actually
		// created in the method serve()

		Socket socket;
		public MultiConnect(Socket socket) {
			this.socket = socket;
		}

		Boolean keepRunning = true;

		public void run() {

			try {
				handleConnection(socket, keepRunning);
			} catch (IOException e) {
				e.printStackTrace(); // but don't terminate serve()
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


    /**
     * Run the server, listening for client connections and handling them.
	 *
     * Never returns unless an exception is thrown.
     * @throws IOException if the main server socket is broken
     * (IOExceptions from individual clients do *not* terminate serve()).
     */
    public void serve() throws IOException {
		while (true) {
			// block until a client connects
			Socket socket = serverSocket.accept();

			// handle the client
			(new Thread(new MultiConnect(socket))).start();
		}
	}
    
    /**
     * Handle a single client connection.  Returns when client disconnects.
     * @param socket  socket where client is connected
     * @throws IOException if connection has an error or terminates unexpectedly
     */
    private void handleConnection(Socket socket, Boolean keepRunning) throws IOException {
        
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        try {
        	for (String line = in.readLine(); line != null ; line = in.readLine()) {
        		String output = handleRequest(line);
				if (output == "Bye" || output == "BOOM!") {
					out.println(output);
					break;
				}
        		if(output != null) {
        			out.println(output);
        		}
        	}
        } finally {        
        	out.close();
        	in.close();
        }
    }

	/**
	 * handler for client input
	 * 
	 * make requested mutations on game state if applicable, then return appropriate message to the user
	 * 
	 * @param input
	 * @return
	 */
	private static String handleRequest(String input) {

		String regex = "(look)|(dig \\d+ \\d+)|(flag \\d+ \\d+)|(deflag \\d+ \\d+)|(help)|(bye)";
		if(!input.matches(regex)) {
			//invalid input
			return null;
		}
		String[] tokens = input.split(" ");
		String outString = "";
		if(tokens[0].equals("look")) {
			// 'look' request
			return board.toString();
		} else if(tokens[0].equals("help")) {
			// 'help' request
			String help = "the command 'look' returns the board\n" +
					"the command 'dig x y' digs in square x y\n" +
					"'flag x y' will flag square x y\n" +
					"'unlag x y' will remove the flag at square x y\n" +
					"bye terminates the session\n";
			return help;
		} else if(tokens[0].equals("bye")) {
			// 'bye' request
			return "Bye";
		} else {
			int x = Integer.parseInt(tokens[1]);
			int y = Integer.parseInt(tokens[2]);
			if(tokens[0].equals("dig")) {
				// 'dig x y' request
				outString = board.dig(x, y);
				if (outString == "BOOM!") {
					return "BOOM!";
				} else {
					return board.toString();
				}
			} else if(tokens[0].equals("flag")) {
				// 'flag x y' request
				board.flag(x, y);
				return board.toString();
			} else if(tokens[0].equals("deflag")) {
				// 'deflag x y' request
				board.unflag(x, y);
				return board.toString();
			}
		}
		//should never get here

		return "";
	}
    
    /**
     * Start a MinesweeperServer running on the default port.
     */
    public static void main(String[] args) {
		String debug_string = args[0];
		Boolean debug = false;
		if (debug_string == "true") {
			debug = true;
		}
		Boolean size_flag = true;
		int size = defaultDim;
		if (args[1] == "-s") {
			size_flag = true;
			size = Integer.parseInt(args[2]);
		} else {
			Path path = Paths.get(args[1]);
		}

        try {
			MinesweeperServer server;
			if (size_flag) {
				server = new MinesweeperServer(PORT, size);
			} else {
				server = new MinesweeperServer(PORT);
			}
            server.serve();
        }
			catch (IOException e) {
            e.printStackTrace();
        }
    }
}