package support;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import representation.Board;
import stringUtilities.MoveWriter;

/**
 * An instance of {@code BadArgumentException} that specifically wraps a {@code Board}. It also allows the capability to print
 * out the whole game's worth of boards leading up to the current position
 * @author matthewslesinski
 *
 */
public class BadBoardException extends BadArgumentException {

	private static final long serialVersionUID = -1306867972968320193L;

	public BadBoardException(Board problemBoard, String message) {
		super(problemBoard, Board.class, message);
	}
	
	/**
	 * Prints (to System.out, which is problematic down the road) the list of boards from the beginning of the game up to the current one,
	 * as well as the moves made and the list of possible moves for each transition
	 */
	public void printBoardTrace() {
		// TODO log this as with other errors
		printStackTrace();
		Board problemBoard = (Board) getArgument();
		List<Board> trace = new LinkedList<>();
		do {
			trace.add(problemBoard);
		} while ((problemBoard = problemBoard.getPreviousPosition()) != null);
		Collections.reverse(trace);
		trace.forEach(board -> {
			if (board.lastMove() != null) {
				System.out.println("Last move: " + MoveWriter.getMoveAsStringInContext(board.lastMove(), board.getPreviousPosition()));
				System.out.println("Out of: " + board.getPreviousPosition().getLegalMoves());
			}
			System.out.println(board.basicToString());
		});
	}

}
