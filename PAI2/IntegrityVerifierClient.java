package src;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;

import javax.swing.*;
import javax.net.*;

public class IntegrityVerifierClient {

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

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a client and opens a connection.
	 */
	public IntegrityVerifierClient(String key, MessageDigest algorithm) {
		this.key = key;
		this.algorithm = algorithm;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Runs this client.
	 */
	public void run() {
		try {
			SocketFactory socketFactory = (SocketFactory) SocketFactory.getDefault();
			Socket socket = (Socket) socketFactory.createSocket("localhost", 7070);
			// Create a PrintWriter to sent messages and MACs to the server
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			String message = JOptionPane.showInputDialog(null, "Enter message:");
			// Send message to the server
			output.println(message);
			// Calculate the MAC with shared key
			String messageMAC = "";// TODO: calculate MAC
			output.println(messageMAC);
			// Flush operations to send messages correctly
			output.flush();
			// Create a BufferedReader to read the response to the server
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// Read response from the server
			String response = input.readLine();
			// Show the response in the client
			JOptionPane.showMessageDialog(null, response);
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

}
