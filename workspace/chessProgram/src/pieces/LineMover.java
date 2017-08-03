package pieces;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import boardFeatures.Direction;
import boardFeatures.Square;
import gamePlaying.Color;
import representation.Board;
import support.UtilityFunctions;

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
	public Collection<Square> getPossibleSquaresToThreaten(Color color, Square fromSquare) {
		return UtilityFunctions.concat(getMovementDirections().stream()
				.map(direction -> fromSquare.getSquaresInDirection(direction))
				.collect(Collectors.toList()));
	}
	
	@Override
	protected List<Square> getSquaresToMoveTo(Square square, Board board, Color color) {
		// TODO Auto-generated method stub
		return null;
	}
}
