package gamePlaying;

/**
 * Holds a representation of the two possible colors in a game, white and black. 
 * @author matthewslesinski
 */
public enum Color {

	WHITE(true),
	BLACK(false);
	
	private final boolean value;
	
	/**
	 * Private constructor for the enums
	 * @param value
	 */
	private Color(boolean value) {
		this.value = value;
	}
	
	/**
	 * Determines if the color of the color is white, as opposed to black
	 * @return if the player is white
	 */
	public boolean isWhite() {
		return value;
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
