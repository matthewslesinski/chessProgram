package pieces;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import boardFeatures.Direction;
import boardFeatures.Square;
import gamePlaying.Color;
import moves.Move;
import representation.Board;

/**
 * Provides the utility method(s) for calculating a king's legal moves
 * @author matthewslesinski
 *
 */
public class King extends PieceUtility {

	
	King(PieceType piece) {
		super();
	}
	
	@Override
	public Set<Move> getLegalMoves(Square square, Board board, Color color) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected PieceType determinePieceType() {
		return PieceType.KING;
	}

	@Override
	protected int determineMaxAttackDistance() {
		return 2;
	}

	@Override
	protected List<Square> getSquaresToMoveTo(Square square, Board board, Color color) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Square> getPossibleSquaresToThreaten(Color color, Square fromSquare) {
		return Arrays.asList(Direction.values()).stream()
				.map(direction -> fromSquare.getNeighbor(direction)).filter(square -> square != null)
				.collect(Collectors.toList());
	}

}
