package pieces;

public enum PieceType {

	PAWN("pawn"),
	KNIGHT("knight"),
	BISHOP("bishop"),
	ROOK("rook"),
	QUEEN("queen"),
	KING("king");
	
	private final String readableForm;
	
	private PieceType(String readableForm) {
		this.readableForm = readableForm;
	}
	
	
	/**
	 * Gets the piece type given by the index
	 * @param index The index to get
	 * @return The {@code PieceType} at that index in the array of {@code PieceType}s
	 */
	public static PieceType getByIndex(int index) {
		return values()[index];
	}
	
	
	@Override
	public String toString() {
		return this.readableForm;
	}
}
