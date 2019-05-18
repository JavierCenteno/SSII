package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.security.Signature;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.net.ServerSocketFactory;

/**
 * Defines a server that receives messages and checks their integrity.
 */
public class Server {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	private Map<String, Integer> wrongOrders;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a server.
	 */
	public Server() {
		wrongOrders = new HashMap<String, Integer>();
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Starts the server.
	 */
	public void start() {
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
				if(wrongOrders.get(receivedMessage) > 2) {
					log("Message rejected");
					output.write("Due to repeated incorrect messages being received, we can't allow you to send that order anymore.");
				} else {
					String receivedEmployee = input.readLine();
					log("Received employee: \"" + receivedEmployee + "\"");
					String receivedSignature = input.readLine();
					log("Received signature: \"" + receivedSignature + "\"");
					boolean verified = verify(DataManager.getPublicKeyOfEmployee(receivedEmployee), receivedMessage.getBytes(), stringToByteArray(receivedSignature));
					if(verified) {
						DataManager.addOrderToEmployee(receivedEmployee, receivedMessage);
						log("Message verified correctly");
						output.write("Your order has been processed correctly.");
					} else {
						Integer numberOfWrongOrders = wrongOrders.get(receivedMessage);
						if(numberOfWrongOrders == null) {
							wrongOrders.put(receivedMessage, 1);
						} else {
							wrongOrders.put(receivedMessage, wrongOrders.get(receivedMessage) + 1);
						}
						log("Message couldn't be verified.");
						output.write("Your identity couldn't be confirmed.");
					}
				}
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

	public static byte[] stringToByteArray(String string) {
		byte[] bytes = new byte[string.length() / 2];
		for (int i = 0; i < string.length(); i += 2) {
			bytes[i / 2] = (byte) ((Character.digit(string.charAt(i), 16) << 4) + Character.digit(string.charAt(i + 1), 16));
		}
		return bytes;
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
