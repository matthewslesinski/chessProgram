package deprecated;

import pieces.PieceType;

//This interface was written and then I realized it wasn't necessary. I'm still keeping it up in case it becomes useful
@Deprecated
public interface CapturingPromotion extends Capture {

	
	@Override
	public default PieceType getMovingPiece() {
		return PieceType.PAWN;
	}

	@Override
	public default boolean isPromotion() {
		return true;
	}
}
