package src;

/**
 * Class that holds utility methods.
 */
public class Util {

	public static String fromByteArray(byte[] bytes) {
		StringBuilder stringBuilder = new StringBuilder(bytes.length * 2);
		for (int index = 0; index < bytes.length; ++index) {
			stringBuilder.append(Integer.toHexString(((int) bytes[index]) & 0xFF));
		}
		return stringBuilder.toString();
	}

}
