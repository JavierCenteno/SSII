package src;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class GenerateKeyPair {

	public static void main(String args[]) {
		try(Scanner scanner = new Scanner(System.in)) {
			System.out.println("Insert path:");
			String path = scanner.nextLine();
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
			keyPairGenerator.initialize(1024);
			KeyPair generatedKeyPair = keyPairGenerator.genKeyPair();
			KeyUtil.saveKeyPair(path, generatedKeyPair);
		} catch (final IOException | NoSuchAlgorithmException exception) {
			throw new IllegalArgumentException(exception);
		}
	}

}
