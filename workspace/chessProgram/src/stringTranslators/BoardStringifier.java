package stringTranslators;

import java.util.Arrays;

import boardFeatures.Square;
import lines.File;
import lines.Rank;
import moves.Move;
import pieces.Piece;
import representation.Board;
import support.Constants;
import support.UtilityFunctions;

public class BoardStringifier<B extends Board> {

	private static final String WHOSE_MOVE_TEXT = "Player to move: ";
	private static final String LAST_MOVE_TEXT = "The previous move: ";
	private final Board board;
	private final Piece[] pieces;
	
	StringBuilder builder = new StringBuilder();
	
	public BoardStringifier(B board) {
		this.board = board;
		pieces = board.toPieceArray();
	}
	
	/**
	 * Appends a dashed line
	 */
	private void addDashedLine() {
		builder.append(Constants.NEWLINE);
		builder.append(Constants.TRIPLE_SPACE);
		builder.append("-----------------------------");
	}
	
	/**
	 * Turns the given square into its string representation
	 * @param currentSquare
	 */
	private void stringifySquare(Square currentSquare) {
		Piece piece = currentSquare.getValueOfSquareInArray(pieces);
		if (currentSquare.isDarkSquare()) {
			builder.append(Constants.ESCAPE_CHARACTER);
			builder.append(Constants.ANSI_DARK_SQUARE);
		}
		builder.append(Constants.SINGLE_SPACE);
		builder.append(piece);
		builder.append(Constants.SINGLE_SPACE);
		if (currentSquare.isDarkSquare()) {
			builder.append(Constants.ESCAPE_CHARACTER);
			builder.append(Constants.ANSI_RESET_ATTRIBUTES);
		}
	}
	
	/**
	 * Adds the letter names for the coordinates for each file below the board
	 */
	private void addFileNames() {
		builder.append(Constants.DOUBLE_SPACE);
		for (File file : File.values()) {
			builder.append(Constants.DOUBLE_SPACE);
			builder.append(file);
		}
		builder.append(Constants.NEWLINE);
	}
	
	/**
	 * Shows the actual board
	 */
	private void stringifyBoard() {
		builder.append(Constants.NEWLINE);
		for (Rank rank : UtilityFunctions.reverseList(Arrays.asList(Rank.values()))) {
			builder.append(rank + Constants.DOUBLE_SPACE);
			for (File file : File.values()) {
				stringifySquare(Square.getByFileAndRank(file, rank));
			}
			builder.append(Constants.NEWLINE);
		}
		addFileNames();
	}
	
	
	/**
	 * Details which pieces have been captured for each side
	 */
	private void addCapturedPieces() {
		// TODO
		return;
	}
	
	/**
	 * Includes which player has the current turn
	 */
	private void addPlayerToMove() {
		builder.append(Constants.TRIPLE_SPACE);
		builder.append(WHOSE_MOVE_TEXT);
		builder.append(board.whoseMove());
		builder.append(Constants.NEWLINE);
	}
	
	/**
	 * Includes what the last move made was, and if there's currently a check on the board
	 */
	private void addLastMove() {
		Move last = board.lastMove();
		if (last != null) {
			builder.append(LAST_MOVE_TEXT);
			builder.append(last.getMoveAsString(true, true));
			if (board.isInCheck()) {
				if (board.isOver()) {
					builder.append(Constants.CHECKMATE_SYMBOL);
				} else {
					builder.append(Constants.CHECK_SYMBOL);
				}
			}
			builder.append(Constants.NEWLINE);
		}
	}
	
	/**
	 * Fills the {@code StringBuilder} with the string representation of the {@code Board} passed to this instance
	 * @return The {@code String} build by the {@code StringBuilder}
	 */
	public String stringify() {
		addDashedLine();
		stringifyBoard();
		addCapturedPieces();
		addPlayerToMove();
		addLastMove();
		addDashedLine();
		return builder.toString();
	}
}
