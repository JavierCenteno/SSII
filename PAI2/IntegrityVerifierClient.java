package src;

import java.io.*;
import java.net.Socket;

import javax.swing.*;
import javax.net.*;

public class IntegrityVerifierClient {

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a client and opens a connection.
	 */
	public IntegrityVerifierClient() {
		try {
			SocketFactory socketFactory = (SocketFactory) SocketFactory.getDefault();
			Socket socket = (Socket) socketFactory.createSocket("localhost", 7070);
			// Create a PrintWriter to sent messages and MACs to the server
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			String mensaje = JOptionPane.showInputDialog(null, "Introduzca su mensaje: ");
			// Send message to the server
			output.println(mensaje);
			// Calculate the MAC with shared key
			String messageMAC = "";// TODO: calculate MAC
			output.println(messageMAC);
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
	public static void run() {
		new IntegrityVerifierClient();
	}

}
