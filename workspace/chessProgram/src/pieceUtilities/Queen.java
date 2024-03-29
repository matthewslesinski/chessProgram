package pieceUtilities;

import java.util.List;

import lines.Direction;
import pieces.PieceType;
import static support.Constants.*;

/**
 * Provides the utility method(s) for calculating a queen's legal moves
 * @author matthewslesinski
 *
 */
public class Queen extends LineMover {

	private static final PieceType TYPE = PieceType.QUEEN;
	
	public Queen() {
		super();
	}

	@Override
	protected PieceType determinePieceType() {
		return TYPE;
	}

	@Override
	List<Direction> getMovementDirections() {
		return QUEEN_DIRECTIONS;
	}
}
