package evaluation;

import representation.Board;

/**
 * Implementors of this interface have some implementation allowing them to come up with an estimate of who is winning in a position.
 * The implementation can be expected to assume that the game is still going on, so if the position is checkmate/stalemate, an implementor
 * of this interface should not be called to evaluate the position.
 * @author matthewslesinski
 *
 */
public interface Evaluator {

	/**
	 * Calculates an estimate of who is winning in a position, assuming the position is not the end of the game.
	 * @param board The board to calculate the evaluation for
	 * @return A double that has a larger magnitude for greater confidence in one side winning, and where a positive number indicates
	 * white is winning, a negative number indicates black is winning, and 0 indicates equality.
	 */
	public double evaluateBoard(Board board);
}
