package src;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JOptionPane;

/**
 * Driver class for the program.
 */
public class Main {

	public static void main(String[] args) throws IOException {
		try {
			String key = JOptionPane.showInputDialog(null, "Enter key:");
			String algorithmName = JOptionPane.showInputDialog(null, "Enter the name of the algorithm:");
			MessageDigest algorithm = MessageDigest.getInstance(algorithmName);
			String message = JOptionPane.showInputDialog(null, "Enter message:");
			// Initialize server and client
			IntegrityVerifierServer server = new IntegrityVerifierServer(key, algorithm);
			IntegrityVerifierClient client = new IntegrityVerifierClient(key, algorithm);
			Thread clientThread = new Thread(() -> client.sendMessage(message));
			Thread serverThread = new Thread(() -> server.getMessage());
			clientThread.start();
			serverThread.start();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

}
