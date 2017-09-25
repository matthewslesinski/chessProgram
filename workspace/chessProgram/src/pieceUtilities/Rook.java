package pieceUtilities;

import java.util.List;

import lines.Direction;
import pieces.PieceType;
import static support.Constants.*;

/**
 * Provides the utility method(s) for calculating a rook's legal moves
 * @author matthewslesinski
 *
 */
public class Rook extends LineMover {

	private static final PieceType TYPE = PieceType.ROOK;
	
	public Rook() {
		super();
	}

	@Override
	protected PieceType determinePieceType() {
		return TYPE;
	}

	@Override
	List<Direction> getMovementDirections() {
		return ROOK_DIRECTIONS;
	}

}
