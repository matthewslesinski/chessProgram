package boardFeatures;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import support.BadArgumentException;
import support.UtilityFunctions;

/**
 * Represents a vertical column of squares on the board, of which there are 8
 * @author matthewslesinski
 */
public enum File implements Line{

	A("a"),
	B("b"),
	C("c"),
	D("d"),
	E("e"),
	F("f"),
	G("g"),
	H("h");
	
	private String readableForm;
	private static final Direction directionOfLine = Direction.UP;

	
	private File(String readableForm) {
		this.readableForm = readableForm;
	}
	
	/**
	 * The set of squares contained in this {@code File}
	 */
	private final List<Square> containedSquares = Arrays.asList(Rank.values()).stream()
			.map(rank -> Square.getByFileAndRank(this, rank))
			.collect(Collectors.toList());
	
	/**
	 * The set of squares contained in this {@code File}, but reversed
	 */
	private final List<Square> reverseContainedSquares = UtilityFunctions.reverseList(containedSquares);
	

	
	@Override
	public int getIndex() {
		return this.ordinal();
	}
	
	@Override
	public String getHumanReadableForm() {
		return readableForm;
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
		return square.getFile() == this;
	}

	@Override
	public int getSpotInLine(Square square) {
		return square.getRank().getIndex();
	}
	
	@Override
	public Direction getForwardDirection() {
		return directionOfLine;
	}
	
	/**
	 * Gets the file that has the given index
	 * @param index The index to get
	 * @return The file with number one greater than the index
	 * @throws BadArgumentException If the index is not an index for a file
	 */
	public static File getByIndex(int index) throws BadArgumentException {
		return Line.getByIndex(index, File.class);
	}
	
	/**
	 * Gets the file that has the given letter
	 * @param readableForm The letter
	 * @return The file with the given letter
	 * @throws BadArgumentException If the string is not a file
	 */
	public static File getByHumanReadableForm(String readableForm) throws BadArgumentException {
		int numericalValue = -1;
		try {
			numericalValue = readableForm.charAt(0) - 96;
			return getByIndex(numericalValue - 1);
		} catch (NumberFormatException e) {
			Line.indexOutOfRange(e, readableForm, String.class);
			return null;
		}
	}
	
	@Override
	public String toString() {
		return getHumanReadableForm();
	}
    
}
