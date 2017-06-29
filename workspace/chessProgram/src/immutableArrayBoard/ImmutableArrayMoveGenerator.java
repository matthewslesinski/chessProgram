package immutableArrayBoard;

import java.util.Set;

import moves.DecompressedBoard;
import moves.Move;
import representation.MoveGenerator;

/**
 * Calculates the moves for a given position. This is a mutable object, and 
 * @author matthewslesinski
 *
 */
public class ImmutableArrayMoveGenerator extends MoveGenerator<ImmutableArrayBoard> {
	
	private DecompressedBoard<ImmutableArrayBoard> preprocessing;
	
	@Override
	public Set<Move> calculateMoves(ImmutableArrayBoard board) {
		clear();
		preprocessing = new DecompressedBoard<>(board);
		// TODO
		return null;
	}
	
	/**
	 * Resets the MoveGenerator to its original state
	 */
	// TODO finish this
	private void clear() {
		
	}

}