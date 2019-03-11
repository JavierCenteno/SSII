package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

/**
 * Defines a server that receives messages and checks their integrity.
 */
public class IntegrityVerifierServer {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	private ServerSocket serverSocket;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a server.
	 */
	public IntegrityVerifierServer() throws Exception {
		// ServerSocketFactory to build theServerSockets
		ServerSocketFactory socketFactory = (ServerSocketFactory) ServerSocketFactory.getDefault();
		// Creation of a ServerSocket listining at 7070 port
		this.serverSocket = (ServerSocket) socketFactory.createServerSocket(7070);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Instances a server and runs it.
	 */
	public static void main(String[] args) {
		IntegrityVerifierServer server = null;
		try {
			server = new IntegrityVerifierServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		server.runServer();
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Execution of the server to listen the clients.
	 */
	private void runServer() {
		while (true) {
			// Hold on the request of the clients to check messages and MACs
			try {
				System.err.println("Waiting clients connections...");
				Socket socket = (Socket) serverSocket.accept();
				// Open a BufferedReader to read from the client
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				// Open a PrintWriter to send data to the clients
				PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				// Read client's message and its MAC
				String message = input.readLine();
				// Compute MAC of the message
				String computedMessageMAC = "";// TODO: compute MAC
				String sentMessageMAC = input.readLine();
				if (sentMessageMAC.equals(computedMessageMAC)) {
					output.println("Message sent with integrity");
				} else {
					output.println("Message sent with NO integrity.");
				}
				output.close();
				input.close();
				socket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

}
