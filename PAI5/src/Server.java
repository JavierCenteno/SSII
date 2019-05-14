package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.time.LocalDateTime;

import javax.net.ServerSocketFactory;

/**
 * Defines a server that receives messages and checks their integrity.
 */
public class Server {

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a server.
	 */
	public Server() {
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Attempts to get a client's message.
	 */
	public static void main(String[] args) {
		// Server socket
		ServerSocket serverSocket = null;
		// Socket to communicate with the client
		Socket socket = null;
		// BufferedReader to read from the client
		BufferedReader input = null;
		// PrintWriter to send data to the client
		PrintWriter output = null;
		try {
			serverSocket = ServerSocketFactory.getDefault().createServerSocket(7070);
			while (true) {
				log("Waiting for connections...");
				socket = serverSocket.accept();
				log("Connection accepted from " + socket.getRemoteSocketAddress().toString());
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				String receivedMessage = input.readLine();
				log("Received message: \"" + receivedMessage + "\"");
				output.write("Your secret message has been correctly stored.");
				output.flush();
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (IllegalArgumentException illegalArgumentException) {
			illegalArgumentException.printStackTrace();
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

	public static boolean verify(PublicKey key, byte[] data, byte[] signature) {
		Signature s = null;
		try {
			s = Signature.getInstance("SHA1withDSA");
			s.initVerify(key);
			s.update(data);
			return s.verify(signature);
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new IllegalArgumentException(exception);
		}
	}

	public static void log(String message) {
		System.err.println("[INFO " + LocalDateTime.now().toString() + "] " + message);
	}

}
