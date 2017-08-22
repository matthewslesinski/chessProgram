package moves;

import boardFeatures.Square;
import gamePlaying.Color;

/**
 * Specifies the types of moves that can be made
 * @author matthewslesinski
 *
 */
public enum MoveType {

	NORMAL,
	CAPTURE,
	CASTLE,
	PROMOTION,
	PROMOTION_WITH_CAPTURE,
	EN_PASSANT;
	
	/**
	 * Determines if a move to an end square on some board would be a capture or not
	 * @param end The goal of the movement
	 * @param board The board it would happen on
	 * @return If the move is normal or if it's a capture
	 */
	public static MoveType infer(Square end, ProcessedBoard<?> board) {
		return board.isEmptySquare(end) ? NORMAL : CAPTURE;
	}

	/**
	 * Similar to {@code infer}, this determines the type of move involving movement from a square to a square on a board.
	 * However, this also includes the possibilities of pawn-specific moves
	 * @param start The start of the movement
	 * @param end The end of the movement
	 * @param board The board it happens on
	 * @return The {@code MoveType}
	 */
	public static MoveType inferForPawns(Square start, Square end, ProcessedBoard<?> board) {
		Color toMove = board.whoseMove();
		if (end.getRank() == toMove.getQueeningRank()) {
			return board.isEmptySquare(end) ? PROMOTION : PROMOTION_WITH_CAPTURE;
		}
		// Remember, en passants have their end square as the enemy pawn's square
		if (end.getRank() == start.getRank()) {
			return EN_PASSANT;
		}
		return infer(end, board);
	}
	
	/**
	 * Similar to {@code infer}, this determines the type of move involving the specified motion, but also
	 * considers the possibility of castling
	 * @param start The start of the motion
	 * @param end The end of the motion
	 * @param board The board it happens on
	 * @return The {@code MoveType}
	 */
	public static MoveType inferForKings(Square start, Square end, ProcessedBoard<?> board) {
		if (Math.abs(start.getFile().getIndex() - end.getFile().getIndex()) > 1) {
			return CASTLE;
		}
		return infer(end, board);
	}
	
	
}
