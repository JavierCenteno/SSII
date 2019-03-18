package src;

/**
 * Class that holds utility methods.
 */
public class Util {

	/**
	 * Converts an integer in the range [0, 15] to an hexadecimal character.
	 */
	private static char intToHex(int i) {
		if (0 <= i && i <= 9) {
			return (char) (i + '0');
		} else if (10 <= i && i <= 15) {
			return (char) (i - 10 + 'A');
		} else {
			throw new IllegalArgumentException();
		}
	}

	public static String fromByteArray(byte[] bytes) {
		StringBuilder stringBuilder = new StringBuilder(bytes.length * 2);
		for (int index = 0; index < bytes.length; ++index) {
			stringBuilder.append(intToHex(bytes[index] & 0xFF));
			stringBuilder.append(intToHex(bytes[index] >>> 4));
		}
		return stringBuilder.toString();
	}

}
