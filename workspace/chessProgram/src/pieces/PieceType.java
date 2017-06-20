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
	
	
	
	
	@Override
	public String toString() {
		return this.readableForm;
	}
}
