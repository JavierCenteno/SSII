package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;

import javax.net.ServerSocketFactory;

/**
 * Defines a server that receives messages and checks their integrity.
 */
public class IntegrityVerifierServer {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Socket used by the server.
	 */
	private ServerSocket serverSocket;
	/**
	 * Key used to generate the MACs.
	 */
	private String key;
	/**
	 * Algorithm used to generate the MACs.
	 */
	private MessageDigest algorithm;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a server.
	 * 
	 * @throws IOException
	 */
	public IntegrityVerifierServer(String key, MessageDigest algorithm) throws IOException {
		// ServerSocketFactory to build theServerSockets
		ServerSocketFactory socketFactory = (ServerSocketFactory) ServerSocketFactory.getDefault();
		// Creation of a ServerSocket listining at 7070 port
		this.serverSocket = (ServerSocket) socketFactory.createServerSocket(7070);
		this.key = key;
		this.algorithm = algorithm;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Runs this server.
	 */
	public void run() {
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
