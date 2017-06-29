package representation;

import java.util.Set;

import moves.Move;

/**
 * Instances of this class are intended to hold the logic for calculating the legal moves for the appropriate type of {@code Board}.
 * @author matthewslesinski
 *
 * @param <B>
 */
public abstract class MoveGenerator<B extends Board> {

	public abstract Set<Move> calculateMoves(B board);
}