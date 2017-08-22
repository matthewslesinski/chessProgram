package pieceUtilities;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import boardFeatures.Square;
import lines.Direction;
import moveCalculationStructures.OutwardLinePortions;
import moves.ProcessedBoard;
import pieces.Piece;
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
	protected Collection<Direction> getDirectionsToMoveIn() {
		return getMovementDirections();
	}
	
	@Override
	public Collection<Square> calculatePossibleSquaresToThreaten(Square fromSquare) {
		return UtilityFunctions.concat(getMovementDirections().stream()
				.map(direction -> fromSquare.getSquaresInDirection(direction))
				.collect(Collectors.toList()));
	}
	
	@Override
	public BiFunction<Function<Square, Piece>, Square, Square> getThreatsInCluster(Set<Square> relevantSquares,
			Square perspective, Collection<Square> possibleThreats) {
		List<Square> squaresToConsider = possibleThreats.stream().filter(square -> relevantSquares.contains(square)).collect(Collectors.toList());
		OutwardLinePortions organizedSquares = new OutwardLinePortions(perspective, squaresToConsider);
		return (occupants, currentSquare) -> organizedSquares.getNext(currentSquare, occupants);
	}
	
	@Override
	protected boolean isMovementAllowed(Square start, Square end, ProcessedBoard<?> board) {
		return !board.isMovementBlocked(start, end);
	}
	
	@Override
	protected boolean addSquareToListOfMoves(Square start, Square end, ProcessedBoard<?> board, List<Square> list) {
		boolean shouldBreak = false;
		Piece occupant = board.getPieceAtSquare(end);
		if (occupant != null && occupant != Piece.NONE) {
			shouldBreak = true;
			if (occupant.getColor() != board.whoseMove()) {
				list.add(end);
			}
		} else {
			list.add(end);
		}
		return shouldBreak;
	}
}
