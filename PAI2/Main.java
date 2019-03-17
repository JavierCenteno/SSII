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
		// Get key and algorithm
		String key = null;
		String algorithmName = null;
		MessageDigest algorithm = null;
		try {
			key = JOptionPane.showInputDialog(null, "Enter key:");
			algorithmName = JOptionPane.showInputDialog(null, "Enter the name of the algorithm:");
			algorithm = MessageDigest.getInstance(algorithmName);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// Initialize server and client
		IntegrityVerifierServer server = new IntegrityVerifierServer(key, algorithm);
		IntegrityVerifierClient client = new IntegrityVerifierClient(key, algorithm);
		Thread serverThread = new Thread(() -> server.run());
		Thread clientThread = new Thread(() -> client.run());
		serverThread.start();
		clientThread.start();
	}

}
