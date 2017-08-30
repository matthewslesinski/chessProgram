package deprecated;

import pieces.PieceType;

//This interface was written and then I realized it wasn't necessary. I'm still keeping it up in case it becomes useful
@Deprecated
public interface EnPassant extends Capture {

	
	@Override
	public default PieceType getCapturedPieceType() {
		return PieceType.PAWN;
	}
	
	@Override
	public default boolean isEnPassant() {
		return true;
	}
}
