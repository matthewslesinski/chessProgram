package pieceUtilities;

import java.util.List;

import boardFeatures.Direction;
import pieces.PieceType;
import support.Constants;

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
		return Constants.ROOK_DIRECTIONS;
	}

}
