package search;

import moves.Move;
import representation.Board;

/**
 * Implementors of this class determine the best move that should be made in a given position, according
 * to its algorith(s).
 * @author matthewslesinski
 *
 */
public interface AI {

	/**
	 * Determines which move should be made in a given position to maximize the likelihood that the position is winning for the player making the move
	 * @param board The position to evaluate the best move for
	 * @return The most likely best move
	 */
	public Move bestMove(Board board);
}
