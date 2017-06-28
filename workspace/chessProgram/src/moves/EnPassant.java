package moves;

import pieces.PieceType;

//This interface was written and then I realized it wasn't necessary. I'm still keeping it up in case it becomes useful
public interface EnPassant extends Capture {

	
	@Override
	public default PieceType getCapturedPiece() {
		return PieceType.PAWN;
	}
	
	@Override
	public default boolean isEnPassant() {
		return true;
	}
}
