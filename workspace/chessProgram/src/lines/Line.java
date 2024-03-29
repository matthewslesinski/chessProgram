package lines;

import java.util.List;

import boardFeatures.Square;
import support.BadArgumentException;

/**
 * Used to represent a straight line on the board, so every instance of a line could potentially contain the two squares in a queen's move.
 * It would be really nice if this could be an abstract class, so that its static methods could be inherited and so that some other things could
 * be specified, but it needs to be an interface since enums already extend the {@code Enum} class implicitly.
 * @author matthewslesinski
 *
 */
public interface Line {

	/**
	 * Returns the index value for this line, in whatever order is imposed on the possible lines of this type
	 * @return The index
	 */
	public int getIndex();
	
	/**
	 * Returns the letter of this file, as a string
	 * @return the string containing the file letter
	 */
	public String getHumanReadableForm();
	
	/**
	 * Gets the type of line this is
	 * @return The {@code LineType}
	 */
	public LineType getType();
	
	/**
	 * Gets the length of this line
	 * @return The length
	 */
	public default int getLength() {
		return getContainedSquares().size();
	}
	
	/**
	 * Checks that an index is contained in this line
	 * @param index The index to check
	 * @return If it's in the bounds of the line
	 */
	public default boolean indexInBounds(int index) {
		return index >= 0 && index < getLength();
	}
	
	/**
	 * Retrieves the square at the beginning of the line
	 * @return The {@code Square}
	 */
	public default Square getStartSquare() {
		return getContainedSquares().get(0);
	}

	/**
	 * Returns a list of the squares in this line
	 * @return The {@code List} of {@code Square}
	 */
	public List<Square> getContainedSquares();
	
	/**
	 * Returns a list of the squares in this line in reverse order
	 * @return The {@code List} of {@code Square}
	 */
	public List<Square> getReverseContainedSquares();
	
	/**
	 * Checks if the line contains the square
	 * @param square The square to check
	 * @return true iff it's in the line
	 */
	public boolean containsSquare(Square square);
	
	/**
	 * Determines how far down the line this square is
	 * @param square The square to check
	 * @return its index in the line
	 */
	public default int getSpotInLine(Square square) {
		return Square.getManhattanDistance(square, getStartSquare()) / getForwardDirection().getSuccessiveManhattanDistanceDelta();
	}
	
	/**
	 * Determines how far down the reversed order of the line this square is
	 * @param square The square to check
	 * @return its index in the reversed line
	 */
	public default int getSpotInReverseLine(Square square) {
		return getLength() - getSpotInLine(square) - 1;
	}
	
	/**
	 * Gets the direction this line goes in
	 * @return the {@code Direction}
	 */
	public default Direction getForwardDirection() {
		return getType().getForwardDirection();
	}
	
	/**
	 * Returns a view to the squares ahead of the provided one in this line
	 * @param square The square from which to look ahead
	 * @return A {@code List} of {@code Square}, which is backed by the contained squares in this line
	 */
	public default List<Square> getSquaresInFront(Square square) {
		return getContainedSquares().subList(getSpotInLine(square) + 1, getLength());
	}
	
	/**
	 * Returns a view to the squares behind the provided one in this line
	 * @param square The square from which to look behind
	 * @return A {@code List} of {@code Square}, which is backed by the contained squares in this line
	 */
	public default List<Square> getSquaresBehind(Square square) {
		return getReverseContainedSquares().subList(getSpotInReverseLine(square) + 1, getLength());
	}
	
	/**
	 * Returns the square that is {@code index} into the line, where the first square is at index 0
	 * @param index The index to get
	 * @return The {@code Square at that index}
	 */
	public default Square getSquare(int index) {
		return indexInBounds(index) ? getContainedSquares().get(index) : null;
	}
	
	/**
	 * Gets the minimum manhattan distance from this line to some square
	 * @param square The square to measure towards
	 * @return The distance
	 */
	public int getManhattanDistanceToSquare(Square square);
	
	/**
	 * Gets the next {@code Square} in the line
	 * @param square The current {@code Square}
	 * @return The next one or null if it's out of bounds
	 */
	public default Square getNextInLine(Square square) throws BadArgumentException {
		if (!containsSquare(square)) {
			throw new BadArgumentException(square, Square.class, "The square is not in the line");
		}
		return getSquare(getSpotInLine(square) + 1);
	}
	
	/**
	 * Gets the previous {@code Square} in the line
	 * @param square The current {@code Square}
	 * @return The previous one or null if it's out of bounds
	 */
	public default Square getPreviousInLine(Square square) throws BadArgumentException {
		if (!containsSquare(square)) {
			throw new BadArgumentException(square, Square.class, "The square is not in the line");
		}
		return getSquare(getSpotInLine(square) - 1);
	}
	
	/**
	 * Gets the {@code Line} that has the given index and is of the given type
	 * @param index The index to get
	 * @param type The subclass of the {@code Line}
	 * @return The {@code Line} with the given index
	 * @throws BadArgumentException If the index is not an index for a {@code Line}
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<? extends Line>> T getByIndex(int index, LineType type) throws BadArgumentException {
		try {
			return (T) type.getEnumValues()[index];
		} catch (ArrayIndexOutOfBoundsException e) {
			Line.indexOutOfRange(e, index, Integer.class);
			// We should never get here since indexOutOfRange throws an exception
			return null;
		}
	}
	
	/**
	 * Logs and prints the stacktrace if there's an error and throws an exception
	 * @param e The error
	 * @param index The argument causing the error
	 * @param type The type of the argument
	 * @throws BadArgumentException The exception to throw
	 */
	static void indexOutOfRange(Exception e, Object index, Class<?> type) throws BadArgumentException {
		// TODO log this
		e.printStackTrace();
		throw new BadArgumentException(index, type);
	}
}
