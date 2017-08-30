package hashing;

import java.util.Random;

import support.UtilityFunctions;

/**
 * Generates random longs by using Java's {@code Random} class to randomly set each bit of the longs
 * @author matthewslesinski
 *
 */
public class PseudoRandomNumbers implements RandomNumberGenerator {

	private static final Random generator = new Random();
	
	/**
	 * Generates the next random long to populate the sequence with
	 * @return The next long
	 */
	private static long getNextLong() {
		long toReturn = 0;
		for (int i : UtilityFunctions.getPrimitiveRange(0, 64)) {
			long mask = generator.nextBoolean() ? 1L << i : 0L;
			toReturn |= mask;
		}
		return toReturn;
	}
	
	@Override
	public long[] generateNumbers(int length) {
		long[] numberStore = new long[length];
		for (int i : UtilityFunctions.getPrimitiveRange(0, length)) {
			numberStore[i] = getNextLong();
		}
		return numberStore;
	}

}
