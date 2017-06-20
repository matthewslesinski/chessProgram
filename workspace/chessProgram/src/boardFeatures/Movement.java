package boardFeatures;

import java.util.List;

import support.UtilityFunctions;

public enum Movement {
	BACKWARDS,
	NOWHERE,
	FORWARDS;
	
	private final int increment = this.ordinal() - 1;
	
	/**
	 * Retrieves the increment for this movement
	 */
	public int getIncrement() {
		return increment;
	}
	
	/**
	 * Returns the list of squares that lie either in front of or behind this square, in the line of the provided type
	 * @param square The base square
	 * @param lineType The type of line 
	 * @return
	 */
	public List<Square> getSquaresToMoveThrough(Square square, Class<? extends Line> lineType) {
		switch (increment) {
		case -1:
			return UtilityFunctions.getLineBySquareAndClass(square, lineType).getSquaresBehind(square);
		case 1:
			return UtilityFunctions.getLineBySquareAndClass(square, lineType).getSquaresInFront(square);
		default:
			return null;
		}
	}
}
