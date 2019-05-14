package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

public class DataManager {

	public static List<String> getOrdersOfEmployee(String employee) {
		// TODO
		return null;
	}

	public static PublicKey getPublicKeyOfEmployee(String employee) {
		// TODO
		return null;
	}

	/**
	 * Reads a public key from an external file.
	 * 
	 * @param filename A given file.
	 * @return The PublicKey object read from the given file.
	 * @throws IOException If an I/O error occurs while reading the file.
	 * @throws InvalidKeySpecException If the key doesn't follow the X.509 specification.
	 */
	public static PublicKey readPublicKey(String filename) throws IOException, InvalidKeySpecException {
		byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
		}
		return keyFactory.generatePublic(keySpec);
	}

}
