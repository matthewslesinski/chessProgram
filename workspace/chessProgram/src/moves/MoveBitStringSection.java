package moves;


/**
 * Represents how information about a {@code Move} is stored in an {@code int} as a form of compression. Each enum value has a startBit and a length.
 * The startBit is what 0 indexed from the right bit the piece of info is stored at. The length is how many bits are used to
 * store that info. 
 * @author matthewslesinski
 *
 */
public enum MoveBitStringSection {
	MOVING_PIECE(0, 3),
	IS_CAPTURE(3, 1),
	CAPTURE_PIECE(4, 3),
	START_SQUARE(7, 6),
	END_SQUARE(13, 6),
	IS_CASTLE(19, 1),
	IS_PROMOTION(20, 1),
	PROMOTION_TYPE(21, 2),
	IS_EN_PASSANT(23, 1),
	COLOR(24, 1)
	;
	
	private final int startBit;
	private final int lengthMask;
	
	private MoveBitStringSection(int startBit, int length) {
		this.startBit = startBit;
		this.lengthMask = ~0 >>> (32 - length);
	}
	
	/**
	 * Given an int that represents a move, this sets a value on that int in the place specified by this enum instance
	 * @param receptacle The int to set the value on. It is assumed the place specified by this enum instance is already zeroized
	 * @param value The value to set
	 * @return The int with the value set
	 */
	public int setValue(int receptacle, int value) {
		int unshiftedValue = lengthMask & value;
		return receptacle | (unshiftedValue << startBit);
	}
	
	/**
	 * Gets the {@code length} long value at the {@code startBit} for this instance
	 * @param receptacle The int to get the value from
	 * @return The value
	 */
	public int getValue(int receptacle) {
		return (receptacle >>> startBit) & lengthMask;
	}
}