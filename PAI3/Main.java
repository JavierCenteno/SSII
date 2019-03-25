package src;

import javax.swing.JOptionPane;

/**
 * Driver class for the program.
 */
public class Main {

	/**
	 * Main method.
	 * 
	 * @param args Console arguments
	 */
	public static void main(String[] args) {
		String message = JOptionPane.showInputDialog(null, "Enter message:");
		// Initialize server and client
		SSLServer server = new SSLServer();
		SSLClient client = new SSLClient();
		Thread serverThread = new Thread(() -> server.getMessage());
		Thread clientThread = new Thread(() -> client.sendMessage(message));
		serverThread.start();
		clientThread.start();
	}

}
