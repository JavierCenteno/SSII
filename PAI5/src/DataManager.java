package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;

public class DataManager {

	private static Properties ORDERS;
	private static String ORDERS_PATH;
	private static String KEYS_PATH;

	static {
		ORDERS = loadProperties(ORDERS_PATH);
		ORDERS_PATH = "data";
		KEYS_PATH = "keys";
	}

	/**
	 * Loads the properties file at the given path.
	 *
	 * @param path The path to a file represented as a sequence of the different
	 *             elements of the path.
	 */
	private static Properties loadProperties(final String... path) {
		final String joinedPath = String.join(File.separator, path);
		try (final FileInputStream inputStream = new FileInputStream(joinedPath)) {
			final Properties properties = new Properties();
			properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			return properties;
		} catch (final IOException exception) {
			throw new IllegalArgumentException(exception);
		}
	}

	/**
	 * Saves the properties file at the given path.
	 *
	 * @param properties The properties to be saved.
	 * @param path       The path to a file represented as a sequence of the
	 *                   different elements of the path.
	 */
	private static void saveProperties(final Properties properties, final String... path) {
		final String joinedPath = String.join(File.separator, path);
		try (final FileOutputStream outputStream = new FileOutputStream(joinedPath)) {
			final OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			properties.store(writer, "");
		} catch (final IOException exception) {
			throw new IllegalArgumentException(exception);
		}
	}

	public static String getOrdersOfEmployee(String employee) {
		return ORDERS.getProperty(employee);
	}

	public static void addOrderToEmployee(String employee, String order) {
		ORDERS.setProperty(employee, ORDERS.getProperty(employee) + ";" + order);
		saveProperties(ORDERS, ORDERS_PATH + File.separator + "orders.properties");
	}

	public static PublicKey getPublicKeyOfEmployee(String employee) {
		try {
			return KeyUtil.loadPublicKey(KEYS_PATH + File.separator + employee + ".public.key");
		} catch (InvalidKeySpecException | IOException exception) {
			exception.printStackTrace();
			throw new IllegalArgumentException(exception);
		}
	}

}
