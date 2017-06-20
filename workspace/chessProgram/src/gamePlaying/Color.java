package gamePlaying;

import boardFeatures.Direction;
import boardFeatures.Rank;

/**
 * Holds a representation of the two possible colors in a game, white and black. 
 * @author matthewslesinski
 */
public enum Color {

	WHITE,
	BLACK;
	
	private final boolean value = this.ordinal() == 0 ? true : false;
	
	
	/**
	 * Determines if the color of the color is white, as opposed to black
	 * @return if the player is white
	 */
	public boolean isWhite() {
		return value;
	}
	
	/**
	 * Gets the other {@code Color} other than this one
	 * @return The {@code Color}
	 */
	public Color getOtherColor() {
		return this.isWhite() ? BLACK : WHITE;
	}
	
	/**
	 * Gets the {@code Direction} this color pushes pawns
	 * @return The {@code Direction}
	 */
	public Direction getPawnPushDirection() {
		return value ? Direction.UP : Direction.DOWN;
	}
	
	/**
	 * Gets the {@code Direction} this color captures with pawns on the left side
	 * @return The {@code Direction}
	 */
	public Direction getLeftPawnCaptureDirection() {
		return value ? Direction.UP_LEFT : Direction.DOWN_LEFT;
	}
	
	/**
	 * Gets the {@code Direction} this color captures with pawns on the right side
	 * @return The {@code Direction}
	 */
	public Direction getRightPawnCaptureDirection() {
		return value ? Direction.UP_RIGHT : Direction.DOWN_RIGHT;
	}
	
	/**
	 * Gets the rank that this color queens on.
	 * @return The {@code Rank}
	 */
	public Rank getQueeningRank() {
		return value ? Rank.ONE : Rank.EIGHT;
	}
	
	/**
	 * Gets the rank that this color performs en passant on
	 * @return The {@code Rank}
	 */
	public Rank getEnPassantRank() {
		return value ? Rank.FIVE : Rank.FOUR;
	}
	
	/**
	 * returns the {@code Color} enum when given a boolean for if it should be white
	 * @param isWhite If the {@code Color} should be the white or black color
	 * @return 
	 */
	public static Color getColor(boolean isWhite) {
		return isWhite ? WHITE : BLACK;
	}
	
	public String toString() {
		return value ? "white" : "black";
	}
}
