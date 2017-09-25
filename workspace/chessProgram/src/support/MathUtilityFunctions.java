package support;


public class MathUtilityFunctions {

	/**
	 * Returns an array-backed list containing the integers between
	 * {@code start} (inclusive) and {@code end} (exclusive)
	 * @param start The start of the range
	 * @param end The first number after the end of the range
	 * @return The list of all the integers between
	 */
	public static int[] getPrimitiveRange(int start, int end) {
		int[] arrayOfInts = new int[end - start];
		for (int i = start; i < end; i++) {
			arrayOfInts[i - start] = i;
		}
		return arrayOfInts;
	}
	
	/**
	 * Returns an array-backed list containing the integers between
	 * {@code start} (inclusive) and {@code end} (exclusive)
	 * @param start The start of the range
	 * @param end The first number after the end of the range
	 * @return The list of all the integers between
	 */
	public static Integer[] getRange(int start, int end) {
		Integer[] arrayOfInts = new Integer[end - start];
		for (int i = start; i < end; i++) {
			arrayOfInts[i - start] = i;
		}
		return arrayOfInts;
	}
	
	/**
	 * Returns a positive, zero, or negative number to represent the sign of the given number
	 * @param value The number
	 * @return -1, 0, or 1
	 */
	public static int getSign(int value) {
		return value == 0 ? 0 : value / Math.abs(value);
	}
	
	/**
	 * Determines if the number is one of the perfect squares less than or equal to 64
	 * @param number The number to check
	 * @return true iff it's a perfect square
	 */
	public static boolean isPerfectSquareUpTo64(int number) {
		switch (number) {
		case 0:
		case 1:
		case 4:
		case 9:
		case 16:
		case 25:
		case 36:
		case 49:
		case 64:
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * Convenience method for XORing two numbers
	 * @param a The first number
	 * @param b The second number
	 * @return The two numbers XORed together
	 */
	public static long xor(long a, long b) {
		return a ^ b;
	}	
}
