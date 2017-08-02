package pieces;

import java.util.List;

import boardFeatures.Direction;
import boardFeatures.Square;
import gamePlaying.Color;
import representation.Board;

/**
 * The utility super class for rooks, bishops, and queens, since they all move in a straight line for a possibly far distance
 * @author matthewslesinski
 *
 */
public abstract class LineMover extends PieceUtility {

	
	protected LineMover() {
		super();
	}
	
	/**
	 * Retrieves the directions this piece can move in
	 * @return The list of directions
	 */
	abstract List<Direction> getMovementDirections();
	
	
	@Override
	protected List<Square> getSquaresToMoveTo(Square square, Board board, Color color) {
		// TODO Auto-generated method stub
		return null;
	}
}
