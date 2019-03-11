package src;

/**
 * Driver class for the program.
 */
public class Main {

	public static void main(String[] args) {
		Thread serverThread = new Thread(IntegrityVerifierServer::run);
		Thread clientThread = new Thread(IntegrityVerifierClient::run);
		serverThread.start();
		clientThread.start();
	}

}
