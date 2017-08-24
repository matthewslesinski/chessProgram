package moves;

import java.util.Set;

import boardFeatures.Square;
import lines.File;
import moveCalculationStructures.KingMoveSet;
import representation.Board;

public class LazyPreProcessing<B extends Board> extends StraightforwardPreProcessing<B> {

	public LazyPreProcessing(B board) {
		super(board);
		// TODO
	}


	@Override
	protected void calculateKingAttackers(Set<Square> coveredAttackers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void calculateSquareAttackers(Square potentiallyAttackedSquare, KingMoveSet kingMoves,
			Set<Square> coveredAttackers, Set<Square> squaresToIgnore) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void calculateKingSafety() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isMovementBlocked(Square start, Square end) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnPassantPinned(File movingPawnFile) {
		// TODO Auto-generated method stub
		return false;
	}
}
