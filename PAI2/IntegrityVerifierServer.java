package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.net.ServerSocketFactory;

/**
 * Defines a server that receives messages and checks their integrity.
 */
public class IntegrityVerifierServer {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Key used to generate the MACs.
	 */
	private String key;
	/**
	 * Algorithm used to generate the MACs.
	 */
	private MessageDigest algorithm;
	/**
	 * Random number generator used to generate the nonces.
	 */
	private Random randomGenerator;
	/**
	 * Set containing all already generated nonces, used to guarantee the uniqueness
	 * of the nonces.
	 */
	private Set<Long> generatedNonces;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a server.
	 * 
	 * @throws IOException
	 */
	public IntegrityVerifierServer(String key, MessageDigest algorithm) throws IOException {
		this.key = key;
		this.algorithm = algorithm;
		try {
			this.randomGenerator = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		this.generatedNonces = new HashSet<Long>();
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Generates a nonce with the guarantee that it hasn't been generated before.
	 */
	private String generateNonce() {
		Long nonce;
		do {
			nonce = this.randomGenerator.nextLong();
		} while (this.generatedNonces.contains(nonce));
		return Util.fromLong(nonce);
	}

	/**
	 * Attempts to get a client's message.
	 */
	public void getMessage() {
		// ServerSocket
		ServerSocket serverSocket = null;
		// Socket to communicate with the client
		Socket socket = null;
		// BufferedReader to read from the client
		BufferedReader input = null;
		// PrintWriter to send data to the client
		PrintWriter output = null;
		try {
			ServerSocketFactory serverSocketFactory = (ServerSocketFactory) ServerSocketFactory.getDefault();
			// Create a ServerSocket listening at port 7070
			serverSocket = (ServerSocket) serverSocketFactory.createServerSocket(7070);
			System.err.println("Waiting for connections...");
			socket = (Socket) serverSocket.accept();
			// Open a BufferedReader to read from the client
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// Open a PrintWriter to send data to the client
			output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			// Calculate nonce
			String nonce = this.generateNonce();
			output.write(nonce + "\n");
			output.flush();
			// Read client's message and its MAC
			String message = input.readLine();
			// Compute MAC of the message, calculated as the hash of the combination of the
			// message, the key and the nonce
			String computedMessageMAC = Util.fromByteArray(algorithm.digest((message + key + nonce).getBytes()));
			// MAC
			String messageMAC = input.readLine();
			if (messageMAC.equals(computedMessageMAC)) {
				output.write("Message sent with integrity.\n");
			} else {
				output.write("Message sent with NO integrity.\n");
			}
			output.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				input.close();
				output.close();
				socket.close();
				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
