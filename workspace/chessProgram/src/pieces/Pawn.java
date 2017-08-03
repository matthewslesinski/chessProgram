package pieces;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import boardFeatures.Square;
import gamePlaying.Color;
import moves.Move;
import representation.Board;

/**
 * Provides the utility method(s) for calculating a pawn's legal moves
 * @author matthewslesinski
 *
 */
public class Pawn extends PieceUtility {

	Pawn(PieceType piece) {
		super();
	}
	
	@Override
	public Set<Move> getLegalMoves(Square square, Board board, Color color) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected PieceType determinePieceType() {
		return PieceType.PAWN;
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
		// TODO
		return null;
	}
}
