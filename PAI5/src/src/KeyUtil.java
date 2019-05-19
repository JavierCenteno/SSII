package src;

import java.io.*;
import java.security.*;
import java.security.spec.*;

public class KeyUtil {

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Prevent this class from being instanced.
	 */
	private KeyUtil() {
	}

	////////////////////////////////////////////////////////////////////////////////
	// Utility methods

	public static String keyToString(Key key) {
		return Util.byteArrayToString(key.getEncoded());
	}

	////////////////////////////////////////////////////////////////////////////////
	// Save methods

	/**
	 * Saves the given public key at the given path, with the file ending in
	 * ".public.key".
	 */
	public static void savePublicKey(String path, PublicKey publicKey) throws IOException {
		try (final FileOutputStream outputStream = new FileOutputStream(path + ".public.key")) {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
			outputStream.write(x509EncodedKeySpec.getEncoded());
		} catch (final IOException exception) {
			throw new IllegalArgumentException(exception);
		}
	}

	/**
	 * Saves the given private key at the given path, with the file ending in
	 * ".private.key".
	 */
	public static void savePrivateKey(String path, PrivateKey privateKey) throws IOException {
		try (final FileOutputStream outputStream = new FileOutputStream(path + ".private.key")) {
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
			outputStream.write(pkcs8EncodedKeySpec.getEncoded());
		} catch (final IOException exception) {
			throw new IllegalArgumentException(exception);
		}
	}

	/**
	 * Saves the given key pair at the given path, with the files ending in
	 * ".public.key" and ".private.key".
	 */
	public static void saveKeyPair(String path, KeyPair keyPair) throws IOException {
		savePublicKey(path, keyPair.getPublic());
		savePrivateKey(path, keyPair.getPrivate());
	}

	////////////////////////////////////////////////////////////////////////////////
	// Load methods

	/**
	 * Loads the given public key at the given path, with the file ending in
	 * ".public.key".
	 */
	public static PublicKey loadPublicKey(String path)
			throws IOException, InvalidKeySpecException {
		try (final FileInputStream inputStream = new FileInputStream(path + ".public.key")) {
			byte[] encodedPublicKey = inputStream.readAllBytes();
			KeyFactory keyFactory = KeyFactory.getInstance("DSA");
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
			return keyFactory.generatePublic(publicKeySpec);
		} catch (final IOException | NoSuchAlgorithmException exception) {
			throw new IllegalArgumentException(exception);
		}
	}

	/**
	 * Loads the given private key at the given path, with the file ending in
	 * ".private.key".
	 */
	public static PrivateKey loadPrivateKey(String path)
			throws IOException, InvalidKeySpecException {
		try (final FileInputStream inputStream = new FileInputStream(path + ".private.key")) {
			byte[] encodedPrivateKey = inputStream.readAllBytes();
			KeyFactory keyFactory = KeyFactory.getInstance("DSA");
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
			return keyFactory.generatePrivate(privateKeySpec);
		} catch (final IOException | NoSuchAlgorithmException exception) {
			throw new IllegalArgumentException(exception);
		}
	}

	/**
	 * Loads the given key pair at the given path, with the files ending in
	 * ".public.key" and ".private.key".
	 */
	public static KeyPair loadKeyPair(String path)
			throws IOException, InvalidKeySpecException {
		PublicKey publicKey = loadPublicKey(path);
		PrivateKey privateKey = loadPrivateKey(path);
		return new KeyPair(publicKey, privateKey);
	}

}
