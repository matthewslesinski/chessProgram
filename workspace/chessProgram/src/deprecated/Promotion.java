package deprecated;

import moves.Move;
import pieces.PieceType;

//This interface was written and then I realized it wasn't necessary. I'm still keeping it up in case it becomes useful
@Deprecated
public interface Promotion extends Move {

		
	@Override
	public default PieceType getMovingPieceType() {
		return PieceType.PAWN;
	}

	@Override
	public default boolean isPromotion() {
		return true;
	}

}
