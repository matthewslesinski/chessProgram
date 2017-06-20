package support;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import boardFeatures.DownRightDiagonal;
import boardFeatures.Line;
import boardFeatures.Rank;
import boardFeatures.Square;
import boardFeatures.UpRightDiagonal;

/**
 * This class provides static utility functions
 * @author matthewslesinski
 *
 */
public class UtilityFunctions {

	
	/**
	 * Returns an array-backed list containing the integers between
	 * {@code start} (inclusive) and {@code end} (exclusive)
	 * @param start The start of the range
	 * @param end The first number after the end of the range
	 * @return The list of all the integers between
	 */
	public static List<Integer> getRange(int start, int end) {
		Integer[] arrayOfInts = new Integer[end - start];
		for (int i = start; i < end; i++) {
			arrayOfInts[i - start] = i;
		}
		return Arrays.asList(arrayOfInts);
	}
	
	/**
	 * Returns a positive, zero, or negative number to represent the sign of the given number
	 * @param value The number
	 * @return -1, 0, or 1
	 */
	public static int getSign(int value) {
		return value / Math.abs(value);
	}
	
	/**
	 * Returns a new list (backed by an {@code ArrayList}) that contains a reverse-order shallow
	 * copy of the given list
	 * @param list The list to reverse
	 * @return The reverse order list
	 */
	public static <T> List<T> reverseList(List<T> list) {
		List<T> newList = new ArrayList<>(list);
		Collections.reverse(newList);
		return newList;
	}
	
	/**
	 * Determines which member field to retrieve when trying to get the {@code Line} containing the {@code square} that
	 * is of type {@code type}
	 * @param square The square in the line
	 * @param type The type of line
	 * @return The instance of the type of line
	 */
	public static Line getLineBySquareAndClass(Square square, Class<? extends Line> type) {
		if (type.equals(File.class)) {
			return square.getFile();
		} else if (type.equals(Rank.class)) {
			return square.getRank();
		} else if (type.equals(UpRightDiagonal.class)) {
			return square.getUpRightDiagonal();
		} else if (type.equals(DownRightDiagonal.class)) {
			return square.getDownRightDiagonal();
		} else {
			throw new BadArgumentException(square, type, "There should not be a fifth Line class");
		}
	}
	
}
