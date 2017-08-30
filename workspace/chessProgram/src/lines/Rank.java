package lines;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import boardFeatures.Square;
import support.BadArgumentException;
import support.UtilityFunctions;

/**
 * Represents a horizontal row of squares on the board, of which there are 8
 * @author matthewslesinski
 */
public enum Rank implements Line {

	ONE("1"),
	TWO("2"),
	THREE("3"),
	FOUR("4"),
	FIVE("5"),
	SIX("6"),
	SEVEN("7"),
	EIGHT("8");
	
	/** The string form of the number of this rank */
	private String readableForm;
	
	/** The set of squares contained in this {@code Rank} */
	private List<Square> containedSquares;
	
	/** The set of squares contained in this {@code Rank}, but reversed */
	private List<Square> reverseContainedSquares;

	
	private Rank(String readableForm) {
		this.readableForm = readableForm;
	}
	
	@Override
	public int getIndex() {
		return this.ordinal();
	}
	
	@Override
	public String getHumanReadableForm() {
		return readableForm;
	}
	
	@Override
	public LineType getType() {
		return type();
	}
	
	@Override
	public List<Square> getContainedSquares() {
		return containedSquares;
	}
	
	@Override
	public List<Square> getReverseContainedSquares() {
		return reverseContainedSquares;
	}
	
	@Override
	public boolean containsSquare(Square square) {
		return square.getRank() == this;
	}
	
	@Override
	public int getManhattanDistanceToSquare(Square square) {
		return Math.abs(square.getRank().getIndex() - this.getIndex());
	}
	
	/**
	 * Gets the {@code Rank} with the given ordinal/index
	 * @param index The index to get
	 * @return The {@code Rank}
	 */
	public static Rank getByIndex(int index) {
		return Line.getByIndex(index, type());
	}
	
	/**
	 * Gets the rank that has the given number
	 * @param readableForm The number
	 * @return The rank with the given number or null if the input is null
	 * @throws BadArgumentException If the string is not a rank
	 */
	public static Rank getByHumanReadableForm(String readableForm) throws BadArgumentException {
		if (readableForm == null) {
			return null;
		}
		int numericalValue = -1;
		try {
			numericalValue = Integer.parseInt(readableForm);
			return getByIndex(numericalValue - 1);
		} catch (NumberFormatException e) {
			Line.indexOutOfRange(e, readableForm, String.class);
			// Should never get here because indexOutOfRange always throws an exception
			return null;
		}
	}
	
	/**
	 * Retrieves the {@code enum} describing which type of line this is
	 * @return The {@code LineType}
	 */
	public static LineType type() {
		return LineType.RANK;
	}
	
	@Override
	public String toString() {
		return getHumanReadableForm();
	}
    
	/**
	 * Determines which squares are contained in the {@code Rank}s
	 */
	public static void setContainedSquares() {
		for (Rank rank : values()) {
			rank.containedSquares = Arrays.stream(UtilityFunctions.getRange(0, 8))
					.map(UtilityFunctions.bindAtEnd(Square::getByFileAndRankIndices, rank.getIndex()))
					.collect(Collectors.toList());
			rank.reverseContainedSquares = UtilityFunctions.reverseList(rank.containedSquares);
		}
	}
}
