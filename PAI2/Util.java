package src;

/**
 * Class that holds utility methods.
 */
public class Util {

	/**
	 * Convert an array of bytes to an hexadecimal string.
	 */
	public static String fromByteArray(byte[] bytes) {
		StringBuilder stringBuilder = new StringBuilder(bytes.length * 2);
		for (int index = 0; index < bytes.length; ++index) {
			stringBuilder.append(Integer.toHexString(((int) bytes[index]) & 0xFF));
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert a long integer to a padded 16 character hexadecimal string.
	 */
	public static String fromLong(long l) {
		return String.format("%016x", l);
	}

}
