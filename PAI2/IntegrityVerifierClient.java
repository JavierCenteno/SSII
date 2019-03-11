import java.io.*;
import javax.swing.*;
import javax.net.*;

public class IntegrityVerifierClient {

	////////////////////////////////////////////////////////////////////////////////
	// Instance initialiers

	/**
	 * Constructs a client and opens a connection.
	 */
	public IntegrityVerifierClient() {
		try {
			SocketFactory socketFactory = (SocketFactory) SocketFactory.getDefault();
			Socket socket = (Socket) socketFactory.createSocket("localhost", 7070 );
			// Create a PrintWriter to sent messages and MACs to the server
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			String userName = JOptionPane.showInputDialog(null,"Introduzca su mensaje:");
			// Send message to the server
			output.println(mensaje);
			// Calculate the MAC with shared key
			output.println(macdelMensaje);
			// Flush operations to send messages correctly
			output.flush();
			// Create a BufferedReader to read the response to the server
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// Read response from the server
			String respuesta = input.readLine();
			// Show the response in the client
			JOptionPane.showMessageDialog(null, respuesta);
			// Closing connections
			output.close();
			input.close();
			socket.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Instances a client.
	 */
	public static void main(String[] args) {
		new IntegrityVerifierClient();
	}

}
