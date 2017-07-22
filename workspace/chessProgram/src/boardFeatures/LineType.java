package boardFeatures;

import java.util.function.Function;

/**
 * An enum to keep track of the different types of classes that extends {@code Line}. This way, abstraction can be
 * implemented more easily
 * @author matthewslesinski
 *
 */
public enum LineType {

	FILE(File.class),
	RANK(Rank.class),
	UP_RIGHT_DIAGONAL(UpRightDiagonal.class),
	DOWN_RIGHT_DIAGONAL(DownRightDiagonal.class);
	
	// Must be done after all LineTypes have been initialized, since otherwise it needs to be called in the constructor
	// when checking to see which enum value 'this' is won't work because 'this' won't be initialized yet
	static {
		for (LineType type : values()) {
			type.determineCharacteristics();
		}
	}
	
	private final Class<? extends Enum<? extends Line>> type;
	private final Enum<? extends Line>[] valuesArray;
	private Function<Square, ? extends Enum<? extends Line>> getLineBySquare;
	private Direction forwardDirection;
	
	private LineType(Class<? extends Enum<? extends Line>> type) {
		this.type = type;
		this.valuesArray = type.getEnumConstants();
	}
	
	/**
	 * Returns which class this wraps
	 * @return The class
	 */
	public Class<? extends Enum<? extends Line>> getType() {
		return type;
	}
	
	/**
	 * The possible values for the type this corresponds to
	 * @return The array of enum values for the type of {@code Line}
	 */
	public Enum<? extends Line>[] getEnumValues() {
		return valuesArray;
	}
	
	/**
	 * Gets the {@code Line} of this type that contains the specified {@code Square}
	 * @param square The {@code Square} in this line
	 * @return The {@code Line} of this type containing that {@code Square}
	 */
	public Enum<? extends Line> getLineBySquare(Square square) {
		return getLineBySquare.apply(square);
	}
	
	/**
	 * Which {@code Direction} is forwards along this type of {@code Line}
	 * @return The {@code Direction} for forwards
	 */
	public Direction getForwardDirection() {
		return forwardDirection;
	}
	
	
	
	/**
	 * Convenience method for the sake of style to initialize various values for all the {@code LineTypes}
	 */
	private void determineCharacteristics() {
		switch (this) {
		case FILE:
			this.getLineBySquare = (square) -> square.getFile();
			this.forwardDirection = Direction.UP;
			break;
		case RANK:
			this.getLineBySquare = (square) -> square.getRank();
			this.forwardDirection = Direction.RIGHT;
			break;
		case UP_RIGHT_DIAGONAL:
			this.getLineBySquare = (square) -> square.getUpRightDiagonal();
			this.forwardDirection = Direction.UP_RIGHT;
			break;
		case DOWN_RIGHT_DIAGONAL:
			this.getLineBySquare = (square) -> square.getDownRightDiagonal();
			this.forwardDirection = Direction.DOWN_RIGHT;
			break;
		default:
			// wtf
			break;
		}
	}
}
