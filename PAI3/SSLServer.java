
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * Defines a server that receives messages and checks their integrity.
 */
public class SSLServer {

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a server.
	 */
	public SSLServer() {
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Attempts to get a client's message.
	 */
		public static void main(String[] args) {

		// ServerSocket
		ServerSocket serverSocket = null;
		// Socket to communicate with the client
		Socket socket = null;
		// BufferedReader to read from the client
		BufferedReader input = null;
		// PrintWriter to send data to the client
		PrintWriter output = null;
		try {
			SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			// Create a ServerSocket listening at port 7070
			serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(7070);
			serverSocket.setEnabledCipherSuites(new String[] {"TLS_DHE_RSA_WITH_AES_256_CBC_SHA256"});
			System.err.println("Waiting for connections...");
			socket = serverSocket.accept();
			// Open a BufferedReader to read from the client
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// Open a PrintWriter to send data to the client
			output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			String clientInput=input.readLine();
			output.write("Your secret message has been correctly stored.");
			output.flush();
			System.out.println(clientInput);

			output.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (IllegalArgumentException illegalArgumentException) {
			illegalArgumentException.printStackTrace();
		}finally {
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
