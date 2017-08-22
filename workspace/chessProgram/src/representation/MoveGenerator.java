package representation;

import java.util.Set;

import moves.Move;

/**
 * Instances of this class are intended to hold the logic for calculating the legal moves for the appropriate type of {@code Board}.
 * @author matthewslesinski
 *
 * @param <B> The type of Board that this generates moves for
 */
public abstract class MoveGenerator<B extends Board> {

	/**
	 * Calculates the legal moves for a given position
	 * @param board The board to calculate moves for
	 * @return The {@code Set} of legal {@code Move}s
	 */
	public abstract Set<Move> calculateMoves(B board);
	
	/**
	 * Determines if the board most recently used for calculating moves by this generator is in check
	 * @return true iff it is
	 */
	public abstract boolean isInCheck();
}
