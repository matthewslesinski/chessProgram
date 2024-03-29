package lines;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import boardFeatures.Square;
import support.BadArgumentException;
import static support.UtilityFunctions.*;

public enum UpRightDiagonal implements Line {
	
	A8,
	A7_B8,
	A6_C8,
	A5_D8,
	A4_E8,
	A3_F8,
	A2_G8,
	A1_H8,
	B1_H7,
	C1_H6,
	D1_H5,
	E1_H4,
	F1_H3,
	G1_H2,
	H1;
	
	/** The ordinal of the long diagonal of this type */
	private static final int CENTER_INDEX = 7;
	
	/** The length of the longest diagonal of this type */
	private static final int MAX_LENGTH = 8;
	
	/** The set of squares contained in this {@code UpRightDiagonal} */	
	private List<Square> containedSquares;

	/** The set of squares contained in this {@code UpRightDiagonal}, but reversed */
	private List<Square> reverseContainedSquares;

	@Override
	public int getIndex() {
		return this.ordinal();
	}

	@Override
	public String getHumanReadableForm() {
		return this.getSquare((byte) 0).toString() + "-" +
				this.getSquare((byte) (this.getLength() - 1)).toString();
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
		return square.getUpRightDiagonal() == this;
	}
	
	@Override
	public int getManhattanDistanceToSquare(Square square) {
		return Math.abs(square.getUpRightDiagonal().getIndex() - this.getIndex());
	}
	
	/**
	 * Gets the diagonal that has the given index
	 * @param index The index to get
	 * @return The {@code UpRightDiagonal} with number one greater than the index
	 * @throws BadArgumentException If the index is not an index for a file
	 */
	public static UpRightDiagonal getByIndex(int index) throws BadArgumentException {
		return Line.getByIndex(index, type());
	}
	
	/**
	 * Returns the {@code UpRightDiagonal} containing a square
	 * @param square the {@code Square} to check
	 * @return the {@code UpRightDiagonal}
	 */
	public static UpRightDiagonal getBySquare(Square square) {
		int index = CENTER_INDEX + square.getFile().getIndex() - square.getRank().getIndex();
		return getByIndex(index);
	}
	
	/**
	 * Gets the list of squares that are in this diagonal
	 * @param index The index identifying the diagonal
	 * @return The list of {@code Square}
	 */
	private static List<Square> getListOfContainedSquares(int index) {
		// The length is 8 minus how far the index is from the center, which is at index 7
		int length = MAX_LENGTH - Math.abs(index - CENTER_INDEX);
		return Arrays.stream(getRange(0, length))
				.map(offset -> Square.getByFileAndRank(
						// As you go rightward along the diagonal, the file of the square rises proportionally
						// The start file is only either 0 or index minus the center diagonal index when the
						// diagonal has a higher index than the center
						File.getByIndex(offset + ((index / MAX_LENGTH) * (index - CENTER_INDEX))),
						// As you go rightward along the diagonal, the rank rises. The start rank is the center diagonal
						// index minus the index, when the index is less than the center's, or 0
						Rank.getByIndex((index / MAX_LENGTH - 1) * -1 * (CENTER_INDEX - index) + offset)))
				.collect(Collectors.toList());
	}
	
	/**
	 * Retrieves the {@code enum} describing which type of line this is
	 * @return The {@code LineType}
	 */
	public static LineType type() {
		return LineType.UP_RIGHT_DIAGONAL;
	}
	
	@Override
	public String toString() {
		return getHumanReadableForm();
	}

	/**
	 * Determines which squares are contained in the {@code UpRightDiagonal}s
	 */
	public static void setContainedSquares() {
		for (UpRightDiagonal diagonal : values()) {
			diagonal.containedSquares = getListOfContainedSquares(diagonal.getIndex());
			diagonal.reverseContainedSquares = reverseList(diagonal.containedSquares);
		}
	}
	
}
