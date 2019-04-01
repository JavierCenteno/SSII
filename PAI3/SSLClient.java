import java.io.*;
import java.net.Socket;
import javax.swing.JOptionPane;

import javax.net.ssl.SSLSocketFactory;

public class SSLClient {

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a client.
	 */
	public SSLClient() {
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Attempts to send a message to the server.
	 */
 	public static void main(String[] args) {

		// Socket to communicate with the server
		Socket socket = null;
		// BufferedReader to read from the server
		BufferedReader input = null;
		// PrintWriter to send data to the server
		PrintWriter output = null;
		try {
			SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			socket = (Socket) socketFactory.createSocket("localhost", 7070);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			String user = JOptionPane.showInputDialog(null, "Enter user:");
			String password = JOptionPane.showInputDialog(null, "Enter password:");
			String message = JOptionPane.showInputDialog(null, "Enter message:");

			output.write(user+","+password+","+message+"\n");
			output.flush();


		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				input.close();
				output.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
	}

}
