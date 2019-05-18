package src;

/**
 * Class that holds utility methods.
 */
public class Util {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Alphabet of hexadecimal characters to use when converting between integers
	 * and hexadecimal strings.
	 */
	private static final String HEXADECIMAL_DIGITS = "0123456789ABCDEF";

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Prevent this class from being instanced.
	 */
	private Util() {
	}

	////////////////////////////////////////////////////////////////////////////////
	// Byte conversion methods

	/**
	 * Converts an array of bytes to an hexadecimal string.
	 */
	public static String byteArrayToString(byte[] bytes) {
		StringBuilder stringBuilder = new StringBuilder(bytes.length * 2);
		for (int index = 0; index < bytes.length; ++index) {
			stringBuilder.append(HEXADECIMAL_DIGITS.charAt((bytes[index] & 0xF0) >>> 4));
			stringBuilder.append(HEXADECIMAL_DIGITS.charAt((bytes[index] & 0x0F)));
		}
		return stringBuilder.toString();
	}

	/**
	 * Converts an hexadecimal string to an array of bytes.
	 */
	public static byte[] stringToByteArray(String string) {
		string = string.replace('a', 'A');
		string = string.replace('b', 'B');
		string = string.replace('c', 'C');
		string = string.replace('d', 'D');
		string = string.replace('e', 'E');
		string = string.replace('f', 'F');
		byte[] bytes = new byte[string.length() / 2];
		for (int i = 0; i < string.length(); i += 2) {
			bytes[i / 2] = (byte) ((HEXADECIMAL_DIGITS.indexOf(string.charAt(i)) << 4)
					+ HEXADECIMAL_DIGITS.indexOf(string.charAt(i + 1)));
		}
		return bytes;
	}

}
