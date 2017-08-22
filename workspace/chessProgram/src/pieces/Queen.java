package pieces;

import java.util.List;

import boardFeatures.Direction;
import support.Constants;

/**
 * Provides the utility method(s) for calculating a queen's legal moves
 * @author matthewslesinski
 *
 */
public class Queen extends LineMover {

	private static final PieceType TYPE = PieceType.QUEEN;
	
	Queen() {
		super();
	}

	@Override
	protected PieceType determinePieceType() {
		return TYPE;
	}

	@Override
	List<Direction> getMovementDirections() {
		return Constants.QUEEN_DIRECTIONS;
	}
}
