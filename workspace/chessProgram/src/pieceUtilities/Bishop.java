package pieceUtilities;

import java.util.List;

import lines.Direction;
import pieces.PieceType;
import static support.Constants.*;

/**
 * Provides the utility method(s) for calculating a bishop's legal moves
 * @author matthewslesinski
 *
 */
public class Bishop extends LineMover {

	private static final PieceType TYPE = PieceType.BISHOP;
	
	public Bishop() {
		super();
	}

	@Override
	protected PieceType determinePieceType() {
		return TYPE;
	}
	
	@Override
	List<Direction> getMovementDirections() {
		return BISHOP_DIRECTIONS;
	}
}
