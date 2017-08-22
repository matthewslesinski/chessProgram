package pieces;

import java.util.List;

import boardFeatures.Direction;
import support.Constants;

/**
 * Provides the utility method(s) for calculating a bishop's legal moves
 * @author matthewslesinski
 *
 */
public class Bishop extends LineMover {

	private static final PieceType TYPE = PieceType.BISHOP;
	
	Bishop() {
		super();
	}

	@Override
	protected PieceType determinePieceType() {
		return TYPE;
	}
	
	@Override
	List<Direction> getMovementDirections() {
		return Constants.BISHOP_DIRECTIONS;
	}
}
